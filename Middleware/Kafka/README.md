# Kafka



​	Kafka是 一个开源的 分布式事件流平台 （Event Streaming Platform），被数千家公司用于高性能数据管道、流分析、数据集成和关键任务应用

> ​	消息队列 企业中比较常见的消息队列产品主要有Kafka、ActiveMQ 、RabbitMQ 、 RocketMQ 等。 在大数据场景主要采用 Kafka 作为消息队列。在 JavaEE 开发中主要采用 ActiveMQ、 RabbitMQ、RocketMQ

**应用场景**

传统的消息队列的主要应用场景包括：缓存/消峰、解耦和异步通信

- 缓冲/消峰：有助于控制和优化数据流经过系统的速度，解决生产消息和消费消息的处理速度不一致的情况
- 解耦：允许你独立的扩展或修改两边的处理过程，只要确保它们遵守同样的接口约束
- 异步通信：允许用户把一个消息放入队列，但并不立即处理它，然后在需要的时候再去处理它们

<img src="images/image-20231010222304502.png" alt="image-20231010222304502" style="zoom:67%;" />

**消息队列的两种模式**

1）点对点模式：消费者主动拉取数据，消息收到后清除消息

<img src="images/image-20231010225931253.png" alt="image-20231010225931253" style="zoom:80%;" />

2）发布/订阅模式

- 可以有多个topic主题（浏览、点赞、收藏、评论等）
- 消费者消费数据之后，不删除数据
- 每个消费者相互独立，都可以消费到数据

![image-20231010230026046](images/image-20231010230026046.png)

## **Kafka 基础架构**

1. 为方便扩展，并提高吞吐量，一个 topic 分为多个 partition
2. .配合分区的设计，提出消费者组的概念，组内每个消费者并行消费
3. 为提高可用性，为每个 partition 增加若干副本，类似NameNode HA
4. ZK 中记录谁是 leader，Kafka2.8.0 以后也可以配置不采用 ZK

<img src="images/image-20231010230342102.png" alt="image-20231010230342102" style="zoom:80%;" />

（1）Producer：消息生产者，就是向 Kafka broker 发消息的客户端。 
（2）Consumer：消息消费者，向 Kafka broker 取消息的客户端。 
（3）Consumer Group（CG）：消费者组，由多个 consumer 组成。消费者组内每个消 费者负责消费不同分区的数据，一个分区只能由一个组内消费者消费；消费者组之间互不 影响。所有的消费者都属于某个消费者组，即消费者组是逻辑上的一个订阅者。
（4）Broker：一台 Kafka 服务器就是一个 broker。一个集群由多个 broker 组成。一个 broker 可以容纳多个 topic。
（5）Topic：可以理解为一个队列，生产者和消费者面向的都是一个 topic。 
（6）Partition：为了实现扩展性，一个非常大的 topic 可以分布到多个 broker（即服 务器）上，一个 topic 可以分为多个 partition，每个 partition 是一个有序的队列。 
（7）Replica：副本。一个 topic 的每个分区都有若干个副本，一个 Leader 和若干个 Follower。 
（8）Leader：每个分区多个副本的“主”，生产者发送数据的对象，以及消费者消费数 据的对象都是 Leader。 
（9）Follower：每个分区多个副本中的“从”，实时从 Leader 中同步数据，保持和 Leader 数据的同步。Leader 发生故障时，某个 Follower 会成为新的 Leader。



## 部署

### 集群规划

| hadoop001 | hadoop002 | hadoop003 |
| :-------: | :-------: | :-------: |
|    zk     |    zk     |    zk     |
|   kafka   |   kafka   |   kafka   |



### 安装

0）官方下载地址：http://kafka.apache.org/downloads.html

1）解压安装包

```bash
mkdir /opt/module
tar -zxvf kafka_2.12-3.6.0.tgz -C /opt/module/
mv kafka_2.12-3.6.0 kafka
```

2）进入到 `/opt/module/kafka` 目录，修改配置文件

```bash
cd config/
vim server.properties
### 输入以下内容
#broker 的全局唯一编号，不能重复，只能是数字
broker.id=0
#处理网络请求的线程数量
num.network.threads=3
#用来处理磁盘 IO 的线程数量
num.io.threads=8
#发送套接字的缓冲区大小
socket.send.buffer.bytes=102400
#接收套接字的缓冲区大小
socket.receive.buffer.bytes=102400
#请求套接字的缓冲区大小
socket.request.max.bytes=104857600
#kafka 运行日志(数据)存放的路径，路径不需要提前创建，kafka 自动帮你创建，可以配置多个磁盘路径，路径与路径之间可以用"，"分隔
log.dirs=/opt/module/kafka/datas
#topic 在当前 broker 上的分区个数
num.partitions=1
#用来恢复和清理 data 下数据的线程数量
num.recovery.threads.per.data.dir=1
# 每个 topic 创建时的副本数，默认时 1 个副本
offsets.topic.replication.factor=1
#segment 文件保留的最长时间，超时将被删除
log.retention.hours=168
#每个 segment 文件的大小，默认最大 1G
log.segment.bytes=1073741824
# 检查过期数据的时间，默认 5 分钟检查一次是否数据过期
log.retention.check.interval.ms=300000
#配置连接 Zookeeper 集群地址（在 zk 根目录下创建/kafka，方便管理）
zookeeper.connect=hadoop102:2181,hadoop103:2181,hadoop104:2181/kafka
```

3）分发安装包

```bash
xsync kafka/

scp -r /opt/module/kafka hadoop2:/opt/module/
scp -r /opt/module/kafka hadoop3:/opt/module/
```

4）分别在 hadoop103 和 hadoop104 上修改配置文件 `/opt/module/kafka/config/server.properties` 中的 `broker.id=1`、`broker.id=2`

> 注：broker.id 不得重复，整个集群中唯一

```bash
vim kafka/config/server.properties

### 修改:
# The id of the broker. This must be set to a unique integer for each broker.
broker.id=1

vim kafka/config/server.properties
### 修改:
# The id of the broker. This must be set to a unique integer for each broker.
broker.id=2

log.dirs=/data/kafka
# or 
log.dirs=/opt/module/kafka/datas

zookeeper.connect=hadoop001:2181,hadoop002:2181,hadoop003:2181/kafka
```

5）配置环境变量

（1）在 `/etc/profile.d/container_env.sh` 文件中增加 `kafka` 环境变量配置
```bash
sudo vim /etc/profile.d/container_env.sh

# KAFKA_HOME
export KAFKA_HOME=/opt/module/kafka
export PATH=$PATH:$KAFKA_HOME/bin

scp -r /etc/profile.d/container_env.sh hadoop2:/etc/profile.d/
scp -r /etc/profile.d/container_env.sh hadoop3:/etc/profile.d/
source /etc/profile

### 分发环境变量文件到其他节点，并 source
sudo /home/gardenia/bin/xsync /etc/profile.d/container_env.sh
```

6）启动集群

（1）先启动 Zookeeper 集群，然后启动 Kafka
```bash
zk.sh start
```

（2）依次在 hadoop001、hadoop002、hadoop003节点上启动  `Kafka` 
```bash
bin/kafka-server-start.sh -daemon config/server.properties
```

> 注意：配置文件的路径要能够到 `server.properties` 
>
> 关闭集群：`bin/kafka-server-stop.sh` 

#### 集群启停脚本

1）在 `/home/gardenia/bin`  目录下创建文件 `kf.sh` 脚本文件

```bash
#! /bin/bash
case $1 in 
"start"){
	for i in hadoop001 hadoop002 hadoop003
	do
		echo " --------start $i Kafka-------"
		ssh $i "/opt/module/kafka/bin/kafka-server-start.sh -daemon /opt/module/kafka/config/server.properties"
	done
};;
"stop"){
	for i in hadoop001 hadoop002 hadoop003
	do
		echo " --------stop $i Kafka-------"
		ssh $i "/opt/module/kafka/bin/kafka-server-stop.sh "
	done
};;
esac
```

2）添加执行权限

```bash
chmod +x kf.sh
```

3）集群命令

```bash
### 启动集群
kf.sh start

### 停止
kf.sh stop
```

