package com.lijs.rank.deadlock;

/**
 * @author ljs
 * @date 2024-12-31
 * @description
 */
public class DeadLock {

    private static void deadLock() {
        Object a = new Object();
        Object b = new Object();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (a) {
                    try {
                        System.out.println("t1-a");
                        Thread.sleep(1000);
                        synchronized (b) {
                            System.out.println("t1-b");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (b) {
                    try {
                        System.out.println("t2-b");
                        Thread.sleep(1000);
                        synchronized (a) {
                            System.out.println("t2-a");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t1.start();
        t2.start();
    }

}
