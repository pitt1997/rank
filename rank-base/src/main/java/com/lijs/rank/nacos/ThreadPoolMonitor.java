package com.lijs.rank.nacos;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 监控与报警：
 * <p>
 * 定期采集线程池的运行数据并上报。
 * 当线程池指标异常时，触发报警。
 *
 * @author ljs
 * @date 2024-12-30
 * @description
 */
public class ThreadPoolMonitor {
    public void monitor(DynamicThreadPool pool) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            // int activeCount = pool.getActiveCount();
            // int queueSize = pool.getQueue().size();
            // 上报或记录指标
        }, 0, 10, TimeUnit.SECONDS);
    }
}