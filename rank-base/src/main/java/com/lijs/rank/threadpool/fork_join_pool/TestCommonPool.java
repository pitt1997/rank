package com.lijs.rank.threadpool.fork_join_pool;

import java.util.concurrent.ForkJoinPool;

/**
 * @author author
 * @date 2026-10-10
 * @description
 */
public class TestCommonPool {
    public static void main(String[] args) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        System.out.println("并行度 parallelism：" + pool.getParallelism());
        System.out.println("活跃线程 activeThreadCount：" + pool.getActiveThreadCount());
        System.out.println("队列等待任务数：" + pool.getQueuedTaskCount());
        System.out.println("是否是守护线程池：" + pool.isShutdown());
    }
}