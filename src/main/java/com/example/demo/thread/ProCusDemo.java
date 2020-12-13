package com.example.demo.thread;

/**
 * 〈一句话功能简述〉
 * 多线程中的生产者消费者模型
 *
 *  如果是 多生产者和多消费者的话，就是 notifyAll()
 *         单生产者和单消费者的话，就是 notify()
 *
 * @author bf
 * @create 2018/2/7
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ProCusDemo {

    public static void main(String[] args){
        Dept dept = new Dept(100);
        Produce mPro1 = new Produce(dept);
        Produce mPro2 = new Produce(dept);

        Customer mCus1 = new Customer(dept);
        Customer mCus2 = new Customer(dept);

        mPro1.produce(20);
        mCus1.customer(30);
        mCus2.customer(50);
        mPro2.produce(60);
        mPro1.produce(80);
        mCus1.customer(120);
        mPro1.produce(60);
        mCus1.customer(30);
        mPro1.produce(70);
        mCus1.customer(50);

        /**
         *
         * Thread-0 produce( 20) --> left=  0, inc= 20, size= 20
         Thread-9 customer( 50) --> left= 30, inc= 20, size=  0
         等待生产 ing ..........
         Thread-8 produce( 70) --> left=  0, inc= 70, size= 70
         Thread-7 customer( 30) --> left=  0, inc= 30, size= 40
         Thread-6 produce( 60) --> left=  0, inc= 60, size=100
         Thread-5 customer(120) --> left= 20, inc=100, size=  0
         等待生产 ing ..........
         Thread-4 produce( 80) --> left=  0, inc= 80, size= 80
         Thread-3 produce( 60) --> left= 40, inc= 20, size=100
         等待消费 ing ...........
         Thread-2 customer( 50) --> left=  0, inc= 50, size= 50
         Thread-1 customer( 30) --> left=  0, inc= 30, size= 20
         Thread-3 produce( 60) --> left=  0, inc= 40, size= 60
         Thread-5 customer(120) --> left=  0, inc= 20, size= 40
         Thread-9 customer( 50) --> left=  0, inc= 30, size= 10

         可以看出，消费完了之后，生产者就会进行生产，生产打到最大值了，就会通知消费者进行消费
         * */
    }

}

/**
 * 生产者消费者模型工厂，负责生产数据和消费数据
 */
class Dept{

    /** 工厂总容量 */
    private int capacity;

    /** 工厂当前容量 */
    private int size;

    public Dept(int capacity) {
        this.capacity = capacity;
        this.size = 0;
    }

    /**
     *  生产数据，必须要加 synchronized 或者 Lock 进行加锁
     * @param val
     */
    public synchronized void produce(final int val){
       try{
           // 能生产的数量
           int left = val;
           while (left > 0){
               // 如果仓库满了，就等待，当有消费者进行消费的时候，再进行后续操作
               while (size >= capacity){
                   System.out.println("等待消费 ing ...........");
                   wait();
               }
               // 100 - 20 < 90 ? 80 : 90
               // 根据仓库的当前容量计算可生产的最大数量
               int inc = (capacity - size) < left  ? (capacity - size) : left;
               // 增加 生产的容量
               size += inc;
               // 生产剩余数量
               left -= inc;
               System.out.printf("%s produce(%3d) --> left=%3d, inc=%3d, size=%3d\n",
                       Thread.currentThread().getName(), val, left, inc, size);
                // 生产完唤醒 消费者进行消费
               notifyAll();
           }
       }catch (InterruptedException e){
       }
    }

    /**
     * 消费资源
     * @param val
     */
    public synchronized void customer(final int val){
        try{
            // 能消费的数量
            int left = val;
            while (left > 0){
                // 仓库没有存量的时候，等待生产者生产
                while (size <= 0){
                    System.out.println("等待生产 ing ..........");
                    wait();
                }
                // 60 > 50 ? 50 : 60 ---> 消费的数量不能大于存量
                int inc = size > left ? left : size;
                // 容量 - 消费的数量
                size -= inc;
                // 没有东西可消费的数量
                left -= inc;
                System.out.printf("%s customer(%3d) --> left=%3d, inc=%3d, size=%3d\n",
                        Thread.currentThread().getName(), val, left, inc, size);
                // 消费完成，等待生产者生产数据
                notifyAll();
            }
        }catch (InterruptedException e){
        }
    }
}

/**
 * 生产者实例
 */
class Produce{

    private Dept dept;

    public Produce(Dept dept) {
        this.dept = dept;
    }

    public void produce(final int val){
        new Thread(){
            @Override
            public void run() {
                dept.produce(val);
            }
        }.start();
    }
}

/**
 * 消费者实例
 */
class Customer{

    private Dept dept;

    public Customer(Dept dept) {
        this.dept = dept;
    }

    public void customer(final int val){
        new Thread(){
            @Override
            public void run() {
                dept.customer(val);
            }
        }.start();
    }
}


