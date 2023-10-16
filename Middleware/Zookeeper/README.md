# Zookeeper





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

