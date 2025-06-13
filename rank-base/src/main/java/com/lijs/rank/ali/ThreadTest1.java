package com.lijs.rank.ali;

import java.util.concurrent.Semaphore;

/**
 * @author author
 * @date 2025-06-12
 * @description
 */
public class ThreadTest1 {

    private static final Integer MAX = 100;
    private static int index = 1;
    private static final Semaphore semaphore = new Semaphore(1);

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (index < MAX) {
                try {
                    semaphore.acquire();
                    if (index % 2 == 1) {
                        System.out.println("奇数：" + index++);
                    }
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaphore.release();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            while (index <= MAX) {
                try {
                    semaphore.acquire();
                    if (index % 2 == 0) {
                        System.out.println("偶数：" + index++);
                    }
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaphore.release();
                }
            }
        });

        t1.start();
        t2.start();
    }

}