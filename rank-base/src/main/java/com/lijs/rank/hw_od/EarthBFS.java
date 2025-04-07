package com.lijs.rank.hw_od;

import java.util.*;

/**
 * 这是一个典型的模拟 + 广度优先搜索（BFS）问题，可以类比于“多源 BFS 扩散染色”。
 * 思路总结：
 * 1. 把每个手动启动的发动机看作 BFS 的起点。
 * 2. 每个发动机被启动后，会在下一时刻关联启动它相邻的两个发动机（注意：首尾相连，即 0 和 N-1 互为相邻）。
 * 3. 记录每个发动机最早被启动的时间。
 * 4. 最后找出启动时间最大的那些发动机。
 *
 * 测试：
 * 8 2
 * 0 2
 * 0 6
 *
 * 输出
 * 2
 * 4
 *
 * @author ljs
 * @date 2025-04-07
 * @description
 */
public class EarthBFS {

    static class Node {
        // 位置
        private int index;

        // 启动时间
        private int time;

        public Node(int index, int time) {
            this.index = index;
            this.time = time;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }

    // 有发动机M个，点火的有N个
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();
        int n = sc.nextInt();

        int[] times = new int[m];
        Queue<Node> queue = new LinkedList<>();
        Arrays.fill(times, Integer.MAX_VALUE);

        // N组数据依次读取 0 2 ： 标识时间 0 时点火位置2
        for (int i = 0; i < n; i++) {
            int t = sc.nextInt();
            int p = sc.nextInt();
            if (t < times[p]) {
                times[p] = t;
            }
            Node node = new Node(p, t);
            queue.add(node);
        }

        // queue 不空
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            int index = node.index;
            int tTime = node.time + 1;
            int left = index == 0 ? m - 1 : index - 1;
            int right = index == m - 1 ? 0 : index + 1;
            if (times[left] > tTime) { // 当前值 > 期望值
                // 设置期望值
                times[left] = tTime;
                queue.offer(new Node(left, tTime));
            }
            if (times[right] > tTime) { // 当前值 > 期望值
                // 设置期望值
                times[right] = tTime;
                queue.offer(new Node(right, tTime));
            }
        }

        // 找到最大的
        int max = Integer.MIN_VALUE;
        for (int tmp : times) {
            max = Math.max(max, tmp);
        }

        // System.out.println("max:" + max);

        List<Integer> res = new ArrayList<>();
        // 继续迭代出来最大的
        for (int i = 0; i < m; i++) {
            if (times[i] == max) {
                res.add(i);
            }
        }

        for (Integer re : res) {
            System.out.println(re);
        }

    }

}

