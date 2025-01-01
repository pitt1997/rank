package com.lijs.rank.list;

import com.lijs.rank.Rank;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ljs
 * @date 2024-12-31
 * @description
 */
public class ListNodeTest {

    public static void main(String[] args) {
        // 双指针
        System.out.println("合并两个有序链表:");
        mergeTwoListsTest();
    }

    private static void mergeTwoListsTest() {
        Rank rank = new Rank();
        // 1 2 4
        ListNodeRank.ListNode nodeOne1 = new ListNodeRank.ListNode(1);
        ListNodeRank.ListNode nodeOne2 = new ListNodeRank.ListNode(2);
        ListNodeRank.ListNode nodeOne3 = new ListNodeRank.ListNode(4);
        nodeOne1.next = nodeOne2;
        nodeOne2.next = nodeOne3;

        // 1 3 4
        ListNodeRank.ListNode nodeTwo1 = new ListNodeRank.ListNode(1);
        ListNodeRank.ListNode nodeTwo2 = new ListNodeRank.ListNode(3);
        ListNodeRank.ListNode nodeTwo3 = new ListNodeRank.ListNode(4);
        nodeTwo1.next = nodeTwo2;
        nodeTwo2.next = nodeTwo3;
        ListNodeRank.ListNode res = mergeTwoLists(nodeOne1, nodeTwo1);
        while (res != null) {
            System.out.println(res.val);
            res = res.next;
        }
    }

    // 找到交叉的node
    public ListNodeRank.ListNode getIntersectionNode(ListNodeRank.ListNode headA, ListNodeRank.ListNode headB) {
        ListNodeRank.ListNode index = headA;
        Set<ListNodeRank.ListNode> nodeSet = new HashSet<>();
        while (index != null) {
            nodeSet.add(index);
            index = index.next;
        }
        ListNodeRank.ListNode indexB = headB;
        while (indexB != null) {
            if (nodeSet.contains(indexB)) {
                return indexB;
            } else {
                indexB = indexB.next;
            }
        }
        return null;
    }

    public static ListNodeRank.ListNode mergeTwoLists(ListNodeRank.ListNode list1, ListNodeRank.ListNode list2) {
        if (list1 == null && list2 == null) {
            return null;
        }
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        // 指针 ij
        // 结果节点
        ListNodeRank.ListNode head = new ListNodeRank.ListNode(-1);
        ListNodeRank.ListNode pHead = head;
        while (list1 != null && list2 != null) {
            if (list1.val >= list2.val) {
                // 小的获取后走一步
                pHead.next = list2;
                list2 = list2.next;
            } else {
                pHead.next = list1;
                list1 = list1.next;
            }
            pHead = pHead.next;
        }
        // list1 走完循环
        if (list1 == null) {
            pHead.next = list2;
        }

        if (list2 == null) {
            pHead.next = list1;
        }
        return head.next;
    }

    //     p1 p2 p3
    // 翻转 1->2->3->null  null<-1<-2<-3
    private static ListNodeRank.ListNode reverse(ListNodeRank.ListNode head) {
        if(head == null || head.next == null) {
            return head;
        }
        ListNodeRank.ListNode p1 = head;
        ListNodeRank.ListNode p2 = p1.next;
        ListNodeRank.ListNode p3 = p2.next;
        while(p2 != null) {
            p2.next = p1;  // 改变指向
            // 3个指针分别向后移动一位
            p1 = p2;
            p2 = p3;
            if(p3 != null) {
                p3 = p3.next;
            }
        }
        head.next = null;
        return p1;
    }

}
