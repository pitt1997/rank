package com.lijs.rank.pointers;

import java.util.Arrays;

/**
 * @author ljs
 * @date 2024-12-31
 * @description
 */
public class TwoPointer {

    public static String findLongestCommonSubstring(String s1, String s2) {
        int maxLen = 0; // 记录最长公共子串长度
        int endIndex = 0; // 记录最长公共子串结束位置

        // 双指针遍历
        for (int i = 0; i < s1.length(); i++) {
            for (int j = 0; j < s2.length(); j++) {
                int length = 0;
                // 比较从 s1[i] 和 s2[j] 开始的子串
                while (i + length < s1.length() && j + length < s2.length() &&
                        s1.charAt(i + length) == s2.charAt(j + length)) {
                    length++;
                }

                // 更新最长公共子串
                if (length > maxLen) {
                    maxLen = length;
                    endIndex = i + maxLen;
                }
            }
        }

        // 返回最长公共子串
        return s1.substring(endIndex - maxLen, endIndex);
    }

    // 示例 1：
    //输入：nums = [-1,2,1,-4], target = 1
    //输出：2
    //解释：与 target 最接近的和是 2 (-1 + 2 + 1 = 2)。

    //示例 2：
    //输入：nums = [0,0,0], target = 1
    //输出：0
    //解释：与 target 最接近的和是 0（0 + 0 + 0 = 0）。
    public static int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int n = nums.length;
        int ans = nums[0] + nums[1] + nums[2];
        for (int i=0;i<n;i++) {
            int start = i+1;
            int end = n-1;
            while (start < end) {
                int sum = nums[end] + nums[start] + nums[i];
                if (Math.abs(sum-target)<Math.abs(ans - target)) {
                    ans = sum;
                }
                if (sum==target) {
                    return target;
                }
                if (sum>target) {
                    end--;
                }
                if (sum<target) {
                    start++;
                }
            }
        }
        return ans;
    }

    private static void mergeTwoArr(int[] nums1, int m, int[] nums2, int n) {
        if(nums2 == null || n == 0) {
            return;
        }
        int[] ans = new int[m];
        int i = 0;
        int j = 0;
        int c = 0;
        for(;i < m - n && j < n; c++) {
            int cur = nums1[i];
            if(nums1[i] > nums2[j]) {
                // j小则j走一步
                cur = nums2[j];
                j++;
            } else {
                // i小则i走一步
                cur = nums1[i];
                i++;
            }
            ans[c] = cur;
        }

        if(i < m - n) {
            // i没有走完
            for(;i < m - n; i++) {
                ans[c] = nums1[i];
                c++;
            }
        }

        if(j < n) {
            // j没有走完
            for(;j < n; j++) {
                ans[c] = nums2[j];
                c++;
            }
        }

        for(i = 0; i < m; i++) {
            nums1[i] = ans[i];
        }
    }

}
