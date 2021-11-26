package com.wgc.concurrent.demo;

import java.util.concurrent.CountDownLatch;

/**
 * @Author wgc
 * @Description //CountDownLatch 使用示例
 * 概念：让一些线程阻塞直到另一些线程完成一系列操作后才被唤醒。
 * 当一个或多个线程调用await()方法时，调用线程会被阻塞。其他线程调用countDown方法会将计数器减1
 * （调用countDown方法的线程不会阻塞），当计数器的值变为0时，因调用await方法被阻塞的线程会被唤醒，继续执行。
 * @Date 2021/11/26
 **/
public class CountDownLatchDemo {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        try {
            for (int i = 0; i < 5; i++) {
                new Thread(()->{
                    System.out.println(Thread.currentThread().getName()+"离开会议室");
                    try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
                    countDownLatch.countDown();
                },"开会人员"+(i+1)).start();
            }
            countDownLatch.await();
            System.out.println("全部人员离开会议室，管理者"+Thread.currentThread().getName()+"关闭会议室");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
