package com.lijs.rank.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ljs
 * @date 2024-11-21
 * @description
 */
public class OddEvenThread {

    private static final List<Integer> list = new ArrayList<>();
    private static final Lock queueLock = new ReentrantLock();
    private static final Condition takeCondition = queueLock.newCondition();
    private static final Condition putCondition = queueLock.newCondition();
    private static final int capacity = 10; // 队列最大容量

    // 向队列中添加元素
    public static void put(int value) {
        queueLock.lock();
        try {
            // 队列已满时阻塞
            while (list.size() >= capacity) {
                System.out.println("Queue is full, waiting...");
                putCondition.await();
            }
            // 添加元素
            list.add(value);
            System.out.println("Produced: " + value);
            // 通知消费者可以消费
            takeCondition.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Producer interrupted");
        } finally {
            queueLock.unlock();
        }
    }

    // 从队列中获取元素
    public static int take() {
        queueLock.lock();
        try {
            // 队列为空时阻塞
            while (list.isEmpty()) {
                System.out.println("Queue is empty, waiting...");
                takeCondition.await();
            }
            // 移除元素
            int value = list.remove(0);
            System.out.println("Consumed: " + value);
            // 通知生产者可以生产
            putCondition.signalAll();
            return value;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Consumer interrupted");
            return -1; // 错误标志
        } finally {
            queueLock.unlock();
        }
    }

//    public static void main(String[] args) {
//        // 生产者线程
//        Thread producer = new Thread(() -> {
//            for (int i = 0; i < 20; i++) {
//                put(i);
//            }
//        });
//
//        // 消费者线程
//        Thread consumer = new Thread(() -> {
//            for (int i = 0; i < 20; i++) {
//                take();
//            }
//        });
//
//        producer.start();
//        consumer.start();
//    }

    private static final int MAX = 100; // 打印到100
    private static int number = 1;     // 当前打印的数字
    private static final Lock lock = new ReentrantLock(); // 重入锁
    private static final Condition oddCondition = lock.newCondition(); // 奇数条件
    private static final Condition evenCondition = lock.newCondition(); // 偶数条件

    public static void main(String[] args) {
        Thread oddThread = new Thread(() -> printOdd(), "Odd-Thread");
        Thread evenThread = new Thread(() -> printEven(), "Even-Thread");

        oddThread.start();
        evenThread.start();
    }

    // 打印奇数
    public static void printOdd() {
        lock.lock();
        try {
            while (number <= MAX) {
                if (number % 2 == 0) { // 如果是偶数，等待
                    oddCondition.await();
                } else {
                    System.out.println(Thread.currentThread().getName() + " prints: " + number++);
                    evenCondition.signal(); // 唤醒偶数线程
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    // 打印偶数
    public static void printEven() {
        lock.lock();
        try {
            while (number <= MAX) {
                if (number % 2 != 0) { // 如果是奇数，等待
                    evenCondition.await();
                } else {
                    System.out.println(Thread.currentThread().getName() + " prints: " + number++);
                    oddCondition.signal(); // 唤醒奇数线程
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

}
