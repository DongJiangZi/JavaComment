package com.cyx.cinema.platform.entity;
//ER图 Entity Relational
//实体包常用包名：entity 实体 pojo(plain ordinary java object)简单java对象
//bo(business object) 业务对象 vo(view object) 视图对象 dto(data transfer object) 数据传输对象
//domain 领域模型  model 数据模型

import java.io.Serializable;

/**
 * 用户
 */
public class User implements Serializable {
    /**
     * 用户角色：普通用户、管理员、商家
     */
    public enum Role{
        USER,MANAGER,SELLER
    }

    /**
     * 用户名
     */
    protected String username;
    /**
     * 密码
     */
    protected String password;
    /**
     * 安全码
     */
    protected String securityCode;
    /**
     * 角色
     */
    protected Role role;
    /**
     * 账号状态：-1-审核不通过，0-待审核，1-正常，2-冻结
     */
    protected int state;

    public User(String username, String password, String securityCode) {
        this.username = username;
        this.password = password;
        this.securityCode = securityCode;
        this.role = Role.USER;//默认为普通用户
        this.state = 1;//状态默认为正常
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public Role getRole() {
        return role;
    }

    public int getState() {
        return state;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        String roleStr = role == Role.MANAGER ? "管理员" : "普通用户";
        String stateStr = state == 1 ? "正常" : "冻结";
        return username + "\t" + roleStr + "\t" + stateStr;
    }
}
