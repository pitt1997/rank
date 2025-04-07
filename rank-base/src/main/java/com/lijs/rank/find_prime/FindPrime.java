package com.lijs.rank.find_prime;

import java.util.Scanner;

/**
 * @author ljs
 * @date 2025-04-01
 * @description
 */
public class FindPrime {

    private static boolean isPrime(int num) {
        for (int i = 2; i <= num / i; i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    public static int count(int n) {
        int i = n / 2, j = n - i;
        while (!isPrime(i) || !isPrime(j)) {
            i++;
            j--;
        }
        return j;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int n = Integer.parseInt(in.next());
            int res = FindPrime.count(n);
            System.out.println(res);
            System.out.println(n - res);
        }
    }

}
