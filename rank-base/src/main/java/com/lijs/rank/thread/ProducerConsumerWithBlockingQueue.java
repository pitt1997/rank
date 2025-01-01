package com.lijs.rank.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author ljs
 * @date 2024-12-25
 * @description
 */
public class ProducerConsumerWithBlockingQueue {

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

        Thread producerThread = new Thread(() -> {
            int value = 0;
            try {
                while (true) {
                    System.out.println("Produced: " + value);
                    queue.put(value++); // 队列满时阻塞
                    Thread.sleep(500); // 模拟生产延迟
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumerThread = new Thread(() -> {
            try {
                while (true) {
                    int value = queue.take(); // 队列空时阻塞
                    System.out.println("Consumed: " + value);
                    Thread.sleep(1000); // 模拟消费延迟
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producerThread.start();
        consumerThread.start();
    }

}
