package com.lijs.rank.threadpool.reject_policy;

/**
 * @author author
 * @date 2025-06-23
 * @description
 */
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FakeTaskDatabase {
    // 模拟数据库中的任务队列（被拒绝落库的）
    private static final Queue<Runnable> dbTasks = new ConcurrentLinkedQueue<>();

    public static void saveTask(Runnable r) {
        dbTasks.offer(r);
        System.out.println("任务已保存到数据库：" + r);
    }

    public static Runnable fetchTask() {
        return dbTasks.poll();
    }
}
