package com.lijs.rank.hw_od;

import java.util.*;

/**
 * @author author
 * @date 2025-04-07
 * @description
 */
public class DeapMain {

    public static int maxProfit(List<Integer> mList, List<Integer> nList, int capital) {
        List<Fruit> fruits = new ArrayList<>();

        // 1. 构建有利润的水果列表
        for (int i = 0; i < mList.size(); i++) {
            int cost = mList.get(i);
            int sell = nList.get(i);
            int profit = sell - cost;
            if (profit > 0) {
                fruits.add(new Fruit(cost, profit));
            }
        }

        // 2. 按成本升序排序
        fruits.sort(Comparator.comparingInt(f -> f.cost));

        // 3. 最大堆：存放当前可购买的水果利润
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

        int index = 0;
        while (true) {
            // 加入所有当前本钱能买得起的水果利润
            while (index < fruits.size() && fruits.get(index).cost <= capital) {
                maxHeap.offer(fruits.get(index).profit);
                index++;
            }

            if (maxHeap.isEmpty()) break;

            // 买入并卖出利润最高的水果
            capital += maxHeap.poll();
        }

        return capital;
    }

    static class Fruit {
        int cost;
        int profit;

        Fruit(int cost, int profit) {
            this.cost = cost;
            this.profit = profit;
        }
    }

    public static void main(String[] args) {
        // 示例输入
        Scanner scanner = new Scanner(System.in);
        String[] mStr = scanner.nextLine().split(",");
        String[] nStr = scanner.nextLine().split(",");
        int capital = Integer.parseInt(scanner.nextLine());

        List<Integer> mList = new ArrayList<>();
        List<Integer> nList = new ArrayList<>();
        for (String s : mStr) mList.add(Integer.parseInt(s.trim()));
        for (String s : nStr) nList.add(Integer.parseInt(s.trim()));

        int result = maxProfit(mList, nList, capital);
        System.out.println(result);
    }

}