package com.wgc.thread;

/**
 * @Author wgc
 * @Description // 基础版 生产消费者
 * @Date 2021/12/5
 **/
public class ProductConsumerWaitNotifyDemo {
    public static void main(String[] args) {
        Resource resource = new Resource();

            new Thread(()->{
                for (int i = 0; i < 10; i++) {
                    resource.product();
                }
            },"product").start();
            new Thread(()->{
                for (int i = 0; i < 10; i++) {
                    resource.consumer();
                }
            },"consumer").start();
    }
    static class Resource{
        private Object object = new Object();
        private int number;
        private void product(){
            synchronized (object){
                while (number!=0){
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                number++;
                System.out.println(Thread.currentThread().getName()+"生产："+number);
                object.notifyAll();
            }

        }
        private void consumer(){
            synchronized (object){
                while (number==0){
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                number--;
                System.out.println(Thread.currentThread().getName()+"消费："+number);
                object.notifyAll();
            }
        }
    }
}
