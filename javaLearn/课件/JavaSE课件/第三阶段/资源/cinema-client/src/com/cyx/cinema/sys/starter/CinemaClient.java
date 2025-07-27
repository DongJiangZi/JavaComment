package com.cyx.cinema.sys.starter;

import com.cyx.cinema.sys.action.UserAction;
import com.cyx.cinema.sys.entity.FilmHall;
import com.cyx.cinema.sys.entity.User;
import com.cyx.cinema.sys.menu.Menu;
import com.cyx.cinema.sys.menu.MenuManager;
import com.cyx.cinema.sys.util.InputUtil;

import java.util.List;
import java.util.Map;

/**
 * 影院客户端
 */
public class CinemaClient {
    /**
     * 当前登录用户
     */
    private static User currentUser;

    public static void main(String[] args) {
        showInterface(MenuManager.LOGIN_MENUS);
    }

    /**
     * 展示界面
     */
    private static void showInterface(Menu[] menus){
        MenuManager.showMenu(menus);
        int number = InputUtil.getInputInteger("请选择菜单编号：", 1, menus.length);
        Menu select = menus[number-1];
        switch (select.getAction()){
            case "login":
                Map<String,Object> result = UserAction.login();
                if(result == null){//登录失败
                    System.out.println("账号或密码错误，请稍后重试");
                    showInterface(MenuManager.LOGIN_MENUS);
                } else {
                    int process = (int) result.get("process");
                    if(process == 1){//登录成功
                        currentUser = (User) result.get("user");
                        Menu[] mainMains = currentUser.isManager() ? MenuManager.MANAGER_MENUS : MenuManager.USER_MENUS;
                        showInterface(mainMains);
                    } else {
                        String msg;
                        if(process == 0){//账号或密码错误
                            msg ="账号或密码错误，请稍后重试";
                        } else if(process == -1){//账号不存在
                            msg = "账号不存在，请先注册";
                        } else {//账号已被冻结
                            msg = "账号已被冻结，请申请解冻";
                        }
                        System.out.println(msg);
                        showInterface(MenuManager.LOGIN_MENUS);
                    }
                }
                break;
            case "register":
                UserAction.register();
                showInterface(MenuManager.LOGIN_MENUS);
                break;
            case "getPasswordBack":
                UserAction.getPasswordBack();
                showInterface(MenuManager.LOGIN_MENUS);
                break;
            case "unfrozenApply":
                UserAction.unfrozenApply();
                showInterface(MenuManager.LOGIN_MENUS);
                break;
            case "quit":
                UserAction.quit();
                break;
            case "showChildren":
                List<Menu> children = select.getChildren();
                Menu[] childMenus = children.toArray(new Menu[children.size()]);
                showInterface(childMenus);
                break;
            case "goBackLogin":
                showInterface(MenuManager.LOGIN_MENUS);
                break;
            case "goBackMain":
                showInterface(currentUser.isManager() ? MenuManager.MANAGER_MENUS : MenuManager.USER_MENUS);
                break;
            case "addFilm"://增加影片
                UserAction.addFilm();
                showSiblingMenus(select);
                break;
            case "updateFilm"://修改影片
                UserAction.updateFilm();
                showSiblingMenus(select);
                break;
            case "deleteFilm"://删除影片
                UserAction.deleteFilm();
                showSiblingMenus(select);
                break;
            case "getFilmList"://查看影片
                UserAction.getFilmList();
                showSiblingMenus(select);
                break;
            case "addFilmHall"://增加影厅
                UserAction.addFilmHall();
                showSiblingMenus(select);
                break;
            case "updateFilmHall"://修改影厅
                UserAction.updateFilmHall();
                showSiblingMenus(select);
                break;
            case "deleteFilmHall"://删除影厅
                UserAction.deleteFilmHall();
                showSiblingMenus(select);
                break;
            case "getFilmHallList"://查看影厅
                UserAction.getFilmHallList();
                showSiblingMenus(select);
                break;
            case "addFilmPlan"://增加播放计划
                UserAction.addFilmPlan();
                showSiblingMenus(select);
                break;
            case "updateFilmPlan"://修改播放计划
                UserAction.updateFilmPlan();
                showSiblingMenus(select);
                break;
            case "deleteFilmPlan"://删除播放计划
                UserAction.deleteFilmPlan();
                showSiblingMenus(select);
                break;
            case "getFilmPlanList"://查看播放计划
                UserAction.getFilmPlanList();
                showSiblingMenus(select);
                break;
            case "getUserList"://查看用户
                UserAction.getUserList();
                showSiblingMenus(select);
                break;
            case "frozenUser"://冻结用户
                UserAction.frozenUser();
                showSiblingMenus(select);
                break;
            case "unfrozenUser"://解冻用户
                UserAction.unfrozenUser();
                showSiblingMenus(select);
                break;
            case "getUnfrozenApplyList"://查看用户解冻申请
                UserAction.getUnfrozenApplyList();
                showSiblingMenus(select);
                break;
            case "getOrderList"://查看订单
                UserAction.getOrderList();
                showSiblingMenus(select);
                break;
            case "getUserOrderList"://查看用户订单
                UserAction.getUserOrderList(currentUser.getUsername());
                showSiblingMenus(select);
                break;
            case "updateOrder"://修改订单
                UserAction.updateOrder();
                showSiblingMenus(select);
                break;
            case "cancelOrder"://取消订单
                UserAction.cancelOrder();
                showSiblingMenus(select);
            case "auditOrder"://审核订单
                UserAction.auditOrder();
                showSiblingMenus(select);
                break;
            case "orderSeatOnline"://在线订座
                UserAction.orderSeatOnline(currentUser.getUsername());
                showSiblingMenus(select);
                break;
        }
    }

    /**
     * 展示与所选择子菜单同级的子菜单列表
     * @param menu
     */
    private static void showSiblingMenus(Menu menu){
        Menu parent = menu.getParent();
        List<Menu> children = parent.getChildren();
        Menu[] menus = children.toArray(new Menu[children.size()]);
        showInterface(menus);
    }
}
