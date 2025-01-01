package com.lijs.rank.dp;

/**
 * @author ljs
 * @date 2024-12-18
 * @description
 */
public class PackDp01 {

    public static void main(String[] args) {
        int[] value = {1, 5, 3, 8, 1};
        int[] weight = {2, 1, 5, 2, 3};

//        Scanner scanner = new Scanner(System.in);
//        for (int i = 0; i < 5; i++) {
//            int a = scanner.nextInt();
//            int b = scanner.nextInt();
//            value[i] = a;
//            weight[i] = b;
//        }
//
//        System.out.println(value[0]);
//        System.out.println(weight[0]);


        System.out.println(maxV(value, weight, 8));
    }

    private static int maxV(int[] value, int[] weight, int capacity) {
        int count = value.length;
        // [i][j] 前i个并且能够装重量j获取到的最大值 dp[i][j]
        int[][] dp = new int[count + 1][capacity + 1];
        for (int j = 1; j <= capacity; j++) {
            if (j >= weight[0]) {
                dp[1][j] = value[0];
            } else {
                dp[1][j] = 0;
            }
        }
        // 填充
        for (int i = 2; i <= count; i++) {
            // i是当前遍历第几个物品
            int v = value[i - 1];
            int w = weight[i - 1];
            for (int j = 1; j <= capacity; j++) {
                if (w <= j) {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - w] + v);
                } else {
                    // w > 当前容量 - 装不下了
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        return dp[count][capacity];
    }


}
