package com.lijs.rank.comparator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author author
 * @date 2025-06-12
 * @description
 */
public class ArraysSort {

    public static void main(String[] args) {

        int[][] arr = {
                {1, 9, 5},
                {3, 2, 8},
                {4, 6, 7}
        };

        // 按第2列（下标1）升序排序
        Arrays.sort(arr, Comparator.comparingInt(a -> a[1]));

        // 输出结果
        for (int[] row : arr) {
            System.out.println(Arrays.toString(row));
        }
        // 输出：
        // [3, 2, 8]
        // [4, 6, 7]
        // [1, 9, 5]

        // 多级排序（先按第1列，再按第2列）
        Arrays.sort(arr, Comparator
                .comparingInt((int[] a) -> a[0]) // 第一优先级：第1列
                .thenComparingInt(a -> a[1])      // 第二优先级：第2列
        );

        // 按第3列（下标2）降序
        Arrays.sort(arr, (a, b) -> Integer.compare(b[2], a[2]));

        // 或使用Comparator.reversed()
        // Arrays.sort(arr, Comparator.comparingInt(a -> a[2]).reversed());

        Integer[] arrs = {5, 2, 9, 1, 5};
        arrs[1] = 1;
        arrs[8] = 10;
        Arrays.sort(arrs, Collections.reverseOrder());
    }

}