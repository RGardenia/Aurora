package com.atguigu.bigdata.sparksql;

import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

public class SparkSQL09_Source_Req_3 {
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

        // TODO 补全数据
        /*

		select
			area,
		    product_id,
			product_name,
			city_name
		from (
			select
				click_product_id,
				city_id
			from user_visit_action
			where click_product_id != -1
		) a
		join (
			select
				product_id,
				product_name
			from product_info
		) p on a.click_product_id = p.product_id
		join (
			select
				city_id,
				city_name,
				area
			from city_info
		) c on a.city_id = c.city_id

         */
        sparkSession.sql("select\n" +
                "\tarea,\n" +
                "\tclick_product_id,\n" +
                "\tproduct_name,\n" +
                "\tcity_name\n" +
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
                "\tselect\n" +
                "\t\tcity_id,\n" +
                "\t\tcity_name,\n" +
                "\t\tarea\n" +
                "\tfrom city_info\n" +
                ") c on a.city_id = c.city_id").createOrReplaceTempView("t1");

        // TODO 对补全的数据分组聚合
        /*

        select
            *,
            count(*) clickCnt,
            cityRemark(city_name) cityremark
        from t1
        group by area, product_id, product_name

         */
        sparkSession.sql("select\n" +
                "\t*,\n" +
                "\tcount(*) clickCnt,\n" +
                "\tcityRemark(city_name) cityremark\n" +
                "from t1\n" +
                "group by area, product_id, product_name").createOrReplaceTempView("t2");

        // TODO 给每一行数据增加排序号
        /*

         select
            *,
            rank() over ( partition by area order by clickCnt desc ) rk
         from t2

         */
        sparkSession.sql("select\n" +
                "\t*,\n" +
                "\trank() over ( partition by area order by clickCnt desc ) rk\n" +
                "from t2").createOrReplaceTempView("t3");

        // TODO 根据排序号保留组内前3名
        sparkSession.sql("select * from t3 where rk <= 3").show();


        // TODO 释放资源
        sparkSession.close();

    }
}
