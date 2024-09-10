package com.atguigu.bigdata.sparksql;

import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;

public class SparkSQL02_Model_1 {
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

        // [1,2,3,4]
        // {name=zs,age=30}

        // TODO Dataframe
//        ds.foreach(
//                row -> {
//                    System.out.println(row.getInt(2));
//                }
//        );

        // TODO 将数据模型中的数据类型进行转换，将Row转换成其他对象进行处理
        final Dataset<User> userDS = ds.as(Encoders.bean(User.class));

        userDS.foreach(
                user -> {
                    System.out.println(user.getName());
                }
        );


        // TODO 释放资源
        sparkSession.close();

    }
}
class User implements Serializable {
    private int id;
    private int age;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}