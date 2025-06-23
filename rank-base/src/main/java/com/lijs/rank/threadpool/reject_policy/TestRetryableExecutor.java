package com.lijs.rank.threadpool.reject_policy;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author author
 * @date 2025-06-23
 * @description
 */
public class TestRetryableExecutor {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(2);
        RetryableThreadPoolExecutor executor = new RetryableThreadPoolExecutor(
                2, 2, 10, TimeUnit.SECONDS,
                queue,
                new RetryableThreadPoolExecutor.NamedThreadFactory("worker"),
                new RetryableThreadPoolExecutor.SaveToDbRejectedHandler()
        );

        // 提交 5 个任务，其中后面 1-2 个任务会被拒绝
        for (int i = 1; i <= 5; i++) {
            int finalI = i;
            executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " 执行任务 " + finalI);
                if (finalI == 3) {
                    throw new RuntimeException("任务3模拟异常");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
    }
}
