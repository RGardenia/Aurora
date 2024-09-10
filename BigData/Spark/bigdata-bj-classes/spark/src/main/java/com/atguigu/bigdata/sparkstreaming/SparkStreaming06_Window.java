package com.atguigu.bigdata.sparkstreaming;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;

public class SparkStreaming06_Window {
    public static void main(String[] args) throws Exception {

        SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("SparkStreaming");
        final JavaStreamingContext jsc = new JavaStreamingContext(conf, new Duration(3 * 1000));

        final JavaReceiverInputDStream<String> socketDS = jsc.socketTextStream("localhost", 9999);

        // Word Count
        final JavaDStream<String> flatDS = socketDS.flatMap(
                line -> Arrays.asList(line.split(" ")).iterator()
        );

        final JavaPairDStream<String, Integer> wordDS = flatDS.mapToPair(
                word -> new Tuple2<>(word, 1)
        );
        // TODO 窗口 : 其实就是数据的范围（时间）
        //      window方法可以改变窗口的数据范围（默认数据范围为采集周期）
        //      window方法可以传递2个参数
        //             第一个参数表示窗口的数据范围（时间）
        //             第二个参数表示窗口的移动幅度（时间），可以不用传递，默认使用的就是采集周期
        //      SparkStreaming是在窗口移动的时候计算的。
        final JavaPairDStream<String, Integer> windowDS = wordDS.window(
                new Duration(3 * 1000),
                new Duration(6 * 1000));
        final JavaPairDStream<String, Integer> wordCountDS = windowDS.reduceByKey(Integer::sum);

        wordCountDS.print();

        jsc.start();
        jsc.awaitTermination();
    }
}
