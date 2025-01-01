package com.lijs.rank.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ljs
 * @date 2024-12-25
 * @description
 */
public class ThreadPoolTest {


    public static void main(String[] args) {
        // 线程池
        threadPool();
    }

    // 线程测试
    private static void threadPool() {

        // Executors.newFixedThreadPool(10);

        // 创建一个线程池
        ThreadPoolExecutor executor1 = new ThreadPoolExecutor(
                4,                              // 核心线程数
                5,                             // 最大线程数
                200L,                            // 超时时间
                TimeUnit.SECONDS,               // 超时时间单位
                new LinkedBlockingQueue<>(2), // 工作队列
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略
        );


        // 创建一个线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                4,                              // 核心线程数
                5,                             // 最大线程数
                200L,                            // 超时时间
                TimeUnit.SECONDS,               // 超时时间单位
                new LinkedBlockingQueue<>(2), // 工作队列
                // Executors.defaultThreadFactory(), // 默认线程工厂
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略
        );
        // 提交任务
        for (int i = 0; i < 20; i++) {
            final int taskNumber = i;
            executor.execute(() -> {
                System.out.println("Executing task " + taskNumber + " by " + Thread.currentThread().getName());
                try {
                    Thread.sleep(10000); // 模拟任务执行
                    System.out.println("Executing done task " + taskNumber + " by " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            });
        }

        // 提交任务
        for (int i = 0; i < 20; i++) {
            final int taskNumber = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("gogogo.");
                }
            });
        }

        // 关闭线程池

        executor.shutdown();
    }


}
