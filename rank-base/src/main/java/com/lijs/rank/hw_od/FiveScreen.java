package com.lijs.rank.hw_od;

import java.util.Scanner;

/**
 * 5、标题:5键键盘的输出
 * 【5键键盘的输出】有一个特殊的 5键键盘，上面有 a,ctrl-c,ctrl-x,ctrl-v,ctrl-a五个键。
 * a键在屏幕上输出一个字母 a;
 * ctrl-c将当前选择的字母复制到剪贴板;
 * ctrl-x将当前选择的 字母复制到剪贴板，并清空选择的字母;
 * ctrl-v将当前剪贴板里的字母输出到屏幕;
 * ctrl-a 选择当前屏幕上所有字母。
 * 注意:
 * 1、剪贴板初始为空，新的内容被复制到剪贴板时会覆盖原来的内容
 * 2、当屏幕上没有字母时，ctrl-a无效
 * 3、当没有选择字母时，ctrl-c和 ctrl-x无效
 * 4、当有字母被选择时，a和ctrl-v这两个有输出功能的键会先清空选择的字母，再进行输出
 * 给定一系列键盘输入，输出最终屏幕上字母的数量。
 * 输入描述:
 * 输入为一行，为简化解析，用数字 12345代表 a,ctrl-c,ctrl-x,ctrl-v,ctrl-a五个键的输入，数字用空格分隔
 * 输出描述:
 * 输出一个数字，为最终屏目上字母的数量。
 *
 * @author ljs
 * @date 2025-04-07
 * @description
 */
public class FiveScreen {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // 1 2 3 4 5
        String[] opts = sc.nextLine().split(" ");
        // 展示
        StringBuffer res = new StringBuffer();
        // 是否选中
        boolean selected = false;
        // 剪切板
        String cp = "";

        for (String opt : opts) {
            // 用数字 12345代表 a,ctrl-c,ctrl-x,ctrl-v,ctrl-a五个键的输入，数字用空格分隔
            if("1".equals(opt)) {
                //
                if (selected) {
                    res = new StringBuffer();
                    selected = false;
                }
                res.append("a");
            } else if ("2".equals(opt)) {
                if (selected) {
                    cp = res.toString();
                }
            } else if ("3".equals(opt)) {
                if (selected) {
                    cp = res.toString();
                    res = new StringBuffer();
                    selected = false;
                }
            } else if ("4".equals(opt)) { // ctrl-v
                if (selected) {
                    res = new StringBuffer();
                    selected = false;
                }
                res.append(cp);
            } else if ("5".equals(opt)) {
                if (!res.toString().isEmpty()) {
                    selected = true;
                }
            } else {
                // 错误
            }
        }

        System.out.println(res.length());
    }

}

