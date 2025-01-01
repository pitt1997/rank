package com.lijs.rank.lru;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ljs
 * @date 2024-12-31
 * @description
 */
public class LRU {

    public static void main(String[] args) {
        testLRU();
    }

    private static void testLRU() {
        LRUCache lruCache = new LRUCache(2);
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        System.out.println("LRU get key 1 : " + lruCache.get(1));
        lruCache.put(3, 3);
        System.out.println("LRU get key 2 : " + lruCache.get(2));
    }

    static class LRUCache extends LinkedHashMap<Integer, Integer> {
        private int capacity;

        public LRUCache(int capacity) {
            super(capacity, 0.75F, true);
            this.capacity = capacity;
        }

        public int get(int key) {
            return super.getOrDefault(key, -1);
        }

        public void put(int key, int value) {
            super.put(key, value);
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
            return size() > capacity;
        }
    }

}
