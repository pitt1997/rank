package com.lijs.rank.regex;

/**
 * @author author
 * @date 2025-07-23
 * @description
 */

import java.util.regex.Pattern;

public class RegexPerformanceTest {
    private static final String DATE_VALID_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";
    private static final Pattern PRECOMPILED_PATTERN = Pattern.compile(DATE_VALID_PATTERN);
    private static final String TEST_DATE = "2023-10-01";

    public static void main(String[] args) {
        int iterations = 1000; // 测试 100 次或 1000 次
        testStringMatches(iterations);
        testPrecompiledPattern(iterations);
        testSimpleCharCheck(iterations);
    }

    // 1. 使用 String.matches()
    private static void testStringMatches(int iterations) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            TEST_DATE.matches(DATE_VALID_PATTERN);
        }
        long duration = System.nanoTime() - start;
        System.out.printf("String.matches() %d次: %d ns (%.3f ms)\n",
                iterations, duration, duration / 1_000_000.0);
    }

    // 2. 使用预编译 Pattern
    private static void testPrecompiledPattern(int iterations) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            PRECOMPILED_PATTERN.matcher(TEST_DATE).matches();
        }
        long duration = System.nanoTime() - start;
        System.out.printf("Precompiled Pattern %d次: %d ns (%.3f ms)\n",
                iterations, duration, duration / 1_000_000.0);
    }

    // 3. 直接字符比对（最快）
    private static void testSimpleCharCheck(int iterations) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            isValidDate(TEST_DATE);
        }
        long duration = System.nanoTime() - start;
        System.out.printf("Simple char check %d次: %d ns (%.3f ms)\n",
                iterations, duration, duration / 1_000_000.0);
    }

    private static boolean isValidDate(String date) {
        if (date == null || date.length() != 10) return false;
        return date.charAt(4) == '-' && date.charAt(7) == '-';
    }
}