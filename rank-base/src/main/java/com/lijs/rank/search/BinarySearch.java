package com.lijs.rank.search;

/**
 * @author author
 * @date 2024-11-20
 * @description
 */
public class BinarySearch {

    public static int binarySearch(int[] nums, int target) {
        int left = 0;              // 左边界
        int right = nums.length - 1; // 右边界

        while (left <= right) {    // 当左右边界没有交错时继续
            int mid = left + (right - left) / 2; // 防止溢出的中间索引

            if (nums[mid] == target) { // 如果中间值等于目标值
                return mid;           // 返回索引
            } else if (nums[mid] < target) {
                left = mid + 1;       // 目标值在右侧
            } else {
                right = mid - 1;      // 目标值在左侧
            }
        }

        return -1; // 如果找不到目标值，返回 -1
    }

    public static void main(String[] args) {
        int[] nums = {1, 3, 5, 7, 9, 11};
        int target = 7;
        int result = binarySearch(nums, target);

        System.out.println("目标值的索引是: " + result);
    }


}