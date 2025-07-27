package com.cyx.cinema.sys.util;

import com.cyx.cinema.sys.message.Message;

import java.io.*;
import java.net.Socket;

/**
 * 套接字工具类
 */
public class SocketUtil {

    /**
     * 读取客户端发送的信息
     * @param client 客户端套接字
     * @param <T> 因为不知道客户端发送的信息携带的是什么数据类型，因此使用泛型
     * @return
     */
    public static<T> Message<T> receiveMsg(Socket client){
        try {
            InputStream is = client.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            Message<T> msg = (Message<T>) ois.readObject();
            client.shutdownInput();
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 向客户端返回处理结果
     * @param client 客户端套接字
     * @param data 处理结果
     * @param <V> 因为不清楚处理结果是什么数据类型，因此使用泛型
     */
    public static <V> void sendBack(Socket client, V data){
        try {
            OutputStream os = client.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(data);
            oos.flush();
            client.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
