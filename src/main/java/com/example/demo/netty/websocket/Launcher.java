package com.example.demo.netty.websocket;

/**
 * 〈一句话功能简述〉
 * netty websocket 聊天室启动类
 *
 * @author bf
 * @create 2018/3/19
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class Launcher {

    public static void main(String[] args) {
        new WebSocketServer().run(9090);
    }

}
