package com.example.demo.netty.serialize.messagepack;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 〈一句话功能简述〉
 * DelimiterBasedFrameDecoder 解码器测试
 *  用法和 LineBasedFrameDecoder 类似，只是 LineBasedFrameDecoder 是针对换行的，
 *  DelimiterBasedFrameDecoder 可以自定义结束位置
 *
 * FixedLengthFrameDecoder 固定长度解码器
 *
 * API详解(@link https://www.cnblogs.com/googlemeoften/p/6082785.html):
 *  1: ChannelOption.SO_BACKLOG 对应的是 TCP/IP 协议listen函数中的 backlog,
 *      多个客户端来的时候，服务端不能即使处理，就把客户端的请求放入列队中等待处理，backlog 指定了列队的长度
 *  2: ChannelOption.TCP_NODELAY 该参数的使用和 Nagle算法有关
 *      Nagle算法 将小的数据包，拼接成更大的数据包，然后发送。
 *
 *  3: ChannelOption.SO_KEEPALIVE 对应套接字选项中的 SO_KEEPLIVE, 设置TCP参数，设置之后，会检测连接状态，如果2小时未连接
 *      会发送一个测试数据报文
 *
 *  4:
 *
 *
 * @author bf
 * @create 2018/3/15
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class MessagePackServer {

    public static void main(String[] args){
        new MessagePackServer().bind(9999);
    }

    /**
     * 绑定端口
     * @param port
     */
    public void bind(final int port){
        // 接收 NIO 请求线程组
        EventLoopGroup boos = new NioEventLoopGroup();

        EventLoopGroup work = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(boos, work).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            //这里设置通过增加包头表示报文长度来避免粘包
                            channel.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(
                                    1024, 0,
                                    2,0,2));
                            // 添加 MessagePack 编码器和解码器
                            channel.pipeline().addLast("message decoder", new MessageDecoder());
                            //这里设置读取报文的包头长度来避免粘包
                            channel.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
                            channel.pipeline().addLast("message encoder", new MessageEncoder());
                            channel.pipeline().addLast(new MessagePackServerHandler());
                        }
                    });
            // 绑定端口，同步等待成功
            ChannelFuture future = bootstrap.bind(port).sync();
            // 等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 释放资源
            boos.shutdownGracefully();
            work.shutdownGracefully();
        }

    }

}
