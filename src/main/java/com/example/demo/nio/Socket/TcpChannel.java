package com.example.demo.nio.Socket;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

final class TcpChannel
{
    private long endTime;
    private SelectionKey key;

    public TcpChannel(SelectableChannel channel, long endTime, int op) throws IOException
    {
        boolean done = false;
        Selector selector = null;
        this.endTime = endTime;
        try {
            selector = Selector.open();
            channel.configureBlocking(false);
            key = channel.register(selector, op);
            done = true;
        } finally {
            if (!done && selector != null) {
                selector.close();
            }
            if (!done) {
                channel.close();
            }
        }
    }

    static void blockUntil(SelectionKey key, long endTime) throws IOException
    {
        long timeout = endTime - System.currentTimeMillis();
        int nkeys = 0;
        if (timeout > 0) {
            nkeys = key.selector().select(timeout);
        } else if (timeout == 0) {
            nkeys = key.selector().selectNow();
        }
        if (nkeys == 0) {
            throw new SocketTimeoutException();
        }
    }

    void cleanup()
    {
        try {
            key.selector().close();
            key.channel().close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void bind(SocketAddress addr) throws IOException
    {
        SocketChannel channel = (SocketChannel) key.channel();
        channel.socket().bind(addr);
    }

    void connect(SocketAddress addr) throws IOException
    {
        SocketChannel channel = (SocketChannel) key.channel();

        key.interestOps(key.interestOps() | SelectionKey.OP_CONNECT);

        try {
            if (!key.isConnectable()) {
                blockUntil(key, endTime);
            }
            if (!channel.connect(addr) && !channel.finishConnect()) {
                throw new ConnectException();
            }
        } finally {
            if (key.isValid()) {
                key.interestOps(key.interestOps() & ~SelectionKey.OP_CONNECT);
            }
        }
    }

    void send(ByteBuffer buffer) throws IOException
    {
        Send.operate(key, buffer, endTime);
    }

    void recv(ByteBuffer buffer) throws IOException
    {
        Recv.operate(key, buffer, endTime);
    }
}
