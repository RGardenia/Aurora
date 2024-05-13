# **Jenkins**

在当今软件开发领域，持续集成和持续交付（CI/CD）已经成为了提高开发效率、保证软件质量以及加速软件交付的关键实践。而Jenkins作为最流行的开源CI/CD工具之一，扮演着至关重要的角色。Jenkins提供了丰富的功能和灵活的配置选项，使得开发团队能够轻松地构建、测试和部署他们的应用程序。

通常，Jenkins 通常作为独立应用程序在其自己的进程中运行。 Jenkins WAR 内置了Winstone（一个Jetty servlet 容器包装器），并且可以在具有 Jenkins 支持的 Java 版本的任何操作系统上运行。

理论上，`Jenkins`也可以在传统的servlet容器如Apache Tomcat或WildFly中运行，但实际上在这些容器上并未进行充分的测试验证，并且有许多注意事项。



# Install

### **最低的硬件要求**

**RAM**：256MB

**存储**：1GB的驱动空间（如果将Jenkins作为Docker容器运行建议至少10GB）

### **依赖要求**

运行Jenkins需要以下Java版本：

| 支持的Java版本             | 长期支持（LTS）发布   | 每周发布           |
| :------------------------- | :-------------------- | :----------------- |
| Java 11、Java 17或Java 21  | 2.426.1（2023年11月） | 2.419（2023年8月） |
| Java 11或Java 17           | 2.361.1（2022年9月）  | 2.357（2022年6月） |
| Java 8、Java 11 或 Java 17 | 2.346.1（2022年6月）  | 2.340（2022年3月） |
| Java 8或Java 11            | 2.164.1（2019年3月）  | 2.164（2019年2月） |

如果您安装了不受支持的Java版本，Jenkins控制器将无法运行。

以下将以Docker方式、Linux 服务、Windows上安装Jenkins安装包、macOS上等多操作系统多方式安装Jenkins的LTS(长期支持)版本

## **通过Docker的方式安装**

### **安装Jenkins**

Docker可以快速安装Jenkins，在Windows、macOS上可以通过安装Docker Desktop的方式安装Docker。

安装好Docker后在macOS和Linux上

```bash
docker run -d \
--name jenkins \
-p 8010:8080 -p 50000:50000 \
-v jenkins_home:/var/jenkins_home \
--restart=unless-stopped \
jenkins/jenkins:lts-jdk17
```

参数含义:

`-d`: 在后台运行容器

`--name` jenkins: 指定容器的名字为jenkins

`-p`: 将容器的8080端口绑定宿主机的8010端口

`-v`: 将容器的/var/jenkins_home目录映射到Docker的jenkins_home卷

`--restart=unless-stopped`: 指定容器在非人为停止的情况下发生异常退出时自动重启

`jenkins/jenkins:lts-jdk17`: 使用的 Jenkins 镜像，这里是 Jenkins 官方提供的 LTS 版本镜像，并且包含了 JDK 17。

如果本地不包含jenkins/jenkins:lts-jdk17镜像则会先进行镜像拉取，拉取完成则会启动一个名为`jenkins`容器



### **配置Jenkins**

使用上述`Docker`的方式下载镜像、安装并运行`Jenkins`后，我们还需要完成Jenkins设置向导后便可以使用`Jenkins`。

设置向导将引导完成插件列表、创建第一个管理员用户、设置Jenkins的代理地址等几个快速的“一次性”步骤来解锁 `Jenkins`

当首次访问新安装的`Jenkins`实例时，系统会要求使用`jenkins`自动生成的密码进行解锁才能进行下一步操作，而通过Docker方式安装的`Jenkins`可通过`docker logs`命令来获取jenkins容器的日志从而获得密码

1、访问浏览器的`http://<ip>:8010` (安装 Jenkins 时为 Jenkins 配置的端口）并等待`解锁Jenkins`页面出现

（1）将上述ip换成你自己的ip

（2）将上述的端口调整为你自己设置的宿主机端口

2、获取初始化密码(此密码在设置完Jenkins用户后就没用了)从 Jenkins 控制台日志输出中（在 2 组星号之间）

执行`docker logs <jenkins>`

将其中`jenkins`替换为你自己起的容器名称或者通过docker ps 获取容器的id

在 Docker 中运行 Jenkins也可以通过如下命令将Jenkins的密码打印在控制台上而无需在容器中执行查看日志命令

3、插件安装

插件安装可以选择使用`安装推荐的插件`或`选择插件来安装`两种方式，其中选择`选择插件来安装`中默认勾选了`安装推荐插件`中的插件，如果没有想要自己想要使用什么插件可直接选择`安装推荐的插件`方式进行安装

选择完成后进行插件安装，此步骤可以看到安装的插件列表以及插件的安装进度(后期不需要的插件可以卸载)

4、创建第一个管理员账号

在插件安装完成后就可以设置第一个管理员用户了，需要输入用户名、密码以及邮箱等信息

5、设置jenkins URL地址

6、完成安装

## **Linux下安装Jenkins服务**

在Linux下安装`Jenkins`服务需要依赖服务器上的Java环境，且目前最低Java版本为Java11，推荐使用`Java17`。很多小伙伴可能担心服务器上的应用程序使用的JDK8直接升级为JAVA17后会出现应用程序不兼容的问题，我们可以通过`alternatives`来解决

### **安装JDK17**

例如我Linux服务器上的JDK的版本为1.8,此时我需要安装JDK17以便能够正常运行Jenkins的LTS版本

1、安装OpenJDK17

```bash
yum install fontconfig java-17-openjdk
```

2、如遇到CentOS源无openjdk17可以使用如下方式安装

```bash
yum -y install wget vim
wget https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.rpm
sudo yum -y install ./jdk-17_linux-x64_bin.rpm
yum install fontconfig
```

3、切换JDK版本

如果本地已经安装了其它版本的JDK，可能在使用java -version输出java 版本信息不会显示最新的JDK17

执行如下命令:

```bash
alternatives --config java
```

4、其它方式

方式一： 通过上传JDK17的压缩包，解压后记录JDK17的路径(不需要配置环境变量);

方式二：通过yum install java-11-openjdk后在`/usr/lib/jvm`下找到openJDK11的路径，通过修改jenkins.sevice中的JAVA_HOME来设置JDK的路径(下面会介绍)，注意LTS版本最低需要JDK11

### **安装Jenkins**

1、添加Jenkins源

```bash
sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io-2023.key
```

2、安装Jenkins

```bahs
yum install -y jenkins
```

3、Jenkins服务说明

从 Jenkins 2.335 和 Jenkins 2.332.1 开始，该包配置为使用`systemd`而不是较旧的 System init。也就是说LTS版本的Jenkins服务需要使用`systemctl`命令来进行管理

4、设置jenkins开启启动

```bash
systemctl enanle jenkins # 设置Jenkins服务开机启动，并创建jenkins.service
```

在创建的jekins.service文件中包含了jenkins的配置，但是默认安装的时候并不会创建这个jenkins.service文件。

5、启动jenkins

```
systemctl start jenkins # 启动
systemctl status jenkins # 查看服务状态
systemctl stop jenkins # 停止
```

6、获取Jenkinsd的密码

```
cat /var/lib/jenkins/secrets/initialAdminPassword
```

7、配置jerkins

jenkins默认的端口为8080，通过浏览器访问后即可与第一部中配置Jenkins同理

### **调整Jenkins(可选)**

1、使用JDK1.8启动Jenkins的LTS版本问题解决

```
journalctl -u jenkins # 查看jenkins 服务日志
```

java 8 启动异常信息如下

问题可能的原因：如果先安装了Jenkins并执行了启动命令在去调整的操作系统中Java的环境变量会导致jenkins无法生效

解决办法: 修改`jenkins.service`中的`JAVA_HOME`部分，增加`/usr/lib/jvm/jdk-17-oracle-x64/bin/java` (通过上述Java17 安装的默认位置)配置

```
vi /usr/lib/systemd/system/jenkins.service
# 增加如下内容
Environment="/usr/lib/jvm/jdk-17-oracle-x64/bin/java"
```

修改后重载systemctl服务以便jenkins.service的修改生效

```
systemctl daemon-reload 
```

2、调整Jenkins的启动端口号

jenkins 的默认端口号为8080，如果默认的端口号与其它应用冲突也可能会导致Jenkins无法正常启动。修改`jenkins.service`文件中的`JENKINS_PORT`

```
systemctl daemon-reload
```

3、防火墙问题

一般Linux上都会启动firwalld防火墙服务，当我们正常启动了一个Jenkins服务后可能在浏览器中无法访问，极大的原因可能是防火墙没有开放对应的端口

```
firewall-cmd --list-port # 查看当前防火墙域开发的端口
firewall-cmd --add-port=8030/tcp --permanent # 开放8030端口
firewall-cmd --reload # 重载防火墙以便上述端口开放生效
```

更多firewall-cmd命令可以参考:[Linux防火墙firewalld命令总结](http://mp.weixin.qq.com/s?__biz=MzI4NjUwMDk2NQ==&mid=2247488197&idx=1&sn=9d27f73de517a718a646bdf5f113b7ae&chksm=ebdaa3cfdcad2ad9b5e35d958957d67460d59459dc6834956cec1df4ca0001eeb2548b39aeda&scene=21#wechat_redirect)

## **离线安装Jenkins**

离线安装Jenkins需要先下载jenkins的最新war包，并通过java -jar 命令启动

1、下载Jenkins最新war包

2、启动Jenkins

```
java -jar jenkins.war
```

3、修改启动端口

```
java -jar jenkins.war --httpPort=9090
```

4、后台运行

```
nohup java -jar jenkins.war --httpPort=9090 > jenkins.log &
```

5、更多命令可通过如下命令参考

```
java -jar jenkins.war --help
```

> Jenkins官方网站:https://www.jenkins.io
>
> 官网安装指南：https://www.jenkins.io/doc/book/installing
>
> Jenkins Docker镜像: https://hub.docker.com/r/jenkins/jenkins/