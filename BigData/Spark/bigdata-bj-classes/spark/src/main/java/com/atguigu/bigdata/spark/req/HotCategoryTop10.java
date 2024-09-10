package com.atguigu.bigdata.spark.req;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class HotCategoryTop10 {
    public static void main(String[] args) {

        // TODO 搭建Spark的运行环境
        SparkConf conf = new SparkConf();
        conf.setAppName("HotCategoryTop10");
        conf.setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        // TODO 将文件作为数据源，对接RDD
        final JavaRDD<String> dataRDD = jsc.textFile("data/user_visit_action.txt");

        // TODO 需求分析
        //      热门( 点击数量，下单数量，支付数量 )品类Top10
        //      1. 对同一个品类的不同行为进行统计
        //         对同一个品类的数据进行分组（点击，下单，支付）。
        //         (品类，点击数量)
//                 (品类，下单数量)
//                 (品类，支付数量)
        //      2. 对统计结果进行排序
        //         (user, age, amount) 先按照年龄排序，再按照金额排序
        //         (品类, 点击数量，下单数量，支付数量 )
        //      3. 对排序后的结果取前10条
        //         first(), take(10)

        /*
                (品类, (品类，1，  0，      0))
                (品类ID，1，  0，      0)
                (品类ID，1，  0，      0)
                (品类ID，1，  0，      0)
                (品类ID，1，  0，      0)
                (品类ID，1，  0，      0)
                (品类ID， 0， 1，      0)
                (品类ID， 0， 1，      0)
                (品类ID， 0， 1，      0)
                (品类ID， 0， 1，      0)
                (品类ID， 0， 1，      0)
                (品类ID， 0， 1，      0)
                (品类ID， 0， 1，      0)
                (品类ID， 0， 0，      1)
                (品类ID， 0， 0，      1)
                (品类ID， 0， 0，      1)
                (品类ID， 0， 0，      1)
                (品类ID， 0， 0，      1)
                (品类ID， 0， 0，      1)
                (品类ID， 0， 0，      1)
             => reduceByKey
                (品类, 点击数量，下单数量，支付数量 )
             --------------------------------------
                (品类，10，  0，  0)
                (品类， 0，  5，  0)
                ------------------
                (品类，10，  5，  0)
                (品类， 0，  0，  2)
                -----------------
             =>
                (品类, 10，  5，  2)
         */
        // TODO 开发原则
        //      大数据开发：数据量大
        //      1. 多什么，删什么（减小数据规模）
        //          去除搜索数据
        //          对于的数据字段
        //      2. 缺什么，补什么
        //          品类。行为类型
        //      3. 功能实现中，要尽可能少地使用shuffle操作。shuffle越多，性能越低。

        // TODO 1. 将多余的数据进行删除（过滤）处理
        final JavaRDD<String> filterRDD = dataRDD.filter(
                line -> {
                    // TODO 判断数据是否为搜索数据，如果为搜索数据，那么数据就不要(false)。
                    // TODO 判断数据是否为搜索数据，如果不为搜索数据，那么数据就保留（true）。
                    // TODO 分解数据
                    final String[] ss = line.split("_");
                    return "null".equals(ss[5]);
                }
        );

        // TODO 2. 将过滤后的数据进行分组统计
        // -------------------------- 点击数量统计 -----------------------------------------
        // 保留点击数据
        final JavaRDD<String> clickDadtaRDD = filterRDD.filter(
                line -> {
                    final String[] ss = line.split("_");
                    return !"-1".equals(ss[6]);
                }
        );
        // reduceByKey
        final JavaPairRDD<String, Integer> clickCountRDD = clickDadtaRDD.mapToPair(
                line -> {
                    // (体育用品，1)
                    // (体育用品，1)
                    // (体育用品，1)
                    // (体育用品，1)
                    final String[] ss = line.split("_");
                    return new Tuple2<>(ss[6], 1);
                }
        ).reduceByKey(Integer::sum);

        System.out.println(clickCountRDD.take(10));
        // -------------------------- 下单数量统计 -----------------------------------------
        // reduceByKey
        // -------------------------- 支付数量统计 -----------------------------------------
        // reduceByKey


        jsc.close();

    }
}
