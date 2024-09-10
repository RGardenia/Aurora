package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.Arrays;
import java.util.List;

public class Spark06_Operate_Transform_Distinct {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);
        final List<Integer> nums = Arrays.asList(1,1,1,1,2,2);
        final JavaRDD<Integer> rdd = jsc.parallelize(nums, 3);

        // TODO distinct : 去重
        //      hashSet去重，是单点去重
        //      distinct ，是分布式去重, 采用了分组+shuffle的处理方式
        rdd.distinct().collect().forEach(System.out::println);


        jsc.close();

    }
}
