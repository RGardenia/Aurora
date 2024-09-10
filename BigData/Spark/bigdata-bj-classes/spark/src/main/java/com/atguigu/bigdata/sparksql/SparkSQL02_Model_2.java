package com.atguigu.bigdata.sparksql;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkSQL02_Model_2 {
    public static void main(String[] args) {

        final SparkSession sparkSession = SparkSession
                .builder()
                .master("local[*]")
                .appName("SparkSQL")
                .getOrCreate();
        final Dataset<Row> ds = sparkSession.read().json("data/user.json");

        // TODO 模型对象的访问
        //      将数据模型转换为二维的结构（行，列），可以通过SQL文进行访问
        //      视图：是表的查询结果集。表可以增加，修改，删除，查询。
        //           视图不能增加，不能修改，不能删除，只能查询
        ds.createOrReplaceTempView("user");

        // TODO 当前JDK版本不适合开发SQL
        /*
        "
           select
                *
            from a
            join b on a.id = b.id
            union
            select
                *
           from c
        "

         */
        sparkSession.sql("select * from user").show();






        // TODO 释放资源
        sparkSession.close();

    }
}
