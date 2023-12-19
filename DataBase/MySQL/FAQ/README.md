1. 启动mysql容器，在 `var/lib/docker/containers/` 下查看容器

命令： 

`docker run --name mysql_cjw `     // 容器名设置

`-v $PWD/conf:/etc/mysql/conf.d`    // 容器卷 共享

```
-v $PWD/logs:/logs/mysql
-v $PWD/data:/var/lib/mysql
```

`-e MYSQL_ROOT_PASSWORD=123456`   // 设置密码

`-d -i`   // 后台运行

`-p 3306:3306 mysql:latest`  // 端口设置 与 版本设置



```shell
docker run --name mysql \
  -v $PWD/conf:/etc/mysql/conf.d \
  -v $PWD/logs:/logs/mysql \
  -v $PWD/data:/var/lib/mysql \
  -e MYSQL_ROOT_PASSWORD=151613  \
  -d -i -p 3306:3306 mysql:8.0.28
```





1. 进入mysql容器，并登陆mysql

命令：`docker exec -it mysql bash`

命令：`mysql -uroot -p`



1. 开启远程访问权限

命令：`use mysql;`

命令：`select host,user from user;`

命令：`ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '151613';`

命令：`flush privileges;`



1. 查看docker日志

命令：`docker logs -f --tail 10 <containId>`