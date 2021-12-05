package com.wgc.concurrent.demo;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @Author wgc
 * @Description //
 * @Date 2021/12/4
 **/
public class CallalbeDemo {
    public static void main(String[] args) throws Exception{
        Play play = new Play();
        FutureTask futureTask = new FutureTask(play);
        FutureTask futureTask2 = new FutureTask(play);
        // AA和BB 共用一个futuretask时，只会由其中一个线程执行call()方法
        new Thread(futureTask,"AA").start();
        new Thread(futureTask,"BB").start();
        new Thread(futureTask2,"CC").start();

        Integer result = null;
        // futureTask.get()方法会阻塞到获得结果
//        Integer result = (Integer) futureTask.get();

        System.out.println(Thread.currentThread().getName()+"线程获得执行结果："+result);

        result = (Integer) futureTask.get();
        System.out.println(Thread.currentThread().getName()+"线程获得阻塞结束后的执行结果："+result);
    }

    static class Play implements Callable<Integer>{
        @Override
        public Integer call() throws Exception {
            System.out.println(Thread.currentThread().getName()+"执行call()方法");
            // 模拟处理耗时2s
            try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
            return new Random().nextInt(10);
        }
    }
}
