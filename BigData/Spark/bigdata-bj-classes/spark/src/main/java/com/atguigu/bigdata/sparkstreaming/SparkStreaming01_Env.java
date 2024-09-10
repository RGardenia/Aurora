package com.atguigu.bigdata.sparkstreaming;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class SparkStreaming01_Env {
    public static void main(String[] args) throws Exception {

        // TODO 构建环境对象
        //      Spark在流式数据的处理场景中对核心功能环境进行了封装
        SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("SparkStreaming");

        final JavaStreamingContext jsc = new JavaStreamingContext(conf, new Duration(3 * 1000));

        // TODO 启动数据采集器
        jsc.start();
        // TODO 等待数据采集器的结束，如果采集器停止运行，那么main线程会继续执行
        jsc.awaitTermination();

        // TODO 数据采集器是一个长期执行的任务，所以不能停止，也不能释放资源
        //jsc.close();
//        while ( true ) {}

    }
}
