package com.lijs.rank.char_opt;

import java.util.Scanner;

/**
 * @author ljs
 * @date 2025-04-01
 * @description
 */
public class CharacterTest {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String code = in.next();
        int score = 0;
        int l = 0, u = 0, m = 0, n = 0, f = 0;
        if (code.length() <= 4) score += 5;
        else if (code.length() <= 7) score += 10;
        else score += 25;
        for (char c : code.toCharArray()) {
            if (Character.isUpperCase(c)) {
                u++;
                l = 1;
            } else if (Character.isLowerCase(c)) {
                l = 1;
                if (u > 0) m = 1;
            } else if (Character.isDigit(c)) n++;
            else f++;
        }
        if (f > 0 && m == 1 && n > 0) score += 5;
        else if (l == 1 && n > 0 && f > 0) score += 3;
        else if (l == 1 && n > 0) score += 2;

        if (m == 1) score += 20;
        else if (l == 1) score += 10;

        if (n == 1) score += 10;
        else if (n > 1) score += 20;

        if (f == 1) score += 10;
        else if (f > 1) score += 25;

        if (score >= 90) System.out.println("VERY_SECURE");
        else if (score >= 80) System.out.println("SECURE");
        else if (score >= 70) System.out.println("VERY_STRONG");
        else if (score >= 60) System.out.println("STRONG");
        else if (score >= 50) System.out.println("AVERAGE");
        else if (score >= 25) System.out.println("WEAK");
        else System.out.println("VERY_WEAK");
    }

}
