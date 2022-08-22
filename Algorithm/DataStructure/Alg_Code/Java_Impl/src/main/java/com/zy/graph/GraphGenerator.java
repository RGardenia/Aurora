package com.zy.graph;

import com.zy.utils.Edge;
import com.zy.utils.Node;

import java.util.*;

/**
 * @author 张雨 - 栀
 * @Since 2022/3/27 13:57
 *
 * 图
 *
 * 遍历  ： https://cdn.jsdelivr.net/gh/Rainbow503/PicGo/img/QQ图片20220329095107.png
 */
public class GraphGenerator {

    /**
     * 建 图
     * @param matrix 节点表， 即邻接表
     * @return 返回入口节点
     */
    public static Graph createGraph(Integer[][] matrix){
        Graph graph = new Graph();
        for(int i = 0; i < matrix.length; i ++){
            Integer from = matrix[i][0];
            Integer to = matrix[i][1];
            Integer weight = matrix[i][2];
            // 加 节点
            if(!graph.nodes.containsKey(from)){
                graph.nodes.put(from, new Node(from));
            }
            if(!graph.nodes.containsKey(to)){
                graph.nodes.put(to, new Node(to));
            }
            // 建 出入度
            Node fromNode = graph.nodes.get(from);
            Node toNode = graph.nodes.get(to);
            Edge newEdge = new Edge(weight, fromNode, toNode);
            // 连接 图
            fromNode.nexts.add(toNode);
            fromNode.out++;
            toNode.in++;
            fromNode.edges.add(newEdge);
            graph.edges.add(newEdge);
        }
        return graph;
    }

    //region 图的 宽度优先遍历
    /**
     * 宽度优先遍历   必须是连通图
     * 可遍历 有向图与无向图
     * @param node 入口
     */
    public static void bfs(Node node){
        if(node == null){ return; }
        Queue<Node> queue = new LinkedList<>();
        HashSet<Node> set = new HashSet<>();
        queue.add(node);
        set.add(node);
        while(!queue.isEmpty()){
            Node cur = queue.poll();
            System.out.println(cur.val);
            for(Node next : cur.nexts){
                if(!set.contains(next)){
                    set.add(next);
                    queue.add(next);
                }
            }
        }
    }
    //#endregion

    //region 图的 深度优先遍历
    /**
     *
     * @param node 入口
     */
    public static void dfs(Node node){
        if(node == null){ return; }
        Stack<Node> stack = new Stack<>();
        HashSet<Node> set = new HashSet<>();
        stack.add(node);
        set.add(node);
        System.out.println(node.val);
        while(!stack.isEmpty()){
            Node cur = stack.pop();
            for(Node next : cur.nexts){
                // 不在 set 中，添加进 stack 与 set 中，直到走到低
                if(!set.contains(next)){
                    stack.push(node);
                    // 后添加 next ，之后要 pop
                    stack.push(next);
                    set.add(next);
                    System.out.println(next.val);
                    break;
                }
            }
        }
    }
    //#endregion

    //region 图 拓扑排序
    /**
     *
     * @param graph 图
     */
    public static List<Node> sortedTopology(Graph graph){
        HashMap<Node, Integer> inMap = new HashMap<>();
        Queue<Node> zeroQueue = new LinkedList<>();
        for(Node node : graph.nodes.values()){
            inMap.put(node, node.in);
            if(node.in == 0){
                zeroQueue.add(node);
            }
        }
        List<Node> result = new ArrayList<>();
        while (!zeroQueue.isEmpty()) {
            Node cur = zeroQueue.poll();
            result.add(cur);
            for(Node next : cur.nexts){
                inMap.put(next, inMap.get(next) - 1);
                if(inMap.get(next) == 0){
                    zeroQueue.add(next);
                }
            }
        }
        return result;
    }
    //#endregion
}
