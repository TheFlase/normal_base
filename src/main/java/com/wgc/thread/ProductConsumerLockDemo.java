package com.wgc.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author wgc
 * @Description //生产者消费者  Lock 版本
 * @Date 2021/11/24
 **/
public class ProductConsumerLockDemo {
    private int number = 0;
    private Lock lock = new ReentrantLock();
    Condition conditionLock = lock.newCondition();

    public static void main(String[] args) {
        ProductConsumerLockDemo lockDemo = new ProductConsumerLockDemo();
        new Thread(()->{
            for (int i = 0; i < 3; i++) {
                lockDemo.put();
            }
        },"Product").start();

        new Thread(()->{
            for (int i = 0; i < 3; i++) {
                lockDemo.get();
            }
        },"Consumer").start();
    }


    private void put(){
        try {
            lock.lock();
            while (number !=0){
                //如果生产出来了就等待
                conditionLock.await();
            }
            number++;
            System.out.println(Thread.currentThread().getName()+"\t "+number);
            conditionLock.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    private void get(){
        try {
            lock.lock();
            while (number ==0){
                //如果消费了就等待
                conditionLock.await();
            }
            number--;
            System.out.println(Thread.currentThread().getName()+"\t "+number);
            conditionLock.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
