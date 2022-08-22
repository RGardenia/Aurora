package com.zy.Unknown;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class IntersectionArray {

    public int[] intersection1(int[] nums1, int[] nums2){

        TreeMap<Integer, Integer> map = new TreeMap<>();
        for(int num: nums1){
            if(!map.containsKey(num))
                map.put(num,1);
                //未出现过，键入并频次加一
            else
                map.put(num, map.get(num) + 1);
                //频次加一
        }

        ArrayList<Integer> list = new ArrayList<>();
        for(int num: nums2){
            if(map.containsKey(num)){
                list.add(num);
                map.put(num, map.get(num) - 1);  //频次减一
                if(map.get(num) == 0)
                    map.remove(num);             //移除元素频次为零的
            }
        }

        int[] res = new int[list.size()];
        for(int i = 0; i < list.size(); i++)
            res[i] = list.get(i);

        return res;
    }

    public int[] intersection(int[] nums1, int[] nums2){

        TreeSet<Integer> set = new TreeSet<>();
        for(int num: nums1)
            set.add(num);   //去重

        ArrayList<Integer> list = new ArrayList<>();
        for(int num : nums2){
            if(set.contains(num)){
                //无重复的set中是否包含num
                list.add(num);
                set.remove(num);
                //在set中除去已找到的元素，下次就找不到了
            }
        }

        int[] res = new int[list.size()];
        for(int i = 0; i < list.size(); i++)
            res[i] = list.get(i);

        return res;
    }
}
