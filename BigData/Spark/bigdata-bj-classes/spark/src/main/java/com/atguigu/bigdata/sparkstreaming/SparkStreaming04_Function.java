package com.atguigu.bigdata.sparkstreaming;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;

public class SparkStreaming04_Function {
    public static void main(String[] args) throws Exception {

        SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("SparkStreaming");
        final JavaStreamingContext jsc = new JavaStreamingContext(conf, new Duration(3 * 1000));

        // TODO 通过环境对象对接Socket数据源，获取数据模型，进行数据处理
        final JavaReceiverInputDStream<String> socketDS = jsc.socketTextStream("localhost", 9999);

        // Word Count
        final JavaDStream<String> flatDS = socketDS.flatMap(
                line -> Arrays.asList(line.split(" ")).iterator()
        );

        final JavaPairDStream<String, Integer> wordDS = flatDS.mapToPair(
                word -> new Tuple2<>(word, 1)
        );

        final JavaPairDStream<String, Integer> wordCountDS = wordDS.reduceByKey(Integer::sum);

        // TODO DStream确实就是对RDD的封装，但是不是所有的方法都进行了分装。有些方法不能使用：sortBy, sortByKey
        //      如果特定场合下，就需要使用这些方法，那么就需要将DStream转换为RDD使用


        //wordCountDS.print();
        wordCountDS.foreachRDD(
                rdd -> {
                    rdd.sortByKey().collect().forEach(System.out::println);
                }
        );

        jsc.start();
        jsc.awaitTermination();
    }
}
