package com.atguigu.bigdata.sparksql;

import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.expressions.Aggregator;

import java.util.*;

public class MyCityRemarkUDAF extends Aggregator<String, MyCityRemarkBuffer, String> {
    @Override
    public MyCityRemarkBuffer zero() {
        return new MyCityRemarkBuffer(0L, new HashMap<String, Long>());
    }

    @Override
    // TODO 将函数的输入值和缓冲区的数据进行聚合处理
    public MyCityRemarkBuffer reduce(MyCityRemarkBuffer buffer, String city) {
        buffer.setCount( buffer.getCount() + 1 );
        final Map<String, Long> cityMap = buffer.getCityMap();
        final Long cityCount = cityMap.get(city);
        if ( cityCount == null ) {
            cityMap.put(city, 1L);
        } else {
            cityMap.put(city, cityCount + 1);
        }
        buffer.setCityMap(cityMap);
        return buffer;
    }

    @Override
    // TODO 合并缓冲区
    public MyCityRemarkBuffer merge(MyCityRemarkBuffer b1, MyCityRemarkBuffer b2) {

        b1.setCount( b1.getCount() + b2.getCount() );

        final Map<String, Long> map1 = b1.getCityMap();
        final Map<String, Long> map2 = b2.getCityMap();
        /*
           c1 : { beijing : 10, tianjin : 20, baoding : 30 }
           c2 : { beijing : 40, tianjin : 50, sjz : 60 }
           ------------------------------------------------
           c3 : { beijing : 50, tianjin : 70, baoding : 30, sjz : 60 }

           1. 将 map1 保持不变
           2. 对 map2 进行遍历
           3. 如果 map2 中的 key 在 map1 存在，那么合并数据
           4. 如果 map2 中的 key 不在 map1 存在，那么直接添加即可
         */
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
        b1.setCityMap(map1);
        return b1;
    }

    @Override
    public String finish(MyCityRemarkBuffer buffer) {
        StringBuilder ss = new StringBuilder();

        final Long total = buffer.getCount();
        final Map<String, Long> cityMap = buffer.getCityMap();
        List<CityCount> ccs = new ArrayList<CityCount>();
        cityMap.forEach(
            (k, v) -> {
                ccs.add( new CityCount(k, v) );
            }
        );
        //TODO 对List进行排序
        Collections.sort(ccs);

        final CityCount cityCount0 = ccs.get(0);
        final long pc0 = cityCount0.getCount() * 100 / total; // 10 * 100/20 => 50
        ss.append(cityCount0.getCityName() + " "+ pc0 +"%");


        final CityCount cityCount1 = ccs.get(1);
        final long pc1 = cityCount1.getCount() * 100 / total; // 10 * 100/20 => 50
        ss.append(cityCount1.getCityName() + " "+ pc1 +"%");

        if ( ccs.size() > 2 ) {
            ss.append("其他 "+(100 - pc0 - pc1)+"%");
        }

        return ss.toString();
    }

    @Override
    public Encoder<MyCityRemarkBuffer> bufferEncoder() {
        return Encoders.bean(MyCityRemarkBuffer.class);
    }

    @Override
    public Encoder<String> outputEncoder() {
        return Encoders.STRING();
    }
}
