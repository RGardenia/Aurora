package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.Arrays;
import java.util.List;

public class Spark02_Operate_Transform_Map_4 {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);


        final List<Integer> nums = Arrays.asList(1, 2, 3, 4);

        // TODO RDD的方法
        // 【1，2】【3，4】
        final JavaRDD<Integer> rdd = jsc.parallelize(nums, 2);


        // TODO map方法的作用就是将传入的A转换为B返回，但是没有限制A和B的关系。
        final JavaRDD<Integer> newRDD = rdd.map(num -> num * 2);

        rdd.saveAsTextFile("output1");
        newRDD.saveAsTextFile("output2");




        jsc.close();

    }
}
