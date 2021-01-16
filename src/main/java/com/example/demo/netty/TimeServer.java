package com.example.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty server
 *
 */
public class TimeServer {

    public void bind(final int port){
        /**
         * EventLoopGroup 是线程组，包含了一组 NIO 线程，专门用于网络线程处理。
         *
         *  bossGroup：用于服务端接受客户端的链接
         *  workerGroup：用于 SocketChannel 网络读写
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());

            // 绑定端口 同步等待成功 ，同步阻塞方法
            ChannelFuture future = b.bind(port).sync();

            // 等待服务器监听端口关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 推出 释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new TimeServer().bind(8080);
    }

}
