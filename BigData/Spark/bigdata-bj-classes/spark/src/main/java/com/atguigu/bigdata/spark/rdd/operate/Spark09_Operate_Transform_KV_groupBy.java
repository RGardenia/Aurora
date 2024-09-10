package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Spark09_Operate_Transform_KV_groupBy {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);
        final List<Integer> nums = Arrays.asList(1, 2, 3, 4);
        final JavaRDD<Integer> rdd = jsc.parallelize(nums, 2);

        // TODO groupBy：按照指定的规则对数据进行分组
        //           给每一个数据增加一个标记，相同的标记的数据会放置在一个组中，这个标记就是组名
        //      groupBy结果：就是KV类型的数据
        //           (0，【2，4】) => (0，6)
        //           (1, 【1，3】) => (1，4)
        /*
         1 => 1
         2 => 0
         3 => 1
         4 => 0
         */
        final JavaPairRDD<Integer, Iterable<Integer>> groupRDD = rdd.groupBy(
                num -> num % 2
        );

        groupRDD.mapValues(
                iter -> {
                    int sum = 0;
                    final Iterator<Integer> iterator = iter.iterator();
                    while (iterator.hasNext() ) {
                        final Integer num = iterator.next();
                        sum = sum + num;
                    }
                    return sum;
                }
        ).collect().forEach(System.out::println);

        // TODO word -> count


        jsc.close();

    }
}
