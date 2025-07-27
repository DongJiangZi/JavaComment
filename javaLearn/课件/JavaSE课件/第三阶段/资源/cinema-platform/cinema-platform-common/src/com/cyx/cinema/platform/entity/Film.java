package com.cyx.cinema.platform.entity;

import java.io.Serializable;

/**
 * 影片
 */
public class Film implements Serializable {
    /**
     * 编号
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 主演
     */
    private String actor;
    /**
     * 描述
     */
    private String description;
    /**
     * 拥有者
     */
    private String owner;

    public Film(String name, String actor, String description, String owner) {
        this.name = name;
        this.actor = actor;
        this.description = description;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public String getActor() {
        return actor;
    }

    public String getOwner() {
        return owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id + "\t" + name + "\t" + actor + "\t" + description;
    }
}
