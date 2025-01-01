package com.lijs.rank.thread;

/**
 * @author ljs
 * @date 2024-12-25
 * @description
 */
public class WaitNotifyThread {

    public static void main(String[] args) {
        System.out.println("===>>>wait/notify");
        testThreadNotify();
        System.out.println("===>>>wait/notify");
    }

    static int count = 1;

    private static void testThreadNotify() {
        final Object o = new Object();
        Thread t1 = new Thread(() -> {
            while (count <= 100) {
                synchronized (o) {
                    if (count % 2 == 1) {
                        System.out.println("奇数:" + count);
                        count++;
                        o.notify();
                    } else {
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }, "奇数");

        Thread t2 = new Thread(() -> {
            while (count <= 100) {
                synchronized (o) {
                    if (count % 2 == 0) {
                        System.out.println("偶数:" + count);
                        count++;
                        o.notify();
                    } else {
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }, "偶数");
        t1.start();
        t2.start();
    }

}
