package com.atguigu.bigdata.spark.rdd.dep;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class Spark07_Persist {
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

        final JavaRDD<Tuple2<String, Integer>> rdd = jsc.parallelize(datas, 2);
        final JavaPairRDD<String, Integer> mapRDD = rdd.mapToPair(
            kv -> {
                System.out.println("*********************");
                return kv;
            }
        );
        // TODO 所有的shuffle操作性能是非常低，所以Spark为了提升shuffle算子的性能，每个shuffle算子都是自动含有缓存
        //      如果重复调用相同规则的shuffle算子，那么第二次shuffle算子不会有shuffle操作.

        final JavaPairRDD<String, Integer> reduceRDD = mapRDD.reduceByKey(Integer::sum);
        //reduceRDD.groupByKey().collect();
        // final JavaPairRDD<String, Integer> wordCountRDD1 = wordCountRDD.reduceByKey(Integer::sum);
        //System.out.println("###############################");
        //reduceRDD.sortByKey().collect();
        reduceRDD.reduceByKey(Integer::sum).collect();

        // http://localhost:4040
        System.out.println("计算完毕");
        Thread.sleep(1000000L);

        jsc.close();

    }
}