package com.atguigu.bigdata.spark.test;

import java.util.Arrays;
import java.util.List;

public class Test5 {
    public static void main(String[] args) {

        final List<Integer> integers = Arrays.asList(1, 2, 3, 4);

        for (Integer integer : integers) {
            test1(integer);
        }
        System.out.println("***************************************");
        test2(integers);
    }
    private static void test1( Integer num ) {
        System.out.println(num);
    }
    private static void test2( List<Integer> nums ) {
        for (Integer num : nums) {
            System.out.println(num);
        }
    }
}
