#!/bin/bash

yum -y update
yum install -y conntrack ipvsadm ipset jq sysstat curl iptables libseccomp

# 01 安装必要的依赖
sudo yum install -y yum-utils \
  device-mapper-persistent-data \
  lvm2


# 02 设置docker仓库
sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

# 【设置要设置一下阿里云镜像加速器】
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://j16wttpi.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload


# 03 安装docker
yum install -y docker-ce docker-ce-cli containerd.io
#yum install -y docker-ce-20.10.14 docker-ce-cli-20.10.14 containerd.io

# 04 启动docker
sudo systemctl start docker && sudo systemctl enable docker
