package com.wgc.concurrent.locks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author wgc
 * @Description // 读写锁 示例
 * 对ReentrantReadWriteLock来说，其读锁是共享锁，其写锁是独占锁。
 * 读锁的共享锁可以保证并发读是高效的。读写，写读，写写的过程是互斥的。
 * @Date 2021/11/25
 **/
public class ReentrantReadWriteLockDemo {
    private volatile Map<String,Object> map = new HashMap<>();
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private static final String value = "1";


    public static void main(String[] args) {
        ReentrantReadWriteLockDemo readWriteLockDemo = new ReentrantReadWriteLockDemo();
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            new Thread(()->{
                readWriteLockDemo.put(String.valueOf(finalI),value);
            },"写入线程"+i).start();
        }
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

        for (int i = 0; i < 3; i++) {
            int finalI = i;
            new Thread(()->{
                readWriteLockDemo.get(String.valueOf(finalI));
            },"读取线程"+i).start();
        }

    }

    private void put(String key,Object value){
        rwLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()+"开始写入key:"+key+",value:"+value);
            map.put(key,value);
            // 模拟处理时间
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName()+"写入完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    private Object get(String key){
        rwLock.readLock().lock();
        Object result = null;
        try {
            System.out.println(Thread.currentThread().getName()+"开始读取key:"+key);
            result = map.get(key);
            System.out.println(Thread.currentThread().getName()+"读取结束，结果是："+String.valueOf(result));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rwLock.readLock().unlock();
        }
        return null;
    }
}
