package com.example.demo.thread;

/**
 * 〈一句话功能简述〉
 * 多线程中的生产者消费者模型
 *
 * @author bf
 * @create 2018/2/7
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ProCusDemo {

    public static void main(String[] args){
        Dept dept = new Dept(100);
        Produce produce = new Produce(dept);
        Customer customer = new Customer(dept);
        Customer customer2 = new Customer(dept);

        produce.produce(160);
        customer.customer(80);
        customer2.customer(30);
        produce.produce(40);
        produce.produce(80);
        customer.customer(110);
        customer.customer(70);
        produce.produce(30);
    }

}


/**
 *  生产工厂
 */
class Dept{

    /** 生产容量 */
    private int capacity;

    /** 仓库的实际容量 */
    private int size;

    public Dept(int capacity) {
        this.capacity = capacity;
        this.size = 0;
    }

    /**
     * 生产
     * @param val
     */
    public synchronized void produce(int val){
        try{
            //
            int left = val;
            while (left > 0){
                // 生产完成的时候，等待，当消费者消费过之后，不满足条件，再继续进行生产
                while (size >= capacity){
                    wait();
                }
                // 这个条件是防止生产的数据超出 所能生产的最大数
                int inc = size + left > capacity ? (capacity - size) : left;
                size += inc;
                left -= inc;
                System.out.printf("%s produce(%3d) --> left=%3d, inc=%3d, size=%3d\n", Thread.currentThread().getName(), val, left, inc, size);
                // 生产完成后通知消费者消费
                notifyAll();
            }
        }catch (InterruptedException e){

        }
    }

    /**
     * 消费 产品
     */
    public synchronized void customer(int val){
        try {
            int left = val;
            while (left > 0){
                // 没有库存的时候，通知生产者生产产品
                while (size <= 0){
                    wait();
                }
                // 如果消费的产品和工厂里面还剩多少的产品进行比较
                int inc = (size < left) ? size : left;
                size -= inc;
                left -= inc;
                System.out.printf("%s customer(%3d) --> left=%3d, inc=%3d, size=%3d\n", Thread.currentThread().getName(), val, left, inc, size);
                // 消费完 通知 生产者继续生产
                notifyAll();
            }

        }catch (InterruptedException e){

        }
    }

    @Override
    public String toString() {
        return "Dept{" +
                "capacity=" + capacity +
                ", size=" + size +
                '}';
    }
}

/**
 * 生产者
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
 * 消费者
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

