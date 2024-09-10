package com.atguigu.bigdata.spark.rdd.instance;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public class Spark03_RDD_Disk {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");
        final JavaSparkContext jsc = new JavaSparkContext(conf);

        // TODO 构建RDD数据处理模型
        //      利用环境对象对接磁盘数据（文件），构建RDD对象

        // textFile方法可以传递一个参数：文件路径
        final JavaRDD<String> rdd = jsc.textFile("D:\\idea\\classes\\bigdata-bj-classes231226\\data\\test.txt");

        final List<String> collect = rdd.collect();
        collect.forEach(System.out::println);

        jsc.close();

    }
}
