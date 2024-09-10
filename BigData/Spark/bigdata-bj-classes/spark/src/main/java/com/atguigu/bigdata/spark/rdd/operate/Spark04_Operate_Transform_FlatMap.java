package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Spark04_Operate_Transform_FlatMap {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");
        final JavaSparkContext jsc = new JavaSparkContext(conf);

        final List<List<Integer>> datas = Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4)
        );

        final JavaRDD<List<Integer>> rdd = jsc.parallelize(datas, 2);

        // TODO RDD的转换方法：flatmap (扁平映射)
        //      flat(数据扁平化) + map(映射)
        final JavaRDD<Integer> flatMapRDD = rdd.flatMap(new FlatMapFunction<List<Integer>, Integer>() {
            @Override
            public Iterator<Integer> call(List<Integer> list) throws Exception {
                List<Integer> nums = new ArrayList<>();
                list.forEach(
                        num -> nums.add(num * 2)
                );
                return nums.iterator();
            }
        });


        flatMapRDD.collect().forEach(System.out::println);


        jsc.close();

    }
}
