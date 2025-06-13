package com.lijs.rank.ali;

/**
 * @author author
 * @date 2025-06-12
 * @description
 */
public class ThreadTest {

    private static final Object lock = new Object();
    private static final int MAX = 100;

    private static int index = 1;


    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            // 奇数
            while (index <= MAX) {
                synchronized (lock) {
                    if (index % 2 == 1) {
                        System.out.println("奇数:" + index);
                        index++;
                        lock.notify();
                    } else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        Thread t2 = new Thread(() -> {
            while (index <= MAX) {
                synchronized (lock) {
                    if (index % 2 == 0) {
                        System.out.println("偶数:" + index);
                        index++;
                        lock.notify();
                    } else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        t1.start();
        t2.start();
    }

}