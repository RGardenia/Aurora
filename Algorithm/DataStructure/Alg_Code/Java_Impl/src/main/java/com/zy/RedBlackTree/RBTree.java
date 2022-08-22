package com.zy.RedBlackTree;

public class RBTree<K extends Comparable<K>, V>{

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node{
        public K key;
        public V value;
        public Node left, right;
        public boolean color;

        public Node(K key, V value){
            this.key = key;
            this.value = value;
            left = null;
            right = null;
            color = RED;
        }
    }

    private Node root;
    private int size;

    public RBTree(){
        root = null;
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    //判断一个节点node的颜色
    private boolean isRed(Node node){
        if(node == null)
            return BLACK;
        return node.color;
    }

    //左旋转
    private Node leftRotate(Node node){

        Node x = node.right;

        node.right = x.left;
        x.left = node;

        x.color = node.color;;
        node.color = RED;

        return x;
    }

    //右翻转
    private Node rightRotate(Node node){

        Node x = node.left;

        node.left = x.right;
        x.right = node;

        x.color = node.color;
        node.color = RED;

        return x;
    }

    //颜色翻转
    private void flipColors(Node node){

        node.color = RED;
        node.left.color = BLACK;
        node.right.color = BLACK;
    }

    //添加新元素（key， value）
    public void add(K key, V value) {
        root = add(root , key, value);
        root.color = BLACK;     //保持根节点为黑节点
    }
    //返回新树的根
    private Node add(Node node, K key, V value){

        if(node == null){
            size ++;
            return new Node(key, value);
        }

        if(key.compareTo(node.key) < 0)
            node.left = add(node.left, key, value);
        else if(key.compareTo(node.key) > 0)
            node.right = add(node.right, key, value);
        else  //key.comparableTo(node.key) == 0   即修改新的value
            node.value = value;

        //维护红黑树的性质
        if(isRed(node.right) && !isRed(node.left))
            node = leftRotate(node);

        if(isRed(node.left) && isRed(node.left.left))
            node = rightRotate(node);

        if(isRed(node.left) && isRed(node.right))
            flipColors(node);

        return node;
    }

    //返回以node为根节点的二分搜索树中，key所在的结点
    private Node getNode(Node node, K key){

        if(node == null)
            return null;

        if(key.compareTo(node.key) == 0)
            return node;
        else if(key.compareTo(node.key) < 0)
            return getNode(node.left, key);
        else
            return getNode(node.right, key);
    }

    public boolean contains(K key) {
        return getNode(root, key) != null;
    }

    public V get(K key) {
        Node node = getNode(root, key);
        return node == null ? null : node.value;
    }

    public void set(K key, V newValue) {
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
    public Node removeMin(Node node){
        if(node.left == null) {
            Node rightNode = node.right;
            node.right = null;
            size --;
            return rightNode;
        }

        node.left = removeMin(node.left);
        return node;
    }

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

        if(key.compareTo(node.key) < 0){
            node.left = remove(node.left, key);
            return node;
        }
        else if(key.compareTo(node.key) > 0) {
            node.right = remove(node.right, key);
            return node;
        }
        else{
            //key.comparableTo(node.e) == 0

            //待删除结点左子树为空
            if(node.left == null){
                Node rightNode = node.right;
                node.right = null;
                size--;
                return rightNode;
            }

            //待删除结点右子树为空
            if(node.right == null){
                Node leftNode = node.left;
                node.left = null;
                size--;
                return leftNode;
            }

            //待删除结点左右都不为空
            // 找到比待删除结点大的最小结点，即待删除结点右子树的最小结点
            // 用这个结点代替删除的结点
            Node successor = minimum(node.right);
            //minimum返回以参数为根的树中最小的结点
            successor.right = removeMin(node.right);
            //找到待删除结点右子树中的最小结点并删除，返回右子树的根给successor
            successor.left = node.left;

            node.left = node.right = null;

            return successor;
        }
    }

}

