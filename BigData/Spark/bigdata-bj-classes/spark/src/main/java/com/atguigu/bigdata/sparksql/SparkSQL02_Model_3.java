package com.atguigu.bigdata.sparksql;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkSQL02_Model_3 {
    public static void main(String[] args) {

        final SparkSession sparkSession = SparkSession
                .builder()
                .master("local[*]")
                .appName("SparkSQL")
                .getOrCreate();
        final Dataset<Row> ds = sparkSession.read().json("data/user.json");


        // TODO 采用DSL语法进行访问
        //      select * from user
        ds.select("*").show();






        // TODO 释放资源
        sparkSession.close();

    }
}
