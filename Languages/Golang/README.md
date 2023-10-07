## Golang





## Install

**Windows**

```bash
go env -w GO111MODULE=on
go env -w GOPROXY=https://goproxy.cn,direct

```

**Linux**

```bash
# 下载
wget https://studygolang.com/dl/golang/go1.20.6.linux-amd64.tar.gz
gunzip go1.20.6.linux-amd64.tar.gz
tar -xvf go1.20.6.linux-amd64.tar
# 配置系统变量
mv go /usr/local/
echo 'export PATH=$PATH:/usr/local/go/bin' >> /etc/profile
source /etc/profile

# 查看版本   正常显示则表明安装正常
go --version
```

