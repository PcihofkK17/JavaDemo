package com.example.demo.netty.timedemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 测试 TCP 粘包、拆包
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private int count;

    private byte[] req;

    // private final ByteBuf firstMessage;

    public TimeClientHandler() {
        req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
        // 将数据读取到 ByteBuf 中
        // firstMessage = Unpooled.buffer(req.length);

        // firstMessage.writeBytes(req);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message = null;
        for (int i = 0; i < 100; i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }

        // ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // TCP 粘包拆包
/*        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        // 读取信息
        byteBuf.readBytes(bytes);
        String body = new String(bytes, "UTF-8");*/

        String body = (String) msg;
        System.out.println("now is: " + body + "; the count is: " + ++count);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 释放资源
        ctx.close();
    }
}
