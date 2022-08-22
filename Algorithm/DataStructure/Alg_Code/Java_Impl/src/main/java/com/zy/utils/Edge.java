package com.zy.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 张雨 - 栀
 * @Since 2022/3/27 13:56
 *
 * 图 的 边
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Edge {
    public int weight;
    public Node from;
    public Node to;
}
