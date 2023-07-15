# NVM



> nvm-windows的github地址: https://github.com/coreybutler/nvm-windows 
>
> 安装包下载地址： https://github.com/coreybutler/nvm-windows/releases



##  安装及使用



### Install

![image-20230509120957929](.\images\image-20230509120957929.png)

安装环境：`Win10系统，64位`

​		下载 `nvm-windows` 解压安装，安装前首先要卸载已安装的任何版本的 `NodeJS`，安装过程需要设置 NVM 的安装路径和 `NodeJS` 的快捷方式路径，可以选择任意路径(指定安装目录和当前所使用的`nodejs`的目录,这两个路径中不要带有特殊的字符以及空格，否则会在`nvm use xxx`的时候出错，无法正确解析指定的`nodejs`的版本的地址)。

> 在安装的时候，自动会把`nvm`和`nodejs`的目录添加到系统环境变量中(环境变量 `NVM_HOME` 和 `NVM_SYMLINK`)，所以安装后可以直接测试安装是否成功。



### Nvm Command

```shell
nvm install v<nodejs_v>

nvm use <nodejs_v>
```

安装成功后在 `NVM` 安装目录下出现一个 `v10.16.2` 文件夹，使用 `nvm list` 命令查看已安装 `NodeJS` 列表

`nvm list` 

> 带 * 版本代表当前使用的 NodeJS 版本。