# 计网 概略笔记

>  原链接：https://blog.csdn.net/Lifereunion/article/details/118384823



## 互联网协议介绍

​	互联网的核心是一系列协议，总称为”互联网协议”（Internet Protocol Suite），正是这一些协议规定了电脑如何连接和组网。理解了这些协议，就理解了互联网的原理。由于这些协议太过庞大和复杂，没有办法在这里一概而全，只能介绍一下日常开发中接触较多的几个协议。

### 互联网分层模型

​	互联网的逻辑实现被分为好几层。每一层都有自己的功能，就像建筑物一样，每一层都靠下一层支持。用户接触到的只是最上面的那一层，根本不会感觉到下面的几层。要理解互联网就需要自下而上理解每一层的实现的功能。



​	如上图所示，互联网按照不同的模型划分会有不用的分层，但是不论按照什么模型去划分，越往上的层越靠近用户，越往下的层越靠近硬件。在软件开发中我们使用最多的是上图中将互联网划分为五个分层的模型。

​	接下来我们一层一层的自底向上介绍一下每一层。

### 物理层

​	电脑要与外界互联网通信，需要先把电脑连接网络，可以用双绞线、光纤、无线电波等方式。这就叫做”实物理层”，它就是把电脑连接起来的物理手段。它主要规定了网络的一些电气特性，作用是负责传送0和1的电信号。

### 数据链路层

​	单纯的0和1没有任何意义，所以我们使用者会为其赋予一些特定的含义，规定解读电信号的方式：例如：多少个电信号算一组？每个信号位有何意义？这就是”数据链接层”的功能，它在”物理层”的上方，确定了物理层传输的0和1的分组方式及代表的意义。早期的时候，每家公司都有自己的电信号分组方式。逐渐地，一种叫做”以太网”（Ethernet）的协议，占据了主导地位。

​	以太网规定，一组电信号构成一个数据包，叫做”帧”（Frame）。每一帧分成两个部分：标头（Head）和数据（Data）。其中”标头”包含数据包的一些说明项，比如发送者、接受者、数据类型等等；”数据”则是数据包的具体内容。”标头”的长度，固定为18字节。”数据”的长度，最短为46字节，最长为1500字节。因此，整个”帧”最短为64字节，最长为1518字节。如果数据很长，就必须分割成多个帧进行发送。

​	那么，发送者和接受者是如何标识呢？以太网规定，连入网络的所有设备都必须具有”网卡”接口。数据包必须是从一块网卡，传送到另一块网卡。网卡的地址，就是数据包的发送地址和接收地址，这叫做MAC地址。每块网卡出厂的时候，都有一个全世界独一无二的MAC地址，长度是48个二进制位，通常用12个十六进制数表示。前6个十六进制数是厂商编号，后6个是该厂商的网卡流水号。有了MAC地址，就可以定位网卡和数据包的路径了。

​	通过ARP协议来获取接受方的MAC地址，有了MAC地址之后，如何把数据准确的发送给接收方呢？其实这里以太网采用了一种很”原始”的方式，它不是把数据包准确送到接收方，而是向本网络内所有计算机都发送，让每台计算机读取这个包的”标头”，找到接收方的MAC地址，然后与自身的MAC地址相比较，如果两者相同，就接受这个包，做进一步处理，否则就丢弃这个包。这种发送方式就叫做”广播”（broadcasting）。

### 网络层

​	按照以太网协议的规则我们可以依靠MAC地址来向外发送数据。理论上依靠MAC地址，你电脑的网卡就可以找到身在世界另一个角落的某台电脑的网卡了，但是这种做法有一个重大缺陷就是以太网采用广播方式发送数据包，所有成员人手一”包”，不仅效率低，而且发送的数据只能局限在发送者所在的子网络。也就是说如果两台计算机不在同一个子网络，广播是传不过去的。这种设计是合理且必要的，因为如果互联网上每一台计算机都会收到互联网上收发的所有数据包，那是不现实的。

​	因此，必须找到一种方法区分哪些MAC地址属于同一个子网络，哪些不是。如果是同一个子网络，就采用广播方式发送，否则就采用”路由”方式发送。这就导致了”网络层”的诞生。它的作用是引进一套新的地址，使得我们能够区分不同的计算机是否属于同一个子网络。这套地址就叫做”网络地址”，简称”网址”。

​	“网络层”出现以后，每台计算机有了两种地址，一种是MAC地址，另一种是网络地址。两种地址之间没有任何联系，MAC地址是绑定在网卡上的，网络地址则是网络管理员分配的。网络地址帮助我们确定计算机所在的子网络，MAC地址则将数据包送到该子网络中的目标网卡。因此，从逻辑上可以推断，必定是先处理网络地址，然后再处理MAC地址。

​	规定网络地址的协议，叫做IP协议。它所定义的地址，就被称为IP地址。目前，广泛采用的是IP协议第四版，简称IPv4。IPv4这个版本规定，网络地址由32个二进制位组成，我们通常习惯用分成四段的十进制数表示IP地址，从0.0.0.0一直到255.255.255.255。

​	根据IP协议发送的数据，就叫做IP数据包。IP数据包也分为”标头”和”数据”两个部分：”标头”部分主要包括版本、长度、IP地址等信息，”数据”部分则是IP数据包的具体内容。IP数据包的”标头”部分的长度为20到60字节，整个数据包的总长度最大为65535字节。

### 传输层

​	有了MAC地址和IP地址，我们已经可以在互联网上任意两台主机上建立通信。但问题是同一台主机上会有许多程序都需要用网络收发数据，比如QQ和浏览器这两个程序都需要连接互联网并收发数据，我们如何区分某个数据包到底是归哪个程序的呢？也就是说，我们还需要一个参数，表示这个数据包到底供哪个程序（进程）使用。这个参数就叫做”端口”（port），它其实是每一个使用网卡的程序的编号。每个数据包都发到主机的特定端口，所以不同的程序就能取到自己所需要的数据。

​	“端口”是0到65535之间的一个整数，正好16个二进制位。0到1023的端口被系统占用，用户只能选用大于1023的端口。有了IP和端口我们就能实现唯一确定互联网上一个程序，进而实现网络间的程序通信。

​	在数据包中加入端口信息，这就需要新的协议。最简单的实现叫做UDP协议，它的格式几乎就是在数据前面，加上端口号。UDP数据包，也是由”标头”和”数据”两部分组成：”标头”部分主要定义了发出端口和接收端口，”数据”部分就是具体的内容。UDP数据包非常简单，”标头”部分一共只有8个字节，总长度不超过65,535字节，正好放进一个IP数据包。

​	UDP 协议的优点是比较简单，容易实现，但是缺点是可靠性较差，一旦数据包发出，无法知道对方是否收到。为了解决这个问题，	提高网络可靠性，TCP协议就诞生了。TCP协议能够确保数据不会遗失。它的缺点是过程复杂、实现困难、消耗较多的资源。TCP数据包没有长度限制，理论上可以无限长，但是为了保证网络的效率，通常TCP数据包的长度不会超过IP数据包的长度，以确保单个TCP数据包不必再分割。

### 应用层

​	应用程序收到”传输层”的数据，接下来就要对数据进行解包。由于互联网是开放架构，数据来源五花八门，必须事先规定好通信的数据格式，否则接收方根本无法获得真正发送的数据内容。”应用层”的作用就是规定应用程序使用的数据格式，例如我们TCP协议之上常见的Email、HTTP、FTP等协议，这些协议就组成了互联网协议的应用层。

​	如下图所示，发送方的HTTP数据经过互联网的传输过程中会依次添加各层协议的标头信息，接收方收到数据包之后再依次根据协议解包得到数据。

![HTTP数据传输图解](images/httptcpip.png)

## Socket 编程

​	Socket 是BSD UNIX的进程通信机制，通常也称作”套接字”，用于描述IP地址和端口，是一个通信链的句柄。Socket可以理解为TCP/IP网络的API，它定义了许多函数或例程，程序员可以用它们来开发TCP/IP网络上的应用程序。电脑上运行的应用程序通常通过”套接字”向网络发出请求或者应答网络请求。

> Socket把底层的内容给屏蔽掉了，我们只需要关注于Socket的API层面，即可完成网络通信

### Socket 图解

​	`Socket`是应用层与TCP/IP协议族通信的中间软件抽象层。在设计模式中，`Socket`其实就是一个门面模式，它把复杂的TCP/IP协议族隐藏在`Socket`后面，对用户来说只需要调用Socket规定的相关函数，让`Socket`去组织符合指定的协议数据然后进行通信。

![socket图解](images/socket.png)

# 1.计算机网络体系结构

## 1.1.OSI模型

`OSI`是`Open System Interconnection`，开放系统互联。

**`OSI`七层模型：**

- 7 应用层(Application)：能够产生网络流量能够和用户交互的应用程序。

- 6 表示层(Presentation)：数据加密压缩？ (开发人员需要考虑的问题）。

- 5 会话层(Session)：服务和客户端建立的会话 (查木马 netstat -nb)。

- 4 运输层(Transport): 可靠传输(建立会话)、不可靠传输(不建立会话)、流量控制。

- 3 网络层(Network)：IP地址编址 选择最佳路径。

- 2 数据链路层(Data Link)：数据如何封装 添加物理层地址(MAC地址)。

- 1 物理层(Physical)：电压 接口标准。

程序开发人员需要考虑：应用层、表示层、会话层。

网络工程师需要考虑：运输层、网络层、数据链路层、物理层。



进行网络排错时：要从低层向高层来排错。

![osi七层模型](images/osi.png)



## 1.2.TCP/IP模型

**`TCP/IP`五层模型：**

- 5应用层(Appplication)：传输数据单元。

- 4运输层(Transport)：运输层报文。

- 3网络层(Network)：IP数据报(IP分组)。

- 2数据链路层(Data Link)：数据帧。

- 1物理层(Physical)：比特流 011010101。

# 2.物理层

## 2.1.物理层基本概念

物理层解决如何在连接各种计算机的**传输媒体**上传输**数据比特流**，而不是指具体的传输媒体

物理层的主要任务描述为：确定与传输媒体的接口的一些特性，即：

- 机械特性：例如接口形状，大小，引线数目
- 电气特性：例如规定电压范围（-5V到+5V）
- 功能特性：例如规定-5V表示0，+5V表示1
- 过程特性：也称规程特性，规定建立连接时各个相关部件的工作步骤

## 2.2.数据通信基础知识

> 1、典型的数据通信模型

**相关术语：**

- 通信的目的是传送消息。
- 数据(data)：运送消息的实体。
- 信号(signal)：数据的电气的或电磁的表现。
- 模拟信号：代表消息的参数的取值是连续的。
- 数字信号：代表消息的参数的取值是离散的。

# 3.数据链路层

## 3.1.基本概念

**数据链路层的信道类型：**

- **点对点信道。**这种信道使用一对一的点对点通信方式。
- **广播信道。**这种信道使用一对多的广播通信方式，因此过程比较复杂。广播信道上连接的主机很多，因此必须使用专用的共享信道协议来协调这些主机的数据发送。

**链路与数据链路：**

- **链路：**是一条点对点的物理线路段，中间没有任何其他断点。**一条链路只是一条通路的一个组成部分。**
- **数据链路：**除了物理线路外，还必须有通信协议来控制这些数据的传输。若把实现这些协议的硬件和软件加到链路上，就构成了数据链路。**现在最常用的方法是使用适配器（即网卡）来实现这些协议的硬件和软件。一般的适配器都包括了数据链层和物理层这两层的功能。**



**帧：**数据链路层传送的是帧。

**数据链路层像个数字管道：**常常在两个対等的数据链层之间画一个数字管道，而在这条数字管道上传输的数据单位是帧。

## 3.2.三个基本问题

> 问题1：封装成帧

**封装成帧：**

- 就是在一段数据前后分别添加首部和尾部,然后就构成了一个帧。确定帧的界限。
- 首部和尾部的一个重要作用就是进行帧定界。



问题：为什么要有帧首部和帧尾部？只有帧首部不行吗？

计算机在接收帧的时候，只有同时收到帧首部和帧尾部才认为这是一个完整的帧，如果只有帧首部或者只有帧尾部，那么接收端计算机会将该帧丢弃，要求发送端计算机重新发送帧。



> 问题2：透明传输

**透明传输**：数据在传输过程中，数据部分可能会有帧开始符和帧结束符。这时就会出现问题，如下图所示：



**用字节填充法解决透明传输的问题**：

- 发送端的数据链路层在数据中出现控制字符`SOH`或者`EOT`的前面插入转义字符`ESC`。
- 接收端的数据链路层在将数据送往网络层之前删除插入的**转义字符**。
- 如果转义字符也出现数据当中，那么应在转义字符之前插入一个转义字符。当接收端收到连续的两个转义字符时，就删除其中前面的一个。



> 问题3：差错控制

传输过程中可能会产生**比特差错**：1可能会变成0，而0也可能变成1。为了保证数据传输的可靠性，在计算机网络传输数据时，必须采用各种**差错检测措施**。

**循环冗余检验CRC可以用于差错检测**。CRC差错检测技术只能做到**无差错接收**。

- 无差错接收：凡是接收端数据链路层接收的帧都没有传输差错（有差错的帧就被抛弃而不被接收）。

**要做到可靠传输,计算机端就必须加上确认和重传机制。**

## 3.3.PPP协议

**PPP协议**：`Point-to-Point Protocol `，现在全世界使用的最多的数据链路层协议是**点对点协议**。用户使用拨号上网，接入到因特网时，一般都是使用`PPP`协议。

**PPP协议具有以下功能**：

（1）PPP具有动态分配[IP](https://baike.baidu.com/item/IP)地址的能力，允许在连接时刻协商IP地址；

（2）PPP支持多种[网络协议](https://baike.baidu.com/item/网络协议)，比如[TCP/IP](https://baike.baidu.com/item/TCP%2FIP)、[NetBEUI](https://baike.baidu.com/item/NetBEUI)、[NWLINK](https://baike.baidu.com/item/NWLINK)等；

（3）PPP具有错误检测能力，但不具备纠错能力，所以**PPP协议是不可靠传输协议**；

（4）无重传的机制，网络开销小，速度快。

（5）PPP具有[身份验证](https://baike.baidu.com/item/身份验证)功能。

（6） PPP可以用于多种类型的物理介质上，包括串口线、电话线、移动电话和光纤（例如SDH），PPP也用于Internet接入。

**PPP协议的组成**：

- 网络控制协议(NCP)：允许在点到点上连接上使用多种网络层协议。
- 链路控制协议(LCP)：建立并维护数据链路连接。
- 高级数据链路控制协议(HDLC)。



> PPP协议帧格式

![PPP协议帧格式](https://img-blog.csdnimg.cn/20200810225701704.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1JyaW5nb18=,size_16,color_FFFFFF,t_70)

`PPP`协议有2个字节的协议字段。

- `0x0021`——PPP帧的信息字段就是IP数据报。
- `0xC021`——信息字段是PPP链路控制数据。
- `0x8021`——表示信息部分是网络控制数据。
- `0xC023`——信息字段是安全性认证PAP。
- `0xC025`——信息字段是LQR。
- `0xC233`——信息字段是安全性认证`CHAP`。



**字节填充问题**：如果信息字段中出现了标志字段的值，可能会误认为“结束标志”怎么办？？？？

- 将信息字段中出现的每个`0x7E`字节转变成为2字节序列（`0x7D`和`0x5E`）。

- 若信息字段中出现一个`0x7D`字节，则将其转变为2个字节序列（`0x7D`和`0x5D`）。

## 3.4.以太网

**以太网是一种计算机局域网技术**。

局域网的主要特点是：网络为一个单位所拥有，并且地理范围和站点数目均有限。

局域网具有如下的一些主要优点：

- 具有广播功能，从一个站点可很方便地访问全网。局域网上的主机可共享连接在局域网上的各种硬件和软件资源。
- 便于系统的扩展和逐渐地演变，各设备的位置可以灵活调整和改变。
- 提高了系统的可靠性、可用性和生存性。

# 4.网络层

## 4.1.虚拟互联网

> 网络互连的设备

**中间设备**又称为中间系统或者中继系统。

- 物理层中继系统：转发器`repeater`。
- 数据链路层中继系统：网桥或桥接器`bridge`。
- 网络层中继系统：路由器`router`。
- 网络层以上的中继系统：网关`gateway`。

> 互连网络与虚拟互连网络

虚拟互连网络：

- 所谓虚拟互连网络，它的意思就是互连起来的各种物理网络的异构性本来是客观存在的，但是我们利用IP协议就可以把这些性能各异的网络**在网络层看起来好像是一个统一的网络。**
- 这种使用IP协议的虚拟互连网络可简称为IP网。
- 使用IP网通信的好处是：**当IP网上的主机进行通信时，就好像在一个单个网络上通信一样，他们看不见互连的各网络的具体异构细节**（如具体的编址方案、路由选择协议、等等）。

> IP协议简介

网际协议IP是`TCP/IP`体系中两个最主要的协议之一。

与IP协议配合使用的还有四个协议：

- `ARP`：地址解析协议、
- `RARP`：逆地址解析协议。
- `ICMP`：网际控制报文协议。
- `IGMP`：网际组管理协议。

**网络层IP协议：RIP、OSPF、BGP协议动态路由最佳路径这些协议统称为IP协议**。



## 4.2.IP地址

> IP地址中的网络号字段和主机号字段



常用的三种类比的网络IP地址：

| 网络类别 |  最大网络数  | 第一个可用的网络号 | 最后一个可用的网络号 | 每个网络中最大的主机数 |
| :------: | :----------: | :----------------: | :------------------: | :--------------------: |
|    A     | 126(2^7 - 2) |         1          |         126          |       16,777,214       |
|    B     |   2^14 - 1   |       128.1        |       191.255        |         65,534         |
|    C     |   2^21 - 1   |      192.0.1       |     223.255.255      |          254           |

**特殊的地址**：

- `127.0.0.1`：本机地址。

**保留的私网地址**：在互联网上是没有这些地址的，只能在企业的内网中可以访问。

- `10.0.0.0`。
- `172.16.0.0`——`172.31.0.0`。
- `192.168.0.0`——`192.168.255.0`。

## 4.3.子网掩码

> 子网掩码的作用

**子网掩码主要作用**：

- 用于屏蔽IP地址的一部分以区别网络标识和主机标识，并说明该IP地址是在局域网上，还是在远程网上。
- 是用于将一个大的IP网络划分为若干小的子网络。



## 4.4.子网划分

> 1、将一个C类网等分成两个子网

**通常来说，某个网段的第一个主机地址，被设置为网关。**

- A子网第一个可用的主机地址：`192.168.0.1`。

- B子网第一个可用的主机地址：`192.168.0.129`。



理解：如果说，将`192.168.0.0 255.255.255.0`这个C类网络划分为4个子网，那么子网掩码就是`255.255.255.192`。



## 4.4.IP地址和MAC地址

> IP地址和MAC地址的关系



> 计算机之间通信过程



## 4.5.ARP协议

> ARP协议

`ARP`：地址解析协议，是根据IP地址获取计算机物理MAC地址的协议。主机发送信息时将包含目标IP地址的ARP请求**广播**到局域网络上的所有主机，并接收返回消息，以此确定目标的物理地址。

**一句话：通过目标设备的IP地址，查询目标设备的MAC地址，以保证通信的进行**。

> ARP欺骗

常见的ARP欺骗手法：同时对局域网内的一台主机和网关进行ARP欺骗，更改这台主机和网关的ARP缓存表。如下图（PC2是攻击主机，PC1是被攻击主机）所示：

攻击主机PC2发送ARP应答包给被攻击主机PC1和网关，分别修改它们的ARP缓存表, 将它们的IP地址所对应的MAC地址，全修改为攻击主机PC2的MAC地址，这样它们之间数据都被攻击主机PC2截获。

> 如何查看ARP欺骗

```shell
# 使用arp -a 命令来查看当前计算机连接到网关的MAC地址
# 如果查询到的MAC地址就是我们路由器网关的MAC地址,证明没有发生ARP欺骗
# 如果查询到的MAC地址就是其他计算机的MAC地址，说明发生了ARP欺骗，当前计算机的数据会被拦截
C:\Users\14666>arp -a

接口: 192.168.136.1 --- 0x2
  Internet 地址         物理地址              类型
  192.168.136.254       00-50-56-f0-d3-f2     动态
  192.168.136.255       ff-ff-ff-ff-ff-ff     静态
  224.0.0.2             01-00-5e-00-00-02     静态
  224.0.0.22            01-00-5e-00-00-16     静态
  224.0.0.251           01-00-5e-00-00-fb     静态
  224.0.0.252           01-00-5e-00-00-fc     静态
  239.255.255.250       01-00-5e-7f-ff-fa     静态
  255.255.255.255       ff-ff-ff-ff-ff-ff     静态
```

## 4.6.IP数据报首部

**一个IP数据报由首部和数据两部分组成**。

- 首部的前一部分是固定长度，共20字节，是所有IP数据报必须具有的。
- 在首部的固定部分的后面试一些可选字段，其长度是可变的！

IP数据报首部：

- 版本：占4位，指IP协议的版本，目前的IP协议版本号为4（即IPv4）。

- 首部长度：占4位，可表示的最大数值，是15个单位（一个单位为4字节），因此IP数据报首部长度的最大值是60字节。

- 区分服务（QoS服务质量）：占8位，用来获取更好的服务，只有在使用区分服务时，这个字段才起作用。在一般的情况下都不使用这个字段。

- 总长度：占16位，指首部和数据部分之和的长度，单位为字节，因此IP数据报的最大长度为`2^16 - 1 = 65535`字节。总长度必须不超过最大传送单元MTU。

- 标识：占16位，它是一个计数器，用来产生数据报的标识，不是序号，每产生一个数据报，就增加1。

- 片偏移：占13位，较长的分组在分片后，某片在原分组中的相对位置。片偏移以8个字节为偏移单位。

- 生存时间：`TTL Time to Live`。IP数据报每过一个路由器TTL就会减1。永远到达不了的IP数据报就会超时。

  ```shell
  # ping 百度的服务器 使TTL = 5 数据报都到不了服务器就过期了！
  C:\Users\14666>ping www.baidu.com -i 5
  
  正在 Ping www.a.shifen.com [39.156.66.18] 具有 32 字节的数据:
  请求超时。
  来自 221.183.47.205 的回复: TTL 传输中过期。
  来自 221.183.47.205 的回复: TTL 传输中过期。
  来自 221.183.47.205 的回复: TTL 传输中过期。
  
  39.156.66.18 的 Ping 统计信息:
      数据包: 已发送 = 4，已接收 = 3，丢失 = 1 (25% 丢失)，
  ```

- 协议：占8位，指出此数据报携带的数据使用何种协议，以便目的主机的IP层将数据部分上交给哪个处理过程。

- 首部检验和：检查首部有没有错误。
- 源地址和目标地址：发送端IP地址和接收端IP地址。
- 可变部分：IP首部的可变长部分就是一个选项字段，用来支持排错、测量以及安全等措施，内容很丰富。

## 4.7.ICMP协议

> ICMP简介

ICMP（`Internet Control Message Protocol`）：

- ICMP允许主机或路由器报告差错情况和提供有关异常情况的报告。
- **ICMP不是高层协议，而是IP层的协议。**
- ICMP报文作为IP层数据报的数据，加上数据报的首部，组成IP数据报发送出来。

> ICMP报文类型

- `ICMP`报文的种类有两种，即**ICMP差错报告报文**和**ICMP询问报文**。
- `ICMP`报文的前4个字节是统一的格式，共有三个字段：即类型、代码和检验和。接着的4个字节的内容与ICMP的类型有关。
- 差错报告报文有五种：终点不可达、源点抑制、时间超过、参数问题、改变路由（重定向）。
- 询问报文有两种：回送请求和回答报文，时间戳请求和回答报文。

```shell
# pathping 跟踪路由转发 可以查看哪里丢包多
C:\Windows\System32>pathping www.baidu.com

通过最多 30 个跃点跟踪
到 www.a.shifen.com [39.156.66.18] 的路由:
  0  LAPTOP-19OEB0UT [192.168.31.254]
  1  XiaoQiang [192.168.31.1]
  2  100.93.64.1
  3  111.63.223.193
  4  111.11.64.21
  5     *     221.183.47.205
  6  221.183.37.189
  7     *        *        *
正在计算统计信息，已耗时 150 秒...
            指向此处的源   此节点/链接
跃点  RTT    已丢失/已发送 = Pct  已丢失/已发送 = Pct  地址
  0                                           LAPTOP-19OEB0UT [192.168.31.254]
                                0/ 100 =  0%   |
  1    4ms     0/ 100 =  0%     0/ 100 =  0%  XiaoQiang [192.168.31.1]
                                0/ 100 =  0%   |
  2    8ms     0/ 100 =  0%     0/ 100 =  0%  100.93.64.1
                                0/ 100 =  0%   |
  3    5ms     0/ 100 =  0%     0/ 100 =  0%  111.63.223.193
                                0/ 100 =  0%   |
  4   10ms     0/ 100 =  0%     0/ 100 =  0%  111.11.64.21
                                0/ 100 =  0%   |
  5    7ms     0/ 100 =  0%     0/ 100 =  0%  221.183.47.205
                              100/ 100 =100%   |
  6  ---     100/ 100 =100%     0/ 100 =  0%  221.183.37.189

跟踪完成。
```

# 5.运输层

## 5.1.运输层和应用层的关系

```shell
# netstat -an 可以查看计算机侦听的端口
C:\Users\14666>netstat -an

活动连接

  协议  本地地址          外部地址        状态
  TCP    0.0.0.0:135            0.0.0.0:0              LISTENING
  TCP    0.0.0.0:443            0.0.0.0:0              LISTENING
  TCP    0.0.0.0:445            0.0.0.0:0              LISTENING
  TCP    0.0.0.0:902            0.0.0.0:0              LISTENING
  TCP    0.0.0.0:912            0.0.0.0:0              LISTENING
  TCP    0.0.0.0:3306           0.0.0.0:0              LISTENING
  TCP    0.0.0.0:5040           0.0.0.0:0              LISTENING
  TCP    0.0.0.0:49664          0.0.0.0:0              LISTENING
  TCP    0.0.0.0:49665          0.0.0.0:0              LISTENING
  TCP    0.0.0.0:49666          0.0.0.0:0              LISTENING
  TCP    0.0.0.0:49667          0.0.0.0:0              LISTENING
  TCP    0.0.0.0:49668          0.0.0.0:0              LISTENING
  TCP    0.0.0.0:49680          0.0.0.0:0              LISTENING
  TCP    127.0.0.1:8307         0.0.0.0:0              LISTENING
  TCP    127.0.0.1:28317        0.0.0.0:0              LISTENING
  TCP    127.0.0.1:50094        127.0.0.1:65001        ESTABLISHED
  TCP    127.0.0.1:50119        0.0.0.0:0              LISTENING
  TCP    127.0.0.1:65000        0.0.0.0:0              LISTENING
  TCP    127.0.0.1:65001        0.0.0.0:0              LISTENING
  TCP    127.0.0.1:65001        127.0.0.1:50094        ESTABLISHED
  TCP    192.168.31.254:139     0.0.0.0:0              LISTENING
  TCP    192.168.31.254:50124   52.139.250.253:443     ESTABLISHED
  TCP    192.168.31.254:50339   60.210.8.160:443       ESTABLISHED
  TCP    192.168.31.254:50340   60.210.8.160:443       ESTABLISHED
  TCP    192.168.31.254:50341   60.210.8.160:443       ESTABLISHED
  TCP    192.168.31.254:50358   23.41.64.91:80         ESTABLISHED
  TCP    192.168.31.254:50372   23.207.174.68:443      ESTABLISHED
  TCP    192.168.31.254:50911   111.62.72.10:443       ESTABLISHED
  TCP    192.168.31.254:51172   111.62.79.254:443      ESTABLISHED
  TCP    192.168.31.254:51297   120.92.79.46:7823      ESTABLISHED
  TCP    192.168.31.254:51366   111.62.241.131:443     ESTABLISHED
  TCP    192.168.31.254:51369   111.62.72.6:443        ESTABLISHED
  TCP    192.168.31.254:51375   202.89.233.101:443     ESTABLISHED
  TCP    192.168.31.254:51376   202.89.233.101:443     ESTABLISHED
  TCP    192.168.31.254:51377   40.90.22.185:443       ESTABLISHED
  TCP    192.168.31.254:51378   204.79.197.222:443     ESTABLISHED
  TCP    192.168.31.254:51380   13.107.4.254:443       ESTABLISHED
  TCP    192.168.31.254:51381   13.107.246.254:443     ESTABLISHED
  TCP    192.168.31.254:51382   13.107.6.254:443       ESTABLISHED
  TCP    192.168.31.254:51384   109.244.23.87:80       TIME_WAIT
  TCP    192.168.31.254:51389   112.34.111.123:443     ESTABLISHED
  TCP    192.168.110.1:139      0.0.0.0:0              LISTENING
  TCP    192.168.136.1:139      0.0.0.0:0              LISTENING
  TCP    [::]:135               [::]:0                 LISTENING
  TCP    [::]:443               [::]:0                 LISTENING
  TCP    [::]:445               [::]:0                 LISTENING
  TCP    [::]:49664             [::]:0                 LISTENING
  TCP    [::]:49665             [::]:0                 LISTENING
  TCP    [::]:49666             [::]:0                 LISTENING
  TCP    [::]:49667             [::]:0                 LISTENING
  TCP    [::]:49668             [::]:0                 LISTENING
  TCP    [::]:49680             [::]:0                 LISTENING
  TCP    [::1]:8307             [::]:0                 LISTENING
  TCP    [::1]:49916            [::]:0                 LISTENING
```

TCP：分段、编号、流量控制、建立会话 ，**可靠传输**。

UDP：一个数据包就能完成数据通信，不建立会话，**不可靠传输**。



