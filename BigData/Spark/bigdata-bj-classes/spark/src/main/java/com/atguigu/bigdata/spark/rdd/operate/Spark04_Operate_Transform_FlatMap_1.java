package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Spark04_Operate_Transform_FlatMap_1 {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");
        final JavaSparkContext jsc = new JavaSparkContext(conf);

        final JavaRDD<String> rdd = jsc.textFile("data/test.txt");

        // TODO map方法只负责转换数据(A -> B[B1, B2, B3])，不能将数据拆分后独立使用
        //      line     => hadoop hive flume
        //      string[] => [hadoop, hive, flume]
        //      flatMap方法可以将数据拆分后独立使用（A -> B1, B2, B3）
//        final JavaRDD<String[]> newRDD = rdd.map(
//                line -> line.split(" ")
//        );
        final JavaRDD<String> newRDD = rdd.flatMap(
                line -> Arrays.asList(line.split(" ")).iterator()
        );

        newRDD.collect().forEach(System.out::println);


        jsc.close();

    }
}
