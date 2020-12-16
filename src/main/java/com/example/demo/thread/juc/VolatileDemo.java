package com.example.demo.thread.juc;

/**
 * volatile 关键字：保证共享变量的可见性，是轻量级的 synchroized
 *      -- 线程每操作一些共享数据的时候，会从共享数据缓存到自己的线程变量中一份，这样的话，多个线程操作共享数据的话，
 *         可能会造成读取到的值是缓存中的数据，使用 volatile 关键字的话，可以保证一个线程修改共享变量的值的时候，
 *         另一个线程读到该修改后的值
 */
public class VolatileDemo {

    public static void main(String[] args) {
        VolatileThread volatileThread = new VolatileThread();
        // 开启线程
        new Thread(volatileThread).start();

        while (true){
            // 如果被 另一个线程改过值的话 break
            if(volatileThread.getFlag()){
                System.out.println("--------------");
                break;
            }
        }
    }
}


class VolatileThread implements Runnable{

    /**
     *  这里面没有使用 volatile 关键字的情况就是 只打印 Thread-0 flag : true
     *  因为在 main 启动的时候，main 线程一开始读到的是 flag=false, 并在自己的线程变量中做了缓存
     *  我们 该线程启动之后，改为 true ，main 线程并没有读到这个被改过的值（共享变量不可见），所以造成了这种情况
     *  添加 volatile 关键字就行了
     */
    // private boolean flag = false;
    private volatile boolean flag = false;

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 更改共享变量的值
        flag = true;
        System.out.println(Thread.currentThread().getName() + " flag : " + getFlag());
    }

    public boolean getFlag(){
        return flag;
    }
}
