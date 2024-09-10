package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Spark12_Operate_Transform_KV_wordCount {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        //conf.setMaster("local");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);

        // TODO groupByKey方法作用是将KV类型的数据直接按照K对V进行分组
        //     (a, [1,3])
        //     (b, [2,4])
        final List<Tuple2<String, Integer>> datas =
                new ArrayList<>();
        datas.add(new Tuple2<String, Integer>("a", 1));
        datas.add(new Tuple2<String, Integer>("a", 2));
        datas.add(new Tuple2<String, Integer>("a", 3));
        datas.add(new Tuple2<String, Integer>("a", 4));

        final JavaRDD<Tuple2<String, Integer>> rdd = jsc.parallelize(datas);
        final JavaPairRDD<String, Integer> mapRDD = rdd.mapToPair(t -> t);

        // TODO 将分组聚合功能进行简化操作
        //     reduceByKey方法的作用：将KV类型的数据按照 K 对 V 进行reduce（将多个值聚合成一个值）操作
        //         [1,2,3,4] => 10
        //     计算的基本思想：两两计算
        //     (i1, i2) => i3
        //final JavaPairRDD<String, Integer> wordCountRDD = mapRDD.reduceByKey(Integer::sum);
//        final JavaPairRDD<String, Integer> wordCountRDD = mapRDD.reduceByKey(
//                new Function2<Integer, Integer, Integer>() {
//                    @Override
//                    public Integer call(Integer v1, Integer v2) throws Exception {
//                        return v1 + v2;
//                    }
//                }
//        );
//        final JavaPairRDD<String, Integer> wordCountRDD = mapRDD.reduceByKey(
//                (i1, i2) -> i1 + i2
//        );
        final JavaPairRDD<String, Integer> wordCountRDD = mapRDD.reduceByKey(Integer::sum);

        wordCountRDD.collect().forEach(System.out::println);

        jsc.close();

    }
}
class SumTest {
    public static int sum(Integer v1, Integer v2) {
        return v1 + v2;
    }
}