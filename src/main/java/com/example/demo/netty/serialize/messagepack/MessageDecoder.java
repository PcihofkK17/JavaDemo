package com.example.demo.netty.serialize.messagepack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * 〈一句话功能简述〉
 * MessageDecoder 解码器
 *
 * @author bf
 * @create 2018/3/15
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {

    /**
     * 解码
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // readableBytes 读取可读字节
        int length = byteBuf.readableBytes();
        final byte[] array = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);
        MessagePack messagePack = new MessagePack();
        // MessagePack.read 反序列化数组，并加入到反序列化解码列表 list 当中
        list.add(messagePack.read(array));
    }
}
