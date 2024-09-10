package com.atguigu.bigdata.sparksql;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF1;

import static org.apache.spark.sql.types.DataTypes.StringType;

public class SparkSQL04_Source_CSV {
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

        // TODO CSV文件就是将数据采用逗号分隔的数据文件
        final Dataset<Row> csv = sparkSession.read()
                .option("header", "true") // 配置
                .option("sep","_") // 配置：\t => tsv, csv
                .csv("data/user.csv");
        csv.show();

        // select avg(_c2) from user


        // TODO 释放资源
        sparkSession.close();

    }
}
