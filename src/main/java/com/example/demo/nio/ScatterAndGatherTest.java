package com.example.demo.nio;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * scatter(分散)：分散，从channel 中读取的数据，写入到多个buffer中
 * gather(聚集)：写入 channel 的时候，将多个 buffer 的数据写入到同一个 channel
 *
 * Created by bf on 2018/1/30.
 */
public class ScatterAndGatherTest {


    @Test
    public void test() throws IOException {
        FileChannel fileChannel = FileChannel.open(Paths.get("F:\\芦田爱菜.bmp"), StandardOpenOption.READ);

        ByteBuffer buffer1 = ByteBuffer.allocate(100);
        ByteBuffer buffer2 = ByteBuffer.allocate(1024);

        ByteBuffer[] bufferArray = {buffer1, buffer2};

        // 将通道中的是数据读取到 多个 Buffer 中，这时候通道会往多个 Buffer 中写数据，第一个写满，就写第二个
       fileChannel.read(bufferArray);


    }
}
