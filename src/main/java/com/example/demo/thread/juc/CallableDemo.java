package com.example.demo.thread.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 实现多线程的第三种方式： Callable 接口，需要使用 Future 接受返回值
 *  需要结合 FutureTask 使用
 *  Callable 的线程方法拥有返回值
 */
public class CallableDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CallableThread callableThread = new CallableThread();
        // 获取 FutureTask
        FutureTask futureTask = new FutureTask(callableThread);
        // 开启线程
        new Thread(futureTask).start();

        System.out.println(futureTask.get());
    }
}


class CallableThread implements Callable<Integer>{

    /**
     * 不同于 run() , call()  有返回值，并且有怕抛出异常
     * @return
     * @throws Exception
     */
    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " callable run ....");
        return 1;
    }
}
