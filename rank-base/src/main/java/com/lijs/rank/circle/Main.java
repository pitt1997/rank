package com.lijs.rank.circle;

/**
 * @author ljs
 * @date 2025-04-01
 * @description
 */
class ListNode {

    int val;
    ListNode next = null;

    ListNode(int val) {
        this.val = val;
    }
}

public class Main {

    public int LastRemaining_Solution(int n, int m) {

        if (n <= 0 || m <= 0) {
            return -1;
        }

        ListNode head = new ListNode(0);
        ListNode node = head;
        for (int i = 1; i < n; i++) {
            node.next = new ListNode(i);
            node = node.next;
        }
        node.next = head;

        int k = 0;
        while (node.next != node) { // 结束条件
            if (++k == m) {
                node.next = node.next.next;
                k = 0;
            } else {
                node = node.next;
            }
        }

        return node.val;
    }

}
