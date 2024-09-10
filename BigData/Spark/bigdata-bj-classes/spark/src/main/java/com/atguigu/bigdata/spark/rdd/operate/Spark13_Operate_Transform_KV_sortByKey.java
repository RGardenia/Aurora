package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Spark13_Operate_Transform_KV_sortByKey {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);

        final List<Tuple2<String, Integer>> datas =
                new ArrayList<>();
        datas.add(new Tuple2<String, Integer>("a", 1));
        datas.add(new Tuple2<String, Integer>("b", 3));
        datas.add(new Tuple2<String, Integer>("a", 4));
        datas.add(new Tuple2<String, Integer>("b", 2));

        final JavaRDD<Tuple2<String, Integer>> rdd = jsc.parallelize(datas);
        final JavaPairRDD<String, Integer> mapRDD = rdd.mapToPair(t -> t);

        // TODO sortByKey方法
        //      groupByKey  : 按照 K 对 V 进行分组
        //      reduceByKey : 按照 K 对 V 进行两两聚合
        //      sortByKey   : 按照 K 排序
        final JavaPairRDD<String, Integer> sortRDD = mapRDD.sortByKey(false);

        sortRDD.collect().forEach(System.out::println);


        jsc.close();

    }
}
