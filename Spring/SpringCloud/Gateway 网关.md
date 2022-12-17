# 06、Gateway 新一代网关

# 一、Gateway概述

## 1、Gateway是什么

gateway 官网：https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.1.RELEASE/reference/html/



Cloud全家桶中有个很重要的组件就是网关，在1.x版本中都是采用Zuul网关；

但在2.x版本中，zuul的升级就是一直跳票，SpringCloud最后自己研发了一个网关代替Zuul

那就是 SpringCloud Gateway  ，gateway是zuul 1.x版本的替代。



Gateway是在Spring生态系统之上架构的API网关服务，基于Spring 5,Spring Boot2 和Project Reactor技术。

Gateway旨在提供一种简单而有效的方式来对API进行路由，以及提供一些强大的过滤器功能，例如：熔断、限流、重试等。



SpringCloud Gateway作为Spring cloud生态系统中的网关，目标是代替 Zuul，在SpringCloud2.0以上版本中，没有对新版本的Zuul 2.0以上实现最新高性能版本进行集成，仍然还是使用的Zuul 1.x非Reactor模式的老版本。而为了提升网关的性能，SpringCloud Gateway是基于WebFlux框架实现的，而WebFlux框架底层则使用了高性能的Reactor模式通信框架Netty，【说穿了就是 SpringCloud Gateway是异步非阻塞式】

## 2、Gateway能干什么

- 反向代理
- 鉴权

- 流量控制
- 熔断

- 日志监控

## 3、微服务架构中网关在哪里

<img src="Gateway 网关.assets/image-20210726115107167.png" alt="image-20210726115107167" style="zoom:50%;" />



## 4、SpringCloud Gateway具有的特征

- 基于Spring Frameword5 ,Project Reactor 和 SpringBoot 2.0进行构建；
- 动态路由：能够匹配任何请求属性

- 可以对路由指定Predicate(断言)和Filter（过滤器）
- 集成Hystrix的断路器功能；

- 集成Spring Cloud的服务发现功能
- 易于编写的Predicate（断言）和Filter（过滤器）

- 请求限流功能；
- 支持路径重写



## 5、SpringCloud Gateway与zuul的区别

在SpringCloud Finchley 正式版之前（现在H版），SpringCloud推荐的网关是Netflix提供的zuul。



1. Zuul1.x 是一个基于阻塞 I/O的API网关
2. Zuul1.x 基于Servlet2.5使用阻塞架构它不支持任何长连接 （如WebSocket）Zuul的设计模式和Nginx较像，每次I/O操作都是从工作线程中选择一个执行，请求线程被阻塞到工作线程完成，但是差别是Nginx用C++实现，Zuul用java实现，而JVM本身会有第一次加载较慢的情况，使得Zuul的性能相对较差。



1. Zuul 2.x理念更加先进，像基于Netty非阻塞和支持长连接，但SpringCloud目前还没有整合。Zuul2.x的性能较Zuul 1.x有较大的提升。在性能方面，根据官方提供的基准测试，Spring Cloud Gateway的RPS（每秒请求次数）是Zuul的1.6倍。
2. Spring Cloud Gateway建立在Spring Framework 5、project Reactor和Spring Boot2 之上，使用非阻塞API

1. Spring Cloud Gateway 还支持WebSocket，并且与Spring紧密集成拥有更好的开发体验。



# 二、Gateway的三大核心概念

## 1、Route路由

路由是构建网关的基本模块，它由ID，目标URI，一系列的断言和过滤器组成，如果断言为true则匹配该路由。

## 2、Predicate 断言

开发人员可以匹配Http请求中的所有内容（例如请求头或者请求参数），如果请求参数与断言相匹配则进行路由。

## 3、Filter 过滤

指的是Spring框架中的GatewayFilter的实例，使用过滤器，可以在请求被路由前或者之后对请求进行修改。

## 4、总结

- web 请求，通过一些匹配条件，定位到真正的服务节点，并在这个转发过程的前后，进行一些精细化控制
- predicate 就是我们的匹配条件

- filter：就可以理解为一个无所不能的拦截器，有了这两个元素，再加上目标的uri，就可以实现一个具体的路由了。

![image-20210726115142068](Gateway 网关.assets/image-20210726115142068.png)

# 三、Spring Cloud Gateway工作流程

<img src="Gateway 网关.assets/image-20210726115159583.png" alt="image-20210726115159583" style="zoom:50%;" />

- 客户端向Spring Cloud Gateway发出请求，然后再Gateway Handler Mapping 中找到与请求相匹配的路由，将其发生到Gateway Web Handler
- Handler 再通过指定的的过滤器链来讲请求发送到我们实际的服务执行业务逻辑，然后返回。

- 过滤器之间使用虚线分开是因为过滤器可能会在发送代理请求之前（"pre"） 或之后("post")执行业务逻辑。
- Filter在“pre”类型的过滤器可以做**参数校验，权限校验，流量监控，日志输出，协议转换**等。

- 在“post”类型的过滤器可以做**响应内容、响应头的修改，日志的输出，流量监控**等有者非常重要的作用。

核心逻辑：路由转发+执行过滤链



# 四、入门配置

## 1、创建cloud-gateway-gateway-9527 模块

## 2、pom

做网关不需要添加  web starter

```xml
<dependencies>
  <!--新增gateway-->
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
  </dependency>
  <dependency>
    <groupId>com.yixuexi.springcloud</groupId>
    <artifactId>cloud-api-commons</artifactId>
    <version>1.0-SNAPSHOT</version>
  </dependency>

  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
  </dependency>

  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
  </dependency>

</dependencies>
```

## 3、yaml 配置

```yaml
server:
  port: 9527
spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      routes:
        - id: payment_routh #路由的ID，没有固定规则但要求唯一，建议配合服务名
          uri: http://localhost:8001   #匹配后提供服务的路由地址
          predicates:
            - Path=/get/payment/**   #断言,路径相匹配的进行路由
        # 上面表示 如果要访问http://localhost:8001/get/payment/** 需要
        # http://localhost:9527/get/payment/**
        
        - id: payment_routh2
          uri: http://localhost:8001
          predicates:
            - Path=/get/lb/**   #断言,路径相匹配的进行路由

eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
```

## 4、主启动类

```java
@EnableEurekaClient
@SpringBootApplication
public class GatewayMain9527 {
    public static void main(String[] args) {
        SpringApplication.run(GatewayMain9527.class,args);
    }
}
```

## 5、测试

启动网关前访问：http://localhost:**8001**/get/payment/1

启动网关后访问：http://localhost:**9527**/get/payment/1





## 五、Gateway的网关配置

两种配置：

### 1、在配置文件中yaml中配置

```yaml
spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      routes:
        - id: payment_routh #路由的ID，没有固定规则但要求唯一，建议配合服务名
          uri: http://localhost:8001   #匹配后提供服务的路由地址
          predicates:
            - Path=/get/payment/**   #断言,路径相匹配的进行路由
        # 上面表示 如果要访问http://localhost:8001/get/payment/** 需要
        # http://localhost:9527/get/payment/**
        
        - id: payment_routh2
          uri: http://localhost:8001
          predicates:
            - Path=/get/lb/**   #断言,路径相匹配的进行路由
```

### 2、代码中注入RouteLocator的bean

官网案例：

<img src="Gateway 网关.assets/image-20210726115302251.png" alt="image-20210726115302251" style="zoom:50%;" />

## 六、配置动态路由

相当于给网关配置一个负载均衡，因为看上面的配置把8001写死了

默认情况下Gateway会根据注册中心注册的服务列表，以注册中心上微服务名为路径创建动态路由进行转发，从而实现动态路由的功能



开启动态路由：`spring.cloud.gateway.discovery.locator.enabled:true;`

在添加uri的时候，开头是 lb://微服务名

```yaml
spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      routes:
        - id: payment_routh #路由的ID，没有固定规则但要求唯一，建议配合服务名
          # uri: http://localhost:8001   #匹配后提供服务的路由地址
          uri: lb://CLOUD-PAYMENT-SERVICE # lb 为负载均衡
          predicates:
            - Path=/get/payment/**   #断言,路径相匹配的进行路由

        - id: payment_routh2
          # uri: http://localhost:8001
          uri: lb://CLOUD-PAYMENT-SERVICE
          predicates:
            - Path=/get/lb/**   #断言,路径相匹配的进行路由
      discovery:
        locator:
          enabled: true   #开启从注册中心动态创建路由的功能，利用微服务名进行路由
```

**测试**

因为开启了8001和8002两个端口，所以网关负载均衡的效果是 8001/8002切换



# 七、Predicate 断言的使用

gateway启动时打印的信息

<img src="Gateway 网关.assets/image-20210726115338036.png" alt="image-20210726115338036" style="zoom: 50%;" />

Spring Cloud Gateway 将路由匹配作为Spring WebFlux Handler Mapping基础架构的一部分。

Spring Cloud Gateway 包括许多内置的Route Predicate 工厂，所有的这些Predicate都和Http请求的不同属性匹配，多个Route Predicate可以进行组合。



Spring Cloud Gateway 创建route对象时，使用RoutePredicateFactory创建Predicate对象，Predicate对象可以赋值给Route，SpringCloud Gateway包含许多内置的Route Predicate Factories.



所有的 这些谓词都匹配Http的请求的各种属性，多种谓词工厂可以组合，并通过逻辑and



官网对gateway的断言每个都写了栗子：https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.1.RELEASE/reference/html/#the-after-route-predicate-factory



## 1、常用的断言

常用的`Route Predicate`



**After Route Predicate**

- `- After=2020-03-08T10:59:34.102+08:00[Asia/Shanghai]`
- 匹配该断言时间之后的 uri请求



**Before Route Predicate**

- `- After=2020-03-08T10:59:34.102+08:00[Asia/Shanghai]`

```
- Before=2020-03-08T10:59:34.102+08:00[Asia/Shanghai]
```



**Between Route Predicate**

- `- Between=2020-03-08T10:59:34.102+08:00[Asia/Shanghai] ,  2020-03-08T10:59:34.102+08:00[Asia/Shanghai]`

**Cookie Route Predicate**

不带cookies访问

带上cookies访问  `- Cookie=username,atguigu   #并且Cookie是username=zhangshuai才能访问`

Cookie Route Predicate 需要两个参数，一个时Cookie name，一个是正则表达式。

路由规则会通过获取对应的Cookie name值和正则表达式去匹配，如果匹配上就会执行路由，如果没有匹配上就不执行

**Header Route Predicate**

两个参数：一个是属性名称和一个正则表达式，这个属性值和正则表达式匹配则执行；



**Host Route Predicate**

- `- Host=**.atguigu.com`



**Method Route Predicate**

- `- Method=GET`

**Path Route Predicate**



 **Query Route Predicate**

- `- Query=username, \d+ #要有参数名称并且是正整数才能路由`



## 2、小总结

- 说白了，Predicate就是为了实现一组匹配规则，让请求过来找到对应的Route进行处理

```yaml
server:
  port: 9527
spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true  #开启从注册中心动态创建路由的功能，利用微服务名进行路由
      routes:
        - id: payment_routh #路由的ID，没有固定规则但要求唯一，建议配合服务名
          #uri: http://localhost:8001   #匹配后提供服务的路由地址
          uri: lb://cloud-payment-service
          predicates:
            - Path=/payment/get/**   #断言,路径相匹配的进行路由
 
        - id: payment_routh2
          #uri: http://localhost:8001   #匹配后提供服务的路由地址
          uri: lb://cloud-payment-service
          predicates:
            - Path=/payment/lb/**   #断言,路径相匹配的进行路由
            #- After=2020-03-08T10:59:34.102+08:00[Asia/Shanghai]
            #- Cookie=username,zhangshuai #并且Cookie是username=zhangshuai才能访问
            #- Header=X-Request-Id, \d+ #请求头中要有X-Request-Id属性并且值为整数的正则表达式
            #- Host=**.atguigu.com
            #- Method=GET
            #- Query=username, \d+ #要有参数名称并且是正整数才能路由
 
 
eureka:
  instance:
    hostname: cloud-gateway-service
  client:
    service-url:
      register-with-eureka: true
      fetch-registry: true
      defaultZone: http://eureka7001.com:7001/eureka
```

# 八、Filter的使用

## 1、Filter是什么

路由过滤器可用于修改进入的Http请求和返回的Http响应，路由过滤器只能过滤指定路由进行使用。



Spring Cloud Gateway 内置了多种路由过滤器，他们都由GatewayFilter的工厂类来产生



官网：https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.1.RELEASE/reference/html/#gatewayfilter-factories



**声明周期 ：** 

**pre  在业务逻辑之前**

**post  在业务逻辑之后**



**种类：**

**单一的：GatewayFilter**

**全局的：GlobalFilter**

## 2、自定义过滤器

### 2.1 两个接口

impiemerts  `GlobalFilter`，`Ordered`

### 2.2 能干吗

全局日志记录

统一网关鉴权



### 2.3 示例代码

```java
package com.atguigu.springcloud.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
@Slf4j
public class MyLogGateWayFilter implements GlobalFilter,Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info("*********come in MyLogGateWayFilter: "+new Date());
        String uname = exchange.getRequest().getQueryParams().getFirst("username");
        if(StringUtils.isEmpty(username)){
            log.info("*****用户名为Null 非法用户,(┬＿┬)");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);//给人家一个回应
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
```

