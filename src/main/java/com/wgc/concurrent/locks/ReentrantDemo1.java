package com.wgc.concurrent.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author wgc
 * @Description //可重入锁演示（线程可以进入任何一个它已经拥有的锁所同步着的代码块）
 * ReentrantLook/Synchronized就是一个典型的可重入锁。
 * 概念：同一个线程外层函数获得锁之后，内存递归函数仍能获取该锁的代码。同一个线程在外层方法获取锁的时候，
 * 进入内层方法会自动获取锁。也就是说，线程可以进入任何一个它已经拥有的锁所同步着的代码块。
 * 作用：最大作用是避免死锁。
 * @Date 2021/11/25
 **/
public class ReentrantDemo1 implements Runnable{
    Lock lock = new ReentrantLock();
    static Object lockObj = new Object();

    public static void main(String[] args) {
        Sporter sporter = new Sporter();
        System.out.println("==========Synchronized 可重入锁演示===========");
        new Thread(()->{
            sporter.play();
        },"AA").start();

        new Thread(()->{
            sporter.play();
        },"BB").start();


        try {
            Thread.sleep(1000);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("==========ReentrantLock 可重入锁演示===========");
        ReentrantDemo1 reentrantDemo1 = new ReentrantDemo1();
        Thread t1 = new Thread(reentrantDemo1);
        Thread t2 = new Thread(reentrantDemo1);
//        t1.start();
//        t2.start();

        new Thread(()->{
            reentrantDemo1.put();
        },"CC").start();
        new Thread(()->{
            reentrantDemo1.put();
        },"DD").start();

    }

    /**
     * 可重入锁演示2
     */
    public static void repeatIn(){
        synchronized (lockObj){
            System.out.println("可重入锁外层。。");
            synchronized (lockObj){
                System.out.println("可重入锁中层。。");
                synchronized (lockObj){
                    System.out.println("可重入锁内层。。");
                }
            }
        }
    }

    static class Sporter{
        private synchronized void play(){
            System.out.println(Thread.currentThread().getName()+"\t 打球");
            jump();
        }

        private synchronized void jump(){
            System.out.println(Thread.currentThread().getName()+"\t 跳高");
        }
    }

    @Override
    public void run() {
        put();
    }

    private void put(){

        lock.lock();
        lock.lock();//如果此加锁没有对应的解锁，则系统不会释放该锁资源
        try {
            System.out.println(Thread.currentThread().getName()+"\tput");
            get();
        } finally {
            // 加锁次数和释放锁次数如果不一致，其他线程就无法获取锁，导致线程一直等待。
            lock.unlock();
            lock.unlock();
        }
    }

    private void get(){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName()+"\tget");
        } finally {
            lock.unlock();
        }
    }
}
