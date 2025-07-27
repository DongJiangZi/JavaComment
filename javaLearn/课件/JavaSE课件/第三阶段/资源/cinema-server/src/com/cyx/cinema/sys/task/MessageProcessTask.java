package com.cyx.cinema.sys.task;

import com.cyx.cinema.sys.entity.*;
import com.cyx.cinema.sys.message.Message;
import com.cyx.cinema.sys.util.DateUtil;
import com.cyx.cinema.sys.util.FileUtil;
import com.cyx.cinema.sys.util.IdGenerator;
import com.cyx.cinema.sys.util.SocketUtil;

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

    private Socket client;

    public MessageProcessTask(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        Message msg = SocketUtil.receiveMsg(client);
        System.out.println(msg);
        if(msg != null){
            switch (msg.getAction()){
                case "register": //注册
                    processRegister(msg);
                    break;
                case "login": //登录
                    processLogin(msg);
                    break;
                case "getPasswordBack"://找回密码
                    processGetPasswordBack(msg);
                    break;
                case "unfrozenApply"://解冻申请
                    processUnfrozenApply(msg);
                    break;
                case "addFilm"://增加影片
                    processAddFilm(msg);
                    break;
                case "updateFilm"://修改影片
                    processUpdateFilm(msg);
                    break;
                case "deleteFilm"://删除影片
                    processDeleteFilm(msg);
                    break;
                case "getFilmList"://查看影片
                    processGetFilmList(msg);
                    break;
                case "addFilmHall"://增加影厅
                    processAddFilmHall(msg);
                    break;
                case "updateFilmHall"://修改影厅
                    processUpdateFilmHall(msg);
                    break;
                case "deleteFilmHall"://删除影厅
                    processDeleteFilmHall(msg);
                    break;
                case "getFilmHallList"://查看影厅
                    processGetFilmHallList();
                    break;
                case "addFilmPlan"://增加播放计划
                    processAddFilmPlan(msg);
                    break;
                case "updateFilmPlan"://修改播放计划
                    processUpdateFilmPlan(msg);
                    break;
                case "deleteFilmPlan"://删除播放计划
                    processDeleteFilmPlan(msg);
                    break;
                case "getFilmPlanList"://查看播放计划
                    processGetFilmPlanList(msg);
                    break;
                case "getUserList"://查看用户
                    processGetUserList();
                    break;
                case "frozenUser"://冻结用户
                    processFrozenUser(msg);
                    break;
                case "unfrozenUser"://解冻用户
                    processUnfrozenUser(msg);
                    break;
                case "getUnfrozenApplyList"://查看用户解冻申请
                    processGetUnfrozenApplyList();
                    break;
                case "getOrderList"://查看订单
                    processGetOrderList(msg);
                    break;
                case "getUserOrderList"://查看用户订单
                    processGetUserOrderList(msg);
                    break;
                case "updateOrder"://修改订单
                    processUpdateOrder(msg);
                    break;
                case "cancelOrder"://取消订单
                    processCancelOrder(msg);
                    break;
                case "auditOrder"://审核订单
                    processAuditOrder(msg);
                    break;
                case "orderSeatOnline"://在线订座
                    processOrderSeatOnline(msg);
                    break;
            }
        }
    }

    /**
     * 处理查看订单请求
     */
    private void processGetOrderList(Message msg) {
        Object data = msg.getData();
        List<Order> orders = FileUtil.readData(FileUtil.ORDER_FILE);
        if(data == null){
            SocketUtil.sendBack(client, orders);
        } else {
            int state = (int) data;
            List<Order> orderList = orders.stream().filter(o->o.getState() == state).collect(Collectors.toList());
            SocketUtil.sendBack(client, orderList);
        }
    }
    /**
     * 处理查看用户订单请求
     */
    private void processGetUserOrderList(Message msg) {
        String username = (String) msg.getData();
        List<Order> orders = FileUtil.readData(FileUtil.ORDER_FILE);
        List<Order> result = orders.stream().filter(o->o.getOwner().equals(username)).collect(Collectors.toList());
        SocketUtil.sendBack(client, result);
    }

    /**
     * 处理更新订单请求
     * @param msg
     */
    private void processUpdateOrder(Message msg) {

    }
    /**
     * 处理用户取消订单请求
     */
    private void processCancelOrder(Message msg) {
        String orderId = (String) msg.getData();
        List<Order> orders = FileUtil.readData(FileUtil.ORDER_FILE);
        int index = -1;
        for(int i=0; i<orders.size(); i++){
            Order order = orders.get(i);
            if(order.getId().equals(orderId)){
                index = i;
                break;
            }
        }
        if(index == -1){
            SocketUtil.sendBack(client, -1);
        } else {
            Order order = orders.get(index);
            if(order.getState() == 1){//订单状态正常
                order.setState(0); //更改订单状态为取消中
                orders.set(index, order);
                boolean success = FileUtil.saveData(orders, FileUtil.ORDER_FILE);
                SocketUtil.sendBack(client, success ? 1 : 0);
            } else {//订单处于取消中或者已退订
                SocketUtil.sendBack(client, -2);
            }
        }
    }

    /**
     * 处理审核订单请求
     * @param msg
     */
    private void processAuditOrder(Message msg) {
        String orderId= (String) msg.getData();
        List<Order> orders = FileUtil.readData(FileUtil.ORDER_FILE);
        int index = -1;
        for(int i=0; i<orders.size(); i++){
            Order order = orders.get(i);
            if(order.getId().equals(orderId)){
                index = i;
                break;
            }
        }
        if(index == -1){
            SocketUtil.sendBack(client, -1);
        } else {
            Order order = orders.get(index);
            order.setState(2);
            orders.set(index, order);
            boolean success = FileUtil.saveData(orders, FileUtil.ORDER_FILE);
            SocketUtil.sendBack(client, success ? 1 : 0);
        }
    }

    /**
     * 处理在线订座请求
     * @param msg
     */
    private void processOrderSeatOnline(Message msg) {
        Map<String,Object> data = (Map<String, Object>) msg.getData();
        String planId  = (String) data.get("planId");
        int row = (int) data.get("row");
        int col = (int) data.get("col");
        String username = (String) data.get("username");
        List<FilmPlan> plans = FileUtil.readData(FileUtil.FILM_PLAN_FILE);
        int index = -1;
        for(int i=0; i<plans.size(); i++){
            FilmPlan plan = plans.get(i);
            if(plan.getId().equals(planId)){
                index = i;
                break;
            }
        }
        if(index == -1){
            SocketUtil.sendBack(client, -1);
        } else {
            FilmPlan plan = plans.get(index);
            FilmHall hall = plan.getFilmHall();
            hall.setOwner(row, col, username);
            plan.setFilmHall(hall);
            plans.set(index, plan);
            boolean success = FileUtil.saveData(plans, FileUtil.FILM_PLAN_FILE);
            Order order = new Order();
            order.setId(IdGenerator.generateId(10));
            order.setFilmName(plan.getFilm().getName());
            order.setOwner(username);
            order.setBegin(plan.getBegin());
            order.setEnd(plan.getEnd());
            String seatInfo = hall.getName() + " 第" + row + "排第" + col + "列";
            order.setSeatInfo(seatInfo);
            List<Order> orders = FileUtil.readData(FileUtil.ORDER_FILE);
            orders.add(order);
            FileUtil.saveData(orders, FileUtil.ORDER_FILE);
            SocketUtil.sendBack(client, success ? 1 : 0);
        }
    }

    /**
     * 处理查看用户请求
     */
    private void processGetUserList() {
        List<User> users = FileUtil.readData(FileUtil.USER_FILE);
        SocketUtil.sendBack(client, users);
    }

    /**
     * 处理冻结用户请求
     */
    private void processFrozenUser(Message msg) {
        String username = (String) msg.getData();
        List<User> users = FileUtil.readData(FileUtil.USER_FILE);
        int index = -1;
        for(int i=0; i<users.size(); i++){
            User user = users.get(i);
            if(username.equals(user.getUsername())){
                index = i;
                break;
            }
        }
        if(index == -1){//账号不存在
            SocketUtil.sendBack(client, -1);
        } else {
            User user = users.get(index);
            if(user.getState() == 1){//正常
                user.setState(0);//设置账号状态为冻结状态
                users.set(index, user);
                boolean success = FileUtil.saveData(users, FileUtil.USER_FILE);
                SocketUtil.sendBack(client, success ? 1 : 0);
            } else {//被冻结
                SocketUtil.sendBack(client, -2);
            }
        }
    }

    /**
     * 处理解冻申请请求
     */
    private void processUnfrozenUser(Message msg) {
        Map<String,Object> data = (Map<String,Object>) msg.getData();
        String id = (String) data.get("id");
        int number = (int) data.get("number");
        List<UnfrozenApply> applies = FileUtil.readData(FileUtil.UNFROZEN_APPLY_FILE);
        int index = -1;
        for(int i=0; i<applies.size(); i++){
            UnfrozenApply apply = applies.get(i);
            if(apply.getId().equals(id)){
                index = i;
                break;
            }
        }
        if(index == -1){
            SocketUtil.sendBack(client, -2);
        } else {
            UnfrozenApply apply = applies.get(index);
            int state = apply.getState();
            if(state == 0){//待处理
                apply.setState(number);
                List<User> users = FileUtil.readData(FileUtil.USER_FILE);
                int userIndex = -1;
                for(int i=0; i<users.size(); i++){
                    User user = users.get(i);
                    if(user.getUsername().equals(apply.getUsername())){
                        userIndex = i;
                        break;
                    }
                }
                if(number == 1){
                    User user =users.get(userIndex);
                    user.setState(1);
                    users.set(userIndex, user);
                    FileUtil.saveData(users, FileUtil.USER_FILE);
                }
                applies.set(index, apply);
                boolean success = FileUtil.saveData(applies, FileUtil.UNFROZEN_APPLY_FILE);
                SocketUtil.sendBack(client, success ? 1 : 0);
            } else {
                SocketUtil.sendBack(client, -1);
            }
        }
    }
    /**
     * 处理查看解冻申请请求
     */
    private void processGetUnfrozenApplyList() {
        List<UnfrozenApply> applies = FileUtil.readData(FileUtil.UNFROZEN_APPLY_FILE);
        SocketUtil.sendBack(client, applies);
    }

    /**
     * 处理添加播放计划请求
     * @param msg
     */
    private void processAddFilmPlan(Message msg) {
        FilmPlan plan = (FilmPlan) msg.getData();
        List<FilmPlan> plans = FileUtil.readData(FileUtil.FILM_PLAN_FILE);
        boolean conflict = plans.stream().anyMatch(fp-> DateUtil.isConflictPlan(plan, fp));
        if(conflict){//播放时间冲突
            SocketUtil.sendBack(client, -1);
        } else {
            plans.add(plan);
            boolean success = FileUtil.saveData(plans, FileUtil.FILM_PLAN_FILE);
            SocketUtil.sendBack(client, success ? 1 : 0);
        }
    }
    /**
     * 处理更新播放计划请求
     * @param msg
     */
    private void processUpdateFilmPlan(Message msg) {
        FilmPlan plan = (FilmPlan) msg.getData();
        List<FilmPlan> plans = FileUtil.readData(FileUtil.FILM_PLAN_FILE);
        int index = -1;
        for(int i=0; i<plans.size(); i++){
            FilmPlan fp = plans.get(i);
            if(plan.getId().equals(fp.getId())){
                index = i;
                break;
            }
        }
        if(index == -1){//更新的播放计划不存在
            SocketUtil.sendBack(client, -2);
        } else {
            FilmPlan remove = plans.remove(index);//现将原来的播放计划移除
            //然后再检测是否存在时间冲突
            boolean conflict = plans.stream().anyMatch(fp-> DateUtil.isConflictPlan(plan, fp));
            if(conflict){
                SocketUtil.sendBack(client, -1);
            } else {
                plan.setFilm(remove.getFilm());
                plan.setFilmHall(remove.getFilmHall());
                plans.add(plan);
                boolean success = FileUtil.saveData(plans, FileUtil.FILM_PLAN_FILE);
                SocketUtil.sendBack(client, success ? 1 : 0);
            }
        }
    }

    /**
     * 处理删除播放计划请求
     * @param msg
     */
    private void processDeleteFilmPlan(Message msg) {
        String planId = (String) msg.getData();
        List<FilmPlan> plans = FileUtil.readData(FileUtil.FILM_PLAN_FILE);
        int index = -1;
        for(int i=0; i<plans.size(); i++){
            FilmPlan fp = plans.get(i);
            if(planId.equals(fp.getId())){
                index = i;
                break;
            }
        }
        if(index == -1){
            SocketUtil.sendBack(client, -1);
        } else {
            plans.remove(index);
            boolean success = FileUtil.saveData(plans, FileUtil.FILM_PLAN_FILE);
            SocketUtil.sendBack(client, success ? 1 : 0);
        }
    }

    /**
     * 处理查看播放计划请求
     * @param msg
     */
    private void processGetFilmPlanList(Message msg) {
        String filmName = (String) msg.getData();
        List<FilmPlan> plans = FileUtil.readData(FileUtil.FILM_PLAN_FILE);
        if(filmName == null || "".equals(filmName)){
            SocketUtil.sendBack(client, plans);
        } else {
            List<FilmPlan> result = plans.stream().filter(fp->{
                Film film = fp.getFilm();
                return film.getName().contains(filmName) || filmName.contains(film.getName());
            }).collect(Collectors.toList());
            SocketUtil.sendBack(client, result);
        }
    }

    /**
     * 处理添加影厅请求
     * @param msg
     */
    private void processAddFilmHall(Message msg) {
        FilmHall hall = (FilmHall) msg.getData();
        List<FilmHall> filmHalls = FileUtil.readData(FileUtil.FILM_HALL_FILE);
        filmHalls.add(hall);
        boolean success = FileUtil.saveData(filmHalls, FileUtil.FILM_HALL_FILE);
        SocketUtil.sendBack(client, success ? 1 : 0);
    }

    /**
     * 处理修改影厅请求
     * @param msg
     */
    private void processUpdateFilmHall(Message msg) {
        FilmHall updateFilmHall = (FilmHall) msg.getData();
        List<FilmHall> halls = FileUtil.readData(FileUtil.FILM_HALL_FILE);
        int index = -1;
        for(int i=0; i<halls.size(); i++){
            FilmHall hall = halls.get(i);
            if(updateFilmHall.getId().equals(hall.getId())){
                index = i;
                break;
            }
        }
        if(index == -1){//说明修改的影厅信息不存在
            SocketUtil.sendBack(client, -1);
        } else {
            halls.set(index, updateFilmHall);
            boolean success = FileUtil.saveData(halls, FileUtil.FILM_HALL_FILE);
            SocketUtil.sendBack(client, success ? 1 : 0);
        }
    }
    /**
     * 处理删除影厅请求
     * @param msg
     */
    private void processDeleteFilmHall(Message msg) {
        String id = (String) msg.getData();
        List<FilmHall> halls = FileUtil.readData(FileUtil.FILM_HALL_FILE);
        int index = -1;
        for(int i=0; i<halls.size(); i++){
            FilmHall hall = halls.get(i);
            if(id.equals(hall.getId())){
                index = i;
                break;
            }
        }
        if(index == -1){//说明删除的影片信息不存在
            SocketUtil.sendBack(client, -1);
        } else {
            halls.remove(index);
            boolean success = FileUtil.saveData(halls, FileUtil.FILM_HALL_FILE);
            SocketUtil.sendBack(client, success ? 1 : 0);
        }
    }
    /**
     * 处理查看影厅请求
     */
    private void processGetFilmHallList() {
        List<FilmHall> halls = FileUtil.readData(FileUtil.FILM_HALL_FILE);
        SocketUtil.sendBack(client, halls);
    }

    /**
     * 处理查看影片请求
     * @param msg
     */
    private void processGetFilmList(Message msg) {
        String name = (String) msg.getData();
        List<Film> films = FileUtil.readData(FileUtil.FILM_FILE);
        if(name == null || "".equals(name)){
            SocketUtil.sendBack(client, films);
        } else {
            List<Film> result = films.stream().filter(
                    f->f.getName().contains(name) || name.contains(f.getName()))
                    .collect(Collectors.toList());
            SocketUtil.sendBack(client, result);
        }
    }

    /**
     * 处理删除影片请求
     * @param msg
     */
    private void processDeleteFilm(Message msg) {
        String id = (String) msg.getData();
        List<Film> films = FileUtil.readData(FileUtil.FILM_FILE);
        int index = -1;
        for(int i=0; i<films.size(); i++){
            Film film = films.get(i);
            if(id.equals(film.getId())){
                index = i;
                break;
            }
        }
        if(index == -1){//说明删除的影片信息不存在
            SocketUtil.sendBack(client, -1);
        } else {
            films.remove(index);
            boolean success = FileUtil.saveData(films, FileUtil.FILM_FILE);
            SocketUtil.sendBack(client, success ? 1 : 0);
        }
    }

    /**
     * 处理修改影片请求
     * @param msg
     */
    private void processUpdateFilm(Message msg) {
        Film updateFilm = (Film) msg.getData();
        List<Film> films = FileUtil.readData(FileUtil.FILM_FILE);
        int index = -1;
        for(int i=0; i<films.size(); i++){
            Film film = films.get(i);
            if(updateFilm.getId().equals(film.getId())){
                index = i;
                break;
            }
        }
        if(index == -1){//说明修改的影片信息不存在
            SocketUtil.sendBack(client, -1);
        } else {
            films.set(index, updateFilm);
            boolean success = FileUtil.saveData(films, FileUtil.FILM_FILE);
            SocketUtil.sendBack(client, success ? 1 : 0);
        }
    }

    /**
     * 处理添加影片请求
     * @param msg
     */
    private void processAddFilm(Message msg) {
        Film film = (Film) msg.getData();
        List<Film> films = FileUtil.readData(FileUtil.FILM_FILE);
        films.add(film);
        boolean success = FileUtil.saveData(films, FileUtil.FILM_FILE);
        SocketUtil.sendBack(client, success ? 1 : 0);
    }


    /**
     * 处理解冻申请请求
     * @param msg
     */
    private void processUnfrozenApply(Message msg) {
        UnfrozenApply apply = (UnfrozenApply) msg.getData();
        //读取存档的用户信息
        List<User> saveUsers = FileUtil.readData(FileUtil.USER_FILE);
        if(saveUsers.isEmpty()){//用户存档信息为空，表明没有任何用注册
            User user = new User("admin", "123456", "CYX");
            user.setManager(true);//设置为管理员
            saveUsers.add(user);//添加至用户列表中
            FileUtil.saveData(saveUsers, FileUtil.USER_FILE);
        }
        int result;
        Optional<User> opt = saveUsers.stream().filter(user->user.getUsername().equals(apply.getUsername())).findFirst();
        if(opt.isPresent()){//账号存在
            User user = opt.get();
            if(user.getState() == 1){//账号正常
                result = -1;
            } else {//账号被冻结
                List<UnfrozenApply> applies = FileUtil.readData(FileUtil.UNFROZEN_APPLY_FILE);
                applies.add(apply);
                boolean success = FileUtil.saveData(applies, FileUtil.UNFROZEN_APPLY_FILE);
                result = success ? 1 : 0;
            }
        } else {//账号不存在，也就不存在解冻申请
            result = -1;
        }
        SocketUtil.sendBack(client, result);
    }

    /**
     * 处理找回密码请求
     * @param msg
     */
    private void processGetPasswordBack(Message msg){
        User getBackUser = (User) msg.getData();
        //读取存档的用户信息
        List<User> saveUsers = FileUtil.readData(FileUtil.USER_FILE);
        if(saveUsers.isEmpty()){//用户存档信息为空，表明没有任何用注册
            User user = new User("admin", "123456", "CYX");
            user.setManager(true);//设置为管理员
            saveUsers.add(user);//添加至用户列表中
            FileUtil.saveData(saveUsers, FileUtil.USER_FILE);
        }
        String result = null;
        Optional<User> opt = saveUsers.stream().filter(user->user.getUsername().equals(getBackUser.getUsername())).findFirst();
        if(opt.isPresent()){//如果Optional中有存储数据
            User user = opt.get();
            //安全码匹配
            if(user.getSecurityCode().equals(getBackUser.getSecurityCode())){
                result = user.getPassword();
            }
        }
        SocketUtil.sendBack(client, result);
    }

    /**
     * 处理登录请求
     */
    private void processLogin(Message msg){
        User loginUser = (User) msg.getData();
        //读取存档的用户信息
        List<User> saveUsers = FileUtil.readData(FileUtil.USER_FILE);
        if(saveUsers.isEmpty()){//用户存档信息为空，表明没有任何用注册
            User user = new User("admin", "123456", "CYX");
            user.setManager(true);//设置为管理员
            saveUsers.add(user);//添加至用户列表中
        }
        Map<String,Object> result = new HashMap<>();
        //查找用户名与登录用户的用户名匹配的账号
        Optional<User> opt = saveUsers.stream().filter(user->user.getUsername().equals(loginUser.getUsername())).findFirst();
        if(opt.isPresent()){//如果有找到
            User user = opt.get();//取出来
            int state = user.getState(); //获取账号状态
            if(state == 1){//账号正常
                //密码匹配
                if(loginUser.getPassword().equals(user.getPassword())){
                    result.put("process", 1);
                    result.put("user", user);
                } else {//密码不匹配
                    result.put("process", 0);
                }
            } else {//账号冻结
                result.put("process", -2);
            }
        } else {//账号不存在
            result.put("process", -1);
        }
        SocketUtil.sendBack(client, result);
    }
    /**
     * 处理注册请求
     */
    private void processRegister(Message msg){
        User registerUser = (User) msg.getData();
        //读取存档的用户信息
        List<User> saveUsers = FileUtil.readData(FileUtil.USER_FILE);
        if(saveUsers.isEmpty()){//用户存档信息为空，表明没有任何用注册
            User user = new User("admin", "123456", "CYX");
            user.setManager(true);//设置为管理员
            saveUsers.add(user);//添加至用户列表中
        }
        int result;
//        boolean existUsername = saveUsers.stream().anyMatch(new Predicate<User>() {
//            @Override
//            public boolean test(User user) {
//                return user.getUsername().equals(registerUser.getUsername());
//            }
//        });
        boolean existUsername = saveUsers.stream().anyMatch(user ->user.getUsername().equals(registerUser.getUsername()));
        if(existUsername){//如果账号已存在
            result = -1;
        } else {
            saveUsers.add(registerUser);//将注册用户添加到用户列表中
            //用户存档
            boolean success = FileUtil.saveData(saveUsers, FileUtil.USER_FILE);
            result = success ? 1 : 0;
        }
        //向客户端反馈结果
        SocketUtil.sendBack(client, result);
    }
}
