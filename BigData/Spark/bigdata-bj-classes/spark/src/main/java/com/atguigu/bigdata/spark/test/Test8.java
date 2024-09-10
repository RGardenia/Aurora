package com.atguigu.bigdata.spark.test;

import java.util.*;

public class Test8 {
    public static void main(String[] args) {

        Map<String, Long> map1 = new HashMap<String, Long>();
        map1.put("bj", 10L);
        map1.put("tj", 20L);
        map1.put("bd", 30L);


        Map<String, Long> map2 = new HashMap<String, Long>();
        map2.put("bj", 40L);
        map2.put("tj", 50L);
        map2.put("sjz", 60L);

        final Iterator<String> iterator = map2.keySet().iterator();
        while ( iterator.hasNext() ) {
            final String key = iterator.next();

            final Long v1 = map1.get(key);
            final Long v2 = map2.get(key);
            if ( v1 == null ) {
                map1.put( key, v2 );
            } else {
                map1.put( key, v1 + v2 );
            }
        }

        System.out.println(map1);

    }
}
