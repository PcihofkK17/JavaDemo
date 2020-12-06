package com.example.demo.nio;

import org.junit.Test;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 选择器
 *
 * Created by bf on 2018/1/31.
 */
public class SelectTest {


    @Test
    public void test() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();

        // 获取一个选择器，选择器只能作用在 非阻塞式 IO 上面
        Selector selector = Selector.open();

        // 开启非阻塞模式
        socketChannel.configureBlocking(false);

        // 将通道注册到选择器当中
        SelectionKey register = socketChannel.register(selector, SelectionKey.OP_ACCEPT);


        while (true){
            // 阻塞到至少有一个通道在你的饿选择器中准备就绪了
            int readChannel = selector.select();
            if(readChannel == 0){
                continue;
            }

            // 获取所有 register 到选择器上面的所有的 SelectionKey
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            // 集合操作
            while(iterator.hasNext()){
                SelectionKey next = iterator.next();

                // 是否准备好的相关操作 接受 - 链接 - 读 - 写
                if(next.isAcceptable()){
                    // do something
                }
                // is readable
                if(next.isReadable()){
                    // do something
                }

                iterator.remove();

            }


        }
    }
}
