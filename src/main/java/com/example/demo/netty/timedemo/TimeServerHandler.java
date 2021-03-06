package com.example.demo.netty.timedemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;


public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    private int count;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // TCP 粘包代码
/*        // 类似于 NIO
        ByteBuf buffer = (ByteBuf) msg;
        // readableBytes() 获取缓冲区的可读的字节数
        byte[] bytes = new byte[buffer.readableBytes()];
        // 将 缓冲区
        buffer.readBytes(bytes);

        // String body = new String(bytes, "UTF-8");
        String body = new String(bytes, "UTF-8").substring(0, bytes.length
                - System.getProperty("line.separator").length());

        System.out.println("the time server receive order: " + body + "; the count is: " + ++count);

        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
                ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";

        currentTime = currentTime + System.getProperty("line.separator");

        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());

        // 异步发送到应答消息给客户端
        ctx.write(resp);*/

        // TCP 不粘包拆包代码
        String body = (String) msg;
        System.out.println("the time server receive order: " + body + "; the count is: " + ++count);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
                ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
        currentTime = currentTime + System.getProperty("line.separator");

        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

