package com.zy.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

/**
 * @author 张雨 - 栀
 * @version 1.0
 * @Since 2022/3/25 22:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Node {
    public int val;
    //#region 适用 二叉树 、 链表
    public Node left;
    public Node right;
    public Node parent;
    //#endregion

    //#region 适用 图
    // 入度
    public int in;
    // 出度
    public int out;
    // 发散出的边
    public ArrayList<Node> nexts;
    // 被指向的边
    public ArrayList<Edge> edges;
    //#endregion

    // 构造
    public Node(int val) {
        this.val = val;
        in = 0;
        out = 0;
        nexts = new ArrayList<>();
        edges = new ArrayList<>();
    }
}
