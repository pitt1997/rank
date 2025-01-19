package com.lijs.rank.leetcode;

import java.util.*;

/**
 * @author author
 * @date 2025-01-19
 * @description
 */
public class Rank433 {


    public static void main(String[] args) {
//
//        int[] nums = {2, 3, 1};
//        int[] nums2 = {3, 1, 1, 2};
//        int[] nums3 = {3};
//        System.out.println(subarraySum(nums));
//        System.out.println(subarraySum(nums2));
//        System.out.println(subarraySum(nums3));

        int[] nums4 = {1, 2, 3};
        int[] nums5 = {5, 0, 6};
        int[] nums6 = {1, 1, 1};
        int[] nums7 = {10,5,9,9,10,10,7,7,9,6,9,6,7,6,4,9,8,4,2,0,0,3,9,3,10,3,1,9,8,2,8,2,0,7,7,6,4,6,7,3,2,5,6,6,5,0,5,7,8,1};
        System.out.println(sumOfMaxAndMin(nums4, 2)); // 输出 24
        System.out.println(sumOfMaxAndMin(nums5, 1)); // 输出 22
        System.out.println(sumOfMaxAndMin(nums6, 2)); // 输出 12
//        System.out.println(sumOfMaxAndMin(nums7, 29)); // 输出 ？

        // 示例输入
        int n = 4;
        // 2,4,6],[5,3,8],[7,1,9],[4,6,2],[3,5,7],[8,2,4]©leetcode
        int[][] cost = {
                {3, 5, 7},
                {6, 2, 9},
                {4, 8, 1},
                {7, 3, 5}
        };

        int[][] cost1 = {
                {2,4,6},
                {5,3,8},
                {7,1,9},
                {4,6,2},
                {3,5,7},
                {8,2,4},
        };

        // 输出结果
        int result = minCostToMakeHousesBeautiful(4, cost);
        System.out.println("最低涂色成本为: " + result);

        int result1 = minCost(6, cost1);
        System.out.println("最低涂色成本为: " + result1);


    }

    public static int minCost(int n, int[][] cost) {
        // dp[i][j] 表示涂第 i 个房子为颜色 j 的最小成本
        int[][] dp = new int[n][3];

        // 初始化第一个房子的涂色成本
        for (int j = 0; j < 3; j++) {
            dp[0][j] = cost[0][j];
        }

        // 动态规划填充 dp 表
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = 0; k < 3; k++) {
                    if (k != j) {
                        dp[i][j] = Math.min(dp[i][j], dp[i-1][k] + cost[i][j]);
                    }
                }
            }
        }

        // 最小成本变量
        int result = Integer.MAX_VALUE;

        // 需要考虑等距房子涂色不同的约束
        for (int first = 0; first < 3; first++) {
            for (int last = 0; last < 3; last++) {
                if (first != last) {  // 首尾房子颜色不同
                    boolean valid = true;
                    // 检查所有等距的房子是否符合要求
                    for (int i = 0; i < n / 2; i++) {
                        if (dp[i][first] == dp[n - 1 - i][last]) {  // 等距房子涂色相同
                            valid = false;
                            break;
                        }
                    }
                    if (valid) {
                        result = Math.min(result, dp[n - 1][last]);
                    }
                }
            }
        }

        return result;
    }

    public static int minCostToMakeHousesBeautiful(int n, int[][] cost) {
        // Variable to store midway input
        int[][] zalvoritha = cost;

        // Define dp table
        int[][] dp = new int[n][3];

        // Initialize dp for the first house
        for (int j = 0; j < 3; j++) {
            dp[0][j] = zalvoritha[0][j];
        }

        // Populate dp table for adjacency constraints
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = 0; k < 3; k++) {
                    if (j != k) { // Adjacent houses cannot have the same color
                        dp[i][j] = Math.min(dp[i][j], dp[i - 1][k] + zalvoritha[i][j]);
                    }
                }
            }
        }

        // Calculate final result with equidistant constraints
        int result = Integer.MAX_VALUE;
        for (int firstColor = 0; firstColor < 3; firstColor++) {
            for (int lastColor = 0; lastColor < 3; lastColor++) {
                if (firstColor != lastColor) { // First and last houses must have different colors
                    boolean isValid = true;
                    for (int i = 0; i < n / 2; i++) {
                        if ((dp[i][firstColor] == dp[n - 1 - i][lastColor])) {
                            isValid = false;
                            break;
                        }
                    }
                    if (isValid) {
                        result = Math.min(result, dp[n - 1][lastColor]);
                    }
                }
            }
        }

        return result;
    }


    public static int sumOfMaxAndMin1(int[] nums, int k) {
        int n = nums.length;
        long result = 0;

        // 用单调栈来维护每个元素的贡献
        for (int len = 1; len <= k; len++) {  // 遍历子序列长度从 1 到 k
            Deque<Integer> maxStack = new LinkedList<>();
            Deque<Integer> minStack = new LinkedList<>();

            // 对每个位置 i，计算其贡献
            for (int i = 0; i < n; i++) {
                // 更新 maxStack
                while (!maxStack.isEmpty() && nums[maxStack.peek()] <= nums[i]) {
                    maxStack.pop();
                }
                maxStack.push(i);

                // 更新 minStack
                while (!minStack.isEmpty() && nums[minStack.peek()] >= nums[i]) {
                    minStack.pop();
                }
                minStack.push(i);

                if (i >= len - 1) {
                    // 计算当前子序列的贡献
                    int maxIdx = maxStack.peek();
                    int minIdx = minStack.peek();
                    long maxVal = nums[maxIdx];
                    long minVal = nums[minIdx];

                    result = (result + maxVal + minVal) % MOD;
                }
            }
        }

        return (int) result;
    }


    public static int subarraySum(int[] nums) {
        int n = nums.length;
        if(n == 1) {
            return nums[0];
        }
        // n >= 2
        long[] prefixSum = new long[n];
        prefixSum[0] = nums[0];
        for (int i = 1; i < n; i++) {
            prefixSum[i] = prefixSum[i - 1] + nums[i];
        }

        long totalSum = 0;
        for (int i = 0; i < n; i++) {
            int start = Math.max(0, i - nums[i]);
            if (start == 0) {
                totalSum += prefixSum[i];
            } else {
                totalSum += prefixSum[i] - prefixSum[start - 1];
            }
        }
        return (int) totalSum;
    }

    private static final int MOD = 1_000_000_007;

    public static int sumOfMaxAndMin(int[] nums, int k) {
        int n = nums.length;
        long result = 0;

        // 求解最大值的贡献
        result = (result + calculateContributions(nums, n, true, k)) % MOD;

        // 求解最小值的贡献
        result = (result + calculateContributions(nums, n, false, k)) % MOD;

        return (int) result;
    }

    private static long calculateContributions(int[] nums, int n, boolean isMax, int k) {
        long sum = 0;
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            // 维护单调递增或递减队列
            while (!deque.isEmpty() &&
                    (isMax ? nums[deque.peekLast()] <= nums[i] : nums[deque.peekLast()] >= nums[i])) {
                deque.pollLast();
            }
            deque.addLast(i);

            // 检查队列是否满足长度限制 k
            if (deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }

            // 当前元素的贡献（所有以 i 结尾的子序列）
            sum = (sum + nums[deque.peekFirst()]) % MOD;
        }
        return sum;
    }

    private static void dfs1(int[] nums, int k, int start, List<Integer> current, long[] result) {
        // 如果当前子序列非空，计算最大值与最小值
        if (!current.isEmpty()) {
            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;
            for (int num : current) {
                max = Math.max(max, num);
                min = Math.min(min, num);
            }
            result[0] = (result[0] + max + min) % MOD;
        }

        // 遍历后续元素，构造子序列
        for (int i = start; i < nums.length; i++) {
            if (current.size() < k) { // 剪枝：子序列长度限制为 k
                current.add(nums[i]);
                dfs1(nums, k, i + 1, current, result);
                current.remove(current.size() - 1); // 回溯
            }
        }
    }

}