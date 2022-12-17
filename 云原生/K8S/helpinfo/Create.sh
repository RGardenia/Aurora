#!/bin/bash

K8S_VERSION=1.23.0
KUBE_VERSION=v1.20.15
KUBE_PAUSE_VERSION=3.2
ETCD_VERSION=3.4.13-0
CORE_DNS_VERSION=1.7.0

# 设置master的hostname，并且修改hosts文件
sudo hostnamectl set-hostname master01

vi /etc/hosts
192.168.1.4 master01
192.168.1.5 w1
192.168.1.6 w2


# (1)关闭防火墙
systemctl stop firewalld && systemctl disable firewalld

# (2)关闭selinux
setenforce 0
sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config

# (3)关闭swap
swapoff -a
sed -i '/swap/s/^\(.*\)$/#\1/g' /etc/fstab

# (4)配置iptables的ACCEPT规则
iptables -F && iptables -X && iptables -F -t nat && iptables -X -t nat && iptables -P FORWARD ACCEPT

# (5)设置系统参数
cat <<EOF >  /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF

sysctl --system


# 配置 kubernetes yum 源
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=http://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=http://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg
       http://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF

# 安装 kubeadm & kubelet & kubectl
yum list kubeadm --showduplicates | sort -r
yum install -y kubeadm-$K8S_VERSION kubelet-$K8S_VERSION kubectl-$K8S_VERSION


# docker 和 k8s 设置同一个cgroup
vi /etc/docker/daemon.json
    "exec-opts": ["native.cgroupdriver=systemd"],

systemctl restart docker
# kubelet，这边如果发现输出directory not exist，也说明是没问题的，大家继续往下进行即可
sed -i "s/cgroup-driver=systemd/cgroup-driver=cgroupfs/g" /etc/systemd/system/kubelet.service.d/10-kubeadm.conf
systemctl enable kubelet && systemctl start kubelet


set -e

GCR_URL=k8s.gcr.io
ALIYUN_URL=registry.cn-hangzhou.aliyuncs.com/google_containers

images=(kube-proxy:${KUBE_VERSION}
kube-scheduler:${KUBE_VERSION}
kube-controller-manager:${KUBE_VERSION}
kube-apiserver:${KUBE_VERSION}
pause:${KUBE_PAUSE_VERSION}
etcd:${ETCD_VERSION}
coredns:${CORE_DNS_VERSION})

for imageName in ${images[@]} ; do
  docker pull $ALIYUN_URL/$imageName
  docker tag  $ALIYUN_URL/$imageName $GCR_URL/$imageName
  docker rmi $ALIYUN_URL/$imageName
done

# calico 网络插件
# 在k8s中安装calico
kubectl apply -f https://docs.projectcalico.org/v3.9/manifests/calico.yaml

# 确认一下calico是否安装成功
kubectl get pods --all-namespaces -w
kubectl get pods -n kube-system -w

