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
 * API 详解：
 *  ServerBootstrap: 在netty服务中，用于引导服务端和客户端。
 *      服务端：负责接收客户端的链接，以及为已接受的链接创建子通道
 *      客户端：完成一些客户端相关操作
 *
 *  ServerBootstrap.group 绑定两个 EventLoopGroup 用于处理所有的 NIO 事件
 *                 .channel 绑定通道类型，使用 netty 包下的
 *                 .option  绑定操作类型，这个设置的操作类似，
 *                 .childHandler 绑定通道处理器，处理接收到的请求。且类一般继承 ChannelInitializer 类
 *                 .bind 绑定端口
 *
 *
 *
 *
 */
public class TimeServer {

    public void bind(final int port){
        /**
         * EventLoopGroup 是线程组，包含了一组 NIO 线程，专门用于网络线程处理。
         *
         *  bossGroup：用于服务端接受客户端的链接
         *  workerGroup：用于 SocketChannel 网络读写（处理接收到的链接）
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 启动 NIO 服务的辅助启动类
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
