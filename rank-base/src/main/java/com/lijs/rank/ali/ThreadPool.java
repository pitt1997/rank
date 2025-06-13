package com.lijs.rank.ali;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author author
 * @date 2025-06-12
 * @description
 */
public class ThreadPool {

    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5,
                10,
                100L,
                TimeUnit.MICROSECONDS,
                new LinkedBlockingQueue<>(100),
                new ThreadPoolExecutor.CallerRunsPolicy() // 这里是重点
        );

        threadPoolExecutor.execute(() -> {
            //
        });

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });

    }

}