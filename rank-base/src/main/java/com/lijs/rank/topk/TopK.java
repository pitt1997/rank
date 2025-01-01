package com.lijs.rank.topk;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * @author ljs
 * @date 2024-12-25
 * @description
 */
public class TopK {

    public static void main(String[] args) {
        System.out.println("topK:");
        // topK
        int[] arrK = {3, 2, 5, 4, 1};
        topK(arrK, 3);
        int[] arrKDeap = {3, 2, 5, 4, 1};
        topKDeap(arrKDeap, 3);
    }

    private static void topKDeap(int[] arrK, int k) {
        // 维护一个大小为K的堆。
        // 遍历数组中的元素，将其逐个插入堆中。
        // 对于最小堆：如果当前元素大于堆顶，则替换堆顶并调整堆。
        // 对于最大堆：如果当前元素小于堆顶，则替换堆顶并调整堆。
        // 遍历结束后，堆中即为Top-K元素。
        // 时间复杂度：O(n log K)，适合大规模数据
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k); // 小顶堆
        // PriorityQueue<Integer> minHeap = new PriorityQueue<>(k, (a, b) -> b - a); // 大顶堆
        for (int i = 0; i < arrK.length; i++) {
            if (minHeap.size() < k) {
                minHeap.add(arrK[i]);
            } else if (!minHeap.isEmpty() && arrK[i] > minHeap.peek()) {
                minHeap.poll(); // 移除堆顶最小元素
                minHeap.add(arrK[i]);
            }
        }

        System.out.println(minHeap);
    }

    // topK问题
    private static void topK(int[] arr, int k) {
        int[] maxArr = new int[k];
        // 3, 2, 5, 4, 1
        // 1、选择排序？
        for (int i = 0; i < arr.length; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    minIndex = j;
                }
            }
            swap(arr, i, minIndex);
        }
        System.out.println(Arrays.toString(arr));
        for (int i = arr.length; i >= k; i--) {
            maxArr[arr.length - i] = arr[i - 1];
        }
        System.out.println(Arrays.toString(maxArr));
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

}
