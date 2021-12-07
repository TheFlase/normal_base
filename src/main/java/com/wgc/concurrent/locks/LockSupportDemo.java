package com.wgc.concurrent.locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author wgc
 * @Description //
 * @Date 2021/12/5
 **/
public class LockSupportDemo {
    static Object object = new Object();
    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();
    public static void main(String[] args) {

        // synchronize 版本阻塞唤醒
//        synchronizeWaitNotify();

        // await和signal 版阻塞唤醒
//        awaitSignal();

        // lockSupport 版阻塞唤醒
        lockSupport();


    }

    // lockSupport 版阻塞唤醒
    private static void lockSupport() {
        Thread a = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "准备进行执行park方法进行阻塞");
            // 被阻塞，等待通知等待放行，它需要许可证才可以被放行。
            LockSupport.park();

            //如果先unpark两次，后park一次，线程依然阻塞
//            try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
//            LockSupport.park();
//            LockSupport.park();
            System.out.println(Thread.currentThread().getName()+"已被唤醒");
        }, "A");
        a.start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"唤醒其他线程");
            LockSupport.unpark(a);

            // 如果先unpark两次，后park一次，线程依然阻塞
            LockSupport.unpark(a);
        }, "B").start();
    }

    // await和signal 版阻塞唤醒
    private static void awaitSignal() {
        new Thread(()->{
            // 测试先await后singal，无法唤醒本线程
//            try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName()+"获得锁开始进行阻塞");
                condition.await();
                System.out.println(Thread.currentThread().getName()+"被唤醒");
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        },"A").start();

        new Thread(()->{
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName()+"唤醒其他线程");
                condition.signal();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        },"B").start();
    }

    /**
     * synchronize 结合wait和notify使用。
     * 缺点：必须结合synchronize一起使用
     */
    private static void synchronizeWaitNotify() {
        new Thread(()->{
            // 测试先notify后wait，无法唤醒本线程
//            try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            synchronized (object){
                try {
                    System.out.println(Thread.currentThread().getName()+"准备进行阻塞等待。。。");
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"被唤醒。。。");
            }
        },"A").start();

        new Thread(()->{
            synchronized (object){
                System.out.println(Thread.currentThread().getName()+"唤醒其他线程。。。");
                object.notify();
            }
        },"B").start();
    }


}
