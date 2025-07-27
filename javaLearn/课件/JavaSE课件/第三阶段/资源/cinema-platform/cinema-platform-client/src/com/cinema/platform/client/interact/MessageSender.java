package com.cinema.platform.client.interact;

import com.cyx.cinema.platform.message.Message;
import com.cyx.cinema.platform.util.SocketUtil;

import java.io.IOException;
import java.net.Socket;

/**
 * 消息发送器
 */
public class MessageSender {

    private static final String IP = "localhost";

    private static final int PORT = 8888;

    /**
     * 客户端发送消息
     * @param command
     * @param data
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T,V> T sendMsg(int command, V data){
        try {
            Socket socket = new Socket(IP, PORT);
            Message<V> msg = new Message<>(command, data);
            SocketUtil.sendMsg(socket, msg);
            return SocketUtil.receiveMsg(socket);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
