package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple1;
import scala.Tuple2;
import scala.Tuple22;

import java.util.Arrays;

public class Spark01_Operate {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);

        // TODO RDD的方法
        final JavaRDD<Integer> rdd = jsc.parallelize(
                Arrays.asList(1, 2, 3)
        );

        // RDD的方法会有很多，主要讲解核心，重要的方法
        // 学习的重点：
        //    1. 名字
        //    2. IN
        //    3. OUT

        // RDD的方法会有很多，但是分为2类
        // 1. 转换：将数据向后流转
        // 2. 行动：打开数据开关

        // RDD方法处理数据的分类
        // 1. 单值 : 1, "abc", new User(), new ArrayList(), (Key, Value)
        // 2. 键值 : KV => (Key, Value)
        //          word -> count
//        int i = 10;
//        String s = "abc";
//        Object o = new Object();

        // TODO JDK1.8以后也存在元组，采用特殊的类 : TupleX
        final Tuple1<String> abc = new Tuple1<>("abc");
        final Tuple2<String, String> abc1 = new Tuple2<>("a", "1");
        System.out.println(abc1._1);
        System.out.println(abc1._1());
        System.out.println(abc1._2);
        System.out.println(abc1._2());
        // 马丁的幸运数字是22
        // 元组的最大数据容量为22
        //new Tuple22<>();


        jsc.close();

    }
}
