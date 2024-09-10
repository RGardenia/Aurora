package com.atguigu.bigdata.sparksql;

import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

public class SparkSQL09_Source_Req_2 {
    public static void main(String[] args) {

        // TODO 在编码前，设定Hadoop的访问用户
        System.setProperty("HADOOP_USER_NAME","atguigu");

        final SparkSession sparkSession = SparkSession
                .builder()
                .enableHiveSupport() // TODO 启用Hive的支持
                .master("local[*]")
                .appName("SparkSQL")
                .getOrCreate();

        sparkSession.udf().register("cityRemark", functions.udaf(
                new MyCityRemarkUDAF(), Encoders.STRING()
        ));

        sparkSession.sql("select\n" +
                "\t*\n" +
                "from (\n" +
                "\tselect\n" +
                "\t\t*,\n" +
                "\t\trank() over ( partition by area order by clickCnt desc ) rk\n" +
                "\tfrom (\n" +
                "\t\tselect\n" +
                "\t\t\tarea,\n" +
                "\t\t\tproduct_name,\n" +
                "\t\t\tcount(*) clickCnt,\n" +
                "\t\t\tcityRemark(city_name) cityremark\n" +
                "\t\tfrom (\n" +
                "\t\t\tselect\n" +
                "\t\t\t\tclick_product_id,\n" +
                "\t\t\t\tcity_id\n" +
                "\t\t\tfrom user_visit_action\n" +
                "\t\t\twhere click_product_id != -1\n" +
                "\t\t) a\n" +
                "\t\tjoin (\n" +
                "\t\t\tselect\n" +
                "\t\t\t\tproduct_id,\n" +
                "\t\t\t\tproduct_name\n" +
                "\t\t\tfrom product_info\n" +
                "\t\t) p on a.click_product_id = p.product_id\n" +
                "\t\tjoin (\n" +
                "\t\t\tselect\n" +
                "\t\t\t\tcity_id,\n" +
                "\t\t\t\tcity_name,\n" +
                "\t\t\t\tarea\n" +
                "\t\t\tfrom city_info\n" +
                "\t\t) c on a.city_id = c.city_id\n" +
                "\t\tgroup by area, product_id, product_name\n" +
                "\t) t\n" +
                ") t1 where rk <= 3").show();


        // TODO 释放资源
        sparkSession.close();

    }
}
