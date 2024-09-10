package com.atguigu.bigdata.sparksql;

import org.apache.spark.sql.SparkSession;

public class SparkSQL09_Source_Req_1 {
    public static void main(String[] args) {

        // TODO 在编码前，设定Hadoop的访问用户
        System.setProperty("HADOOP_USER_NAME","atguigu");

        final SparkSession sparkSession = SparkSession
                .builder()
                .enableHiveSupport() // TODO 启用Hive的支持
                .master("local[*]")
                .appName("SparkSQL")
                .getOrCreate();

        sparkSession.sql("select\n" +
                "\tarea,\n" +
                "\tproduct_name,\n" +
                "\tcount(*)\n" +
                "from (\n" +
                "\tselect\n" +
                "\t\tclick_product_id,\n" +
                "\t\tcity_id\n" +
                "\tfrom user_visit_action\n" +
                "\twhere click_product_id != -1\n" +
                ") a\n" +
                "join (\n" +
                "\tselect\n" +
                "\t\tproduct_id,\n" +
                "\t\tproduct_name\n" +
                "\tfrom product_info\n" +
                ") p on a.click_product_id = p.product_id\n" +
                "join (\n" +
                "    select\n" +
                "\t\tcity_id,\n" +
                "\t\tcity_name,\n" +
                "\t\tarea\n" +
                "\tfrom city_info\n" +
                ") c on a.city_id = c.city_id\n" +
                "group by area, product_id, product_name\n" +
                "limit 10").show();


        // TODO 释放资源
        sparkSession.close();

    }
}
