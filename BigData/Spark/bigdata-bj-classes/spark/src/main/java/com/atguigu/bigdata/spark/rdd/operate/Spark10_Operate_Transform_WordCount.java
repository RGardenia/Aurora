package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Spark10_Operate_Transform_WordCount {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);
        // TODO 分组聚合
        //      1. 读取文件
        final JavaRDD<String> lineRDD = jsc.textFile("data/word.txt");
        //      2. 将文件数据进行分解
        final JavaRDD<String> wordRDD = lineRDD.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
        //      3. 将相同的单词分到一个组中
        //         Hello, Hello, Hello
        //         (Hello, [Hello, Hello, Hello])
        final JavaPairRDD<String, Iterable<String>> wordGroupRDD = wordRDD.groupBy(word -> word);
        //      4. 计算每个单词的组中的数量即可
        final JavaPairRDD<String, Integer> wordCountRDD = wordGroupRDD.mapValues(
                iter -> {
                    int len = 0;
                    for (String s : iter) {
                        len++;
                    }
                    return len;
                }
        );

        wordCountRDD
                .collect().forEach(System.out::println);


        jsc.close();

    }
}
