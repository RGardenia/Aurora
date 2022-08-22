package com.zy.Heap;

import com.zy.Array.Array;

public class MaxHeap<E extends Comparable<E>> {

    private Array<E> data;

    public MaxHeap(int capacity){
        data = new Array<E>(capacity);
    }

    public MaxHeap(){
        data = new Array<E>();
    }

    //堆化构造函数
    public MaxHeap(E[] arr){
        data = new Array<>(arr);
        for(int i = parent(arr.length - 1) ; i >= 0; i--)
            siftDown(i);
    }

    public int size(){
        return data.getSize();
    }

    public boolean isEmpty(){
        return data.isEmpty();
    }

    //返回index结点的父亲结点的索引
    private int parent(int index){
        if(index == 0)
            throw new IllegalArgumentException("index-0 does't have parent!");
        return (index -1) / 2;
    }

    //返回完全二叉树的数组表示中，一个索引的元素的左孩子结点的索引
    private int leftChild(int index){
        return index * 2 + 1;
    }

    //返回完全二叉树的数组表示中，一个索引的元素的右孩子结点的索引
    private int rightChild(int index){
        return index * 2 + 2;
    }

    //
    public void add(E e){
        data.addLast(e);
        siftUp(data.getSize() - 1);
    }

    private void siftUp(int k ){

        while(k > 0 && data.get(parent(k)).compareTo(data.get(k)) < 0){
            data.swap(k, parent(k));
            k = parent(k);
            //循环直到满足堆的条件
        }
    }

    //返回堆中的最大元素
    public E findMax(){
        if(data.getSize() == 0)
            throw new IllegalArgumentException("Can not findMax when heap is empty!");
        return data.get(0);
    }

    //取出堆中最大的元素
    public E extractMax(){

        E ret = findMax();

        data.swap(0, data.getSize() - 1);
        data.removeLast();
        siftDown(0);

        return ret;
    }

    private void siftDown(int k){

        while(leftChild(k) < data.getSize()){     //左孩子的索引大于所有的元素数，结束

            int j = leftChild(k);
            if(j + 1 < data.getSize() && data.get(j + 1).compareTo(data.get(j)) > 0)
                //j + 1 表示k的右孩子， 左右孩子比较
                j = rightChild(k);
                //将右孩子给j，即可了解data[j] 是k的俩孩子里最大的

            if(data.get(k).compareTo(data.get(j)) >= 0)
                break;
            //若k的值比最大的还大，返回
            data.swap(k, j);
            k = j;
        }
    }

    //取出堆中的最大元素， 并且替换成元素e
    public E replace(E e){

        E ret = findMax();
        data.set(0, e);
        siftDown(0);
        return ret;
    }

    public void heapSort(E arr[]){
        MaxHeap<E> maxHeap = new MaxHeap<>(arr);

        for(int i = arr.length - 1; i >= 0; i --)
            arr[i] = maxHeap.extractMax();
    }
}
