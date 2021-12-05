package com.wgc.thread.threadpool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author wgc
 * @Description //线程池使用demo
 * @Date 2021/12/4
 **/
public class ThreadPoolApiDemo {
    public static void main(String[] args) {

        // 简单api使用 demo
//        apiUseSimpleDemo1();

        // 自定义线程参数执行 demo
        apiUseByAjustProperties();
        
    }

    private static void apiUseByAjustProperties(){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,
                5, 100L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());//处理不来直接丢弃任务，不抛异常
//                new ThreadPoolExecutor.DiscardOldestPolicy());//抛弃等待最久的任务，尝试提交新任务
//                new ThreadPoolExecutor.CallerRunsPolicy());//将处理不来的任务返回给调用者
//                new ThreadPoolExecutor.AbortPolicy());//处理不来直接抛异常
        try {
            for (int i = 1; i <= 9; i++) {
                final int temp = i;
                threadPoolExecutor.execute(()->{
                    System.out.println("线程"+Thread.currentThread().getName()+"处理业务："+temp);

                    // 停顿为了模拟等待队列满时，按照maximumPoolSize数量创建临时线程去执行任务
                    try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}

                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPoolExecutor.shutdown();
        }
    }

    /**
     * 简单api使用 demo
     */
    private static void apiUseSimpleDemo1() {
        //        ExecutorService executorService = Executors.newScheduledThreadPool(3);
//        ExecutorService executorService = Executors.newWorkStealingPool(3);


        ExecutorService executorService = Executors.newFixedThreadPool(5);
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        ExecutorService executorService = Executors.newCachedThreadPool();

        try {
            AtomicInteger finalI = new AtomicInteger(1);
            for (int i = 0; i < 10; i++) {
                executorService.execute(()->{
                    System.out.println(Thread.currentThread().getName()+"\t 办理业务:"+ finalI.getAndIncrement());
                });
                try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}
