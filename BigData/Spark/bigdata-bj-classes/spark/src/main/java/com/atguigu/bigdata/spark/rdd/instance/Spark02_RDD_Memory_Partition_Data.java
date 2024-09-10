package com.atguigu.bigdata.spark.rdd.instance;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public class Spark02_RDD_Memory_Partition_Data {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);


        final List<Integer> names = Arrays.asList(1,2,3,4,5,6);
        /*
          【1】
          【2，3】
          【4】
          【5，6】
          -------------------------------
          len=6, partnum=4

          (0 until 4) => [0, 1, 2, 3]

          0 => ((i * length) / numSlices, (((i + 1) * length) / numSlices))
            => ((0 * 6) / 4, (((0 + 1) * 6) / 4))
            => (0, 1) => 1
          1 => ((i * length) / numSlices, (((i + 1) * length) / numSlices))
            => ((1 * 6) / 4, (((2) * 6) / 4))
            => (1, 3) => 2
          2 => ((i * length) / numSlices, (((i + 1) * length) / numSlices))
            => ((2 * 6) / 4, (((3) * 6) / 4))
            => (3, 4) => 1
          3 => ((i * length) / numSlices, (((i + 1) * length) / numSlices))
            => ((3 * 6) / 4, (((4) * 6) / 4))
            => (4, 6) => 2
         */

        // TODO Spark分区数据的存储基本原则：平均分
        final JavaRDD<Integer> rdd = jsc.parallelize(names, 4);

        rdd.saveAsTextFile("output");


        jsc.close();

    }
}
