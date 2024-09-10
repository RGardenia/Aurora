package com.atguigu.bigdata.spark.rdd.dep;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class Spark01_Dep {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);

        final List<Tuple2<String, Integer>> datas =
                new ArrayList<>();
        datas.add(new Tuple2<String, Integer>("a", 1));
        datas.add(new Tuple2<String, Integer>("a", 2));
        datas.add(new Tuple2<String, Integer>("a", 3));
        datas.add(new Tuple2<String, Integer>("a", 4));

        final JavaRDD<Tuple2<String, Integer>> rdd = jsc.parallelize(datas);
        System.out.println(rdd.toDebugString());
        System.out.println("*******************************************");
        final JavaPairRDD<String, Integer> mapRDD = rdd.mapToPair(t -> t);
        System.out.println(mapRDD.toDebugString());
        System.out.println("*******************************************");
        final JavaPairRDD<String, Integer> wordCountRDD = mapRDD.reduceByKey(Integer::sum);
        System.out.println(wordCountRDD.toDebugString());
        System.out.println("*******************************************");

        wordCountRDD.collect().forEach(System.out::println);

        jsc.close();

    }
}