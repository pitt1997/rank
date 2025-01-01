package com.lijs.rank.nacos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ljs
 * @RefreshScope 动态刷新 Bean 属性
 * @date 2024-12-30
 * @description
 */
@Component
@RefreshScope
public class DynamicThreadPoolConfig {

    @Value("${threadpool.corePoolSize:10}")
    private int corePoolSize;

    @Value("${threadpool.maximumPoolSize:20}")
    private int maximumPoolSize;

    @Value("${threadpool.queueCapacity:100}")
    private int queueCapacity;

    public ThreadPoolExecutor buildThreadPool() {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                60L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueCapacity),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
