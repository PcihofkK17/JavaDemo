package com.example.demo.nio.Socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 *  接受数据
 *
 * Created by bf on 2018/2/1.
 */
class Recv implements Operator
{
    public int io(SocketChannel channel, ByteBuffer buffer) throws IOException
    {
        return channel.read(buffer);
    }
    
    public static final void operate(final SelectionKey key, final ByteBuffer buffer, final long endTime) throws IOException
    {
        Operation.operate(SelectionKey.OP_READ, key, buffer, endTime, operator);
    }
    public static final Recv operator = new Recv();
}