package com.lijs.rank.lru;

import java.util.HashMap;
import java.util.Map;

/**
 * @author author
 * @date 2024-11-20
 * @description 实现LRU算法结构
 */
public class LRUCache {

    /**
     * 链表结构
     */
    static class Node {
        public int key;
        public int value;
        public Node prev;
        public Node next;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private Map<Integer, Node> cacheMap = null;
    private final Node head;
    private final Node tail;


    public LRUCache(int capacity) {
        this.capacity = capacity;
        cacheMap = new HashMap<>(capacity);
        this.head = new Node(-1, -1); // 虚拟头节点
        this.tail = new Node(-1, -1); // 虚拟尾节点
        head.next = tail;
    }

    public int get(int key) {
        if (!cacheMap.containsKey(key)) {
            return -1;
        }
        Node target = cacheMap.get(key);
        moveToHead(target);
        return target.value;
    }

    public void put(int key, int value) {
        if (cacheMap.containsKey(key)) {
            // 更新节点
            Node node = cacheMap.get(key);
            node.value = value;
            moveToHead(node);
        } else {
            if (cacheMap.size() >= capacity) {
                // 移除尾节点
                Node toRemove = removeTail();
                cacheMap.remove(toRemove.key);
            }
            // 添加新节点
            Node newNode = new Node(key, value);
            cacheMap.put(key, newNode);
            addToHead(newNode);
        }
    }

    private void moveToHead(Node node) {
        removeNode(node);
        addToHead(node);
    }

    private void addToHead(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private Node removeTail() {
        Node toRemove = tail.prev;
        removeNode(toRemove);
        return toRemove;
    }

    // get(int key)

    // set(int key, int value)

    public static void main(String[] args) {


    }

}