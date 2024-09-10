package com.atguigu.bigdata.spark.rdd.dep;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class Spark06_Persist {
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

                return kv;
            }
        );

        //mapRDD.cache();
        mapRDD.checkpoint();

        final JavaPairRDD<String, Integer> wordCountRDD = mapRDD.reduceByKey(Integer::sum);
        System.out.println(wordCountRDD.toDebugString());
        System.out.println("*********************");
        wordCountRDD.collect();
        System.out.println(wordCountRDD.toDebugString());

        // TODO cache方法会在血缘关系中增加依赖关系。
        // TODO checkpoint方法切断（改变）血缘关系。

        jsc.close();

    }
}