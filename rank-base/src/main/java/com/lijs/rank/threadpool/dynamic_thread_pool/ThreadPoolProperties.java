package com.lijs.rank.threadpool.dynamic_thread_pool;

/**
 * @author author
 * @date 2025-06-23
 * @description
 */

// 配置中心参数

// thread.pool:
//  coreSize: 2
//  maxSize: 10
//  queueCapacity: 100
//  keepAliveSeconds: 60
//  threadNamePrefix: "biz-thread-"

//@Data
//@Component
//@RefreshScope
//@ConfigurationProperties(prefix = "thread.pool")
public class ThreadPoolProperties {
    private int coreSize;
    private int maxSize;
    private int queueCapacity;
    private int keepAliveSeconds;
    private String threadNamePrefix;

    public int getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(int coreSize) {
        this.coreSize = coreSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }
}