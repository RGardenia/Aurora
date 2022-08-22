package com.zy.SetAndMap;

public interface Set<E> {

    void add(E e);
    void remove(E e);
    int getSize();
    boolean contains(E e );
    boolean isEmpty();
}
