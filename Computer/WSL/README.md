# WSL























## & Windows 文件互传



开启Ubuntu（Linux环境）下的FTP服务
打开WSL的终端窗口，然后执行如下命令来安装 FTP 服务：

sudo apt-get install vsftpd

等待软件自动安装，安装完成以后使用 VI 命令打开/etc/vsftpd.conf，命令如下：

sudo vi /etc/vsftpd.conf

打开 vsftpd.conf 文件以后找到如下两行：

local_enable=YES
write_enable=YES



改完 vsftpd.conf 以后保存退出，使用如下命令重启 FTP 服务：

```c
sudo /etc/init.d/vsftpd restart
```

IP 从 ifconfig 中找

```bash
sudo apt install net-tools
ifconfig

eth0
```





**root  ssh 远程登录**

```bash
sudo apt-get install -y openssh-server

sudo passwd root

sudo sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config
# sudo vim /etc/ssh/sshd_config	PermitRootLogin 改为 yes	最大连接数的三行注释放开

sudo service ssh status
sudo service ssh start
sudo service ssh restart

# 设置 SSH 服务开机自启动
sudo systemctl enable ssh
# systemctl restart sshd
```

## Net

静态IP必须和当前动态IP的网段一致，否则可能导致网络无法连接。如果静态IP已经被分配给网络中的其他主机，会造成IP冲突，网络无法连接，直接将当前动态IP配置为静态IP可以避免这一情况。

```bash
# 列出网络配置文件，其在不同Ubuntu版本中可能会有差异，但一定是.yaml文件
ls /etc/netplan/

# 备份现有的配置文件，这样如果配置错误可以恢复
sudo cp  /etc/netplan/01-network-manager-all.yaml \
	/etc/netplan/01-network-manager-all.yaml.bak

vim /etc/netplan/00-installer-config.yaml
# 如果使用xshell方式，请在vi编辑器命令模式下输入(避免复制格式混乱)
:set paste

# 配置文件修改如下：
network:
  ethernets:
    ens33:
      dhcp4: no
      addresses: [192.168.3.10/24]
      optional: true
      gateway4: 192.168.3.1
      nameservers:
        addresses: [8.8.8.8]
  version: 2
# 其中 addresses 是静态 IP 地址，gateway4 是网关地址，nameservers 是 DNS 服务器地址

sudo netplan apply
```



## 源

https://packages.ubuntu.com/



```bash
sudo add-apt-repository universe multiverse

sudo apt update

hwe-support-status --verbose

sudo nano /etc/apt/sources.list
# 添加如下内容
deb http://mirrors.aliyun.com/ubuntu/ jammy main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ jammy main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ jammy-security main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ jammy-security main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ jammy-updates main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ jammy-updates main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ jammy-proposed main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ jammy-proposed main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ jammy-backports main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ jammy-backports main restricted universe multiverse
deb https://mirrors.ustc.edu.cn/ubuntu/ jammy main restricted universe multiverse
deb-src https://mirrors.ustc.edu.cn/ubuntu/ jammy main restricted universe multiverse
deb https://mirrors.ustc.edu.cn/ubuntu/ jammy-updates main restricted universe multiverse
deb-src https://mirrors.ustc.edu.cn/ubuntu/ jammy-updates main restricted universe multiverse
deb https://mirrors.ustc.edu.cn/ubuntu/ jammy-backports main restricted universe multiverse
deb-src https://mirrors.ustc.edu.cn/ubuntu/ jammy-backports main restricted universe multiverse
deb https://mirrors.ustc.edu.cn/ubuntu/ jammy-security main restricted universe multiverse
deb-src https://mirrors.ustc.edu.cn/ubuntu/ jammy-security main restricted universe multiverse
deb https://mirrors.ustc.edu.cn/ubuntu/ jammy-proposed main restricted universe multiverse
deb-src https://mirrors.ustc.edu.cn/ubuntu/ jammy-proposed main restricted universe multiverse
deb http://mirrors.163.com/ubuntu/ jammy main restricted universe multiverse
deb http://mirrors.163.com/ubuntu/ jammy-security main restricted universe multiverse
deb http://mirrors.163.com/ubuntu/ jammy-updates main restricted universe multiverse
deb http://mirrors.163.com/ubuntu/ jammy-proposed main restricted universe multiverse
deb http://mirrors.163.com/ubuntu/ jammy-backports main restricted universe multiverse
deb-src http://mirrors.163.com/ubuntu/ jammy main restricted universe multiverse
deb-src http://mirrors.163.com/ubuntu/ jammy-security main restricted universe multiverse
deb-src http://mirrors.163.com/ubuntu/ jammy-updates main restricted universe multiverse
deb-src http://mirrors.163.com/ubuntu/ jammy-proposed main restricted universe multiverse
deb-src http://mirrors.163.com/ubuntu/ jammy-backports main restricted universe multiverse

sudo apt update
```





## 固定 wsl-ubuntu 的 IP 地址

```bash
vim /etc/init.d/init.sh

sudo ip addr del $(ip addr show eth0|grep 'inet\b'|awk '{print $2}' |head -n 1) dev eth0
sudo ip addr add 172.24.219.69/20 broadcast 172.24.219.255 dev eth0
sudo ip route add 0.0.0.0/0 via 172.24.219.1 dev eth0

# sudo echo 'nameserver 8.8.8.8
# nameserver 114.114.114.114' > /etc/resolv.conf

echo 'nameserver 8.8.8.8
nameserver 114.114.114.114' | sudo tee /etc/resolv.conf > /dev/null

/etc/wsl.conf
[network]
generateResolvConf = false

powershell -c "
wsl -d Ubuntu-22.04 -u root /etc/init.d/init.sh start;Get-NetAdapter 'vEthernet (WSL)' | Get-NetIPAddress | Remove-NetIPAddress -Confirm:$False; New-NetIPAddress -IPAddress 172.24.219.1 -PrefixLength 24 -InterfaceAlias 'vEthernet (WSL)'; Get-NetNat | ? Name -Eq WSLNat | Remove-NetNat -Confirm:$False; New-NetNat -Name WSLNat -InternalIPInterfaceAddressPrefix 172.24.219.0/20;
"

wsl -d Ubuntu-22.04 -u root /etc/init.d/init.sh start;Get-NetAdapter 'vEthernet (WSL)' | Get-NetIPAddress | Remove-NetIPAddress -Confirm:$False; New-NetIPAddress -IPAddress 172.24.219.1 -PrefixLength 24 -InterfaceAlias 'vEthernet (WSL)'; Get-NetNat | ? Name -Eq WSLNat | Remove-NetNat -Confirm:$False; New-NetNat -Name WSLNat -InternalIPInterfaceAddressPrefix 172.24.219.0/20;
```

https://blog.csdn.net/cotex_A9/article/details/132695156





## VMware 虚拟机扩展 磁盘空间

```bash
df -h

fdisk -l
```





## FAQ



```bash
sudo apt-get clean && sudo apt-get update && sudo apt-get upgrade 

dpkg -i --force-overwrite /var/cache/apt/archives/linux-tools-common_5.4.0-176.196_all.deb
```











