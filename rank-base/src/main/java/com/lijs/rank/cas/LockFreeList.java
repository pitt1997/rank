package com.lijs.rank.cas;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author author
 * @date 2024-12-26
 * @description CAS实现队列
 */
public class LockFreeList<E> {

    private final AtomicReference<Node<E>> head;

    public LockFreeList() {
        this.head = new AtomicReference<>(null);  // 初始为空链表
    }

    // 链表节点
    private static class Node<E> {
        final E value;
        final AtomicReference<Node<E>> next;

        public Node(E value) {
            this.value = value;
            this.next = new AtomicReference<>(null);
        }
    }

    // 无锁插入操作
    public void add(E value) {
        Node<E> newNode = new Node<>(value);
        while (true) {
            Node<E> currentHead = head.get();  // 获取当前头节点
            newNode.next.set(currentHead);      // 设置新节点的下一个节点为当前头节点
            if (head.compareAndSet(currentHead, newNode)) {  // CAS 更新头节点
                break;  // 成功插入
            }
        }
    }

    // 无锁读取操作
    public E poll() {
        while (true) {
            Node<E> currentHead = head.get();  // 获取当前头节点
            if (currentHead == null) {
                return null;  // 空列表，返回 null
            }
            Node<E> nextNode = currentHead.next.get();  // 获取下一个节点
            if (head.compareAndSet(currentHead, nextNode)) {  // CAS 更新头节点
                return currentHead.value;  // 成功读取并移除头节点
            }
        }
    }
}