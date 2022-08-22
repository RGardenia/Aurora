package com.zy.SetAndMap;

import com.zy.LinkedList.ChainList;

public class LinkedListSet<E> implements Set<E>{

    private ChainList<E> list;

    public LinkedListSet(){
        list = new ChainList<>();
    }

    @Override
    public int getSize() {
        return list.getSize();
    }

    @Override
    public boolean contains(E e) {
        return list.contains(e);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void add(E e) {
        //不添加重复的元素
        if(!list.contains(e))
            list.addFirst(e);
    }

    @Override
    public void remove(E e) {
        list.removeElement(e);
    }
}