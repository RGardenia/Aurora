package com.zy.UnionFind;

public class UnionFindOne implements UF{

    private int[] id;

    public UnionFindOne(int size){

        id = new int[size];

        //编号  令编号都不一样
        for(int i = 0; i < id.length; i++)
            id[i] = i;
    }

    @Override
    public int getSize() {
        return id.length;
    }

    //查找元素p所对应的集合编号
    private int find(int p){
        if(p < 0 || p >= id.length)
            throw new IllegalArgumentException("p is out of boundary!");

        return id[p];
    }

    @Override
    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    //合并元素p和元素q所属的集合
    @Override
    public void unionElements(int p, int q) {

        int pID = find(p);
        int qID = find(q);

        if(pID == qID)
            return;

        // 依次令所有元素的id 值都改写
        for(int i = 0; i < id.length; i ++)
            if(id[i] == pID)
                id[i] = qID;
    }
}
