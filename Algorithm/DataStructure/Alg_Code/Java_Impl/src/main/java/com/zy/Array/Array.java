package com.zy.Array;

public class Array<E> {   //本质是类型

    private E[] data;
    private int size;     //本质是数据

    /**
     *
     * @param capacity
     */
    public Array(int capacity){
        data = (E[])new Object[capacity];
        //object是所有类的父类，创建后，强制类型转换
        size = 0;
    }

    public Array(){
        this(10);
    }

    public Array(E[] arr){
        data = (E[])new Object[arr.length];
        for(int i = 0; i < arr.length; i++)
            data[i] = arr[i];
        size = arr.length;
    }

    public int getSize(){
        return size;
    }

    public int getCapacity(){
        return data.length;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public static int sum(int[] nums){
        int sum = 0;
        for(int num: nums) {
            sum += num;
        }
        return sum;
    }

    public void insert(int index, E e){
        if(index < 0 || index > size) {
            throw new IllegalArgumentException("Insert failure!");
        }

        if(size == data.length)                //扩大容量
            resize(2 * data.length);

        for(int i = size - 1; i >= index; i--){
            data[i + 1] = data[i];
        }
        data[index] = e;
        size ++;
    }

    public void addLast(E e){
        insert(size, e);
    }

    public void addFirst(E e){
        insert(0, e);
    }

    //获取index索引位置的元素
    //客户无法查询无元素的索引
    public E get(int index){
        if(index < 0 || index >= size)
            throw new IllegalArgumentException("index failure!");

        return data[index];
    }

    public E getLast(){
        return get(size-1);
    }

    public E getFirst(){
        return get(0);
    }

    public void set(int index, E e){   //修改操作
        if(index < 0 || index >= size)
            throw new IllegalArgumentException("index failure!");

        data[index] = e;
    }

    public boolean contains(E e){   //是否包含e元素
        for(int i = 0 ; i < size ; i++){
            if(data[i].equals(e))
                return true;
        }
        return false;
    }

    //不合法的索引返回-1
    public int find(E e){
        for(int i = 0; i < size; i++){
            if(data[i].equals(e)) return i;
        }
        return -1;
    }

    //删除元素
    public E remove(int index){
        
        if(index < 0 || index >= size)
            throw new IllegalArgumentException("index failure!");

        E ret = data[index];
        for(int i = index + 1; i < size; i++){
            data[i-1] = data[i];
        }
        size --;
        data[size] = null;           //释放size索引的空间

        //lazy方法，到容积的四分之一时在减半
        if(size == data.length / 4 && data.length / 2 != 0)
            resize(data.length / 2);

        return ret;
    }

    public E removeFirst(){
        return remove(0);
    }

    public E removeLast(){
        return remove(size-1);
    }

    //从数组中删除元素 e
    public void removeElement(E e){
        int index = find(e);
        if(index != -1)
            remove(index);
    }

    //交换位置
    public void swap(int i, int j){

        if(i < 0 || i >= size || j < 0 || j >= size)
            throw new IllegalArgumentException("Index is illegal!");

        E t = data[i];
        data[i] = data[j];
        data[j] = t;
    }

    @Override
    public String toString(){
        StringBuilder res = new StringBuilder();
        res.append(String.format("Array: size = %d, capacity = %d %n" , size, data.length));
        res.append('[');
        for(int i = 0; i < size; i++){
            res.append(data[i]);
            if(i != size - 1)
                res.append(",");
        }
        res.append(']');
        return res.toString();
    }

    private void resize(int newCapacity){
        E[] newData = (E[])new Object[newCapacity];
        for(int i = 0; i < size; i++)
            newData[i] = data[i];
        data = newData;                      //java自动回收
    }
}
