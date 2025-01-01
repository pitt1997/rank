package com.lijs.rank;

/**
 * @author ljs
 * @date 2024-11-07
 * @description Rank
 */
public class Rank {

    public static void main(String[] args) {
        // 二分

        // 堆

        // 栈、链表、队列、

        // 线程死锁

        String s1 = "abcde";
        String s2 = "abfce";


        //System.out.println(myAtoi("  -43"));
        System.out.println(myAtoi("  43"));
        System.out.println(myAtoi(" sda 43"));
        System.out.println(myAtoi(" 4 43 a"));

        System.out.println(myAtoiA("  43"));
        System.out.println(myAtoiA(" sda 43"));
        System.out.println(myAtoiA(" 4 43 a"));

        System.out.println("======");
        //String result = findLongestCommonSubstring(s1, s2);
        //System.out.println("最长公共子串是: " + result);

        // juc countDownLatch Semaphore ... automic integer...

        // 其他

        // 面经

    }


    public static int myAtoiA(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int sign = 1; // 符号
        int i = 0;
        int n = s.length();
        int ans = 0;
        while (i < n && s.charAt(i) == ' ') {
            i++;
        }

        while (i < n && s.charAt(i) == '+' || s.charAt(i) == '-') {
            sign = s.charAt(i) == '-' ? -1 : sign;
            i++;
        }

        while (i < n && Character.isDigit(s.charAt(i))) {
            int cur = s.charAt(i) - '0';
            ans = ans * 10 + cur;
            i++;
        }

        return ans * sign;
    }


    public static int myAtoi(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        int i = 0, sign = 1, result = 0;
        int n = s.length();

        // 1. 去除空格
        while (i < n && s.charAt(i) == ' ') {
            i++;
        }

        // 2. 处理符号
        if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
            sign = (s.charAt(i) == '-') ? -1 : 1;
            i++;
        }

        // 3. 逐字符转换为整数
        while (i < n && Character.isDigit(s.charAt(i))) {
            int digit = s.charAt(i) - '0';

            // 4. 检查是否溢出
            if (result > (Integer.MAX_VALUE - digit) / 10) {
                return (sign == 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }

            result = result * 10 + digit;
            i++;
        }

        return result * sign;
    }

}
