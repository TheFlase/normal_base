package com.wgc.concurrent.demo;

import java.util.concurrent.Semaphore;

/**
 * @Author wgc
 * @Description //Semaphore 使用示例
 * 概念：信号量主要用于两个目的，一个是用于多个共享资源的互斥使用，另外一个用于并发线程数的控制。
 * @Date 2021/11/26
 **/
public class SemaphoreDemo {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 6; i++) {
            int finalI = i;
            new Thread(()->{
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"抢到了餐位");
                    try {Thread.sleep(finalI *1000);} catch (InterruptedException e) {e.printStackTrace();}
                    System.out.println(Thread.currentThread().getName()+"离开了餐位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            },String.valueOf(i)).start();
        }
    }
}
