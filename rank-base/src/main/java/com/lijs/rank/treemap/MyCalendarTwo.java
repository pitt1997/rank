package com.lijs.rank.treemap;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author author
 * @date 2025-01-04
 * @description
 */
public class MyCalendarTwo {

    static TreeMap<Integer, Integer> cnt = new TreeMap<Integer, Integer>();

    public MyCalendarTwo() {
        //cnt = new TreeMap<Integer, Integer>();
    }

    public static boolean book(int start, int end) {
        int ans = 0;
        int maxBook = 0;
        cnt.put(start, cnt.getOrDefault(start, 0) + 1);
        cnt.put(end, cnt.getOrDefault(end, 0) - 1);
        for (Map.Entry<Integer, Integer> entry : cnt.entrySet()) {
            int freq = entry.getValue();
            maxBook += freq;
            ans = Math.max(maxBook, ans);
            if (maxBook > 2) {
                cnt.put(start, cnt.getOrDefault(start, 0) - 1);
                cnt.put(end, cnt.getOrDefault(end, 0) + 1);
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
//        MyCalendarTwo.book(10,12);
//        MyCalendarTwo.book(13,15);
//        MyCalendarTwo.book(17,19);

        System.out.println(1);

        MyCalendarTwo.book(10,15);
        MyCalendarTwo.book(12,18);
        MyCalendarTwo.book(17,19);
    }


}