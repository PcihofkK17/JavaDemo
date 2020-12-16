package com.example.demo.thread.juc;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * countdownlatch: 同步辅助器，类似于一个线程的操作，要等待其他的几个线程全部完成之后再执行，就可以使用这玩意
 *              允许一个或者多个线程，等待其他线程完成执行
 *
 *
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);

        CountDownLatchThread cdlt = new CountDownLatchThread(countDownLatch);

        long start = System.currentTimeMillis();

        for (int i = 0; i < 10; i++) {
            new Thread(cdlt, "count").start();
        }

        // 当前线程 在 计数器 为 0 之前一直等待，除非线程被中断
        countDownLatch.await();

        /**
         * 没有 countDownLatch 等待线程执行完的时候，这条打印语句已经直接执行过了
         */
        System.out.println("总耗时：" + (System.currentTimeMillis() - start));

    }

}

/**
 * 实例： 等待10个线程全部执行完成，统计执行时间
 */
class CountDownLatchThread implements Runnable{

    private CountDownLatch latch;

    public CountDownLatchThread(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
            System.out.println(Thread.currentThread().getName() + new Random());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 递减锁存器的计数，如果计数 为 0 ，则释放所有等待的线程
            latch.countDown();
        }

    }
}