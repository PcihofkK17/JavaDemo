package com.example.demo.nio;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * NIO 缓存区 API 测试
 *
 *  Buffer 是 NIO 中的缓冲区，用于数据传输，NIO 提供该 API 用户访问内存中的这块内存区域
 *
 *  一：
 *  step1:  写入数据到 Buffer
 *  step2:  调用 flip() 方法，标识缓冲区数据可读取
 *  step3:  从 Buffer 中读取数据
 *  step4:  调用 clear() 或者 compact() 方法
 *
 * 二：缓存区中的四个属性
 *  capacatiy: 容量，缓冲区中红存储的最大数据量，一单buffer满了，需要清空缓冲区数据（读取数据或者清楚数据），然后才能往
 *              缓存区中填写数据
 *  position: 位置 缓冲区 正在操作的数据的位置
 *  limit: 界限，表示缓冲区可操作数据的大小
 *              写模式下面 limit 和 capacatiy 一样
 *              读模式下面，limit 表示最多可以读取多少数据，因此buffer 切换到读模式下，limit 会被设置成 写模式下的position
 *
 *三：缓冲区清除的两个方法的区别
 *     -- clear()   清除缓冲区所有数据
 *     -- compact() 只会清除已经读到的数据
 *
 * Created by bf on 2018/1/27.
 */
public class BufferTest {


    @Test
    public void test1(){
        String str = "高雪纯鲍放";
        // 获取缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        System.out.println(buffer.capacity()); // 1024
        System.out.println(buffer.limit()); // 1024
        System.out.println(buffer.position()); // 0

        // 写入数据
        buffer.put(str.getBytes());

        System.out.println(buffer.capacity()); // 1024
        System.out.println(buffer.limit()); // 1024
        System.out.println(buffer.position()); // 15

        // 将 buffer 切换到读的模式
        buffer.flip();

        System.out.println(buffer.capacity()); // 1024
        System.out.println(buffer.limit()); // 15，只存入了15个字节的数据，所以读的界限就是写入数据时的 position
        System.out.println(buffer.position()); // 0

        // 使用get 读取数据
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        System.out.println(new String(bytes));

        // 将 position 设置为0，可重复读
        buffer.rewind();




    }

}
