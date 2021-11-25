package com.wgc.concurrent.locks;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author wgc
 * @Description //自旋锁 示例
 * @Date 2021/11/25
 **/
public class SpinLockDemo {

    private AtomicReference<Thread> reference = new AtomicReference<>();

    public static void main(String[] args) {
        SpinLockDemo spinLockDemo = new SpinLockDemo();
        new Thread(()->{
            spinLockDemo.lock();
            try {
                // 模拟业务处理耗时
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spinLockDemo.unlock();
        },"A").start();
        try {
            // 暂停1秒让A线程先执行
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(()->{
            spinLockDemo.lock();
            try {
                // 模拟业务处理耗时
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spinLockDemo.unlock();
        },"B").start();
    }

    public void lock(){
        Thread thread = Thread.currentThread();

        while (!reference.compareAndSet(null,thread)){
//            System.out.println("线程"+thread.getName()+"在自旋尝试获取锁。。。");
        }
        System.out.println("线程"+thread.getName()+"\t 成功获得锁");
    }
    public void unlock(){
        Thread thread = Thread.currentThread();
        reference.compareAndSet(thread,null);
        System.out.println("线程"+thread+"\t 解锁成功");
    }
}
