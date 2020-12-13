package com.example.demo.thread;

/**
 * 〈一句话功能简述〉
 * 线程的两种创建方式的不同
 *  一：两种创建线程的方式
 *      1：继承 Thread 类，重写 run 方法
 *      2：实现 Runnable 接口，实现 run 方法
 *      Runnable 公用一套资源，Thread 因为每次都是新 new 的，会拥有自己的单独的一套资源
 * 二：start() 和 run() 的区别
 *      start() 是开启线程，让该线程变为可执行状态，启用的线程会调用 run() 方法
 *      run() 就是线程的执行方法（普通的成员方法），不会开启新的线程
 *
 * 三：常用 API
 *  1：yield() - 线程礼让，让该线程从 运行状态 ---> 就绪状态，这时候还有可能重新被 执行
 *     于 wait() 的区别： wait() 是 由运行状态 ---> 阻塞状态, 且释放锁
 *                       yeild 不释放锁
 *
 *  2：sleep() - 线程休眠，休眠多少秒，使当前线程由 运行状态 ---> 阻塞状态
 *      于 wait() 的区别：同样 wait() 会释放锁，当时 sleep() 不会
 *
 *  3：join() - 让主线程等待子线程运行结束后才能继续进行
 *
 *  4：interrupt() - interrupt 不会终止运行状态下的线程，他会将线程的中断标记设置为 true
 *
 *
 *
 *
 * @author bf
 * @create 2018/2/6
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ThreadDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new MyThread();
        Thread t2 = new MyThread();
        Thread t3 = new MyThread();
        t1.start();
        t2.start();
        t3.start();

        Thread.sleep(1000);
        System.out.println("--------------------------------");

        MyRunnable runnable = new MyRunnable();
        Thread r1 = new Thread(runnable);
        Thread r2 = new Thread(runnable);
        Thread r3 = new Thread(runnable);

        r1.start();
        r2.start();
        r3.start();

    }

}

/**
 * 继承 Thread 重写 run 方法
 */
class MyThread extends Thread{

    private int ticket = 10;

    private Thread thread;

    @Override
    public void run() {
        for(int i = 0; i < 20; i++){
            if(this.ticket > 0 ){
                System.out.println(this.getName() + "卖票: ticket " + ticket--);
            }
        }
    }

    @Override
    public void start() {
        System.out.println("重写 start");
        if(thread == null){
            thread = new Thread();
            thread.start();
        }
    }
}

/**
 *  实现 Runnable 接口，实现
 */
class MyRunnable implements Runnable{

    private int ticket = 10;

    @Override
    public void run() {
        for (int i = 0; i < 20; i++){
            if(ticket > 0){
                System.out.println(Thread.currentThread().getName() + "卖票: ticket " + ticket --);
            }
        }
    }
}
