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
# reboot

## 部署节点与其他节点互信
ssh-keygen
ssh-copy-id -i id_rsa.pub <hostname>

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

# 内核优化
#执行kubeadm init前，要做参数优化
vim /etc/modules-load.d/modules.conf
ip_vs
br_netfilter
#加载模块使其生效
modprobe ip_vs
modprobe br_netfilter
#调整内核参数
vim /etc/sysctl.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1 #桥接网络模式，流量的分发过滤
net.ipv4.ip_forward = 1 #开启内核路由转发功能
vm.max_map_count=262144
kernel.pid_max=4194303
fs.file-max=1000000
net.ipv4.tcp_max_tw_buckets=6000
net.netfilter.nf_conntrack_max=2097152
vm.swappiness=0
sysctl -p #使其生效
```

**安装工具 kubeadm**

```bash
# 安装kubeadm、kubectl、kubelet 
#配置阿里镜像加速
apt-get update && apt-get install -y apt-transport-https
curl https://mirrors.aliyun.com/kubernetes/apt/doc/apt-key.gpg | apt-key add -  
# 执行此步骤报错可能需要执行 sudo apt-get install -y gnupg
cat <<EOF >/etc/apt/sources.list.d/kubernetes.list
deb https://mirrors.aliyun.com/kubernetes/apt/ kubernetes-xenial main
EOF
apt-get update
apt-cache madison kubeadm
apt-get install kubeadm=1.24.3-00 kubectl=1.24.3-00 kubelet=1.24.3-00
```

**安装 containerd**

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

