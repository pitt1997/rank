package com.lijs.rank.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * Guava 官方 RateLimiter 对照 demo。
 * <p>
 * 用法和 {@link SimpleRateLimiter} 完全同构，可对比两个类的输出：
 * 行为一致说明手写版复刻了 SmoothBursty 的语义。
 * <p>
 * Guava RateLimiter 补充知识点：
 * 1. RateLimiter.create(qps)                 -> SmoothBursty，允许突发（桶容量=1 秒量）
 * 2. RateLimiter.create(qps, 预热期, unit)    -> SmoothWarmingUp，冷启动时发令牌间隔
 * 从大到小线性收窄（梯形曲线），适合数据库连接池等需要预热的目标资源
 * 3. setRate() 运行期动态调整速率
 * 4. 只有单机限流能力；分布式限流要用 Redis + Lua 或 Sentinel 等方案
 *
 * @author ljs
 * @date 2026-10-12
 * @description Guava RateLimiter 使用示例，与手写版对照
 */
public class GuavaRateLimiterDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Guava RateLimiter（QPS=2），突发 5 个请求 ===");
        RateLimiter limiter = RateLimiter.create(2);
        long start = System.nanoTime();
        // Guava 也是惰性补令牌：刚 create 完桶是空的，这里先睡 1.5s 攒令牌，与手写版场景一对齐
        Thread.sleep(1500);
        for (int i = 1; i <= 5; i++) {
            double waited = limiter.acquire();
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            System.out.printf("第 %d 个请求：acquire 返回等待 %.0fms，距 demo 开始 %dms%n", i, waited, elapsedMs);
        }

        System.out.println();
        System.out.println("=== tryAcquire：等不起就拒绝 ===");
        // 一次透支 5 个，产生 2.5s 的债，和手写版场景二对齐
        System.out.printf("acquire(5) 等待 %.0fms 立即通过%n", limiter.acquire(5));
        System.out.println("tryAcquire(200ms) -> " + limiter.tryAcquire(200, TimeUnit.MILLISECONDS));
        System.out.printf("下一个 acquire 等待 %.0fms%n", limiter.acquire());

        System.out.println();
        System.out.println("=== 预热模式（SmoothWarmingUp）：前几个令牌间隔更大，逐步收敛 ===");
        RateLimiter warmup = RateLimiter.create(5, 1, TimeUnit.SECONDS);
        for (int i = 1; i <= 6; i++) {
            System.out.printf("预热模式第 %d 个令牌，间隔 %.0fms%n", i, warmup.acquire() * 1000);
        }
    }
}
