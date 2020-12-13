package com.example.demo.thread;

/**
 * 〈一句话功能简述〉
 * Thread 线程中断 interrupt() 方法测试、
 *  当线程由于被调用 wait(), sleep(), join() 等方法时，会让线程进入阻塞状态，这时候调用 interrupt() 方法的时候
 *  会将线程的中断状态变为 true .但是由于是阻塞状态，所以这个状态会被清除，并且抛出 InterruptedException
 *  然后利用这个 异常就可以终止线程
 *
 *  interrupt 只是更改线程的中断标志 false/true ，并不直接中断线程，如果你的代码里面没有对线程的中断状态进行判断的话，即使
 *      设置为 true ，也照样运行
 *
 * interrupt() 和 isInterrupt() 方法的区别
 *  interrupt() 返回中断标记，并清除中断标记
 *  isInterrupt() 只返回中断标记
 *
 * @author bf
 * @create 2018/2/7
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class InterruptDemo {

    public static void main(String[] args){
        try {
            Thread t1 = new MyInterruptThread("t1");
            System.out.println(t1.getName() + "( " + t1.getState() + " ) is new");

            t1.start();
            System.out.println(t1.getName() + "( " + t1.getState() + " ) is start");

            // 主线程休眠，让t1 线程执行，执行300ms 的时候中断 t1 线程
            Thread.sleep(3000);
            t1.interrupt();
            System.out.println(t1.getName() + "( " + t1.getState() + " ) is interrupted");

            Thread.sleep(3000);
            System.out.println(t1.getName() + "( " + t1.getState() + " ) is interrupted now");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}

class MyInterruptThread extends Thread{

    public MyInterruptThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        try {
            int i = 0;
            // 不断监控是否是中断状态，如果是 false 则休眠 100ms
            while (!isInterrupted()){
                // 阻塞状态
                Thread.sleep(1000);
                i++;
                System.out.println(Thread.currentThread().getName() + "status: " + this.getState() + " loop " + i);
            }
        // 线程中断异常，如果在 sleep() 阻塞状态下，其他地方调用 interrupt() 方法，那么就会抛出这个异常，并中断线程操作
        // 这个要放在 while() 外，不然抛出异常不会跳出循环
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "is interrupt");
            e.printStackTrace();
        }
    }
}
