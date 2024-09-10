package com.atguigu.bigdata.sparksql;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;

public class SparkSQL01_Env_3 {
    public static void main(String[] args) {

        // TODO 构建环境对象
        //      Spark在结构化数据的处理场景中对核心功能，环境进行了封装
        //      构建SparkSQL的环境对象时，一般采用构建器模式
        //      构建器模式： 构建对象
        SparkConf conf = new SparkConf().setAppName("sparksql").setMaster("local[*]");

        final SparkSession sparkSession = SparkSession
                .builder()
                .config(conf)
                .getOrCreate();

        // TODO 环境之间的转换
        //      Core : SparkContext -> SQL : SparkSession
        //new SparkSession( new SparkContext(conf) );
        // TODO SQL  : SparkSession -> Core : SparkContext
        //final SparkContext sparkContext = sparkSession.sparkContext();
        //sparkContext.parallelize()
        // TODO SQL  : SparkSession -> Core : JavaSparkContext
        final SparkContext sparkContext = sparkSession.sparkContext();
        final JavaSparkContext jsc = new JavaSparkContext(sparkContext);
        jsc.parallelize(Arrays.asList(1,2,3,4));

        // TODO 释放资源
        sparkSession.close();

    }
}
