package com.example.demo.thread.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 〈一句话功能简述〉
 * Condition 示例
 *  condition, 相比于Object 中的沉睡唤醒方法，他能够更精确的控制多线程的休眠和唤醒，对于同一个锁，我们
 *           可以创建多个 condition ,在不同的情况下使用不同的 condition.(详见 @linkplain ConditionDemo2 )
 *
 * 一：API 详解
 *  1：await() - 当前线程在接收到信号（signal(), signalAll） 活中断之前，一直处于等待状态
 *  2：signal() - 唤醒一个线程
 *  3：signalAll() - 唤醒所有等待的线程
 *      (这几个方法和 Object 中的不同，wait(),notify(),notifyAll() 这些方法在调用的时候必须持有对象的锁，
 *      也就是必须加 synchronized, 而 signal() ,signalAll() 不需要)
 *  4:awaitUninterruptibly() - Causes the current thread to wait until it is signalled, 导致当前线程等待，只到接收到信号
 *                              中断不行。
 *
 * @author bf
 * @create 2018/2/9
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ConditionDemo {

    /** 锁对象 */
    private static Lock lock = new ReentrantLock();

    /** 获取 Condition 对象 */
    private static Condition condition = lock.newCondition();

    public static void main(String[] args){

        Thread cThread = new ConditionThread("cThread");

        // 获取锁
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " start ..");
            cThread.start();

            /** condition await 等待 run里面的 signal 唤醒 */
            System.out.println(Thread.currentThread().getName() + " await ..");
            condition.await();

            System.out.println(Thread.currentThread().getName() + " contiune ..");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 释放锁
            lock.unlock();
        }


    }


    /**
     * 静态内部类
     */
    static class ConditionThread extends Thread{

        public ConditionThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            // 锁住
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "wakeup others !!!!");
                condition.notify();
            }finally {
                // 解锁
                lock.unlock();
            }
        }
    }

}



