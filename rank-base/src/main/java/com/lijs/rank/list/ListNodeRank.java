package com.lijs.rank.list;

/**
 * @author author
 * @date 2024-11-20
 * @description 链表相关题目
 */
public class ListNodeRank {

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    // 反转链表
    // 1 -> 2 -> 3 -> 4 -> 5
    // p1   p2   p3
    // 1 <- 2 <- 3 <- 4 <- 5
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode p1 = head;
        ListNode p2 = p1.next;
        ListNode p3 = p2.next;
        while (p2 != null) {
            p2.next = p1;  // 改变指向
            // 3个指针分别向后移动一位
            p1 = p2;
            p2 = p3;
            if (p3 != null) {
                p3 = p3.next;
            }
        }
        head.next = null;
        return p1;
    }


}