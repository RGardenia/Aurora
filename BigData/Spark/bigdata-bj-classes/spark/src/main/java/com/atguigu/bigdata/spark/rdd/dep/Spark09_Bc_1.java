package com.atguigu.bigdata.spark.rdd.dep;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Serializable;

import java.util.Arrays;
import java.util.List;

public class Spark09_Bc_1 {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);

        final JavaRDD<String> rdd = jsc.parallelize(
                Arrays.asList("Hello", "Spark", "Hadoop", "Flink", "Spark", "Hadoop")
        );

        List<String> okList = Arrays.asList("Spark", "Hadoop");
        // TODO 默认数据传输以Task为单位进行传输，如果想要以Executor为单位传输，那么需要进行包装（封装）
        final Broadcast<List<String>> broadcast = jsc.broadcast(okList);

        final JavaRDD<String> filterRDD = rdd.filter(
                s -> broadcast.value().contains(s)
        );

        filterRDD.collect().forEach(System.out::println);

        jsc.close();

    }
}

