package com.cyx.cinema.platform.server.task;

import com.cyx.cinema.platform.command.Command;
import com.cyx.cinema.platform.entity.*;
import com.cyx.cinema.platform.message.Message;
import com.cyx.cinema.platform.server.util.FileUtil;
import com.cyx.cinema.platform.server.util.FilmPlanValidator;
import com.cyx.cinema.platform.util.IdGenerator;
import com.cyx.cinema.platform.util.SocketUtil;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 消息处理任务
 */
public class MessageProcessTask implements Runnable{

    private Socket socket;

    public MessageProcessTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Message msg = SocketUtil.receiveMsg(socket);
        System.out.println(msg);
        switch (msg.getCommand()){
            case Command.REGISTER://注册
                processRegister(msg);
                break;
            case Command.VIEW_ENTRY_APPLY://查看入驻申请
                processViewEntryApply(msg);
                break;
            case Command.LOGIN://登录
                processLogin(msg);
                break;
            case Command.GET_PASSWORD_BACK://找回密码
                processGetPasswordBack(msg);
                break;
            case Command.UNFROZEN_APPLY://解冻申请
                processUnfrozenApply(msg);
                break;
            case Command.VIEW_SELLERS://查看商家
                processViewSeller(msg);
                break;
            case Command.AUDIT_SELLER://审核商家
                processAuditSeller(msg);
                break;
            case Command.FROZEN_SELLER://冻结商家
                processFrozenSeller(msg);
                break;
            case Command.VIEW_USERS://查看用户
                processViewUsers();
                break;
            case Command.FROZEN_USER: //冻结用户
                processFrozenUser(msg);
                break;
            case Command.VIEW_UNFROZEN_APPLIES://查看解冻申请
                processViewUnfrozenApplies();
                break;
            case Command.AUDIT_UNFROZEN_APPLY://审批解冻申请
                processAuditUnfrozenApply(msg);
                break;
            case Command.ADD_FILM://添加影片
                processAddFilm(msg);
                break;
            case Command.UPDATE_FILM://更新影片
                processUpdateFilm(msg);
                break;
            case Command.DELETE_FILM://删除影片
                processDeleteFilm(msg);
                break;
            case Command.VIEW_FILMS://查看影片
                processViewFilms(msg);
                break;
            case Command.ADD_FILM_HALL://添加影厅
                processAddFilmHall(msg);
                break;
            case Command.UPDATE_FILM_HALL://更新影厅
                processUpdateFilmHall(msg);
                break;
            case Command.DELETE_FILM_HALL://删除影厅
                processDeleteFilmHall(msg);
                break;
            case Command.VIEW_FILM_HALLS://查看影厅
                processViewFilmHalls(msg);
                break;
            case Command.ADD_FILM_PLAN://添加影片计划
                processAddFilmPlan(msg);
                break;
            case Command.UPDATE_FILM_PLAN://更新影片计划
                processUpdateFilmPlan(msg);
                break;
            case Command.DELETE_FILM_PLAN://删除影片计划
                processDeleteFilmPlan(msg);
                break;
            case Command.VIEW_FILM_PLANS://查看影片计划
                processViewFilmPlans(msg);
                break;
            case Command.ORDER_SEAT_ONLINE://在线订座
                processOrderSeatOnline(msg);
                break;
            case Command.GET_FILM_PLAN_BY_ID://通过影片计划编号查找影片计划
                processGetFilmPlanById(msg);
                break;
            case Command.VIEW_ORDERS://查看订单
                processViewOrders(msg);
                break;
            case Command.UPDATE_ORDER://修改订单
                processUpdateOrder(msg);
                break;
            case Command.GET_ORDER_BY_ID://通过订单编号获取订单
                processGetOrderById(msg);
                break;
            case Command.CANCEL_ORDER://取消订单
                processCancelOrder(msg);
                break;
        }
    }

    /**
     * 处理取消订单
     * @param msg
     */
    private void processCancelOrder(Message msg) {
        Map<String,Object> data = (Map<String, Object>) msg.getData();
        String id = (String) data.get("id");
        String owner = (String) data.get("owner");
        List<Order> orders = FileUtil.readData(FileUtil.ORDER_FILE);
        Order order = null;
        for(int i=0; i<orders.size(); i++){
            Order o = orders.get(i);
            if(o.getId().equals(id) && o.getUser().equals(owner)){
                order = o;
                break;
            }
        }
        if(order == null){
            SocketUtil.sendMsg(socket, -1);
        } else {
            int state = order.getState();
            if(state == 0){//订单已取消
                SocketUtil.sendMsg(socket, -2);
            } else {
                List<FilmPlan> plans = FileUtil.readData(FileUtil.FILM_PLAN_FILE);
                FilmPlan plan = null;
                for(int i=0; i<plans.size(); i++){
                    FilmPlan fp = plans.get(i);
                    if(fp.getId().equals(order.getFilmPlanId())){
                        plan = fp;
                        break;
                    }
                }
                if(plan == null){//影片计划不存在
                    SocketUtil.sendMsg(socket, -1);
                } else {
                    FilmHall hall = plan.getHall();
                    hall.cancelSeat(order.getRow(), order.getCol());
                    boolean success = FileUtil.saveData(plans, FileUtil.FILM_PLAN_FILE);
                    if(success){
                        order.setState(0);//订单状态更改为取消
                        success = FileUtil.saveData(orders, FileUtil.ORDER_FILE);
                        SocketUtil.sendMsg(socket, success ? 1 : 0);
                    } else {
                        SocketUtil.sendMsg(socket, -1);
                    }
                }
            }
        }
    }

    /**
     * 处理通过订单编号获取订单
     * @param msg
     */
    private void processGetOrderById(Message msg) {
        String id = (String) msg.getData();
        List<Order> orders = FileUtil.readData(FileUtil.ORDER_FILE);
        Optional<Order> opt = orders.stream().filter(o->o.getId().equals(id)).findFirst();
        if(opt.isPresent()){
            SocketUtil.sendMsg(socket, opt.get());
        } else {
            SocketUtil.sendMsg(socket, null);
        }
    }

    /**
     * 处理修改订单
     * @param msg
     */
    private void processUpdateOrder(Message msg) {
        Map<String,Object> data = (Map<String, Object>) msg.getData();
        String id = (String) data.get("id");
        List<Order> orders = FileUtil.readData(FileUtil.ORDER_FILE);
        Order order = null;
        for(int i=0; i<orders.size(); i++){
            Order o= orders.get(i);
            if(o.getId().equals(id)){
                order = o;
                break;
            }
        }
        if(order == null){
            SocketUtil.sendMsg(socket, 0);
        } else {
            List<FilmPlan> plans = FileUtil.readData(FileUtil.FILM_PLAN_FILE);
            FilmPlan plan = null;
            for(int i=0; i<plans.size(); i++){
                FilmPlan fp = plans.get(i);
                if(fp.getId().equals(order.getFilmPlanId())){
                    plan = fp;
                    break;
                }
            }
            if(plan == null){
                SocketUtil.sendMsg(socket, 0);
            } else {
                FilmHall hall = plan.getHall();
                int originalRow = order.getRow();
                int originalCol = order.getCol();
                int row = (int) data.get("row");
                int col = (int) data.get("col");
                hall.changeSeat(originalRow, originalCol, row, col);
                boolean success = FileUtil.saveData(plans, FileUtil.FILM_PLAN_FILE);
                if(success){
                    order.setRow(row);
                    order.setCol(col);
                    success = FileUtil.saveData(orders, FileUtil.ORDER_FILE);
                }
                SocketUtil.sendMsg(socket, success ? 1 : 0);
            }
        }
    }

    /**
     * 处理查看订单
     * @param msg
     */
    private void processViewOrders(Message msg) {
        User user = (User) msg.getData();
        List<Order> orders = FileUtil.readData(FileUtil.ORDER_FILE);
        List<Order> result = orders.stream().filter(o->{
            if(user instanceof Seller){//商家
                return o.getSeller().getUsername().equals(user.getUsername());
            } else {//普通用户
                return o.getUser().equals(user.getUsername());
            }
        }).collect(Collectors.toList());
        SocketUtil.sendMsg(socket, result);
    }

    /**
     * 处理通过影片计划编号查找影片计划
     * @param msg
     */
    private void processGetFilmPlanById(Message msg) {
        String id = (String) msg.getData();
        List<FilmPlan> plans = FileUtil.readData(FileUtil.FILM_PLAN_FILE);
        Optional<FilmPlan> opt = plans.stream().filter(fp->fp.getId().equals(id)).findFirst();
        if(opt.isPresent()){
            SocketUtil.sendMsg(socket, opt.get());
        } else {
            SocketUtil.sendMsg(socket, null);
        }
    }

    /**
     * 处理在线订座
     * @param msg
     */
    private void processOrderSeatOnline(Message msg) {
        Map<String,Object> data = (Map<String, Object>) msg.getData();
        String id = (String) data.get("id");
        List<FilmPlan> plans = FileUtil.readData(FileUtil.FILM_PLAN_FILE);
        FilmPlan plan = null;
        for(int i=0; i<plans.size(); i++){
            FilmPlan fp = plans.get(i);
            if(fp.getId().equals(id)){
                plan = fp;
                break;
            }
        }
        if(plan == null){
            SocketUtil.sendMsg(socket, 0);
        } else {
            int row = (int) data.get("row");
            int col = (int) data.get("col");
            String owner = (String) data.get("owner");
            FilmHall hall = plan.getHall();
            hall.orderSeat(row, col, owner);
            Film film = plan.getFilm();
            Order order = new Order();
            order.setId(IdGenerator.generateId(10));
            order.setFilmPlanId(id);
            order.setFilmName(film.getName());
            order.setFilmHallName(hall.getName());
            order.setPlayDate(plan.getPlayDate());
            order.setBeginTime(plan.getBeginTime());
            order.setEndTime(plan.getEndTime());
            order.setRow(row);
            order.setCol(col);
            order.setUser(owner);
            order.setSeller(plan.getOwner());
            List<Order> orders = FileUtil.readData(FileUtil.ORDER_FILE);
            orders.add(order);
            boolean success = FileUtil.saveData(orders,FileUtil.ORDER_FILE);
            if(success){
                success = FileUtil.saveData(plans, FileUtil.FILM_PLAN_FILE);
            }
            SocketUtil.sendMsg(socket, success ? 1 : 0);
        }
    }

    /**
     * 处理查看影片计划
     * @param msg
     */
    private void processViewFilmPlans(Message msg) {
        Map<String,Object> data = (Map<String, Object>) msg.getData();
        String filmName = (String) data.get("filmName");
        String owner = (String) data.get("owner");
        List<FilmPlan> plans = FileUtil.readData(FileUtil.FILM_PLAN_FILE);
        List<FilmPlan> result = plans.stream().filter(fp->{
            boolean matchFilmName = "y".equals(filmName) || fp.getFilm().getName().equals(filmName);
            boolean matchUser = owner == null || owner.equals(fp.getOwner().getUsername());
            return matchFilmName && matchUser;
        }).collect(Collectors.toList());
        SocketUtil.sendMsg(socket, result);
    }
    /**
     * 处理删除影片计划
     * @param msg
     */
    private void processDeleteFilmPlan(Message msg) {
        Map<String,Object> data = (Map<String, Object>) msg.getData();
        String id = (String) data.get("id");
        String owner = (String) data.get("owner");
        List<FilmPlan> plans = FileUtil.readData(FileUtil.FILM_PLAN_FILE);
        int index = -1;
        for(int i=0; i<plans.size(); i++){
            FilmPlan fp = plans.get(i);
            if(fp.getId().equals(id) && fp.getOwner().getUsername().equals(owner)){
                index = i;
                break;
            }
        }
        if(index == -1){
            SocketUtil.sendMsg(socket, -1);
        } else {
            plans.remove(index);
            boolean success = FileUtil.saveData(plans, FileUtil.FILM_PLAN_FILE);
            SocketUtil.sendMsg(socket, success ? 1 : 0);
        }
    }
    /**
     * 处理修改影片计划
     * @param msg
     */
    private void processUpdateFilmPlan(Message msg) {
        Map<String,Object> data = (Map<String, Object>) msg.getData();
        String id = (String) data.get("id");
        String date = (String) data.get("date");
        String beginTime = (String) data.get("beginTime");
        String endTime = (String) data.get("endTime");
        String owner = (String) data.get("owner");
        List<FilmPlan> plans = FileUtil.readData(FileUtil.FILM_PLAN_FILE);
        int index = -1;
        for(int i=0; i<plans.size(); i++){
            FilmPlan fp = plans.get(i);
            if(id.equals(fp.getId()) && owner.equals(fp.getOwner().getUsername())){
                index = i;
                break;
            }
        }
        if(index == -1){//没有找到
            SocketUtil.sendMsg(socket, -1);
        } else {
            FilmPlan updatePlan = plans.remove(index);
            updatePlan.setPlayDate(date);
            updatePlan.setBeginTime(beginTime);
            updatePlan.setEndTime(endTime);
            boolean conflict = plans.stream().anyMatch(fp->FilmPlanValidator.isConflictPlan(fp, updatePlan));
            if(conflict){//时间冲突
                SocketUtil.sendMsg(socket, -2);
            } else {
                plans.add(index, updatePlan);
                boolean success = FileUtil.saveData(plans, FileUtil.FILM_PLAN_FILE);
                SocketUtil.sendMsg(socket, success ? 1 : 0);
            }
        }
    }

    /**
     * 处理添加影片计划
     * @param msg
     */
    private void processAddFilmPlan(Message msg) {
        FilmPlan plan = (FilmPlan) msg.getData();
        List<FilmPlan> plans = FileUtil.readData(FileUtil.FILM_PLAN_FILE);
        boolean conflict = plans.stream().anyMatch(fp -> FilmPlanValidator.isConflictPlan(fp, plan));
        if(conflict){//影片计划存在时间冲突
            SocketUtil.sendMsg(socket, -1);
        } else {
            plan.setId(IdGenerator.generateId(10));
            plans.add(plan);
            boolean success = FileUtil.saveData(plans, FileUtil.FILM_PLAN_FILE);
            SocketUtil.sendMsg(socket, success ? 1 : 0);
        }
    }

    /**
     * 处理查看影厅
     * @param msg
     */
    private void processViewFilmHalls(Message msg) {
        String owner = (String) msg.getData();
        List<FilmHall> halls = FileUtil.readData(FileUtil.FILM_HALL_FILE);
        List<FilmHall> result = halls.stream().filter(h->h.getOwner().equals(owner)).collect(Collectors.toList());
        SocketUtil.sendMsg(socket, result);
    }
    /**
     * 处理删除影厅
     * @param msg
     */
    private void processDeleteFilmHall(Message msg) {
        Map<String,Object> data = (Map<String, Object>) msg.getData();
        String id = (String) data.get("id");
        String owner = (String) data.get("owner");
        List<FilmHall> halls = FileUtil.readData(FileUtil.FILM_HALL_FILE);
        int index = - 1;
        for(int i=0; i<halls.size(); i++){
            FilmHall hall = halls.get(i);
            if(hall.getId().equals(id) && hall.getOwner().equals(owner)){
                index = i;
                break;
            }
        }
        if(index == -1){
            SocketUtil.sendMsg(socket, -1);
        } else {
            halls.remove(index);
            boolean success = FileUtil.saveData(halls, FileUtil.FILM_HALL_FILE);
            SocketUtil.sendMsg(socket, success ? 1 : 0);
        }
    }
    /**
     * 处理修改影厅
     * @param msg
     */
    private void processUpdateFilmHall(Message msg) {
        FilmHall hall = (FilmHall) msg.getData();
        List<FilmHall> halls = FileUtil.readData(FileUtil.FILM_HALL_FILE);
        int index = -1;
        for(int i=0; i<halls.size(); i++){
            FilmHall fh = halls.get(i);
            if(hall.getId().equals(fh.getId()) && hall.getOwner().equals(fh.getOwner())){
                index = i;
                break;
            }
        }
        if(index == -1){
            SocketUtil.sendMsg(socket, -1);
        } else {
            halls.set(index, hall);
            boolean success = FileUtil.saveData(halls, FileUtil.FILM_HALL_FILE);
            SocketUtil.sendMsg(socket, success ? 1: 0);
        }
    }

    /**
     * 处理添加影厅
     * @param msg
     */
    private void processAddFilmHall(Message msg) {
        FilmHall hall = (FilmHall) msg.getData();
        hall.setId(IdGenerator.generateId(10));
        List<FilmHall> halls = FileUtil.readData(FileUtil.FILM_HALL_FILE);
        boolean exists = halls.stream().anyMatch(h->h.getName().equals(hall.getName()));
        if(exists){
            SocketUtil.sendMsg(socket, -1);
        } else {
            halls.add(hall);
            boolean success = FileUtil.saveData(halls, FileUtil.FILM_HALL_FILE);
            SocketUtil.sendMsg(socket, success ? 1 : 0);
        }
    }

    /**
     * 处理删除影片
     * @param msg
     */
    private void processDeleteFilm(Message msg) {
        Map<String,Object> data = (Map<String, Object>) msg.getData();
        String id = (String) data.get("id");
        String owner = (String) data.get("owner");
        List<Film> films = FileUtil.readData(FileUtil.FILM_FILE);
        int index = -1;
        for(int i=0; i<films.size(); i++){
            Film f = films.get(i);
            if(id.equals(f.getId()) && owner.equals(f.getOwner())){
                index = i;
                break;
            }
        }
        if(index == -1){//删除的影片不存在
            SocketUtil.sendMsg(socket, -1);
        } else {
            films.remove(index);
            boolean success = FileUtil.saveData(films, FileUtil.FILM_FILE);
            SocketUtil.sendMsg(socket, success ? 1 : 0);
        }
    }

    /**
     * 处理更新影片
     * @param msg
     */
    private void processUpdateFilm(Message msg) {
        Film film = (Film) msg.getData();
        List<Film> films = FileUtil.readData(FileUtil.FILM_FILE);
        int index = -1;
        for(int i=0; i<films.size(); i++){
            Film f = films.get(i);
            if(film.getId().equals(f.getId()) && film.getOwner().equals(f.getOwner())){
                index = i;
                break;
            }
        }
        if(index == -1){//影片不存在
            SocketUtil.sendMsg(socket, -1);
        } else {
            films.set(index, film);
            boolean success = FileUtil.saveData(films, FileUtil.FILM_FILE);
            SocketUtil.sendMsg(socket, success ? 1 : 0);
        }
    }

    /**
     * 处理查看影片
     * @param msg
     */
    private void processViewFilms(Message msg) {
        Map<String,Object> data = (Map<String, Object>) msg.getData();
        String owner = (String) data.get("owner");
        String name = (String) data.get("name");
        List<Film> films = FileUtil.readData(FileUtil.FILM_FILE);
        List<Film> result = films.stream().filter(f->{
            String seller = f.getOwner();
            if(seller.equals(owner)){
                if("y".equalsIgnoreCase(name)) return true;
                else return name.contains(f.getName()) || f.getName().contains(name);
            }
            return false;
        }).collect(Collectors.toList());
        SocketUtil.sendMsg(socket, result);
    }

    /**
     * 处理添加影片
     * @param msg
     */
    private void processAddFilm(Message msg) {
        Film film = (Film) msg.getData();
        List<Film> films = FileUtil.readData(FileUtil.FILM_FILE);
        boolean exists = films.stream().anyMatch(f->f.getName().equals(film.getName()) && f.getActor().equals(film.getActor()));
        if(exists){//影片存在
            SocketUtil.sendMsg(socket, -1);
        } else {
            film.setId(IdGenerator.generateId(10));
            films.add(film);
            boolean success = FileUtil.saveData(films, FileUtil.FILM_FILE);
            SocketUtil.sendMsg(socket, success ? 1 : 0);
        }
    }

    /**
     * 处理冻结用户
     * @param message
     */
    private void processFrozenUser(Message message) {
        String username = (String) message.getData();
        List<User> users = FileUtil.readData(FileUtil.USER_FILE);
        int index = -1;
        for(int i=0; i<users.size(); i++){
            if(username.equals(users.get(i).getUsername())){
                index = i;
                break;
            }
        }
        if(index == -1){//账号不存在
            SocketUtil.sendMsg(socket, -1);
        } else {
            User user = users.get(index);
            int state = user.getState();
            if(state == 1){//正常
                user.setState(2);//冻结
                users.set(index, user);
                boolean success = FileUtil.saveData(users, FileUtil.USER_FILE);
                SocketUtil.sendMsg(socket, success ? 1: 0);
            } else {//其他状态不可冻结
                SocketUtil.sendMsg(socket, 2);
            }
        }
    }

    /**
     * 处理查看用户
     */
    private void processViewUsers() {
        List<User> users = FileUtil.readData(FileUtil.USER_FILE);
        List<User> sellers = users.stream().filter(u->(u instanceof Seller)).collect(Collectors.toList());
        users.removeAll(sellers);
        SocketUtil.sendMsg(socket, users);
    }
    /**
     * 处理审批解冻申请
     * @param msg
     */
    private void processAuditUnfrozenApply(Message msg) {
        Map<String,Object> data = (Map<String, Object>) msg.getData();
        String id = (String) data.get("id");
        int state = (int) data.get("state");
        List<UnfrozenApply> applies = FileUtil.readData(FileUtil.UNFROZEN_APPLY_FILE);
        UnfrozenApply apply = null;
        for(int i=0; i<applies.size(); i++){
            UnfrozenApply ua = applies.get(i);
            if(ua.getId().equals(id)){
                apply = ua;
                break;
            }
        }
        if(apply == null){//解冻申请不存在
            SocketUtil.sendMsg(socket, -1);
        } else {
            int applyState = apply.getState();
            if(applyState == 0){//待审核状态
                apply.setState(state);
                if(state == 1){//审批通过
                    List<User> users = FileUtil.readData(FileUtil.USER_FILE);
                    User user = null;
                    for(int i=0; i<users.size(); i++){
                        User u = users.get(i);
                        if(u.getUsername().equals(apply.getUsername())){
                            user = u;
                            break;
                        }
                    }
                    if(user == null){//should not be happened
                        SocketUtil.sendMsg(socket, 0);
                    } else {
                        user.setState(1);//设置账号状态正常
                        boolean success = FileUtil.saveData(users, FileUtil.USER_FILE);
                        if(success){
                            success = FileUtil.saveData(applies, FileUtil.UNFROZEN_APPLY_FILE);
                        }
                        SocketUtil.sendMsg(socket, success ? 1 : 0);
                    }
                } else {//审批不通过
                    boolean success = FileUtil.saveData(applies, FileUtil.UNFROZEN_APPLY_FILE);
                    SocketUtil.sendMsg(socket, success ? 1 : 0);
                }
            } else {//其他状态均不能审批
                SocketUtil.sendMsg(socket, -2);
            }
        }
    }


    /**
     * 处理查看解冻申请请求
     */
    private void processViewUnfrozenApplies() {
        List<UnfrozenApply> applies = FileUtil.readData(FileUtil.UNFROZEN_APPLY_FILE);
        SocketUtil.sendMsg(socket, applies);
    }

    /**
     * 处理冻结商家
     * @param msg
     */
    private void processFrozenSeller(Message msg) {
        String username = (String) msg.getData();
        changeSellerState(username, 1, 2);
    }

    /**
     * 更改给定账号的状态
     * @param username 账号
     * @param originalState 原来的状态
     * @param targetState 目标状态
     */
    private void changeSellerState(String username, int originalState, int targetState){
        List<User> users = FileUtil.readData(FileUtil.USER_FILE);
        Seller seller = null;
        for(int i=0; i<users.size(); i++){
            User user = users.get(i);
            if(user instanceof Seller){
                if(user.getUsername().equals(username)){
                    seller = (Seller) user;
                    break;
                }
            }
        }
        if(seller == null){//商家不存在
            SocketUtil.sendMsg(socket, -1);
        } else {
            int sellerState = seller.getState();
            if(sellerState == originalState){
                seller.setState(targetState);
                boolean success = FileUtil.saveData(users, FileUtil.USER_FILE);
                SocketUtil.sendMsg(socket, success ? 1 : 0);
            } else {
                SocketUtil.sendMsg(socket, -2);
            }
        }
    }

    /**
     * 处理审核商家
     * @param msg
     */
    private void processAuditSeller(Message msg) {
        Map<String,Object> data = (Map<String, Object>) msg.getData();
        String username = (String) data.get("username");
        int state = (int) data.get("state");
        changeSellerState(username, 0, state);
    }

    /**
     * 处理查看商家
     * @param msg
     */
    private void processViewSeller(Message msg) {
        String cinemaName = (String) msg.getData();
        List<User> users = FileUtil.readData(FileUtil.USER_FILE);
        List<Seller> sellers = users.stream()
                .filter(u->(u instanceof Seller))
                .map(u->(Seller)u)
                .filter(s-> "y".equalsIgnoreCase(cinemaName) || s.getCinemaName().contains(cinemaName) || cinemaName.contains(s.getCinemaName()))
                .collect(Collectors.toList());
        SocketUtil.sendMsg(socket, sellers);
    }

    /**
     * 处理解冻申请
     * @param msg
     */
    private void processUnfrozenApply(Message msg) {
        UnfrozenApply apply = (UnfrozenApply) msg.getData();
        List<User> users = FileUtil.readData(FileUtil.USER_FILE);
        Optional<User> opt = users.stream().filter(u->u.getUsername().equals(apply.getUsername())).findFirst();
        if(opt.isPresent()){
            User user = opt.get();
            int state = user.getState();
            if(state == 2){//账号处于冻结状态
                List<UnfrozenApply> applies = FileUtil.readData(FileUtil.UNFROZEN_APPLY_FILE);
                boolean exists = applies.stream().anyMatch(ua->ua.getUsername().equals(apply.getUsername()) && ua.getState() == 0);
                if(exists){//该账号存在未处理的解冻申请
                    SocketUtil.sendMsg(socket, -3);
                } else {
                    apply.setId(IdGenerator.generateId(10));
                    applies.add(apply);
                    boolean success = FileUtil.saveData(applies, FileUtil.UNFROZEN_APPLY_FILE);
                    SocketUtil.sendMsg(socket, success ? 1 : 0);
                }
            } else {//其他状态均不能进行解冻申请
                SocketUtil.sendMsg(socket, -2);
            }
        } else {//解冻账号不存在
            SocketUtil.sendMsg(socket, -1);
        }
    }

    /**
     * 处理找回密码
     * @param msg
     */
    private void processGetPasswordBack(Message msg) {
        User user = (User) msg.getData();
        List<User> users = FileUtil.readData(FileUtil.USER_FILE);
        Optional<User> opt = users.stream().filter(u->u.getUsername().equals(user.getUsername())).findFirst();
        if(opt.isPresent()){
            User u = opt.get();
            boolean valid = user.getSecurityCode().equals(u.getSecurityCode());
            SocketUtil.sendMsg(socket, valid ? u.getPassword() : null);
        } else {//账号不存在
            SocketUtil.sendMsg(socket, null);
        }
    }

    /**
     * 处理登录
     * @param msg
     */
    private void processLogin(Message msg) {
        User user = (User) msg.getData();
        List<User> users = FileUtil.readData(FileUtil.USER_FILE);
        if(users.isEmpty()){
            User manager=  new User("admin", "123456", "CYX");
            manager.setRole(User.Role.MANAGER);//设置为管理员
            users.add(manager);
            FileUtil.saveData(users, FileUtil.USER_FILE);
        }
        Optional<User> opt = users.stream().filter(u->u.getUsername().equals(user.getUsername())).findFirst();
        Map<String,Object> result = new HashMap<>();
        if(opt.isPresent()){//说明账号存在
            User loginUser = opt.get();
            //获取账号状态
            int state = loginUser.getState();
            if(state == 2){//冻结
                result.put("result", 3);
            } else if(state == 0){//待审核
                result.put("result", 2);
            } else if(state == -1){//审核不通过
                result.put("result", -1);
            } else {//正常
                boolean success = user.getPassword().equals(loginUser.getPassword());
                if(success){//登录成功
                    loginUser.setPassword(null);//密码置空，保障隐私安全
                    loginUser.setSecurityCode(null);//安全码置空，保障隐私安全
                    result.put("result", 1);
                    result.put("user", loginUser);
                } else {
                    result.put("result", 0);
                }
            }
        } else {//账号不存在
            result.put("result", -1);
        }
        SocketUtil.sendMsg(socket, result);
    }

    /**
     * 处理查看入驻申请
     * @param msg
     */
    private void processViewEntryApply(Message msg) {
        String cinemaName = (String) msg.getData();
        List<User> users = FileUtil.readData(FileUtil.USER_FILE);
        List<Seller> sellers = users.stream().filter(u->{
            if(u instanceof Seller){
                return cinemaName.contains(((Seller) u).getCinemaName()) || ((Seller) u).getCinemaName().contains(cinemaName);
            }
            return false;
        }).map(u->(Seller)u).collect(Collectors.toList());
        SocketUtil.sendMsg(socket, sellers);
    }

    /**
     * 处理注册/入驻申请
     * @param msg
     */
    private void processRegister(Message msg) {
        User user = (User) msg.getData();//获取消息携带的数据
        List<User> users = FileUtil.readData(FileUtil.USER_FILE);
        if(users.isEmpty()){
            User manager=  new User("admin", "123456", "CYX");
            manager.setRole(User.Role.MANAGER);//设置为管理员
            users.add(manager);
        }
//        List<Seller> sellerList = users.stream().filter(new Predicate<User>() {
//            @Override
//            public boolean test(User u) {
//                return (u instanceof Seller);
//            }
//        }).map(new Function<User, Seller>() {
//            @Override
//            public Seller apply(User user) {
//                return (Seller)user;
//            }
//        }).collect(Collectors.toList());
        List<Seller> sellerList = users.stream()
                .filter(u->(u instanceof Seller)).map(u->(Seller)u)
                .collect(Collectors.toList());
        users.removeAll(sellerList);
        boolean exists;//账号是否存在
        if(user instanceof Seller){//是商家
            exists = sellerList.stream().anyMatch(s->s.getUsername().equals(user.getUsername()) || s.getCinemaName().equals(((Seller) user).getCinemaName()));
        } else {//是用户
            exists = users.stream().anyMatch(u->u.getUsername().equals(user.getUsername()));
        }
        if(exists){//账号存在
            SocketUtil.sendMsg(socket, -1);
        } else {
            users.add(user);//注册用户/商家添加到用户列表中
            //将用户列表重新存档
            boolean success = FileUtil.saveData(users, FileUtil.USER_FILE);
            SocketUtil.sendMsg(socket, success ? 1 : 0);
        }
    }
}
