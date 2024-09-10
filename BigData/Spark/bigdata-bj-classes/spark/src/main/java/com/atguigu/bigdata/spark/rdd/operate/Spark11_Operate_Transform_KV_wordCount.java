package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

public class Spark11_Operate_Transform_KV_wordCount {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);

        // TODO groupByKey方法作用是将KV类型的数据直接按照K对V进行分组
        //     (a, [1,3])
        //     (b, [2,4])
        jsc.parallelizePairs(
                Arrays.asList(
                        new Tuple2<>("a", 1),
                        new Tuple2<>("b", 2),
                        new Tuple2<>("a", 3),
                        new Tuple2<>("b", 4)
                )
        )
        .groupByKey()
        .mapValues(
                iter -> {
                    int sum = 0;
                    final Iterator<Integer> iterator = (Iterator<Integer>)iter.iterator();
                    while (iterator.hasNext() ) {
                        int num = iterator.next();
                        sum = sum + num;
                    }
                    return sum;
                }
        )
        .collect().forEach(System.out::println);


        jsc.close();

    }
}
