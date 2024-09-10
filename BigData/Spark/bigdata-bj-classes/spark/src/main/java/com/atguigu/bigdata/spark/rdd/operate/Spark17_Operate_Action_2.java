package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Spark17_Operate_Action_2 {
    public static void main(String[] args) throws Exception {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);


        final List<Integer> nums = Arrays.asList(4, 2, 3, 1);
        final JavaRDD<Integer> rdd = jsc.parallelize(nums, 2);
        final JavaPairRDD<String, Integer> pairRDD = rdd.mapToPair(
                num -> new Tuple2<>("a", num)
        );

        // TODO 保存
        pairRDD.saveAsTextFile("output1");
        pairRDD.saveAsObjectFile("output2");


        jsc.close();

    }
}
