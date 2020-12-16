package com.example.demo.thread.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程案例 Demo
 *  启动3个线程，按顺序打印 ABCABCABC ....
 */
public class AltenateDemo {

    public static void main(String[] args) {

        AltenateThread altenateThread = new AltenateThread();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    altenateThread.printA(i);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    altenateThread.printB(i);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    altenateThread.printC(i);
                }
            }
        }).start();

    }
}


class AltenateThread {

    private int num = 1;

    private Lock lock = new ReentrantLock();

    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();
    private Condition conditionC = lock.newCondition();

    public void printA(int val){
        lock.lock();
        try {
            // num = 1 才执行 打印 A 的操作
            if(num != 1){
                // await 需要获取锁
                conditionA.await();
            }
            for (int i = 0; i < 1; i++) {
                System.out.println("A ----- " + val);
            }

            num = 2;
            // A 打印完成之后，唤醒 打印B 的操作
            conditionB.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }

    public void printB(int val){
        lock.lock();
        try {
            while (num != 2){
                conditionB.await();
            }
            for (int i = 0; i < 2; i++) {
                System.out.println("B ----- " + val);
            }
            num = 3;
            conditionC.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void printC(int val){
        lock.lock();
        try {
            while (num != 3){
                conditionC.await();
            }
            for (int i = 0; i < 3; i++) {
                System.out.println("C ----- " + val);
            }
            // 再次唤醒 打印 A 的操作
            num = 1;
            // 再次唤醒 A
            conditionA.signal();

            System.out.println("------------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }


}

