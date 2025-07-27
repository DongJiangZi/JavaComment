package com.cinema.platform.client.action;

import com.cinema.platform.client.interact.MessageSender;
import com.cinema.platform.client.util.InputUtil;
import com.cyx.cinema.platform.command.Command;
import com.cyx.cinema.platform.entity.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户行为
 */
public class UserAction {
    /**
     * 注册
     */
    public static void register(){
        String username = InputUtil.getInputText("请输入账号：");
        String password = InputUtil.getInputText("请输入密码：");
        String securityCode = InputUtil.getInputText("请输入安全码：");
        User user = new User(username, password, securityCode);
        Integer result = MessageSender.sendMsg(Command.REGISTER, user);
        if(result == null || result == 0){
            System.out.println("注册失败，请稍后重试");
        } else if(result == 1){
            System.out.println("注册成功");
        } else {
            System.out.println("账号已被注册");
        }
    }
    /**
     * 入驻申请
     */
    public static void entryApply(){
        String username = InputUtil.getInputText("请输入账号：");
        String password = InputUtil.getInputText("请输入密码：");
        String securityCode = InputUtil.getInputText("请输入安全码：");
        String cinemaName = InputUtil.getInputText("请输入影院名称：");
        String cinemaAddress = InputUtil.getInputText("请输入影院地址：");
        Seller seller = new Seller(username, password, securityCode, cinemaName, cinemaAddress);
        Integer result = MessageSender.sendMsg(Command.REGISTER, seller);
        if(result == null || result == 0){
            System.out.println("入驻申请失败，请稍后重试");
        } else if(result == 1){
            System.out.println("入驻申请成功，请等待审核");
        } else {
            System.out.println("入驻申请账号或影院名称已被使用");
        }
    }
    /**
     * 登录
     */
    public static Map<String,Object> login(){
        String username = InputUtil.getInputText("请输入账号：");
        String password = InputUtil.getInputText("请输入密码：");
        User user = new User(username, password, null);
        return MessageSender.sendMsg(Command.LOGIN, user);
    }
    /**
     * 查案入驻申请
     */
    public static void viewEntryApply(){
        String cinemaName = InputUtil.getInputText("请输入影院名称：");
        List<Seller> sellers = MessageSender.sendMsg(Command.VIEW_ENTRY_APPLY, cinemaName);
        if(sellers == null || sellers.isEmpty()){
            System.out.println("未找到与\"" + cinemaName + "\"相关的入驻申请");
        } else {
            System.out.println("账号\t影院名称\t影院地址\t角色\t状态");
            sellers.forEach(System.out::println);
        }
    }
    /**
     * 找回密码
     */
    public static void getPasswordBack(){
        String username = InputUtil.getInputText("请输入账号：");
        String securityCode = InputUtil.getInputText("请输入安全码：");
        User user = new User(username, null, securityCode);
        String result = MessageSender.sendMsg(Command.GET_PASSWORD_BACK, user);
        if(result == null){
            System.out.println("安全码不正确");
        } else {
            System.out.println("您的密码是：" + result);
        }
    }
    /**
     * 解冻申请
     */
    public static void unfrozenApply(){
        String username = InputUtil.getInputText("请输入账号：");
        String reason = InputUtil.getInputText("请输入解冻理由：");
        UnfrozenApply apply = new UnfrozenApply(username, reason);
        Integer result = MessageSender.sendMsg(Command.UNFROZEN_APPLY, apply);
        if(result == null || result == 0){
            System.out.println("解冻申请发送失败，请稍后重试");
        } else if(result == -1){
            System.out.println("解冻账号不存在");
        } else if(result == 1){
            System.out.println("解冻申请发送成功，请等待审批");
        } else if(result == -2){
            System.out.println("账号未被冻结，不能进行解冻申请");
        } else {
            System.out.println("账号存在未处理的解冻申请，请勿重复发送");
        }
    }
    /**
     * 查看商家
     */
    public static void viewSellers(){
        String cinemaName = InputUtil.getInputText("请输入影院名称：");
        List<Seller> sellers = MessageSender.sendMsg(Command.VIEW_SELLERS, cinemaName);
        if(sellers == null || sellers.isEmpty()){
            System.out.println("未找到与\"" + cinemaName + "\"相关的商家信息");
        } else {
            System.out.println("账号\t\t影院名称\t影院地址\t\t角色\t\t状态");
            sellers.forEach(System.out::println);
        }
    }
    /**
     * 审核商家
     */
    public static void auditSeller(){
        String username = InputUtil.getInputText("请输入商家账号：");
        int state = InputUtil.getInputInteger("请输入审核状态：（-1-审核不通过，1-审核通过）", new int[]{-1, 1});
        Map<String,Object> data = new HashMap<>();
        data.put("username", username);
        data.put("state", state);
        Integer result = MessageSender.sendMsg(Command.AUDIT_SELLER, data);
        if(result == null || result == 0){
            System.out.println("审核失败，请稍后重试");
        } else if(result == 1){
            System.out.println("审核成功");
        } else if(result == -1){
            System.out.println("审核商家不存在");
        } else {
            System.out.println("当前商家无法进行审核");
        }
    }
    /**
     * 冻结商家
     */
    public static void frozenSeller(){
        String username = InputUtil.getInputText("请输入商家账号：");
        Integer result = MessageSender.sendMsg(Command.FROZEN_SELLER, username);
        if(result == null || result == 0){
            System.out.println("冻结失败，请稍后重试");
        } else if(result == 1){
            System.out.println("冻结成功");
        } else if(result == -1){
            System.out.println("冻结商家不存在");
        } else {
            System.out.println("当前商家无法进行冻结");
        }
    }
    /**
     * 查看用户
     */
    public static void viewUsers(){
        List<User> users = MessageSender.sendMsg(Command.VIEW_USERS, null);
        if(users == null || users.isEmpty()){
            System.out.println("当前暂无用户信息");
        } else {
            System.out.println("账号\t\t\t角色\t状态");
            users.forEach(System.out::println);
        }
    }
    /**
     * 冻结用户
     */
    public static void frozenUser(){
        String username = InputUtil.getInputText("请输入冻结账号：");
        Integer result = MessageSender.sendMsg(Command.FROZEN_USER, username);
        if(result == null || result == 0){
            System.out.println("冻结账号失败，请稍后重试");
        } else if(result == 1){
            System.out.println("冻结账号成功");
        } else if(result == -1){
            System.out.println("冻结账号不存在");
        } else {
            System.out.println("账号\"" + username + "\"所处状态不能进行冻结操作");
        }
    }
    /**
     * 查看解冻申请
     */
    public static void viewUnfrozenApplies(){
        List<UnfrozenApply> applies = MessageSender.sendMsg(Command.VIEW_UNFROZEN_APPLIES, null);
        if(applies == null || applies.isEmpty()){
            System.out.println("当前暂无解冻申请");
        } else {
            System.out.println("编号\t\t\t\t解冻账号\t理由\t\t处理状态");
            applies.forEach(System.out::println);
        }
    }
    /**
     * 审批解冻申请
     */
    public static void auditUnfrozenApply(){
        String id = InputUtil.getInputText("请输入解冻申请编号：");
        int state = InputUtil.getInputInteger("请输入审批状态：（1-审批通过，2-已驳回）", new int[]{1,2});
        Map<String,Object> data = new HashMap<>();
        data.put("id", id);
        data.put("state", state);
        Integer result = MessageSender.sendMsg(Command.AUDIT_UNFROZEN_APPLY, data);
        if(result == null || result == 0){
            System.out.println("审批失败，请稍后重试");
        } else if(result == 1){
            System.out.println("审批成功");
        } else if(result == -1){
            System.out.println("审核的解冻申请不存在");
        } else {
            System.out.println("当前账号无法进行审核");
        }
    }
    /**
     * 查看影厅
     */
    public static void viewFilmHalls(String owner){
        List<FilmHall> halls =MessageSender.sendMsg(Command.VIEW_FILM_HALLS, owner);
        if(halls == null || halls.isEmpty()){
            System.out.println("当前暂无影厅信息");
        } else {
            System.out.println("编号\t\t\t\t名称\t\t总排数\t总列数");
            halls.forEach(System.out::println);
        }
    }
    /**
     * 添加影厅
     */
    public static void addFilmHall(String owner){
        String name = InputUtil.getInputText("请输入影厅名称：");
        int row = InputUtil.getInputInteger("请输入影厅总排数：", 5, 10);
        int col = InputUtil.getInputInteger("请输入影厅总列数：", 5, 10);
        FilmHall hall = new FilmHall(name, row, col, owner);
        Integer result = MessageSender.sendMsg(Command.ADD_FILM_HALL, hall);
        if(result == null || result == 0){
            System.out.println("增加影厅失败，请稍后重试");
        } else if(result == 1){
            System.out.println("增加影厅成功");
        } else {
            System.out.println("影厅信息已存在，请勿重复添加");
        }
    }
    /**
     * 修改影厅
     */
    public static void updateFilmHall(String owner){
        String id = InputUtil.getInputText("请输入影厅编号：");
        String name = InputUtil.getInputText("请输入影厅名称：");
        int row = InputUtil.getInputInteger("请输入影厅总排数：", 5, 10);
        int col = InputUtil.getInputInteger("请输入影厅总列数：", 5, 10);
        FilmHall hall = new FilmHall(name, row, col, owner);
        hall.setId(id);
        Integer result = MessageSender.sendMsg(Command.UPDATE_FILM_HALL, hall);
        if(result == null || result == 0){
            System.out.println("更新影厅失败，请稍后重试");
        } else if(result == 1){
            System.out.println("更新影厅成功");
        } else {
            System.out.println("为找到与\"" + id + "\"相关的影厅信息，更新失败");
        }
    }
    /**
     * 删除影厅
     */
    public static void deleteFilmHall(String owner){
        String id = InputUtil.getInputText("请输入影厅编号：");
        Map<String,Object> data = new HashMap<>();
        data.put("id", id);
        data.put("owner", owner);
        Integer result = MessageSender.sendMsg(Command.DELETE_FILM_HALL, data);
        if(result == null || result == 0){
            System.out.println("删除影厅失败");
        } else if(result == 1){
            System.out.println("删除影厅成功");
        } else {
            System.out.println("未找到与\"" + id + "\"相关的影厅信息，删除失败");
        }
    }
    /**
     * 查看影片
     */
    public static void viewFilms(String owner){
        String name = InputUtil.getInputText("请输入影片名称：（Y-查看所有）");
        Map<String,Object> data = new HashMap<>();
        data.put("name", name);
        data.put("owner", owner);
        List<Film> films = MessageSender.sendMsg(Command.VIEW_FILMS, data);
        if(films == null || films.isEmpty()){
            System.out.println("未找到与\"" + name + "\"相关的影片信息");
        } else {
            System.out.println("编号\t\t\t\t名称\t\t主演\t描述");
            films.forEach(System.out::println);
        }
    }
    /**
     * 添加影片
     */
    public static void addFilm(String owner){
        String name = InputUtil.getInputText("请输入影片名称：");
        String actor = InputUtil.getInputText("请输入影片主演：");
        String description = InputUtil.getInputText("请输入影片描述：");
        Film film = new Film(name, actor, description, owner);
        Integer result = MessageSender.sendMsg(Command.ADD_FILM, film);
        if(result == null || result == 0){
            System.out.println("增加影片失败，请稍后重试");
        } else if(result == 1){
            System.out.println("增加影片成功");
        } else {
            System.out.println("影片信息已存在，请勿重复添加");
        }
    }
    /**
     * 修改影片
     */
    public static void updateFilm(String owner){
        String id = InputUtil.getInputText("请输入影片编号：");
        String name = InputUtil.getInputText("请输入影片名称：");
        String actor = InputUtil.getInputText("请输入影片主演：");
        String description = InputUtil.getInputText("请输入影片描述：");
        Film film = new Film(name, actor, description, owner);
        film.setId(id);
        Integer result =MessageSender.sendMsg(Command.UPDATE_FILM, film);
        if(result == null || result == 0){
            System.out.println("修改影片失败，请稍后重试");
        } else if(result == 1){
            System.out.println("修改影片成功");
        } else {
            System.out.println("未找到修改的影片信息");
        }
    }
    /**
     * 删除影片
     */
    public static void deleteFilm(String owner){
        String id = InputUtil.getInputText("请输入影片编号：");
        Map<String,Object> data = new HashMap<>();
        data.put("id", id);
        data.put("owner", owner);
        Integer result = MessageSender.sendMsg(Command.DELETE_FILM, data);
        if(result == null || result == 0){
            System.out.println("删除影片失败，请稍后重试");
        } else if(result == 1){
            System.out.println("删除影片成功");
        } else {
            System.out.println("删除的影片不存在");
        }
    }
    /**
     * 商家查看影片计划
     */
    public static void viewFilmPlans(User owner){
        String filmName = InputUtil.getInputText("请输入影片名称：（Y表示查询所有）");
        Map<String,Object> data = new HashMap<>();
        data.put("filmName", filmName);
        if(owner instanceof Seller)//商家
            data.put("owner", owner.getUsername());
        List<FilmPlan> films = MessageSender.sendMsg(Command.VIEW_FILM_PLANS, data);;
        if(films == null || films.isEmpty()){
            System.out.println("当前暂无影片计划信息");
        } else {
            System.out.println("编号\t\t\t\t影片名称\t影厅名称\t播放日期\t\t\t\t时间\t\t\t\t余票\t所属影院");
            films.forEach(System.out::println);
        }
    }
    /**
     * 添加影片计划
     */
    public static void addFilmPlan(Seller owner){
        String name = InputUtil.getInputText("请输入影片名称：（Y-查看所有）");
        Map<String,Object> data = new HashMap<>();
        data.put("name", name);
        data.put("owner", owner.getUsername());
        List<Film> films = MessageSender.sendMsg(Command.VIEW_FILMS, data);
        if(films == null || films.isEmpty()){
            System.out.println("未找到与\"" + name + "\"相关的影片信息");
        } else {
            System.out.println("编号\t\t\t\t名称\t\t主演\t描述");
            films.forEach(System.out::println);
            Film selectFilm = null;
            while (selectFilm == null){
                String filmId = InputUtil.getInputText("请输入影片编号：");
                Optional<Film> opt = films.stream()
                        .filter(f->f.getId().equals(filmId) && f.getOwner().equals(owner.getUsername()))
                        .findFirst();
                if(opt.isPresent()){
                    selectFilm = opt.get();
                } else {
                    System.out.println("影片编号输入错误");
                }
            }
            List<FilmHall> halls =MessageSender.sendMsg(Command.VIEW_FILM_HALLS, owner.getUsername());
            if(halls == null || halls.isEmpty()){
                System.out.println("当前暂无影厅信息");
            } else {
                System.out.println("编号\t\t\t\t名称\t\t总排数\t总列数");
                halls.forEach(System.out::println);
                FilmHall selectFilmHall = null;
                while (selectFilmHall == null){
                    String filmHallId = InputUtil.getInputText("请输入影厅编号：");
                    Optional<FilmHall> opt = halls.stream()
                            .filter(h->h.getId().equals(filmHallId) && h.getOwner().equals(owner.getUsername()))
                            .findFirst();
                    if(opt.isPresent()){
                        selectFilmHall = opt.get();
                    } else {
                        System.out.println("影厅编号输入错误");
                    }
                }
                String date = InputUtil.getInputDate("请输入播放日期：", "yyyy-MM-dd");
                String beginTime = InputUtil.getInputDate("请输入开始时间：", "HH:mm:ss");
                String endTime = InputUtil.getInputDate("请输入结束时间：", "HH:mm:ss");
                FilmPlan plan = new FilmPlan(selectFilm, selectFilmHall, date, beginTime, endTime, owner);
                Integer result = MessageSender.sendMsg(Command.ADD_FILM_PLAN, plan);
                if(result == null || result == 0){
                    System.out.println("增加影片计划失败，请稍后重试");
                } else if(result == 1){
                    System.out.println("增加影片计划成功");
                } else {
                    System.out.println("增加的影片计划存在时间冲突，请重新制定计划");
                }
            }
        }
    }
    /**
     * 修改影片计划
     */
    public static void updateFilmPlan(Seller owner){
        String id = InputUtil.getInputText("请输入影片计划编号：");
        String date = InputUtil.getInputDate("请输入播放日期：", "yyyy-MM-dd");
        String beginTime = InputUtil.getInputDate("请输入开始时间：", "HH:mm:ss");
        String endTime = InputUtil.getInputDate("请输入结束时间：", "HH:mm:ss");
        Map<String,Object> data =new HashMap<>();
        data.put("id", id);
        data.put("date", date);
        data.put("beginTime", beginTime);
        data.put("endTime", endTime);
        data.put("owner", owner.getUsername());
        Integer result = MessageSender.sendMsg(Command.UPDATE_FILM_PLAN, data);
        if(result == null || result == 0){
            System.out.println("修改影片计划失败，请稍后重试");
        } else if(result == 1){
            System.out.println("修改影片计划成功");
        } else if(result == -1){
            System.out.println("修改的影片计划不存在");
        } else {
            System.out.println("修改的影片计划存在时间冲突，请重新制定");
        }
    }
    /**
     * 删除影片计划
     */
    public static void deleteFilmPlan(Seller owner){
        String id = InputUtil.getInputText("请输入影片计划编号：");
        Map<String,Object> data = new HashMap<>();
        data.put("id", id);
        data.put("owner", owner.getUsername());
        Integer result = MessageSender.sendMsg(Command.DELETE_FILM_PLAN, data);
        if(result == null || result == 0){
            System.out.println("删除影片计划失败");
        } else if(result == 1){
            System.out.println("删除影片计划成功");
        } else {
            System.out.println("未找到与\"" + id + "\"相关的影片计划信息");
        }
    }
    /**
     * 查看订单
     */
    public static void viewOrders(User user){
        List<Order> orders = MessageSender.sendMsg(Command.VIEW_ORDERS, user);
        if(orders == null || orders.isEmpty()){
            System.out.println("当前并无订单信息");
        } else {
            System.out.println("编号\t\t\t\t影院\t\t影片名称\t影厅名称\t座位信息\t\t播放日期\t\t\t\t播放时间\t\t\t\t影院地址\t\t\t状态");
            orders.forEach(System.out::println);
        }
    }
    /**
     * 在线订座
     */
    public static void orderSeatOnline(String owner){
        String id = InputUtil.getInputText("请输入影片计划编号：");
        FilmPlan plan = MessageSender.sendMsg(Command.GET_FILM_PLAN_BY_ID, id);
        if(plan == null){
            System.out.println("未找到与\"" + id + "\"相关的影片计划");
        } else {
            FilmHall hall = plan.getHall();
            hall.showSeatInfo();
            while (true){
                int row = InputUtil.getInputInteger("请输入行号：", 0, hall.getTotalRow());
                int col = InputUtil.getInputInteger("请输入列号：", 0, hall.getTotalCol());
                if(hall.hasOwner(row, col)){
                    System.out.println("选择的座位已被订购，请重新选择");
                } else {
                    Map<String,Object> data = new HashMap<>();
                    data.put("id", id);
                    data.put("row", row);
                    data.put("col", col);
                    data.put("owner", owner);
                    Integer result = MessageSender.sendMsg(Command.ORDER_SEAT_ONLINE, data);
                    if(result == null || result == 0){
                        System.out.println("订座失败，请稍后重试");
                    } else {
                        System.out.println("订座成功");
                    }
                    break;
                }
            }
        }
    }
    /**
     * 修改订单
     */
    public static void updateOrder(String owner){
        String id = InputUtil.getInputText("请输入订单编号：");
        Order order = MessageSender.sendMsg(Command.GET_ORDER_BY_ID, id);
        if(order == null){
            System.out.println("未找到与\"" + id + "\"相关的订单");
            return;
        }
        FilmPlan plan = MessageSender.sendMsg(Command.GET_FILM_PLAN_BY_ID, order.getFilmPlanId());
        if(plan == null){
            System.out.println("未找到与\"" + order.getFilmPlanId() + "\"相关的影片计划");
            return;
        }
        FilmHall hall = plan.getHall();
        if(hall.getRestTickets() > 0){//还有余票
            hall.showSeatInfo();
            while (true){
                int row = InputUtil.getInputInteger("请输入行号：", 0, hall.getTotalRow());
                int col = InputUtil.getInputInteger("请输入列号：", 0, hall.getTotalCol());
                if(hall.hasOwner(row, col)){
                    System.out.println("选择的座位已被订购，请重新选择");
                } else {
                    Map<String,Object> data = new HashMap<>();
                    data.put("id", id);
                    data.put("row", row);
                    data.put("col", col);
                    data.put("owner", owner);
                    Integer result = MessageSender.sendMsg(Command.UPDATE_ORDER, data);
                    if(result == null || result == 0){
                        System.out.println("修改订单失败，请稍后重试");
                    } else {
                        System.out.println("修改订单成功");
                    }
                    break;
                }
            }
        } else {
            System.out.println("当前影片的影票已全部售完");
        }
    }
    /**
     * 取消订单
     */
    public static void cancelOrder(String owner){
        String id = InputUtil.getInputText("请输入订单编号：");
        Map<String,Object> data = new HashMap<>();
        data.put("id", id);
        data.put("owner", owner);
        Integer result = MessageSender.sendMsg(Command.CANCEL_ORDER, data);
        if(result == null || result == 0){
            System.out.println("取消订单失败，请稍后重试");
        } else if(result == 1){
            System.out.println("取消订单成功");
        } else if(result == -1){
            System.out.println("取消的订单不存在");
        } else {
            System.out.println("当前取消的订单已处于取消状态，不能再取消");
        }
    }
}
