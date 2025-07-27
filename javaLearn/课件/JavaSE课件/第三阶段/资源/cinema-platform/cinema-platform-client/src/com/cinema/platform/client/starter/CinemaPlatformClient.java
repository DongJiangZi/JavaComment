package com.cinema.platform.client.starter;

import com.cinema.platform.client.action.UserAction;
import com.cinema.platform.client.menu.Menu;
import com.cinema.platform.client.menu.MenuManager;
import com.cinema.platform.client.util.InputUtil;
import com.cyx.cinema.platform.command.Command;
import com.cyx.cinema.platform.entity.FilmHall;
import com.cyx.cinema.platform.entity.Seller;
import com.cyx.cinema.platform.entity.User;

import java.util.Map;


/**
 * 影院平台客户端
 */
public class CinemaPlatformClient {

    private static User currentUser;

    public static void main(String[] args) {
        showInterface(MenuManager.LOGIN_MENUS);
    }

    /**
     * 展示给定的菜单
     * @param menus
     */
    private static void showInterface(Menu[] menus){
        MenuManager.showMenu(menus);
        int number = InputUtil.getInputInteger("请选择菜单编号：", 1, menus.length);
        Menu select = menus[number-1];
        switch (select.getCommand()){
            case Command.REGISTER://注册
                UserAction.register();
                showInterface(menus);
                break;
            case Command.ENTRY_APPLY://商家入驻
                UserAction.entryApply();
                showInterface(menus);
                break;
            case Command.VIEW_ENTRY_APPLY://查看入驻申请
                UserAction.viewEntryApply();
                showInterface(menus);
                break;
            case Command.LOGIN://登录
                login(menus);
                break;
            case Command.GET_PASSWORD_BACK://找回密码
                UserAction.getPasswordBack();
                showInterface(menus);
                break;
            case Command.UNFROZEN_APPLY://解冻申请
                UserAction.unfrozenApply();
                showInterface(menus);
                break;
            case Command.VIEW_SELLERS://查看商家
                UserAction.viewSellers();
                showInterface(menus);
                break;
            case Command.AUDIT_SELLER://审核商家
                UserAction.auditSeller();
                showInterface(menus);
                break;
            case Command.FROZEN_SELLER://冻结商家
                UserAction.frozenSeller();
                showInterface(menus);
                break;
            case Command.VIEW_USERS://查看用户
                UserAction.viewUsers();
                showInterface(menus);
                break;
            case Command.FROZEN_USER: //冻结用户
                UserAction.frozenUser();
                showInterface(menus);
                break;
            case Command.VIEW_UNFROZEN_APPLIES://查看解冻申请
                UserAction.viewUnfrozenApplies();
                showInterface(menus);
                break;
            case Command.AUDIT_UNFROZEN_APPLY://审批解冻申请
                UserAction.auditUnfrozenApply();
                showInterface(menus);
                break;
            case Command.ADD_FILM://添加影片
                UserAction.addFilm(currentUser.getUsername());
                showInterface(menus);
                break;
            case Command.UPDATE_FILM://更新影片
                UserAction.updateFilm(currentUser.getUsername());
                showInterface(menus);
                break;
            case Command.DELETE_FILM://删除影片
                UserAction.deleteFilm(currentUser.getUsername());
                showInterface(menus);
                break;
            case Command.VIEW_FILMS://查看影片
                UserAction.viewFilms(currentUser.getUsername());
                showInterface(menus);
                break;
            case Command.ADD_FILM_HALL://添加影厅
                UserAction.addFilmHall(currentUser.getUsername());
                showInterface(menus);
                break;
            case Command.UPDATE_FILM_HALL://更新影厅
                UserAction.updateFilmHall(currentUser.getUsername());
                showInterface(menus);
                break;
            case Command.DELETE_FILM_HALL://删除影厅
                UserAction.deleteFilmHall(currentUser.getUsername());
                showInterface(menus);
                break;
            case Command.VIEW_FILM_HALLS://查看影厅
                UserAction.viewFilmHalls(currentUser.getUsername());
                showInterface(menus);
                break;
            case Command.ADD_FILM_PLAN://添加影片计划
                UserAction.addFilmPlan((Seller)currentUser);
                showInterface(menus);
                break;
            case Command.UPDATE_FILM_PLAN://更新影片计划
                UserAction.updateFilmPlan((Seller)currentUser);
                showInterface(menus);
                break;
            case Command.DELETE_FILM_PLAN://删除影片计划
                UserAction.deleteFilmPlan((Seller)currentUser);
                showInterface(menus);
                break;
            case Command.VIEW_FILM_PLANS://查看影片计划
                UserAction.viewFilmPlans(currentUser);
                showInterface(menus);
                break;
            case Command.ORDER_SEAT_ONLINE://在线订座
                UserAction.orderSeatOnline(currentUser.getUsername());
                showInterface(menus);
                break;
            case Command.VIEW_ORDERS://查看订单
                UserAction.viewOrders(currentUser);
                showInterface(menus);
                break;
            case Command.UPDATE_ORDER://修改订单
                UserAction.updateOrder(currentUser.getUsername());
                showInterface(menus);
                break;
            case Command.CANCEL_ORDER://取消订单
                UserAction.cancelOrder(currentUser.getUsername());
                showInterface(menus);
                break;
            case Command.GO_BACK_MAIN:
                showUserMenus();
                break;
            case Command.SHOW_CHILDREN:
                showInterface(select.getChildren());
                break;
            case Command.GO_BACK_LOGIN:
                showInterface(MenuManager.LOGIN_MENUS);
                break;
            case Command.QUIT:
                System.out.println("感谢使用影院选票平台");
                System.exit(0);
                break;
        }
    }

    /**
     * 登录
     * @param menus
     */
    private static void login(Menu[] menus){
        Map<String,Object> data = UserAction.login();
        if(data == null){
            System.out.println("账号或密码错误，请重新登录");
        } else {
            int result = (int) data.get("result");
            if(result == 0){
                System.out.println("账号或密码错误，请重新登录");
                showInterface(menus);
            } else if(result == 1){
                currentUser = (User) data.get("user");
                showUserMenus();
            } else if(result == 2){
                System.out.println("当前账号正在审批，请耐心等待");
                showInterface(menus);
            } else if(result == 3){
                System.out.println("账号已被冻结，请解冻后再登录");
                showInterface(menus);
            } else {
                System.out.println("账号不存在，请先注册");
                showInterface(menus);
            }
        }
    }

    /**
     * 展示当前用户的菜单
     */
    private static void showUserMenus(){
        User.Role role = currentUser.getRole();
        Menu[] mainMenus;
        if(role == User.Role.MANAGER){
            mainMenus = MenuManager.MANAGER_MENUS;
        } else if(role == User.Role.SELLER){
            mainMenus = MenuManager.SELLER_MENUS;
        } else {
            mainMenus = MenuManager.USER_MENUS;
        }
        showInterface(mainMenus);
    }
}