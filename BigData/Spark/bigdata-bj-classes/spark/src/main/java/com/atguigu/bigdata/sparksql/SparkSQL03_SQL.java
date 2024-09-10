package com.atguigu.bigdata.sparksql;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkSQL03_SQL {
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

        // TODO Spark SQL中对数据模型也进行了封装 ： RDD -> Dataset
        //      对接文件数据源时，会将文件中的一行数据封装为Row对象
        final Dataset<Row> ds = sparkSession.read().json("data/user.json");
        //final RDD<Row> rdd = ds.rdd();

        // TODO 将数据模型转换成表，方便SQL的使用
        ds.createOrReplaceTempView("user");


        // TODO 使用SQL文的方式操作数据
        String sql = "select avg(age) from user";
        final Dataset<Row> sqlDS = sparkSession.sql(sql);

        // TODO 展示数据模型的效果
        sqlDS.show();


        // TODO 释放资源
        sparkSession.close();

    }
}
