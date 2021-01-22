package com.example.demo.netty.websocket;

/**
 * netty websocket 聊天室启动类
 */
public class Launcher {

    public static void main(String[] args) {
        new WebSocketServer().run(8080);
    }

}
