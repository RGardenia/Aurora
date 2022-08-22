package com.zy.AVLTree;

import com.zy.SetAndMap.Set;

public class AVLSet<E extends Comparable<E>> implements Set<E> {

    private AVLTree<E, Object> avl;

    public AVLSet(){
        avl = new AVLTree<E, Object>();
    }

    @Override
    public void add(E e) {
        avl.add(e, null);
    }

    @Override
    public void remove(E e) {
        avl.remove(e);
    }

    @Override
    public int getSize() {
        return avl.getSize();
    }

    @Override
    public boolean contains(E e) {
        return avl.contains(e);
    }

    @Override
    public boolean isEmpty() {
        return avl.isEmpty();
    }
}
