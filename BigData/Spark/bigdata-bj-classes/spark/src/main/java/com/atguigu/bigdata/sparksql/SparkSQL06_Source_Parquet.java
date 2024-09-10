package com.atguigu.bigdata.sparksql;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkSQL06_Source_Parquet {
    public static void main(String[] args) {

        // TODO 构建环境对象
        //      Spark在结构化数据的处理场景中对核心功能，环境进行了封装
        //      构建SparkSQL的环境对象时，一般采用构建器模式
        //      构建器模式： 构建对象
        final SparkSession sparkSession = SparkSession
                .builder()
                .master("local[*]")
                .appName("SparkSQL")
                .getOrCreate();


        //final Dataset<Row> json = sparkSession.read().parquet("data/users.parquet");
        //json.show();
        final Dataset<Row> csv = sparkSession.read().json("data/user.json");

        csv.write().parquet("output");

        // TODO 释放资源
        sparkSession.close();

    }
}
