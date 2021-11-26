package com.wgc.concurrent.demo;

import java.util.concurrent.SynchronousQueue;

/**
 * @Author wgc
 * @Description //SynchronousQueue 示例
 * 一个不存储元素的阻塞队列。每个插入操作必须等到另外一个线程调用移除操作，
 * 否则插入操作一直处于阻塞状态，吞吐量通常要高于LinkedBlockingQueue。
 * 使用阻塞方法  take()和put()
 * @Date 2021/11/26
 **/
public class SynchronousQueueDemo {
    public static void main(String[] args) {
        SynchronousQueue<Object> synchronousQueue = new SynchronousQueue<>();

        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getName()+"取元素:"+synchronousQueue.take());
                System.out.println(Thread.currentThread().getName()+"取元素:"+synchronousQueue.take());
                System.out.println(Thread.currentThread().getName()+"取元素:"+synchronousQueue.take());
                // take空元素后阻塞等待
//                System.out.println(Thread.currentThread().getName()+"取元素:"+synchronousQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"B").start();
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getName()+"存放元素:1");
                synchronousQueue.put(1);
                System.out.println(Thread.currentThread().getName()+"存放元素:2");
                synchronousQueue.put(2);
                System.out.println(Thread.currentThread().getName()+"存放元素:3");
                synchronousQueue.put(3);
//                System.out.println(Thread.currentThread().getName()+"存放元素:4");
//                synchronousQueue.put(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A").start();
    }

}
