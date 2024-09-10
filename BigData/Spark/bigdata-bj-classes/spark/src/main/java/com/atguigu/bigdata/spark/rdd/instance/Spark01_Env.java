package com.atguigu.bigdata.spark.rdd.instance;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class Spark01_Env {
    public static void main(String[] args) {

        // TODO 构建Spark的运行环境

        // TODO 创建Spark配置对象
        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");

        //      SparkException : A master URL must be set in your configuration
        //      SparkException : An application name must be set in your configuration
        final JavaSparkContext jsc = new JavaSparkContext(conf);


        // TODO 释放资源
        jsc.close();

    }
}
