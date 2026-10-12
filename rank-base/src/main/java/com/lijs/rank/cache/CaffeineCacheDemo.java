package com.lijs.rank.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine 缓存 demo：本地缓存中性能最强的一个（基于 JDK8，Spring Boot 2.x 默认本地缓存实现）。
 * <p>
 * 原理要点（对比 lru 包下手写的 LRUCache 更好理解）：
 * <p>
 * 1. 淘汰算法 W-TinyLFU（不是纯 LRU）：
 * 纯 LRU 的问题是对"偶发批量扫描"不友好 —— 扫一遍冷数据就能把全部热数据挤出去。
 * Caffeine 用【新key先进入 Window LRU 小窗】+【CountMinSketch 频次草图统计访问频率】，
 * 要淘汰时比较"频率"，新来的冷 key 频率低，挤不掉老的热 key。
 * CountMinSketch 只是一个极小的 hash 桶数组，空间远小于真实数据，拿精度换内存。
 * <p>
 * 2. 数据结构：类似 ConcurrentHashMap 的分段环（RingBuffer 记录读写事件，异步维护淘汰），
 * 读写几乎无锁，所以吞吐远高于加锁 LRU / synchronized Map。
 * <p>
 * 3. 过期策略：expireAfterWrite（写入后 N 秒过期，最常用）、expireAfterAccess（访问后 N 秒）、
 * expireAfterVariable（自定义，如按每个条目自己的 TTL）。
 * 过期数据的清理同样是"惰性 + 异步"，不启专门线程扫描全表。
 * <p>
 * 4. recordStats()：命中率、淘汰数、加载平均耗时等指标，配合监控用。
 *
 * @author ljs
 * @date 2026-10-12
 * @description Caffeine 缓存核心特性演示
 */
public class CaffeineCacheDemo {

    /**
     * 模拟昂贵的回源加载（比如查库），统计加载次数用来观察命中情况
     */
    private static int loadCount = 0;

    public static void main(String[] args) throws InterruptedException {
        demo1EvictionBySize();
        demo2ExpireAndReload();
        demo3ManualCacheAndStats();
    }

    /**
     * 演示一：maximumSize 容量淘汰（W-TinyLFU）。
     * 容量 3，先放入 k1~k4，k1/k2 被淘汰；
     * 再反复访问 k3、k4 抬高它们的频率，此时插入 k5、k6 ——
     * 新 key 挤不掉高频率的老 key，挤掉的是低频的。
     */
    private static void demo1EvictionBySize() {
        System.out.println("=== 演示一：maximumSize 容量淘汰（W-TinyLFU）===");
        LoadingCache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(3)
                .build(CaffeineCacheDemo::loadFromDB);

        cache.get("k1");
        cache.get("k2");
        cache.get("k3");
        cache.get("k4"); // 超容量，k1 被淘汰（最早且无后续访问）
        System.out.println("放入 k1~k4 后 size=" + cache.estimatedSize());

        // 反复访问 k3/k4 提升频率，让它们成为"热数据"
        for (int i = 0; i < 5; i++) {
            cache.get("k3");
            cache.get("k4");
        }
        cache.get("k5");
        cache.cleanUp();
        System.out.println("热数据 k3/k4 被访问后插入 k5，size=" + cache.estimatedSize());
        System.out.println("k3(热) 还在吗: " + (cache.getIfPresent("k3") != null)
                + "，k2(冷) 还在吗: " + (cache.getIfPresent("k2") != null));
        System.out.println();
    }

    /**
     * 演示二：expireAfterWrite 过期 + LoadingCache 自动回源。
     * 3 秒过期：过期前第二次 get 命中缓存（loadCount 不变）；
     * 过期后 get 触发重新加载（loadCount +1），对调用方透明。
     */
    private static void demo2ExpireAndReload() throws InterruptedException {
        System.out.println("=== 演示二：expireAfterWrite(3s) 过期回源 ===");
        LoadingCache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build(CaffeineCacheDemo::loadFromDB);

        System.out.println(cache.get("user:1") + "（首次 get，加载一次）");
        System.out.println(cache.get("user:1") + "（3s 内再 get，命中缓存）");
        System.out.println("睡 3.5s 让条目过期...");
        Thread.sleep(3500);
        System.out.println(cache.get("user:1") + "（过期后再 get，自动重新加载）");
        System.out.println("累计回源次数 loadCount=" + loadCount);
        System.out.println();
    }

    /**
     * 演示三：手动 Cache（不用 LoadingCache）+ 移除监听 + 命中率统计。
     */
    private static void demo3ManualCacheAndStats() {
        System.out.println("=== 演示三：手动 Cache + removalListener + recordStats ===");
        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .recordStats()
                .removalListener((String key, String value, RemovalCause cause) ->
                        System.out.println("  [移除监听] key=" + key + "，原因=" + cause))
                .build();

        cache.put("a", "1");
        cache.put("b", "2");
        cache.invalidate("a"); // 主动失效，触发移除监听（原因 EXPLICIT）

        // get(key, mappingFunction)：不存在才计算，存在直接返回（原子操作，天然防缓存击穿）
        cache.get("c", k -> loadFromDB(k));

        System.out.println("a=" + cache.getIfPresent("a") + "（已被 invalidate）");
        System.out.println("c=" + cache.getIfPresent("c"));
        System.out.printf("命中率=%.2f%%（hitCount=%d, missCount=%d, evictionCount=%d）%n",
                cache.stats().hitRate() * 100,
                cache.stats().hitCount(),
                cache.stats().missCount(),
                cache.stats().evictionCount());
    }

    private static String loadFromDB(String key) {
        loadCount++;
        return "DB加载结果[" + key + "]";
    }
}
