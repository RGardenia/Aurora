# K8S_Containerd Cluster

[Kubernetes 版本： v1.26](https://v1-26.docs.kubernetes.io/zh-cn/docs/setup/production-environment/tools/kubeadm/install-kubeadm/)

背景介绍
`kubernetes 1.24` 版本正式弃用 docker， 开始使用 `containerd` 作为容器运行时

运行时介绍

- OCI(Open Container Initiative)：2015年Google、docker、Redhat、IBM共同成立，定义了运行标准和镜像标准
- CRI(Container Runtime Interface)：2016 年12月Kubernetes 发布 CRI(容器运行时接口), 可以支持rkt等不同的运行时
- CRI-O：由redhat发起并开源，用于替代docker成为kubernetes的运行时，2016年开发,2019年4月8号进入CNCF孵化

## 集群规划与 K8S 安装

| 类型   | 服务器IP      | 主机名   | 配置  |
| ------ | ------------- | -------- | ----- |
| master | 192.168.2.131 | ubuntu01 | 2c/4g |
| node   | 192.168.2.132 | ubuntu02 | 2c/4g |
| node   | 192.168.2.133 | ubuntu03 | 2c/4g |

### 环境准备

```bash
# 设置主机名
hostnamectl set-hostname <newhostname>

yum install -y epel-release
yum install -y net-tools
yum install -y vim

## 关闭防火墙，关闭防火墙开机自启
systemctl stop firewalld
systemctl disable firewalld.service

# reboot
echo "192.168.3.4 master" >> /etc/hosts
echo "192.168.3.5 node1" >> /etc/hosts
echo "192.168.3.6 node2" >> /etc/hosts

# 时间同步
# 启动 chronyd 服务
systemctl start chronyd
# 设置 chronyd 服务开机自启
systemctl enable chronyd
# chronyd 服务启动几秒钟后使用 date 命令验证时间
date
## OR
yum install -y ntpdate
ntpdate ntp.aliyun.com

## 部署节点与其他节点互信
ssh-keygen
# ssh-copy-id -i id_rsa.pub <hostname>
ssh-copy-id -f -i /root/.ssh/id_rsa -p 22 root@master
ssh-copy-id -f -i /root/.ssh/id_rsa -p 22 root@node1
ssh-copy-id -f -i /root/.ssh/id_rsa -p 22 root@node2

# Fix Static Ip
vim /etc/sysconfig/network-scripts/ifcfg-ens160
ONBOOT=yes
BOOTPROTO=static
IPADDR=192.168.3.4
PREFIX=24
NETMASK=255.255.255.0
GATEWAY=192.168.3.1
DNS1=8.8.8.8
# systemctl restart network
# systemctl restart NetworkManager
# systemctl status NetworkManager
# nmcli c reload
# nmcli c up $160
# nmcli n on
# 还有种可能是克隆时 MAC 地址冲突	https://www.codenong.com/cs106874096/

# 将 SELinux 设置为 permissive 模式（相当于将其禁用）
sudo setenforce 0
sudo sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config
# sed -ri 's/SELINUX=enforcing/SELINUX=disabled/' /etc/selinux/config

# 禁用 swap 分区 注释掉 swap 分区一行
# k8s 要求关闭系统的swap。如果不关闭，默认配置下kubelet无法启动。不过可以在启动kubelet时添加命令行参数来解决 --fail-swap-on=false
swapoff -a
sed -i 's/.*swap.*/#&/' /etc/fstab
# 检查关闭swap成功
free -h

# 操作系统内核优化
# 调整内核参数
vim /etc/sysctl.d/k8s.conf
# 桥接网络模式，流量的分发过滤	开启内核路由转发功能
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
net.ipv4.ip_forward = 1
vm.max_map_count=262144
kernel.pid_max=4194303
fs.file-max=1000000
net.ipv4.tcp_max_tw_buckets=6000
net.netfilter.nf_conntrack_max=2097152
vm.swappiness=0

sudo sysctl -p /etc/sysctl.d/k8s.conf
sudo sysctl --system
# 检查是否应用成功
sysctl net.bridge.bridge-nf-call-iptables net.bridge.bridge-nf-call-ip6tables net.ipv4.ip_forward

# 执行 kubeadm init 前，要做参数优化
cat <<EOF | sudo tee /etc/modules-load.d/k8s.conf
ip_vs
overlay
br_netfilter
EOF
## 加载模块使其生效
modprobe overlay
modprobe ip_vs
modprobe br_netfilter
## 查看是否加载
lsmod | grep br_netfilter
lsmod | grep overlay

# 对 product_uuid 校验	确保 MAC 地址唯一
sudo cat /sys/class/dmi/id/product_uuid

# 检查端口是否启用 netcat nc
nc 127.0.0.1 6443

# 配置 IPVS
## kube-proxy 中的 IPVS 实现通过减少对 iptables 的使用来增加可扩展性。当 k8s 集群中的负载均衡配置变多的时候，IPVS能实现比 iptables 更高效的转发性能
# 安装ipset和ipvsadm
yum install -y ipset ipvsadm
# 编写配置文件
cat > /etc/sysconfig/modules/ipvs.modules <<EOF
#!/bin/bash
modprobe -- ip_vs
modprobe -- ip_vs_rr
modprobe -- ip_vs_wrr
modprobe -- ip_vs_sh
modprobe -- nf_conntrack
EOF
# 添加权限并执行
chmod +x /etc/sysconfig/modules/ipvs.modules && bash /etc/sysconfig/modules/ipvs.modules 
# 查看对应的模块是否加载成功
lsmod | grep -e ip_vs -e nf_conntrack

# YUM 源
### 谷歌 YUM 源
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://packages.cloud.google.com/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://packages.cloud.google.com/yum/doc/yum-key.gpg
        https://packages.cloud.google.com/yum/doc/rpm-package-key.gpg
EOF

### 阿里云	报错：修改 repo_gpgcheck=0 跳过验证
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64/
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF

### 华为云：	具体见 https://www.huaweicloud.com/zhishi/Kubernetes.html
cat <<EOF > /etc/yum.repos.d/kubernetes.repo 
[kubernetes] 
name=Kubernetes 
baseurl=https://repo.huaweicloud.com/kubernetes/yum/repos/kubernetes-el7-$basearch 
enabled=1 
gpgcheck=1 
repo_gpgcheck=1 
gpgkey=https://repo.huaweicloud.com/kubernetes/yum/doc/yum-key.gpg https://repo.huaweicloud.com/kubernetes/yum/doc/rpm-package-key.gpg 
EOF

yum check-update  # 清除 yum 缓存
### 执行完成之后 需要刷新 yum 源
yum repolist
```

### **安装工具 kubeadm**

```bash
### install CNI
curl -L -k https://github.com/containernetworking/plugins/releases/download/v1.3.0/cni-plugins-linux-amd64-v1.3.0.tgz
mkdir /opt/cni/bin -p
tar xf cni-plugins-linux-amd64-v1.3.0.tgz -C /opt/cni/bin
cat << EOF | tee /etc/cni/net.d/10-containerd-net.conflist
{
 "cniVersion": "1.0.0",
 "name": "containerd-net",
 "plugins": [
   {
     "type": "bridge",
     "bridge": "cni0",
     "isGateway": true,
     "ipMasq": true,
     "promiscMode": true,
     "ipam": {
       "type": "host-local",
       "ranges": [
         [{
           "subnet": "10.88.0.0/16" 
         }],
         [{
           "subnet": "2001:db8:4860::/64"
         }]
       ],
       "routes": [
         { "dst": "0.0.0.0/0" },
         { "dst": "::/0" }
       ]
     }
   },
   {
     "type": "portmap",
     "capabilities": {"portMappings": true},
     "externalSetMarkChain": "KUBE-MARK-MASQ"
   }
 ]
}
EOF
# 至此，可以进行 复制 ISO 系统镜像

# 安装 kubeadm、kubectl、kubelet 
# 查看可用版本
yum list kubeadm.x86_64 --showduplicates | sort -r
yum list kubelet.x86_64 --showduplicates | sort -r
yum list kubectl.x86_64 --showduplicates | sort -r

apt-cache madison kubeadm

sudo yum install -y kubelet kubeadm kubectl --disableexcludes=kubernetes --setopt=obsoletes=0
sudo systemctl enable --now kubelet
```

### **安装 containerd**

```bash
apt-cache madison  containerd # 验证仓库版版本，不建议二进制方式安装
#推荐二进制方式，以下为具体安装步骤
cd /usr/local/src/
https://github.com/containerd/containerd/releases/download/v1.6.6/containerd-1.6.6-linux-amd64.tar.gz #下载地址
tar xvf containerd-1.6.6-linux-amd64.tar.gz
cp bin/* /usr/local/bin/
vim /lib/systemd/system/containerd.service     #查看service文件，将以下内容复制到containerd.service文件中，修改执行路径ExecStart为/usr/local/bin
----------------------------------------------------
# Copyright The containerd Authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

[Unit]
Description=containerd container runtime
Documentation=https://containerd.io
After=network.target local-fs.target

[Service]
ExecStartPre=-/sbin/modprobe overlay
ExecStart=/usr/local/bin/containerd

Type=notify
Delegate=yes
KillMode=process
Restart=always
RestartSec=5
# Having non-zero Limit*s causes performance problems due to accounting overhead
# in the kernel. We recommend using cgroups to do container-local accounting.
LimitNPROC=infinity
LimitCORE=infinity
LimitNOFILE=infinity
# Comment TasksMax if your systemd version does not supports it.
# Only systemd 226 and above support this version.
TasksMax=infinity
OOMScoreAdjust=-999

[Install]
WantedBy=multi-user.target
-----------------------------------------------------
#默认输出配置文件
mkdir /etc/containerd/
containerd config default > /etc/containerd/config.toml
vim /etc/containerd/config.toml 
sandbox_image = "k8s.gcr.io/pause:3.6" 修改为 sandbox_image = "registry.cn-hangzhou.aliyuncs.com/google_containers/pause:3.7"
#配置镜像加速
[plugins."io.containerd.grpc.v1.cri".registry.mirrors."docker.io"]
  endpoint = ["https://lzpmltr2.mirror.aliyuncs.com"]
  
systemctl restart containerd && systemctl enable containerd #启动设置开机自启动
```

### 安装 kubelet

```bash
### 为了实现docker使用的cgroupdriver与kubelet使用的cgroup的一致性，建议修改如下文件内容。
vim /etc/sysconfig/kubelet
KUBELET_EXTRA_ARGS="--cgroup-driver=systemd"
```

