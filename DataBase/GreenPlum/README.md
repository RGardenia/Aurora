# GreenPlum



# 简介

## 1.1 MPP架构的由来

MPP（Massively Parallel Processing）架构是一种用于处理大规模数据的计算架构，它通过将任务分配给多个处理单元并行执行，以提高处理速度和性能。MPP 架构的由来可以追溯到对大规模数据处理需求的不断增长，传统的单一处理器或对称多处理器（SMP）架构无法满足这些需求。MPP 架构允许在大规模数据集上实现水平扩展，通过添加更多的处理单元来增加计算和存储能力。

l **分布式存储：** MPP 数据库系统通常使用分布式存储架构，将数据分散存储在多个节点上。每个节点都有自己的存储单元，这样可以提高数据的读取和写入速度。

l **并行处理：** MPP 架构通过将任务分解成小块，并同时在多个处理单元上执行这些任务来实现并行处理。每个处理单元负责处理数据的一个子集，然后将结果合并以生成最终的输出。

l **共享无状态架构：** MPP 系统通常采用共享无状态的架构，即每个节点之间没有共享的状态。这使得系统更容易水平扩展，因为可以简单地添加更多的节点，而不需要共享状态的复杂管理。

l **负载平衡：** MPP 数据库通常具有负载平衡机制，确保任务在各个节点上均匀分布，避免某些节点成为性能瓶颈。

l **高可用性：** 为了提高系统的可用性，MPP 架构通常设计成具有容错和故障恢复机制。如果一个节点出现故障，系统可以继续运行，而不会丢失数据或中断服务。

一些知名的 MPP 数据库系统包括 Teradata、Greenplum、Amazon Redshift 等。这些系统广泛应用于企业数据仓库、商业智能和大数据分析等领域。总体而言，MPP 架构通过将任务分布到多个节点并行执行，以及有效地利用分布式存储和处理的方式，提供了一种高性能、可伸缩的数据处理解决方案，适用于处理大规模数据的场景。



## 1.2 GreenPlum

Greenplum是基于开源PostgreSQL的分布式数据库，采用shared-nothing架构，即主机、操作系统、内存、存储都是每台服务器独立自我控制，不存在共享。

Greenplum本质上是一个关系型数据库集群，实际上是由多个独立的数据库服务组合而成的一个逻辑数据库。这种数据库集群采取的是MPP（Massively Parallel Processing）架构，大规模并行处理。

![image-20240319120755852](images/image-20240319120755852.png)

​	GreenPlum数据库是由Master Server、Segment Server和Interconnect三部分组成，Master Server和Segment Server的互联使用Interconnect。

​	Greenplum是一个关系型数据库，是由数个独立的数据服务组合成的逻辑数据库，整个集群由多个数据节点（Segment Host）和控制节点（Master Host）组成。在典型的Shared-Nothing中，每个节点上所有的资源的CPU、内存、磁盘都是独立的，每个节点都只有全部数据的一部分，也只能使用本节点的数据资源。在Greenplum中，需要存储的数据在进入到表时，将先进行数据分布的处理工作，将一个表中的数据平均分布到每个节点上，并为每个表指定一个分布列（Distribute Column）,之后便根据Hash来分布数据，基于Shared-Nothing的原则，Greenplum这样处理可以充分发挥每个节点处IO的处理能力。

- **Master**节点：是整个系统的控制中心和对外的服务接入点，它负责接收用户SQL请求，将SQL生成查询计划并进行并行处理优化，然后将查询计划分配到所有的Segment节点并进行处理，协调组织各个Segment节点按照查询计划一步一步地进行并行处理，最后获取到Segment的计算结果，再返回给客户端。从用户的角度看Greenplum集群，看到的只是Master节点，无需关心集群内部的机制，所有的并行处理都是在Master控制下自动完成的。Master节点一般只有一个或二个。
- **Segment**节点：是Greenplum执行并行任务的并行计算节点，它接收Master的指令进行MPP并行计算，因此所有Segment节点的计算性总和就是整个集群的性能，通过增加Segment节点，可以线性化得增加集群的处理性能和存储容量，Segment节点可以是1~10000个节点。
- **Interconnect**：是Master节点与Segment节点、Segment节点与Segment节点之间进行数据传输的组件，它基于千兆交换机或者万兆交换机实现数据再节点之间的高速传输。

外部数据在加载到Segment时，采用并行数据流进行加载，直接加载到Segment节点，这项独特的技术是Greenplum的专有技术，保证数据在最短时间内加载到数据库中。

**优缺点**

**1）优点**

（1）数据存储

当今是个数据不断膨胀的时代，采取MPP架构的数据库系统可以对海量数据进行管理。

（2）高并发

Greenplum利用强大并行处理能力提供并发支持。

（3）线性扩展

Greenplum线性扩展支持为数据分析系统将来的拓展给予了技术上的保障，用户可根据实施需要进行容量和性能的扩展。

（4）高性价比

Greenplum数据库软件系统节点基于业界各种开放式硬件平台，在普通的x86 Server上就能达到很高的性能，因此性价比很。同样，Greenplum产品的维护成本相比同类厂商也低许多。

（5）反应速度

Greenplum通过准实时、实时的数据加载方式，实现数据仓库的实时更新，进而实现动态数据仓库(ADW)。

（6）高可用性

对于主节点，Greenplum提供Master/Stand by机制进行主节点容错，当主节点发生错误时，可以切换到Stand by节点继续服务。

（7）系统易用

Greenplum产品是基于流行的PostgreSQL之上开发，几乎所有的PostgreSQL客户端工具及PostgreSQL应用都能运行在Greenplum平台上，在Internet上有着丰富的PostgreSQL资源供用户参考。

**2）缺点**

（1）主从双层架构，并非真正的扁平架构，存在性能瓶颈和SPOF单点故障。

（2）无法支持数据压缩态下的DML操作，不易于数据的维护和更新。

（3）单个节点上的数据库没有并行和大内存使用能力，必须通过部署多个实列（segment servers）来充分利用系统资源，造成使用和部署很复杂。



# 安装

## 2.1 GreenPlum安装地址

**1）GreenPlum官网地址**

https://greenplum.org/

**2）文档查看地址**

https://docs.vmware.com/en/VMware-Tanzu-Greenplum/6/greenplum-database/GUID-landing-index.html

**3）下载地址**

https://network.pivotal.io/products/vmware-tanzu-greenplum



## 2.2 部署

1. **安装模板虚拟机，IP地址**192.168.10.101**、主机名称**hadoop101**、内存**8G**、核数4个、**硬盘50G

2. **hadoop100**虚拟机配置要求如下（本文Linux系统全部以CentOS-7.5-x86-1804为例）

   ```bash
   yum install -y epel-release
   yum install -y  vim net-tools psmisc  nc rsync  lrzsz  ntp libzstd openssl-static tree iotop git
   systemctl stop firewalld
   systemctl disable firewalld.service
   
   rpm -qa | grep -i java | xargs -n1 rpm -e --nodeps
   # rpm -qa：查询所安装的所有rpm软件包
   # grep -i：忽略大小写
   # xargs -n1：表示每次只传递一个参数
   # rpm -e –nodeps：强制卸载软件
   ```

3. **模板机hosts映射文件**

   ```bash
   vim /etc/hosts
   192.168.10.100 hadoop100
   192.168.10.101 hadoop101
   192.168.10.102 hadoop102
   192.168.10.103 hadoop103
   192.168.10.104 hadoop104
   192.168.10.105 hadoop105
   192.168.10.106 hadoop106
   192.168.10.107 hadoop107
   192.168.10.108 hadoop108
   reboot
   ```

4. 环境

   **硬件：克隆**3台虚拟机（每台4核、8G内存、50G存储）

   **操作系统：CentOS-7.5-x86-1804**

   **GreenPlum**版本：open-source-greenplum-db-6.25.3-rhel7-x86_64

   **数据库节点安装规划：**1台master节点, 无standby节点，2台segment节点

   主机名配置及节点规划如下：

   | 主机IP             | **主机名** | **节点规划** |
   | ------------------ | ---------- | ------------ |
   | **192.168.10.102** | hadoop102  | master节点   |
   | **192.168.10.103** | hadoop103  | segment1节点 |
   | **192.168.10.104** | hadoop104  | segment2节点 |

5. 安装前配置

   ```bash
   yum install -y apr apr-util bash bzip2 curl krb5 libcurl libevent libxml2 libyaml zlib openldap openssh-client openssl openssl-libs perl readline rsync R sed tar zip krb5-devel
   
   # 关闭 SElinux
   vim /etc/selinux/config
   ```

   **操作系统参数配置**

   `vim /etc/sysctl.conf`

   （1）共享内存（每台节点需要单独计算）

   ​         kernel.shmall = _PHYS_PAGES / 2 ，系统可用的内存页总量的一半，可以用 getconf _PHYS_PAGES 查看系统可用的内存页总量

   ​                  `echo $(expr $(getconf _PHYS_PAGES) / 2)`

   ​         kernel.shmmax = kernel.shmall * PAGE_SIZE ，命令 getconf PAGE_SIZE 或者页大小

   ​                  `echo $(expr $(getconf _PHYS_PAGES) / 2 \* $(getconf PAGE_SIZE))`

   （2）主机内存

   ​         vm.overcommit_memory 系统使用该参数来确定可以为进程分配多少内存。对于GP数据库，此参数应设置为2

   ​         vm.overcommit_ratio 以为进程分配内的百分比，其余部分留给操作系统。默认值为50。建议设置95

   （3）端口设定

   ​         为避免在Greenplum初始化期间与其他应用程序之间的端口冲突，指定的端口范围 net.ipv4.ip_local_port_range。使用gpinitsystem初始化Greenplum时，请不要在该范围内指定Greenplum数据库端口

   （4）系统内存

   ​         系统内存大于64G ,建议以下配置：

   ```yaml
   vm.dirty_background_ratio = 0
   vm.dirty_ratio = 0
   vm.dirty_background_bytes = 1610612736 # 1.5GB
   vm.dirty_bytes = 4294967296 # 4GB
   ```

   ​         系统内存小于等于 64GB，移除vm.dirty_background_bytes 设置，并设置以下参数

   ```bash
   vm.dirty_background_ratio = 3
   vm.dirty_ratio = 10
   ```

   ​         本次系统参数配置如下：

   ```bash
   kernel.shmall = 1019650
   kernel.shmmax = 4176486400
   #设置系统范围内共享内存段的最大数量，默认4096
   kernel.shmmni = 4096
   # See Segment Host Memory           
   # 主机内存
   vm.overcommit_memory = 2
   # See Segment Host Memory
   vm.overcommit_ratio = 95
   # See Port Settings 端口设定
   net.ipv4.ip_local_port_range = 10000 65535
   kernel.sem = 500 2048000 200 40960
   kernel.sysrq = 1
   kernel.core_uses_pid = 1
   kernel.msgmnb = 65536
   kernel.msgmax = 65536
   kernel.msgmni = 2048
   net.ipv4.tcp_syncookies = 1
   net.ipv4.conf.default.accept_source_route = 0
   net.ipv4.tcp_max_syn_backlog = 4096
   net.ipv4.conf.all.arp_filter = 1
   net.core.netdev_max_backlog = 10000
   net.core.rmem_max = 2097152
   net.core.wmem_max = 2097152
   vm.swappiness = 10
   vm.zone_reclaim_mode = 0
   vm.dirty_expire_centisecs = 500
   vm.dirty_writeback_centisecs = 100
   # See System Memory
   # 系统内存
   vm.dirty_background_ratio = 3
   vm.dirty_ratio = 10
   ```

   ​         设置完成后 重载参数

   ```bash
   sysctl -p
   ```

   **系统资源限制**

   ​         修改系统资源限制配置文件(/etc/security/limits.conf)，3台主机同步修改，都添加以下参数：

   ```bash
   *       soft    nofile  65536
   *       hard    nofile  65536
   *       soft    nproc   131072
   *       hard    nproc   131072
   # “*” 星号表示所有用户
   # noproc 是代表最大进程数
   #	nofile 是代表最大文件打开数
   ```

   ​         同时，针对CentOS 7操作系统，还需修改：/etc/security/limits.d/20-nproc.conf 文件

   ```bash
   * soft nofile 65536
   * hard nofile 65536
   * soft nproc 131072
   * hard nproc 131072
   ```

   ​         退出重新登陆，ulimit -u 命令显示每个用户可用的最大进程数max user processes。验证返回值为131072

   **SSH 连接阈值**

   ​         Greenplum数据库管理程序中的gpexpand、 gpinitsystem、gpaddmirrors，使用 SSH连接来执行任务。在规模较大的Greenplum集群中，程序的ssh连接数可能会超出主机的未认证连接的最大阈值。发生这种情况时，会收到以下错误：ssh_exchange_identification: Connection closed by remote host。

   ​         为避免这种情况，可以更新 /etc/ssh/sshd_config 或者 /etc/sshd_config 文件的 MaxStartups 和 MaxSessions 参数。

   ​         root用户登陆所有服务器，编辑配置文件：/etc/ssh/sshd_config，修改完成，重启sshd服务，使参数生效。

   ```bash
   vi /etc/ssh/sshd_config
   MaxSessions 200
   MaxStartups 100:30:1000
   
   systemctl restart sshd
   ```

   **修改字符集**

   ​         检查主机的字符集，字符集必须是 en_US.UTF-8，查看LANG环境变量或者通过 locale 命令

   ​         `echo $LANG` 

   ​         如果不是 en_US.UTF-8字符集，则用root用户进行设置，退出重新登陆后，再进行查询设置是否生效

   ​         `localectl set-locale LANG=en_US.UTF-8`

   **集群时钟同步**

   ​         如果集群时间不一致，每台节点使用如下命令

   ​         `ntpdate cn.pool.ntp.org`











