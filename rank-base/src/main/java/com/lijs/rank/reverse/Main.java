package com.lijs.rank.reverse;

/**
 * StringBuffer 才有 reverse
 *
 * @author ljs
 * @date 2025-04-01
 * @description
 */
public class Main {
    public String reverseWords(String s) {
        s = s.trim();
        StringBuffer sb = new StringBuffer(s);
        s = sb.reverse().toString();

        String[] arr = s.split(" ");
        String res = "";
        for (String str : arr) {
            if ("".equals(str.trim())) {
                continue;
            }
            // 继续反转

            StringBuffer sbT = new StringBuffer(str);

            str = sbT.reverse().toString();

            res = res + " " + str;
        }
        res = res.trim();
        return res;
    }
}