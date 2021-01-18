package com.example.demo.netty.serialize.messagepack;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 〈一句话功能简述〉
 * Echo server 处理器
 *
 * @author bf
 * @create 2018/3/15
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class MessagePackServerHandler extends ChannelHandlerAdapter{

    /**
     * 接收客户端 发送过来的信息
     *  由于配置了解码器，所以可以直接转换
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("receive message is userInfo name is: " + msg.toString());

        /**
         * 因为设置了解码器，所以这里面使用 ByteBuf 发送数据的话，会造成数据发送失败，发送不了客户端
         *
         * ByteBuf buf = Unpooled.copiedBuffer(("hasd receive").getBytes());
         */
        ctx.write("hasd receive");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * 发生异常的时候 处理
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
