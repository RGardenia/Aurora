package com.atguigu.bigdata.sparksql;

import org.apache.spark.sql.SparkSession;

public class SparkSQL09_Source_Req {
    public static void main(String[] args) {

        // TODO 在编码前，设定Hadoop的访问用户
        System.setProperty("HADOOP_USER_NAME","atguigu");

        final SparkSession sparkSession = SparkSession
                .builder()
                .enableHiveSupport() // TODO 启用Hive的支持
                .master("local[*]")
                .appName("SparkSQL")
                .getOrCreate();

        //sparkSession.sql("show tables").show();

//        sparkSession.sql("CREATE TABLE `user_visit_action`(\n" +
//                "  `date` string,\n" +
//                "  `user_id` bigint,\n" +
//                "  `session_id` string,\n" +
//                "  `page_id` bigint,\n" +
//                "  `action_time` string,\n" +
//                "  `search_keyword` string,\n" +
//                "  `click_category_id` bigint,\n" +
//                "  `click_product_id` bigint, --点击商品id，没有商品用-1表示。\n" +
//                "  `order_category_ids` string,\n" +
//                "  `order_product_ids` string,\n" +
//                "  `pay_category_ids` string,\n" +
//                "  `pay_product_ids` string,\n" +
//                "  `city_id` bigint --城市id\n" +
//                ")\n" +
//                "row format delimited fields terminated by '\\t';");
//
//        sparkSession.sql("load data local inpath 'data/user_visit_action.txt' into table user_visit_action;");
//
//        sparkSession.sql("CREATE TABLE `city_info`(\n" +
//                "  `city_id` bigint, --城市id\n" +
//                "  `city_name` string, --城市名称\n" +
//                "  `area` string --区域名称\n" +
//                ")\n" +
//                "row format delimited fields terminated by '\\t';");



        sparkSession.sql("CREATE TABLE `product_info`(\n" +
                "  `product_id` bigint, -- 商品id\n" +
                "  `product_name` string, --商品名称\n" +
                "  `extend_info` string\n" +
                ")\n" +
                "row format delimited fields terminated by '\\t';");

        sparkSession.sql("load data local inpath 'data/city_info.txt' into table city_info;");
        sparkSession.sql("load data local inpath 'data/product_info.txt' into table product_info;");

        sparkSession.sql("select * from city_info limit 10").show();

        // TODO 需求：
        //    Spark Core :      热门品类Top10
        //    Spark SQL  : 各区域热门商品Top3

        // TODO 1. 如果需求中有【各个XXXX】描述
        //      表述的基本含义就是相同的数据分在一个组中 ：group by
        // TODO 2. 热门：只从点击量统计 => (商品ID, 点击数量)
        // TODO 3. Top3: （组内）排序后取前3名

        /*

        区域      商品      点击数量    排序号
        -----------------------------------
        华北      鞋       5000        1
        华北      鞋       5000        1
        华北      鞋       5000        1
        华北      鞋       5000        1
        华北      鞋       5000        1
        华北      衣服     3500        2
        华北      帽子     1500        3
        东北      鞋       6000        1
        东北      手机     5500        2
        东北      电脑     5200        3

        ---------------------------------------------------------------------
        区域      商品      城市      点击数量     总的点击数量     比率      顺写号
        ---------------------------------------------------------------------
        华北      鞋       北京      5000        14000         5/14      1
        华北      鞋       天津      4000        14000         4/14      2
        华北      鞋       保定      3000        14000         3/14      3
        华北      鞋       石家庄    2000        14000         2/14      4

        1. 行转列？
        2. 如何拼接结果

        SQL不能做或不好做的功能，可以采用自定义 UDF，UDAF 函数来实现

        需求的实现方式：SQL + UDAF


         */



        // TODO 释放资源
        sparkSession.close();

    }
}
