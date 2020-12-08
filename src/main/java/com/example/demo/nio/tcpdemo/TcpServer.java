package com.example.demo.nio.tcpdemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * 〈一句话功能简述〉
 * TCP 服务端
 *
 * @author bf
 * @create 2018/2/2
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class TcpServer {

    /** 服务端超时时间 单位 ms */
    private static final int TIME_OUT = 30000;

    /** 缓冲区大小 */
    private static final int BUFFER_SIZE = 1024;

    /** 服务端 port */
    private static final int PORT = 8989;

    public static void main(String[] args) throws IOException {
        // 获取选择器
        final Selector selector = Selector.open();
        // 打开监听通道
        final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // TCP 服务端绑定端口号
        serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
        // 开启非阻塞模式
        serverSocketChannel.configureBlocking(false);
        // 将通道绑定到选择器上面,绑定 ACCEPT(接收)事件，接收来自客户端的请求
        // 只有非阻塞式的才能绑定选择器
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        TcpHandler tcpHandler = new TcpHandlerImpl();

        while (true){
            try {
                //  wait an event
                if(selector.select(TIME_OUT) == 0){
                    System.out.println("独自等待");
                    continue;
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    // 操作完一个，需要移除 当前操作的 selectionKey
                    iterator.remove();

                    if(selectionKey.isAcceptable()){
                        tcpHandler.handleAccept(selectionKey);
                    }
                    if( selectionKey.isValid() && selectionKey.isReadable()){
                        tcpHandler.handleWrite(selectionKey);
                    }
                    if(selectionKey.isValid() && selectionKey.isReadable()){
                        tcpHandler.handleRead(selectionKey);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
