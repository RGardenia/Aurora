package com.atguigu.bigdata.spark.rdd.dep;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Serializable;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Spark09_Bc {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);

        final JavaRDD<Integer> rdd = jsc.parallelize(
                Arrays.asList(1, 2, 3, 4)
        );

//        final Integer reduce = rdd.reduce(Integer::sum);
//        System.out.println(reduce);
        MyValue mv = new MyValue();
        rdd.foreach(num -> {
            System.out.println(num);
            mv.sum += num;
        });

        System.out.println(mv.sum);

        jsc.close();

    }
}
class MyValue implements Serializable {
    public int sum = 0;
}