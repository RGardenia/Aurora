package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class Spark08_Operate_Transform_KV_1 {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);

        final List<Integer> nums = Arrays.asList(1, 2, 3, 4);

        final JavaRDD<Integer> rdd = jsc.parallelize(nums);
        // TODO 单值类型数据可以和KV类型进行转换
        rdd.mapToPair(
            num -> new Tuple2<Integer, Integer>(num, num * 2)
        )
        .mapValues(
            num -> num * 2
        )
        .collect().forEach(System.out::println);

        jsc.close();

    }
}
