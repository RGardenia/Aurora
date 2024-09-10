package com.atguigu.bigdata.spark.rdd.dep;

import org.apache.spark.Partitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class Spark08_Part_1 {
    public static void main(String[] args) throws Exception{

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);

        final List<Tuple2<String, Integer>> datas =
                new ArrayList<>();
        datas.add(new Tuple2<String, Integer>("nba", 1));
        datas.add(new Tuple2<String, Integer>("cba", 2));
        datas.add(new Tuple2<String, Integer>("nba", 3));
        datas.add(new Tuple2<String, Integer>("wnba", 4));

        final JavaRDD<Tuple2<String, Integer>> rdd = jsc.parallelize(datas, 2);
        final JavaPairRDD<String, Integer> mapRDD = rdd.mapToPair(
                kv -> {
                    return kv;
                }
        );

        //final JavaPairRDD<String, Integer> reduceRDD = mapRDD.reduceByKey(Integer::sum);//mapRDD.reduceByKey(new MyPartitioner(3), Integer::sum);
        //final JavaPairRDD<String, Integer> reduceRDD1 = reduceRDD.reduceByKey(Integer::sum);//reduceRDD.reduceByKey(new MyPartitioner(3), Integer::sum);
        final JavaPairRDD<String, Integer> reduceRDD = mapRDD.reduceByKey(new MyPartitioner(3), Integer::sum);
        final JavaPairRDD<String, Integer> reduceRDD1 = reduceRDD.reduceByKey(new MyPartitioner(3), Integer::sum);
        reduceRDD1.collect();

        System.out.println("计算完毕");
        // Stage = 1 + 1 = 2
        // Task  = 2 + 3 = 5
        Thread.sleep(100000000L);

        jsc.close();

    }
}
// TODO 自定义分区器
//     1. 创建自定义类
//     2. 继承抽象类 Partitioner
//     3. 重写方法（2 + 2）
//         Partitioner(2) + Object(2)
//     4. 构建对象，在算子中使用
class MyPartitioner extends Partitioner {

    private int numPartitions;

    public MyPartitioner( int num ) {
        this.numPartitions = num;
    }


    @Override
    // TODO 指定分区的数量
    public int numPartitions() {
        return this.numPartitions;
    }

    @Override
    // TODO 根据数据的KEY来获取数据存储的分区编号，编号从0开始
    public int getPartition(Object key) {
        if ( "nba".equals(key) ) {
            return 0;
        } else if ( "wnba".equals(key) ) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int hashCode() {
        return numPartitions;
    }

    @Override
    public boolean equals(Object o) {
        if ( o instanceof MyPartitioner ) {
            MyPartitioner other = (MyPartitioner)o;
            return this.numPartitions == other.numPartitions;
        } else {
            return false;
        }
    }
}