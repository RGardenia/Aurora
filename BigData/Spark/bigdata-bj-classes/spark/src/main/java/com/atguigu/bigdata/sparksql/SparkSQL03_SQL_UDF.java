package com.atguigu.bigdata.sparksql;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF1;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StringType$;

import static org.apache.spark.sql.types.DataTypes.StringType;

public class SparkSQL03_SQL_UDF {
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

        // prefix + name
        //  Name: + zhangsan
        //  Name: + lisi
        //  Name: + wangwu
        //String sql = "select 'Name:' + name from user";
        //String sql = "select concat('Name:',name) from user";
        //String sql = "select 'Name:'||name from user";
        // mysql, oracle(||), db2, sqlserver
        // Shark => Spark On Hive => SparkSQL => Spark parse SQL
        //       => Hive On Spark => 数据仓库   => Hive parse SQL


        // TODO SparkSQL提供了一种特殊的方式，可以在SQL中增加自定义方法来实现复杂的逻辑

        // TODO 如果想要自定义的方法能够在SQL中使用，那么必须在SPark中进行声明和注册
        //      register方法需要传递3个参数
        //           第一个参数表示SQL中使用的方法名
        //           第二个参数表示逻辑 : IN => OUT
        //           第三个参数表示返回的数据类型 : DataType类型数据，需要使用scala语法操作，需要特殊的使用方式。
        //                 scala Object => Java
        //                 StringType$.MODULE$ => case object StringType
        //                 DataTypes.StringType
        sparkSession.udf().register("prefixName", new UDF1<String, String>() {
            @Override
            public String call(String name) throws Exception {
                return "Name:" + name;
            }
        }, StringType);

        String sql = "select prefixName(name) from user";
        final Dataset<Row> sqlDS = sparkSession.sql(sql);
        sqlDS.show();


        // TODO 释放资源
        sparkSession.close();

    }
}
