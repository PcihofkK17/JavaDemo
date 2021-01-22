package com.example.demo.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * netty websocket 聊天室 服务类
 *
 * API 详解:
 *  1: HttpServerCodec: 将请求或应答消息，编码或解码为 HTTP 消息
 *  2: HttpObjectAggregator: 目的是将 Http 的多个部分组合成一条完整的 Http 消息(HTTP 消息组装)
 *  3: ChunkedWriteHandler: 像客户端发送 HTML5 文件, 主要是用来处理大数据流，如果直接传送 1G 的文件，很可能蹭爆JVM
 *                          但是加了这个就没事了。(Http 消息通信支持)
 */
public class WebSocketServer {

    /**
     * 启动服务
     * @param port
     */
    public void run(final int port){
        EventLoopGroup boss = new NioEventLoopGroup();

        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("http-codec", new HttpServerCodec());
                            pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                            pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                            pipeline.addLast("handler", new BananaWebSocketServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();

            System.out.println("netty websocket 聊天室启动，端口：" + port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
