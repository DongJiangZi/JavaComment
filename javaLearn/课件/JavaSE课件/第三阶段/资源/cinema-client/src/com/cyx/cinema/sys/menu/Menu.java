package com.cyx.cinema.sys.menu;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单
 */
public class Menu {
    /**
     * 菜单编号
     */
    private int order;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 触发的行为
     */
    private String action;
    /**
     * 子菜单列表
     */
    private List<Menu> children = new ArrayList<>();
    /**
     * 父菜单
     */
    private Menu parent;

    public Menu(int order, String name, String action) {
        this(order, name, action, null);
    }

    public Menu(int order, String name, String action, Menu parent) {
        this.order = order;
        this.name = name;
        this.action = action;
        this.parent = parent;
    }

    public Menu getParent() {
        return parent;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public String getAction() {
        return action;
    }

    /**
     * 添加子菜单
     * @param child
     */
    public void addChild(Menu child){
        children.add(child);
    }

    @Override
    public String toString() {
        return order + "." + name;
    }
}
