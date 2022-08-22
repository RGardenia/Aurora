package com.zy.AVLTree;

import java.util.ArrayList;

public class AVLTree<K extends Comparable<K>, V> {

    private class Node{
        public K key;
        public V value;
        public Node left, right;
        public int height;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            left = null;
            right = null;
            height = 1;
        }
    }

    private Node root;
    private int size;

    public AVLTree(){
        root = null;
        size = 0;
    }

    public int getSize(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public boolean isBST(){
        ArrayList<K> keys = new ArrayList<>();
        inOrder(root, keys);
        for(int i = 1; i < keys.size(); i++)
            if(keys.get(i-1).compareTo(keys.get(i)) > 0)
                return false;
        return true;
    }

    private void inOrder(Node node, ArrayList<K> keys){

        if(node == null)
            return;

        inOrder(node, keys);
        keys.add(node.key);
        inOrder(node.right, keys);
    }

    //判断该二叉树是否是一颗平衡二叉树
    public boolean isBalanced(){
        return isBalanced(root);
    }

    private boolean isBalanced(Node node){

        if(node == null)
            return true;

        int balanceFactor = getBalanceFactor(node);
        if(Math.abs(balanceFactor) > 1)
            return false;
        return isBalanced(node.left) && isBalanced(node.right);
    }

    private int getHeight(Node node){
        if(node == null)
            return 0;
        return node.height;
    }

    //获得node的平衡因子
    private int getBalanceFactor(Node node){
        if(node == null)
            return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    //右旋转
    private Node rightRotate(Node y){

        Node x = y.left;
        Node T3 = x.right;

        //向右旋转
        x.right = y;
        y.left = T3;

        //更新height
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    //左旋转
    private Node leftRotate(Node y){

        Node x = y.right;
        Node T2 = x.left;

        //向左旋转
        x.left = y;
        y.right = T2;

        //更新height
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    public void add(K key, V value){
        root = add(root, key, value);
    }

    //向以root为根的二分搜索树中插入新元素，递归
    private Node add(Node node,K key, V value){

        if(node == null){
            size++;
            return new Node(key, value);
        }

        if(key.compareTo(node.key) < 0)
            node.left = add(node.left, key, value);
        else if(key.compareTo(node.key) > 0)
            node.right = add(node.right, key, value);
        else      //相等的时候
            node.value = value;

        //更新height
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        //计算平衡因子
        int balanceFactor = getBalanceFactor(node);
        if(Math.abs(balanceFactor) > 1)
            System.out.println("unbalance : " + balanceFactor);

        //维护平衡性
        //LL
        if(balanceFactor > 1 && getBalanceFactor(node.left) >= 0)
            return rightRotate(node);
        //RR
        if(balanceFactor < -1 && getBalanceFactor(node.right) <= 0)
            return leftRotate(node);
        //LR
        if(balanceFactor > 1 && getBalanceFactor(node.left) < 0){
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        //RL
        if(balanceFactor < -1 && getBalanceFactor(node.right) > 0){
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    private Node getNode(Node node, K key){
        if(node == null)
            return null;

        if(key.equals(node.key))
            return node;
        else if(key.compareTo(node.key) < 0)
            return getNode(node.left, key);
        else
            return getNode(node.right, key);
    }

    //二分搜索树中是否包含元素 e
    public boolean contains(K key){
        return getNode(root, key) != null;
    }

    public V get(K key){

        Node node = getNode(root, key);
        return node == null ? null : node.value;
    }

    public void set(K key, V newValue){
        Node node = getNode(root, key);
        if(node == null)
            throw new IllegalArgumentException(key + "does't exist!");

        node.value = newValue;
    }
    //返回以node为根的二分搜索树的最小值所在的结点
    private Node minimum(Node node){
        if(node.left == null)
            return node;
        return minimum(node.left);
    }

    //删除以node为根的二分搜索树中的最小节点
    //返回新二分搜索树的根
    /*public Node removeMin(Node node){
        if(node.left == null) {
            Node rightNode = node.right;
            node.right = null;
            size --;
            return rightNode;
        }

        node.left = removeMin(node.left);
        return node;
    }
*/

    public V remove(K key) {

        Node node = getNode(root, key);
        if(node != null){
            root = remove(root, key);
            return node.value;
        }
        return null;
    }

    //删除以node为根的二分搜索树中键为key的结点， （递归）  返回新树的根
    private Node remove(Node node, K key){

        if(node == null)
            return null;

        Node retNode;
        if(key.compareTo(node.key) < 0){
            node.left = remove(node.left, key);
            retNode = node;
        }
        else if(key.compareTo(node.key) > 0) {
            node.right = remove(node.right, key);
            retNode =  node;
        }
        else{
            //key.comparableTo(node.e) == 0

            //待删除结点左子树为空
            if(node.left == null){
                Node rightNode = node.right;
                node.right = null;
                size--;
                retNode =  rightNode;
            }

            //待删除结点右子树为空
            else if(node.right == null){
                Node leftNode = node.left;
                node.left = null;
                size--;
                retNode = leftNode;
            }

            else {//待删除结点左右都不为空
                // 找到比待删除结点大的最小结点，即待删除结点右子树的最小结点
                // 用这个结点代替删除的结点
                Node successor = minimum(node.right);
                //minimum返回以参数为根的树中最小的结点
                successor.right = remove(node.right, successor.key);
                //找到待删除结点右子树中的最小结点并删除，返回右子树的根给successor
                successor.left = node.left;

                node.left = node.right = null;

                retNode = successor;
            }
        }

        if(retNode == null)
            return null;

        //维护平衡
        //更新height
        retNode.height = 1 + Math.max(getHeight(retNode.left), getHeight(retNode.right));

        //计算平衡因子
        int balanceFactor = getBalanceFactor(retNode);

        //维护平衡性
        //LL
        if(balanceFactor > 1 && getBalanceFactor(retNode.left) >= 0)
            return rightRotate(retNode);
        //RR
        if(balanceFactor < -1 && getBalanceFactor(retNode.right) <= 0)
            return leftRotate(retNode);
        //LR
        if(balanceFactor > 1 && getBalanceFactor(retNode.left) < 0){
            retNode.left = leftRotate(retNode.left);
            return rightRotate(retNode);
        }
        //RL
        if(balanceFactor < -1 && getBalanceFactor(retNode.right) > 0){
            retNode.right = rightRotate(retNode.right);
            return leftRotate(retNode);
        }

        return retNode;
    }
}
