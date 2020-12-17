package com.example.demo.thread.juc;

/**
 * 〈一句话功能简述〉
 * LuckSupport 帮 AQS 完成相应线程的阻塞和唤醒工作
 *  不同于Object wait() notify() 等方法，这种需要获取锁，LuckSupport 不需要获取锁
 *
 *  一：API
 *   park(Thread a) - 阻塞当前线程
 *   unpark(Thread a) - 唤醒当前线程
 *
 * @author bf
 * @create 2018/2/9
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class LuckSupportDemo {
}
