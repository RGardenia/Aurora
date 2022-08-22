package com.zy.SetAndMap;

public class LinkedListMap<K, V> implements Map<K, V>{

    public class Node{
        public K key;
        public V value;
        public Node next;

        public Node(K key, V value, Node next){
            this.key = key;
            this.next = next;
            this.value = value;
        }
        public Node(K key){ this(key,null,null);}

        public Node(){ this(null,null,null);}

        @Override
        public String toString(){ return key.toString() + ": " + value.toString(); }
    }

    private Node dummyHead;
    private int size;

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public V get(K key) {
        Node node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean contains(K key) {
        return getNode(key) != null;
    }

    //找到key所对应的结点并返回
    private Node getNode(K key){

        Node cur = dummyHead.next;
        while(cur != null){
            if(cur.key.equals(key))
                return cur;
            cur = cur.next;
        }
        return null;
    }

    @Override
    public void add(K key, V value) {

        Node node = getNode(key);
        //看是否有key的键
        if(node == null){
            dummyHead.next = new Node(key, value, dummyHead.next);
            size ++;
        }
        else
            node.value = value;
    }

    //重置键key的值
    @Override
    public void set(K key, V newValue) {

        Node node = getNode(key);
        if(node == null)
            throw new IllegalArgumentException(key + " does't exist!");

        node.value = newValue;
    }

    @Override
    public V remove(K key) {
        //找到要删除的结点
        Node prev = dummyHead;
        while(prev.next != null){
            if(prev.next.key.equals(key))
                break;
            prev = prev.next;
        }

        if(prev.next != null){
            Node delNode = prev.next;
            prev.next = delNode.next;
            prev.next = null;
            size --;
            return delNode.value;
        }

        return null;
    }
}
