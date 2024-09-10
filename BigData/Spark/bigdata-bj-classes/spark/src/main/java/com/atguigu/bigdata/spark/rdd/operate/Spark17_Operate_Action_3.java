package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class Spark17_Operate_Action_3 {
    public static void main(String[] args) throws Exception {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);


        final List<Integer> nums = Arrays.asList(1,2,3,4);
        final JavaRDD<Integer> rdd = jsc.parallelize(nums, 1);

        //rdd.foreach(System.out::println);
        // 单点循环：1, 2, 3, 4
        //rdd.collect().forEach(System.out::println);
        //System.out.println("******************************");
        // 分布式循环
        // TODO foreach执行效率低，但是占内存比较小。
        rdd.foreach(
                num -> System.out.println(num)
        );
        System.out.println("**************************************");
        // TODO foreachPartition执行效率高，但是依托于内存大小。
        rdd.foreachPartition(
                list -> {
                    System.out.println(list);
                }
        );

        rdd.reduce(
            (num1, num2) -> {
                return num1 + num2;
            }
        );

        jsc.close();

    }
}
