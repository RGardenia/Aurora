package com.atguigu.gmall.flume.interceptor;

import com.alibaba.fastjson.JSONObject;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import java.util.List;
import java.util.Map;

/**
 * TODO 自定义FLume拦截器
 *    可以对FLume采集的数据进行拦截操作
         1 ： 校验数据的JSON格式是否正确
         2 ： 生成数据动态时间
 */
public class TimestampInterceptor implements Interceptor {

    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        // 1、获取header和body的数据
        Map<String, String> headers = event.getHeaders();
        String json = new String(event.getBody(), StandardCharsets.UTF_8);

        try {
            //2、将body的数据类型转成jsonObject类型（方便获取数据）
            JSONObject jsonObject = JSONObject.parseObject(json);

            //3、header中timestamp时间字段替换成日志生成的时间戳（解决数据漂移问题）
            //   不使用Flume采集的系统时间，需要使用数据自带的时间
            //   从日志数据中获取ts时间戳字段，替换系统默认时间。
            String ts = jsonObject.getString("ts");
            headers.put("timestamp", ts);

            return event;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        Iterator<Event> iterator = list.iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (intercept(event) == null) {
                iterator.remove();
            }
        }
        return list;
    }

    @Override
    public void close() {

    }

    // SparkSQL
    //    构建器模式
    //        SparkSession.build().setMaster.setAppName.getOrCreate();
    //
    public static class Builder implements Interceptor.Builder {
        @Override
        public Interceptor build() {
            return new TimestampInterceptor();
        }

        @Override
        public void configure(Context context) {
        }
    }
}