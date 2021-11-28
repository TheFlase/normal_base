package com.wgc.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author wgc
 * @Description // 生产消费者 示例
 * 题目：多线程之间顺序调用，实现A->B->C三个线程启动，要求如下
 * A打印1次，B打印2次，C打印3次，重复打印10轮。
 * @Date 2021/11/26
 **/
public class SyncAndReentrantLockDemo {
    public static void main(String[] args) {
        Data data = new Data();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.play1(i);
            }
        },"A").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.play2(i);
            }
        },"B").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.play3(i);
            }
        },"C").start();
    }

    static class Data{
        private Lock lock = new ReentrantLock();
        private int number = 1;// 1A 2B 3C
        Condition p1 = lock.newCondition();
        Condition p2 = lock.newCondition();
        Condition p3 = lock.newCondition();

        private void play1(int i){
            lock.lock();
            try {
                while (number!=1){
                    p1.await();
                }
                System.out.println("第"+i+"次打印");
                System.out.println("111111");
                number++;
                p2.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        private void play2(int i){
            lock.lock();
            try {
                while (number!=2){
                    p2.await();
                }
                System.out.println("222222");
                System.out.println("222222");
                number++;
                p3.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        private void play3(int i){
            lock.lock();
            try {
                while (number!=3){
                    p3.await();
                }
                System.out.println("333333");
                System.out.println("333333");
                System.out.println("333333");
                number=1;
                p1.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        private void printByTime(Thread thread,int printTime){
            for (int i = 0; i < printTime; i++) {
                System.out.println(thread.getName()+"");
            }
        }
    }
}
