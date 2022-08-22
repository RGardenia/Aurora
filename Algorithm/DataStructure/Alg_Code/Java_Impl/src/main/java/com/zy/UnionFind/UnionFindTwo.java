package com.zy.UnionFind;

public class UnionFindTwo implements UF{

    private int[] parent;

    public UnionFindTwo(int size){

        parent = new int[size];

        for(int i = 0; i < size; i++)
            parent[i] = i;
    }

    @Override
    public int getSize() {
        return parent.length;
    }

    //查找元素p的根节点
    //时间复杂度为O（h），h为树的高度
    private int find(int p){
        if(p < 0 || p >= parent.length)
            throw new IllegalArgumentException("p is out of boundary!");

        while(p != parent[p])
            p = parent[p];

        return p;
    }

    @Override
    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    @Override
    public void unionElements(int p, int q) {

        int pRoot = find(p);
        int qRoot = find(q);

        if(pRoot == qRoot)
            return;

        parent[pRoot] = qRoot;
    }
}
