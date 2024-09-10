package com.atguigu.bigdata.spark.rdd.dep;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class Spark03_Dep {
    public static void main(String[] args) throws Exception{

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

        final JavaRDD<Tuple2<String, Integer>> rdd = jsc.parallelize(datas, 2);
        final JavaPairRDD<String, Integer> mapRDD = rdd.mapToPair(
            kv -> {
                System.out.println("*********************");
                return kv;
            }
        );
        final JavaPairRDD<String, Integer> wordCountRDD = mapRDD.reduceByKey(Integer::sum);
        wordCountRDD.collect();
        System.out.println("计算1完毕");
        System.out.println("####################################");
        final JavaRDD<Tuple2<String, Integer>> rdd1 = jsc.parallelize(datas, 2);
        final JavaPairRDD<String, Integer> mapRDD1 = rdd1.mapToPair(kv -> {
            System.out.println("*********************");
            return kv;
        });
        final JavaPairRDD<String, Iterable<Integer>> groupRDD = mapRDD1.groupByKey();

        groupRDD.collect();
        System.out.println("计算2完毕");

        jsc.close();

    }
}