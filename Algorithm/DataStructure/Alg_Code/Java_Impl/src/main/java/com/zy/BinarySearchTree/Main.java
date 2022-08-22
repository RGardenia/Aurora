package com.zy.BinarySearchTree;

import com.zy.utils.Node;
import org.junit.Test;
import sun.reflect.generics.tree.Tree;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    //#region main
    public static void main(String[] args) {

        /*BST<Integer> bst = new BST<>();
        int[] nums = {5, 3, 6, 8, 4, 2};

        for(int num: nums)
            bst.add(num);

        bst.levelOrder();    //层序遍历*/

        /*bst.preOrder();
        System.out.println();

        bst.preOrderNR();*/

        /*bst.inOrder();
        System.out.println();

        bst.postOrder();
        System.out.println();
        System.out.println(bst);*/

        BST<Integer> bst = new BST<>();
        Random random = new Random();

        int n = 10;
        for(int i = 0; i < n; i++)
            bst.add(random.nextInt(100));

        ArrayList<Integer> nums = new ArrayList<>();
        while(!bst.isEmpty())
            nums.add(bst.removeMin());

        System.out.println(nums);
        for(int i = 0; i < nums.size() ; i++)
            if(nums.get(i -  1) > nums.get(i))
                throw new IllegalArgumentException("Error!");
        System.out.println("removeMin test completed!");
    }
    //#endregion

    @Test
    public void test01(){

    }

    //#region
    //#endregion

    //#region 树的序列化与反序列化  先序
    public static String serialByPre(Node node){
        if(node == null){
            return "#_";
        }
        String res = node.getVal() + "_";
        res += serialByPre(node.getLeft());
        res += serialByPre(node.getRight());
        return res;
    }
    /**
     * 反序列化
     * @param preStr 序列化字串
     * @return 头节点
     */
    public static Node antiSerialByString(String preStr){
        String[] values = preStr.split("_");
        Queue<String> queue = new LinkedList<>();
        for(int i = 0; i != values.length; i++){
            queue.add(values[i]);
        }
        return reconByPreString(queue);
    }
    private static Node reconByPreString(Queue<String> queue){
        String value = queue.poll();
        if(value.equals("#")){
            return null;
        }
        Node head = new Node(Integer.valueOf(value));
        head.setLeft(reconByPreString(queue));
        head.setRight(reconByPreString(queue));
        return head;
    }
    //#endregion

    //#region 判断一棵二叉树是否是另一棵树的子树

    //#endregion

    //#region  返回 o1 与 o2 的最低公共祖先
    // 法 二
    public static Node lowestAncestor(Node head, Node o1, Node o2) {
        if (head == null || head == o1 || head == o2){
            return head;
        }
        Node left = lowestAncestor(head.getLeft(), o1, o2);  // o1
        Node right = lowestAncestor(head.getRight(), o1, o2);  // o2
        if(left != null && right != null){
            return head;
        }
        return left != null ? left : right;
    }
    // 法 二
    public static Node lca(Node head, Node o1, Node o2){
        HashMap<Node, Node> fatherMap = new HashMap<>();
        processMinAncestor(head, fatherMap);
        HashSet<Node> set = new HashSet<>();
        set.add(o1);
        Node cur = o1;
        while(cur != fatherMap.get(cur)){
            set.add(cur);
            cur = fatherMap.get(cur);
        }
        set.add(head);

        return null;
    }
    private static void processMinAncestor(Node head, HashMap<Node, Node> fatherMap){
        if(head == null){
            return;
        }
        fatherMap.put(head.getLeft(), head);
        fatherMap.put(head.getRight(), head);
        processMinAncestor(head.getLeft(), fatherMap);
        processMinAncestor(head.getRight(), fatherMap);
    }
    //#endregion

    //#region  判断是否为平衡二叉树
    public static boolean isBalance(Node head){
        return processBalance(head).isBalanced;
    }
    private static class ReturnType{
        public boolean isBalanced;
        public int height;
        public ReturnType(boolean isB, int height){
            isBalanced = isB;
            this.height = height;
        }
    }
    private static ReturnType processBalance(Node x){
        if(x == null) return new ReturnType(true, 0);
        ReturnType left = processBalance(x.getLeft());
        ReturnType right = processBalance(x.getRight());

        int height = Math.max(left.height, right.height) + 1;
        // 左数平衡 && 右数平衡 && 左树与右树的高度不超过 1
        boolean isBalance = left.isBalanced && right.isBalanced
                && Math.abs(left.height - right.height) < 2;

        return new ReturnType(isBalance, height);
    }
    //#endregion

    //#region  判断是否为完全二叉树
    public static boolean isCBT(Node head){
        if(head == null){
            return true;
        }
        LinkedList<Node> queue = new LinkedList<>();
        boolean leaf = false;
        Node l = null;
        Node r = null;
        queue.add(head);
        while(!queue.isEmpty()){
            head = queue.poll();
            l = head.getLeft();
            r = head.getRight();
            // leaf 为 true ，即无子节点，判断其是否有子节点
            if((leaf && (l != null || r != null)) || (l == null && r != null)){
                return false;
            }
            if(l != null){
                queue.add(l);
            }
            if(r != null){
                queue.add(r);
            }
            // 一次标记为 true ，永久为 true
            if(l == null || r == null){
                leaf = true;
            }
        }
        return true;
    }
    //#endregion

    //#region  合并二叉树
    /**
     * 递归 合并
     * @param t1
     * @param t2
     * @return
     */
    public TreeNode mergeTrees_recursive(TreeNode t1, TreeNode t2) {
        if (t1 == null) { return t2; }
        if (t2 == null) { return t1; }
        TreeNode merged = new TreeNode(t1.val + t2.val);
        merged.left = mergeTrees_recursive(t1.left, t2.left);
        merged.right = mergeTrees_recursive(t1.right, t2.right);
        return merged;
    }

    /**
     * 迭代 合并
     * @param t1
     * @param t2
     * @return
     */
    public TreeNode mergeTrees_iterative(TreeNode t1, TreeNode t2) {
        if (t1 == null) { return t2; }
        if (t2 == null) { return t1; }
        TreeNode merged = new TreeNode(t1.val + t2.val);
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        Queue<TreeNode> queue1 = new LinkedList<TreeNode>();
        Queue<TreeNode> queue2 = new LinkedList<TreeNode>();
        queue.offer(merged);
        queue1.offer(t1);
        queue2.offer(t2);
        while (!queue1.isEmpty() && !queue2.isEmpty()) {
            TreeNode node = queue.poll(), node1 = queue1.poll(), node2 = queue2.poll();
            TreeNode left1 = node1.left, left2 = node2.left, right1 = node1.right, right2 = node2.right;
            if (left1 != null || left2 != null) {
                if (left1 != null && left2 != null) {
                    TreeNode left = new TreeNode(left1.val + left2.val);
                    node.left = left;
                    queue.offer(left);
                    queue1.offer(left1);
                    queue2.offer(left2);
                } else if (left1 != null) {
                    node.left = left1;
                } else if (left2 != null) {
                    node.left = left2;
                }
            }
            if (right1 != null || right2 != null) {
                if (right1 != null && right2 != null) {
                    TreeNode right = new TreeNode(right1.val + right2.val);
                    node.right = right;
                    queue.offer(right);
                    queue1.offer(right1);
                    queue2.offer(right2);
                } else if (right1 != null) {
                    node.right = right1;
                } else {
                    node.right = right2;
                }
            }
        }
        return merged;
    }
    //#endregion

    //#region  验证二叉搜索树
    /**
     * 设计一个递归函数 helper(root, lower, upper) 来递归判断，函数表示考虑以 root 为根的子树，
     * 判断子树中所有节点的值是否都在 (l,r)(l,r) 的范围内（注意是开区间）。
     * 如果 root 节点的值 val 不在 (l,r)(l,r) 的范围内说明不满足条件直接返回，否则我们要继续递归调用检查它的左右子树是否满足，
     * 如果都满足才说明这是一棵二叉搜索树。
     *
     * 那么根据二叉搜索树的性质，在递归调用左子树时，我们需要把上界 upper 改为 root.val，
     * 即调用 helper(root.left, lower, root.val)，因为左子树里所有节点的值均小于它的根节点的值。同理递归调用右子树时，
     * 我们需要把下界 lower 改为 root.val，即调用 helper(root.right, root.val, upper)。
     *
     * 函数递归调用的入口为 helper(root, -inf, +inf)， inf 表示一个无穷大的值。
     *
     *
     * @param root 根
     * @return 返回
     */
    public boolean isValidBST(TreeNode root) {
        return isValidBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValidBST(TreeNode node, long min, long max){
        if(node == null){
            return true;
        }
        if(node.val <= min || node.val >= max) return false;
        return isValidBST(node.left, min, node.val) && isValidBST(node.right, node.val, max);
    }

    long pre = Long.MIN_VALUE; // 记录上一个节点的值，初始值为Long的最小值

    public boolean isValidBST_recursive(TreeNode root) {
        return inorder(root);
    }

    // 中序遍历   递归
    private boolean inorder(TreeNode node) {
        if(node == null) return true;
        boolean l = inorder(node.left);
        if(node.val <= pre) return false;
        pre = node.val;
        boolean r = inorder(node.right);
        return l && r;
    }

    public boolean isValidBST_recursive_inorder(TreeNode root) {
        Deque<TreeNode> stack = new LinkedList<TreeNode>();
        double inorder = -Double.MAX_VALUE;

        while (!stack.isEmpty() || root != null) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            // 如果中序遍历得到的节点的值小于等于前一个 inorder，说明不是二叉搜索树
            if (root.val <= inorder) {
                return false;
            }
            inorder = root.val;
            root = root.right;
        }
        return true;
    }
    //#endregion

    //#region  翻转二叉树
    /**
     *
     * @param root 根
     * @return 根节点
     */
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        root.left = right;
        root.right = left;
        return root;
    }
    //#endregion

    //#region 层序遍历
    /**
     * 层序遍历
     * @param root root
     * @return 层序遍历
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ret = new ArrayList<List<Integer>>();
        if (root == null) {
            return ret;
        }

        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            List<Integer> level = new ArrayList<Integer>();
            int currentLevelSize = queue.size();
            for (int i = 1; i <= currentLevelSize; ++i) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) { queue.offer(node.left); }
                if (node.right != null) { queue.offer(node.right); }
            }
            ret.add(level);
        }
        return ret;
    }
    //#endregion

    //#region 迭代遍历   前中后
    /**
     * 迭代 前序遍历
     * @param root
     * @return
     */
    public List<Integer> preorderTraversal_iterative(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        if (root == null) {
            return res;
        }

        Deque<TreeNode> stack = new LinkedList<TreeNode>();
        TreeNode node = root;
        while (!stack.isEmpty() || node != null) {
            while (node != null) {
                res.add(node.val);
                stack.push(node);
                node = node.left;
            }
            node = stack.pop();
            node = node.right;
        }
        return res;
    }

    /**
     * 迭代 中序遍历
     * @param root
     * @return
     */
    public List<Integer> inorderTraversal_iterative(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        Deque<TreeNode> stk = new LinkedList<TreeNode>();
        while (root != null || !stk.isEmpty()) {
            while (root != null) {
                stk.push(root);
                root = root.left;
            }
            root = stk.pop();
            res.add(root.val);
            root = root.right;
        }
        return res;
    }

    /**
     * 迭代  后序遍历
     * @param root
     * @return
     */
    public List<Integer> postorderTraversal_iterative(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

//        Deque<TreeNode> stack = new LinkedList<TreeNode>();
//        TreeNode prev = null;
//        while (root != null || !stack.isEmpty()) {
//            while (root != null) {
//                stack.push(root);
//                root = root.left;
//            }
//            root = stack.pop();
//            // prev 用于右节点遍历完成后不在重复遍历
//            if (root.right == null || root.right == prev) {
//                res.add(root.val);
//                prev = root;
//                root = null;
//            } else {
//                stack.push(root);
//                root = root.right;
//            }
//        }

        while (!stack.isEmpty()) {
            TreeNode curr = stack.pop();
            res.add(curr.val);
            if (curr.left != null) stack.push(curr.left);
            if (curr.right != null) stack.push(curr.right);
        }
        // 由后序遍历的倒序添加元素并反转
        Collections.reverse(res);
        return res;
    }
    //#endregion

    //#region  递归遍历   前中后
    /**
     * 递归 前序遍历
     * @param root
     * @return
     */
    public List<Integer> preorderTraversal_recursive(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        preorder(root, res);
        return res;
    }

    private void preorder(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        res.add(root.val);
        preorder(root.left, res);
        preorder(root.right, res);
    }

    /**
     *  递归 中序遍历
     * @param root
     * @return
     */
    public List<Integer> inorderTraversal_recursive(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        inorder(root, res);
        return res;
    }

    public void inorder(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        inorder(root.left, res);
        res.add(root.val);
        inorder(root.right, res);
    }

    /**
     * 递归 后序遍历
     * @param root 根
     * @return 返回
     */
    public List<Integer> postorderTraversal_recursive(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        postorder(root, res);
        return res;
    }

    private void postorder(TreeNode node, List<Integer> res) {
        if (node == null) return;
        postorder(node.left, res);
        postorder(node.right, res);
        res.add(node.val);
    }
    //#endregion
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
