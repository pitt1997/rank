package com.lijs.rank.ali;

import java.util.*;

/**
 * @author author
 * @date 2025-06-12
 * @description
 */
public class ZipSolution {
    // 问题①：字符串压缩
    //要求：将 "xxxyyyrrz" 压缩成 "3x3y2rz"，即连续相同字符用 数字+字符 表示，若只出现一次则直接保留字符。
    //
    //思路
    //遍历字符串，统计连续相同字符的个数。
    //
    //如果字符连续出现次数 >1，则拼接 次数+字符；否则直接拼接字符。

    public static String compress(String s) {
        if (s == null || s.isEmpty()) return s;

        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i - 1)) {
                count++;
            } else {
                if (count > 1) {
                    sb.append(count);
                }
                sb.append(s.charAt(i - 1));
                // 重置一下！！！
                count = 1;
            }
        }
        // 处理最后一个字符
        if (count > 1) {
            sb.append(count);
        }
        sb.append(s.charAt(s.length() - 1));
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(compress("xxxyyyrrz")); // 输出: "3x3y2rz"
        System.out.println(compress("abc"));      // 输出: "abc"
        System.out.println(compress("aabbbcc"));  // 输出: "2a3b2c"

        System.out.println(sortByRepeatCount("xxyyyrrz")); // 输出: "3x3y2rz"

    }


    // 问题②：按重复数排序
    //要求：给定字符串 "xxxyyyrrz"，压缩成 "3x3y2rz"，并按重复次数排序（重复次数相同则保持原顺序）。
    //
    //思路
    //压缩字符串：
    //
    //遍历字符串，统计连续相同字符的个数。
    //
    //如果字符连续出现次数 >1，则拼接 次数+字符；否则直接拼接字符。
    //
    //按重复次数排序：
    //
    //提取每个字符及其重复次数（如 "3x" → ('x', 3)）。
    //
    //按重复次数升序/降序排序，次数相同则保持原顺序（稳定排序）。
    //
    //重组排序后的字符串。


    // 2. 按重复次数排序
    public static String sortByRepeatCount(String s) {
        // 遍历 key -value形式
        Map<String, Pair> map = new HashMap<>();
        for (char c : s.toCharArray()) {
            if (map.containsKey(c + "")) {
                Pair pair = map.get(c + "");
                pair.count = pair.count + 1;
            } else {
                map.put(c + "", new Pair(c, 1));
            }
        }
        // map - list中
        List<Pair> pairList = new ArrayList<>();
        for (String k : map.keySet()) {
            pairList.add(map.get(k));
        }

        // 顺序
        // Collections.sort(pairList, Comparator.comparingInt(a -> a.count));

        // 倒序
        // pairList.sort(Comparator.comparingInt((Pair a) -> a.count).reversed());

        pairList.sort((a, b) -> Integer.compare(b.count, a.count));


        for (Pair pair : pairList) {
            System.out.println("c" + pair.c + ", count:" + pair.count);
        }

        return null;
    }


    // 辅助类：存储字符和重复数
    static class Pair {
        char c;
        int count;

        Pair(char c, int count) {
            this.c = c;
            this.count = count;
        }
    }
}