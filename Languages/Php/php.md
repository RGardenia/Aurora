# PHP 简介



# 安装

## Apache 安装

官网地址：

下载地址：https://www.apachehaus.com/cgi-bin/download.plx 

> https://www.apachelounge.com/download/

1. 修改 `httpd.conf`  文件

> 在文件夹中查找`conf文件夹`下边的`httpd.conf`文件，修改文件为自己的路径(写你自己的路径)
>
> 将其中所有前面没有被"#“注释的"c:\Apache24”、"c:\Apache24\htdocs"和"c:\Apache24\cgi-bin"改成对应的路径

![image-20220425204820371](../doc/images/image-20220425204820371.png)

> 注：还是在此文件中下边，有一个端口号配置：`Listen 80` ，如果你的`80`端口被已经被占用，你自己修改一个没有用的端口号！！！

> 指定IP和端口：在httpd.conf找到`ServerName www.example.com:80` ，将前面的注释去掉，将`www.example.com` 修改为"localhost"。
>
> 如果你要修改端口，将这里"80”和前面“Listen：80”中的80一起修改。
>
> （可选）添加系统变量：将"Apache的安装路径\bin"添加到Path中

2. **用【管理员】身份打开 CMD，输入`httpd.exe -k install`**

> 证安装是否成功：运行Apache的启动httpd.exe。
>
> 在浏览器上输入localhost:80，如果不是无法访问那么Apache的配置便完成了。



### Apache 安装问题

报错：

![image-20220425205003587](../doc/images/image-20220425205003587.png)

> 此服务需要手动开启！



## PHP 安装

官方下载：https://windows.php.net/download

> 注意：下载的PHP VC版本不能比前面安装的vc redist版高。

1. 配置加载文件

> 进入PHP安装目录，复制一份php.ini-development改名为php.ini放到安装路径下，打开找到"extension_dir"，去掉注释符，将值改为"PHP安装路径\ext"

2. 在Apache中加载PHP

打开Apache的配置文件`conf\httpd.conf`，找到`LoadModule`区域，在其后加入：

> // 在Apache中以 module 的方式加载 PHP
>
> LoadModule php7_module "PHP安装路径\php7apache2_4.dll" 
>
> “php7_module”中的“7”要和PHP的版本对应；
>
> // 此外，不同的PHP版本“php7apache2_4.dll”可能不同。
>
> // 告诉Apache PHP的安装路径12
>
> PHPIniDir "PHP安装路径" 

3. 定义执行PHP模块的文件

查找 AddType application/x-gzip .gz .tgz，在其下一行添加代码：

`AddType application/x-httpd-php .php .html`：声明.php和.html的文件能执行PHP程序

> 测试：在 Apache安装路径\htdocs下新建文件：test.php，里面编辑：<?php phpinfo(); ?>， 启动Apache， 在浏览器输入：localhost:8889/test.php



## 集成 Mysql

在PHP中加载连接MySQL的程序集：在`php.ini extension`板块中增加一行`extension=php_mysqli.dll`

> 说明：不同的PHP版本可能提供不同的连接mysq的程序集，去ext文件夹下看看PHP提供的是什么这里就写什么。不同的程序集可能在连接数据库的时候使用的函数也不一样。

测试：在test.php中编辑：

```php
<?php $mysqli = mysqli_connect("localhost","root","pwd") or die("cannt connet"); ?>
```

启动数据库，重启Apache，在浏览器端查看，如果没有错误信息便配置正确了。