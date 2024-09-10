package com.atguigu.bigdata.sparksql;

import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.expressions.Aggregator;

import java.io.Serializable;

// TODO 自定义UDAF函数，实现年龄的平均值
//      1. 创建自定义的【公共】类
//      2. 继承 org.apache.spark.sql.expressions.Aggregator
//      3. 设定泛型
//          IN : 输入数据类型
//          BUFF : 缓冲区的数据类型
//          OUT : 输出数据类型
//     4. 重写方法 （ 4（计算） + 2(状态)）
public class MyAvgAgeUDAF extends Aggregator<Long, AvgAgeBuffer, Long> {
    @Override
    // TODO 缓冲区的初始化操作
    public AvgAgeBuffer zero() {
        return new AvgAgeBuffer(0L, 0L);
    }

    @Override
    // TODO 将输入的年龄和缓冲区的数据进行聚合操作
    public AvgAgeBuffer reduce(AvgAgeBuffer buffer, Long in) {
        buffer.setTotal(buffer.getTotal() + in);
        buffer.setCnt(buffer.getCnt() + 1);
        return buffer;
    }

    @Override
    // TODO 合并缓冲区的数据
    public AvgAgeBuffer merge(AvgAgeBuffer b1, AvgAgeBuffer b2) {
        b1.setTotal(b1.getTotal() + b2.getTotal());
        b1.setCnt(b1.getCnt() + b2.getCnt());
        return b1;
    }

    @Override
    // TODO 计算最终结果
    public Long finish(AvgAgeBuffer buffer) {
        return buffer.getTotal() / buffer.getCnt();
    }

    @Override
    public Encoder<AvgAgeBuffer> bufferEncoder() {
        return Encoders.bean(AvgAgeBuffer.class);
    }

    @Override
    public Encoder<Long> outputEncoder() {
        return Encoders.LONG();
    }
}