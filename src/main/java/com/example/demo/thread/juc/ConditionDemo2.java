package com.example.demo.thread.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 〈一句话功能简述〉
 * Condition 精确控制线程 示例 Demo
 *
 * @author bf
 * @create 2018/2/9
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ConditionDemo2 {

    private static BoundeBuffer buffer = new BoundeBuffer();

    public static void main(String[] args) {
        // 启动10个线程不断的 放入 和 拿去操作 - 和 生产者 消费者 模式一样
        for (int i = 0; i < 10; i++) {
            new PutThread("t" + i, i).start();
            new TakeThread("t" + i).start();
        }
        /**
         * 可以看出，生产出来的数据，都会被消费者 消费
         * */
    }

    static class PutThread extends Thread {

        private int num;

        public PutThread(String name, int num) {
            super(name);
            this.num = num;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1);
                buffer.put(num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class TakeThread extends Thread {

        public TakeThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1);
                buffer.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class BoundeBuffer {

    /**
     * lock
     */
    private static final Lock lock = new ReentrantLock();

    private static final Condition full = lock.newCondition();

    private static final Condition empty = lock.newCondition();

    /**
     * 商品数量
     */
    private Object[] item = new Object[5];

    int putPtr, takePtr, count;

    /**
     * 往 仓库 放东西，等待消费
     *
     * @param val
     */
    public void put(Object val) {
        // 锁住
        lock.lock();
        try {
            //  如果仓库满了，就等待
            while (count == item.length) {
                full.await();
            }
            // 赋值
            item[putPtr] = val;
            // 如果仓库满了 则 putPtr 置为 0
            if (++putPtr == item.length) {
                putPtr = 0;
            }

            // 容量计数器 + 1
            ++count;
            // 仓库放完之后，通知来取商品
            empty.signal();
            System.out.println(Thread.currentThread().getName() + "put :" + (Integer) val);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 解锁
            lock.unlock();
        }

    }

    /**
     * 消费仓库里面的产品
     *
     * @return
     */
    public Object take() throws InterruptedException {
        // 锁住
        lock.lock();
        try {
            // 如果仓库未空，则等待
            while (count == 0) {
                empty.await();
            }
            // 从 仓库 中取出商品
            Object val = item[takePtr];

            //  如果仓库未空 择 置 0
            if (++takePtr == item.length) {
                takePtr = 0;
            }
            // 仓库 数量 - 1
            count--;
            // 通知仓库继续进行生产
            full.signal();
            System.out.println(Thread.currentThread().getName() + " take :" + (Integer) val);

            return val;
        } finally {
            lock.unlock();
        }
    }
}





