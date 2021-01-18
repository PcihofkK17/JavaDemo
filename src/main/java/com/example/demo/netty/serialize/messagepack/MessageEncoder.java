package com.example.demo.netty.serialize.messagepack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * 〈一句话功能简述〉
 * 编码器
 *
 * @author bf
 * @create 2018/3/15
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class MessageEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf2) throws Exception {
        MessagePack pack = new MessagePack();
        byte[] bytes = pack.write(msg);
        byteBuf2.writeBytes(bytes);
    }

}
