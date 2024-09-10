package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.Arrays;
import java.util.List;

public class Spark03_Operate_Transform_Filter {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");
        final JavaSparkContext jsc = new JavaSparkContext(conf);
        final List<Integer> nums = Arrays.asList(1, 2, 3, 4);
        final JavaRDD<Integer> rdd = jsc.parallelize(nums, 2);

        // TODO RDD的转换方法：filter(过滤)
        //      RDD可以根据指定的过滤规则对数据源中的数据进行筛选过滤
        //      如果满足规则（返回结果true），那么数据保留，如果不满足规则（返回结果false），那么数据就会丢弃
//        final JavaRDD<Integer> filterRDD = rdd.filter(new Function<Integer, Boolean>() {
//            @Override
//            public Boolean call(Integer num) throws Exception {
//                return false;
//            }
//        });

        // Map => A(String) -> B(User, List, Int)
        // Filter => A ->
        // filter方法在执行过程中可能会出现数据倾斜的情况，需要慎重考虑
        final JavaRDD<Integer> filterRDD = rdd.filter(num -> num % 2 == 1);

        filterRDD.collect().forEach(System.out::println);


        jsc.close();

    }
}
