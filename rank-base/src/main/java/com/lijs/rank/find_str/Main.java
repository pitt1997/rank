package com.lijs.rank.find_str;

import java.util.Scanner;

/**
 * 找出字符串中第一个只出现一次的字符
 *
 * @author ljs
 * @date 2025-04-01
 * @description
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String str = sc.nextLine();
            System.out.println(firstOneStr(str));
        }
    }

    private static String firstOneStr(String str) {
        for (int i = 0; i < str.length(); i++) {
            String tempStr = str.replaceAll(str.charAt(i) + "", "");
            if (str.length() - tempStr.length() == 1) {
                return str.charAt(i) + "";
            }
        }
        return "-1";
    }

}
