package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.Arrays;
import java.util.List;

public class Spark15_Operate_Action {
    public static void main(String[] args) throws Exception {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);


        final List<Integer> nums = Arrays.asList(1, 2, 3, 4);
        final JavaRDD<Integer> rdd = jsc.parallelize(nums, 2);
        final JavaRDD<Object> newRDD = rdd.map(num -> {
            //System.out.println("num = " + num);
            return num * 2;
        });

        // TODO collect方法就是RDD的行动算子
        //      RDD的行动算子会触发作业（Job）的执行
        //newRDD.collect().forEach(System.out::println);

        // 行动算子和转换算子如何区分？
        //     如果方法调用后，执行Job了，那么这个方法就是行动算子(X)
        //     如果方法调用后，不执行Job，那么这个方法就是转换算子(X)
        // 转换算子的目的：将旧的RDD转换成新的RDD，为了组合多个RDD的功能
        //        RDD(In) -> RDD(Out)
        final JavaRDD<Object> sortByRDD = newRDD.sortBy(num -> num, true, 2);
//        final JavaRDD<Object> map = newRDD.map(num -> num);
//        final JavaPairRDD<Object, Iterable<Object>> objectIterableJavaPairRDD = newRDD.groupBy(num -> num);

//        final List<Object> collect = newRDD.collect();
//        final long count = newRDD.count();
        sortByRDD.collect();

        System.out.println("计算完毕");

        // http://localhost:4040
        Thread.sleep(10000000L);


        jsc.close();

    }
}
