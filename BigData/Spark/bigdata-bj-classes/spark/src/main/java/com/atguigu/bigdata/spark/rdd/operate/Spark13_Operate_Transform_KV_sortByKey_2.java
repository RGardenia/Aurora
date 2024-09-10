package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class Spark13_Operate_Transform_KV_sortByKey_2 {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);

        final List<Tuple2<String, Integer>> datas =
                new ArrayList<>();
        datas.add(new Tuple2<String, Integer>("a", 1));
        datas.add(new Tuple2<String, Integer>("a", 3));
        datas.add(new Tuple2<String, Integer>("a", 4));
        datas.add(new Tuple2<String, Integer>("a", 2));

        final JavaRDD<Tuple2<String, Integer>> rdd = jsc.parallelize(datas);
        final JavaPairRDD<String, Integer> mapRDD = rdd.mapToPair(t -> t);

        // (a, 1) => (1, (a, 1))
        // (a, 3) => (3, (a, 3))
        // (a, 4) => (4, (a, 4))
        // (a, 2) => (2, (a, 2))
        final JavaPairRDD<Integer, Tuple2<String, Integer>> mapRDD1 = mapRDD.mapToPair(
                kv -> new Tuple2<>(kv._2, kv)
        );
        mapRDD1
            .sortByKey()
            .map(
                kv -> kv._2
            ).collect().forEach(System.out::println);


        jsc.close();

    }
}
