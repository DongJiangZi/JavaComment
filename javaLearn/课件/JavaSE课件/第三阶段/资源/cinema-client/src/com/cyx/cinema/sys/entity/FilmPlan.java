package com.cyx.cinema.sys.entity;

import com.cyx.cinema.sys.util.DateUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 影片播放计划
 */
public class FilmPlan implements Serializable {
    /**
     * 编号
     */
    private String id;
    /**
     * 影片
     */
    private Film film;
    /**
     * 影厅
     */
    private FilmHall filmHall;
    /**
     * 开始时间
     */
    private Date begin;
    /**
     * 结束时间
     */
    private Date end;


    public FilmPlan(String id, Film film, FilmHall filmHall, Date begin, Date end) {
        this.id = id;
        this.film = film;
        this.filmHall = filmHall;
        this.begin = begin;
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public Film getFilm() {
        return film;
    }

    public FilmHall getFilmHall() {
        return filmHall;
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public void setFilmHall(FilmHall filmHall) {
        this.filmHall = filmHall;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString() {
        String desc = film == null ? "" : film.getDescription();
        String hallName = filmHall == null ? "" : filmHall.getName();
        int restTicket = filmHall == null ? 0 : filmHall.getRestTicket();
        return id + "\t" + film.getName() + "\t" + desc + "\t" + hallName
                + "\t" + DateUtil.date2str(begin) + "\t" + DateUtil.date2str(end) + "\t" + restTicket;
    }
}
