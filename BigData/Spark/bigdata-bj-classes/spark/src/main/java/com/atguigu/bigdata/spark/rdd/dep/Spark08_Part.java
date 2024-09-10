package com.atguigu.bigdata.spark.rdd.dep;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class Spark08_Part {
    public static void main(String[] args) throws Exception{

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);

        final List<Tuple2<String, Integer>> datas =
                new ArrayList<>();
        datas.add(new Tuple2<String, Integer>("a", 1));
        datas.add(new Tuple2<String, Integer>("a", 2));
        datas.add(new Tuple2<String, Integer>("a", 3));
        datas.add(new Tuple2<String, Integer>("a", 4));

        final JavaRDD<Tuple2<String, Integer>> rdd = jsc.parallelize(datas, 3);
        final JavaPairRDD<String, Integer> mapRDD = rdd.mapToPair(
                kv -> {
                    System.out.println("*********************");
                    return kv;
                }
        );

        // (((1 + 2) + 3) + 4) => 10
        // ((1) + (2)) + (3 + 4) => 10
        // (a, 10)

        // TODO reduceByKey方法需要传递两个参数
        //     1. 第一个参数表示数据分区的规则，参数可以不用传递，使用时，会使用默认值（默认分区规则:HashPartitioner）
        //        HashPartitioner中有getPartition一个方法
        //              getPartition需要传递一个参数Key, 然后方法需要返回一个值，表示分区编号，分区编号从0开始。
        //                 逻辑： 分区编号 <=  Key.hashCode % partNum (哈希取余)
        //     2. 第二个参数表示数据聚合的逻辑
        final JavaPairRDD<String, Integer> reduceRDD = mapRDD.reduceByKey(Integer::sum);
        final JavaPairRDD<String, Integer> sortByKey = mapRDD.sortByKey();
        final JavaPairRDD<String, Iterable<Integer>> stringIterableJavaPairRDD = mapRDD.groupByKey();

        // TODO 数据分区的规则
        //      计算后数据所在的分区是通过Spark的内部计算（分区）完成,尽可能让数据均衡（散列）一些，但是不是平均分。
        reduceRDD.saveAsTextFile("output");

        jsc.close();

    }
}