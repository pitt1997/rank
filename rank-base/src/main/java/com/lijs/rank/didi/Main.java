package com.lijs.rank.didi;

/**
 * @author author
 * @date 2025-06-25
 * @description
 */
public class Main {
    // 最大连续数字串
    // 完成函数：int maxnumstr(char *inputstr, char *outputstr)
    // 函数功能：找出inputstr中的最长连续数字串存储到outputstr里并返回长度，如调用maxnumstr("123abc1423a", outputstr)
    // 后返回4且outputstr中为 "1423"

    static String outputStr = null;

    public static void main(String[] args) {
        System.out.println("最大长度:" + maxNumStr("123abc1423a"));
        System.out.println("最大长度串:" + outputStr);

        outputStr = null;

        System.out.println("最大长度:" + maxNumStr("aaa"));
        System.out.println("最大长度串:" + outputStr);

        outputStr = null;

        System.out.println("最大长度:" + maxNumStr(""));
        System.out.println("最大长度串:" + outputStr);

        outputStr = null;

        System.out.println("最大长度:" + maxNumStr("123abc1423a12222222aa"));
        System.out.println("最大长度串:" + outputStr);

        outputStr = null;

        System.out.println("最大长度:" + maxNumStr(null));
        System.out.println("最大长度串:" + outputStr);

        outputStr = null;

        System.out.println("最大长度:" + maxNumStr("1234567890"));
        System.out.println("最大长度串:" + outputStr);

        outputStr = null;

        System.out.println("最大长度:" + maxNumStr("123a"));
        System.out.println("最大长度串:" + outputStr);

    }

    /**
     * 最大连续数字串
     *
     * @param inputStr
     * @return
     */
    private static int maxNumStr(String inputStr) {
        if (inputStr == null || inputStr.isEmpty()) {
            return 0;
        }

        // 输入串长度
        int len = inputStr.length();
        int max = 0;
        int startIndex = 0;

        // eg. 123abc1423a
        for (int i = 0; i < len; i++) { // i = 0
            if (!isNumber(inputStr.charAt(i))) {
                continue;
            }
            // i位置就是数字
            if (1 > max) {
                startIndex = i;
            }
            max = Math.max(max, 1);

            int j = i + 1; // j 是 i 的下一个位置
            while (j < len && isNumber(inputStr.charAt(j))) {
                int currLen = j - i + 1;
                if (currLen > max) {
                    startIndex = i;
                }
                // 这里计数一下
                max = Math.max(max, currLen);
                j++;
            }
            // i -> j
        }

        if (max > 0) {
            // 记录完整串
            outputStr = inputStr.substring(startIndex, startIndex + max);
        }

        return max;
    }

    private static boolean isNumber(char c) {
        if (c >= '0' && c <= '9') {
            // 数字串
            return true;
        }
        return false;
    }
}