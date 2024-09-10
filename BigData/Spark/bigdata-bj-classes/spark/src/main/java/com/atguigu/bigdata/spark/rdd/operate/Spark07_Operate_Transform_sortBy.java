package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public class Spark07_Operate_Transform_sortBy {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);
        final List<Integer> nums = Arrays.asList(1,33,3,2,4,11);
        final JavaRDD<Integer> rdd = jsc.parallelize(nums, 2);

        // TODO sortBy方法：按照指定的排序规则对数据进行排序
        //      sortBy方法可以传递三个参数
        //           第一个参数表示排序规则:
        //                Spark会为每一个数据增加一个标记，然后按照标记对数据进行排序
        //           第二个参数表示排序的方式：升序（true），降序(false)
        //           第三个参数表示分区数量
        /*

          1, 33, 3, 2, 4, 11
          ------------------
          标记：1, 33, 3, 2, 4, 11
          =>
          标记：1，2，3，4，11，33
          ---------------------
          数据：1，2，3，4，11，33
          -----------------------------------------------------
              1,   33,   3,   2,   4,    11
          ------------------
         标记："1", "33", "3", "2", "4", "11"
         -----------------------------
         标记："1", "11", "2", "3", "33", "44"
         -----------------------------
               1,  11,  2,    3,   33, 44

         */
        //rdd.saveAsTextFile("output1");
        rdd
            .sortBy(
                   num -> "" + num, true, 2
            )
            .collect().forEach(System.out::println);
            //.saveAsTextFile("output2");


        jsc.close();

    }
}
