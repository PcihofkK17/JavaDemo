package com.example.demo.netty.decoder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author bf
 * @create 2018/3/15
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class EchoClientHandler extends ChannelHandlerAdapter {

    private int count;

    static final String msg = "Hi hello world, Welcome to netty.&_";

    /**
     * 已 &_ 分隔符（没有的话，会发生粘包） 发送 10 次消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(msg.getBytes()));
        }
    }

    /**
     * 接收服务端发送过来的数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("This count is: " + ++count + "Echo rececive server: [" + msg +"]");
    }

    /**
     * 异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
