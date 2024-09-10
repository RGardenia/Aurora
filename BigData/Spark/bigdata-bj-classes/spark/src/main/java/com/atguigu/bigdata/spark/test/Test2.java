package com.atguigu.bigdata.spark.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test2 {
    public static void main(String[] args) {
        // TODO 在数据处理过程中，一般不会改变原始数据
        final List<Integer> nums = Arrays.asList(1, 2, 3, 4);

//        for ( int i = 0; i < nums.size(); i++ ) {
//            //nums.add(i, nums.get(i) * 2);
//            nums.set(i, nums.get(i) * 2);
//        }
//        System.out.println(nums);

        List<Integer> newNums = new ArrayList<>();
        for ( int i = 0; i < nums.size(); i++ ) {
            final int newValue = nums.get(i) * 2;
            //final int OUT = IN * 2;
            newNums.add(newValue);
        }
        System.out.println(newNums);

    }
}
