package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public class Spark17_Operate_Action {
    public static void main(String[] args) throws Exception {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);


        final List<Integer> nums = Arrays.asList(4, 2, 3, 1);
        final JavaRDD<Integer> rdd = jsc.parallelize(nums, 2);

        // TODO collect用于采集数据
        final List<Integer> collect = rdd.collect();
        // TODO count获取结果数量
        final long count = rdd.count();
        // TODO count获取结果的第一个
        final long first = rdd.first();
        // TODO take从结果中获取前N个
        final List<Integer> take = rdd.take(3);

        System.out.println(collect); // [4,2,3,1]
        System.out.println(count); // 4
        System.out.println(first); // 4
        System.out.println(take); // [4,2,3]


        jsc.close();

    }
}
