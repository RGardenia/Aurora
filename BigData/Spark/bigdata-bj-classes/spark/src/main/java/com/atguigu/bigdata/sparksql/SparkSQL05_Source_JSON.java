package com.atguigu.bigdata.sparksql;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkSQL05_Source_JSON {
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

        // org.apache.spark.sql.AnalysisException:
       //            Since Spark 2.3, the queries from raw JSON/CSV files are disallowed when the
       //            referenced columns only include the internal corrupt record column
       //                    (named _corrupt_record by default)

        // JSON : JavaScript Object Notation
        //        对象 ：{}
        //        数组 ：[]
        //        JSON文件：整个文件的数据格式符合JSON格式，不是一行数据符合JSON格式
        // TODO SparkSQL其实就是对Spark Core RDD的封装。RDD读取文件采用的是Hadoop，hadoop是按行读取。
        //      SparkSQL只需要保证JSON文件中一行数据符合JSON格式即可，无需整个文件符合JSON格式
        final Dataset<Row> json = sparkSession.read().json("data/user.json");

        json.write().json("output");
        json.show();


        // TODO 释放资源
        sparkSession.close();

    }
}
