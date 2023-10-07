# 互联网协议

## Go 语言实现 TCP 通信

### TCP 协议

​	TCP/IP(Transmission Control Protocol/Internet Protocol) 即传输控制协议/网间协议，是一种面向连接（连接导向）的、可靠的、基于字节流的传输层（Transport layer）通信协议，因为是面向连接的协议，数据像水流一样传输，会存在黏包问题。

### TCP 服务端

​	一个TCP服务端可以同时连接很多个客户端，例如世界各地的用户使用自己电脑上的浏览器访问淘宝网。因为Go语言中创建多个`goroutine` 实现并发非常方便和高效，所以可以每建立一次链接就创建一个 `goroutine` 去处理。

TCP服务端程序的处理流程：

1. 监听端口
2. 接收客户端请求建立链接
3. 创建 `goroutine` 处理链接。

使用Go语言的 `net` 包实现的TCP服务端代码如下：

```go
// 用于接收请求的方法
func processConn(conn net.Conn)  {
	// 与客户端通信
	var tmp [128]byte
	// 使用for循环监听消息
	for {
		n, err := conn.Read(tmp[:])
		if err != nil {
			fmt.Println("read from conn failed, err:", err)
			return
		}
		fmt.Println(conn, string(tmp[:n]))
	}
}
func main() {
	// 本地端口启动服务
	listen, err := net.Listen("tcp", "127.0.0.1:20000")
	if err!= nil {
		fmt.Println("start server on failed ", err)
	}

	// for循环监听
	for {
		// 等待别人来建立连接
		conn, err := listen.Accept()
		if err != nil {
			fmt.Println("accept failed, err: ", err)
			return
		}
		go processConn(conn)
	}
}
```

### TCP 客户端

```go
func getInput() string {
	//使用os.Stdin开启输入流
	//函数原型 func NewReader(rd io.Reader) *Reader
	//NewReader创建一个具有默认大小缓冲、从r读取的*Reader 结构见官方文档
	in := bufio.NewReader(os.Stdin)
	//in.ReadLine函数具有三个返回值 []byte bool error
	//分别为读取到的信息 是否数据太长导致缓冲区溢出 是否读取失败
	str, _, err := in.ReadLine()
	if err != nil {
		return err.Error()
	}
	return string(str)
}

// tcp client
func main() {
	// 与server端建立连接
	conn, err := net.Dial("tcp", "127.0.0.1:20000")
	if err != nil {
		fmt.Println("dial 127.0.0.1:20000 failed, err：", err)
		return
	}
	// 发送数据
	conn.Write([]byte(getInput()))

	// 关闭流
	defer conn.Close()
}
```

## TCP 黏包

### 黏包实例

服务端代码如下：

```go
// socket_stick/server/main.go
func process(conn net.Conn) {
	defer conn.Close()
	reader := bufio.NewReader(conn)
	var buf [1024]byte
	for {
		n, err := reader.Read(buf[:])
		if err == io.EOF {
			break
		}
		if err != nil {
			fmt.Println("read from client failed, err:", err)
			break
		}
		recvStr := string(buf[:n])
		fmt.Println("收到client发来的数据：", recvStr)
	}
}

func main() {

	listen, err := net.Listen("tcp", "127.0.0.1:30000")
	if err != nil {
		fmt.Println("listen failed, err:", err)
		return
	}
	defer listen.Close()
	for {
		conn, err := listen.Accept()
		if err != nil {
			fmt.Println("accept failed, err:", err)
			continue
		}
		go process(conn)
	}
}
```

客户端代码如下：

```go
// socket_stick/client/main.go

func main() {
	conn, err := net.Dial("tcp", "127.0.0.1:30000")
	if err != nil {
		fmt.Println("dial failed, err", err)
		return
	}
	defer conn.Close()
    
    // 连续发送20次的hello到服务器
	for i := 0; i < 20; i++ {
		msg := `Hello, Hello. How are you?`
		conn.Write([]byte(msg))
	}
}
```

将上面的代码保存后，分别编译。先启动服务端再启动客户端，可以看到服务端输出结果如下：

```bash
收到client发来的数据： Hello, Hello. How are you?Hello, Hello. How are you?Hello, Hello. How are you?Hello, Hello. How are you?Hello, Hello. How are you?
收到client发来的数据： Hello, Hello. How are you?Hello, Hello. How are you?Hello, Hello. How are you?Hello, Hello. How are you?Hello, Hello. How are you?Hello, Hello. How are you?Hello, Hello. How are you?Hello, Hello. How are you?
收到client发来的数据： Hello, Hello. How are you?Hello, Hello. How are you?
收到client发来的数据： Hello, Hello. How are you?Hello, Hello. How are you?Hello, Hello. How are you?
收到client发来的数据： Hello, Hello. How are you?Hello, Hello. How are you?
```

客户端分10次发送的数据，在服务端并没有成功的输出10次，而是多条数据 粘 到了一起。



### 为什么会出现粘包

主要原因就是 `tcp` 数据传递模式是流模式，在保持长连接的时候可以进行多次的收和发。

“粘包”可发生在发送端也可发生在接收端：

1. 由 `Nagle` 算法造成的发送端的粘包： `Nagle` 算法是一种改善网络传输效率的算法。简单来说就是当提交一段数据给TCP发送时，TCP并不立刻发送此段数据，而是等待一小段时间看看在等待期间是否还有要发送的数据，若有则会一次把这两段数据发送出去。
2. 接收端接收不及时造成的接收端粘包：TCP会把接收到的数据存在自己的缓冲区中，然后通知应用层取数据。当应用层由于某些原因不能及时的把TCP的数据取出来，就会造成TCP缓冲区中存放了几段数据。

### 解决办法

出现”粘包”的关键在于接收方不确定将要传输的数据包的大小，因此可以对数据包进行封包和拆包的操作。

封包：封包就是给一段数据加上包头，这样一来数据包就分为包头和包体两部分内容了(过滤非法包时封包会加入”包尾”内容)。包头部分的长度是固定的，并且它存储了包体的长度，根据包头长度固定以及包头中含有包体长度的变量就能正确的拆分出一个完整的数据包。

可以自己定义一个协议，比如数据包的前4个字节为包头，里面存储的是发送的数据的长度。

```go
// socket_stick/proto/proto.go
package proto

import (
	"bufio"
	"bytes"
	"encoding/binary"
)

// Encode 将消息编码
func Encode(message string) ([]byte, error) {
	// 读取消息的长度，转换成int32类型（占4个字节）
	var length = int32(len(message))
	var pkg = new(bytes.Buffer)
	// 写入消息头
	err := binary.Write(pkg, binary.LittleEndian, length)
	if err != nil {
		return nil, err
	}
	// 写入消息实体
	err = binary.Write(pkg, binary.LittleEndian, []byte(message))
	if err != nil {
		return nil, err
	}
	return pkg.Bytes(), nil
}

// Decode 解码消息
func Decode(reader *bufio.Reader) (string, error) {
	// 读取消息的长度
	lengthByte, _ := reader.Peek(4) // 读取前4个字节的数据
	lengthBuff := bytes.NewBuffer(lengthByte)
	var length int32
	err := binary.Read(lengthBuff, binary.LittleEndian, &length)
	if err != nil {
		return "", err
	}
	// Buffered返回缓冲中现有的可读取的字节数。
	if int32(reader.Buffered()) < length+4 {
		return "", err
	}

	// 读取真正的消息数据
	pack := make([]byte, int(4+length))
	_, err = reader.Read(pack)
	if err != nil {
		return "", err
	}
	return string(pack[4:]), nil
}
```

接下来在服务端和客户端分别使用上面定义的`proto`包的`Decode`和`Encode`函数处理数据。

服务端代码如下：

```go
// socket_stick/server2/main.go

func process(conn net.Conn) {
	defer conn.Close()
	reader := bufio.NewReader(conn)
	for {
		msg, err := proto.Decode(reader)
		if err == io.EOF {
			return
		}
		if err != nil {
			fmt.Println("decode msg failed, err:", err)
			return
		}
		fmt.Println("收到client发来的数据：", msg)
	}
}

func main() {

	listen, err := net.Listen("tcp", "127.0.0.1:30000")
	if err != nil {
		fmt.Println("listen failed, err:", err)
		return
	}
	defer listen.Close()
	for {
		conn, err := listen.Accept()
		if err != nil {
			fmt.Println("accept failed, err:", err)
			continue
		}
		go process(conn)
	}
}
```

客户端代码如下：

```go
// socket_stick/client2/main.go

func main() {
	conn, err := net.Dial("tcp", "127.0.0.1:30000")
	if err != nil {
		fmt.Println("dial failed, err", err)
		return
	}
	defer conn.Close()
	for i := 0; i < 20; i++ {
		msg := `Hello, Hello. How are you?`
		data, err := proto.Encode(msg)
		if err != nil {
			fmt.Println("encode msg failed, err:", err)
			return
		}
		conn.Write(data)
	}
}
```

## Go 语言实现 UDP 通信

### UDP 协议

​	UDP协议（User Datagram Protocol）中文名称是用户数据报协议，是OSI（Open System Interconnection，开放式系统互联）参考模型中一种**无连接**的传输层协议，不需要建立连接就能直接进行数据发送和接收，属于不可靠的、没有时序的通信，但是UDP协议的实时性比较好，通常用于视频直播相关领域。

### UDP 服务端

使用Go语言的`net`包实现的UDP服务端代码如下：

```go
// UDP/server/main.go

// UDP server端
func main() {
	listen, err := net.ListenUDP("udp", &net.UDPAddr{
		IP:   net.IPv4(0, 0, 0, 0),
		Port: 30000,
	})
	if err != nil {
		fmt.Println("listen failed, err:", err)
		return
	}
	defer listen.Close()
	for {
		var data [1024]byte
		n, addr, err := listen.ReadFromUDP(data[:]) // 接收数据
		if err != nil {
			fmt.Println("read udp failed, err:", err)
			continue
		}
		fmt.Printf("data:%v addr:%v count:%v\n", string(data[:n]), addr, n)
		_, err = listen.WriteToUDP(data[:n], addr) // 发送数据
		if err != nil {
			fmt.Println("write to udp failed, err:", err)
			continue
		}
	}
}
```

### UDP 客户端

使用Go语言的`net`包实现的UDP客户端代码如下：

```go
// UDP 客户端
func main() {
    // 拨号连接
	socket, err := net.DialUDP("udp", nil, &net.UDPAddr{
		IP:   net.IPv4(0, 0, 0, 0),
		Port: 30000,
	})
	if err != nil {
		fmt.Println("连接服务端失败，err:", err)
		return
	}
	defer socket.Close()
	sendData := []byte("Hello server")
	_, err = socket.Write(sendData) // 发送数据
	if err != nil {
		fmt.Println("发送数据失败，err:", err)
		return
	}
	data := make([]byte, 4096)
	n, remoteAddr, err := socket.ReadFromUDP(data) // 接收数据
	if err != nil {
		fmt.Println("接收数据失败，err:", err)
		return
	}
	fmt.Printf("recv:%v addr:%v count:%v\n", string(data[:n]), remoteAddr, n)
}
```

<hr>

## HTTP请求

- `context` 
- 单元测试（给函数做单元测试）
- `pprof` 调试工具（go 语言内置工具）
  - 可以看到代码的 cpu 和运行时的一些信息
  - 能看到一些图表信息，如内存占用、cpu 占用等

### Http 服务端

Go语言内置的`net/http` 包提供了 HTTP 客户端和服务端的实现

#### HTTP 协议

​	超文本传输协议（HTTP，Hyper Text Transfer Protocol）是互联网上应用最为广泛的一种网络传输协议，所有的`www` 文件都必须遵守这个标准。设计 HTTP 最初的目的是为了提供一种发布和接收 HTML 页面的方法。

#### HTTP客户端

使用`net/http`包编写一个简单的发送HTTP请求的 Client 端，代码如下：

```go
package main

import "net/http"

func f1(w http.ResponseWriter, r *http.Request) {
	str := "hello 沙河！"
	w.Write([]byte(str))
}

func main() {
	http.HandleFunc("/posts/Go/15_socket/", f1)
	http.ListenAndServe("127.0.0.1:9090", nil)
}
```

也可以通过读取文件中的内容，然后进行显示

```go
func f1(w http.ResponseWriter, r *http.Request) {
	index, err := ioutil.ReadFile("./index.html")
	if err != nil {
		w.Write([]byte(fmt.Sprintf("%v", err)))
	}
	w.Write([]byte(index))
}

func main() {
	http.HandleFunc("/index", f1)
	http.HandleFunc("/home", f1)
	http.HandleFunc("/about", f1)
	http.ListenAndServe("127.0.0.1:9090", nil)
}
```

#### 自定义 Server

要管理服务端的行为，可以创建一个自定义的 Server：

```go
s := &http.Server{
	Addr:           ":8080",
	Handler:        myHandler,
	ReadTimeout:    10 * time.Second,
	WriteTimeout:   10 * time.Second,
	MaxHeaderBytes: 1 << 20,
}
log.Fatal(s.ListenAndServe())
```

#### 网站运行运行流程

![image-20200817175202590](images/image-20200817175202590.png)

- HTTP：超文本传输协议，规定了浏览器和网站服务器之间通信的规则
  - 规定了浏览器和网站服务器之间通信的规则

- HTML：超文本标记语言，学的就是标记的符号，标签
- CSS：层叠样式表，规定了HTML中标签的具体样式（颜色/背景/大小/位置/浮动...）
- JavaScript：一种跑在浏览器上的编程语言

### HTTP 客户端

可以使用 `http` 客户端，去请求 URL 地址，得到内容

```go
func main() {
	res, err := http.Get("http://127.0.0.1:9090/query?name=zansan&age=10")
	if err != nil {
		fmt.Println(err)
		return
	}

	// 从res中把服务端返回的数据读取出来
	b, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Println(err)
		return
	}
	fmt.Println(string(b))
}
```

对于GET请求，参数都放在URL上（query param），请求体上是没有数据的，可以通过以下方法来获取

```go
func f2(w http.ResponseWriter, r *http.Request) {
	fmt.Println(r.URL)
	fmt.Println(r.URL.Query()) // 识别URL中的参数
	queryParams := r.URL.Query()
	name := queryParams.Get("name")
	age := queryParams.Get("age")
	fmt.Println("传递来的name:", name)
	fmt.Println("传递来的age:", age)
	fmt.Println(r.Method)
	fmt.Println(ioutil.ReadAll(r.Body))
}

func main() {
	http.HandleFunc("/query", f2)
	http.ListenAndServe("127.0.0.1:9090", nil)
}
```

同时上述的 `url` 也支持中文的请求，如下所示

```go
res, err := http.Get("http://127.0.0.1:9090/query?name=张三&age=10")
```

或者可以使用更为复杂的请求方式，可以使用下面的方式

```go
data := url.Values{}
urlObj, _ := url.Parse("http://127.0.0.1:9090/query")
data.Set("name", "周林")
data.Set("age", "100")
// 对请求进行编码
queryStr := data.Encode()
urlObj.RawQuery = queryStr

req, err := http.NewRequest("GET", urlObj.String(), nil)
if err != nil {
    fmt.Println(err)
    return
}
fmt.Println(req)
```

#### 自定义 Client

```go
client := &http.Client{
	CheckRedirect: redirectPolicyFunc,
}
resp, err := client.Get("http://example.com")
// ...
req, err := http.NewRequest("GET", "http://example.com", nil)
// ...
req.Header.Add("If-None-Match", `W/"wyzzy"`)
resp, err := client.Do(req)
```

#### 自定义 Transport

要管理代理、TLS配置、keep-alive、压缩和其他设置，创建一个Transport：

```go
tr := &http.Transport{
	TLSClientConfig:    &tls.Config{RootCAs: pool},
	DisableCompression: true,
}
client := &http.Client{Transport: tr}
resp, err := client.Get("https://example.com")
```

`Client` 和 `Transport` 类型都可以安全的被多个 `goroutine` 同时使用。出于效率考虑，应该一次建立、尽量重用。







