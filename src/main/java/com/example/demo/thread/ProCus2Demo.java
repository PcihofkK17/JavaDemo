package com.example.demo.thread;

/**
 *  生成着 消费者 模型 2
 */
public class ProCus2Demo {

    public static void main(String[] args) {
        Resource resource = new Resource();

        new Thread(new Produce2(resource)).start();
        new Thread(new Customer2(resource)).start();
        new Thread(new Produce2(resource)).start();
        new Thread(new Customer2(resource)).start();

    }

}


/**
 *  资源
 */
class Resource{

    /** 共享资源标识 */
    private int number = 0;

    /** 是否已经生产 标识，如果生产过就 wait() 然后等待消费者消费，  */
    private boolean flag = false;


    /**
     * 生产资源
     */
    public synchronized void produce(){
        // 如果已经生产过，让生成者等待，消费者消费过再生产
        while(flag){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        number++;
        // 生成资源
        System.out.println(Thread.currentThread().getName() + " produce number: " + number);
        // 生产过标识
        flag = true;
        // 一个生产者一个消费者的话，使用notify()就可以，多个生产者多个消费者的话，使用notifyAll() ,保证唤醒所有消费者
        notifyAll();
    }

    /**
     * 消费资源
     */
    public synchronized void customer(){
        // 如果消费过，则等待生产者生产
        while (!flag){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 消费数据
        System.out.println(Thread.currentThread().getName() + " customer number:" + number);
        // 消费标识
        flag = false;
        notifyAll();
    }

}

class Produce2 implements Runnable{

    private Resource resource;

    public Produce2(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource.produce();
        }
    }
}

class Customer2 implements Runnable{

    private Resource resource;

    public Customer2(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource.customer();
        }
    }

}
