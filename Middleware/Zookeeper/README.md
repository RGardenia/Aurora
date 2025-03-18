# Zookeeper

​	ZooKeeper是一个分布式的，开放源码的分布式应用程序协调服务，是Google的Chubby一个开源的实现，是Hadoop和Hbase的重要组件。它是一个为分布式应用提供一致性服务的软件，提供的功能包括：配置维护、域名服务、分布式同步、组服务等。
​	ZooKeeper的目标就是封装好复杂易出错的关键服务，将简单易用的接口和性能高效、功能稳定的系统提供给用户。

**特点：**

- 最终一致性：客户端看到的数据最终是一致的。
- 可靠性：服务器保存了消息，那么它就一直都存在。
- 实时性：ZooKeeper 不能保证两个客户端同时得到刚更新的数据。
- 独立性（等待无关）：不同客户端直接互不影响。
- 原子性：更新要不成功要不失败，没有第三个状态。

#### **数据发布与订阅**

​	发布与订阅即所谓的配置管理，顾名思义就是将数据发布到ZooKeeper节点上，供订阅者动态获取 数据，实现配置信息的集中式管理和动态更新。例如全局的配置信息，地址列表等就非常适合使 用。

​	数据发布/订阅的一个常见的场景是配置中心，发布者把数据发布到 ZooKeeper 的一个或一系列的 节点上，供订阅者进行数据订阅，达到动态获取数据的目的。

配置信息一般有几个特点:

- 数据量小的KV
- 数据内容在运行时会发生动态变化
- 集群机器共享，配置一致

#### **配置管理**

​	程序分布式的部署在不同的机器上，将程序的配置信息放在ZooKeeper的znode下，当有配置发生改变时，也就是znode发生变化时，可以通过改变zk中某个目录节点的内容，利用watch通知给各 个客户端 从而更改配置。

​	集群管理主要指集群监控和集群控制两个方面。前者侧重于集群运行时的状态的收集，后者则是对 集群进行操作与控制。开发和运维中，面对集群，经常有如下需求:

- 希望知道集群中究竟有多少机器在工作
- 对集群中的每台机器的运行时状态进行数据收集
- 对集群中机器进行上下线的操作



#### 分布式通知与协调

1、分布式环境中，经常存在一个服务需要知道它所管理的子服务的状态。

a）NameNode需知道各个Datanode的状态

b）JobTracker需知道各个TaskTracker的状态

2、心跳检测机制可通过ZooKeeper来实现

3、信息推送可由ZooKeeper来实现，ZooKeeper相当于一个发布/订阅系统

#### 分布式锁

处于不同节点上不同的服务，它们可能需要顺序的访问一些资源，这里需要一把分布式的锁。

分布式锁具有以下特性：**写锁、读锁、时序锁。**

写锁：在zk上创建的一个临时的**无编号**的节点。由于是无序编号，在创建时不会自动编号，导致只能客户端有一个客户端得到锁，然后进行写入。

读锁：在zk上创建一个临时的**有编号**的节点，这样即使下次有客户端加入是同时创建相同的节点时，他也会自动编号，也可以获得锁对象，然后对其进行读取。

时序锁：在zk上创建的一个临时的**有编号的节点根据编号的大小控制锁**。

#### 分布式队列

分布式队列分为两种：

1、当一个队列的成员都聚齐时，这个队列才可用，否则一直等待所有成员到达，这种是同步队列。

a）一个job由多个task组成，只有所有任务完成后，job才运行完成。

b）可为job创建一个/job目录，然后在该目录下，为每个完成的task创建一个临时的Znode，一旦 临时节点数目达到task总数，则表明job运行完成。

2、队列按照FIFO方式进行入队和出队操作，例如实现生产者和消费者模型。



## 部署

### 集群规划

| hadoop001 | hadoop002 | hadoop003 |
| :-------: | :-------: | :-------: |
|    zk     |    zk     |    zk     |

### 安装（ 集群 ）

1. 官网首页  下载
   https://zookeeper.apache.org/

2. 上传 压缩包 并解压、

   ```bash
   tar -zxvf apache-zookeeper-3.9.1-bin.tar.gz -C /opt/module/
   mv apache-zookeeper-3.9.1-bin zookeeper
   ```
   
3. 配置服务器编号

   ```bash
   # 在/opt/module/zookeeper 这个目录下创建 zkData
   mkdir zkData
   
   # 在/opt/module/zookeepe/zkData 目录下创建一个 myid 的文件
   vi myid
   
   # 在文件中添加与 server 对应的编号（注意：上下不要有空行，左右不要有空格）
   2
   
   # 拷贝配置好的 zookeeper 到其他机器
   xsync zookeeper
   # 并分别在 hadoop002、hadoop003 上修改 myid 文件中内容为 2、3
   ```

4. 配置 `zoo.cfg` 文件

   ```bash
   # 重命名 /opt/module/zookeeper/conf 这个目录下的 zoo_sample.cfg 为 zoo.cfg
   mv zoo_sample.cfg zoo.cfg
   vim zoo.cfg
   # 修改数据存储路径配置
   dataDir=/opt/module/zookeeper/zkData
   
   # 增加如下配置
   ####################### cluster ##########################
   server.1=hadoop001:2888:3888
   server.2=hadoop002:2888:3888
   server.3=hadoop003:2888:3888
   
   # 同步 zoo.cfg 配置文件
   xsync zoo.cfg
   ```

   > # 配置参数解读
   > `server.A=B:C:D` 
   >
   > - A 是一个数字，表示这个是第几号服务器；集群模式下配置一个文件 myid，这个文件在 dataDir 目录下，这个文件里面有一个数据就是 A 的值，Zookeeper 启动时读取此文件，拿到里面的数据与 zoo.cfg 里面的配置信息比较从而判断到底是哪个 server
   > - B 是这个服务器的地址
   > - C 是这个服务器 Follower 与集群中的 Leader 服务器交换信息的端口
   > - D 是万一集群中的 Leader 服务器挂了，需要一个端口来重新进行选举，选出一个新的 Leader，而这个端口就是用来执行选举时服务器相互通信的端口

5. 集群操作

   ```bash
   # 分别启动 Zookeeper
   bin/zkServer.sh start
   
   # 查看状态
   bin/zkServer.sh status
   ```

<hr>

#### ZK 集群启动停止脚本

```bash
# 在 hadoop001 的 /home/gardenia/bin 目录下创建脚本
vim zk.sh

#!/bin/bash

case $1 in
"start"){
	for i in hadoop001 hadoop002 hadoop003
	do
		echo ---------- zookeeper $i starts ------------
		ssh $i "/opt/module/zookeeper/bin/zkServer.sh start"
	done
};;
"stop"){
	for i in hadoop001 hadoop002 hadoop003
	do
		echo ---------- zookeeper $i stops ------------
		ssh $i "/opt/module/zookeeper/bin/zkServer.sh stop"
	done
};;
"status"){
	for i in hadoop001 hadoop002 hadoop003
	do
		echo ---------- zookeeper $i status ------------
		ssh $i "/opt/module/zookeeper/bin/zkServer.sh status"
	done
};;
esac

## 增加脚本执行权限
chmod u+x zk.sh
# Zookeeper 集群启动脚本
zk.sh start
# Zookeeper 集群停止脚本
zk.sh stop
# Zookeeper 集群状态脚本
zk.sh status
```

