package com.zy.UnionFind;

public class UnionFindRank implements UF{

    private int[] parent;
    private int[] rank;    //rank[i]表示以 i 为根的集合所表示的数的层数

    public UnionFindRank(int size){

        parent = new int[size];
        rank = new int[size];

        for(int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 1;    //层数为一
        }
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

        if(rank[pRoot] < rank[qRoot])
            // 令pRoot 指向 qRoot
            parent[pRoot] = qRoot;
            //层数小的数作为层数大的数的子树
        else if(rank[qRoot] < rank[pRoot])
            parent[qRoot] = pRoot;
        else{
            parent[qRoot] = pRoot;
            rank[pRoot] += 1;
        }
    }
}
