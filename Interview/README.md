# 实习



自我介绍

名字 + 年级 + 本科学习实习 + 研究生项目&方向 + 技术栈 +（Future运维）





## 英文介绍

> <font color="Cyan2"><strong>Good morning honorable judges, I am very happy that I have this precious opportunity for this interview.  My name is Zhang Yu, 21 years old this year, from JiangSu SuZhou TaiCang, educated at Qinghai Nationalities University majoring in electronic Commerce. </strong></font>
>
> <font color="mediumpurple"><strong>During the three years in the university, I kept a good learning attitude and kept the first score in my major.  I have obtained Cet-4 and CET-6, Computer Rank 3 and Software Designer certificate.  I also participated in The Blue Bridge Cup Algorithm Competition and won the provincial second prize.</strong></font>
>
> <font color="mediumAquamarine"><strong>Meanwhile, I learned and mastered a series of technologies of Java ecology in my spare time. Including but not limited to Spring Web framework, Mybatis, Mysql and Reids database, Tomcat and Nginx Server, etc. Also familiar based on Linux environment of software development.</strong></font>
>
> <font color="olivedrab"><strong>The academy has cultivated my full confidence and professionalism and solid knowledge and strong professional skills in the past three years.</strong></font>
>
> <font color="tomato"><strong> I am honest and responsible, have independent and enterprising character, work hard, be good at thinking, adapt to the new environment ability is very strong. In the shortest time to complete the transition from students to professional staff, to the best of their own efforts to integrate into the new work life.</strong></font>
>
> <font color="darkorange"><strong>lf l am lucky enough to become a member of your company, I will put all the enthusiasm and strength into work, obtain due scores, for the development of the company to contribute their strength.</strong></font>
>
> <font color="sandybrown"><strong>Though my practical experience is not very rich, I believe that I have the solid professional knowledge and practical experience during my internship, plus the spirit of good learning and progress, I can be competent for the job needs.  </strong></font>
>
> <font color="red"><strong>I hope your company will give me this opportunity.</strong></font>







`load average` 表示系统处于**可运行状态的进程数**。

通常看 `1min`, `5min`, `15min` 三个值，关注是否持续升高。

**排查思路**：

1. **CPU瓶颈** → `top`、`htop` 查看 CPU 利用率。
2. **I/O瓶颈** → `iostat -x`、`iotop` 看磁盘I/O是否满载。
3. **内存瓶颈** → `free -m`，看是否有大量 swap 使用。
4. **进程状态** → `ps -eo state,pid,cmd | grep D` 看是否有大量不可中断进程（通常I/O卡死）。
5. **僵尸/堵塞线程** → `top -H` 配合 `strace` 排查。



**TIME_WAIT 太多解决办法**：

- 调优参数

  ：

  - `net.ipv4.tcp_tw_reuse = 1`
  - `net.ipv4.tcp_tw_recycle = 0`（K8s 环境注意不要开）
  - `tcp_fin_timeout` 适当调小。

- **应用优化**：

  - 使用长连接，减少频繁短连接。

  **反向代理层复用连接**。

1. 查看当前限制 → `ulimit -n`
2. 查看系统限制 → `/etc/security/limits.conf`
3. 查看实际占用 → `lsof | wc -l`
4. 查找占用多的进程 → `lsof -n | awk '{print $2}' | sort | uniq -c | sort -nr | head`
5. 具体分析进程 → 有没有**未关闭连接**、**日志文件句柄泄露**。



确认本机 CPU、内存、I/O 是否正常 → `top`、`dmesg`。

查看网卡状态 → `ethtool eth0`，是否有丢包、链路问题。

检查路由 → `traceroute` 看丢在哪跳。

**抓包** → `tcpdump -i eth0` 分析是否有重传。

上下游机器也互 ping，排查是否是链路设备（如交换机）问题。

### MySQL 慢查询如何排查？

1. 开启 `slow_query_log`，分析日志
2. 使用 `EXPLAIN` 查看执行计划
3. 是否走索引？有无全表扫描
4. 表结构设计是否合理，避免太多 JOIN
5. 可能需要加索引、拆分大表、SQL 改写



### MQ 堆积原因及排查？

1. 查看消费端是否正常 → **消费延迟**、**消费异常**
2. 生产端流量突增 → 是否限流
3. Broker 节点负载，磁盘、网络瓶颈
4. 查看消息堆积是否在某个 partition/topic，考虑分区均衡



**CI/CD**

- Ansible + Jenkins，SaltStack，Terraform。
- 批量发布流程：
  1. 分机器分批灰度
  2. 先 health check → 发布 → 检查 → 继续下一批
  3. 发布失败自动回滚
  4. 配合 GitLab/Jenkins 实现版本控制 &流水线



### 监控体系如何设计？

1. **数据采集** → Node Exporter、Blackbox、App 内埋点
2. **指标监控** → Prometheus 存储，Grafana 展示
3. **日志监控** → ELK/EFK
4. **异常告警** → Alertmanager，设置阈值、抑制、合并策略
5. **链路追踪** → Jaeger/Zipkin





### 设计一个高可用配置中心？

1. 核心服务多副本 + leader 选举（如etcd/Consul）。
2. 配置存储持久化。
3. 支持灰度发布。
4. Watch机制，应用自动感知配置变更。
5. 容灾：多数据中心部署，跨IDC同步。



strace -p <PID>        # 附着到进程
strace ./your_command  # 直接跟踪命令执行
strace -e trace=network ./your_command  # 只看网络相关
strace -o output.log ./your_command  # 输出到文件
strace -tt -p <PID>    看哪个系统调用没返回



`traceroute` 用来 **追踪数据包经过的路由节点**，排查网络链路

**工作原理**：

- 通过 **TTL (Time To Live)** 控制，依次增加TTL，探测每一跳路由。
- 默认使用 **UDP**（Linux），可以用 `-I` 切到 ICMP。

traceroute -n <host>  # 不反查域名，加快速度
traceroute -I <host>  # 使用 ICMP



**内核调优参数**

**net.ipv4.tcp_tw_reuse = 1**

- **作用**：允许 **TIME_WAIT 状态的连接重用**，主要用于短连接（如高并发 HTTP 请求），减少 `TIME_WAIT` 过多导致的端口耗尽。
- **适用场景**：客户端或发起连接的服务端，**避免端口耗尽**问题。

**net.ipv4.tcp_tw_recycle = 0**

- **作用**：加快 `TIME_WAIT` 回收。
- K8s 环境禁用原因
  - 它依赖时间戳，NAT 场景（多Pod共享IP）会导致 **连接复用失败**，出现 **连接拒绝** 或 **延迟高**。
  - 所以 **K8s 集群中千万别开**。

**tcp_fin_timeout**

- **作用**：TCP连接关闭后，`FIN_WAIT_2` 状态保持的时间。
- **调小原因**：释放连接更快，防止大量 `FIN_WAIT` 占用资源。
- **设置示例**：

net.ipv4.tcp_fin_timeout = 30  # 默认60，通常调到10-30



## 实习 – 物流货运支付中的事务不一致



**物流货运后台系统**，测试时，用户下单支付后，系统需要：

1. 生成货运订单（订单模块）（选择了CarryID）
2. 扣款用户余额（支付模块，写数据库余额表）
3. 调用外部**承运方系统**接口分配运输车辆（远程 HTTP 接口调用）（承运方失败，保留订单！反复回滚）
4. 写入货运状态记录表

**目标**：所有操作成功，事务提交；任意一步失败，事务回滚，保持数据一致性。

❗️ **问题产生点**

- **外层事务 `@Transactional` 用的是默认 `REQUIRED`**。
- **内部方法 `PaymentService` 和 `CarrierService` 使用了 `REQUIRES_NEW`，每次新建独立事务。**



**1️⃣ 订单生成 & 扣款步骤放一个本地事务内 → 单独提交**

- 确保订单和扣款成功，数据落库，**承运方失败不会影响订单的保留**。

**2️⃣ 承运方接口调用 & 更新订单状态 → 异步或事务提交后再处理**

- 调用远程承运方接口，**失败不影响订单表和余额表**。
- 失败时，更新订单表一个状态字段，如 **`carrier_status = FAILED`**，方便后续补偿或用户提示。

**事务同步钩子**

```java
TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
    @Override
    public void afterCommit() {
        // 调用承运方接口
        boolean success = carrierFeignClient.allocate(order.getId());
        if (success) {
            // 更新 carrier_status = ALLOCATED
            orderRepository.updateCarrierStatus(order.getId(), "ALLOCATED");
        } else {
            // 更新 carrier_status = FAILED
            orderRepository.updateCarrierStatus(order.getId(), "FAILED");
            // 可以记录日志/发告警
        }
    }
});
```

**通过消息队列异步处理**

将调用远程服务改成 **发送消息**，远程服务监听消息后处理。

- 如果本地事务失败，消息未发出，**下游服务无感知**。
- 如果本地事务成功，消息发出，确保一致性。

ThreadLocal

| 步骤                | 处理方式                                                     |
| ------------------- | ------------------------------------------------------------ |
| 1. 生成订单         | 订单信息暂存在 ThreadLocal 中，不写入数据库                  |
| 2. 扣款余额         | 扣款信息暂存在 ThreadLocal 中，不直接更新 DB                 |
| 3. 调用承运方接口   | 执行远程 HTTP 请求，但如果失败，ThreadLocal 中的状态数据丢弃 |
| 4. 成功后，批量提交 | 承运方接口成功后，统一写入订单、扣款、状态记录（可以写一个批量提交方法） |









## 实习感悟

> 第一天：实习真的就是去打杂的，除非是大厂！！！
>
> 看了一星期的 FineReport，都快超神了，还在深入…  人都快没了
>
> 一行代码没写，除了 SQL …