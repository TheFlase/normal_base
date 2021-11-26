package com.wgc.concurrent.demo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Author wgc
 * @Description // CyclicBarrier 使用示例
 * 概念：CyclicBarrier的字面意思是可循环使用的屏障。它主要做的事情是，
 * 让一组线程达到一个屏障（也可以叫同步点）时被阻塞，直到最后一个线程到达屏障时，
 * 屏障才会开门，所有被屏障拦截的线程才会继续干活，线程进入屏障通过CyclicBarrier的await()方法
 * @Date 2021/11/26
 **/
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5,()->{
            System.out.println(Thread.currentThread().getName()+"组织开会");
        });
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"走进了会议室");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },"与会人员"+i).start();
        }
    }
}
