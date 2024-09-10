package com.atguigu.bigdata.sparksql;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

public class SparkSQL01_Env {
    public static void main(String[] args) {

        // TODO 构建环境对象
        //      Spark在结构化数据的处理场景中对核心功能，环境进行了封装
        //new JavaSparkContext()
        SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("SparkSQL");
        SparkContext sc = new SparkContext(conf);

//        final JavaSparkContext jsc = new JavaSparkContext(conf);

        final SparkSession sparkSession = new SparkSession(sc);

        // TODO 释放资源
        sparkSession.close();

    }
}
