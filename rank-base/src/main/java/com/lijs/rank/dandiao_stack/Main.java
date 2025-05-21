package com.lijs.rank.dandiao_stack;

/**
 * 题目描述
 * 一贫如洗的樵夫阿里巴巴在去砍柴的路上，无意中发现了强盗集团的藏宝地，藏宝地有编号从0-N的箱子，每个箱子上面有一个数字，箱子排列成一个环，编号最大的箱子的下一个是编号为0的箱子。
 * 请输出每个箱了贴的数字之后的第一个比它大的数，如果不存在则输出-1。
 * 输入描述
 * 输入一个数字字串，数字之间使用逗号分隔，例如: 1,2,3,1
 *
 * 1 ≤ 字串中数字个数 ≤ 10000:
 * -100000 ≤ 每个数字值 ≤ 100000
 *
 * 输出描述
 * 下一个大的数列表，以逗号分隔，例如: 2,3,6,-1,6
 * 用例1
 * 输入
 *  代码解读复制代码2,5,2
 *
 * 输出
 *  代码解读复制代码5,-1,5
 *
 * 说明
 *
 * 第一个2的下一个更大的数是5;
 * 数字5找不到下一个更大的数;
 * 第二个2的下一个最大的数需要循环搜索，结果也是 5
 *
 * 用例2
 * 输入
 *  代码解读复制代码3,4,5,6,3
 *
 * 输出
 *
 * 4,5,6,-1,4
 *
 * 题解
 * 思路： 单调栈
 *
 * 定义一个栈，栈中维持递减的序列，处理过程为：当遍历一个元素时，迭代循环对比栈顶元素是否比自己小，如果是则弹出栈顶并设置栈顶元素位置值为当前遍历元素值。
 * 因为题目中箱子是成环，后面的箱子需要会前面的元素进行对比，采取循环遍历箱子两次，只在第一次循环过程元素入栈，第二次循环只进行对比，即可有效解决成环这个问题。
 *
 * 作者：行动跟不上思想
 * 链接：https://juejin.cn/post/7487858040877563904
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 *
 * @author author
 * @date 2025-04-07
 * @description
 */
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] split = sc.nextLine().split(",");
        int n = split.length;
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = Integer.parseInt(split[i]);
        }

        int[] res = new int[n];
        Arrays.fill(res, -1); // 默认填充为 -1

        // Deque<Integer> stack = new ArrayDeque<>(); // 存索引
        Stack<Integer> stack = new Stack<>(); // 存索引

        for (int i = 0; i < 2 * n; i++) {
            int curIndex = i % n;
            while (!stack.isEmpty() && nums[curIndex] > nums[stack.peek()]) {
                res[stack.pop()] = nums[curIndex];
            }

            // 这里很关键，如果每一个都被遍历过那么就后续只是比较，不再入栈
            if (i < n) {
                stack.push(curIndex);
            }
        }

        // 输出结果
        for (int i = 0; i < n; i++) {
            if (i != 0) System.out.print(",");
            System.out.print(res[i]);
        }
        System.out.println();
    }
}
