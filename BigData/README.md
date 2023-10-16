

# Quick Big Data





## Stop Colony

```bash
### Kafka
kf.sh stop

### Zookeeper
zk.sh stop

### Hadoop
$HADOOP_HOME/sbin/stop-all.sh

```





## Docker Image

```bash
# 通过build Dockfile生成带ssh功能的centos镜像	Dockfile
FROM centos
MAINTAINER gardenia
 
RUN cd /etc/yum.repos.d/
RUN sed -i 's/mirrorlist/#mirrorlist/g' /etc/yum.repos.d/CentOS-*
RUN sed -i 's|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g' /etc/yum.repos.d/CentOS-*
RUN yum makecache
RUN yum update -y
RUN yum install -y openssh-server sudo
RUN sed -i 's/UsePAM yes/UsePAM no/g' /etc/ssh/sshd_config
RUN yum install -y openssh-clients
RUN yum install -y which
RUN yum -y install net-tools
RUN yum install -y vim
RUN yum -y install rsync

RUN echo "root:123" | chpasswd
RUN echo "root   ALL=(ALL)       ALL" >> /etc/sudoers
RUN ssh-keygen -t dsa -f /etc/ssh/ssh_host_dsa_key
RUN ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key

RUN mkdir /var/run/sshd
EXPOSE 22

# COPY jdk /usr/local/jdk
# 拷贝并解压jdk，根据自己的版本修改
ADD jdk-8u381-linux-x64.tar.gz /usr/local/
RUN mv /usr/local/jdk1.8.0_381 /usr/local/jdk
ENV JAVA_HOME /usr/local/jdk1.8
ENV PATH $JAVA_HOME/bin:$PATH

# COPY hadoop /usr/local/hadoop
# 拷贝并解压 hadoop，根据自己的版本修改
ADD hadoop-3.3.6.tar.gz /usr/local/
RUN mv /usr/local/hadoop-3.3.6 /usr/local/hadoop
ENV HADOOP_HOME /usr/local/hadoop
ENV PATH $HADOOP_HOME/bin:$PATH

## 在 hosts 中写入 host 信息
RUN echo "192.168.9.3 hadoop1" >> /etc/hosts
RUN echo "192.168.9.4 hadoop2" >> /etc/hosts
RUN echo "192.168.9.5 hadoop3" >> /etc/hosts

CMD ["/usr/sbin/sshd", "-D"]
```

### 生成基础镜像

```bash
docker build -t="centos-hadoop" .
```

### 创建网桥，分配 IP

```bash
# 创建网桥
docker network create --subnet=192.168.9.0/24 hadoop

docker network ls

### 启动三个容器并指定网桥	{ 若需要 sshd 服务，需要映射 端口 这里默认 1950 }
docker run --privileged=true -d --network hadoop --hostname hadoop1 --ip 192.168.9.3 --name hadoop1 -p 9870:9870 -p 19888:19888 -p 50070:50070 -p 8088:8088 -p 9001:9001 -p 1950:22 centos-hadoop /usr/sbin/init

docker run --privileged=true -d --network hadoop --name hadoop2 --hostname hadoop2 --ip 192.168.9.4 centos-hadoop /usr/sbin/init

docker run --privileged=true -d --network hadoop --name hadoop3 --hostname hadoop3 --ip 192.168.9.5 centos-hadoop /usr/sbin/init

# 添加端口(都没用，只能重来)
docker port hadoop1
# 容器停止时，添加端口
docker container update --publish 9001:9001 hadoop1


### 查看网桥使用情况
docker network inspect hadoop

hadoop1 192.168.9.3/24
hadoop2 192.168.9.4/24
hadoop3 192.168.9.5/24

192.168.9.3 hadoop1
192.168.9.4 hadoop2
192.168.9.5 hadoop3
```

### 登录容器，配置信息

```bash
docker exec -it hadoop1 bash
docker exec -it hadoop2 bash
docker exec -it hadoop3 bash

systemctl stop firewalld

systemctl disable firewalld.service

## 在每个 hadoop 服务器中配置 ip 地址映射
vi /etc/hosts
```

```bash
## 给每台 hadoop 服务器中配置 ssh 免密登录	rm -f /root/.ssh/known_hosts
ssh-keygen

ssh-copy-id -f -i /root/.ssh/id_rsa -p 22 root@hadoop1
ssh-copy-id -f -i /root/.ssh/id_rsa -p 22 root@hadoop2
ssh-copy-id -f -i /root/.ssh/id_rsa -p 22 root@hadoop3
```

```bash
### 修改 Hadoop 配置文件	/usr/local/hadoop/etc/hadoop
mkdir /home/hadoop
mkdir /home/hadoop/tmp /home/hadoop/hdfs_name /home/hadoop/hdfs_data

vi hadoop-env.sh

export JAVA_HOME=/usr/local/jdk
export HADOOP_HOME=/usr/local/hadoop
export HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop


vi workers
# localhost
hadoop2
hadoop3

### 配置环境变量
/etc/profile.d/container_env.sh

## JDK
export JAVA_HOME=/usr/local/jdk
export PATH=$JAVA_HOME/bin:$PATH

## Hadoop
export HADOOP_HOME=/usr/local/hadoop
export PATH=$HADOOP_HOME/bin:$PATH
export PATH=$HADOOP_HOME/sbin:$PATH

export HDFS_NAMENODE_USER=root
export HDFS_DATANODE_USER=root
export HDFS_JOURNALNODE_USER=root
export HDFS_SECONDARYNAMENODE_USER=root
export YARN_RESOURCEMANAGER_USER=root
export YARN_NODEMANAGER_USER=root

## Kafka
export KAFKA_HOME=/opt/module/kafka
export PATH=$PATH:$KAFKA_HOME/bin

source /etc/profile
```

### 分发配置信息

```bash
# 拷贝到 hadoop2 和 hadoop3 上
scp -r $HADOOP_HOME/etc/hadoop hadoop2:/usr/local/hadoop/etc/
scp -r $HADOOP_HOME/etc/hadoop hadoop3:/usr/local/hadoop/etc/

scp -r /home/hadoop hadoop2:/home
scp -r /home/hadoop hadoop3:/home

scp -r /etc/profile.d/container_env.sh hadoop2:/etc/profile.d/
scp -r /etc/profile.d/container_env.sh hadoop3:/etc/profile.d/

### 给文件赋权(每台 Hadoop)
chmod -R 777 /usr/local/hadoop
chmod -R 777 /usr/local/jdk
```

### 启动 Hadoop 集群

```bash
### hadoop1
# 格式化 hdfs
hdfs namenode -format

# 一键启动 Hadoop 集群
source /etc/profile
$HADOOP_HOME/sbin/start-all.sh
# 启动历史日志服务 
$HADOOP_HOME/bin/mapred --daemon start historyserver

# 测试 Hadoop 集群(每台 Hadoop)
jps

# hadoop1是名称结点，hadoop2是第二名称节点和数据节点

# 关闭历史任务服务 
$HADOOP_HOME/bin/mapred --daemon stop historyserver
# 关闭服务 
$HADOOP_HOME/sbin/stop-all.sh
```


