package com.lijs.rank.nacos;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ljs
 * @date 2024-12-30
 * @description
 */
public class DynamicThreadPool {

    private volatile ThreadPoolExecutor threadPool;

    public DynamicThreadPool() {
        this.threadPool = createThreadPool(10, 20, 100);
    }

    private ThreadPoolExecutor createThreadPool(int corePoolSize, int maximumPoolSize, int queueCapacity) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                60L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueCapacity),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public void updateThreadPool(int corePoolSize, int maximumPoolSize, int queueCapacity) {
        ThreadPoolExecutor newThreadPool = createThreadPool(corePoolSize, maximumPoolSize, queueCapacity);

        // 将旧线程池中的任务迁移到新线程池
        BlockingQueue<Runnable> oldQueue = threadPool.getQueue();
        while (!oldQueue.isEmpty()) {
            Runnable task = oldQueue.poll();
            if (task != null) {
                newThreadPool.execute(task);
            }
        }

        // 关闭旧线程池
        threadPool.shutdown();

        // 替换线程池
        this.threadPool = newThreadPool;
    }

    public ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

//    @NacosConfigListener(dataId = "threadpool-config", group = "DEFAULT_GROUP")
//    @NacosConfigListener(dataId = "threadpool-config.properties", timeout = 3000)
//    public void onConfigChange(String newConfig) {
//        int newCoreSize = parseCoreSize(newConfig);
//        int newMaxSize = parseMaxSize(newConfig);
//        dynamicThreadPool.updatePoolSize(newCoreSize, newMaxSize);
//    }
}