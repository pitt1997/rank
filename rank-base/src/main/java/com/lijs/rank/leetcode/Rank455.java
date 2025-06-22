package com.lijs.rank.leetcode;

import java.util.*;

/**
 * @author author
 * @date 2025-06-22
 * @description
 */
public class Rank455 {

    public static void main(String[] args) {
        // String s = "abcdea";
        String s = "bcadbc";
        System.out.println(maxLengthSubString(s));
        List<String> list = new ArrayList<>();
        Set<String> set = new HashSet<>();
        LinkedList<String> linkedList = new LinkedList<>();
        //         1
        //              2
        //       5           3
        //              4
        int[] a = {1,2,3,4,5};
        System.out.println(checkPrimeFrequency(a));


        int[] numWays = {0, 1, 0, 2, 0, 3, 0, 4, 0, 5};
        int[] result = findCoinDenominations(numWays);
        System.out.println(Arrays.toString(result)); // 输出: [2, 4, 6]

    }

    public static boolean checkPrimeFrequency(int[] nums) {
        int n = nums.length;
        if (n <= 1) {
            return false;
        }

        // n >= 2
        Set<Integer> set = new HashSet<>();
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            if(set.contains(num)) {
                // 超过1个
                if (map.containsKey(num)) {
                    map.put(num, map.get(num) + 1);
                } else {
                    map.put(num, 2);
                }
            } else {
                // 第一次出现
                set.add(num);
            }
        }

        for (Integer tmpNum : map.keySet()) {
            if (isPrime(map.get(tmpNum))) {
                return true;
            }
        }

        return false;
    }

    private static boolean isPrime(int num) {
        if (num == 1) {
            return false;
        }

        if (num == 2) {
            return true;
        }

        for (int i = 2; i < num; i++) {
            if (num % i == 0) {
                return false;
            }
        }

        return true;
    }

    public static String maxLengthSubString(String s) {
        if (s == null) {
            return null;
        }

        // 原始串的长度
        int n = s.length();
        if (n <= 1) {
            return s;
        }

        // n >= 2
        Set<Character> set = new HashSet<>();
        int start = 0;
        int end = 1;
        int maxLength = 1;
        for (int i = 0; i < n; i++) {
            int j = i + 1;
            while (j < n && !set.contains(s.charAt(j))) {
                set.add(s.charAt(j));
                // 判断一下是否最长
                if(j - i + 1 > maxLength) {
                    start = i;
                    end = j;
                    maxLength = j - i + 1;
                }
                j++;
            }
            // 如果前面包含的话
            set = new HashSet<>();
        }

        return s.substring(start, end);
    }

    public static int[] findCoinDenominations(int[] numWays) {
        int n = numWays.length;
        if (n <= 1) return new int[0];

        List<Integer> coins = new ArrayList<>();
        int[] dp = new int[n];
        dp[0] = 1; // 初始化

        for (int i = 1; i < n; i++) {
            if (numWays[i] == 0) continue; // 无法凑出，跳过

            // 检查是否需要用 i 作为新面值
            if (dp[i] != numWays[i]) {
                coins.add(i); // 添加新面值
                // 更新 dp 数组
                for (int j = i; j < n; j++) {
                    dp[j] += dp[j - i];
                }
            }
        }

        // 转换为数组并返回
        return coins.stream().mapToInt(i -> i).toArray();
    }


    // 生成所有候选面值组合
    private static List<List<Integer>> generateCandidates(List<Integer> required, int maxDenom) {
        Set<Integer> requiredSet = new HashSet<>(required);
        List<Integer> optional = new ArrayList<>();

        // 可选面值：1..maxDenom，排除必须面值
        for (int i = 1; i <= maxDenom; i++) {
            if (!requiredSet.contains(i)) {
                optional.add(i);
            }
        }

        // 生成所有可能的组合（包括仅required的情况）
        List<List<Integer>> candidates = new ArrayList<>();
        candidates.add(new ArrayList<>(required)); // 初始组合：仅必须面值

        // 添加可选面值的所有可能组合
        for (int num : optional) {
            int size = candidates.size();
            for (int i = 0; i < size; i++) {
                List<Integer> newCandidate = new ArrayList<>(candidates.get(i));
                newCandidate.add(num);
                newCandidate.sort(Integer::compareTo);
                if (!candidates.contains(newCandidate)) {
                    candidates.add(newCandidate);
                }
            }
        }

        return candidates;
    }

    // 验证面值组合是否匹配numWays
    private static boolean verify(List<Integer> denoms, int[] numWays) {
        int n = numWays.length;
        int[] dp = new int[n];
        dp[0] = 1; // 金额0有1种方法

        for (int i = 1; i < n; i++) {
            for (int d : denoms) {
                if (i >= d) {
                    dp[i] += dp[i - d];
                }
            }
            if (dp[i] != numWays[i]) {
                return false;
            }
        }
        return true;
    }

}