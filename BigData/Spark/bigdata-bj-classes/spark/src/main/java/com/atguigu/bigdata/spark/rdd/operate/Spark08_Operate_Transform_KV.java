package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class Spark08_Operate_Transform_KV {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);

        // TODO KV类型一般表示 2元组
        //      Spark RDD会整体数据的处理就称之为单值类型的数据处理
        //      Spark RDD会KV数据个体的处理就称之为KV类型的数据处理: K 和 V 不作为整体使用
        final Tuple2<String, Integer> a = new Tuple2<String, Integer>("a", 1); // a, 2
        final Tuple2<String, Integer> a1 = new Tuple2<String, Integer>("b", 2); // b, 4
        final Tuple2<String, Integer> a2 = new Tuple2<String, Integer>("c", 3); // c, 6

        final List<Tuple2<String, Integer>> tuple2s = Arrays.asList(a, a1, a2 );

//        final JavaRDD<Tuple2<String, Integer>> rdd = jsc.parallelize(tuple2s);
//
//        rdd.map(
//            t -> new Tuple2<>( t._1, t._2 * 2 )
//        )
//        .collect().forEach(System.out::println);

        // TODO 上面的代码不是对KV类型饿数据进行处理，还是单值类型处理，是将2元组当成一个整体来使用。
        final JavaPairRDD pairRDD = jsc.parallelizePairs(tuple2s);
        // TODO mapValues方法只对 V进行处理，K不做任何操作
        pairRDD.mapValues(
                num -> {
                    int i = (Integer)num;
                    return i * 2;
                }
        ).collect().forEach(System.out::println);

        jsc.close();

    }
}
