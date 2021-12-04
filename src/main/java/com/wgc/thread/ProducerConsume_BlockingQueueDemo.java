package com.wgc.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author wgc
 * @Description //阻塞队列版本 生产消费者
 * @Date 2021/11/28
 **/
public class ProducerConsume_BlockingQueueDemo {
    public static void main(String[] args) {
        OperateResource operateResource = new OperateResource(new ArrayBlockingQueue<Integer>(10));
        new Thread(()->{
            operateResource.produce();
        },"Producer").start();

        new Thread(()->{
            operateResource.consume();
        },"Consumer").start();

        try {Thread.sleep(5000);} catch (InterruptedException e) {e.printStackTrace();}
        System.out.println();
        System.out.println("5秒时间到，现在叫停生产");
        operateResource.stop();
    }

    static class OperateResource {
        private volatile boolean flag = true;
        BlockingQueue<Integer> blockingQueue = null;
        private AtomicInteger atomicInteger = new AtomicInteger();

        public OperateResource(BlockingQueue<Integer> blockingQueue) {
            this.blockingQueue = blockingQueue;
            System.out.println(blockingQueue.getClass().getName());
        }

        public void produce(){
            int temp ;
            boolean productFlag;
            while (flag){
                temp=atomicInteger.getAndIncrement();
                System.out.println("生产者生产了："+temp);
                try {
                    productFlag = blockingQueue.offer(temp, 2L, TimeUnit.SECONDS);
                    if(productFlag){
                        System.out.println(Thread.currentThread().getName()+"\t生产了："+temp);
                    }else {
                        System.out.println(Thread.currentThread().getName()+"\t生产失败");
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName()+"线程停止生产");
        }

        public void consume(){
            while (flag){
                try {
                    Integer integer = blockingQueue.poll(2L, TimeUnit.SECONDS);
                    if(null == integer){
                        System.out.println("2秒内没有可消费信息，停止消费");
                        return;
                    }else {
                        System.out.println("消费者消费："+integer);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stop(){
            this.flag = false;
        }
    }
}


