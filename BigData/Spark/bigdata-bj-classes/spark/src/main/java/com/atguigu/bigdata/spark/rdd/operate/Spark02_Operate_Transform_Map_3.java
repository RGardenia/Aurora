package com.atguigu.bigdata.spark.rdd.operate;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public class Spark02_Operate_Transform_Map_3 {
    public static void main(String[] args) {

        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("spark");

        final JavaSparkContext jsc = new JavaSparkContext(conf);
        final List<Integer> nums = Arrays.asList(1, 2, 3, 4);
        final JavaRDD<Integer> rdd = jsc.parallelize(nums, 2);

        // TODO JDK1.8来自于Scala语言，由马丁引入，能省则省
//        final JavaRDD<String> newRDD = rdd.map(new Function<Integer, String>() {
//            @Override
//            public String call(Integer in) throws Exception {
//                return in + "abc";
//            }
//        });
        // TODO 如果Java中接口采用注解@FunctionalInterface声明，那么接口的使用就可以采用JDK提供的函数式编程的语法实现（λ表达式）
        //      1. return 可以省略 : map方法就需要返回值，所以不屑return
        //      2. 分号 可以省略 : 可以采用换行的方式表示代码逻辑
        //      3. 大括号 可以省略 : 如果逻辑代码只有一行
        //      4. 小括号 可以省略 : 参数列表中的参数只有一个
        //      5. 参数和箭头 可以省略 : 参数在逻辑中只使用了一次(需要有对象来实现功能)
//        rdd.map(
//                num -> num * 2
//        );

        final JavaRDD<Integer> newRDD = rdd.map(NumberTest::mul2);

        newRDD.collect().forEach(System.out::println);
        System.out.println("*************************************");
        System.out.println(nums);


        jsc.close();

    }
}

class NumberTest {
    public static int mul2( Integer num ) {
        return num * 2;
    }
}