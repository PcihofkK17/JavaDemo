package com.example.demo.nio.Socket;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

interface Operator
{
    class Operation
    {
        static void operate(final int op, final SelectionKey key, final ByteBuffer buffer, final long endTime, final Operator optr) throws IOException
        {
            final SocketChannel channel = (SocketChannel) key.channel();
            // 获取 缓冲区的 capacity 容量
            final int total = buffer.capacity();
            // 将此键的 interest 集合设置为给定值
            key.interestOps(op);
            try {
                while (buffer.position() < total) {
                    if (System.currentTimeMillis() > endTime) {
                        throw new SocketTimeoutException();
                    }
                    if ((key.readyOps() & op) != 0) {
                        if (optr.io(channel, buffer) < 0) {
                            throw new EOFException();
                        }
                    } else {
                        TcpChannel.blockUntil(key, endTime);
                    }
                }
            } finally {
                if (key.isValid()) {
                    key.interestOps(0);
                }
            }
        }
    }

    int io(SocketChannel channel, ByteBuffer buffer) throws IOException;
}