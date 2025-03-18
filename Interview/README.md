# 实习



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