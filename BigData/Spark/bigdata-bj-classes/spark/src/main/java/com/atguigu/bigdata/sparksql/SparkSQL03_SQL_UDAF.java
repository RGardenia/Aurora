package com.atguigu.bigdata.sparksql;

import org.apache.spark.sql.*;
import static org.apache.spark.sql.functions.*;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.expressions.Aggregator;

import java.io.Serializable;

import static org.apache.spark.sql.types.DataTypes.StringType;

public class SparkSQL03_SQL_UDAF {
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


        final Dataset<Row> ds = sparkSession.read().json("data/user.json");
        ds.createOrReplaceTempView("user");

        // TODO SparkSQL采用特殊的方式将UDAF转换成UDF使用
        //      UDAF使用时需要创建自定义聚合对象
        //        udaf方法需要传递2个参数
        //             第一个参数表示UDAF对象
        //             第二个参数表示UDAF对象
        sparkSession.udf().register("avgAge", udaf(
                new MyAvgAgeUDAF(), Encoders.LONG()
        ));

        String sql = "select avgAge(age) from user";
        final Dataset<Row> sqlDS = sparkSession.sql(sql);
        sqlDS.show();


        // TODO 释放资源
        sparkSession.close();

    }
}
