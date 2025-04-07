package com.lijs.rank.merge_intervals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author ljs
 * @date 2025-04-02
 * @description
 */

public class IntervalIntersection {
    public static List<int[]> mergeCommonIntervals(int[][] intervals) {
        List<int[]> commonIntervals = new ArrayList<>();

        // 计算所有公共区间
        int n = intervals.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int start = Math.max(intervals[i][0], intervals[j][0]);
                int end = Math.min(intervals[i][1], intervals[j][1]);
                if (start <= end) {
                    commonIntervals.add(new int[]{start, end});
                }
            }
        }

        // 按起始值排序
        commonIntervals.sort(Comparator.comparingInt(a -> a[0]));

        // 合并区间
        List<int[]> result = new ArrayList<>();
        for (int[] interval : commonIntervals) {
            if (result.isEmpty() || result.get(result.size() - 1)[1] < interval[0]) {
                result.add(interval);
            } else {
                result.get(result.size() - 1)[1] = Math.max(result.get(result.size() - 1)[1], interval[1]);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int[][] intervals = {{0, 3}, {1, 3}, {3, 5}, {3, 6}};
        List<int[]> result = mergeCommonIntervals(intervals);

        // 输出结果
        System.out.print("[");
        for (int i = 0; i < result.size(); i++) {
            System.out.print(Arrays.toString(result.get(i)));
            if (i < result.size() - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
}
