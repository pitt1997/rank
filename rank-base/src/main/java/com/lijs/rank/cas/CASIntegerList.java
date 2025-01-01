package com.lijs.rank.cas;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author author
 * @date 2024-12-26
 * @description
 */
public class CASIntegerList {

    private final AtomicReference<Node> head;

    public CASIntegerList() {
        this.head = new AtomicReference<>(null);
    }

    // 链表节点
    private static class Node {
        final Integer value;
        final AtomicReference<Node> next;

        public Node(Integer value) {
            this.value = value;
            this.next = new AtomicReference<>(null);
        }
    }

    // 无锁插入操作
    public void add(Integer value) {
        Node newNode = new Node(value);
        while (true) {
            Node currentHead = head.get();  // 获取当前头节点
            newNode.next.set(currentHead);      // 设置新节点的下一个节点为当前头节点
            if (head.compareAndSet(currentHead, newNode)) {  // CAS 更新头节点
                break;  // 成功插入
            }
        }
    }

    // 无锁读取操作
    public Integer poll() {
        while (true) {
            Node currentHead = head.get();  // 获取当前头节点
            if (currentHead == null) {
                return null;  // 空列表，返回 null
            }
            Node nextNode = currentHead.next.get();  // 获取下一个节点
            if (head.compareAndSet(currentHead, nextNode)) {  // CAS 更新头节点
                return currentHead.value;  // 成功读取并移除头节点
            }
        }
    }
}