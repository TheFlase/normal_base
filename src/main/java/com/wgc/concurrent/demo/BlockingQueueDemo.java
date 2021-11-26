package com.wgc.concurrent.demo;

/**
 * @Author wgc
 * @Description // 阻塞队列示例
 * @Date 2021/11/26
 **/

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞队列
 * 概念：它是一个队列，遵循FIFO。通常地，一个线程往队列put元素，一个线程往队列里面take元素。当阻塞队列为空时，从队列中获取元素的操作将会被阻塞。当队列为满时，往队列里面添加元素的操作将会被阻塞。
 * 为什么需要阻塞队列？在多线程领域，所谓阻塞，指的是线程在某些情况下会被挂起（即阻塞），一旦条件满足，被挂起的线程又会自动被唤醒。使用BlockingQueue的好处是我们不需要关系什么时候需要线程阻塞，什么时候需要唤醒线程。兼顾效率和安全的情况下，降低程序的复杂性。
 *
 * 常用：
 * ArrayBlockingQueue:是一个基于数组的有界阻塞队列，此队列按照FIFO原则对元素进行排序。
 * LinkedBlockingQueue是一个基于链表结构的有界阻塞队列，此队列按照FIFO排序元素，吞吐量高于ArrayBlockingQueue.大小默认为Integer.Max_VALUE，慎用。
 * SynchronousQueue:一个不存储元素的阻塞队列。每个插入操作必须等到另外一个线程调用移除操作，否则插入操作一直处于阻塞状态，吞吐量通常要高于LinkedBlockingQueue。
 * 较常用：
 * PriorityBlockingQueue:支持优先级排序的无界阻塞队列。
 * DelayQueue:使用优先级队列实现的延迟无界阻塞队列。
 * LinkedTransferQueue:由链表结构组成的无界阻塞队列。
 * LinkedBlockingDeque:由链表结构组成的双向阻塞队列。
 *
 * BlockingQueue的核心用法
 * 方法类型     抛出异常  特殊值    阻塞       超时
 * 插入           add(e)     offer(e)   put(e)    offer(e,time,unit)
 * 移除         remove()   poll()      take()     poll(time,unit)
 * 检查         element()   peek()    不可用    不可用
 *
 * 抛异常：
 * 当阻塞队列满时，往队列add元素抛IllegalStateException:Queue full
 * 当阻塞队列空时，往队列remove抛NoSuchElementException
 *
 * 特殊值：
 * 插入方法，成功true，失败false
 * 移除方法，成功返回出队列的元素，队列里面没有返回null
 *
 * 一直阻塞：
 * 当队列满时，生产者线程继续往里put元素，队列会一直阻塞生产线程直到put数据or响应中断退出。
 * 当阻塞队列空时，消费者线程试图从队列中take元素，队列会一直阻塞消费者线程直到队列可用。
 *
 * 超时退出：
 * 当阻塞队列满时，队列会阻塞生产者线程一定时间，超过限时后生产者线程会退出。
 */
public class BlockingQueueDemo {
    public static void main(String[] args) {
        // 抛异常的操作
//        exceptionOperate();

        // 特殊值操作
//        SpecialOperate();

        // 阻塞操作
//        blockingOperate();

        // 超时操作
        timeoutOperate();

    }

    // 超时操作
    private static void timeoutOperate() {
        BlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
        try {
            blockingQueue.offer("a",1L, TimeUnit.SECONDS);
            blockingQueue.offer("b",1L, TimeUnit.SECONDS);
            blockingQueue.offer("c",1L, TimeUnit.SECONDS);
            System.out.println("======插完3个元素=======");
            blockingQueue.offer("d",3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 阻塞操作
    private static void blockingOperate() {
        BlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
        try {
            blockingQueue.put("a");
            blockingQueue.put("b");
            blockingQueue.put("c");
            System.out.println("===============");
//            blockingQueue.put("d");
            blockingQueue.take();
            blockingQueue.take();
            blockingQueue.take();
            // 没元素可取，阻塞
            blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 特殊值操作
    private static void SpecialOperate() {
        BlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        System.out.println(blockingQueue.offer("d"));

        System.out.println(blockingQueue.peek());

        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
    }

    // 抛异常的操作
    private static void exceptionOperate() {
        BlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
        blockingQueue.add("a");
        blockingQueue.add("b");
        blockingQueue.add("c");
//        blockingQueue.add("d"); // 超出容量add()插入抛Queue full异常

        // 试取队首元素,不清除
        System.out.println(blockingQueue.element());

        blockingQueue.remove();
        blockingQueue.remove();
        blockingQueue.remove();
//        blockingQueue.remove();// remove()超出容量抛NoShchElementException异常
    }
}
