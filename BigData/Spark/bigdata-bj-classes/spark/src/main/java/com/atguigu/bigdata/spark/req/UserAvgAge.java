package com.atguigu.bigdata.spark.req;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class UserAvgAge {
    public static void main(String[] args) {

        // TODO 搭建Spark的运行环境
        SparkConf conf = new SparkConf();
        conf.setAppName("UserAvgAge");
        conf.setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        // TODO 将文件作为数据源，对接RDD
        final JavaRDD<String> dataRDD = jsc.textFile("data/user.txt");

        // TODO 获取年龄的平均值
        //      RDD功能和方法，数据源的数据，格式不是我们的学习重点。所以解析数据非常的繁琐。
        //      所在在特殊的场景中，Spark对数据处理的逻辑进行了封装，简化开发。
        //      SparkSQL其实就是对Spark RDD的封装
        // line => 30
        // line => 40
        // line => 50
        //final List<Integer> ages = Arrays.asList(30, 40, 50);
        final JavaRDD<Integer> ageRDD = dataRDD.map(
                line -> {
                    int age = 0;
                    final String lineData = line.trim();
                    final String attrsData = lineData.substring(1, lineData.length()-1);

                    final String[] attrs = attrsData.split(",");
                    for (String attr : attrs) {
                        final String[] kv = attr.trim().split(":");
                        for ( int i = 0; i < kv.length; i+=2 ) {
                            if ( "\"age\"".equals(kv[i].trim()) ) {
                                age = Integer.parseInt(kv[i+1]);
                                break;
                            }
                        }
                    }
                     return age;
                }
        );

        long avgAge = ageRDD.reduce(Integer::sum) / ageRDD.count();

        System.out.println(avgAge);


        jsc.close();

    }
}
