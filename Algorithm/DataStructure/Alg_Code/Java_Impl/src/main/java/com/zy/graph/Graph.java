package com.zy.graph;

import com.zy.utils.Edge;
import com.zy.utils.Node;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author 张雨 - 栀
 * @Since 2022/3/27 13:57
 *
 * 图
 */
public class Graph {
    public HashMap<Integer, Node> nodes;
    public HashSet<Edge> edges;

    public Graph(){
        nodes = new HashMap<>();
        edges = new HashSet<>();
    }
}
