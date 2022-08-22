**SpringBoot 的核⼼配置⽂件有⼏个？它们的区别是什么？**
SpringBoot 的核⼼配置⽂件是 application 和 boostrap 配置⽂件。 Application 配置⽂件这个容易
理解，主要⽤于 Spring boot 项⽬的⾃动化配置。
Bootstrap 配置⽂件有以下⼏个应⽤场景。
（1）使用 Spring Cloud Config 配置中心时，这时需要在 bootstrap 配置文件中添加连接到配置
中心的配置属性来加载外部配置中心的配置信息；
（2）一些固定的不能被覆盖的属性；
（3）一些加密/解密的场景；

**SpringBoot 的配置⽂件有哪⼏种格式？它们有什么区别？**
1．properties 和 yml，它们的区别主要是书写格式不同。
2．Properties： app.user.name= javastack
Yml：
app：
user：name： javastack
3．yml 格式不⽀持@PropertySource 注解导⼊配置。
4．properties 和 yml 中包含相同属性时，properties 文件优先级高于 yml 文件。

**什么是 YAML？**
YAML 是一种人类可读的数据序列化语言。它通常用于配置文件。与属性文件相比，如果我们
想要在配置文件中添加复杂的属性，YAML 文件就更加结构化，而且更少混淆。可以看出 YAML
具有分层配置数据。

**如何在自定义端口上运行 Spring Boot 应用程序？**
为了在自定义端口上运行 Spring Boot 应用程序，您可以在 application.properties 中指定端口。
server.port = 8090

**SpringBoot 的核⼼注解是哪个？它主要由哪⼏个注解组成的？**
启动类上⾯注解是@SpringBootApplication，它也是 SpringBoot 的核⼼注解，主要包含 了以下 3
个注解： 包 括 @ComponentScan ， @SpringBootConfiguration，@EnableAutoConfiguration。
@EnableAutoConfiguration 的 作 ⽤启 动 ⾃动 的 配 置 ， @EnableAutoConfiguration 注解就是
SpringBoot 根据你添加的 jar 包来配置你项⽬的默认配置，⽐如根据 spring-boot-starter-web， 来
判断你项⽬是否添加了 webmvc 和 tomcat，就会⾃动帮你配置 web 项⽬中所需要的默 配置。
@ComponentScan 扫 描 当 前 包 及 其 ⼦ 包 下 被 @Component ，@Controller ，
@Service ， @Repository 注解标记的类并纳⼊ spring 容器中进⾏管理。
@SpringBootConfiguration 继承⾃@Configuration，⼆者功能也⼀直，标注当前类是配置类， 并
会将当前类内声明的⼀个或多个以@Bean 注解标记的⽅法的实例纳⼊到 spring 容器中 并且实
例名就是⽅法名。

**SpringBoot 有哪几种读取配置的⽅式？**
SpringBoot 可以通过@PropertySource，@Value，@Environment，@ConfigurationProperties 来
绑定变量。

**开启 SpringBoot 特性有哪⼏种⽅式？**

1. 继承 spring-boot-starter-parent 项⽬。
2. 导⼊ spring-boot-dependencies 项⽬依赖。

**SpringBoot 需要独⽴的容器运⾏吗？**
可以不需要，内置了 Tomcat/Jetty 等容器。

**运⾏ SpringBoot 有哪⼏种⽅式？**
打包⽤命令或者放到容器中运⾏。
⽤ Maven/Gradle 插件运⾏。
直接执⾏ main ⽅法运⾏。

**SpringBoot ⾃动配置原理是什么？**
注解@EnableAutoConfiguraction，@Configuration，@ConditionalOnClass 就是⾃动配置 的
核⼼，⾸先它得是⼀个配置⽂件，其次根据类路径下是否有这个类取⾃动配置。

**你如何理解 SpringBoot 中的 Starters？**
Starters 可以理解为启动器，它包含了⼀系列可以集成到应⽤⾥⾯的依赖包，可以⼀站 式集
成 Spring 及其他技术，⽽不需要到处找示例代码和依赖包。如想使⽤ Spring JPA 访问 数据库，
只要加⼊ spring-boot-starter-data-jpa 启动器依赖就能使⽤了。

**如何在 SpringBoot 启动的时候运⾏⼀些特定的代码？**
可以实现接⼝ ApplicationRunner 或者 CommandLineRunner，这两个接⼝实现⽅式⼀样， 它
们都只提供了⼀个 run ⽅法。

**SpringBoot ⽀持哪些⽇志框架？推荐和默认的⽇志框架是哪个？**
SpringBoot ⽀持 Java Util Logging，Log4j2，Logback 作为⽇志框架，如果你使⽤ Starters 启
动器，SpringBoot 将使⽤ Logback 作为默认框架。

**SpringBoot 实现热部署有哪⼏种⽅式？**
主要有两种⽅式： Spring Loaded 和Spring-boot-devtools

**如何重新加载 Spring Boot 上的更改，而无需重新启动服务器？**
这可以使用 DEV 工具来实现。通过这种依赖关系，可以节省任何更改，嵌入式 tomcat 将重
新启动。Spring Boot 有一个开发工具（DevTools）模块，它有助于提高开发人员的生产力。Java
开发人员面临的一个主要挑战是将文件更改自动部署到服务器并自动重启服务器。开发人员可以
重新加载 Spring Boot 上的更改，而无需重新启动服务器。这将消除每次手动部署更改的需要。
Spring Boot 在发布它的第一个版本时没有这个功能。这是开发人员最需要的功能。DevTools 模
块完全满足开发人员的需求。该模块将在生产环境中被禁用。它还提供 H2 数据库控制台以更好
地测试应用程序。

**你如何理解 SpringBoot 配置加载顺序？**
在 SpringBoot ⾥⾯，可以使⽤以下⼏种⽅式来加载配置。
Properties ⽂件。
Yaml ⽂件。
系统环境变量。
命令⾏参数。

**SpringBoot 项⽬ jar 包打成 war 包需要什么？**

1. 去掉 pom.xml 的内置 tomcat
2. 在 pom.xml 中配置启动类，使⽤ spring-boot-maven-plugin 插件。
3. 修改打包⽅式为war⽅式。
4. 修改启动类，继承 SpringBootServletInitializer 类，然后重写⾥⾯的 configure ⽅法，设定为启
   动类。
5. 打包测试，通过命令 mvn clean package 打包。

**SpringBoot 怎么定义不同环境配置？**
在 SpringBoot 中多环境配置⽂件名需要满⾜ application-{profile}.properties 的格式，其 中
{profile}对应环境标识，⽐如： application-dev.properties：开发环境。
application-test.properties：测试环境。
application-prod.properties：⽣产环境。
⾄ 于 那 些 具 体 的 配 置 ⽂ 件 会 被 加 载 ，需 要 在 application.properties ⽂ 件 中
通 过 spring.profiles.active 属性来设置，其中对应{profile0 值}。如：spring.profiles.active=test

**springboot 中常用的 starter 的组件有哪些.**
spring-boot-starter-parent //boot 项目继承的父项目模块.
spring-boot-starter-web //boot 项目集成 web 开发模块.
spring-boot-starter-tomcat //boot 项目集成 tomcat 内嵌服务器.
spring-boot-starter-test //boot 项目集成测试模块.
mybatis-spring-boot-starter //boot 项目集成 mybatis 框架.
spring-boot-starter-jdbc //boot 项目底层集成 jdbc 实现数据库操作支持.
其他诸多组件，可到 maven 中搜索，或第三方 starter 组件到 github 上查询

**Spring Boot 中的监视器是什么？**
Spring boot actuator 是 spring 启动框架中的重要功能之一。Spring boot 监视器可帮助您访问生产
环境中正在运行的应用程序的当前状态。有几个指标必须在生产环境中进行检查和监控。即使一
些外部应用程序可能正在使用这些服务来向相关人员触发警报消息。监视器模块公开了一组可直
接作为 HTTP URL 访问的 REST 端点来检查状态。

**如何使用 Spring Boot 实现分页和排序？**
使用 Spring Boot 实现分页非常简单。使用 Spring Data-JPA 可以实现将可分页的
org.springframework.data.domain.Pageable 传递给存储库方法。

**springboot 与 spring 的区别.**
java 在集成 spring 等框架需要作出大量的配置，开发效率低，繁琐.所以官方提出 springboot 的
核心思想：习惯优于配置.可以快速创建开发基于 spring 框架的项目.或者支持可以不用或很少的
spring 配置即可。

**springboot 项目需要兼容老项目（spring 框架），该如何实现.**
集成老项目 spring 框架的容器配置文件即可：spring-boot 一般提倡零配置，但是如果需要配置，
也可增加：@ImportResource({“classpath：spring1.xml” ， “classpath：spring2.xml”})注意：
resources/spring1.xml 位置