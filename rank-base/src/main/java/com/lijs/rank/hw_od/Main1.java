package com.lijs.rank.hw_od;

import java.util.*;

/**
 * @author author
 * @date 2025-04-07
 * @description
 */
public class Main1 {

    static class Job {
        int order;
        int time;
        public Job(int order, int time) {
            this.order = order;
            this.time = time;
        }
    }

    // 表示在给定的时刻向队列中添加一个任务，并且任务的执行的耗时，比如1:时间1，执行耗时：3
    // 2:时间2，执行耗时：2
    // 3:时间3，执行耗时：3
    // 输入
    // 1 3 2 2 3 3
    // 3 2
    // 输出
    // 7 0
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String n = in.nextLine();
        String m = in.nextLine();
        n = n.trim();
        m = m.trim();
        String[] arr = m.split(" ");
        // 队列大小
        int queueSize = Integer.parseInt(arr[0]);
        // 工作线程大小
        int workSize = Integer.parseInt(arr[1]);
        String[] jobArr = n.split(" ");
        int jobSize = jobArr.length / 2;
        // 所有任务集合
        List<Job> list = new ArrayList<>();
        for(int i = 0; i < jobSize; i++) {
            int o = i * 2;
            int t = i * 2 + 1;
            Job job = new Job(o, t);
            list.add(job);
        }
        // 一开始可以初始化执行workSize个任务

        // 最后一个任务执行的时间是多少


        Queue<Job> queue = new LinkedList<>();
        // 时间片每次+1
        // 纪录淘汰的 当队列满则淘汰最老的任务
        int time = 0;
        for (int i = 0; i < jobSize; i++) {
            Job job = list.get(i);
            queue.add(job);
            time += job.time;
            if (queue.size() > queueSize) {
                Job remove = queue.remove();
                time -= remove.time;
            }
        }
        // 计算剩余时间
        int remainTime = 0;
        while (!queue.isEmpty()) {
            Job job = queue.remove();
            remainTime += job.time;
        }

    }


}