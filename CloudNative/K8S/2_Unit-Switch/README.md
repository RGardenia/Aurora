# Unit



```bash
# master 主机需要开放
6443             Kubernetes API server
2379-2380        etcd server client API
10250            Kubelet API
10259            kube-scheduler
10257            kube-controller-manager
 
# node 主机需要开放
10250            Kubelet API
30000-32767      NodePort Services
```



# Deploy Minikube

## Ubuntu 

> https://kubernetes.io/zh-cn/docs/tasks/tools/install-kubectl-linux/#install-using-other-package-management
> https://minikube.sigs.k8s.io/docs/start/

```bash
sudo apt-get update
# apt-transport-https 可以是一个虚拟包
sudo apt-get install -y apt-transport-https ca-certificates curl

curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.28/deb/Release.key | sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg

# 源	覆盖 /etc/apt/sources.list.d/kubernetes.list 中的所有现存配置
echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.28/deb/ /' | sudo tee /etc/apt/sources.list.d/kubernetes.list

sudo apt-get update
sudo apt-get install -y kubectl
# OR
snap install kubectl --classic
kubectl version --client

curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube_latest_amd64.deb
sudo dpkg -i minikube_latest_amd64.deb

minikube config set driver docker

minikube start --force --driver=docker

# 部署 Dashboard
minikube dashboard
### kubectl proxy --port=8001 --address='120.46.36.46' --accept-hosts='^.*' &
kubectl expose service kubernetes-dashboard --type=NodePort --name=kube-dashboard-service --port=8001 --target-port=80
nohup kubectl proxy --port=8001 --address='0.0.0.0' --accept-hosts='^.*' &
```

## Centos	K8S

> https://blog.csdn.net/gudongkun1121/article/details/132126441

```bash
cat /etc/redhat-release
cat /etc/os-release

hostnamectl set-hostname gardenia
cat >> /etc/hosts << EOF  
106.14.45.61 gardenia
EOF

# 时间同步
systemctl start chronyd &&
systemctl enable chronyd

# 禁用 selinux
cat /etc/selinux/config
SELINUX=disabled

# 禁用 swap 分区
cat /etc/fstab
/dev/mapper/centos-root /                       xfs     defaults        0 0
UUID=532ab9ca-839e-4ca2-9ac5-b871d9cc7f71 /boot     xfs     defaults        0 0
# /dev/mapper/centos-swap swap                    swap    defaults        0 0

# 创建虚拟网卡
vim /etc/sysconfig/network-scripts/ifcfg-eth0:1
DEVICE=eth0:1
TYPE=Ethernet
ONBOOT=yes
NM_CONTROLLED=yes
BOOTPROTO=static
IPADDR=106.14.45.61
NETMASK=255.255.255.0

systemctl restart NetworkManager
nmcli c reload
# nmcli c up $160

# 配置 iptables
cat <<EOF | tee /etc/modules-load.d/k8s.conf
overlay
br_netfilter
EOF

modprobe overlay
modprobe br_netfilter

cat <<EOF | tee /etc/sysctl.d/kubernetes.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
net.ipv4.ip_forward = 1
EOF
# 重新加载
sysctl --system
# Or sysctl -p
## 加载网桥过滤模块
# 查看模块是否被加载
lsmod | grep br_netfilter
lsmod | grep overlay
# 查看 sysctl 参数是否都为1
sysctl net.bridge.bridge-nf-call-iptables net.bridge.bridge-nf-call-ip6tables net.ipv4.ip_forward

# 配置 ipvs 功能
yum install ipset ipvsadmin -y
# 添加需要加载的模块写入脚本文件
cat << EOF >  /etc/sysconfig/modules/ipvs.modules
#!/bin/bash
modprobe -- ip_vs
modprobe -- ip_vs_rr
modprobe -- ip_vs_wrr
modprobe -- ip_vs_sh
modprobe -- nf_conntrack_ipv4
EOF
# 为脚本文件添加执行权限
chmod +x /etc/sysconfig/modules/ipvs.modules
# 执行脚本文件
/bin/bash /etc/sysconfig/modules/ipvs.modules
# 查看对应的模块是否加载成功
lsmod | grep -e ip_vs -e nf_conntrack_ipv4

# 配置 containerd
wget https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo -O /etc/yum.repos.d/docker-ce.repo
## 若是 Alibaba 的 Centos
sudo vim /etc/yum.repos.d/docker-ce.repo
:%s/$releasever/8/g
## 安装 containerd
yum makecache
yum install containerd.io -y

containerd config default > /etc/containerd/config.toml

vim /etc/containerd/config.toml
# root = "/home/containerd"
[plugins."io.containerd.grpc.v1.cri"]
  sandbox_image = "registry.cn-hangzhou.aliyuncs.com/google_containers/pause:3.9"
[plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc]
  [plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc.options]
    SystemdCgroup = true
# [plugins."io.containerd.grpc.v1.cri".registry.mirrors]
#   [plugins."io.containerd.grpc.v1.cri".registry.mirrors."docker.io"]
#     endpoint = ["https://a79n7bst.mirror.aliyuncs.com"]
[plugins."io.containerd.grpc.v1.cri".containerd.default_runtime]
    runtime_type = "io.containerd.runtime.v1.linux"
#ERROR
FATA[0000] validate service connection: CRI v1 image API is not implemented for endpoint "unix:///run/containerd/containerd.sock": rpc error: code = Unimplemented desc = unknown service runtime.v1.ImageService

systemctl enable containerd
systemctl restart containerd
systemctl status containerd
# sudo yum install -y docker-buildx-plugin docker-compose-plugin

# cri-dockerd	跳过
sudo yum install -y wget
sudo wget https://github.com/Mirantis/cri-dockerd/releases/download/v0.3.4/cri-dockerd-0.3.4-3.el7.x86_64.rpm
sudo rpm -ivh cri-dockerd-0.3.4-3.el7.x86_64.rpm
sudo systemctl daemon-reload
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://c12xt3od.mirror.aliyuncs.com"]
}
EOF
vim /usr/lib/systemd/system/cri-docker.service
ExecStart=/usr/bin/cri-dockerd --network-plugin=cni --pod-infra-container-image=registry.aliyuncs.com/google_containers/pause:3.7
## 重载系统守护进程
sudo systemctl daemon-reload
## 设置 cri-dockerd 自启动
sudo systemctl enable cri-docker.socket cri-docker
## 启动 cri-dockerd
sudo systemctl start cri-docker.socket cri-docker
## 检查 Docker 组件状态
sudo systemctl status docker cir-docker.socket cri-docker

# 安装 kubeadm、kubelet 和 kubectl
## 配置 yum 源
cat << EOF | tee /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.cloud.tencent.com/kubernetes/yum/repos/kubernetes-el7-x86_64/
enabled=1
gpgcheck=0
repo_gpgcheck=0
EOF

# 这里注意安装版本
yum install -y kubelet kubeadm kubectl --disableexcludes=kubernetes
systemctl enable kubelet

sudo crictl config runtime-endpoint unix:///var/run/containerd/containerd.sock

## 配置 kubelet cgroup	Docker 跳过
vim /etc/sysconfig/kubelet
KUBELET_CGROUP_ARGS="--cgroup-driver=systemd"
KUBE_PROXY_MODE="ipvs"

# 安装 runc
sudo wget https://github.com/opencontainers/runc/releases/download/v1.1.10/runc.amd64
sudo install -m 755 runc.amd64 /usr/local/bin/runc
runc -v

# 初始化 集群
## 镜像
kubeadm config images list
kubeadm config images pull
## 初始化 Master
kubeadm init  \
--kubernetes-version=v1.28.5 \
--image-repository=registry.aliyuncs.com/google_containers \
--apiserver-advertise-address=106.14.45.61 \
--apiserver-cert-extra-sans=106.14.45.61 \
--pod-network-cidr=10.244.0.0/16 \
--service-cidr=10.96.0.0/12
# apiserver-advertise-address 指定为自己内网 IP
# --control-plane-endpoint=gardenia
# --cri-socket=unix:///var/run/cri-dockerd.sock

# 切入 Docker
kubeadm reset --cri-socket unix:///var/run/cri-dockerd.sock
kubeadm init --config=init-config.yaml

kubeadm config print init-defaults > init-config.yaml

# 创建必要文件
mkdir -p $HOME/.kube &&
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config &&
sudo chown $(id -u):$(id -g) $HOME/.kube/config

# 让 master 参与服务调度，不做 control-plane
kubectl taint nodes --all node-role.kubernetes.io/control-plane-
kubectl label nodes gardenia node-role.kubernetes.io/control-plane
kubectl label node --all kubernetes.io/role=master

# 安装网络附加组件	https://github.com/flannel-io/flannel/releases
# ctr -n k8s.io images import flanneld-v0.24.0-amd64.docker
vim kube-flannel.yml
wget https://github.com/flannel-io/flannel/blob/master/Documentation/kube-flannel.yml
kubectl apply -f kube-flannel.yml

kubectl get pods --all-namespaces -o wide
kubectl get nodes
kubectl get cs

# 部署 Dashboard	https://github.com/kubernetes/dashboard/releases	Skip
crictl pull docker.io/kubernetesui/dashboard:v2.7.0
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml
kubectl delete -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml

kubectl get svc -n kubernetes-dashboard
kubectl get pods -n kubernetes-dashboard
## 暴露端口
kubectl edit svc kubernetes-dashboard -n kubernetes-dashboard
type: NodePort

## Nginx 代理
### kubectl create configmap nginx-conf --from-file nginx.conf
### kubectl get configmap
cat >> dash-usr.yaml << EOF  
apiVersion: v1
kind: ServiceAccount
metadata:
  name: admin-user
  namespace: kubernetes-dashboard
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: admin-user
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: admin-user
  namespace: kubernetes-dashboard
EOF

kubectl apply -f dash-usr.yaml
kubectl -n kubernetes-dashboard get secret $(kubectl -n kubernetes-dashboard get sa/admin-user -o jsonpath="{.secrets[0].name}") -o go-template="{{.data.token | base64decode}}"
# https://blog.csdn.net/a437936609/article/details/122325282
```



## FAQ

```bash

1. [kubelet-check] Initial timeout of 40s passed.
crictl --runtime-endpoint unix:///var/run/containerd/containerd.sock ps -a | grep kube | grep -v pause
# apiserver 停了

# 不用担心名称不同，镜像 id 是一样的，会被 k8s 识别到
crictl pull registry.aliyuncs.com/google_containers/kube-apiserver:v1.28.5 &&
crictl pull registry.aliyuncs.com/google_containers/kube-controller-manager:v1.28.5 &&
crictl pull registry.aliyuncs.com/google_containers/kube-scheduler:v1.28.5 &&
crictl pull registry.aliyuncs.com/google_containers/kube-proxy:v1.28.5 &&
crictl pull registry.aliyuncs.com/google_containers/pause:3.9 &&
crictl pull registry.aliyuncs.com/google_containers/etcd:3.5.9-0 &&
crictl pull registry.aliyuncs.com/google_containers/coredns:v1.10.1

vim bash.sh
images=(
    kube-apiserver:v1.28.5
    kube-controller-manager:v1.28.5
    kube-scheduler:v1.28.5
    kube-proxy:v1.28.5
    pause:3.9
    etcd:3.5.9-0
    coredns:v1.10.1
)
for imageName in ${images[@]} ; do
	docker pull registry.cn-hangzhou.aliyuncs.com/google_containers/$imageName
	docker tag registry.cn-hangzhou.aliyuncs.com/google_containers/$imageName	registry.k8s.io/$imageName
	docker rmi registry.cn-hangzhou.aliyuncs.com/google_containers/$imageName
done
docker tag registry.k8s.io/coredns:v1.10.1 registry.k8s.io/coredns/coredns:v1.10.1

chmod +x bash.sh
./bash.sh

docker images --format "{{.Repository}}:{{.Tag}}" | grep 'k8s.gcr.io' | awk '{print $1}' | xargs docker rmi
```

### Nginx Config

```bash
yum -y install gcc pcre-devel zlib-devel openssl openssl-devel &&
wget http://nginx.org/download/nginx-1.23.0.tar.gz &&
tar -zxvf nginx-1.23.0.tar.gz &&
cd nginx-1.23.0 &&
./configure --prefix=/usr/local/nginx --with-http_stub_status_module --with-http_ssl_module &&
make & make install &&
cd /usr/local/nginx/ &&
./sbin/nginx -t

vim /usr/local/nginx/conf/nginx.conf
location / {
    proxy_pass https://localhost:30098/;
}

/usr/local/nginx/sbin/nginx
```



# Switch



## Docker To

```bash
crictl info
# Config
vim /etc/containerd/config.toml

[plugins."io.containerd.grpc.v1.cri".registry]
      [plugins."io.containerd.grpc.v1.cri".registry.mirrors]
        [plugins."io.containerd.grpc.v1.cri".registry.mirrors."docker.io"]
          endpoint = ["https://registry-1.docker.io"]
        [plugins."io.containerd.grpc.v1.cri".registry.mirrors."registry.cn-shanghai.aliyuncs.com"]
          endpoint = ["https://registry.cn-hangzhou.aliyuncs.com"]
      [plugins."io.containerd.grpc.v1.cri".registry.configs]
        [plugins."io.containerd.grpc.v1.cri".registry.configs."registry.cn-shanghai.aliyuncs.com".tls]
          insecure_skip_verify = true
        [plugins."io.containerd.grpc.v1.cri".registry.configs."registry.cn-shanghai.aliyuncs.com".auth]
          username = "GardeniaR"
          password = "1516Chen"

systemctl restart containerd.service

crictl pull registry.cn-shanghai.aliyuncs.com/gardenia_hadoop/centos-hadoop-kafka
# 至此，镜像准备完成 ~

kubectl get svc -n kubernetes-dashboard

# 获取 Token
kubectl create token --namespace kubernetes-dashboard --duration 2592000s kubernetes-dashboard
eyJhbGciOiJSUzI1NiIsImtpZCI6ImpwSVg3UDJyM3lUNVBNeWpmME5razIwdTNTSjY5dVVlQ25pSGlBZldjWFkifQ.eyJhdWQiOlsiaHR0cHM6Ly9rdWJlcm5ldGVzLmRlZmF1bHQuc3ZjLmNsdXN0ZXIubG9jYWwiXSwiZXhwIjoxNzAyNDUzMzM2LCJpYXQiOjE2OTk4NjEzMzYsImlzcyI6Imh0dHBzOi8va3ViZXJuZXRlcy5kZWZhdWx0LnN2Yy5jbHVzdGVyLmxvY2FsIiwia3ViZXJuZXRlcy5pbyI6eyJuYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsInNlcnZpY2VhY2NvdW50Ijp7Im5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsInVpZCI6ImE1ZjMzOTQ3LTM5MTMtNGQwYi1hMGI3LWVkNDMwNjAzMjFjNiJ9fSwibmJmIjoxNjk5ODYxMzM2LCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZXJuZXRlcy1kYXNoYm9hcmQ6a3ViZXJuZXRlcy1kYXNoYm9hcmQifQ.coNkWOv5GT9slBHkG1gWdRSh27l6svFuJB1hSuEOdo8ew9u9TgqYszeFhavOwOkLtiThubvcgX5jpQVRoTte5g0KVToJZdjwnqLntDPXM6NEeiazjch12NPYs9jKJhAGTrtAFTBx2mLy6bu69Ud8fT4a_j4Wg5eqNoIuqIc81CmvXkE9ErGMfy7BVy1rg10f8YEOVY9orTOYPtG600oErnpWeHKsH1_RK9rrpR1OuZ2MrQkEEcwvLErP-QXPGTIF4j8CFFL0DRd8QVaIbvWpCe68qBDaRTJv7dJ9x885tT0HoYQ_V_6WWzHFCqFvZognQUJFedzGLHIfnvrTmz26oQ


kubectl create ns hadoop

kubectl apply -f /opt/centos-hadoop-kafka.yml
kubectl get pods -n hadoop --show-labels -o wide
kubectl get svc -n hadoop

kubectl expose deploy hadoop1 --port=22 --target-port=22 --type=NodePort

vim /opt/centos-hadoop-kafka.yml

ssh hadoop1.hadoop.svc

curl -I hadoop1.hadoop.svc:9870
ss -tlnp | grep 22
```

```yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: hadoop1
  namespace: hadoop
  labels:
    node: hadoop1
spec:
  replicas: 1
  selector:
    matchLabels:
      node: hadoop1
  template:
    metadata:
      labels:
        node: hadoop1
    spec:
      hostname: hadoop1
      containers:
      - name: hadoop1
        image: registry.cn-shanghai.aliyuncs.com/gardenia_hadoop/centos-hadoop-kafka
        securityContext:
          privileged: true
        command: ["/usr/sbin/init", "-c","--"]
        ports:
        - containerPort: 22
        - containerPort: 9870
        - containerPort: 19888
        - containerPort: 50070
        - containerPort: 8088
        - containerPort: 9001
---
apiVersion: v1
kind: Service
metadata:
  name: hadoop1
  namespace: hadoop
spec:
  selector:
    node: hadoop1
  ports:
  - name: port-22
    port: 22
    targetPort: 22
  - name: port-9870
    port: 9870
    targetPort: 9870
  - name: port-19888
    port: 19888
    targetPort: 19888
  - name: port-50070
    port: 50070
    targetPort: 50070
  - name: port-8088
    port: 8088
    targetPort: 8088
  - name: port-9001
    port: 9001
    targetPort: 9001
```



















