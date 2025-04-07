package com.lijs.rank.order;

import java.util.*;

/**
 * @author ljs
 * @date 2025-04-01
 * @description
 */
public class Main {

    public static void main(String[] args) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(10, Collections.reverseOrder());
        PriorityQueue<Integer> minHeapA = new PriorityQueue<>(10, Collections.reverseOrder());
        PriorityQueue<Integer> minHeapB = new PriorityQueue<>(10, Comparator.naturalOrder());
        PriorityQueue<Integer> minHeapC = new PriorityQueue<>(10, Comparator.reverseOrder());
        List<Integer> listA = new ArrayList<>();
        List<String> listB = new ArrayList<>();
        listA.sort(Collections.reverseOrder());
        Collections.sort(listA, Comparator.naturalOrder());
        Collections.sort(listA, Comparator.reverseOrder());
    }

}
