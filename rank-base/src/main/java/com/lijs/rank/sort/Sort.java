package com.lijs.rank.sort;

import java.util.Arrays;

/**
 * @author ljs
 * @date 2024-12-24
 * @description
 */
public class Sort {

    public static void main(String[] args) {
        System.out.println("冒泡排序:");
        // 冒泡 时间:O(n2)、空间:O(1)、稳定
        int[] arrB = {3, 2, 5, 4, 1};
        sortBubble(arrB);

        System.out.println("选择排序:");
        // 选择 时间复杂度：O(n²)、空间复杂度：O(1)、是否稳定：否
        int[] arrS = {3, 2, 5, 4, 1};
        sortSelect(arrS);

        System.out.println("快速排序:");
        // 快速 O(n log n) O(log n) 不稳定
        int[] arrQ = {3, 2, 5, 4, 1};
        sortQuick(arrQ, 0, 4);
        System.out.println(Arrays.toString(arrQ));

        int[] arr = {4, 2, 2, 8, 3, 3, 1};
        System.out.println("原始数组: " + Arrays.toString(arr));
        countingSort(arr);
        System.out.println("排序后数组: " + Arrays.toString(arr));
    }

    // 算法步骤 基准+双指针
    // 选择一个基准元素，通常选择数组的最后一个元素。
    // 遍历数组，将比基准小的元素放在左边，比基准大的元素放在右边。
    // 递归地对左子数组和右子数组进行快速排序。
    // 2 1 3 5 4  基准=4
    //       j
    public static void sortQuick(int[] arr, int left, int right) {
        // 递归结束标识
        if (left < right) {
            // 找到基准位置
            int pIndex = partition(arr, left, right);
            sortQuick(arr, left, pIndex - 1);
            sortQuick(arr, pIndex + 1, right);
        }
    }

    private static int partition(int[] arr, int left, int right) {
        // 双指针 ij
        int pV = arr[right];
        int i = left; // i之前是基准左边
        for (int j = left; j < right; j++) {
            if (arr[j] < pV) {
                // 交换ij位置上的元素
                swap(arr, i, j);
                i++; // 坐标右移一位
            }
        }
        // 最后交互i、p的位置的元素
        swap(arr, i, right);
        return i;
    }

    // 冒泡
    // 冒泡排序的基本思想是通过重复地比较相邻的元素，并根据需要交换它们来将最大的元素“冒泡”到数组的末尾。
    // 每一轮比较后，最大的元素会移动到当前未排序部分的最后位置。
    private static void sortBubble(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        // 3,2,5,4,1 从0位置开始、每次比较交换两个数，直到循环完成 n*(n-1)
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, i, j);
                }
            }
        }
        // 注意这个转换打印
        System.out.println(Arrays.toString(arr));
    }

    // 选择
    // 选择排序的基本思想是每次从未排序部分中找到最小（或最大）的元素，并将其放到未排序部分的起始位置。
    private static void sortSelect(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        // 3, 2, 5, 4, 1
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            int minIndex = i;
            // 去找到这个min
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[i]) {
                    minIndex = j;
                }
            }
            // swap
            swap(arr, i, minIndex);
        }
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 计数排序适用于非负整数或可以映射到非负整数的元素（例如字符的ASCII值）。其实现过程主要分为以下几步：
     *
     * 统计频率：
     *
     * 创建一个计数数组（大小为数组元素的值范围大小），记录输入数组中每个元素出现的次数。
     * 累加频率：
     *
     * 对计数数组进行前缀和计算，这样计数数组中的每个值表示小于或等于该值的元素数量。
     * 填充结果：
     *
     * 遍历输入数组，根据计数数组中的值，将每个元素放到结果数组的正确位置，然后更新计数数组以减少对应值的计数。
     */
    public static void countingSort(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        // 找到数组中的最大值和最小值
        int max = 0;
        int min = 0;
        for (int num : arr) {
            max = Math.max(max, num);
            min = Math.min(min, num);
        }
        int range = max - min + 1;
        // 创建计数数组并统计频率
        int[] count = new int[range];
        for (int num : arr) {
            // 对应值位置设置出现次数
            count[num - min]++;
        }
        // [1,8]
        // arr = [4, 2, 2, 8, 3, 3, 1]
        // count[0] = 1 表示数组中小于或等于 1 的元素有 1 个。
        // count[1] = 1 表示数组中小于或等于 2 的元素有 1 个。
        // count[2] = 3 表示数组中小于或等于 2 的元素有 3 个。
        // count[3] = 5 表示数组中小于或等于 3 的元素有 5 个。
        // count = [1, 0, 2, 2, 1, 0, 0, 0, 1]
        // count = [1, 1, 3, 5, 6, 6, 6, 6, 7]

        // 累加频率
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        // 创建结果数组并填充
        int[] result = new int[arr.length];
        for (int i = arr.length - 1; i >= 0; i--) { // 倒序遍历以保证稳定性
            result[--count[arr[i] - min]] = arr[i];
        }

        // 将结果拷贝回原数组
        System.arraycopy(result, 0, arr, 0, arr.length);
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

}
