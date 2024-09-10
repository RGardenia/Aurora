package com.atguigu.bigdata.spark.test;

import java.util.Arrays;
import java.util.List;

public class Test3 {
    public static void main(String[] args) {
        final List<String> strings = Arrays.asList(
                "zhangsan", "lisi", "wangwu"
        );
        for (String name : strings) {
            System.out.println(test(name));
        }

    }
    private static String test(String name) {

//        final String s1 = name.substring(0, 1);
//        final String s2 = s1.toUpperCase();
//        final String s3 = name.substring(1);
//        return s2 + s3;


        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
