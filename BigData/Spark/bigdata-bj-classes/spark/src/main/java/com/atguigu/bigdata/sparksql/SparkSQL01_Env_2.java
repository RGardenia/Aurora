package com.atguigu.bigdata.sparksql;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

public class SparkSQL01_Env_2 {
    public static void main(String[] args) {

        // TODO 构建环境对象
        //      Spark在结构化数据的处理场景中对核心功能，环境进行了封装
        //      构建SparkSQL的环境对象时，一般采用构建器模式
        //      构建器模式： 构建对象
        SparkConf conf = new SparkConf().setAppName("sparksql").setMaster("local[*]");

        final SparkSession sparkSession = SparkSession
                .builder()
                .config(conf)
                .getOrCreate();

        // TODO 释放资源
        sparkSession.close();

    }
}