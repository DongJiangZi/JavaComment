package com.cyx.cinema.sys.message;

import java.io.Serializable;

/**
 * 传输的信息
 */
public class Message<T> implements Serializable {
    /**
     * 动作，行为
     */
    private String action;
    /**
     * 数据
     */
    private T data;

    public Message(String action, T data) {
        this.action = action;
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return  action +" =>" + data;
    }
}
