package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Serializable;

import java.util.Arrays;
import java.util.List;

public class Spark19_Operate_Action {
    public static void main(String[] args) throws Exception {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);


        final List<String> nums = Arrays.asList("Hadoop", "Hive", "Spark", "Flink");
        final JavaRDD<String> rdd = jsc.parallelize(nums, 2);

        // RDD算子(方法)的逻辑代码是在Executor端执行的，其他的代码都是Driver端执行
        Search search = new Search("H");
        search.match(rdd);



        jsc.close();

    }
}

class Search {
    private String query;
    public Search( String query ) {
        this.query = query;
    }
    public void match( JavaRDD<String> rdd ) {
        String q = this.query;
        rdd.filter(
                s -> s.startsWith(q)
        ).collect().forEach(System.out::println);
    }
}