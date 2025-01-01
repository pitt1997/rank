package com.lijs.rank.thread;

import java.util.concurrent.Semaphore;

/**
 * @author ljs
 * @date 2024-12-25
 * @description
 */
public class SemaphoreThread {

    public static void main(String[] args) {
        System.out.println("线程操作:");
        threadTest();
    }

    // 线程测试
    private static void threadTest() {
        // 交替打印奇数偶数1-100
        PrintOddEvenSSemaphore();
    }

    private static int tCount = 1;

    private static final Object o = new Object();

    private static Semaphore semaphore = new Semaphore(1);

    private static void PrintOddEven() {
        Thread t1 = new Thread(() -> {
            while (tCount < 100) {
                synchronized (o) {
                    if (tCount % 2 == 1) {
                        System.out.println(Thread.currentThread() + "打印奇数:" + tCount);
                        tCount++;
                        o.notify();
                    } else {
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, "奇数线程");

        Thread t2 = new Thread(() -> {
            while (tCount < 100) {
                synchronized (o) {
                    if (tCount % 2 == 0) {
                        System.out.println(Thread.currentThread() + "打印偶数:" + tCount);
                        tCount++;
                        o.notify();
                    } else {
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, "偶数线程");

        t1.start();
        t2.start();
    }

    private static void PrintOddEvenSSemaphore() {
        Thread t1 = new Thread(() -> {
            while (tCount < 100) {
                try {
                    semaphore.acquire();
                    if (tCount % 2 == 1) {
                        System.out.println(Thread.currentThread() + "打印奇数:" + tCount);
                        tCount++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        }, "奇数线程");

        Thread t2 = new Thread(() -> {
            while (tCount < 100) {
                try {
                    semaphore.acquire();
                    if (tCount % 2 == 0) {
                        System.out.println(Thread.currentThread() + "打印偶数:" + tCount);
                        tCount++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        }, "偶数线程");

        t1.start();
        t2.start();

    }

    // 反面教材 该代码会出现死锁
    // 1. 线程未区分执行顺序
    // 线程 t1 和 t2 直接同时启动。假设 t1（奇数线程）先获取锁并打印了一个奇数后调用 o.notify()，这时会唤醒 t2（偶数线程）。
    // 但在 t2 被唤醒后，发现此时打印的并不是偶数，执行 o.wait() 进入等待状态。
    // 同时，t1 继续执行下一个循环，因为此时 t2 已经在等待状态，o.wait() 会导致 t1 自己也等待，从而两个线程都进入等待状态，发生死锁。
    // 2. wait/notify 错误使用
    // o.wait() 会让当前线程释放锁并进入等待队列，但如果下一个被唤醒的线程不符合预期（如奇数线程本应唤醒偶数线程，结果自己又被唤醒），则又会进入等待状态。
    // 这种场景下，notify() 只能随机唤醒一个等待线程，无法保证被唤醒的线程是否是预期的线程。
    private static void PrintOddEvenDeadLock() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                synchronized (o) {
                    if (i % 2 == 1) {
                        System.out.println(Thread.currentThread() + "打印奇数:" + i);
                        o.notify();
                    } else {
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("t1-" + i);
            }
        }, "奇数线程");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                synchronized (o) {
                    if (i % 2 == 0) {
                        System.out.println(Thread.currentThread() + "打印偶数:" + i);
                        o.notify();
                    } else {
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("t2-" + i);
            }
        }, "偶数线程");

        t1.start();
        t2.start();
    }
}
