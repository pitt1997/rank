package com.lijs.rank.ratelimiter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 手写简化版令牌桶限流器，算法照搬 Guava RateLimiter 的 SmoothBursty 实现。
 * <p>
 * 核心原理（与直觉不同的两点）：
 * <p>
 * 1. 惰性补充令牌，没有后台线程。
 * 传统做法是起一个定时线程每 X 毫秒往桶里加一个令牌，Guava 完全不是这样：
 * 只记录「上次发放令牌的时间戳 nextFreeTicketNanos」，每次 acquire 时
 * 用 (now - 上次时间) × 速率 一次性把这段时间应有的令牌补出来（见 resync 方法）。
 * 没有请求就永远不计算 —— 零线程、零定时器开销。
 * <p>
 * 2. 令牌不够时，当前请求立即通过，债记给下一个请求。
 * 这是最精妙的地方（见 reserve 方法）：
 * - 拿存量令牌 storedPermits 抵扣一部分，不够的部分视为「预支未来产生的令牌」；
 * - 预支的代价 = freshPermits × stableInterval，直接加到 nextFreeTicketNanos 上；
 * - 当前请求需要等待的时间，早在上一次 reserve 时就算好了（上一次欠的债），
 * 本次透支产生的债由「下一个」请求来还。
 * 效果： acquire 永远能立刻返回等待时长（可能为 0），真正按需平滑排队，
 * 整体速率精确收敛到 QPS。
 *
 * @author ljs
 * @date 2026-10-12
 * @description 手写令牌桶限流器（Guava SmoothBursty 思路）
 */
public class SimpleRateLimiter {

    /**
     * 每秒发放的令牌数（QPS）。
     */
    private final double permitsPerSecond;

    /**
     * 稳定间隔（微秒）：两个令牌之间的产生间隔 = 1秒 / QPS。
     * 例如 QPS=2 时，每个令牌间隔 500_000 微秒（500ms）。
     */
    private final double stableIntervalMicros;

    /**
     * 桶容量（最大存量令牌数）。SmoothBursty 默认存 1 秒的量：
     * QPS=2 最多攒 2 个令牌，攒满后不再累积 —— 这就是"允许突发"的上限。
     */
    private final double maxPermits;

    /**
     * 当前存量令牌数（可为小数，因为按时间比例补发）。
     */
    private double storedPermits;

    /**
     * 核心字段：下一次请求可以"不产生新债"直接拿到令牌的时间点（纳秒）。
     * 三重含义：
     * a) 它之前的空闲期可以折算成存量令牌（resync 用）；
     * b) 它减去 now 就是当前请求需要睡眠的等待时长；
     * c) 透支时它被推向未来，等于把债转嫁给下一个请求。
     */
    private long nextFreeTicketNanos;

    public SimpleRateLimiter(double permitsPerSecond) {
        this.permitsPerSecond = permitsPerSecond;
        this.stableIntervalMicros = TimeUnit.SECONDS.toMicros(1) / permitsPerSecond;
        this.maxPermits = permitsPerSecond;
        this.storedPermits = 0.0D;
    }

    /**
     * 获取 1 个令牌，阻塞直到拿到，返回实际等待的毫秒数。
     */
    public double acquire() {
        return acquire(1);
    }

    /**
     * 获取 permits 个令牌，阻塞直到拿到，返回实际等待的毫秒数。
     */
    public double acquire(int permits) {
        // 微秒级计时，SleepingStopwatch 的等价物
        long waitNanos = reserveAndCalcWait(permits, System.nanoTime());
        // 注意：睡眠在 synchronized 外面！
        // 记账是瞬时完成的，睡眠不持锁，其他线程可以并发进来记账排队。
        LockSupport.parkNanos(waitNanos);
        return waitNanos / 1_000_000.0D;
    }

    /**
     * 尝试在 timeout 内拿到 1 个令牌：等得到就返回 true，等不到立刻返回 false（不阻塞）。
     */
    public boolean tryAcquire(long timeout, TimeUnit unit) {
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        long waitNanos;
        synchronized (this) {
            long now = System.nanoTime();
            // 等待截止时间都追不上还债时间点，必然超时，直接拒绝（不产生新的债）
            if (nextFreeTicketNanos > deadline) {
                return false;
            }
            waitNanos = reserve(1, now);
        }
        LockSupport.parkNanos(waitNanos);
        return true;
    }

    /**
     * 记账：预订 permits 个令牌，返回当前请求需要等待的纳秒数。
     */
    private long reserveAndCalcWait(int permits, long now) {
        synchronized (this) {
            return reserve(permits, now);
        }
    }

    /**
     * 核心算法，全部在锁内执行，纯记账、不做任何阻塞操作。
     */
    private long reserve(int permits, long now) {
        // 第一步：惰性补令牌，把上次到现在之间"应得"的令牌一次性补进桶里
        resync(now);

        // 第二步：算当前请求要等多久 = 上一次记账欠下的债（可能为 0，甚至为负的余量）
        long returnNanos = nextFreeTicketNanos - now;

        // 第三步：拆分本次请求
        // 存量令牌能覆盖的部分（免费，不产生债）
        double storedPermitsToSpend = Math.min(permits, this.storedPermits);
        // 覆盖不了的部分，需要"预支"未来产生的令牌
        double freshPermits = permits - storedPermitsToSpend;

        // 第四步：预支的代价转嫁未来 —— 把产生这些新令牌所需的时间加到 nextFreeTicketNanos 上。
        // 当前请求不等这段（它只等第二步算出的旧债），下一个请求会等到。
        long freshWaitNanos = (long) (freshPermits * stableIntervalMicros * 1000);
        this.nextFreeTicketNanos += freshWaitNanos;

        // 第五步：扣掉消耗的存量令牌
        this.storedPermits -= storedPermitsToSpend;

        return Math.max(returnNanos, 0);
    }

    /**
     * 惰性补令牌：now 比 nextFreeTicketNanos 晚多少，就按速率补多少令牌（封顶 maxPermits）。
     * 如果 now 还没到 nextFreeTicketNanos（前面有人透支了），什么都不做。
     */
    private void resync(long now) {
        if (now > nextFreeTicketNanos) {
            // 流逝的微秒数 / 每个令牌的微秒间隔 = 这段时间新产生的令牌数
            double newPermits = (now - nextFreeTicketNanos) / 1000.0 / stableIntervalMicros;
            this.storedPermits = Math.min(maxPermits, this.storedPermits + newPermits);
            this.nextFreeTicketNanos = now;
        }
    }

    /**
     * 演示三个典型场景，观察输出时间戳即可理解行为。
     */
    public static void main(String[] args) throws InterruptedException {
        demo1Burst();
        demo2OverdraftAndTryAcquire();
        demo3MultiThread();
    }

    /**
     * 场景一：突发流量。空闲 1.5 秒攒满 2 个令牌（桶容量=QPS=2），
     * 随后连发 5 个请求：前 3 个接近 0ms 通过（2 个存量 + 1 个透支），
     * 第 4、5 个分别等约 500ms、1000ms —— 速率被平滑拉回 QPS=2。
     */
    private static void demo1Burst() throws InterruptedException {
        System.out.println("=== 场景一：突发流量（QPS=2，桶容量 2）===");
        SimpleRateLimiter limiter = new SimpleRateLimiter(2);
        long start = System.nanoTime();
        System.out.println("空闲 1.5s，让桶攒满令牌...");
        Thread.sleep(1500);
        for (int i = 1; i <= 5; i++) {
            double waited = limiter.acquire();
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            System.out.printf("第 %d 个请求：acquire 返回等待 %.0fms，距 demo 开始 %dms%n", i, waited, elapsedMs);
        }
        System.out.println();
    }

    /**
     * 场景二：批量透支 + tryAcquire。
     * 一次性 acquire(5)：当前请求等待 0ms 立即通过（透支），
     * 债 5×500ms=2500ms 转嫁给下一个请求：
     * - tryAcquire(200ms) 等不起，直接返回 false；
     * - 下一个 acquire 老老实实睡 ~2.5s。
     */
    private static void demo2OverdraftAndTryAcquire() throws InterruptedException {
        System.out.println("=== 场景二：批量透支与 tryAcquire（QPS=2）===");
        SimpleRateLimiter limiter = new SimpleRateLimiter(2);
        double waited = limiter.acquire(5);
        System.out.printf("acquire(5) 等待 %.0fms 立即通过（透支了未来的令牌）%n", waited);
        System.out.println("tryAcquire(200ms) -> " + limiter.tryAcquire(200, TimeUnit.MILLISECONDS) + "（债太多，等不起）");
        waited = limiter.acquire();
        System.out.printf("下一个 acquire 等待 %.0fms（替上一笔透支还债）%n%n", waited);
    }

    /**
     * 场景三：多线程并发。3 个线程各抢 3 次令牌（QPS=5，间隔 200ms），
     * 输出的获取时刻基本均匀分布在 200ms 一个 —— 不管谁先抢，整体速率恒定。
     */
    private static void demo3MultiThread() {
        System.out.println("=== 场景三：多线程并发（QPS=5，即 200ms/个）===");
        SimpleRateLimiter limiter = new SimpleRateLimiter(5);
        long start = System.nanoTime();
        for (int t = 1; t <= 3; t++) {
            final String name = "线程" + t;
            new Thread(() -> {
                for (int i = 0; i < 3; i++) {
                    limiter.acquire();
                    long elapsedMs = (System.nanoTime() - start) / 1_000_000;
                    System.out.printf("%s 第 %d 次拿到令牌，时刻 %dms%n", name, i + 1, elapsedMs);
                }
            }, name).start();
        }
    }
}