



```shell
[root@zcreunion local]# yum -y install mysql-community-server
MySQL 8.0 Community Server                                                                                       920 kB/s | 1.9 MB     00:02    
MySQL Connectors Community                                                                                        64 kB/s |  68 kB     00:01    
MySQL Tools Community                                                                                            202 kB/s | 322 kB     00:01    
All matches were filtered out by modular filtering for argument: mysql-community-server
Error: Unable to find a match: mysql-community-server


yum module disable mysql


yum -y install mysql-community-server
```





```shell
mysql 8.0 以后的语法
create user root@'%' identified by '123456';

grant all privileges on *.* to root@'%' with grant option;
```

