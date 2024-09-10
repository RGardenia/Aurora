package com.atguigu.bigdata.sparkstreaming;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;

public class SparkStreaming05_Print {
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

        // TODO DStream中print方法类似于RDD的行动算子
        // IllegalArgumentException : No output operations registered, so nothing to execute
//        wordCountDS.print();
        wordCountDS.foreachRDD(
                rdd -> {
                    System.out.println("----------------------------------------");
                    System.out.println("Time :" + System.currentTimeMillis() + " ms");
                    System.out.println("----------------------------------------");
                    rdd.collect().forEach(System.out::println);
                }
        );

        jsc.start();
        jsc.awaitTermination();
    }
}
