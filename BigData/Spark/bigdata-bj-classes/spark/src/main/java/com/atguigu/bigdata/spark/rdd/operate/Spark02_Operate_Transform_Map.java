package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import scala.Tuple1;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class Spark02_Operate_Transform_Map {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);


        final List<Integer> nums = Arrays.asList(1, 2, 3, 4);

        // TODO RDD的方法
        // 【1，2】【3，4】
        final JavaRDD<Integer> rdd = jsc.parallelize(nums, 2);

        // TODO RDD的转换方法：map
        //      对单值数据进行处理

        // map : 映射 （K -> V）, 将A(K)转换为B(V)
        //       将指定对值转换为其他的值的场合
        //       [1,2,3,4] => [2,4,6,8]
        //       学习方法重点：1. 名字，2. IN, 3. OUT
        final JavaRDD<Object> newRDD = rdd.map(new Function<Integer, Object>() {
            @Override
            public Object call(Integer in) throws Exception {
                return in * 2;
            }
        });

        newRDD.collect().forEach(System.out::println);
        System.out.println("*************************************");
        System.out.println(nums);


        jsc.close();

    }
}
