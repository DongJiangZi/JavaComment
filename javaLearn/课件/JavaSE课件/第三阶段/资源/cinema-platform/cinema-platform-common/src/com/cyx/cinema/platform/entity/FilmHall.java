package com.cyx.cinema.platform.entity;

import java.io.PrintStream;
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
    /**
     * 拥有者
     */
    private String owner;

    public FilmHall(String name, int totalRow, int totalCol, String owner) {
        this.name = name;
        this.totalRow = totalRow;
        this.totalCol = totalCol;
        this.owner = owner;
        this.seats = new Seat[totalRow][totalCol];
        for(int i=0; i<totalRow; i++){
            for(int j=0; j<totalCol; j++){
                this.seats[i][j] = new Seat(i, j);
            }
        }
    }

    /**
     * 检测给定排号和列号的座位是否已经被订购
     * @param row
     * @param col
     * @return
     */
    public boolean hasOwner(int row, int col){
        return seats[row][col].getOwner() != null;
    }

    /**
     * 换座
     * @param originalRow
     * @param originalCol
     * @param row
     * @param col
     */
    public void changeSeat(int originalRow, int originalCol, int row, int col){
        String owner = seats[originalRow][originalCol].getOwner();//获取原来座位的用户
        seats[originalRow][originalCol].setOwner(null);//座位所属用户置空，表示未被订购
        seats[row][col].setOwner(owner);//新的座位设置所属用户
    }
    /**
     * 获取余票
     * @return
     */
    public int getRestTickets(){
        int tickets = 0;
        for(Seat[] seatArr : seats){
            for(Seat seat : seatArr){
                if(seat.getOwner() == null){
                    tickets++;
                }
            }
        }
        return tickets;
    }

    /**
     * 订购座位
     * @param row
     * @param col
     * @param owner
     */
    public void orderSeat(int row, int col, String owner){
        seats[row][col].setOwner(owner);
    }

    /**
     * 取消订座
     * @param row
     * @param col
     */
    public void cancelSeat(int row, int col){
        seats[row][col].setOwner(null);
    }
    /**
     * 显示座位信息
     */
    public void showSeatInfo(){
        System.out.print(" ");
        for(int j=0; j<totalCol; j++){
            System.out.print(" " + j + " ");
        }
        System.out.println();
        try {
            for(int i=0; i<totalRow; i++){
                System.out.print(i);
                for(int j=0; j<totalCol; j++){
                    PrintStream ps = seats[i][j].getOwner() == null ? System.out : System.err;
                    ps.print(seats[i][j]);
                    Thread.sleep(20L);
                }
                System.out.println();
            }
        } catch (InterruptedException e) {
        }
    }

    public int getTotalRow() {
        return totalRow;
    }

    public int getTotalCol() {
        return totalCol;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return id + "\t" + name + "\t" + totalRow + "\t" + totalCol;
    }
}
