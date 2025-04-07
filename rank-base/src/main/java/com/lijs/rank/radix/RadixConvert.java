package com.lijs.rank.radix;

import java.util.Scanner;

/**
 * @author ljs
 * @date 2025-04-01
 * @description
 */
public class RadixConvert {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            System.out.println(Integer.parseInt(s.substring(2, s.length()), 16));
        }
    }

    public static void testMy() {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        s = s.replace("0x", "");
        int num = Integer.parseInt(s, 16);
        System.out.println(num);
    }

}
