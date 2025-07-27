package com.cyx.cinema.sys.menu;

import java.util.Arrays;

/**
 * 菜单管理器
 */
public class MenuManager {
    /**
     * 登录菜单
     */
    public static final Menu[] LOGIN_MENUS = {
            new Menu(1, "注册","register"),
            new Menu(2, "登录","login"),
            new Menu(3, "找回密码","getPasswordBack"),
            new Menu(4, "解冻申请","unfrozenApply"),
            new Menu(5, "退出登录","quit")
    };
    /**
     * 普通用户菜单
     */
    public static final Menu[] USER_MENUS;
    static {
        Menu menu1 = new Menu(1, "我的订单","showChildren");
        menu1.addChild(new Menu(1, "查看订单", "getUserOrderList", menu1));
        menu1.addChild(new Menu(2, "修改订单", "updateOrder", menu1));
        menu1.addChild(new Menu(3, "取消订单", "cancelOrder", menu1));
        menu1.addChild(new Menu(4, "返回主菜单", "goBackMain", menu1));

        Menu menu2 = new Menu(2, "购买影票","showChildren");
        menu2.addChild(new Menu(1, "查看播放计划", "getFilmPlanList", menu2));
        menu2.addChild(new Menu(2, "在线订座", "orderSeatOnline", menu2));
        menu2.addChild(new Menu(3, "返回主菜单", "goBackMain", menu2));

        Menu menu3 = new Menu(3, "返回登录","goBackLogin");

        USER_MENUS = new Menu[]{ menu1, menu2, menu3};
    }
    /**
     * 管理员菜单
     */
    public static final Menu[] MANAGER_MENUS;
    static {
        Menu menu1 = new Menu(1, "影片管理","showChildren");
        menu1.addChild(new Menu(1, "查看影片", "getFilmList", menu1));
        menu1.addChild(new Menu(2, "增加影片", "addFilm", menu1));
        menu1.addChild(new Menu(3, "修改影片", "updateFilm", menu1));
        menu1.addChild(new Menu(4, "删除影片", "deleteFilm", menu1));
        menu1.addChild(new Menu(5, "返回主菜单", "goBackMain", menu1));

        Menu menu2 = new Menu(2, "影厅管理","showChildren");
        menu2.addChild(new Menu(1, "查看影厅", "getFilmHallList", menu2));
        menu2.addChild(new Menu(2, "增加影厅", "addFilmHall", menu2));
        menu2.addChild(new Menu(3, "修改影厅", "updateFilmHall", menu2));
        menu2.addChild(new Menu(4, "删除影厅", "deleteFilmHall", menu2));
        menu2.addChild(new Menu(5, "返回主菜单", "goBackMain", menu2));

        Menu menu3 = new Menu(3, "播放计划管理","showChildren");
        menu3.addChild(new Menu(1, "查看播放计划", "getFilmPlanList", menu3));
        menu3.addChild(new Menu(2, "增加播放计划", "addFilmPlan", menu3));
        menu3.addChild(new Menu(3, "修改播放计划", "updateFilmPlan", menu3));
        menu3.addChild(new Menu(4, "删除播放计划", "deleteFilmPlan", menu3));
        menu3.addChild(new Menu(5, "返回主菜单", "goBackMain", menu3));

        Menu menu4 = new Menu(4, "用户管理","showChildren");
        menu4.addChild(new Menu(1, "查看用户", "getUserList", menu4));
        menu4.addChild(new Menu(2, "冻结用户", "frozenUser", menu4));
        menu4.addChild(new Menu(3, "解冻用户", "unfrozenUser", menu4));
        menu4.addChild(new Menu(4, "查看解冻申请", "getUnfrozenApplyList", menu4));
        menu4.addChild(new Menu(5, "返回主菜单", "goBackMain", menu4));

        Menu menu5 = new Menu(5, "订单管理","showChildren");
        menu5.addChild(new Menu(1, "查看订单", "getOrderList", menu5));
        menu5.addChild(new Menu(2, "审核订单", "auditOrder", menu5));
        menu5.addChild(new Menu(3, "返回主菜单", "goBackMain", menu5));

        Menu menu6 = new Menu(6, "返回登录","goBackLogin");

        MANAGER_MENUS = new Menu[]{ menu1, menu2, menu3, menu4, menu5, menu6 };
    }

    /**
     * 展示给定的菜单数组
     * @param menus 菜单数组
     */
    public static void showMenu(Menu[] menus){
        Arrays.stream(menus).forEach(System.out::println);
    }
}
