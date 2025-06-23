package com.lijs.rank.threadpool.dynamic_thread_pool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author author
 * @date 2025-06-23
 * @description
 */
@Component
public class DynamicThreadPoolManager {

    private ThreadPoolExecutor executor;

    @Autowired
    private ThreadPoolProperties properties;

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    @PostConstruct
    public void init() {
        executor = new ThreadPoolExecutor(
                properties.getCoreSize(),
                properties.getMaxSize(),
                properties.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(properties.getQueueCapacity()),
                new NamedThreadFactory(properties.getThreadNamePrefix()),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    // 刷新线程池参数
    public void refresh() {
        System.out.println("线程池参数刷新...");
        executor.setCorePoolSize(properties.getCoreSize());
        executor.setMaximumPoolSize(properties.getMaxSize());
        executor.setKeepAliveTime(properties.getKeepAliveSeconds(), TimeUnit.SECONDS);
        // ⚠️ 队列容量不能直接修改（可创建新队列替换）
    }

    public static class NamedThreadFactory implements ThreadFactory {
        private final String prefix;
        private int count = 1;

        public NamedThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, prefix + count++);
        }
    }
}