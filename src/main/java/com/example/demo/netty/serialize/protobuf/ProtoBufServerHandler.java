package com.example.demo.netty.serialize.protobuf;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerInvoker;
import io.netty.util.concurrent.EventExecutorGroup;

public class ProtoBufServerHandler extends ChannelHandlerAdapter{


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;
        String body = req.getUserName() + "的地址为: " + req.getAddressList().toString();
        System.out.println(body);
        ctx.write(resp(req.getSubReqID()));
    }

    public SubscribeReqProto.SubscribeReq resp(int suqId){
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setUserName("Baofang");
        builder.setProductName("netty book is my");
        builder.setSubReqID(suqId);
        return builder.build();
    }

    /**
     * 读取完成后 将返回数据推送过去
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
