package com.lijs.rank.hw_od;

import java.util.Scanner;

/**
 * @author author
 * @date 2025-04-07
 * @description
 */
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long sum = 0;
        int n = in.nextInt();
        in.nextLine();
        // 2
        // 20CNY53fen
        // 53HKD87cents
        for (int i = 0; i < n; i++) {
            String mon = in.nextLine();
            sum = sum + convert(mon);
        }

        System.out.println(sum);
    }

    private static long convert(String mon) {
        long sum = 0;
        long f = 0;
        if (mon.contains("CNY") || mon.contains("fen")) {
            if (mon.contains("CNY")) {
                String[] arr = mon.split("CNY");
                int c = Integer.parseInt(arr[0]);
                f = c * 100;
                if (arr.length >= 2) {
                    f = f + Integer.parseInt(arr[1].replace("fen", ""));
                }
            } else {
                f = Integer.parseInt(mon.replace("fen", ""));
            }
            sum = f;
        } else if (mon.contains("HKD") || mon.contains("cents")) {
            if (mon.contains("HKD")) {
                String[] arr = mon.split("HKD");
                int c = Integer.parseInt(arr[0]);
                f = c * 100;
                if (arr.length >= 2) {
                    f = f + Integer.parseInt(arr[1].replace("cents", ""));
                }
            } else {
                f = Integer.parseInt(mon.replace("cents", ""));
            }
            sum = (f * 100) / 123;
        } else if (mon.contains("JPY") || mon.contains("sen")) {
            if (mon.contains("JPY")) {
                String[] arr = mon.split("JPY");
                int c = Integer.parseInt(arr[0]);
                f = c * 100;
                if (arr.length >= 2) {
                    f = f + Integer.parseInt(arr[1].replace("sen", ""));
                }
            } else {
                f = Integer.parseInt(mon.replace("sen", ""));
            }
            sum = (f * 100) / 1825;
        } else if (mon.contains("EUR") || mon.contains("euro")) {
            if (mon.contains("EUR")) {
                String[] arr = mon.split("EUR");
                int c = Integer.parseInt(arr[0]);
                f = c * 100;
                if (arr.length >= 2) {
                    f = f + Integer.parseInt(arr[1].replace("euro", ""));
                }
            } else {
                f = Integer.parseInt(mon.replace("euro", ""));
            }
            sum = (f * 100) / 14;
        } else if (mon.contains("GBP") || mon.contains("pence")) {
            if (mon.contains("GBP")) {
                String[] arr = mon.split("GBP");
                int c = Integer.parseInt(arr[0]);
                f = c * 100;
                if (arr.length >= 2) {
                    f = f + Integer.parseInt(arr[1].replace("pence", ""));
                }
            } else {
                f = Integer.parseInt(mon.replace("pence", ""));
            }
            sum = (f * 100) / 12;
        }
        return sum;
    }
}