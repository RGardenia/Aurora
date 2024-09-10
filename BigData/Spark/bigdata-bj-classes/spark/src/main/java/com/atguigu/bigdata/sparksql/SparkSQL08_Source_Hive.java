package com.atguigu.bigdata.sparksql;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

public class SparkSQL08_Source_Hive {
    public static void main(String[] args) {

        // TODO 在编码前，设定Hadoop的访问用户
        System.setProperty("HADOOP_USER_NAME","atguigu");

        final SparkSession sparkSession = SparkSession
                .builder()
                .enableHiveSupport() // TODO 启用Hive的支持
                .master("local[*]")
                .appName("SparkSQL")
                .getOrCreate();

        sparkSession.sql("show tables").show();

        sparkSession.sql("create table user_info(name String,age bigint)");
        sparkSession.sql("insert into table user_info values('zhangsan',10)");
        sparkSession.sql("select * from user_info").show();

        // TODO 释放资源
        sparkSession.close();

    }
}
