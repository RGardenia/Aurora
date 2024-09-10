package com.atguigu.bigdata.spark.test;

public class Test7 {
    public static void main(String[] args) {

        String line = "{\"id\":1001, \"name\":\"zhangsan\", \"age\":30}";
        int age = 0;

        // TODO 数据格式：JSON
        //      1. 每一行就是一个JSON格式的数据，而且表示一个对象，对象内容必须包含在 {} 中
        //      2. 对象中的多个属性必须采用逗号隔开
        //      3. 每一个属性，属性名和属性值之间采用冒号隔开
        //      4. 属性名必须采用双引号声明，属性值如果为字符串类型，也需要采用双引号包含

        // TODO 去除大括号
        final String lineData = line.trim();
        final String attrsData = lineData.substring(1, lineData.length()-1);

        final String[] attrs = attrsData.split(",");
        for (String attr : attrs) {
            final String[] kv = attr.trim().split(":");
            for ( int i = 0; i < kv.length; i+=2 ) {
                if ( "\"age\"".equals(kv[i].trim()) ) {
                    age = Integer.parseInt(kv[i+1]);
                    break;
                }
            }
        }
//        return age;
    }
}
