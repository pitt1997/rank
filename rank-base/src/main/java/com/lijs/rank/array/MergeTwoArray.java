package com.lijs.rank.array;

/**
 * @author ljs
 * @date 2024-12-31
 * @description
 */
public class MergeTwoArray {

    public static void main(String[] args) {
        System.out.println("合并两个有序数组:");
        mergeTwoArrayTest();
    }

    private static void mergeTwoArrayTest() {
        int[] nums1 = {1, 2, 2, 0, 0, 0};
        int[] nums2 = {2, 3, 4};
        merge(nums1, 6, nums2, 3);
        //mergeTwoArr(nums1, 6, nums2, 3);

        System.out.println(nums1);
    }

    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        if (nums1 == null && nums2 == null) {
            return;
        }

        if (nums2 == null) {
            return;
        }
        int[] nums = new int[m + n];
        int index = 0;
        int i = 0;
        int j = 0;
        while (i < m && j < n) {
            // 小的元素放到nums中并且小的数组先走一步
            if (nums1[i] <= nums2[j]) {
                nums[index] = nums1[i];
                i++;
                index++;
            } else {
                nums[index] = nums2[j];
                j++;
                index++;
            }
        }
        // 是否没有走完
        if (i < m) {
            for (; i < m; i++) {
                nums[index] = nums1[i];
                index++;
            }
        }
        // 是否没有走完
        if (j < n) {
            for (; j < n; j++) {
                nums[index] = nums2[j];
                index++;
            }
        }


        for (i = 0; i < m; ++i) {
            nums1[i] = nums[i];
        }

    }


}
