# Arthas

Arthas 是 Albaba 在 2018年9月开源的 Java 诊断工具。支持JDK6+，采用命令行交互模式，可以方便的定位和诊断线上程序运行问题。Arthas 官方文档十分详细，详见:https://alibaba.github.io/arthas



`thread -b` 

`jad com.tuling.jvm.ArthasTest`

使用 ognl 命令可以查看线上系统变量的值，可以修改变量的值



`java -Xms3G -Xmx3G -Xss1M -XX:MetaspaceSize=512M -X%:MaxMetaspaceSize=512M -jar microservice-eureka-serverjar`