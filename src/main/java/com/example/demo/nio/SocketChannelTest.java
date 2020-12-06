package com.example.demo.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * SocketChannel 连接到 TCP 网络套接字的通道
 * ServerSocketChannel 监听新进来的 TCP 连接的通道，
 *
 * Created by bf on 2018/1/31.
 */
public class SocketChannelTest {


    /**
     * TCP 客户端
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("http://127.0.0.1", 8989));

        // 切换成非阻塞模式
        socketChannel.configureBlocking(false);


        Scanner scanner = new Scanner(System.in);

        String sss = scanner.nextLine();
        // 将数据放入缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(sss.getBytes());

        buffer.flip();

        while (buffer.hasRemaining()){
            socketChannel.write(buffer);
        }

    }

    /**
     * TCP 服务端
     */
    @Test
    public void test() throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        // 绑定端口号
        channel.socket().bind(new InetSocketAddress(8989));

        // 异步
        channel.configureBlocking(false);

        while (true){
            // 获取客户端
            SocketChannel accept = channel.accept();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 返回值 代表读取到了多少个字节
            int write = accept.read(buffer);

            System.out.println("size: " + write);

            buffer.flip();

            while(buffer.hasRemaining()){
                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes);
                // 将缓冲区里面的数据放入到 byte 中
                System.out.println(new String(bytes));
            }
        }

    }

}
