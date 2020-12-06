package com.example.demo.nio;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * NIO 通道
 *
 *  一：channel 分类
 *     -- FileChannel           文件通道，从文件读写数据
 *     -- SocketChannel         TCP通道，从TCP读写数据
 *     -- ServerSocketChannel   监听新进来的TCP链接，并对每个链接创建新的 SocketChannel 通道
 *     -- DatagramChannel       UDP通道，通过UDP读写数据
 *
 *  二：通道工作的两种模式
 *      阻塞式和非阻塞式： 在非阻塞模式下，调用的线程不会休眠，请求的操作会立刻返回结果；在阻塞模式下，调用的线程会产生休眠。
 *                       另外除FileChannel不能运行在非阻塞模式下，其余的通道都可阻塞运行也可以以非阻塞的方式运行
 *
 *
 *
 *
 * Created by bf on 2018/1/30.
 */
public class ChannelTest {


    /**
     * FileChannel 测试
     *  transferFrom
     *  transferTo
     */
    @Test
    public void fileChannelTest(){
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = FileChannel.open(Paths.get("F:\\哇哈哈哈哈.txt"), StandardOpenOption.READ);
            out = FileChannel.open(Paths.get("F:\\fileIn.txt"),StandardOpenOption.WRITE, StandardOpenOption.CREATE);

            // 起点 数据终点位置 limit ,往哪个地方写
            // in.transferTo(0, in.size(), out);
            out.transferFrom(in, 0, in.size());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     *  通过 FileChannel 写入数据
     * @throws FileNotFoundException
     */
    @Test
    public void channelTest() throws IOException {
        // rw 表示 read write 可读可写
        RandomAccessFile rw = new RandomAccessFile("F:\\哇哈哈.txt", "rw");

        // 获取文件通道
        FileChannel channel = rw.getChannel();

        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 从通道中读取数据
        while(channel.read(buffer) != -1 ){

            // 缓冲区切换成读取模式
            buffer.flip();

            // 如果缓冲区里面有数据
            while(buffer.hasRemaining()){
                // 创建一个 byte 数组，接收来着 缓冲区里面的上海连
                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes);

                //TODO 这种情况中文乱码 -- 后面解决
                System.out.println(new String(bytes));
            }

            buffer.clear();
        }
        // 关闭通道
        rw.close();
        channel.close();

    }

}
