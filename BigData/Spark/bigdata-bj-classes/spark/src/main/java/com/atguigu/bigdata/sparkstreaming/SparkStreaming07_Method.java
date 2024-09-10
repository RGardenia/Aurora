package com.atguigu.bigdata.sparkstreaming;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;

public class SparkStreaming07_Method {
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

        // TODO code =>
        // int i = 10; (Driver 1)
        wordCountDS.foreachRDD(
                rdd -> {
                    // TODO code =>
                    //int j = 20; (Driver X)
                    //rdd.collect().forEach(System.out::println);
                    rdd.foreach(
                            (num) -> {
                                // TODO code =>
                                // int k = 30; (Executor N)
                                System.out.println(num);
                            }
                    );
                }
        );

        jsc.start();
        jsc.awaitTermination();
    }
}
