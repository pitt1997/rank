package com.lijs.rank.threadpool.thread_pool_type;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author author
 * @date 2025-06-23
 * @description
 */
public class ThreadPoolType {

    public static void main(String[] args) {

        ThreadFactory factory = Executors.defaultThreadFactory();
        Thread t = factory.newThread(() -> System.out.println("Hello"));
        t.start();

        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        poolExecutor.execute(() -> {
            System.out.println("线程池A1");
        });
        poolExecutor.execute(() -> {
            System.out.println("线程池A2");
        });
        poolExecutor.execute(() -> {
            System.out.println("线程池A3");
        });
    }

}