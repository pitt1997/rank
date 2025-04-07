package com.lijs.rank.find_sub_str;

import java.util.Scanner;

/**
 * @author ljs
 * @date 2025-04-01
 * @description
 */
// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class Main {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            String t = sc.nextLine();
            if (isSubStr(s, t)) {
                System.out.println("true");
                return;
            }
            System.out.println("false");
        }
    }

    private static boolean isSubStr(String s, String t) {
        char[] charS = s.toCharArray();
        char[] charT = t.toCharArray();
        int index = 0;
        for (int i = 0; i < charT.length; i++) {
            if (charT[i] == charS[index]) {
                index++;
            }
            if (index >= charS.length) {
                return true;
            }
        }
        return false;
    }
}
