package com.example.demo.netty.serialize.messagepack;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 〈一句话功能简述〉
 * echo client
 *
 * @author bf
 * @create 2018/3/15
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class MessagePackClient {

    public static void main(String[] args){
        new MessagePackClient().connect("127.0.0.1", 9999);
    }


    public void connect(final String host, final int port){
        // 客户端 NIO 线程组
        EventLoopGroup work = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(work).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //这里设置通过增加包头表示报文长度来避免粘包
                            socketChannel.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(1024, 0, 2,0,2));
                            // 添加 MessagePack 编码器和解码器
                            socketChannel.pipeline().addLast("message decoder", new MessageDecoder());
                            //这里设置读取报文的包头长度来避免粘包
                            socketChannel.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
                            socketChannel.pipeline().addLast("message encoder", new MessageEncoder());
                            socketChannel.pipeline().addLast(new MessagePackClientHandler(50));
                        }
                    });
            // 发起异步链接操作
            ChannelFuture future = bootstrap.connect(host, port).sync();
            // 等待客户端链路关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            work.shutdownGracefully();
        }


    }
}
