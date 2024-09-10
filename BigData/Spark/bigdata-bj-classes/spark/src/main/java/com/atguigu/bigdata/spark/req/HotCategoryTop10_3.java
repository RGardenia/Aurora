package com.atguigu.bigdata.spark.req;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Serializable;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HotCategoryTop10_3 {
    public static void main(String[] args) {

        // TODO 搭建Spark的运行环境
        SparkConf conf = new SparkConf();
        conf.setAppName("HotCategoryTop10");
        conf.setMaster("local[*]"); // Yarn : Spark On Yarn
        JavaSparkContext jsc = new JavaSparkContext(conf);

        // TODO 将文件作为数据源，对接RDD
        //      如果当前环境是Yarn，那么相对路径指向的就是 HDFS
        jsc
            .textFile("data/user_visit_action.txt")
            .filter(
                line -> {
                    final String[] ss = line.split("_");
                    return "null".equals(ss[5]);
                }
            )
            .flatMap(
                line -> {
                    final String[] ss = line.split("_");
                    if (!"-1".equals(ss[6])) {
                        // TODO 点击数据
                        return Arrays.asList(new HotCategory(ss[6], 1L, 0L, 0L)).iterator();
                    } else if (!"null".equals(ss[8])) {
                        // TODO 下单数据
                        final String[] ids = ss[8].split(",");
                        List<HotCategory> objs = new ArrayList<>();
                        for (String id : ids) {
                            objs.add( new HotCategory(id, 0L, 1L, 0L) );
                        }
                        return objs.iterator();
                    } else {
                        // TODO 支付数据
                        final String[] ids = ss[10].split(",");
                        List<HotCategory> objs = new ArrayList<>();
                        for (String id : ids) {
                            objs.add( new HotCategory(id, 0L, 0L, 1L) );
                        }
                        return objs.iterator();
                    }
                }
        )
        .mapToPair(obj -> new Tuple2<>(obj.getId(), obj))
        .reduceByKey(
                (obj1, obj2) -> {
                    obj1.setClickCount(obj1.getClickCount() + obj2.getClickCount());
                    obj1.setOrderCount(obj1.getOrderCount() + obj2.getOrderCount());
                    obj1.setPayCount(obj1.getPayCount() + obj2.getPayCount());

                    return obj1;
                }
        )
        .map(kv -> kv._2)
        .sortBy(obj -> obj, true, 2)
        .take(10)
        .forEach(System.out::println);


        jsc.close();

    }
}

// TODO 自定义数据对象
//     1. 实现可序列化接口
//     2. 遵循Bean规范
//     3. 提供无参和全参的构造方法
//     4. 重写toString方法
//     5. 实现可比较的接口，重写比较方法
class HotCategory implements Serializable, Comparable<HotCategory> {
    private String id;
    private Long clickCount;
    private Long orderCount;
    private Long payCount;

    public HotCategory(String id, Long clickCount, Long orderCount, Long payCount) {
        this.id = id;
        this.clickCount = clickCount;
        this.orderCount = orderCount;
        this.payCount = payCount;
    }

    public HotCategory() {
    }

    @Override
    public int compareTo(HotCategory other) {
        if ( this.clickCount > other.clickCount ) {
            return -1;
        } else if ( this.clickCount < other.clickCount ) {
            return 1;
        } else {
            if ( this.orderCount > other.orderCount ) {
                return -1;
            } else if ( this.orderCount < other.orderCount ) {
                return 1;
            } else {
                return (int)(other.payCount - this.payCount);
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    public Long getPayCount() {
        return payCount;
    }

    public void setPayCount(Long payCount) {
        this.payCount = payCount;
    }

    @Override
    public String toString() {
        return "HotCategory{" +
                "id='" + id + '\'' +
                ", clickCount=" + clickCount +
                ", orderCount=" + orderCount +
                ", payCount=" + payCount +
                '}';
    }
}
