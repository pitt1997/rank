package com.lijs.rank.thread;

import cn.hutool.core.util.RandomUtil;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author ljs
 * @date 2024-12-25
 * @description
 */
public class ProducerConsumer {

    public static void main(String[] args) {
        ProducerConsumer example = new ProducerConsumer();

        Thread producerThread = new Thread(() -> {
            example.produce();
        });

        Thread consumerThread = new Thread(() -> {
            example.consume();
        });

        producerThread.start();
        consumerThread.start();
    }

    private Queue<Integer> queue = new LinkedList<>();

    private final Integer size = 5;

    private final Object lock = new Object();

    public void produce() {
        while (true) {
            // 一直尝试生产
            synchronized (lock) { // 针对queue操作时需要锁住
                if (queue.size() < size) {
                    int randomInt = RandomUtil.randomInt(0, 10);
                    queue.add(randomInt);
                    System.out.println("Produced: " + randomInt);
                    lock.notify();
                    try {
                        Thread.sleep(500); // 模拟生产延迟
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void consume() {
        while (true) {
            // 一直尝试消费
            synchronized (lock) { // 针对queue操作时需要锁住
                if (queue.size() > 0) {
                    Integer value = queue.poll();
                    System.out.println("Consumed: " + value);
                    lock.notify();
                    try {
                        Thread.sleep(500); // 模拟生产延迟
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
