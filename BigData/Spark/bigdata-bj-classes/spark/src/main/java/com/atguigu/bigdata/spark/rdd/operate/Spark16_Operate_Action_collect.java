package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public class Spark16_Operate_Action_collect {
    public static void main(String[] args) throws Exception {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);


        final List<Integer> nums = Arrays.asList(4, 2, 3, 1);
        final JavaRDD<Integer> rdd = jsc.parallelize(nums, 2);
        final JavaRDD<Object> newRDD = rdd.map(num -> {
            System.out.println("******");
            return num * 2;
        });

        // TODO collect方法就是行动算子，会触发Job的执行
        //      collect方法就是将Executor端执行的结果按照分区的顺序拉取（采集）回到Driver端, 将结果组合成集合对象
        final List<Object> collect = newRDD.collect();
        collect.forEach(System.out::println);


        jsc.close();

    }
}
