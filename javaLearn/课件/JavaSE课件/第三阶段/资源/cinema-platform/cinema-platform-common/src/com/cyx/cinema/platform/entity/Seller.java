package com.cyx.cinema.platform.entity;

/**
 * 商家
 */
public class Seller extends User {
    /**
     * 影院名称
     */
    private String cinemaName;
    /**
     * 影院地址
     */
    private String cinemaAddress;

    public Seller(String username, String password, String securityCode, String cinemaName, String cinemaAddress) {
        super(username, password, securityCode);
        this.cinemaName = cinemaName;
        this.cinemaAddress = cinemaAddress;
        this.role = Role.SELLER; //默认是商家
        this.state = 0;//默认状态为待审核
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public String getCinemaAddress() {
        return cinemaAddress;
    }

    @Override
    public String toString() {
        String stateStr = state == -1 ? "审核不通过" : state == 0 ? "待审核" : state == 1 ? "正常" : "冻结";
        return username + "\t" + cinemaName + "\t" + cinemaAddress + "\t商家" + "\t" + stateStr;
    }
}
