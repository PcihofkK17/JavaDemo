package com.example.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 〈一句话功能简述〉
 * SocketChannel demo
 *
 * 一：选择器处理的事件类型
 *  1、connect：客户端连接服务端事件，对应值为SelectionKey.OP_CONNECT(8)
 *  2、accept：服务端接收客户端连接事件，对应值为SelectionKey.OP_ACCEPT(16)
 *  3、read：读事件，对应值为SelectionKey.OP_READ(1)
 *  4、write：写事件，对应值为SelectionKey.OP_WRITE(4)
 *
 * @author bf
 * @create 2018/2/1
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class SocketChannelDemo {

    private static final String HOST = "localhost";

    private static final int PORT = 9999;

    private static ExecutorService read = Executors.newFixedThreadPool(5);

    private static ExecutorService write = Executors.newFixedThreadPool(5);

    public static void main(String[] args){
        ServerSocketChannel serverSocketChannel = null;
        ServerSocket socket;
        Selector selector;
        try {

            serverSocketChannel = ServerSocketChannel.open();
            // 获取 channel 对应的 SocketChannel
            socket = serverSocketChannel.socket();
            // serverSocketChannel.bind(new InetSocketAddress(PORT));
            // 绑定 host 和 port
            socket.bind(new InetSocketAddress(HOST, PORT));
            // 开启非阻塞模式
            serverSocketChannel.configureBlocking(false);
            // 工厂方法获取 Selector
            selector = Selector.open();
            // 通道在注册到选择器上面，接收链接就绪状态
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true){
                // 循环检查,阻塞检查，当有就绪状态时，执行后续操作
                // 没有客户端连接的话，会一直阻塞
                int select = selector.select();
                if(select == 0){
                    continue;
                }
                // 获取所有就绪建的 SelectionKey ，注册在选择器上面的
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                // 获取 SelectionKey
                while (it.hasNext()){

                    SelectionKey key = it.next();

                    // 处理接收
                    if(key.isAcceptable()){
                        // 只负责阻塞，接收，管理，不发送和接收是数据
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();

                        // 获取相对应的 SocketChannel
                        SocketChannel socketChannel = channel.accept();
                        if(socketChannel == null){
                            continue;
                        }
                        // 开启异步操作
                        socketChannel.configureBlocking(false);
                        // 通道注册选择器，通知通道准备好读取数据
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }
                    // 处理 可读状态
                    if(key.isReadable()){
                        // 获取通道
                        SocketChannel channel = (SocketChannel) key.channel();
                        // 多线程执行
                        read.execute(new MyReadRunable(channel));
                        /*// 创建缓冲区
                        ByteBuffer buffer = ByteBuffer.allocate(4 * 1024);

                        // 接收数据
                        StringBuffer sb = new StringBuffer();
                        // 保证读取完数据
                        while(channel.read(buffer) > 0){
                            // 切换可读状态
                            buffer.flip();
                            sb.append(new String(buffer.array()));
                            // 读取完清空
                            buffer.clear();
                        }
                        System.out.println("server receive: " + sb.toString());*/
                        // 注册可写
                        channel.register(selector, SelectionKey.OP_WRITE);
                    }

                    // 处理可写状态
                    if(key.isWritable()){
                        SocketChannel channel = (SocketChannel) key.channel();
                        // 多线程操作
                        write.execute(new MyWriteRunnable(channel));

                        // 单线程操作
/*                        String data = "server send data: " + Math.random();
                        // 向缓冲区里面放入数据， 也可用 put
                        ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
                        // 如果还有数据的话
                        while(buffer.hasRemaining()){
                            // 通道写入数据
                            channel.write(buffer);
                        }
                        System.out.println(data);*/

                        // 将这个 通道 channel 注册到选择器上面，并注册可读事件
                        channel.register(selector, SelectionKey.OP_READ);
                    }
                }

                it.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
    }

    /**
     *  多线程 读取
     */
    static class MyReadRunable implements Runnable{

        private SocketChannel socketChannel;

        public MyReadRunable(SocketChannel channel) {
            socketChannel = channel;
        }

        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocate(4 * 1024);
            StringBuffer sb = new StringBuffer();
            try {
                while(socketChannel.read(buffer) > 0){
                    buffer.flip();
                    sb.append(new String(buffer.array()));
                    buffer.clear();
                }
                System.out.println("server receive: " + sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 多线程写入
     */
    static class MyWriteRunnable implements Runnable{

        private SocketChannel socketChannel;

        public MyWriteRunnable(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            String data = "server send data: " + Math.random();
            ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
            try {
                while(buffer.hasRemaining()){
                    socketChannel.write(buffer);
                }
                System.out.println(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
