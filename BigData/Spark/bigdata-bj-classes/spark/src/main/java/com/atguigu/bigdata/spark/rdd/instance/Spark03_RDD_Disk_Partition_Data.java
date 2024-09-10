package com.atguigu.bigdata.spark.rdd.instance;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class Spark03_RDD_Disk_Partition_Data {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);

        // TODO Spark进行分区处理时，需要对每个分区的数据尽快能地平均分配
        //    totalsize = 7
        //    goalsize = totalsize / minpartnum = 7 / 2 = 3
        //    partnum = totalsize / goalsize = 7 / 3 = 2...1 => 2 + 1 = 3

        // TODO Spark不支持文件操作的。文件操作都是由Hadoop完成的
        //    Hadoop进行文件切片数量的计算核文件数据存储计算规则不一样。
        //    1. 分区数量计算的时候，考虑的是近可能的平均 : 按字节来计算
        //    2. 分区数据的存储是考虑业务数据的完整性 : 按照行来读取
        //       读取数据时，还需要考虑数据偏移量，偏移量从0开始的。
        //       读取数据时，相同的偏移量不能重复读取。
        /*
        【3】 => [0, 3]
        【3】 => [3, 6]
        【1】 => [6, 1]
        ----------------------------------------
        1@@ => 012
        2@@ => 345
        3   => 6
        ----------------------------------------
        [0, 3] => 【1,2】
        [3, 6] => 【3】
        [6, 7] =>

        ---------------------------------------------------------------------------
        1. 分区数量
           goalsize = 14 / 4 => 3
           partnum  = 14 / 3 = 4...2 => 4 + 1 = 5

        2. 分区数据
         [0, 3]   => 【11】
         [3, 6]   => 【22】
         [6, 9]   => 【33】
         [9, 12]  => 【44】
         [12, 14] => 【】
         -----------------------------------------
         11@@ => 0123
         22@@ => 4567
         33@@ => 891011
         44   => 1213
         -----------------------------------------
         12345678901234 => 012345678910111213
         [0, 3]   => 【12345678901234】
         [3, 6]   => 【】
         [6, 9]   => 【】
         [9, 12]  => 【】
         [12, 14] => 【】


         */
        final JavaRDD<String> rdd = jsc.textFile("data/test.txt", 4);

        rdd.saveAsTextFile("output");

        jsc.close();

    }
}
