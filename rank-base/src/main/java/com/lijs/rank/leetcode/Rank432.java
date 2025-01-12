package com.lijs.rank.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author author
 * @date 2025-01-12
 * @description
 */
public class Rank432 {


    public static void main(String[] args) {
        int[][] grid = new int[2][2];
        grid[0][0] = 1;
        grid[0][1] = 2;
        grid[1][0] = 3;
        grid[1][1] = 4;

        int[][] grid1 = new int[3][3];
        grid1[0][0] = 1;
        grid1[0][1] = 2;
        grid1[0][2] = 3;

        grid1[1][0] = 4;
        grid1[1][1] = 5;
        grid1[1][2] = 6;

        grid1[2][0] = 7;
        grid1[2][1] = 8;
        grid1[2][2] = 9;

        //1,2,3],[4,5,6],[7,8,9]©leetcode
        //[[2,1],[2,1],[2,1]]
        System.out.println(zigzagTraversal(grid1));


        int[][] coins = new int[3][3];
        coins[0][0] = 0;
        coins[0][1] = 1;
        coins[0][2] = -1;

        coins[1][0] = 1;
        coins[1][1] = -2;
        coins[1][2] = 3;

        coins[2][0] = 2;
        coins[2][1] = -3;
        coins[2][2] = 4;

        // [10,10,10],[10,10,10]©leetcode

        int[][] coins2 = new int[2][3];
        coins2[0][0] = 10;
        coins2[0][1] = 10;
        coins2[0][2] = 10;

        coins2[1][0] = 10;
        coins2[1][1] = 10;
        coins2[1][2] = 10;

        int[][] coins3 = {
                {-16, 8, -7, -19},
                {6, 3, -10, 13},
                {13, 15, 4, -3},
                {-16, 4, 19, -12}
        };

        int[][] coins4 = {
                {-7,12,12,13},
                {-6,19,19,-6},
                {9,-2,-10,16},
                {-4,14,-10,-9}
        };



        System.out.println(maxCoins(coins4, 2));


        System.out.println(1);
    }

    public static List<Integer> zigzagTraversal(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        // 1 3 5 奇数行递增找
        // 2 4 6 偶数行递减找
        List<Integer> res = new ArrayList<>();
        int nextJ = 0;
        for (int i = 0; i < m; i++) {
            if (i % 2 == 0) {
                if (nextJ >= n) {
                    nextJ = nextJ - n;
                }
                // 递增
                for (int j = 0; j < n; j++) {
                    if (j != nextJ) {
                        continue;
                    }
                    res.add(grid[i][j]);
                    nextJ = j + 2;
                }
            } else {
                if (nextJ >= n) {
                    nextJ = nextJ - n;
                }
                for (int j = 0; j < n; j++) {
                    if (j != nextJ) {
                        continue;
                    }
                    res.add(grid[i][n - j - 1]);
                    nextJ = j + 2;
                }
            }
        }
        return res;
    }

    // max(dp[m−1][n−1][0],dp[m−1][n−1][1],dp[m−1][n−1][2])


    public static int maxCoins(int[][] coins, int maxCharm) {
        int m = coins.length;
        int n = coins[0].length;
        int kLimit = 2; // 最多感化 2 个强盗

        // dp[i][j][k] 表示到达 (i, j) 时感化了 k 个强盗后的最大金币数
        int[][][] dp = new int[m][n][kLimit + 1];

        // 初始化 dp 数组，所有值为负无穷表示不可达
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k <= kLimit; k++) {
                    dp[i][j][k] = Integer.MIN_VALUE;
                }
            }
        }

        // 起点初始化
        dp[0][0][0] = Math.max(0, coins[0][0]); // 起点没有强盗，可以直接获得金币
        if (coins[0][0] < 0) {
            dp[0][0][1] = 0; // 使用一次感化能力，金币不变
        }

        // 动态规划
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k <= kLimit; k++) {
                    if (dp[i][j][k] == Integer.MIN_VALUE) continue;

                    // 向右移动
                    if (j + 1 < n) {
                        int nextValue = coins[i][j + 1];
                        // 不感化强盗
                        dp[i][j + 1][k] = Math.max(dp[i][j + 1][k], dp[i][j][k] + Math.max(0, nextValue));
                        // 感化强盗
                        if (nextValue < 0 && k + 1 <= kLimit) {
                            dp[i][j + 1][k + 1] = Math.max(dp[i][j + 1][k + 1], dp[i][j][k]);
                        }
                    }

                    // 向下移动
                    if (i + 1 < m) {
                        int nextValue = coins[i + 1][j];
                        // 不感化强盗
                        dp[i + 1][j][k] = Math.max(dp[i + 1][j][k], dp[i][j][k] + Math.max(0, nextValue));
                        // 感化强盗
                        if (nextValue < 0 && k + 1 <= kLimit) {
                            dp[i + 1][j][k + 1] = Math.max(dp[i + 1][j][k + 1], dp[i][j][k]);
                        }
                    }
                }
            }
        }

        // 取最后位置 (m-1, n-1) 所有可能的感化强盗数的最大值
        int maxCoins = Integer.MIN_VALUE;
        for (int k = 0; k <= kLimit; k++) {
            maxCoins = Math.max(maxCoins, dp[m - 1][n - 1][k]);
        }
        return maxCoins;
    }


    public static int maxCoins(int[][] coins) {
        int maxCharm = 2;
        int m = coins.length, n = coins[0].length;
        int[][][] dp = new int[m][n][maxCharm + 1];

        // 初始化 DP 数组
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k <= maxCharm; k++) {
                    dp[i][j][k] = Integer.MIN_VALUE;
                }
            }
        }

        // 起点初始化
        dp[0][0][0] = coins[0][0] >= 0 ? coins[0][0] : 0; // 不感化强盗
        if (coins[0][0] < 0 && maxCharm > 0) {
            dp[0][0][1] = 0; // 感化起点强盗
        }

        // 状态转移
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k <= maxCharm; k++) {
                    if (dp[i][j][k] == Integer.MIN_VALUE) continue; // 无效状态跳过

                    // 右移
                    if (j + 1 < n) {
                        int nextValue = coins[i][j + 1];
                        // 不感化
                        dp[i][j + 1][k] = Math.max(dp[i][j + 1][k],
                                dp[i][j][k] + (nextValue >= 0 ? nextValue : 0));
                        // 感化
                        if (nextValue < 0 && k + 1 <= maxCharm) {
                            dp[i][j + 1][k + 1] = Math.max(dp[i][j + 1][k + 1], dp[i][j][k]);
                        }
                    }

                    // 下移
                    if (i + 1 < m) {
                        int nextValue = coins[i + 1][j];
                        // 不感化
                        dp[i + 1][j][k] = Math.max(dp[i + 1][j][k],
                                dp[i][j][k] + (nextValue >= 0 ? nextValue : 0));
                        // 感化
                        if (nextValue < 0 && k + 1 <= maxCharm) {
                            dp[i + 1][j][k + 1] = Math.max(dp[i + 1][j][k + 1], dp[i][j][k]);
                        }
                    }
                }
            }
        }

        // 汇总结果
        int maxCoins = Integer.MIN_VALUE;
        for (int k = 0; k <= maxCharm; k++) {
            maxCoins = Math.max(maxCoins, dp[m - 1][n - 1][k]);
        }
        return maxCoins;
    }



    public static int maximumAmount(int[][] coins) {
        int m = coins.length;
        int n = coins[0].length;
        int kLimit = 2; // 最多感化 2 个强盗

        if (m == 1 && n == 1) {
            return Math.max(coins[0][0], 0);
        }

        // dp[i][j][k] 表示到达 (i, j) 时感化了 k 个强盗后的最大金币数
        int[][][] dp = new int[m][n][kLimit + 1];

        // 初始化 dp 数组
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k <= kLimit; k++) {
                    dp[i][j][k] = Integer.MIN_VALUE;
                }
            }
        }

        // 起点初始化
        dp[0][0][0] = Math.max(0, coins[0][0]);
        if (coins[0][0] < 0) {
            dp[0][0][1] = 0; // 使用一次感化能力，金币不变
        }

        // 动态规划
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k <= kLimit; k++) {
                    if (dp[i][j][k] == Integer.MIN_VALUE) continue;

                    // 向右移动
                    if (j + 1 < n) {
                        int nextValue = coins[i][j + 1];
                        // 不感化强盗
                        dp[i][j + 1][k] = Math.max(dp[i][j + 1][k],
                                dp[i][j][k] + Math.max(0, nextValue));
                        // 感化强盗
                        if (nextValue < 0 && k + 1 <= kLimit) {
                            dp[i][j + 1][k + 1] = Math.max(dp[i][j + 1][k + 1], dp[i][j][k]);
                        }
                    }

                    // 向下移动
                    if (i + 1 < m) {
                        int nextValue = coins[i + 1][j];
                        // 不感化强盗
                        dp[i + 1][j][k] = Math.max(dp[i + 1][j][k],
                                dp[i][j][k] + Math.max(0, nextValue));
                        // 感化强盗
                        if (nextValue < 0 && k + 1 <= kLimit) {
                            dp[i + 1][j][k + 1] = Math.max(dp[i + 1][j][k + 1], dp[i][j][k]);
                        }
                    }
                }
            }
        }

        int maxCoins = Integer.MIN_VALUE;
        for (int k = 0; k <= kLimit; k++) {
            maxCoins = Math.max(maxCoins, dp[m - 1][n - 1][k]);
        }
        return maxCoins;
    }


}