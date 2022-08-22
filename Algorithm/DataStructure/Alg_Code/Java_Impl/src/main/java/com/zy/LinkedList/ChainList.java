package com.zy.LinkedList;

public class ChainList<E> {

    //结点的定义及初始化
    private class Node {
        public E e;
        public Node next;

        public Node(E e, Node next) {
            this.e = e;
            this.next = next;
        }

        public Node(E e) {
            this(e, null);
        }

        public Node() {
            this(null, null);
        }

        @Override
        public String toString() {
            return e.toString();
        }
    }

    private Node dummyHead;
    private int size;

    //初始化链表
    public ChainList() {
        dummyHead = new Node(null, null);
        size = 0;
    }

    //获取链表中的元素个数
    public int getSize() {
        return size;
    }

    //返回链表是否为空
    public boolean isEmpty() {
        return size == 0;
    }

    //在链表中添加元素(index)
    public void addMiddle(int index, E e) {

        if (index < 0 || index > size)
            throw new IllegalArgumentException("Add failed. Illegal index!");

        Node prev = dummyHead;
        for (int i = 0; i < index; i++)
            prev = prev.next;

        /*    Node node = new Node(e);
            node.next = prev.next;
            prev.next = node;*/
        prev.next = new Node(e, prev.next);

        size++;
    }

    //在链表头添加元素e
    public void addFirst(E e) {
        /*Node node = new Node(e);
        node.next = head;
        head = node;*/

        /*dummyHead = new Node(e, dummyHead);
        size ++;*/

        addMiddle(0, e);
    }

    //在链表末尾添加元素
    public void addLast(E e) {
        addMiddle(size, e);
    }

    //获得索引的元素
    public E get(int index) {

        if (index < 0 || index > size)
            throw new IllegalArgumentException("Get failed. Illegal index!");

        Node cur = dummyHead.next;
        for (int i = 0; i < index; i++)
            cur = cur.next;
        return cur.e;
    }

    //获得链表第一个元素
    public E getFirst() {
        return get(0);
    }

    //获得链表的最后一个元素
    public E getLast() {
        return get(size - 1);
    }

    //修改链表中的index个元素为e
    //index = 0 为第一个有效的元素
    public void set(int index, E e) {

        if (index < 0 || index > size)
            throw new IllegalArgumentException("Set failed. Illegal index!");

        Node cur = dummyHead.next;
        for (int i = 0; i < index; i++)
            cur = cur.next;
        cur.e = e;
    }

    //查找链表中是否有元素e
    public boolean contains(E e) {

        Node cur = dummyHead.next;
        while (cur != null) {
            if (cur.e.equals(e))
                return true;
            cur = cur.next;
        }
        return false;
    }

    //删除元素index位置的元素，返回删除位置的元素
    public E remove(int index) {

        if (index < 0 || index > size)
            throw new IllegalArgumentException("Remove failed. Illegal index!");

        Node prev = dummyHead;
        for (int i = 0; i < index; i++)
            prev = prev.next;

        Node retNode = prev.next;
        prev.next = retNode.next;
        retNode.next = null;

        size--;

        return retNode.e;
    }

    //从链表中删除第一个元素，返回删除的元素
    public E removeFirst() {
        return remove(0);
    }

    //从链表中删除最后一个元素，返回删除的元素
    public E removeLast() {
        return remove(size - 1);
    }

    //从链表中删除元素e
    public void removeElement(E e) {

        Node prev = dummyHead;
        //找到要删除结点的前一个结点
        while (prev.next != null) {
            if (prev.next.e.equals(e))
                break;
            prev = prev.next;
        }
        //删除结点
        if (prev.next != null) {
            Node delNode = prev.next;
            prev.next = delNode.next;
            delNode.next = null;
        }
    }

    @Override
    public String toString() {

        StringBuilder res = new StringBuilder();

        /*Node cur = dummyHead.next;

        while(cur != null){
            res.append(cur + "->");
            cur = cur.next;
        }*/

        for (Node cur = dummyHead.next; cur != null; cur = cur.next)
            res.append(cur + "->");

        res.append("null");
        return res.toString();
    }
}
