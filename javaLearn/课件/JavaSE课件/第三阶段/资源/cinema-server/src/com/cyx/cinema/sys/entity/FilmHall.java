package com.cyx.cinema.sys.entity;

import java.io.Serializable;

/**
 * 影厅
 */
public class FilmHall implements Serializable {
    /**
     * 编号
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 总排数
     */
    private int totalRow;
    /**
     * 总列数
     */
    private int totalCol;
    /**
     * 座位列表
     */
    private Seat[][] seats;

    public FilmHall(String id, String name, int totalRow, int totalCol) {
        this.id = id;
        this.name = name;
        this.totalRow = totalRow;
        this.totalCol = totalCol;
        this.seats = new Seat[totalRow][totalCol];
        for(int i=0;i<totalRow; i++){
            for(int j=0; j<totalCol; j++){
                this.seats[i][j] = new Seat(i, j);
            }
        }
    }

    /**
     * 获取余票
     * @return
     */
    public int getRestTicket(){
        int totalTicket = 0;
        for(Seat[] seatArr : seats){
            for(Seat seat: seatArr){
                if(seat.getOwner() == null){
                    totalTicket++;
                }
            }
        }
        return totalTicket;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public int getTotalCol() {
        return totalCol;
    }

    public void setTotalCol(int totalCol) {
        this.totalCol = totalCol;
    }

    /**
     * 给定排号和列号的座位是否已经售卖
     * @param row
     * @param col
     * @return
     */
    public boolean hasOwner(int row, int col){
        return seats[row][col].getOwner() != null;
    }

    /**
     * 给定排号和列号的座位设定用户
     * @param row
     * @param col
     * @param username
     */
    public void setOwner(int row, int col, String username){
        seats[row][col].setOwner(username);
    }

    /**
     * 展示座位信息
     */
    public void showSeats(){
        System.out.print(" ");
        for(int i=0; i<totalCol; i++){
            System.out.print(" " + i + " ");
        }
        System.out.println();
        for(int i=0;i<totalRow; i++){
            System.out.print(i);
            for(int j=0; j<totalCol; j++){
                Seat seat = seats[i][j];
                if(seat.getOwner() == null){
                    System.out.print(seat);
                } else {
                    System.err.print(seat);
                }
                try {
                    Thread.sleep(20L);
                } catch (InterruptedException e) {
                }
            }
            System.out.println();
        }
    }

    @Override
    public String toString() {
        return id + "\t" + name + "\t" + totalRow * totalCol;
    }
}
