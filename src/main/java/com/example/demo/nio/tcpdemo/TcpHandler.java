package com.example.demo.nio.tcpdemo;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * 〈一句话功能简述〉
 * TCP 操作处理类
 *
 * @author bf
 * @create 2018/2/2
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface TcpHandler {

    /**
     *  处理 accept 事件
     * @param key
     */
    void handleAccept(SelectionKey key) throws IOException;

    /**
     * 处理 read 事件
     * @param key
     */
    void handleRead(SelectionKey key) throws IOException;

    /**
     * 处理 write 事件
     * @param key
     */
    void handleWrite(SelectionKey key) throws IOException;

}
