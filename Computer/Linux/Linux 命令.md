## 常用的基本命令

**Linux的命令，很多很多，至少三四千条，也是我们学习Linux的基本功，学这部分东西，没的啥子捷径方法，两个注意：**

**a、多用，熟能生巧，用着用着就记住了，至少常用命令要用熟**

**b、记好笔记，忘记了回来查，笔记上没有的百度，多实验，多记录，积累经验**

**cd命令，和dos下一样的，注意用cd .. 的时候，中间必须有空格**

**绝大多数**linux命令都是这样一个格式：命令  [-选项]  [参数]

有多个选项的时候，我们可以将选项连写，也可以分开写，用空格隔离，

比如：`ls -al`  或  `ls -a -l`

选项有简写，和完整写之分，一般简写前只有一个“-”，完整写有两个“--”

比如：ls --all

### 目录管理

我们知道Linux的目录结构为树状结构，最顶级的目录为根目录 /。

其他目录通过挂载可以将它们添加到树中，通过解除挂载可以移除它们。

在开始本教程前我们需要先知道什么是绝对路径与相对路径。

> 绝对路径：

路径的写法，由根目录 `/` 写起，例如：`/usr/share/doc` 这个目录。

> 相对路径：

路径的写法，不是由 `/` 写起，例如由 `/usr/share/doc` 要到 `/usr/share/man` 底下时，可以写成：`cd ../man` 这就是相对路径的写法啦！

> 处理目录常用命令

- ls: 列出目录
- cd：切换目录
- pwd：显示目前的目录
- mkdir：创建一个新的目录
- rmdir：删除一个空的目录
- cp: 复制文件或目录
- rm: 移除文件或目录
- mv: 移动文件与目录，或修改文件与目录的名称

你可以使用 man [命令] 来查看各个命令的使用文档，如 ：man cp。

#### ls命令（列出目录）

在Linux中Is可能是最常常被使用的！	

选项与参数：

```shell
 [root@nancy ~]# ls -a:   #参数: all ,查看全部的文件,包括隐藏文件
 [root@nancy ~]# ls -l:   #参数列出所有的文件,包含文件的属性和权限,没有隐藏文件
 [root@nancy ~]# ls -al   #查看全部的文件包括隐藏文件的属性和权限
 所有Linux命令可以组合使用!
```

补充：

```shell
 [root@nancy ~]# ls -A #列出除.及..的其它文件
 [root@nancy ~]# ls -r #反序排列
 [root@nancy ~]# ls -t #以文件修改时间排序
 [root@nancy ~]# ls -S #以文件大小排序
 [root@nancy ~]# ls -h #以易读大小显示
```

#### cd命令（切换目录）

语法：

```shell
cd [相对路径或绝对路径]
 cd:     #切换目录命令!
 ./:     #当前目录
 /:      #绝对路径
 cd..:   #返回上一级目录
 cd ~:   #回到当前的用户目录
 pwd :   #显示当前用户所在的目录
```

例如：

```shell
 # 切换到用户目录下
 [root@nancy /]# cd home  
 
 # 使用 mkdir 命令创建 nancy 目录
 [root@nancy home]# mkdir nancy
 
 # 进入 kuangstudy 目录
 [root@nancy home]# cd nancy
 
 # 回到上一级
 [root@nancy nancy]# cd ..
 
 # 回到根目录
 [root@nancy nancy]# cd /
 
 # 表示回到自己的家目录，亦即是 /root 这个目录
 [root@nancy nancy]# cd ~
```

#### pwd命令（显示当前目录）

语法：

```shell
 [root@nancy home]# pwd   # 单纯显示出目前的工作目录
```

#### mkdir（创建一个目录）

语法：

```shell
 mkdir [-mp] 目录名称
```

选项与参数：

-m ：配置文件的权限喔！直接配置，不需要看默认权限 (umask) 的脸色～

-p ：帮助你直接将所需要的目录(包含上一级目录)递归创建起来！

例如：

```shell
 # 进入我们用户目录下
 [root@nancy /]# cd /home
 
 # 创建一个 test 文件夹
 [root@nancy home]# mkdir test
 
 # 创建多层级目录
 [root@nancy home]# mkdir test1/test2/test3/test4
 mkdir: cannot create directory ‘test1/test2/test3/test4’:
 No such file or directory  # <== 没办法直接创建此目录啊！
 
 # 加了这个 -p 的选项，可以自行帮你创建多层目录！
 [root@nancy home]# mkdir -p test1/test2/test3/test4
 
 # 创建权限为 rwx--x--x 的目录。
 [root@nancy home]# mkdir -m 711 test2
 [root@nancy home]# ls -l
 drwxr-xr-x 2 root root  4096 Mar 12 21:55 test
 drwxr-xr-x 3 root root  4096 Mar 12 21:56 test1
 drwx--x--x 2 root root  4096 Mar 12 21:58 test2
```

#### rmdir ( 删除空的目录 )

语法：

```shell
 rmdir [-p] 目录名称
```

选项与参数：-p ：连同上一级『空的』目录也一起删除

例如：

```shell
 # 看看有多少目录存在？
 [root@nancy home]# ls -l
 drwxr-xr-x 2 root root  4096 Mar 12 21:55 test
 drwxr-xr-x 3 root root  4096 Mar 12 21:56 test1
 drwx--x--x 2 root root  4096 Mar 12 21:58 test2
 
 # 可直接删除掉，没问题
 [root@nancy home]# rmdir test
 
 # 因为尚有内容，所以无法删除！
 [root@nancy home]# rmdir test1
 rmdir: failed to remove ‘test1’: Directory not empty
 
 # 利用 -p 这个选项，立刻就可以将 test1/test2/test3/test4 依次删除。
 [root@nancy home]# rmdir -p test1/test2/test3/test4
```

注意：这个 rmdir 仅能删除空的目录，你可以使用 rm 命令来删除非空目录，后面我们会将学习！

#### cp（复制文件or目录）

语法：

```shell
 [root@nancy /]# cp [-adfilprsu] 来源档(source) 目标档(destination)
 [root@nancy /]# cp [options] source1 source2 source3 .... directory
```

选项与参数：

-a：相当于 -pdr 的意思，至于 pdr 请参考下列说明；(常用)

-p：连同文件的属性一起复制过去，而非使用默认属性(备份常用)；

-d：若来源档为连结档的属性(link file)，则复制连结档属性而非文件本身；

-r：递归持续复制，用于目录的复制行为；(常用)

-f：为强制(force)的意思，若目标文件已经存在且无法开启，则移除后再尝试一次；

-i：若目标档(destination)已经存在时，在覆盖时会先询问动作的进行(常用)

-l：进行硬式连结(hard link)的连结档创建，而非复制文件本身。

-s：复制成为符号连结档 (symbolic link)，亦即『捷径』文件；

-u：若 destination 比 source 旧才升级 destination ！

例如：

```shell
 # 找一个有文件的目录，我这里找到 root目录
 [root@nancy home]# cd /root
 [root@nancy ~]# ls
 install.sh
 [root@nancy ~]# cd /home
 
 # 复制 root目录下的install.sh 到 home目录下
 [root@nancy home]# cp /root/install.sh /home
 [root@nancy home]# ls
 install.sh
 
 # 再次复制，加上-i参数，增加覆盖询问？
 [root@nancy home]# cp -i /root/install.sh /home
 cp: overwrite ‘/home/install.sh’? y # n不覆盖，y为覆盖
```

#### rm ( 移除文件或目录 )

语法：

```shell
 rm [-fir] 文件或目录
```

选项与参数：

-f ：就是 force 的意思，忽略不存在的文件，不会出现警告信息；

-i ：互动模式，在删除前会询问使用者是否动作

-r ：递归删除啊！最常用在目录的删除了！这是非常危险的选项！！！

例如：

```shell
 rm -rf /  #系统中所有的文件被删除了，删库跑路就是这么操作的！
 
 # 将刚刚在 cp 的实例中创建的 install.sh删除掉！
 [root@nancy home]# rm -i install.sh
 rm: remove regular file ‘install.sh’? y
 # 如果加上 -i 的选项就会主动询问喔，避免你删除到错误的档名！
 
 # 尽量不要在服务器上使用 rm -rf /
 mv ( 移动文件与目录，或修改名称 )
```

#### mv （移动/重命名文件或者目录）

语法：

```shell
 [root@nancy ~]# mv [-fiu] source destination
 [root@nancy ~]# mv [options] source1 source2 source3 .... directory
```

选项与参数：

-f ：force 强制的意思，如果目标文件已经存在，不会询问而直接覆盖；

-i ：若目标文件 (destination) 已经存在时，就会询问是否覆盖！

-u ：若目标文件已经存在，且 source 比较新，才会升级 (update)

例如：

```shell
 # 复制一个文件到当前目录
 [root@nancy home]# cp /root/install.sh /home
 
 # 创建一个文件夹 test
 [root@nancy home]# mkdir test
 
 # 将复制过来的文件移动到我们创建的目录，并查看
 [root@nancy home]# mv install.sh test
 [root@nancy home]# ls
 test
 [root@nancy home]# cd test
 [root@nancy test]# ls
 install.sh
 
 # 将文件夹重命名，然后再次查看！
 [root@nancy test]# cd ..
 [root@nancy home]# mv test mvtest
 [root@nancy home]# ls
 mvtest
```

#### 其他命令

**touch命令用于修改文件或者目录的时间属性。若文件不存在，系统会建立一个新的文件。**

**命令格式：**touch 文件

**cat 命令用显示文件内容。**

**命令格式：**cat [-选项] 文件

**加参数：-n 或 --number**：由 1 开始对所有输出的行数编号。

**more 命令类似 cat ，不过会以一页一页的形式显示，更方便使用者逐页阅读，而最基本的指令就是按空白键（space）就往下一页显示，按 b 键就会往回（back）一页显示**

- Enter 向下n行，需要定义。默认为1行
- 空格键 向下滚动一屏
- q 退出more

**less 工具也是对文件或其它输出进行分页显示的工具，应该说是linux正统查看文件内容的工具，功能极其强大。**

**1．命令格式：**

less [-选项] 文件

**2．命令功能：**

less 与 more 类似，但使用 less 可以随意浏览文件，而 more 仅能向前移动，却不能向后移动，而且 less 在查看之前不会加载整个文件。

/字符串：向下搜索“字符串”的功能

?字符串：向上搜索“字符串”的功能

n：重复前一个搜索（与 / 或 ? 有关），next

N：反向重复前一个搜索（与 / 或 ? 有关）

Q 退出less 命令

Enter 向下n行，需要定义。默认为1行

空格键 向下滚动一屏

b键 向上滚动一屏

[pagedown]：向下翻动一行

[pageup]： 向上翻动一行

**head (head) 用来显示档案的开头至标准输出中。如果指定了多于一个文件，在每一段输出前会给出文件名作为文件头。**

1、head -n 5 log.txt

说明：显示文件的前n行

**tail命令用途是依照要求将指定的文件的最后部分输出**

1、tail filename

说明：显示filename最后10行。

2、tail -F filename

说明：动态监视filename文件的尾部内容（默认10行，相当于增加参数 -n 10），刷新显示在屏幕上。退出，按下CTRL+C。

3、tail -n 20 filename

说明：显示filename最后20行。

### 基本属性

#### 文件属性

Linux系统是一种典型的多用户系统，不同的用户处于不同的地位，拥有不同的权限。为了保护系统的安全性，Linux系统对不同的用户访问同一文件（包括目录文件）的权限做了不同的规定。

在Linux中我们可以使用`ll`或者`ls –l`命令来显示一个文件的属性以及文件所属的用户和组，如：

![图片](Linux 命令.assets/640)

实例中，boot文件的第一个属性用"d"表示。"d"在Linux中代表该文件是一个目录文件。

在Linux中第一个字符代表这个文件是目录、文件或链接文件等等：

- 当为[ d ]则是目录
- 当为[ - ]则是文件；
- 若是[ l ]则表示为链接文档 ( link file )；
- 若是[ b ]则表示为装置文件里面的可供储存的接口设备 ( 可随机存取装置 )；
- 若是[ c ]则表示为装置文件里面的串行端口设备，例如键盘、鼠标 ( 一次性读取装置 )。

接下来的字符中，以三个为一组，且均为『rwx』 的三个参数的组合。

**其中，[ r ]代表可读(read)、[ w ]代表可写(write)、[ x ]代表可执行(execute)。**

要注意的是，这三个权限的位置不会改变，如果没有权限，就会出现减号[ - ]而已。

每个文件的属性由左边第一部分的10个字符来确定（如下图）：

![图片](Linux 命令.assets/640)

从左至右用0-9这些数字来表示。

第0位确定文件类型，第1-3位确定属主（该文件的所有者）拥有该文件的权限。第4-6位确定属组（所有者的同组用户）拥有该文件的权限，第7-9位确定其他用户拥有该文件的权限。

其中：

第1、4、7位表示**读权限**，如果用"r"字符表示，则有读权限，如果用"-"字符表示，则没有读权限；

第2、5、8位表示**写权限**，如果用"w"字符表示，则有写权限，如果用"-"字符表示没有写权限；

第3、6、9位表示**可执行权限**，如果用"x"字符表示，则有执行权限，如果用"-"字符表示，则没有执行权限。

对于文件来说，它都有一个特定的所有者，也就是对该文件具有所有权的用户。

同时，在Linux系统中，用户是按组分类的，一个用户属于一个或多个组。

文件所有者以外的用户又可以分为文件所有者的同组用户和其他用户。

因此，Linux系统按文件所有者、文件所有者同组用户和其他用户来规定了不同的文件访问权限。

在以上实例中，boot 文件是一个目录文件，属主和属组都为 root。

#### 修改文件属性

##### 1、chgrp：更改文件属组

```shell
 chgrp [-R] 属组名 文件名
```

-R：递归更改文件属组，就是在更改某个目录文件的属组时，如果加上-R的参数，那么该目录下的所有文件的属组都会更改。

##### 2、chown：更改文件属主，也可以同时更改文件属组

```shell
 chown [–R] 属主名 文件名
 chown [-R] 属主名：属组名 文件名
```

##### 3、chmod：更改文件9个属性(必须掌握)

```shell
 chmod [-R] xyz 文件或目录
```

Linux文件属性有两种设置方法，一种是数字，一种是符号。

Linux文件的基本权限就有九个，分别是owner/group/others三种身份各有自己的read/write/execute权限。

先复习一下刚刚上面提到的数据：文件的权限字符为：『-rwxrwxrwx』， 这九个权限是三个三个一组的！其中，我们可以使用数字来代表各个权限，各权限的分数对照表如下：

```
r:4 w:2 x:1
[root@localhost home]# chmod 777 mhw    文件最高权限 赋予所有用户可读可执行!
```

每种身份(owner/group/others)各自的三个权限(r/w/x)分数是需要累加的，

例如当权限为：[-rwxrwx---] 分数则是：

- owner = rwx = 4+2+1 = 7
- group = rwx = 4+2+1 = 7
- others = rwx  = 4+2+1= 7

```shell
 [root@localhost home]# ls -ll
 total 4
 drwx------. 15 mhw mhw  4096 Jul 18 19:55 mhw
 drwxr-xr-x.  2 root root   21 Jul 18 20:42 nancy
 drwxr-xr-x.  2 root root    6 Jul 18 23:49 nancy1
 drwxr-xr-x.  2 root root    6 Jul 18 23:49 nancy2
 drwxr-xr-x.  2 root root    6 Jul 18 19:33 test
 [root@localhost home]# chmod 777 mhw
 [root@localhost home]# ls -ll
 total 4
 drwxrwxrwx. 15 mhw mhw  4096 Jul 18 19:55 mhw
 drwxr-xr-x.  2 root root   21 Jul 18 20:42 nancy
 drwxr-xr-x.  2 root root    6 Jul 18 23:49 nancy1
 drwxr-xr-x.  2 root root    6 Jul 18 23:49 nancy2
 drwxr-xr-x.  2 root root    6 Jul 18 19:33 test
 [root@localhost home]# chmod 700 mhw
 total 4
 drwx------. 15 mhw mhw  4096 Jul 18 19:55 mhw
 drwxr-xr-x.  2 root root   21 Jul 18 20:42 nancy
 drwxr-xr-x.  2 root root    6 Jul 18 23:49 nancy1
 drwxr-xr-x.  2 root root    6 Jul 18 23:49 nancy2
 drwxr-xr-x.  2 root root    6 Jul 18 19:33 test
```

### 文件内容查看

#### 概述

Linux系统中使用以下命令来查看文件的内容：

- cat 由第一行开始显示文件内容
- tac 从最后一行开始显示，可以看出 tac 是 cat 的倒着写！
- nl 显示的时候，顺道输出行号！
- more 一页一页的显示文件内容
- less 与 more 类似，但是比 more 更好的是，他可以往前翻页！
- head 只看头几行
- tail 只看尾巴几行

你可以使用 man [命令]来查看各个命令的使用文档，如 ：man cp。

#### 网络配置目录

网络配置目录：

```shell
 /etc/sysconfig/network-scripts/
 [root@localhost network-scripts]# ls
 ifcfg-ens33 ifdown-ppp       ifup-ib     ifup-Team
 ifcfg-lo     ifdown-routes   ifup-ippp   ifup-TeamPort
 ifdown       ifdown-sit       ifup-ipv6   ifup-tunnel
 ifdown-bnep ifdown-Team     ifup-isdn   ifup-wireless
 ifdown-eth   ifdown-TeamPort ifup-plip   init.ipv6-global
 ifdown-ib   ifdown-tunnel   ifup-plusb   network-functions
 ifdown-ippp ifup             ifup-post   network-functions-ipv6
 ifdown-ipv6 ifup-aliases     ifup-ppp
 ifdown-isdn ifup-bnep       ifup-routes
 ifdown-post ifup-eth         ifup-sit
```

查看网络配置: `ifconfig命令`

#### cat由第一行开始显示文件内容

语法：

```shell
 [root@nancy home] cat [-AbEnTv]
```

选项与参数：

-A ：相当于 -vET 的整合选项，可列出一些特殊字符而不是空白而已；

-b ：列出行号，仅针对非空白行做行号显示，空白行不标行号！

-E ：将结尾的断行字节 $ 显示出来；

-n ：列印出行号，连同空白行也会有行号，与 -b 的选项不同；

-T ：将 [tab] 按键以 ^I 显示出来；

-v ：列出一些看不出来的特殊字符

例如：

```shell
 root@localhost network-scripts]# cat ifcfg-lo
 DEVICE=lo
 IPADDR=127.0.0.1
 NETMASK=255.0.0.0
 NETWORK=127.0.0.0
 # If you're having problems with gated making 127.0.0.0/8 a martian,
 # you can change this to something else (255.255.255.255, for example)
 BROADCAST=127.255.255.255
 ONBOOT=yes
 NAME=loopback
```

#### tac由最后一行开始显示

语法：

```shell
 [root@nancy home] tac [-AbEnTv]
```

例如：

```shell
 [root@localhost network-scripts]# tac ifcfg-lo
 NAME=loopback
 ONBOOT=yes
 BROADCAST=127.255.255.255
 # you can change this to something else (255.255.255.255, for example)
 # If you're having problems with gated making 127.0.0.0/8 a martian,
 NETWORK=127.0.0.0
 NETMASK=255.0.0.0
 IPADDR=127.0.0.1
 DEVICE=lo
```

#### nl显示行号（常用）

语法：

```shell
 nl [-bnw] 文件
```

选项与参数：

-b ：指定行号指定的方式，主要有两种：-b a ：表示不论是否为空行，也同样列出行号(类似 cat -n)；-b t ：如果有空行，空的那一行不要列出行号(默认值)；

-n ：列出行号表示的方法，主要有三种：-n ln ：行号在荧幕的最左方显示；-n rn ：行号在自己栏位的最右方显示，且不加 0 ；-n rz ：行号在自己栏位的最右方显示，且加 0 ；

-w ：行号栏位的占用的位数。

例如：

```shell
 [root@localhost network-scripts]# nl ifcfg-lo
      1DEVICE=lo
      2IPADDR=127.0.0.1
      3NETMASK=255.0.0.0
      4NETWORK=127.0.0.0
      5# If you're having problems with gated making 127.0.0.0/8 a martian,
      6# you can change this to something else (255.255.255.255, for example)
      7BROADCAST=127.255.255.255
      8ONBOOT=yes
      9NAME=loopback
```

#### more一页一页翻动

在 more 这个程序的运行过程中，你有几个按键可以按的：

空白键 (space)：代表向下翻一页；

Enter ：代表向下翻『一行』；

/字串 ：代表在这个显示的内容当中，向下搜寻『字串』这个关键字；

:f ：立刻显示出档名以及目前显示的行数；

q ：代表立刻离开 more ，不再显示该文件内容。

b 或 [ctrl]-b ：代表往回翻页，不过这动作只对文件有用，对管线无用。

```shell
 [root@nancy etc]# more /etc/csh.login
 ....(中间省略)....
 --More--(28%) # 重点在这一行喔！你的光标也会在这里等待你的命令
```

#### less支持上下翻动

less运行时可以输入的命令有：

空白键 ：向下翻动一页；

[pagedown]：向下翻动一页；

[pageup] ：向上翻动一页；

/字串 ：向下搜寻『字串』的功能；

?字串 ：向上搜寻『字串』的功能；

n ：重复前一个搜寻 (与 / 或 ? 有关！)

N ：反向的重复前一个搜寻 (与 / 或 ? 有关！)

q ：离开 less 这个程序；

```shell
 [root@kuangshen etc]# more /etc/csh.login
 ....(中间省略)....  
 :   # 这里可以等待你输入命令！
```

- less与more类似,但是比more更好的是,他可以往前翻页!（空格翻页，上下键代表上下翻动页面，退出q命令，查找字符串/要查询的字符向下查询，向上查询使用?要查询的字符串,用n继续搜寻下一个,用N向上寻找）

#### head看头几行

语法：

```shell
 head [-n number] 文件
```

选项与参数：-n 后面接数字，代表显示几行的意思！

默认的情况中，显示前面 10 行！若要显示前 20 行，就得要这样：

```shell
 [root@nancy etc]# head -n 20 /etc/csh.login
```

#### tail取出文件后面几行

语法：

```shell
 tail [-n number] 文件
```

选项与参数：

-n ：后面接数字，代表显示几行的意思

默认的情况中，显示最后 10 行！若要显示最后 20 行，就得要这样：

```shell
 [root@kuangshen etc]# tail -n 20 /etc/csh.login
```

#### find 搜索命令

**因为Linux下面一切皆文件，经常需要搜索某些文件来配置，所以对于linux来说find是一条很重要的命令。linux下面的find指令用于在目录结构中搜索文件，并执行指定的操作。它提供了相当多的查找条件，功能很强大。在不指定查找目录的情况下，find会在对整个系统进行遍历。**

**所以：find命令是一个非常耗时，耗资源的命令，一定记住：**

**1、不能在系统繁忙时段运行；**

**2、尽可能在最小的搜索范围的前提下使用**

语法：

```shell
 find [查找目录] [查找规则] [查找完后的操作]
```

**通过文件名字查找，如名字为test的文件或目录，这个是精准查找**

```shell
 find ./  -name test
```

**加通配符，查找名字包含test的文件或目录，这个是模糊查找**

```shell
 find ./ -name *test*      # 表示任意字符， ？表示一个字符
 find ./ -i name *test*    # 不区分大小写的查找
```

**查询文件大小大于100M的文件**

```shell
 #注意这里的单位是数据块，它和K的换算：1数据块=215字节=0.5K 所以100M=102400K=20800，
 #+表示大于，-表示小于，不写表示等于
 find ./ -size  +204800
```

**查询所有者为xxx的所拥有文件**

```shell
 find / -user xxxx
```

**查询用户组为xxx的所拥有文件**

```shell
 find / -group xxxx
```

**查询在etc目录下5分钟内被修改过文件属性的文件和目录**

```shell
 find /etc -cmin -5    #amin被访问， cmin属性被修改，mmin内容被修改
```

**多条件查询，在/etc目录下查找文件大小大于80M,并且小于100M的文件**

```shell
 find /etc -size +163840  -a  -size -204800  # -a:表示and，并且关系， 此外还有-o：表示or，或者关系
```

**默认查找的内容是目录和文件，但是我们只想找到文件或者目录中的一个，如：查找/etc目录下的init开头的文件**

```shell
 find /etc -name init\* -a -type f   #这里f:表示文件，d:表示目录， l：表示软链接
```

**查找文件后，在进一步执行某些命令，如：查找出文件后在显示详细属性信息**

```shell
 #{} \;后面的这三个符号，你把它当作固定结构记住就行，其中“{}”就代表 find 命令的查找结果。
 #注意-exec可以替换-ok，功能一样只是多一个提示
 find /etc -name init\*  -exec ls -l {} \;
```

**通过i节点查询文件,举个例子就好了：**

```shell
 find /etc -inum 12313 -exec rm {} \;
```

### 拓展：Linux 链接概念

Linux 链接分两种，一种被称为硬链接（Hard Link），另一种被称为符号链接（Symbolic Link）。

情况下，ln 命令产生硬链接。

#### 硬链接

**硬链接：A—B,假设B是A的硬链接，那么他们两个指向了同一个文件!允许一个文件拥有多个路径，用户可以通过这种机制硬链接到一个重要文件上，防止误删**

硬连接指通过索引节点来进行连接。在 Linux 的文件系统中，保存在磁盘分区中的文件不管是什么类型都给它分配一个编号，称为索引节点号(Inode Index)。在 Linux 中，多个文件名指向同一索引节点是存在的。比如：A 是 B 的硬链接（A 和 B 都是文件名），则 A 的目录项中的 inode 节点号与 B 的目录项中的 inode 节点号相同，即一个 inode 节点对应两个不同的文件名，两个文件名指向同一个文件，A 和 B 对文件系统来说是完全平等的。删除其中任何一个都不会影响另外一个的访问。

硬连接的作用是允许一个文件拥有多个有效路径名，这样用户就可以建立硬连接到重要文件，以防止“误删”的功能。其原因如上所述，因为对应该目录的索引节点有一个以上的连接。只删除一个连接并不影响索引节点本身和其它的连接，只有当最后一个连接被删除后，文件的数据块及目录的连接才会被释放。也就是说，文件真正删除的条件是与之相关的所有硬连接文件均被删除。

#### 软链接

**软链接：类似Windows下的快捷方式，删除源文件，快捷方式也就访问不**了

另外一种连接称之为符号连接（Symbolic Link），也叫软连接。软链接文件有类似于 Windows 的快捷方式。它实际上是一个特殊的文件。在符号连接中，文件实际上是一个文本文件，其中包含的有另一文件的位置信息。比如：A 是 B 的软链接（A 和 B 都是文件名），A 的目录项中的 inode 节点号与 B 的目录项中的 inode 节点号不相同，A 和 B 指向的是两个不同的 inode，继而指向两块不同的数据块。但是 A 的数据块中存放的只是 B 的路径名（可以根据这个找到 B 的目录项）。A 和 B 之间是“主从”关系，如果 B 被删除了，A 仍然存在（因为两个是不同的文件），但指向的是一个无效的链接。

例如：

操作步骤：创建链接`ln`命令！`touch`命令创建文件！`echo`输入字符串

```shell
 [root@nancy /]# cd /home
 [root@nancy home]# touch f1 # 创建一个测试文件f1
 [root@nancy home]# ls
 f1
 [root@nancy home]# ln f1 f2     # 创建f1的一个硬连接文件f2
 [root@nancy home]# ln -s f1 f3   # 创建f1的一个符号连接文件f3
 [root@nancy home]# ls -li       # -i参数显示文件的inode节点信息
 2580245 -rw-r--r-- 2 root root     0 Mar 13 00:50 f1
 2580245 -rw-r--r-- 2 root root     0 Mar 13 00:50 f2
 2580247 lrwxrwxrwx 1 root root     2 Mar 13 00:50 f3 -> f1
```

从上面的结果中可以看出，硬连接文件 f2 与原文件 f1 的 inode 节点相同，均为 397247，然而符号连接文件的 inode 节点不同。

通过上面的测试可以看出：当删除原始文件 f1 后，硬连接 f2 不受影响，但是符号连接 f1 文件无效；

依此您可以做一些相关的测试，可以得到以下全部结论：

删除符号连接f3,对f1,f2无影响；

删除硬连接f2，对f1,f3也无影响；

删除原文件f1，对硬连接f2没有影响，导致符号连接f3失效；

同时删除原文件f1,硬连接f2，整个文件会真正的被删除。