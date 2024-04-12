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





