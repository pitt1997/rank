package com.lijs.rank.thread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ljs
 * @date 2024-12-25
 * @description
 */
public class ProducerConsumerWithLock {

    private final Queue<Integer> queue = new LinkedList<>();
    private final int MAX_SIZE = 5;
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public void produce() throws InterruptedException {
        int value = 0;
        while (true) {
            lock.lock();
            try {
                while (queue.size() == MAX_SIZE) {
                    notFull.await(); // 队列满，等待消费者消费
                }
                System.out.println("Produced: " + value);
                queue.add(value++);
                notEmpty.signal(); // 通知消费者
            } finally {
                lock.unlock();
            }
            Thread.sleep(500); // 模拟生产延迟
        }
    }

    public void consume() throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (queue.isEmpty()) {
                    notEmpty.await(); // 队列空，等待生产者生产
                }
                int value = queue.poll();
                System.out.println("Consumed: " + value);
                notFull.signal(); // 通知生产者
            } finally {
                lock.unlock();
            }
            Thread.sleep(1000); // 模拟消费延迟
        }
    }

    public static void main(String[] args) {
        ProducerConsumerWithLock example = new ProducerConsumerWithLock();

        Thread producerThread = new Thread(() -> {
            try {
                example.produce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumerThread = new Thread(() -> {
            try {
                example.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producerThread.start();
        consumerThread.start();
    }

}
