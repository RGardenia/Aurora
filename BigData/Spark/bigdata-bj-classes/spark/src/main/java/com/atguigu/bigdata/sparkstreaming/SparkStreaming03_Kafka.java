package com.atguigu.bigdata.sparkstreaming;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import java.util.*;

public class SparkStreaming03_Kafka {
    public static void main(String[] args) throws Exception {

        SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("SparkStreaming");
        final JavaStreamingContext jsc = new JavaStreamingContext(conf, new Duration(3 * 1000));

        // TODO 通过环境对象对接Socket数据源，获取数据模型，进行数据处理
        // 创建配置参数
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"hadoop102:9092,hadoop103:9092,hadoop104:9092");
        map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        map.put(ConsumerConfig.GROUP_ID_CONFIG,"atguigu");
        map.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest"); // LEO

        // 需要消费的主题
        ArrayList<String> strings = new ArrayList<>();
        strings.add("aaa");

        JavaInputDStream<ConsumerRecord<String, String>> directStream = KafkaUtils.createDirectStream(
                jsc ,
                LocationStrategies.PreferBrokers(),
                ConsumerStrategies.<String, String>Subscribe(strings,map));

        directStream.map(new Function<ConsumerRecord<String, String>, String>() {
            @Override
            public String call(ConsumerRecord<String, String> v1) throws Exception {
                return v1.value();
            }
        }).print(100);

        jsc.start();
        jsc.awaitTermination();
    }
}
