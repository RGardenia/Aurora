package com.atguigu.bigdata.spark.req;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Serializable;
import scala.Tuple2;

public class HotCategoryTop10_1 {
    public static void main(String[] args) {

        // TODO 搭建Spark的运行环境
        SparkConf conf = new SparkConf();
        conf.setAppName("HotCategoryTop10");
        conf.setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        // TODO 将文件作为数据源，对接RDD
        final JavaRDD<String> dataRDD = jsc.textFile("data/user_visit_action.txt");

        final JavaRDD<String> filterRDD = dataRDD.filter(
                line -> {
                    final String[] ss = line.split("_");
                    return "null".equals(ss[5]);
                }
        );

        // TODO 2. 将过滤后的数据进行分组统计
        //         将过滤后的数据进行结构的转变：(品类， 0， 1，      0)
        final JavaRDD<HotCategory> mapRDD = filterRDD.map(
                line -> {
                    final String[] ss = line.split("_");
                    if (!"-1".equals(ss[6])) {
                        // TODO 点击数据
                        return new HotCategory(ss[6], 1L, 0L, 0L);
                    } else if (!"null".equals(ss[8])) {
                        // TODO 下单数据
                        // ((a,b,c), 0, 1, 0)
                        // =>
                        // (a, 0, 1, 0)
                        // (b, 0, 1, 0)
                        // (c, 0, 1, 0)
                        return new HotCategory(ss[8], 0L, 1L, 0L);
                    } else {
                        // TODO 支付数据
                        return new HotCategory(ss[10], 0L, 0L, 1L);
                    }
                }
        );

        final JavaPairRDD<String, HotCategory> kvRDD = mapRDD.mapToPair(obj -> new Tuple2<>(obj.getId(), obj));

        final JavaPairRDD<String, HotCategory> objCountRDD = kvRDD.reduceByKey(
                (obj1, obj2) -> {
                    obj1.setClickCount(obj1.getClickCount() + obj2.getClickCount());
                    obj1.setOrderCount(obj1.getOrderCount() + obj2.getOrderCount());
                    obj1.setPayCount(obj1.getPayCount() + obj2.getPayCount());

                    return obj1;
                }
        );

        objCountRDD.take(10).forEach(System.out::println);


        jsc.close();

    }
}
// TODO 自定义数据对象
//     1. 实现可序列化接口
//     2. 遵循Bean规范
//     3. 提供无参和全参的构造方法
//     4. 重写toString方法
//class HotCategory implements Serializable {
//    private String id;
//    private Long clickCount;
//    private Long orderCount;
//    private Long payCount;
//
//    public HotCategory(String id, Long clickCount, Long orderCount, Long payCount) {
//        this.id = id;
//        this.clickCount = clickCount;
//        this.orderCount = orderCount;
//        this.payCount = payCount;
//    }
//
//    public HotCategory() {
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public Long getClickCount() {
//        return clickCount;
//    }
//
//    public void setClickCount(Long clickCount) {
//        this.clickCount = clickCount;
//    }
//
//    public Long getOrderCount() {
//        return orderCount;
//    }
//
//    public void setOrderCount(Long orderCount) {
//        this.orderCount = orderCount;
//    }
//
//    public Long getPayCount() {
//        return payCount;
//    }
//
//    public void setPayCount(Long payCount) {
//        this.payCount = payCount;
//    }
//
//    @Override
//    public String toString() {
//        return "HotCategory{" +
//                "id='" + id + '\'' +
//                ", clickCount=" + clickCount +
//                ", orderCount=" + orderCount +
//                ", payCount=" + payCount +
//                '}';
//    }
//}
