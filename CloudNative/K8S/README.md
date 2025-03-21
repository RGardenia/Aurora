# Kubernetes





常用的 `kind` 资源类型：

| kind                                 | 说明                                                     |
| ------------------------------------ | -------------------------------------------------------- |
| **Pod**                              | 最小的部署单元，包含一个或多个容器                       |
| **ReplicaSet**                       | 保证某个 Pod 副本数                                      |
| **Deployment**                       | 管理 ReplicaSet，实现滚动升级、回滚等                    |
| **StatefulSet**                      | 有状态应用的控制器，Pod 有固定序号，持久化存储           |
| **DaemonSet**                        | 每个 Node 上部署一个 Pod，适合日志、监控等               |
| **Job**                              | 执行一次性任务，直到成功                                 |
| **CronJob**                          | 定时任务，周期性调度                                     |
| **Service**                          | 稳定暴露一组 Pod，支持 ClusterIP、NodePort、LoadBalancer |
| **Ingress**                          | 七层 HTTP 路由，外部访问入口                             |
| **ConfigMap**                        | 存储配置信息                                             |
| **Secret**                           | 存储敏感数据，如密码、密钥                               |
| **PersistentVolume (PV)**            | 预先配置的存储资源                                       |
| **PersistentVolumeClaim (PVC)**      | 申请使用 PV 的声明                                       |
| **Namespace**                        | 资源隔离                                                 |
| **ResourceQuota**                    | 限制 Namespace 中资源使用量                              |
| **LimitRange**                       | 限制 Pod/Container 的资源使用范围                        |
| **HPA (HorizontalPodAutoscaler)**    | 自动横向扩缩容                                           |
| **VPA (VerticalPodAutoscaler)**      | 自动调整 Pod 资源 request/limit                          |
| **NetworkPolicy**                    | 网络访问控制策略                                         |
| **Role / ClusterRole**               | 权限定义（细粒度 RBAC）                                  |
| **RoleBinding / ClusterRoleBinding** | 权限绑定                                                 |
| **ServiceAccount**                   | Pod 访问 API Server 时使用的身份凭证                     |



**K8S 的资源限制 QoS 是什么？**

QoS（Quality of Service，服务质量）是 Kubernetes 用来区分不同 Pod 的资源分配方式，主要分为三类：

1. **BestEffort**：Pod 中不设置任何 CPU 或内存的请求或限制。这种模式是最佛系的，只要有资源用就行，不强求。
2. **Burstable**：Pod 中至少有一个容器设置了 CPU 或内存的请求。只要你稍微给点资源要求，Pod 就会尽力分配多余的资源。
3. **Guaranteed**：Pod 中所有的容器都必须明确设置 CPU 和内存的请求和限制，而且两者必须相等。这是最高优先级的资源分配模式，保证你分到的资源一定够用。

**K8S 的数据持久化方式有哪些？**

Kubernetes 提供了几种常见的数据持久化方式：

1. **EmptyDir（空目录）**：这是一种临时存储方式。当 Pod 启动时，K8S 会给它分配一个空目录，Pod 里所有容器都能共享使用。数据的生命周期与 Pod 一致，Pod 被删除时，数据也就没了，主要用于临时存储或容器间的共享数据。
2. **HostPath**：这种方式允许将宿主机上的某个文件或目录挂载到 Pod 内部。相当于将宿主机的某些资源直接暴露给容器使用。
3. **PersistentVolume（简称 PV）**：这是 Kubernetes 的一种持久化存储机制。通过 NFS、GFS 等存储后端，提供了长期存储解决方案。用户通过 PersistentVolumeClaim（简称 PVC）来向集群申请存储资源。PVC 和 PV 之间需要定义相同的访问模式和存储类，以便正确关联。

<hr>

**kube-proxy的iptables模式是怎么工作的？**

kube-proxy在Kubernetes 1.2版本后默认启用了iptables模式。简单来说，在这种模式下，kube-proxy不再是个真正的代理，而是通过监听Kubernetes API Server来捕捉Service和Endpoint的变化，并动态更新iptables的规则。这样，客户端请求就可以直接通过iptables的NAT机制被路由到目标Pod，而不再需要经过kube-proxy本身。

**kube-proxy的IPVS模式是如何运作的？**

从Kubernetes 1.11版本开始，IPVS模式被稳定下来。IPVS是专为高性能负载均衡设计的，使用哈希表等更高效的数据结构来管理规则，比iptables模式能支持更大规模的集群。IPVS使用的是ipset，而不是iptables的线性规则链，这让它在处理大量规则时性能表现更出色。

**kube-proxy的IPVS和iptables模式有什么区别和相似之处？**

虽然IPVS和iptables都基于Netfilter，但它们用途不同。iptables更像是个防火墙工具，而IPVS则专门为高性能负载均衡设计。在大规模集群中，IPVS表现更好，支持多种负载均衡算法，比如最少连接和加权等。此外，IPVS还自带健康检查和连接重试功能。



**Kubernetes支持几种常见的调度方式**

- **Deployment/ReplicationController**：自动管理Pod的多副本，保持指定数量的副本运行。

- **NodeSelector**：通过标签将Pod固定调度到特定节点上。

- **NodeAffinity**：定义Pod和节点的亲和性规则，分为硬性必须满足的规则和可优先选择的软规则。

- **Taints和Tolerations**：Taints可以让**节点**拒绝某些Pod，而Toleration允许 **Pod** 在标记了Taint的节点上运行。
  kubectl taint nodes node1 key=value:NoSchedule

  > | Effect               | 作用                                                   |
  > | -------------------- | ------------------------------------------------------ |
  > | **NoSchedule**       | 不允许没有 Toleration 的 Pod 调度到这个节点            |
  > | **PreferNoSchedule** | 尽量避免调度到这个节点，但不是强制的                   |
  > | **NoExecute**        | 不允许调度，同时会驱逐已经存在且没有 Toleration 的 Pod |







## MySQL

```bash


## 创建数据目录文件夹（mysql 运行时的数据目录，约定好的）
mkdir /var/lib/mysql
## 配置文件
mkdir /opt/mysql_config

# 创建 Secret 对象
## 创建 secret，generic：基于普通文本格式，--from-literal：从字面量创建
kubectl create secret generic mysql-auth --from-literal=username=root --from-literal=password=151613 -n garmysql
kubectl get secret mysql-auth
kubectl get secret mysql-auth -o yaml
echo MTUxNjEz | base64 -d

# ConfigMap 存储 Mysql 配置文件	my.cnf
vim mysql-config.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: mysql-config
  labels:
    app: mysql
data:
  my.cnf: |-
    [client]
    default-character-set=utf8mb4
    [mysql]
    default-character-set=utf8mb4
    [mysqld] 
    max_connections = 2000
    secure_file_priv=/var/lib/mysql
    sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION

kubectl create ns garmysql
kubectl create -f mysql-config.yaml -n garmysql

# 创建 PV、PVC	Skip
vim mysql-storage.yaml
apiVersion: v1
kind: PersistentVolume
metadata:
  name: mysql
  labels:
    app: mysql             # 设置 pv 的 label 标签
spec:
  capacity:          
    storage: 39Gi
  accessModes:       
  - ReadWriteOnce
  mountOptions:
  - hard
  - nfsvers=4.1
  nfs:
    server: 192.168.2.11
    path: /nfs/mysql       # 指定 NFS 共享目录的位置，且需提前在该目录中创建 mysql 目录
  persistentVolumeReclaimPolicy: Retain  
---
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: mysql
spec:
  resources:
    requests:
      storage: 39Gi
  accessModes:
  - ReadWriteOnce
  selector:
    matchLabels:
      app: mysql

# 创建 Deployment
vim mysql-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
  labels:
    app: mysql
  namespace: garmysql
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - name: gardenia-mysql
        image: mysql:8.2.0
        imagePullPolicy: IfNotPresent
        ports:
        - containerPort: 3306
        volumeMounts:
        - name: data
          mountPath: /var/lib/mysql
        - name: config
          mountPath: /etc/mysql/conf.d
        env:
        - name: MYSQL_ROOT_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysql-auth
              key: password
      volumes:
      - name: data
        hostPath:
          path: /var/lib/mysql
          type: Directory
      - name: config
        hostPath:
          path: /etc/mysql/conf
          type: Directory

kubectl apply -f mysql-deployment.yaml
kubectl get pods -n garmysql
kubectl describe pods mysql-fdd58cd88-dkzqc -n garmysql
kubectl logs mysql-fdd58cd88-cf5l5 -n garmysql

# 创建 Service
vim mysql-service.yaml
apiVersion: v1
kind: Service
metadata:
  name: mysql-service
  namespace: garmysql
spec:
  selector:
    app: mysql
  type: NodePort
  ports:
  - name: mysql
    protocol: TCP
    port: 3306
    targetPort: 3306
    nodePort: 30169

kubectl apply -f mysql-service.yaml
kubectl get svc -n garmysql

mysql -h106.14.45.61 -P30169 -uroot -p151613
kubectl get all -n garmysql
```

> https://cloud.tencent.com/developer/article/1783227

```bash
vim /run/flannel/subnet.env

FLANNEL_NETWORK=10.244.0.0/16
FLANNEL_SUBNET=10.244.0.1/24
FLANNEL_MTU=1450
FLANNEL_IPMASQ=true
```





## KuBoard

```bash
## 线安装
wget https://addons.kuboard.cn/kuboard/kuboard-v3.yaml
kubectl apply -f https://addons.kuboard.cn/kuboard/kuboard-v3.yaml

kubectl get pods -n kuboard
kubectl get all -n kube-system | grep kuboard

echo $(kubectl -n kube-system get secret $(kubectl -n kube-system get secret | grep kuboard-user | awk '{print $1}') -o go-template='{{.data.token}}' | base64 -d)
```





# Nginx

## Download

```bash
vim default.conf
server {
    listen       80;
    server_name gardenia.com;
    error_page 500  502 504 503  /50x.html;
    
    location /download {
          alias  /usr/local/webapp/download/;
          sendfile on;
          autoindex on;  # 开启目录文件列表
          autoindex_exact_size on;  # 显示出文件的确切大小，单位是bytes
          autoindex_localtime on;  # 显示的文件时间为文件的服务器时间
          charset utf-8,gbk;  # 避免中文乱码
    }
}
kubectl create configmap nginx-config --from-file=./default.conf
kubectl describe pod/nginxsvcdp-57b9f6b5df-cfjf4

mkdir -p /mnt/nginx
kubectl apply -f deploy.yaml
```

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginxsvcdp
spec:
  selector:
    matchLabels:
      app: nginx
  replicas: 1
  minReadySeconds: 1
  strategy:
      rollingUpdate:
          maxSurge: 1
          maxUnavailable: 0
      type: RollingUpdate
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx
        ports:
        - containerPort: 80
        volumeMounts:
        -  mountPath: /etc/nginx/conf.d
           name: config
        -  mountPath: /usr/local/webapp/download/
           name: data
      volumes:
        - name: data
          hostPath:
            path: /mnt/nginx
            type: Directory
        -  name: config
           configMap:
              name: nginx-config
              items:
              -  key: default.conf
                 path: ./default.conf
 
---
apiVersion: v1
kind: Service
metadata:
    name: nginxdpsvc
spec:
    type: NodePort
    selector:
        app: nginx
    ports:
    -  port: 80
       targetPort: 80
       nodePort: 30001
```



