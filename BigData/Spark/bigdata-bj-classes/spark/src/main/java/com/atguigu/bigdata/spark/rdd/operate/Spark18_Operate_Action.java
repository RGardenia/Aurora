package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Serializable;

import java.util.Arrays;
import java.util.List;

public class Spark18_Operate_Action {
    public static void main(String[] args) throws Exception {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);


        final List<Integer> nums = Arrays.asList(1,2,3,4);
        final JavaRDD<Integer> rdd = jsc.parallelize(nums, 2);

        // TODO 对象是在Driver端创建的
        Student s = new Student();
//        Emp e = new Emp();

        // [1,2][3,4]
        // foreach算子是分布式循环：分区内有序，分区间无序
        // TODO
        //    SparkException: Task not serializable
        //    java.io.NotSerializableException: com.atguigu.bigdata.spark.rdd.operate.Student
        rdd.foreach(
                num -> {
                    // TODO 在Executor端循环遍历的时候使用到了Driver端对象
                    //      运行过程中，就需要将Driver端的对象通过网络传递到Executor端，否则无法使用
                    //      这里传输的对象必须要实现可序列化接口，否则无法传递。
                    System.out.println(s.age + num);
                }
        );
        // 31, 32
        // 33, 34

        jsc.close();

    }
}
class Student implements Serializable {
    public int age = 30;
}
//class Emp {
//    public int age = 30;
//}