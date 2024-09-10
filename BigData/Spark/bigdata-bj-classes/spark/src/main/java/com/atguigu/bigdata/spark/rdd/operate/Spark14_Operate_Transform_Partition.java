package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.Arrays;
import java.util.List;

public class Spark14_Operate_Transform_Partition {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);


        final List<Integer> nums = Arrays.asList(1, 3, 5, 2, 4, 6);
        // 【1，3，5】【2，4，6】
        final JavaRDD<Integer> rdd = jsc.parallelize(nums, 2);
        // 【】【2，4，6】
        final JavaRDD<Integer> filterRDD = rdd.filter(num -> num % 2 == 0);

        // TODO 改变分区。
        //      合并分区：缩减分区
        //      coalesce方法默认没有shuffle功能，所以数据不会被打乱重新组合，所以如果要扩大分区是无法实现的。
        //      coalesce方法可以设定第二个shuffle参数，如果设定shuffle功能为true，那么可以扩大分区。
//        final JavaRDD<Integer> coalesceRDD = filterRDD.coalesce(3, true);
//        coalesceRDD.saveAsTextFile("output");

        // TODO 重分区。
        //      repartition方法其实就是设定shuffle为true的coalesce方法
        filterRDD.repartition(3).saveAsTextFile("output");

        // 缩减分区：coalesce
        // 扩大分区：repartition


        jsc.close();

    }
}
