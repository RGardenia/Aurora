package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public class Spark16_Operate_Action_collect_1 {
    public static void main(String[] args) throws Exception {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);


        final List<Integer> nums = Arrays.asList(4, 2, 3, 1);
        final JavaRDD<Integer> rdd = jsc.parallelize(nums, 2);
        //jsc.textFile("hdfs://xxxxx");
        // Spark的计算全部都是在Executor端执行

        // TODO
        //    Spark在编写代码时，调用转换算子，并不会真正执行，因为只是在Driver端组合功能
        //    所以当前的代码其实就是在Driver端执行
        //    所以当前main方法也称之为driver方法，当前运行main线程，也称之Driver线程。
        //    转换算子中的逻辑代码是在Executor端执行的。并不会在Driver端调用和执行。
        //    RDD封装的逻辑其实就是转换算子中的逻辑
        final JavaRDD<Object> newRDD = rdd.map(num -> {
            System.out.println("******");
            return num * 2;
        });

        // TODO collect方法就是行动算子，会触发Job的执行
        //      collect方法就是将Executor端执行的结果按照分区的顺序拉取（采集）回到Driver端, 将结果组合成集合对象
        //      collect方法可能会导致多个Executor的大量数据拉取到Driver端，导致内存溢出，所以生成环境慎用
        final List<Object> collect = newRDD.collect();
        collect.forEach(System.out::println);


        jsc.close();

    }
}
