# Linux

### Linux 简介

Linux，全称 GNU/Linux，是一种免费使用和自由传播的类 UNIX 操作系统，其内核由林纳斯·本纳第克特·托瓦兹于1991年10月5日首次发布，它主要受到Minix和Unix思想的启发，是一个基于POSIX的多用户、多任务、支持多线程和多CPU的操作系统。它能运行主要的Unix工具软件、应用程序和网络协议。它支持32位和64位硬件。Linux继承了Unix以网络为核心的设计思想，是一个性能稳定的多用户网络操作系统。Linux有上百种不同的发行版，如基于社区开发的debian、archlinux，和基于商业开发的Red Hat Enterprise Linux、SUSE、Oracle Linux等。



目前市面上较知名的发行版有：Ubuntu、RedHat、CentOS、Debian、Fedora、SuSE、OpenSUSE、Arch Linux、SolusOS 等。

### Linux与Windows对比

- 软件与支持

> windows平台:数量和质量的优势，不过大部分为收费软件；由微软官方提供重要支持和服务linux平台：大都为开源自由软件，用户可以修改定制和再发布，由于基本免费没有资金支持，部分软件质量和体验欠缺；有全球所有的Linux开发者和自由软件社区提供支持

- 安全性

> Windows平台：三天两头打补丁安装系统安全更新，还是会中病毒木马什么的，各位用户自己感受。Linux平台：要说linux没有安全问题，那当然是不可能的，不会中病毒什么的，也不可能，这一点仁者见仁智者见智，相对来说肯定比Windows平台要更加安全，使用linux你也不用装杀毒软件了。

- 使用习惯

> Windows：普通用户基本都是纯图形界面下操作使用，依靠鼠标和键盘完成一切操作，用户上手容易入门简单Linux：兼具图形界面操作（需要使用带有桌面环境的发行版）和完全的命令行操作，可以只用键盘完成一切操作，新手入门较困难，需要一些学习和指导（这正是我们要做的事情），一旦熟练之后，神马windows，神马图形界面都是浮云~

- 可定制性

> Windows这些年之前算是全封闭的,系统可定制性很差Linux：你想怎么玩就怎么玩，windows能做到得它都能，windows做不到的，它也能

- 应用范畴

> 或许你之前不知道Linux,要知道，你之前在windows使用百度，谷歌，上淘宝，聊QQ时，支撑这些软件和服务的，是后台成千上万的Linux服务器主机，它们时时刻刻都在进行着忙碌的数据处理和运算，可以说世界上大部分软件和服务都是运行在Linux之上的，什么云计算，大数据，移动互联网，说得风起云涌，要没用Linux,全都得瘫（除了微软自家哈），相比这应用范畴大家也懂了哈，要没有Linux，Windows可干不了几个事。

### 学习方式

**基于B站up主狂神说LinuxCentOS7版本课程的学习！**

Linux一切皆文件：文件读、写、权限

1. 认识Linux基础
2. 基本的命令（文件操作，目录管理，文件属性，账号管理，vim编辑器、磁盘管理）
3. 软件的安装和部署！（Redis、Tomcat、Java、Docker）

### 环境搭建

Linux 的安装，安装步骤比较繁琐，现在其实服务器挺普遍的，价格便宜，如果直接不想搭建，也可以直接从阿里云买一台学习用用！如果是学生，在阿里云24岁免验证，直接享受学生优惠。

> 通过VM虚拟机安装Linux CentOS
>
> VMware虚拟机（大家可以通过百度搜索一个注册码复制粘贴就可激活）
>
> **下载地址：**
>
> **链接：https://pan.baidu.com/s/149HZiBqgq79gmIdJiungCQ** 
>
> **提取码：8hfn** 

1、可以通过镜像进行安装！安装操作系统和安装软件是一样的，注意：Linux磁盘分区的时候需要注意分区名即可！/boot/home!

（随意去百度搜索下载一个CentOS7版本的镜像）下载地址：https://mirrors.aliyun.com/centos/7/isos/x86_64/

2、安装 VMware 虚拟机软件，然后打开Linux镜像即可使用！



**VMware的使用方式：**

点击屏幕进入虚拟机，Ctrl+Alt将聚焦退出虚拟机！



**注意的知识点：**

**1、vm的网卡配置，几个选项的区别**

**桥接模式：**表示虚拟机用物理机的真实网卡，不仅能和本机通信，而且还可以和在同一个局域网的其他真实计算机通信，缺点会占用真实网段的ip地址

**nat模式：**默认通过vmnet8这个虚拟网卡和本机通信，不能和同局域网的其他真实计算机通信，但如果本机联入了internet，虚拟机也会跟着能够访问internet

**仅主机模式：**默认通过vmnet1这个虚拟网卡和本机通信，不能链接internet

**网络相关的配置文件路径为：**

 /etc/sysconfig/network-scripts/ifcfg-ens33  #IP地址，子网掩码等配置文件

 /etc/sysconfig/network-scripts/ifcfg-lo #网卡回环地址

 /etc/resolv.conf  #DNS配置文件

 /etc/hosts  #设置主机和IP绑定信息

 /etc/hostname  #设置主机名

> 购买服务器

虚拟机安装后占用空间，也会有些卡顿，我们作为程序员其实可以选择购买一台自己的服务器，这样的话更加接近真实线上工作；

1、阿里云购买服务器：https://www.aliyun.com/minisite/goods?userCode=0phtycgr

2、购买完毕后，获取服务器的ip地址，重置服务器密码，就可以远程登录了

3、下载 xShell 工具，进行远程连接使用！

**注意事项：**

如果要打开端口，需要在阿里云的安全组面板中开启对应的出入规则，不然的话会被阿里拦截！

### Linux目录介绍

1、一切皆文件2、根目录/ ,所有的文件都挂载在这个节点下

登陆系统后，在当前命令窗口下输入命令：

```
ls /
 [root@localhost /]# ls
 bin   dev home lib64 mnt proc run   srv tmp var
 boot etc lib   media opt root sbin sys usr
```

**Linux不像windows有cdef磁盘，它是多根的系统 c:\  d:\  e:\，每个盘里有一个根目录，Linux就只有一个根目录/，其他的所有文件都在这个根目录下：**

![图片](Linux基础.assets/640)

**了解Linux文件系统的目录结构，是学好Linux的至关重要的一步.，深入了解linux文件目录结构的标准和每个目录的详细功能，对于我们用好linux系统很重要，下面我们就先大致了解一下linux目录结构的相关知识，为后面详细学习Linux的细节知识打基础。**

**1、/：根目录**，位于Linux文件系统目录结构的顶层，一般根目录下只存放目录，不要存放文件，/etc、/bin、/dev、/lib、/sbin应该和根目录放置在一个分区中。

**2、/bin，/usr/bin：这两个目录为命令文件目录**，也称为二进制目录。包含了供系统管理员及普通用户使用的重要的linux命令和二进制（可执行）文件，包含shell解释器等。

**3、/boot：该目录中存放系统的内核文件和引导装载程序文件**，/boot/vmlinuz为linux的内核文件，以及/boot/gurb。一般情况下都是单独分区，分区大小200M即可。

**4、/dev：设备（device）文件目录**，存放linux系统下的设备文件，访问该目录下某个文件，相当于访问某个设备，存放连接到计算机上的设备（终端、磁盘驱动器、光驱及网卡等）的对应文件，包括字符设备和块设备等，常用的是挂载光驱：**mount /dev/cdrom  /mnt。**

**5、/etc：系统配置文件存放的目录**，该目录存放系统的大部分配置文件和子目录，不建议在此目录下存放可执行文件，重要的配置文件有/etc/inittab、/etc/fstab、/etc/init.d、/etc/X11（X Window系统有关）、/etc/sysconfig（与网络有关）、/etc/xinetd.d等等，修改配置文件之前记得备份。该目录下的文件由系统管理员来使用，普通用户对大部分文件有只读权限。

**6、/home：系统默认的用户宿主目录**，新增用户账号时，用户的宿主目录都存放在此目录下，表示当前用户的宿主目录，test表示用户test的宿主目录。如果做文件服务器，建议单独分区，并设置较大的磁盘空间，方便用户存放数据。用户传上来的数据和其他系统文件隔离，安全性高些！

**7、/lib，/usr/lib，/usr/local/lib：系统使用的函数库的目录**，程序在执行过程中，需要调用一些额外的参数时需要函数库的协助，该目录下存放了各种编程语言库。典型的linux系统包含了C、C++和FORTRAN语言的库文件。/lib目录下的库映像文件可以用来启动系统并执行一些命令，目录/lib/modules包含了可加载的内核模块，/lib目录存放了所有重要的库文件，其他的库文件则大部分存放在/usr/lib目录下。（不要动）

**8、/mnt，/media：mnt目录主要用来临时挂载文件系统，为某些设备提供默认挂载点，如cdrom**。这样当挂载了一个设备如光驱时，就可以通过访问目录/mnt下的文件来访问相应的光驱上的文件了。

**9、/opt：给主机额外安装软件所摆放的目录**。如果想要自行安装新的KDE 桌面软件，可以将该软件安装在该目录下。以前的 Linux 系统中，习惯放置在 /usr/local 目录下。

**10、/proc：此目录的数据都在内存中，如系统核心，外部设备，网络状态，由于数据都存放于内存中**，所以不占用磁盘空间，比较重要的目录有/proc/cpuinfo、/proc/interrupts、/proc/dma、/proc/ioports、/proc/net/*等。

**11、/root：系统管理员root的宿主目录**，系统第一个启动的分区为/，所以最好将/root和/放置在一个分区下。

**12、/sbin，/usr/sbin，/usr/local/sbin：放置系统管理员使用的可执行命令**，如fdisk、shutdown、mount等。与/bin不同的是，这几个目录是给系统管理员root使用的命令，一般用户只能"查看"而不能设置和使用。

**13、/tmp：一般用户或正在执行的程序临时存放文件的目录**,任何人都可以访问,重要数据不可放置在此目录下。

**14、/srv：服务启动之后需要访问的数据目录**，如www服务需要访问的网页数据存放在/srv/www内。

**15、/usr：应用程序存放目录**，/usr/bin 存放应用程序， /usr/share 存放共享数据，/usr/lib 存放不能直接运行的，却是许多程序运行所必需的一些函数库文件，/usr/local 存放软件升级包，/usr/share/doc 系统说明文件存放目录。

**16、/usr/share/man: 程序说明文件存放目录**，使用 man ls时会查询/usr/share/man/man1/ls.1.gz的内容，建议单独分区，设置较大的磁盘空间。

**17、/var：放置系统执行过程中经常变化的文件，如随时更改的日志文件** /var/log。/var/log/message：所有的登录文件存放目录。/var/spool/mail：邮件存放的目录。建议单独分区，设置较大的磁盘空间。

**18、/run：**目录中存放的是自系统启动以来描述系统信息的文件

**19、 /sys：** 挂载点上挂载sysfs 虚拟文件系统，我们可以通过sysfs 文件系统访问 Linux 内核

**20、 /lost+found：** 这个目录一般情况下是空的，当系统非法关机后，这里就存放了一些文件。

**20、/www：**存放服务器网站相关的资源，环境，网站的项目

**在 Linux 系统中，有几个目录是特别需要注意的，以下提供几个需要注意的目录，以及预设相关的用途：**　

**/etc：这个目录相当重要**，如前所述，你的开机与系统数据文件均在这个目录之下，因此当这个目录被破坏，那你的系统大概也就差不多该死掉了！而在往后的文件中，你会发现我们常常使用这个目录下的 /etc/rc.d/init.d 这个子目录，因为这个 init.d 子目录是开启一些 Linux 系统服务的 scripts 的地方。而在 /etc/rc.d/rc.local 这个文件是开机的执行档。　

**/bin, /sbin, /usr/bin, /usr/sbin：这是系统预设的执行文件的放置目录**，例如 root 常常使用的 userconf, netconf, perl, gcc, c++ 等等的数据都放在这几个目录中，所以如果你在提示字符下找不到某个执行档时，可以在这四个目录中查一查！其中， /bin, /usr/bin 是给系统使用者使用的指令，而 /sbin, /usr/sbin 则是给系统管理员使用的指令！

**/usr/local：这是系统预设的让你安装你后来升级的套件的目录**。例如，当你发现有更新的 Web 套件（如 Apache ）可以安装，而你又不想以 rpm 的方式升级你的套件，则你可以将 apache 这个套件安装在 /usr/local 底下。安装在这里有个好处，因为目前大家的系统都是差不多的，所以如果你的系统要让别人接管的话，也比较容易上手！也比较容易找的到数据！因此，如果你有需要的话，通常都会将 /usr/local/bin 这个路径加到 path 中。

**/home：这个是系统将有账号的人口的家目录设置的地方，用户的主目录。**

**/var：这个路径就重要了！不论是登入、各类服务的问题发生时的记录、以及常态性的服务记录等等的记录目录**，所以当你的系统有问题时，就需要来这个目录记录的文件数据中察看问题的所在！而 mail 的预设放置也是在这里，所以他是很重要的 。

**/usr/share/man, /usr/local/man：这两个目录为放置各类套件说明档的地方**，例如你如果执行 man man，则系统会自动去找这两个目录下的所有说明文件。

补充小命令：tree：可以查看当前目录下的所有子目录和文件结构

### 走进Linux

#### 开机

开机会启动许多程序。它们在Windows中叫做Service(服务)，在Linux就叫做Daemon(守护进程)

一般来说登陆方式有三种：

- 命令行登陆
- SSH登陆
- 图形界面登陆

最高权限账户为root，可以操作一切！

#### 关机

在服务器上，基本不会遇到关机的操作，除非特殊情况下，才会不得已关机。

**常用的几个关机，重启命令**

**shutdown/reboot/init/powerof**

**关机命令之--shutdown**

**作用：关机，重启，定时关机**

**语法：shutdown  [选项]**

**参数：**

**-r  => 重新启动计算机**

**-h  => 关机**

**-h 时间 =>定时关机**

**例如:**

```
 sync        #将数据由内存同步到硬盘中。
 shutdown    #关机指令，你可以man shutdown 来看一下 帮助文档。例如你可以运行如下命令关机:
 shutdown -h 10   #这个命令告诉大家，计算机将在10分钟后关机
 shutdown -h now  #立马关机
 shutdown -h 20:25 #系统会在今天20:25关机
 shutdown -h +10   #十分钟后关机
 shutdown -r now   #系统立马重启
 shutdown -r +10   #系统十分钟后重启
 shutdown -c 取消关机
 reboot   #就是重启，等同于shutdown -r now
 halt     #关闭系统，等同于shutdown -h now和poweroff
```

最后总结：不管是重启还是关闭系统，首先要运行sync命令，把内存中的数据写到磁盘中，执行命令之后没有返回消息则表明运行成功

#### 系统时间管理

**查看和修改系统时间**：**查看硬件时间：# hwclock**  **查看系统时间：# date**

**按照自己想要的格式显示时间的命令：**

```
 # date "+%F" ：2018-11-02           #**%F：完整日期格式，等价于** **%Y-%m-%d（对应年-月-日）
 # date "+%Y-%m-%d" ：2018-11-02
```

**%M：分钟**    **%H 小时**     **%S  秒**

#### Linux命令使用细节和技巧总结

**到这里，我们已经学习了一些基础命令了，可以在前面学习的命令基础上，我们可以总结一下：**

**分析一下前面我们讲的命令的执行过程是：**

**用户输入命令——>传递到shell（命令解析器），shell把命令解析成二进制格式，这里用户的命令又分为内部命令和外部命令——>二进制命令进入系统内核，在内核中调用相关功能**

![图片](Linux基础.assets/640)

**内部命令和外部命令的区别：**

**内部命令：**系统启动的时候这些命令就会被调入内存，常住在内存中，所以执行效率很高

**外部命令：**是系统软件的功能，在用户需要运行这些软件的时候，才现从硬盘上把程序文件调入内存中，执行

**type 命令：**可以检测那些命令是外部命令那些是内部命令

**shell的提示符：**

**完整的提示符：用户名---@---主机名---当前所在目录(~表示当前用户的家目录)---(# root/$普通用户)**

 shell提示符有两个：“#”与“$” 他们的区别

- \#表示是root用户登录，管理员账号登陆
- $表示普通用户登录

**linux不同文件类型对应的颜色：**

- **白色：表示普通文件**
- **蓝色：表示目录**
- **绿色：表示可执行文件**
- **红色：表示压缩文件**
- **浅蓝色：链接文件**
- **红色闪烁：表示软链接的文件有问题**
- **黄色：表示设备文件**
- **灰色：表示其它文件**

**Linux快捷键总结：**  **结束运行中的程序：ctrl+c**     **退出终端：ctrl+d**   **清空屏幕：ctrl+L**

**打开历史命令收索：ctrl+r**    **命令补全：tab键**    **vi编辑文件的时候，快速定位光标到最后一行：G**



在linux终端先输入ls -al,可以看到如:

-rwx-r–r– (一共10个参数)

第一个跟参数跟chmod无关,先不管.

2-4  参数:属于user

5-7  参数:属于group

8-10参数:属于others

接下来就简单了:r = => 可读  w= =>可写 x= =>可执行

r=4 w=2 x=1

所以755代表 rwxr-xr-x

 

755 代表用户对该文件拥有读，写，执行的权限，同组其他人员拥有执行和读的权限，没有写的权限，其他用户的权限和同组人员权限一样。

777代表，user,group ,others ,都有读写和可执行权限。