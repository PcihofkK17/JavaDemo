package com.example.demo.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeServerHandler implements Runnable {

    private int port;

    private CountDownLatch latch;

    /** 异步非阻塞 */
    private AsynchronousServerSocketChannel serverSocketChannel;

    public AsyncTimeServerHandler(int port) {
        this.port = port;

        try {
            // 打开通道
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            // 绑定端口
            serverSocketChannel.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // 同步辅助器，控制顺序
        latch = new CountDownLatch(1);



    }

    public void doAccept(){
    }
}
