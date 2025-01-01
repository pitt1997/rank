package com.lijs.rank.hash;

import java.util.HashMap;
import java.util.Map;

/**
 * @author author
 * @date 2024-12-05
 * @description
 */
public class TwoSum {

    public static void main(String[] args) {

    }

    public int[] twoSum(int[] nums, int target) {
        if(nums == null || nums.length == 0) {
            return null;
        }
        int[] res = new int[2];
        Map<Integer, Integer> indexMap = new HashMap<>();
        for(int i = 0; i < nums.length; i++) {
            indexMap.put(target - nums[i], i);
        }
        for(int i = 0; i < nums.length; i++) {
            int curr = nums[i];
            if(indexMap.containsKey(curr) && i != indexMap.get(curr)) {
                res[0] = i;
                res[1] = indexMap.get(curr);
            }
        }

        return res;
    }



}