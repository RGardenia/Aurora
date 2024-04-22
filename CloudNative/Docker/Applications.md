# 一、Nginx
```shell
# nginx 
docker pull nginx:1.21.1-alpine

# 内部配置文件启动
docker run --name nginx -p 8080:80 -d nginx:1.21.1-alpine

# 拷贝配置文件
docker cp nginx:/etc/nginx/nginx.conf /root/nginx/conf/nginx.conf
docker cp nginx:/etc/nginx/conf.d /root/nginx/conf.d

# 注意 nginx.conf 结尾：include /etc/nginx/conf.d/*.conf;
# 所以, nginx 配置 server 需要在 conf.d 文件夹下配置

# 以配置文件启动
docker run --name nginx --net host \
-v /root/nginx/conf/nginx.conf:/etc/nginx/nginx.conf \
-v /root/nginx/conf.d:/etc/nginx/conf.d \
-v /root/nginx/web:/web \
-v /root/nginx/web1:/web1 \
-v /root/nginx/web2:/web2 \
-d nginx:1.21.1-alpine
```



# 二、Mysql

```shell
# mysql8
docker pull mysql:8.0.26

# 内部配置文件启动
docker run --name mysql8 -e MYSQL_ROOT_PASSWORD=333 \
-p 3309:3306 \
-d mysql:8.0.26

# 拷贝配置文件
# 配置文件可以拷贝 /etc/mysql/conf.d
docker cp mysql8:/etc/mysql/my.cnf /root/mysql8/conf/my.cnf

# 以外部配置文件启动
docker run --name mysql8 -e MYSQL_ROOT_PASSWORD=333 -p 3309:3306 \
-v /root/mysql8/conf/my.cnf:/etc/mysql/my.cnf \
-v /root/mysql8/data:/var/lib/mysql \
-d mysql:8.0.26
```



# 三、zookeeper

```shell
# zookeeper
docker pull zookeeper:3.7.0

docker run --name zookeeper -p 2181:2181 -d zookeeper:3.7.0
```



# 四、离线安装docker

[参考](https://zhuanlan.zhihu.com/p/109480358)

```shell
# 1、环境
sudo yum install -y yum-utils

sudo yum-config-manager \
    --add-repo \   
    http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
    
# 2、下载安装包（参考官方文档）
yumdownloader --resolve docker-ce docker-ce-cli containerd.io

# 3、打包
tar cf docker-ce.offline.tar *.rpm

# 4、上传到离线的主机上，解压
tar xf docker-ce.offline.tar

# 5、安装Docker
sudo rpm -ivh --replacefiles --replacepkgs *.rpm
# 如果提示缺依赖，要么在本地装，要么传上去，例如container-selinux

# 6、启动Dokcer
sudo systemctl start docker 
```



# 五、如何将镜像推送到阿里云容器镜像服务

## 前言

​	原来我都是将镜像推送到Dockerhub中的，因为Dockerhub是国外的软件，所以经常会各种超时的问题，这阵子研究了一下阿里云容器镜像服务，发现可以把一些常用的镜像推送到自己的容器镜像仓库里，然后就可以快速的进行拉取了~，所以我也打算将博客的镜像也推送到阿里云，提高后面镜像拉取的速度。

## 阿里云容器镜像服务

首先我们需要开通 [阿里云容器镜像服务](https://cr.console.aliyun.com/cn-qingdao/instances/repositories) ，然后创建命名空间

这里我已经创建了一个 `mogublog`，创建完成后，我们需要到访问凭证设置我们的密码，然后复制 登录脚本

然后在我们的服务器登录docker进行测试

```bash
# 登录阿里云容器镜像服务
docker login --username=moxi****@163.com registry.cn-shenzhen.aliyuncs.com
```

## 提交镜像

登录后，首先可以查看目前已经存在的镜像有哪些

我们就可以开始给镜像打tag了 【针对已经制作好的镜像打TAG】

```bash
# 格式
docker tag [ImageId] registry.cn-shenzhen.aliyuncs.com/mogublog/mogu_admin:[镜像版本号]
# 样例代码【如下】
docker tag 5f96da15eb94  registry.cn-shenzhen.aliyuncs.com/mogublog/mogu_blog_nacos:latest
```

如果你的镜像没有制作好，可以先制作镜像

```bash
# 查看容器ID
docker ps -a
# 提交镜像
docker commit -m "制作镜像" 容器ID registry.cn-shenzhen.aliyuncs.com/mogublog/mogu_blog_nacos:latest
```

打完tag后，我们就能够看到我们的镜像的tag了

```bash
docker images;
```

下面我们就可以提交到我们的阿里云仓库

```bash
docker push registry.cn-shenzhen.aliyuncs.com/mogublog/mogu_blog_nacos:latest
```

推送完成后，我们到阿里云容器服务仓库，即可看到我们刚刚提交的了



# 六、使用 GithubAction 构建博客镜像提交 DockerHub

## 前言

​	这阵子使用DockerCompose部署博客， 但是还存在一些问题，就是每次我们需要下载 [博客](https://gitee.com/moxi159753/mogu_blog_v2) 源码，然后进行编译，打包，部署。而因为博客还是前后端分离项目，因此为了完成这一系列的操作，就需要在环境中安装maven、node、git 等环境。但是因为这些环境只是在我们项目的构建阶段才会使用，而构建项目完成后，就不需要使用了，因此就打算使用Github Actions在代码提交的后，我们提前把博客的镜像给构建完成，然后上传到DockerHub上，最后在其它人需要使用的时候，就不需要自己重新构建镜像了，而是直接拉取线上的镜像，完成项目的部署。

如果你想了解Github Actions的使用，参考：[使用Github Action完成博客持续集成](http://www.moguit.cn/#/info?blogUid=0762bfb392c2cf0a94c8a7934fe46f8f)

如果你想知道更多的官方Actions，参考：[Build and push Docker images](https://github.com/marketplace/actions/build-and-push-docker-images)

如果想了解博客镜像构建和部署，参考： [使用DockerCompose制作博客YAML镜像文件](http://www.moguit.cn/#/info?blogOid=567)

如果想快速一键部署博客，参考：[DockerCompose一键部署博客(Nacos版)](http://www.moguit.cn/#/info?blogOid=565)

## 环境准备

因为我们需要在Actions中进行镜像构建，首先我们需要创建一个Github Actions的任务，首先定义我们的 actions name，以及触发的条件【触发条件这里有两种，一种是根据tag操作触发，一种是push操作触发】

```bash
name: Master-Build-Docker-Images
# 根据tag操作触发
on:
  push:
    # 每次 push tag 时进行构建，不需要每次 push 都构建。
    # 使用通配符匹配每次 tag 的提交，记得 tag 名一定要以 v 开头
    tags:
      - v*
      
# 根据push操作触发
on:
  push:
    branches:
      - Nacos

```

然后我们就需要在里面引入我们的环境依赖，因此我们引入以下环境

- git拉取代码
- java环境
- docker环境

```bash
jobs:
  push:
    # 如果需要在构建前进行测试的话需要取消下面的注释和上面对应的 test 动作的注释。
    # needs: test

    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v1
        with:
          java-version: 1.8
      - uses: docker/setup-buildx-action@v1
```

## 构建镜像

在我们完成基础环境的搭建后，我们就可以开始使用maven命令 构建我们的镜像了，其实就是运行以下的脚本

```bash
# 安装依赖
mvn clean install
# 打包
mvn docker:build
```

但是在我们进行打包操作的时候，我们需要在 pom文件中添加docker打包插件，以及Dockerfile文件

```bash
<!--docker镜像build插件-->
<plugin>
    <groupId>com.spotify</groupId>
    <artifactId>docker-maven-plugin</artifactId>
    <version>1.2.0</version>
    <configuration>
        <!-- 注意imageName一定要是符合正则[a-z0-9-_.]的，否则构建不会成功 -->
        <imageName>moxi/${project.artifactId}</imageName>
        <dockerDirectory>${project.basedir}/src/main/resources</dockerDirectory>
        <rm>true</rm>
        <resources>
            <resource>
                <targetPath>/</targetPath>
                <directory>${project.build.directory}</directory>
                <include>${project.build.finalName}.jar</include>
            </resource>
        </resources>
    </configuration>
</plugin>
```

在pom文件中，我们指定了Dockerfile文件路径，以及最后打包的镜像名称

![image-20201126093440237](images/image-20201126093440237.png)

同时在Dockerfile文件中，主要定义的就是一些镜像相关的信息，主要包括依赖的java环境，启动时的JVM参数

```bash
FROM java:alpine
VOLUME /tmp
ADD mogu_admin-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Xms256m","-Xmx256m","-jar","/app.jar"]
```

完整的镜像构建过程如下：

```bash
        # 构建镜像，指定镜像名
      - name: Build Docker Images
        run: |
          echo '=====开始mvn clean====='
          mvn clean

          echo '=====开始mvn install&&package====='
          mvn install -DskipTests=true && mvn package -DskipTests=true

          echo '=====开始构建镜像====='
          echo '=====开始构建mogu_admin====='
          cd mogu_admin
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_gateway====='
          cd mogu_gateway
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_monitor====='
          cd mogu_monitor
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_picture====='
          cd mogu_picture
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_search====='
          cd mogu_search
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_sms====='
          cd mogu_sms
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_spider====='
          cd mogu_spider
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_web====='
          cd mogu_web
          mvn docker:build
          cd ..

          echo '=====镜像构建结束====='
```

## 登录镜像网站

### 登录DockerHub

登录DockerHub，将我们构建成功的镜像上传

```bash
# 登录到 dockerhub，使用 GitHub secrets 传入账号密码，密码被加密存储在 GitHub 服务器
- name: Login to DockerHub
    uses: docker/login-action@v1
    with:
      username: ${{ secrets.DOCKER_HUB_USER_NAME }}
      password: ${{ secrets.DOCKER_HUB_PASSWORD }}
```

上面的一部分，使用到了Github的加密方式 Secrets，将我们项目中的敏感信息储存在secrets中，然后通过`${{ secrets.XX }}` 的方式获取，Secrets的存储在下图所示

![image-20201126094625454](images/image-20201126094625454.png)

我们通过 new repository secret，给我们的项目添加密钥，这里主要是对DockerHub的用户名和密码加密，防止直接泄漏在我们的仓库中

### 提交阿里云

如果我们要提交到 [Aliyun容器镜像服务](https://cr.console.aliyun.com/) ，那么就应该这样写 【这里主要以提交DockerHub为重心】

```yaml
      # 登录到 阿里云镜像服务，使用 GitHub secrets 传入账号密码，密码被加密存储在 GitHub 服务器
      - name: Login to Aliyun
        uses: docker/login-action@v1
        with:
          registry: registry.cn-shenzhen.aliyuncs.com
          username: ${{ secrets.ALIYUN_USER_NAME }}
          password: ${{ secrets.ALIYUN_PASSWORD }}
```

同时要求我们的镜像前缀为 `registry.cn-shenzhen.aliyuncs.com/mogublog` ，其中 mogublog 为我们阿里云容器镜像服务的命名空间

![image-20201127172858624](images/image-20201127172858624.png)

最后我们在 maven打包的pom文件中，就需要修改我们的前缀了

```pom
<!--docker镜像build插件-->
<plugin>
    <groupId>com.spotify</groupId>
    <artifactId>docker-maven-plugin</artifactId>
    <version>1.2.0</version>
    <configuration>
        <!-- 注意imageName一定要是符合正则[a-z0-9-_.]的，否则构建不会成功 -->
        <imageName>registry.cn-shenzhen.aliyuncs.com/mogublog/${project.artifactId}</imageName>
        <dockerDirectory>${project.basedir}/src/main/resources</dockerDirectory>
        <rm>true</rm>
        <resources>
            <resource>
                <targetPath>/</targetPath>
                <directory>${project.build.directory}</directory>
                <include>${project.build.finalName}.jar</include>
            </resource>
        </resources>
    </configuration>
</plugin>
```

并且vue打包的时候，也需要使用对应的前缀

```bash
# 打包镜像
docker build -t registry.cn-shenzhen.aliyuncs.com/mogublog/vue_mogu_web .
# 提交镜像
docker push registry.cn-shenzhen.aliyuncs.com/mogublog/vue_mogu_web
```

在我们完成DockerHub的登录后，我们就可以使用 `docker push 镜像名`   提交我们的镜像了

脚本如下

```bash
      - name: Push Docker Image
        run: |
          echo '=====开始上传镜像====='
          echo '=====开始上传mogu_admin====='
          docker push moxi/mogu_admin

          echo '=====开始上传mogu_gateway====='
          docker push moxi/mogu_gateway

          echo '=====开始上传mogu_monitor====='
          docker push moxi/mogu_monitor

          echo '=====开始上传mogu_picture====='
          docker push moxi/mogu_picture

          echo '=====开始上传mogu_search====='
          docker push moxi/mogu_search

          echo '=====开始上传mogu_sms====='
          docker push moxi/mogu_sms

          echo '=====开始上传mogu_spider====='
          docker push moxi/mogu_spider

          echo '=====开始上传mogu_web====='
          docker push moxi/mogu_web
```

## 完整脚本

### DockerHub

上传至DockerHub的完整的脚本，如下所示： `actions_master_build_image.yml` 

```bash
name: Master-Build-Docker-Images

#on:
#  push:
#    # 每次 push tag 时进行构建，不需要每次 push 都构建。使用通配符匹配每次 tag 的提交，记得 tag 名一定要以 v 开头
#    tags:
#      - v*

on:
  push:
    branches:
      - Nacos

jobs:
  push:
    # 如果需要在构建前进行测试的话需要取消下面的注释和上面对应的 test 动作的注释。
    # needs: test

    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v1
        with:
          java-version: 1.8
      - uses: docker/setup-buildx-action@v1
        # 构建镜像，指定镜像名
      - name: Build Docker Images
        run: |
          echo '=====开始mvn clean====='
          mvn clean

          echo '=====开始mvn install&&package====='
          mvn install -DskipTests=true && mvn package -DskipTests=true

          echo '=====开始构建镜像====='
          echo '=====开始构建mogu_admin====='
          cd mogu_admin
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_gateway====='
          cd mogu_gateway
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_monitor====='
          cd mogu_monitor
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_picture====='
          cd mogu_picture
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_search====='
          cd mogu_search
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_sms====='
          cd mogu_sms
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_spider====='
          cd mogu_spider
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_web====='
          cd mogu_web
          mvn docker:build
          cd ..

          echo '=====镜像构建结束====='

      # 登录到 dockerhub，使用 GitHub secrets 传入账号密码，密码被加密存储在 GitHub 服务器
      - name: Login to DockerHub
        uses: docker/login-action@v1
        with:
          username: ${{ secrets.DOCKER_HUB_USER_NAME }}
          password: ${{ secrets.DOCKER_HUB_PASSWORD }}

      - name: Push Docker Image
        run: |
          echo '=====开始上传镜像====='
          echo '=====开始上传mogu_admin====='
          docker push moxi/mogu_admin

          echo '=====开始上传mogu_gateway====='
          docker push moxi/mogu_gateway

          echo '=====开始上传mogu_monitor====='
          docker push moxi/mogu_monitor

          echo '=====开始上传mogu_picture====='
          docker push moxi/mogu_picture

          echo '=====开始上传mogu_search====='
          docker push moxi/mogu_search

          echo '=====开始上传mogu_sms====='
          docker push moxi/mogu_sms

          echo '=====开始上传mogu_spider====='
          docker push moxi/mogu_spider

          echo '=====开始上传mogu_web====='
          docker push moxi/mogu_web

          echo '=====镜像上传结束====='

```

执行完脚本后，进入到 [DockerHub](https://registry.hub.docker.com/) 中，发现已经成功提交到仓库了

![image-20201202091107737](images/image-20201202091107737.png)

### 阿里云容器镜像服务

上传至阿里云容器镜像服务的完整的脚本，如下所示

```yaml
name: Master-Build-Docker-Images

#on:
#  push:
#    # 每次 push tag 时进行构建，不需要每次 push 都构建。使用通配符匹配每次 tag 的提交，记得 tag 名一定要以 v 开头
#    tags:
#      - v*

on:
  push:
    branches:
      - Nacos

jobs:
  push:
    # 如果需要在构建前进行测试的话需要取消下面的注释和上面对应的 test 动作的注释。
    # needs: test

    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v1
        with:
          java-version: 1.8
      - uses: docker/setup-buildx-action@v1
      - uses: actions/setup-node@v1
        with:
          node-version: 12.x
      # 安装maven依赖
      - name: Maven Clean Install
        run: |
          echo '=====开始mvn clean====='
          mvn clean

          echo '=====开始mvn install&&package====='
          mvn install -DskipTests=true && mvn package -DskipTests=true

      - name: Build vue_mogu_admin and vue_mogu_web
        run: |
          echo '=====开始安装Vue_mogu_admin依赖====='
          cd ./vue_mogu_admin
          npm install
          npm run build
          cd ..

          echo '=====开始安装Vue_mogu_web依赖====='
          cd ./vue_mogu_web
          npm install
          npm run build
          cd ..

        # 构建镜像，指定镜像名
      - name: Build Java Docker Images
        run: |

          echo '=====开始构建镜像====='
          echo '=====开始构建mogu_admin====='
          cd mogu_admin
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_gateway====='
          cd mogu_gateway
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_monitor====='
          cd mogu_monitor
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_picture====='
          cd mogu_picture
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_search====='
          cd mogu_search
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_sms====='
          cd mogu_sms
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_spider====='
          cd mogu_spider
          mvn docker:build
          cd ..

          echo '=====开始构建mogu_web====='
          cd mogu_web
          mvn docker:build
          cd ..

          echo '=====镜像构建结束====='

      # 构建镜像，指定镜像名
      - name: Build Vue Docker Images
        run: |

          echo '=====开始构建镜像====='
          echo '=====开始构建vue_mogu_admin====='
          cd vue_mogu_admin
          docker build -t registry.cn-shenzhen.aliyuncs.com/mogublog/vue_mogu_admin .
          cd ..

          cd vue_mogu_web
          docker build -t registry.cn-shenzhen.aliyuncs.com/mogublog/vue_mogu_web .
          cd ..

          echo '=====镜像构建结束====='

      # 登录到 阿里云镜像服务，使用 GitHub secrets 传入账号密码，密码被加密存储在 GitHub 服务器
      - name: Login to Aliyun
        uses: docker/login-action@v1
        with:
          registry: registry.cn-shenzhen.aliyuncs.com
          username: ${{ secrets.ALIYUN_USER_NAME }}
          password: ${{ secrets.ALIYUN_PASSWORD }}

      - name: Push Docker Image
        run: |
          echo '=====开始上传镜像====='
          echo '=====开始上传mogu_admin====='
          docker push registry.cn-shenzhen.aliyuncs.com/mogublog/mogu_admin

          echo '=====开始上传mogu_gateway====='
          docker push registry.cn-shenzhen.aliyuncs.com/mogublog/mogu_gateway

          echo '=====开始上传mogu_monitor====='
          docker push registry.cn-shenzhen.aliyuncs.com/mogublog/mogu_monitor

          echo '=====开始上传mogu_picture====='
          docker push registry.cn-shenzhen.aliyuncs.com/mogublog/mogu_picture

          echo '=====开始上传mogu_search====='
          docker push registry.cn-shenzhen.aliyuncs.com/mogublog/mogu_search

          echo '=====开始上传mogu_sms====='
          docker push registry.cn-shenzhen.aliyuncs.com/mogublog/mogu_sms

          echo '=====开始上传mogu_spider====='
          docker push registry.cn-shenzhen.aliyuncs.com/mogublog/mogu_spider

          echo '=====开始上传mogu_web====='
          docker push registry.cn-shenzhen.aliyuncs.com/mogublog/mogu_web

          echo '=====开始上传vue_mogu_admin====='
          docker push registry.cn-shenzhen.aliyuncs.com/mogublog/vue_mogu_admin

          echo '=====开始上传vue_mogu_web====='
          docker push registry.cn-shenzhen.aliyuncs.com/mogublog/vue_mogu_web

          echo '=====镜像上传结束====='
```

执行完上述脚本后，我们发现已经成功提交到  [阿里云容器镜像服务](https://cr.console.aliyun.com/)

![image-20201202091542735](images/image-20201202091542735.png)

到目前为止，自动化镜像制作已经完成了~





# 七、PostgreSQL

```bash
docker run -d --name some-postgres \
	-v "/etc/postgres/my-postgres.conf":/etc/postgresql/postgresql.conf \
	-e POSTGRES_PASSWORD=151613 \
  -e PGDATA=/var/lib/postgresql/data/pgdata \
	-v /var/lib/postgresql/data:/var/lib/postgresql/data \
	-p 9009:5432 \
	postgres
#	-c 'config_file=/etc/postgresql/postgresql.conf'
```





# 九、ENV

```bash
sudo yum install -y yum-utils

sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo

sudo yum install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

sudo systemctl enable docker --now

#测试工作
docker ps
#  批量安装所有软件
docker compose  
```

prometheus.yml

```yml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']

  - job_name: 'redis'
    static_configs:
      - targets: ['redis:6379']

  - job_name: 'kafka'
    static_configs:
      - targets: ['kafka:9092']
```

docker-compose.yml

```yml
version: '3.9'

services:
  redis:
    image: redis:latest
    container_name: redis
    restart: always
    ports:
      - "6379:6379"
    networks:
      - backend

  zookeeper:
    image: bitnami/zookeeper:latest
    container_name: zookeeper
    restart: always
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
      ALLOW_ANONYMOUS_LOGIN: yes
    networks:
      - backend

  kafka:
    image: bitnami/kafka:3.4.0
    container_name: kafka
    hostname: kafka
    restart: always
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      ALLOW_PLAINTEXT_LISTENER: yes
      KAFKA_CFG_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    networks:
      - backend
  
  kafka-ui:
    image: provectuslabs/kafka-ui:latest
    container_name:  kafka-ui
    restart: always
    depends_on:
      - kafka
    ports:
      - "8080:8080"
    environment:
      KAFKA_CLUSTERS_0_NAME: dev
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka:9092
    networks:
      - backend

  prometheus:
    image: prom/prometheus:latest
    container_name: prometheus
    restart: always
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
    ports:
      - "9090:9090"
    networks:
      - backend

  grafana:
    image: grafana/grafana:latest
    container_name: grafana
    restart: always
    depends_on:
      - prometheus
    ports:
      - "3000:3000"
    networks:
      - backend

networks:
  backend:
    name: backend
```

```bash
docker compose -f docker-compose.yml up -d
```

> https://redis.io/insight/#insight-form







