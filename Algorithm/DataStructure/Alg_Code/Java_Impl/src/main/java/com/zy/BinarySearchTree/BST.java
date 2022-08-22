package com.zy.BinarySearchTree;
import java.util.*;

public class BST <E extends Comparable<E>> {

    private class Node{
        public E e;
        public Node left, right;

        public Node() {
            this.e = null;
            left = null;
            right = null;
        }

        public Node(E e){
            this.e = e;
            left = null;
            right = null;
        }
    }

    private Node root;
    private int size;

    public BST() {
        root = new Node();
        size = 0;
    }

    public BST(E e){
        root = new Node(e);
        size = 0;
    }

    public int getSize(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    //向二分搜索树中添加新元素e
    public void add(E e){

        root = add(root, e);
    }

    //向以root为根的二分搜索树中插入新元素，递归
    private Node add(Node node, E e){

        /*
        if(e.equals(node.e))
            return;
        else if(e.compareTo(node.e) < 0 && node.left == null){ //如果比node的e小
            node.left = new Node(e);
            size ++;
            return;
        }
        else if(e.compareTo(node.e) > 0 && node.right == null){
            node.right = new Node(e);
            size ++;
            return;
        }
        */

        if(node == null){
            size++;
            return new Node(e);
        }

        if(e.compareTo(node.e) < 0)
            node.left = add(node.left, e);
        else if(e.compareTo(node.e) > 0)
            node.right = add(node.right, e);

        return node;
    }

    //二分搜索树中是否包含元素e
    public boolean contains(E e){
        return contains(root, e);
    }

    private boolean contains(Node node, E e){

        if(node == null)
            return false;

        if(e.compareTo(node.e) == 0)
            return true;
        else if(e.compareTo(node.e) < 0)
            return contains(node.left, e);
        else
            return contains(node.right, e);
    }

    //二分搜索树的前序遍历(递归)
    public void preOrder(){
        preOrder(root);
    }

    private void preOrder(Node node){

/*        if(node == null)
            return;*/

        if(node != null){
            System.out.println(node.e);
            preOrder(node.left);
            preOrder(node.right);
        }
    }

    //二分搜索树的非递归方法前序遍历
    public void preOrderNR(){
        Stack<Node> stack = new Stack<>();
        stack.push(root);
        while(!stack.isEmpty()){
            Node cur = stack.pop();
            System.out.println(cur.e);

            if(cur.right != null)
                stack.push(cur.right);
            if(cur.left != null)
                stack.push(cur.left);
        }
    }

    //中序遍历
    public void inOrder(){
        inOrder(root);
    }

    private void inOrder(Node node){

        if(node == null)
            return;
        //依次执行完所有的inorder方法的代码
        inOrder(node.left);
        System.out.println(node.e);
        inOrder(node.right);
    }

    //中序遍历（迭代一）
    public void inorder1(){

        Deque<Node> s = new ArrayDeque<>();
        Node node = root;

        while(!s.isEmpty() || node != null){
            while(node != null){
                s.push(node);
                node = node.left;
            }

            Node cur = s.pop();
            System.out.println(cur.e);
            if(cur.right != null)
                cur = cur.right;
        }
    }

    //中序遍历（迭代二）
    public void inorder2(){

        Deque<Node> s = new ArrayDeque<>();
        Node node = root;

        while(node != null || !s.isEmpty()){
            if(node != null){
                s.push(node);
                node = node.left;
            }else{
                //依次取出栈中的元素
                Node cur = s.pop();

                System.out.println(cur.e);
                node = node.right;
            }
        }
    }

    //后序遍历
    public void postOrder(){
        postOrder(root);
    }

    private void postOrder(Node node){

        if(node == null)
            return;

        postOrder(node.left);
        postOrder(node.right);
        System.out.println(node.e);
    }

    //后序遍历（迭代一）
    public void postOrderTraversal1(){
        List<Node> list = new ArrayList<>();
        if(root == null)
            return;

        Deque<Node> d = new ArrayDeque<>();
        Deque<Node> s = new ArrayDeque<>();

        s.push(root);
        while(!s.isEmpty()){
            Node node = s.pop();
            if(node.left != null)
                s.push(node.left);

            if(node.right != null)
                s.push(node.right);

            d.push(node);
        }

        while(!d.isEmpty())
            list.add(d.pop());
    }

    //后序遍历（迭代二）
    public List<Node> postOrderTraversal2(){
        Node node = root;

        List<Node> res = new ArrayList<>();
        if (node == null) {
            return res;
        }

        Deque<Node> stack = new LinkedList<Node>();
        Node prev = null;
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }

            //将左子树的最后一个给node
            node = stack.pop();
            if (node.right == null || node.right == prev) {
                res.add(node);
                prev = node;
                node = null;
            } else {
                stack.push(node);
                node = node.right;
            }
        }
        return res;
    }


    //二分搜索树的层序遍历（广度优先遍历）
    public void levelOrder(){

        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()){
            Node cur = q.remove();
            System.out.println(cur.e);

            if(cur.left != null)
                q.add(cur.left);
            if(cur.right != null)
                q.add(cur.right);
        }
    }

    //二分搜索树的最小值和最大值,返回其值
    public E minimum(){
        if(size == 0)
            throw new IllegalArgumentException("BST is empty!");

        return minimum(root).e;
    }
    //找到以root为根的最小结点
    private Node minimum(Node node){
        if(node.left == null)
            return node;
        return minimum(node.left);
    }

    public E maximum(){
        if(size == 0)
            throw new IllegalArgumentException("BST is empty!");

        return maximum(root).e;
    }
    //找到以root为根的最大结点
    private Node maximum(Node node){
        if(node.right == null)
            return node;
        return maximum(node.right);
    }

    //删除最小值,返回删去的最小值
    public E removeMin(){
        E ret = minimum();
        root = removeMin(root);

        return ret;
    }//
    private Node removeMin(Node node){
        //找到左子树的最后一个，并删除
        if(node.left == null){    //即要删除node这个结点
            Node rightNode = node.right;
            node.right = null;
            size --;
            return rightNode;
        }
        //到达最底层后，删除结点的右子树作为返回值返回给删除结点的上一结点，即可删除最小值
        node.left = removeMin(node.left);
        return node;
        //最终返回的是删除后的二叉搜索树的根节点
    }

    //删除最大值,返回删去的最大值
    public E removeMax(){
        E ret = maximum();
        root = removeMax(root);

        return ret;
    }
    private Node removeMax(Node node){

        if(node.right== null){
            Node leftNode = node.left;
            node.left = null;
            size --;
            return leftNode;
        }

        node.right = removeMax(node.right);
        return node;
    }

    //在二分搜索树中删除指定的任意值,返回新的二分搜索树的根节点
    //找右子树中的后继，也可以找左子树中的前驱
    //后继为右子树中的最小值，前驱为左子树中的最大值
    public void remove(E e){
        root = remove(root, e);
    }

    private Node remove(Node node, E e){

        if(node == null)
            return null;

        if(e.compareTo(node.e) < 0){
            node.left = remove(node.left, e);
            return node;
        }else if(e.compareTo(node.e) > 0){
            node.right = remove(node.right, e);
            return node;
        }
        //上面在寻找需删除的结点e
        else{
            //e == node.e
            //待删除结点左子树为空的情况
            if(node.left == null){
                Node rightNode = node.right;
                node.right = null;
                size --;
                return rightNode;
            }
            //待删除结点右子树为空的情况
            if(node.right == null){
                Node leftNode = node.left;
                node.left = null;
                size --;
                return leftNode;
            }
//            待删除结点左右子树均不为空的情况
//            找到比待删除结点大的最小结点，即待删除结点右子树的最小结点
//            用这个结点代替删除的结点
            Node successor = minimum(node.right);
            successor.right = removeMin(node.right);
            successor.left = node.left;

            node.left = node.right = null;

            return successor;
        }
    }

    @Override
    public String toString(){
        StringBuilder res = new StringBuilder();
        generateBSTString(root, 0, res);
        return res.toString();
    }


    private void generateBSTString(Node node, int depth, StringBuilder res){

        if(node == null){
            res.append(generateDepthString(depth) + "null\n");
            return;
        }

        res.append(generateDepthString(depth) + node.e + "\n");
        generateBSTString(node.left, depth + 1, res);
        generateBSTString(node.right, depth +1, res);
    }

    private String generateDepthString(int depth){
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < depth; i++)
            res.append("--");
        return res.toString();
    }
}
