package com.atguigu.bigdata.spark.test;

public class Test {
    public static void main(String[] args) {

        String s = " a b "; // char[] value = [' ', 'a', ' ', 'b', ' ']
        // trim去掉字符串首尾半角空格
        String s1 = s.trim(); // char[] value = ['a', ' ', 'b']
        //final String s2 = s.substring(0, 1);
        s.substring(0, 1);
        //s.replaceAll('a, 'c')
        System.out.println("!"+s1+"!");
        // TODO String不可变字符串
        // !ab!
        // !a b!
        // ! a b !
    }
}
