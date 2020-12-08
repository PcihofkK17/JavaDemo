package com.example.demo.nio.tcpdemo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author bf
 * @create 2018/2/2
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class TcpHandlerImpl implements TcpHandler {

    private static final String CHARSET = "UTF-8";

    @Override
    public void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        // 服务端 获取客户端请求的通道  SocketChannel
        SocketChannel accept = channel.accept();
        accept.configureBlocking(false);
        // 绑定可读事件，选择器的使用必须是 非阻塞模式
        accept.register(key.selector(), SelectionKey.OP_READ);
    }

    @Override
    public void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        channel.configureBlocking(false);

        ByteBuffer buffer = ByteBuffer.allocate(4 * 1024);
        StringBuffer sb = new StringBuffer();
        // 将通道中的数据读取到 buffer
        if (channel.read(buffer) > 0){
            buffer.flip();
            sb.append(new String(buffer.array()));
            buffer.clear();
        }else{
            System.out.println("客户端关闭");
            key.cancel();
        }
        System.out.println("从客户端接收的数据为：" + sb.toString());

        // 读取到数据后 回应
        String sendMsg = "侬好客户端：" + new Date().toString() + "已接受到你发送的数据：" + sb.toString();

        ByteBuffer wrap = ByteBuffer.wrap(sendMsg.getBytes("UTF-8"));
        channel.write(wrap);

        // 为下一次读写做准备 ，注册读写事件
        channel.register(key.selector(), SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        String sengMsg = "server send msg: " + Math.random();
        ByteBuffer buffer = ByteBuffer.wrap(sengMsg.getBytes());
        while (buffer.hasRemaining()){
            channel.write(buffer);
        }
        System.out.println("sengMsg: " + sengMsg);
        channel.register(key.selector(), SelectionKey.OP_READ);
    }
}
