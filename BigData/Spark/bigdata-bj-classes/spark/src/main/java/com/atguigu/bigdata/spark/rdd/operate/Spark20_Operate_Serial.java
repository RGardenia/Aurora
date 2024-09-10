package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class Spark20_Operate_Serial {
    public static void main(String[] args) throws Exception {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);


        final List<Integer> nums = Arrays.asList(1,2,3,4);
        final JavaRDD<Integer> rdd = jsc.parallelize(nums, 2);

        rdd.foreach(
                num -> System.out.println(num)
        );
        // TODO JDK1.8的函数式编程其实采用的是对象模拟出来的。

//        final PrintStream out = System.out;
//        rdd.foreach( out::println );

        jsc.close();

    }
}
