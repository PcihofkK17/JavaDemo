package com.example.demo.thread;

/**
 * 〈一句话功能简述〉
 * 线程唤醒和等待
 *
 *  一：API 详解
 *   1：wait() - 让当前对象进入 等待（阻塞状态），直到其他线程调用该对象的 notify() 方法，或者 notifyAll() 方法
 *               让线程重新进入等待状态，
 *               让当前线程进入等待状态，而这个当前线程，就是当前执行的线程，而不是调用对象的这个线程
 *   2：notify() - 唤醒该对象监视器上面等待的单个线程
 *   3：notifyAll() - 唤醒在对象监视器上面所有的等待线程
 *   4：wait(long timeout) - 同 wait(), 或者等待超过 timeout 自动被唤醒。进入就绪状态
 *   5：wait(long timeour. int nanos) - 同 wait(long timeout), 增加超过某个时间量的超时唤醒,
 *                  或（some other thread interrupts the current thread） 其他线程中断当前线程等，都会让他进入就绪状态
 *
 *  二：为什么这些 API 是定义 在object 而不是 Thread 上面呢
 *      notify() 依据什么来唤醒线程，wait() 和 notify() 依据什么来互相关联 --- 对象的同步锁
 *
 *
 *
 * @author bf
 * @create 2018/2/6
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class WaitNotifyDemo {

    public static void main(String[] args){
        // Thread t1 = new ThreadA();
        Thread t1 = new ThreadB("t1");

        synchronized (t1){
            try {
                // 启动 线程 t1
                System.out.println(Thread.currentThread().getName() + " start ");
                t1.start();

                // 主线程通过等待 ti notify 唤醒
                System.out.println(Thread.currentThread().getName() + " wait ");

                /** 当前线程等待（运行状态的线程），当前执行的线程（main）而不是 t1 */
                // t1.wait();
                /** 1：t1 run  3 秒之后 main 线程重新执行，也就是等待的是 main 线程
                 *  2：不同于 sleep() 方法，sleep 方法是谁调用 谁 睡觉
                 *  3：wait 方法会释放锁，所以 之前 synchronized 持有锁的时候 ThreadA 里面的 synchronized同步代码块不会执行
                 *    等待 wait 之后，才会执行
                 * */
                t1.wait(3000);

                System.out.println(Thread.currentThread().getName()+" continue");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}


/**
 * notify api 测试
 */
class ThreadA extends Thread{

    @Override
    public void run() {
        synchronized (this){
            System.out.println(Thread.currentThread().getName() + " call notify()");
            /** 唤醒该对象上面的等待线程 也就是唤醒的是等待中的 main 线程 */
            notify();
        }
    }
}



class ThreadB extends Thread{

    public ThreadB(String name) {
        super(name);
    }

    @Override
    public void run() {
        // 模拟当前线程一直 run 的状态
        System.out.println(Thread.currentThread().getName() + " run ");
        while (true){

        }
    }
}

