package com.atguigu.bigdata.spark.rdd.dep;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class Spark05_Persist_CP {
    public static void main(String[] args) throws Exception{

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);
        // TODO 设定检查点路径:推荐HDFS共享文件系统，也可以使用本地文件路径
        jsc.setCheckpointDir("cp");

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
        // TODO 在RDD重复之前使用之前进行持久化操作
//        mapRDD.cache(); // = mapRDD.persist(StorageLevel.MEMORY_ONLY());
//        mapRDD.persist(StorageLevel.MEMORY_ONLY());
        // TODO SparkException: Checkpoint directory has not been set in the SparkContext
        // TODO 检查点操作目的是希望RDD结果长时间的保存，所以需要保证数据的安全，会从头再跑一遍，性能比较低
        //      为了提高效率，Spark推荐再检查点之前，执行cache方法，将数据缓存。
        mapRDD.cache();
        mapRDD.checkpoint();

        final JavaPairRDD<String, Integer> wordCountRDD = mapRDD.reduceByKey(Integer::sum);
        wordCountRDD.collect();
        System.out.println("计算1完毕");
        System.out.println("####################################");
        final JavaPairRDD<String, Iterable<Integer>> groupRDD = mapRDD.groupByKey();

        groupRDD.collect();
        System.out.println("计算2完毕");

        jsc.close();

    }
}