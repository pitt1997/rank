package com.lijs.rank.threadpool.reject_policy;


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 示例：支持异常捕获 + 拒绝后任务落库 + 数据库重试
 *
 * @author author
 * @date 2025-06-23
 * @description
 */
public class RetryableThreadPoolExecutor extends ThreadPoolExecutor {


    public RetryableThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                       long keepAliveTime, TimeUnit unit,
                                       BlockingQueue<Runnable> workQueue,
                                       ThreadFactory threadFactory, SaveToDbRejectedHandler saveToDbRejectedHandler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    // 用于任务执行前记录时间
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        startTime.set(System.nanoTime());
    }

    /**
     * 在 afterExecute 中，检测当前线程池队列是否有空位，如果有空位，就去 数据库中取出此前被拒绝、已经落库的任务，再重新提交执行。
     *
     * @param r the runnable that has completed
     * @param t the exception that caused termination, or null if
     *          execution completed normally
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);

        // 正常任务执行后的异常处理（同前面逻辑，可略）
        if (t == null && r instanceof Future<?>) {
            try {
                ((Future<?>) r).get();
            } catch (Exception e) {
                t = e;
            }
        }

        if (t != null) {
            System.err.println("任务执行异常：" + t.getMessage());
        }

        // 判断当前队列是否还有空间（可用容量）
        BlockingQueue<Runnable> queue = getQueue();
        int capacity = queue.remainingCapacity();
        if (capacity > 0) {
            System.out.println("线程池有空位，尝试从数据库拉取任务执行");

            // 拉取任务并提交执行（这里可一次拉多条）
            Runnable dbTask;
            while (capacity-- > 0 && (dbTask = FakeTaskDatabase.fetchTask()) != null) {
                System.out.println("从数据库捞出任务并执行：" + dbTask);
                this.submit(dbTask);
            }
        }
    }

//    @Override
//    protected void afterExecute(Runnable r, Throwable t) {
//        super.afterExecute(r, t);
//        long time = System.nanoTime() - startTime.get();
//        startTime.remove();
//        System.out.println("任务耗时：" + time / 1_000_000 + " ms");
//
//        if (t == null && r instanceof Future<?>) {
//            try {
//                ((Future<?>) r).get(); // 捕获任务内部异常
//            } catch (CancellationException ce) {
//                t = ce;
//            } catch (ExecutionException ee) {
//                t = ee.getCause();
//            } catch (InterruptedException ie) {
//                Thread.currentThread().interrupt(); // 恢复中断状态
//            }
//        }
//
//        if (t != null) {
//            System.err.println("任务执行异常：" + t.getMessage());
//            // TODO: 落库或补偿
//        }
//    }

    // 自定义拒绝策略：打印日志 + 模拟落库
    public static class SaveToDbRejectedHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.err.println("任务被拒绝，保存到数据库等待重试：" + r);
            // TODO: 实际落库代码
        }
    }

    // 线程工厂（可选）
    public static class NamedThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNum = new AtomicInteger(1);
        private final String namePrefix;

        public NamedThreadFactory(String prefix) {
            this.namePrefix = prefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, namePrefix + "-" + threadNum.getAndIncrement());
        }
    }
}
