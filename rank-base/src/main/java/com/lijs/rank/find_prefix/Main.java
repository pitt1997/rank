package com.lijs.rank.find_prefix;

/**
 * @author ljs
 * @date 2025-04-01
 * @description
 */
public class Main {

    // s.startsWith(); 知悉方法的使用

    public String longestCommonPrefix(String[] strs) {
        int length = strs.length;
        if (length == 1) {
            return strs[0];
        }

        String res = "";
        String sample = strs[0];
        for (int i = 0; i < sample.length(); i++) {
            res = sample.substring(0, i + 1);
            for (String s : strs) {
                if (!s.startsWith(res)) {
                    // 结束
                    if (res.length() == 1) {
                        return "";
                    } else {
                        return res.substring(0, res.length() - 1);
                    }
                }
            }
        }

        return res;
    }
}

