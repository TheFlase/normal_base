package com.wgc.concurrent.locks;

/**
 * @Author wgc
 * @Description // 死锁demo
 * jps =>查看死锁线程
 * jstack 线程号 => 排查死锁原因
 * @Date 2021/12/5
 **/
public class DeadLockDemo {
    public static void main(String[] args) {
        String lock1 = "lock1";
        String lock2 = "lock2";

        Resouce resouce1 = new Resouce(lock1, lock2);
        Resouce resouce2 = new Resouce(lock2, lock1);
        new Thread(resouce1,"线程A").start();

        new Thread(resouce2,"线程B").start();
    }

    static class Resouce implements Runnable{
        private String lock1;
        private String lock2;

        public Resouce(String lock1, String lock2) {
            this.lock1 = lock1;
            this.lock2 = lock2;
        }

        @Override
        public void run() {
            synchronized (lock1){
                System.out.println(Thread.currentThread().getName()+"持有锁"+lock1+"，尝试获取"+lock2);
                try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
                synchronized (lock2){
                    System.out.println(Thread.currentThread().getName()+"持有锁"+lock2+"，尝试获取"+lock1);
                }
            }
        }
    }
}
