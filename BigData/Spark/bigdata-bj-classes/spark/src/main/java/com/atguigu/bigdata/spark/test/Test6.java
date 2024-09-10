package com.atguigu.bigdata.spark.test;

public class Test6 {
    public static void main(String[] args) {

        String s = "a";

        int num = s.hashCode() % 3;

        System.out.println(num);

    }
}
