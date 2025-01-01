package com.lijs.rank.tree;

/**
 * @author ljs
 * @date 2024-11-21
 * @description
 */
public class TreeNodeRank {

    static class TreeNode {
        public int value;
        public TreeNode left = null;
        public TreeNode right = null;

        public TreeNode(int value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        // 创建测试用例
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(5);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right.right = new TreeNode(6);

        System.out.println("原始二叉树（前序遍历）：");
        //preOrderPrint(root);
        System.out.println();

        // 调用展开为链表的方法
        System.out.println("将二叉树展开为链表...");
        tree2List(root);

        System.out.println("展开后的链表（右子指针）：");
       // printList(root);
    }

    //  4
    // 2  5
    //1 3  6

    //    4
    // 2   5
    //1 3   6   // 前序遍历  4 - 2 - 1 - 3 - 5 - 6
    private static void tree2List(TreeNode root) {
        while (root != null) {
            // 左为null则进行下一个节点
            if (root.left == null) {
                root = root.right;
            } else {
                // 找到左子树最右节点
                TreeNode pre = root.left;
                while (pre.right != null) {
                    pre = pre.right;
                }
                // 将原来的右子树接到左子树最右边
                pre.right = root.right;
                // 再把左子树插入到右子树的左
                root.right = root.left;
                // 继续下一个节点
                root = root.right;
            }
        }
    }
}
