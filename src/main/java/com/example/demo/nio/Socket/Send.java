package com.example.demo.nio.Socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 *  发送数据
 *
 * Created by bf on 2018/2/1.
 */
class Send implements Operator
{
    public int io(SocketChannel channel, ByteBuffer buffer) throws IOException
    {
        return channel.write(buffer);
    }
    public static final void operate(final SelectionKey key, final ByteBuffer buffer, final long endTime) throws IOException
    {
        Operation.operate(SelectionKey.OP_WRITE, key, buffer, endTime, operator);
    }
    public static final Send operator = new Send();
}