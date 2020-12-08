package com.example.demo.nio.tcpdemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 〈一句话功能简述〉
 *  TCP 测试demo
 *      Socket 客户端，向服务端发送信息
 *
 * @author bf
 * @create 2018/2/2
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class TcpClient {

    /** 数据编码集 */
    private static final String MSG_CHARSET = "UTF-8";

    /** 缓冲区的大小 */
    private static final int BUFFER_SIZE = 1024;

    /** 链接 ip */
    private String host;

    /** 链接 port */
    private int port;

    /** 选择器*/
    private Selector selector;

    /** socket 发送数据通道*/
    private SocketChannel socketChannel;

    private static ExecutorService read = Executors.newFixedThreadPool(5);

    /**
     *  创建对象的时候，接收链接的ip 和端口号，并初始化 SocketChannel 客户端所需的操作
     * @param host
     * @param port
     */
    public TcpClient(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        initScoketClient();
    }

    private void initScoketClient() throws IOException {
        socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
        socketChannel.configureBlocking(false);
        // 获取选择器
        selector = Selector.open();
        // 将通道注册到选择器上,并注册 read（读取）事件
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 开启线程，执行通道 READ 事件
        read.execute(new TcpClientRead(selector));
    }

    /**
     * 客户端向 ip 和 port 发送msg
     * @param msg
     */
    public void sengMag(String msg) throws IOException {
        // 向缓冲区放入数据
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes(MSG_CHARSET));
        // 客户端向通道写入数据
        socketChannel.write(buffer);
    }


    class TcpClientRead implements Runnable{

        private Selector selector;

        public TcpClientRead(Selector selector) {
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                // 阻塞来着 服务端 的数据，没有数据的话，就会一直阻塞
                selector.select();

                Iterator<SelectionKey> it = selector.selectedKeys().iterator();

                while (it.hasNext()){
                    SelectionKey key = it.next();
                    // 拿取之后需要移除
                    it.remove();
                    // 如果该 SelectionKey 对应的 channel 中有可读的数据
                    if(key.isReadable()){
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                        StringBuffer sb = new StringBuffer();
                        // 将数据读取到 缓冲区中
                        while(channel.read(buffer) > 0){
                            buffer.flip();
                            // 添加 buffer 数据
                            sb.append(Charset.forName(MSG_CHARSET).newDecoder().decode(buffer).toString());
                            buffer.clear();
                        }
                        System.out.println("接收来自服务端的数据: " + sb.toString());
                        // 注册下一次的 可读通道到选择器中，为下一次数据读取做准备
                        channel.register(selector, SelectionKey.OP_READ);
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        TcpClient tcpClient = new TcpClient("127.0.0.1", 8989);
        System.out.println("请输入需要发送的信息：");
        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();
        tcpClient.sengMag(msg);
    }
}
