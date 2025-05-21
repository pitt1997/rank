package com.lijs.rank.hw_od;

import java.util.Scanner;

/**
 * @author author
 * @date 2025-04-08
 * @description
 */
public class Main3 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] str = in.nextLine().split(" ");
        int N = Integer.parseInt(str[0]);
        int L = Integer.parseInt(str[1]);
        double[] arr = new double[N];
        for (int i = 0; i < N; i++) {
            String s = in.nextLine();
            arr[i] = Double.parseDouble(s);
        }

        // N:3 L:2
        // arr: 2 2 3
        // res: 1 2
        // 子数组 {2,3}的几何平均值最大（k个数的乘积的k次方根）
        // 找到长度为L的最大子数组

        double max = 0;
        int index = 0; // 几何平均值最大的子数组的位置
        int count = 0; // 几何平均值最大的子数组的大小
        int left = 0;
        int right = 0;

        // L 表示子数组的最小长度，那么  L 子数组的最小长度是L，长度可以大于L，并且小于最大长度
        int window = L;
        // 滑动窗口
        while (right < N) {
            if (right - left + 1 < window) {
                right++;
            } else if (right - left + 1 == window) {
                double sum = getKRoot(arr, left, right, window);
                if (sum > max) {
                    max = sum;
                    index = left;
                    count = window;
                }
                // 这里等于窗口最小的开始尝试，直到最大长度
                for (int i = right; i < N; i++) {
                    sum = getKRoot(arr, left, i, window);
                    if (sum > max) {
                        max = sum;
                        index = left;
                        count = window;
                    }
                }
                left++;
                right++;
            }
        }

        System.out.print(index + " " + count);
    }

    private static double getKRoot(double[] arr, int left, int right, int w) {
        double sum = 1;
        for (int i = left; i <= right; i++) {
            sum *= arr[i];
        }
        return Math.pow(sum, (double) 1 / w);
    }
}