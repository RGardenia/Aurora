# Kubernetes 集群 YAML 文件详解

## 概述

​	k8s 集群中对资源管理和资源对象编排部署都可以通过声明样式（YAML）文件来解决，也就是可以把需要对资源对象操作编辑到YAML 格式文件中，这种文件叫做资源清单文件，通过kubectl 命令直接使用资源清单文件就可以实现对大量的资源对象进行编排部署了。一般在开发的时候，都是通过配置YAML文件来部署集群的。

YAML文件：就是资源清单文件，用于资源编排

## YAML 文件介绍

### YAML 概述

YAML ：仍是一种标记语言。为了强调这种语言以数据做为中心，而不是以标记语言为重点。

YAML 是一个可读性高，用来表达数据序列的格式。

### YAML 基本语法

* 使用空格做为缩进
* 缩进的空格数目不重要，只要相同层级的元素左侧对齐即可
* 低版本缩进时不允许使用 Tab 键，只允许使用空格
* 使用 `#` 标识注释，从这个字符一直到行尾，都会被解释器忽略
* 使用 `---` 表示新的 `yaml` 文件开始

### YAML 支持的数据结构

#### **纯量**

单个的、不可再分的值，也就是指的一个简单的值，字符串、布尔值、整数、浮点数、Null、时间、日期。

```yml
# 1 布尔类型
c1: true (或者True)
# 2 整型
c2: 234
# 3 浮点型
c3: 3.14
# 4 null类型 
c4: ~  # 使用~表示null
# 5 日期类型
c5: 2018-02-17    # 日期必须使用ISO 8601格式，即 yyyy-MM-dd
# 6 时间类型
c6: 2018-02-17T15:02:31+08:00  # 时间使用ISO 8601格式，时间和日期之间使用T连接，最后使用+代表时区
# 7 字符串类型
c7: heima     # 简单写法，直接写值 , 如果字符串中间有特殊字符，必须使用双引号或者单引号包裹 
c8: line1
    line2     # 字符串过多的情况可以拆成多行，每一行会被转化成一个空格
```

#### 对象

键值对的集合，又称为映射 (mapping) / 哈希（hashes） / 字典（dictionary）

```yaml
# 对象类型：对象的一组键值对，使用冒号结构表示
paopao:
  name: Gardenia
  address: ShangHai

# yaml 也允许另一种写法，将所有键值对写成一个行内对象
hash: {name: Tom, age: 18}
```

#### 数组

```bash
# 数组类型：一组连词线开头的行，构成一个数组
People
- Tom
- Jack

# 数组也可以采用行内表示法
People: [Tom, Jack]
```



## YAML 文件组成部分

主要分为了两部分，一个是 **控制器的定义** 和 **被控制的对象**

**控制器的定义**

![image-20201114110444032](images/image-20201114110444032.png)

**被控制的对象**

包含一些 **镜像，版本、端口** 等

![image-20201114110600165](images/image-20201114110600165.png)

### 属性说明

在一个 YAML 文件的控制器定义中，有很多属性名称

|  属性名称  |    介绍    |
| :--------: | :--------: |
| apiVersion |  API 版本  |
|    kind    |  资源类型  |
|  metadata  | 资源元数据 |
|    spec    |  资源规格  |
|  replicas  |  副本数量  |
|  selector  | 标签选择器 |
|  template  |  Pod 模板  |
|  metadata  | Pod 元数据 |
|    spec    |  Pod 规格  |
| containers |  容器配置  |



## 快速编写 YAML 文件

一般来说，很少自己手写YAML文件，因为这里面涉及到了很多内容，一般都会借助工具来创建

### kubectl create 命令

这种方式一般用于资源没有部署的时候，可以直接创建一个 YAML 配置文件

```bash
# 尝试运行, 并不会真正的创建镜像
kubectl create deployment web --image=nginx -o yaml --dry-run
```

或者可以输出到一个文件中

```bash
kubectl create deployment web --image=nginx -o yaml --dry-run > hello.yaml
```

### kubectl get 命令导出 yaml 文件

可以首先查看一个目前已经部署的镜像

```bash
kubectl get deploy
```

![image-20201114113115649](images/image-20201114113115649.png)

然后导出 Pod 配置

```bash
kubectl get deploy nginx -o=yaml --export > nginx.yaml
```

然后会生成一个 `nginx.yaml` 的配置文件

![image-20201114184538797](images/image-20201114184538797.png)

<hr>

## 命令式对象配置

命令配合配置文件一起来操作 kubernetes 资源

```yml
apiVersion: v1
kind: Namespace
metadata:
  name: paopao
---
apiVersion: v1
kind: Pod
metadata:
  name: nginx
  namespace: paopao
spec:
  containers:
  - name: nginx-containers
    image: nginx:latest
```

```bash
kubectl create -f nginx.yml
kubectl get -f nginx.yml
kubectl delete -f nginx.yml
```

## 声明式对象配置

```bash
for i in {1..3}; do kubectl apply -f nginx.yaml; done

for i in $(seq 1 3); do kubectl apply -f nginx.yaml; done

for i in $(seq 1 1 3); do kubectl apply -f nginx.yaml; done
```

声明式对象配置就是使用apply描述一个资源最终的状态（在 `yaml` 中定义状态）

- 使用 apply 操作资源：如果资源不存在，就创建，相当于 `kubectl create`
- 如果资源已存在，就更新，相当于 `kubectl patch`



## 资源配额及标签



```bash
# 切换命名空间	kubectl get pods 不指定默认为切换的命名空间
kubectl config set-context --current --namespace=kube-system

# 查看哪些资源属于命名空间级别
kubectl api-resources --namespaced=true
```



### namespace 资源限额

namespace 作为命名空间，有很多资源，那么可以对命名空间资源做限制，防止该命名空间部署的资源超过限制。

```yml
apiVersion: v1 
kind: ResourceQuota
metadata:
  name: mem-cpu-quota
  namespace: test
spec:
  hard:
    requests.cpu: "2"
    requests.memory: 2Gi
    limits.cpu: "4"
    limits.memory: 4Gi
```

创建的 `ResourceQuota` 对象将在 `test` 名字空间中添加以下限制：

- 每个容器必须设置内存请求（memory request），内存限额（memory limit），cpu 请求（cpu request）和 cpu 限额（cpu limit）
- 所有容器的内存请求总额不得超过 2 GiB
- 所有容器的内存限额总额不得超过 4 GiB
- 所有容器的 CPU 请求总额不得超过 2 CPU
- 所有容器的 CPU 限额总额不得超过 4 CPU

```bash
kubectl describe ns test

# 创建 pod 时候必须设置资源限额，否则创建失败
apiVersion: v1 
kind: Pod 
metadata: 
  name: pod-test 
  namespace: test 
  labels: 
    app: tomcat-pod-test 
spec: 
  containers: 
  - name: tomcat-test
    ports: 
    - containerPort: 8080 
    image: tomcat
    imagePullPolicy: IfNotPresent 
    resources: 
      requests: 
        memory: "100Mi" 
        cpu: "500m" 
      limits: 
        memory: "2Gi" 
        cpu: "2" 
```



### 标签

​	标签其实就一对 `key/value` ，被关联到对象上，比如 Pod，标签的使用倾向于能够表示对象的特殊特点

​	标签可以用来划分特定的对象（比如版本，服务类型等），标签可以在创建一个对象的时候直接定义，也可以在后期随时修改，每一个对象可以拥有多个标签，但是，`key` 值必须是唯一的。创建标签之后也可以方便对资源进行分组管理。如果对 Pod 打标签，之后就可以使用标签来查看、删除指定的 Pod。在 k8s 中，大部分资源都可以打标签。

```bash
# 对已经存在的 pod 打标签  表示这个 Pod版本是 v1
kubectl label pods pod-first release=v1

# 查看标签是否打成功 查看指定的 Pod
kubectl get pods pod-first --show-labels

# 查看资源标签
kubectl get pods --show-labels

# 列出默认名称空间下标签 key 是 release 的 pod，不显示标签
kubectl get pods -l release

# 列出默认名称空间下标签 key 是 release、值是 v1 的 pod，不显示标签
kubectl get pods -l release=v1

# 列出默认名称空间下标签 key 是 release 的所有 pod，并打印对应的标签值 key 作为一列 value 在这一列显示出来
kubectl get pods -L release

# 查看所有名称空间下的所有 pod 的标签
kubectl get pods --all-namespaces --show-labels

# 把具有 release 标签的 Pod 显示出来并且显示对应的 key 和 value
kubectl get pods -l release=v1 -L release
```







