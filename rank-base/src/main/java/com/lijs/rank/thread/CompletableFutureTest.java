package com.lijs.rank.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture 常用 API 示例
 *
 * @author ljs
 * @date 2026-10-10
 * @description
 */
public class CompletableFutureTest {

    // 生产环境不建议用默认的 ForkJoinPool.commonPool()，推荐自定义线程池
    private static final ExecutorService POOL = Executors.newFixedThreadPool(4);

    public static void main(String[] args) {
        // 1. 链式调用：supplyAsync 提交异步任务 -> thenApply 转换结果 -> thenAccept 消费结果
        CompletableFuture.supplyAsync(() -> {
                    sleep(500);
                    return "hello";
                }, POOL)
                .thenApply(s -> s + " world")              // 转换：String -> String
                .thenApply(String::toUpperCase)            // 再次转换
                .thenAccept(r -> System.out.println("1. 链式调用结果: " + r)); // 消费，无返回值

        // 2. thenCombine：合并两个独立异步任务的结果
        CompletableFuture<Integer> priceFuture = CompletableFuture.supplyAsync(() -> {
            sleep(300);
            return 100; // 查询商品价格
        }, POOL);
        CompletableFuture<Integer> freightFuture = CompletableFuture.supplyAsync(() -> {
            sleep(300);
            return 20;  // 查询运费
        }, POOL);
        CompletableFuture<Integer> totalFuture = priceFuture.thenCombine(freightFuture, Integer::sum);
        System.out.println("2. 商品价格 + 运费 = " + totalFuture.join());

        // 3. allOf：等待所有任务完成（对应 anyOf 是等任意一个完成）
        CompletableFuture<Void> task1 = CompletableFuture.runAsync(() -> {
            sleep(400);
            System.out.println("3. 任务1完成");
        }, POOL);
        CompletableFuture<Void> task2 = CompletableFuture.runAsync(() -> {
            sleep(400);
            System.out.println("3. 任务2完成");
        }, POOL);
        CompletableFuture.allOf(task1, task2).join(); // 阻塞等待全部完成
        System.out.println("3. 所有任务完成");

        // 4. 异常处理：exceptionally 相当于 try-catch，handle 可同时拿到结果和异常
        CompletableFuture<String> safe = CompletableFuture.supplyAsync(() -> {
                    throw new RuntimeException("远程调用失败");
                }, POOL)
                .thenApply(r -> r + " 正常逻辑不会执行")
                .exceptionally(e -> "降级默认值"); // 捕获异常，返回兜底值
        System.out.println("4. exceptionally 结果: " + safe.join());

        String handleResult = CompletableFuture.supplyAsync(() -> 10 / 0, POOL)
                .handle((r, e) -> {
                    if (e != null) {
                        return "出错了: " + e.getCause();
                    }
                    return "结果: " + r;
                }).join();
        System.out.println("4. handle 结果: " + handleResult);

        POOL.shutdown();
    }

    private static void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}