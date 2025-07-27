package com.cyx.cinema.platform.command;

/**
 * 命令接口
 */
public interface Command {
    //0123456789  9+1=>10
    //1+1 => 10
    //7+1 => 10
    //0123456789a(10)b(11)c(12)d(13)e(14)f(15)
    //15(f)+1 => 10
    /**
     * 注册
     */
    int REGISTER = 0x00;
    /**
     * 登录
     */
    int LOGIN = 0x01;
    /**
     * 入驻申请
     */
    int ENTRY_APPLY = 0x02;
    /**
     * 查看入驻申请
     */
    int VIEW_ENTRY_APPLY = 0x03;
    /**
     * 找回密码
     */
    int GET_PASSWORD_BACK = 0x04;
    /**
     * 申请解冻
     */
    int UNFROZEN_APPLY = 0x05;
    /**
     * 退出
     */
    int QUIT = 0x06;
    /**
     * 显示子菜单
     */
    int SHOW_CHILDREN = 0x07;
    /**
     * 查看订单
     */
    int VIEW_ORDERS = 0x08;
    /**
     * 修改订单
     */
    int UPDATE_ORDER = 0x09;
    /**
     * 取消订单
     */
    int CANCEL_ORDER = 0x0a;
    /**
     * 返回主菜单
     */
    int GO_BACK_MAIN = 0x0b;
    /**
     * 在线订座
     */
    int ORDER_SEAT_ONLINE = 0x2d;
    /**
     * 查看商家
     */
    int VIEW_SELLERS = 0x0c;
    /**
     * 审核商家
     */
    int AUDIT_SELLER = 0x0d;
    /**
     * 冻结商家
     */
    int FROZEN_SELLER = 0x0e;
    /**
     * 返回登录
     */
    int GO_BACK_LOGIN = 0x0f;
    /**
     * 查看用户
     */
    int VIEW_USERS = 0x10;
    /**
     * 冻结用户
     */
    int FROZEN_USER = 0x11;
    /**
     * 查看解冻申请
     */
    int VIEW_UNFROZEN_APPLIES = 0x12;
    /**
     * 审批解冻申请
     */
    int AUDIT_UNFROZEN_APPLY = 0x13;
    /**
     * 查看影厅
     */
    int VIEW_FILM_HALLS = 0x14;
    /**
     * 添加影厅
     */
    int ADD_FILM_HALL = 0x15;
    /**
     * 修改影厅
     */
    int UPDATE_FILM_HALL = 0x16;
    /**
     * 删除影厅
     */
    int DELETE_FILM_HALL = 0x17;
    /**
     * 查看影片
     */
    int VIEW_FILMS = 0x18;
    /**
     * 添加影片
     */
    int ADD_FILM = 0x19;
    /**
     * 修改影片
     */
    int UPDATE_FILM = 0x1a;
    /**
     * 删除影片
     */
    int DELETE_FILM = 0x1b;
    /**
     * 查看商家影片计划
     */
    int VIEW_FILM_PLANS = 0x1c;
    /**
     * 添加影片计划
     */
    int ADD_FILM_PLAN = 0x1d;
    /**
     * 更新影片计划
     */
    int UPDATE_FILM_PLAN = 0x1e;
    /**
     * 删除影片计划
     */
    int DELETE_FILM_PLAN = 0x1f;
    /**
     * 通过影片计划编号查找影片计划
     */
    int GET_FILM_PLAN_BY_ID = 0x20;
    /**
     * 通过定点杆编号查找订单
     */
    int GET_ORDER_BY_ID = 0x21;
}
