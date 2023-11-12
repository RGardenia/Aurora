# Zabbix

https://www.zabbix.com/cn





# Deployment

Address：https://www.zabbix.com/cn/download

```bash
wget https://mirrors.tuna.tsinghua.edu.cn/zabbix/zabbix/4.0/rhel/7/x86_64/zabbix-release-4.0-2.el7.noarch.rpm

rpm -ivh zabbix-release-4.0-2.el7.noarch.rpm

vim /etc/yum.repos.d/zabbix.repo
# 我们需要把baseurl=这一行的前面改成清华的！经过对比可以发现清华的只是前面改变了
https://mirrors.tuna.tsinghua.edu.cn/zabbix
所以我们只需要替换这里就行

%s#http://repo.zabbix.com#https://mirrors.tuna.tsinghua.edu.cn/zabbix#g

# 然后再把 gpgcheck 都改成0，最终如下

[zabbix]
name=Zabbix Official Repository - $basearch
baseurl=https://mirrors.tuna.tsinghua.edu.cn/zabbix/zabbix/4.0/rhel/7/x86_64/
enabled=1
gpgcheck=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-ZABBIX-A14FE591

[zabbix-debuginfo]
name=Zabbix Official Repository debuginfo - $basearch
baseurl=https://mirrors.tuna.tsinghua.edu.cn/zabbix/zabbix/4.0/rhel/7/x86_64/debuginfo
enabled=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-ZABBIX-A14FE591
gpgcheck=0

[zabbix-non-supported]
name=Zabbix Official Repository non-supported - $basearch
baseurl=https://mirrors.tuna.tsinghua.edu.cn/zabbix/zabbix/4.0/rhel/7/x86_64/
enabled=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-ZABBIX
gpgcheck=0

```

1. 安装 `zabbix` 服务端和 `zabbix-web UI`

```bash
# 安装服务和 web
yum install zabbix-server-mysql zabbix-web-mysql  -y



```

2. 安装 `mariadb`，创建 `zabbix` 库，授权 `zabbix` 用户

   ```bash
   yum install mariadb-server -y
   
   systemctl start mariadb
   
   systemctl enable mariadb
   
   mysql_secure_installation
   # 输入这个指令之后回车，然后 n，然后一路 y 就行了，删除匿名用户
   
   mysql
   MariaDB [(none)]> create database zabbix character set utf8 collate utf8_bin;
   
   MariaDB [(none)]> grant all privileges on zabbix.* to zabbix@localhost identified by '123456';
   
   # 修改字符集 建表 授权等
   
   退出mariadb 导入zabbix表结构和初始数据
   zcat /usr/share/doc/zabbix-server-mysql*/create.sql.gz | mysql -uzabbix -p123456 zabbix
   
   # 检查 zabbix 库是否导入成功	出现表就是成功了
   mysql -uroot  zabbix -e 'show tables'
   
   
   ```

   3. 配置启动 `zabbix-server` 

      ```bash
      vim /etc/zabbix/zabbix_server.conf 
      
      DBHost=localhost 
      DBName=zabbix
      DBUser=zabbix
      DBPassword=123456
      
      
      systemctl start zabbix-server 
      systemctl enable zabbix-server
      
      setenforce 0
      getenforce
      systemctl stop firewalld.service
      # 检查一下服务
      netstat -lntup
      # Or 在 zabbix 的安装目录下找到etc/zabbix_server.conf文件，将ListenIP=0.0.0.0前的注释去掉
      
      # 修改 Zabbix 前端的 PHP 配置, 并启动httpd
      vim /etc/httpd/conf.d/zabbix.conf
      
      # 找到 php_value date.timezone 这一行，把时区改成
      php_value date.timezone Asia/Shanghai
      
      # 保存之后启动和开机自启
      systemctl start httpd
      systemctl enable httpd
      # 访问 http://ip/zabbix
      
      # 登录的账号密码
      Admin
      zabbix
      # 修改 zabbix 数据库密码的时候，需要修改的配置文件：
      /etc/zabbix/web/zabbix.conf.php
      ```

      