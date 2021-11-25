package com.wgc.concurrent.atomic;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @Author wgc
 * @Description //原子时间戳引用 解决ABA问题
 * @Date 2021/11/24
 **/
public class AtomicStampedReferenceDemo {
    public static void main(String[] args) {
        AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
        AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<>(100,1);

        System.out.println("========ABA问题产生==============");
        new Thread(()->{
            atomicReference.compareAndSet(100,101);
            atomicReference.compareAndSet(101,100);
        },"A").start();

        new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            atomicReference.compareAndSet(100,110);
            System.out.println(Thread.currentThread().getName()+"\t 修改后值为："+atomicReference.get());
        },"B").start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("=========解决ABA=========");

        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t 修改前版本号为："+stampedReference.getStamp());
            try {
                // 暂停一秒让D线程拿到跟C修改前一样的版本号
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stampedReference.compareAndSet(100,110,
                    stampedReference.getStamp(), stampedReference.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+"\t 第一次修改后版本号为："+stampedReference.getStamp());
            stampedReference.compareAndSet(110,100,
                    stampedReference.getStamp(), stampedReference.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+"\t 第二次修改后版本号为："+stampedReference.getStamp());

        },"C").start();

        new Thread(()->{
            int version = stampedReference.getStamp();
            System.out.println(Thread.currentThread().getName()+"\t 修改前版本号为："+version);
            try {
                // 暂停3秒，让线程C先操作
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean updateResult = stampedReference.compareAndSet(100, 110,
                    version, version + 1);
            System.out.println(Thread.currentThread().getName()+"\t 修改结果为："+updateResult
                    +"\t修改后值为："+stampedReference.getReference()+"\t 当前最新版本号为："+stampedReference.getStamp());

        },"D").start();

    }
}
