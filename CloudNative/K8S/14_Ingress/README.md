# Kubernetes 核心技术 Ingress

## 前言

原来需要将端口号对外暴露，通过 ip + 端口号就可以进行访问，使用 Service 中的 NodePort 来实现

- 在每个节点上都会启动端口
- 在访问的时候通过任何节点，通过ip + 端口号就能实现访问

但是 NodePort 还存在一些缺陷

- 因为端口不能重复，所以每个端口只能使用一次，一个端口对应一个应用
- 实际访问中都是用域名，根据不同域名跳转到不同端口服务中

## Ingress 和 Pod 关系

pod 和 ingress 是通过 Service 进行关联的，而 ingress 作为统一入口，由 service 关联一组 pod 中

![image-20201118102637839](images/image-20201118102637839.png)

- 首先 service 就是关联的 pod
- 然后 ingress 作为入口，首先需要到 service，然后发现一组 pod
- 发现 pod 后，就可以做负载均衡等操作

## Ingress 工作流程

在实际的访问中，都是需要维护很多域名， a.com  和  b.com

然后不同的域名对应的不同的 Service，然后 Service 管理不同的 pod

![image-20201118102858617](images/image-20201118102858617.png)

需要注意，ingress不是内置的组件，需要单独的安装

## 使用 Ingress

步骤如下所示

- 部署ingress Controller【需要下载官方的】
- 创建ingress规则【对哪个Pod、名称空间配置规则】

### 创建 Nginx Pod

创建一个nginx应用，然后对外暴露端口

```bash
# 创建pod
kubectl create deployment web --image=nginx
# 查看
kubectl get pods
```

对外暴露端口

```bash
kubectl expose deployment web --port=80 --target-port=80 --type:NodePort
```

### 部署 ingress controller

下面来通过yaml的方式，部署的ingress，配置文件如下所示

![image-20201118105427248](images/image-20201118105427248.png)

这个文件里面，需要注意的是 hostNetwork: true，改成ture是为了让后面访问到

```bash
kubectl apply -f ingress-con.yaml
```

通过这种方式，其实在外面就能访问，这里还需要在外面添加一层

```bash
kubectl apply -f ingress-con.yaml
```

![image-20201118111256631](images/image-20201118111256631.png)

最后通过下面命令，查看是否成功部署 ingress

```bash
kubectl get pods -n ingress-nginx
```

![image-20201118111424735](images/image-20201118111424735.png)

### Ingress 规则文件

创建 ingress 规则文件，ingress-h.yaml

![image-20201118111700534](images/image-20201118111700534.png)

### 添加域名访问规则

在windows 的 hosts文件，添加域名访问规则【因为没有域名解析，所以只能这样做】

![image-20201118112029820](images/image-20201118112029820.png)

最后通过域名就能访问

![image-20201118112212519](images/image-20201118112212519.png)