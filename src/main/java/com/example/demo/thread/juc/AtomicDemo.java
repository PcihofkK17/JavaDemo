package com.example.demo.thread.juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Atomic 相关的类，能保证多线程操作下的院子性，效率比 synchroinzed 高很多
 *      利用 CAS 算法，不需要阻塞线程，不断的操作，不阻塞线程，知道满足条件，效率高很多
 *      CAS 算法：
 *          CAS 算法有3个操作数
 *          内存值： V
 *          预期值： A
 *          需要更新的值：B
 *           当且仅当，内存值和预期值相等的时候，才更新成 B 值，否则什么都不做。继续判断是否相等（不需要线程阻塞，效率更高）
 */
public class AtomicDemo {

    public static void main(String[] args) {
        AtomicThread aaa = new AtomicThread();

        for (int i = 0; i <= 10; i++){
            new Thread(aaa).start();
        }
    }

}


class AtomicThread implements Runnable{

    /**
     * 多个线程同时操作这个共享数据的话，可能就会造成数据错误
     * 改用 Atomic 想关的类就可以了
     */
    // private int number = 0;
    private AtomicInteger number = new AtomicInteger();

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(getNum());

    }

    public int getNum(){
       // return number++;
        return number.getAndIncrement();
    }
}