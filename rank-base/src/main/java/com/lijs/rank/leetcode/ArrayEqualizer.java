package com.lijs.rank.leetcode;

/**
 *
 * 我们换个思路，用 并查 + DFS 或更简洁的方式模拟分组翻转的过程。
 *
 * 但其实这道题可以用如下正确贪心策略：
 *
 * 正确贪心解法：
 * 我们可以从左到右遍历，只要发现 nums[i] != target，就对 nums[i] 和 nums[i+1] 做一次操作，使得当前的 nums[i] 变为目标值。
 *
 * 目标值我们可以任选一个方向尝试，比如我们尝试将所有元素变成 nums[0]。
 *
 * @author author
 * @date 2025-06-08
 * @description
 */
public class ArrayEqualizer {

    // 贪心算法
    public static boolean canMakeEqual(int[] nums, int k) {
        return canConvert(nums.clone(), 1, k) || canConvert(nums.clone(), -1, k);
    }

    private static boolean canConvert(int[] nums, int target, int k) {
        int n = nums.length;
        int ops = 0;
        for (int i = 0; i < n - 1; i++) {
            if (nums[i] != target) {
                // 操作：翻转 i 和 i+1
                nums[i] *= -1;
                nums[i + 1] *= -1;
                ops++;
            }
        }
        return nums[n - 1] == target && ops <= k;
    }

    // 测试用例
    public static void main(String[] args) {
        int[] nums1 = {1, -1, 1, -1, 1};
        int k1 = 3;
        System.out.println("示例 1 结果: " + canMakeEqual(nums1, k1));  // true

        int[] nums2 = {-1, -1, -1, 1, 1, 1};
        int k2 = 5;
        System.out.println("示例 2 结果: " + canMakeEqual(nums2, k2));  // false

        int[] nums3 = {1, 1, 1, 1};
        int k3 = 0;
        System.out.println("示例 3 结果: " + canMakeEqual(nums3, k3));  // true

        int[] nums4 = {1, -1};
        int k4 = 1;
        System.out.println("示例 4 结果: " + canMakeEqual(nums4, k4));  // false

        int[] nums5 = {1, -1};
        int k5 = 0;
        System.out.println("示例 5 结果: " + canMakeEqual(nums5, k5));  // false

        int[] nums6 = {-1, -1, 1, -1, -1, -1, 1, -1, 1};
        int k6 = 9;
        System.out.println("示例 5 结果: " + canMakeEqual(nums6, k6));
    }

}