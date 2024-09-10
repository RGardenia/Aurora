package com.atguigu.bigdata.spark.rdd.instance;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class Spark01_Env_1 {
    public static void main(String[] args) {

        // TODO 构建Spark的运行环境
        final JavaSparkContext jsc = new JavaSparkContext("local", "spark");


        // TODO 释放资源
        jsc.close();

    }
}
