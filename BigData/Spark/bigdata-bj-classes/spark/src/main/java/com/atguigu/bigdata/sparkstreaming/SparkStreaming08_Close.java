package com.atguigu.bigdata.sparkstreaming;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;

public class SparkStreaming08_Close {
    public static void main(String[] args) throws Exception {

        SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("SparkStreaming");
        final JavaStreamingContext jsc = new JavaStreamingContext(conf, new Duration(3 * 1000));

        // TODO 通过环境对象对接Socket数据源，获取数据模型，进行数据处理
        final JavaReceiverInputDStream<String> socketDS = jsc.socketTextStream("localhost", 9999);
        socketDS.print();

        //jsc.close(); (X)

        jsc.start();

        boolean flg = false;

        // TODO close方法就是用于释放资源，关闭环境，但是不能在当前main方法（线程）中完成
        new Thread(new Runnable() {
            @Override
            public void run() {
                while ( true ) {
                    // TODO 关闭环境，释放资源
                    try {
                        // 关闭SparkStreaming的时候，需要在程序运行的过程中，通过外部操作进行关闭
                        Thread.sleep(5000);
                        if ( flg ) {
                            //jsc.close();//      强制地关闭
                            jsc.stop();//      强制地关闭
                            // jsc.stop(true, true);//      优雅地关闭(stop)
                        }
                    } catch ( Exception e ) {
                        //e.printStackTrace();
                    }
                }
            }
        }).start();
        //jsc.close(); (X)

        jsc.awaitTermination(); // await方法表示当前代码执行到此处后会阻塞直到采集器的中止（结束）

        // TODO close方法用于释放资源
        // jsc.close(); (X)

    }
}
