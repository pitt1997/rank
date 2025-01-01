package com.lijs.rank.nacos;

import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ljs
 * @date 2024-12-30
 * @description
 */
@Component
public class DynamicThreadPoolManager {
    private ThreadPoolExecutor threadPool;

    public DynamicThreadPoolManager() {
        threadPool = new ThreadPoolExecutor(10, 20, 60L,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(100),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public synchronized void updateThreadPool(int corePoolSize, int maximumPoolSize, int queueCapacity) {
        threadPool.setCorePoolSize(corePoolSize);
        threadPool.setMaximumPoolSize(maximumPoolSize);
        threadPool.getQueue().clear(); // 清空旧任务（可选）
        threadPool.getQueue().addAll(new ArrayBlockingQueue<>(queueCapacity));
    }

    public ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }
}
