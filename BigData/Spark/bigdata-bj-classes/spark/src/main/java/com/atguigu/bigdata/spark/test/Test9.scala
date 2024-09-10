package com.atguigu.bigdata.spark.test

object Test9 {

    def main(args: Array[String]): Unit = {

        val rdd = Seq(1, 2, 3, 4)

        rdd.reduce(
            (num1, num2) => {
                return num1 + num2;
            }
        )


    }
}
