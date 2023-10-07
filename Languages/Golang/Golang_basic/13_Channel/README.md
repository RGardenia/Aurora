# Channel	管道

​	管道是 Golang 在语言级别上提供的 `goroutine` 间的通讯方式，可以使用 `channel` 在多个 `goroutine` 之间传递消息。如果说 `goroutine` 是Go程序并发的执行体，`channel` 就是它们之间的连接。`channel` 是可以让一个 `goroutine` 发送特定值到另一个 `goroutine` 的通信机制。

​	Golang 的并发模型是CSP（Communicating Sequential Processes），提倡通过通信共享内存而不是通过共享内存而实现通信。

​	Go语言中的管道（channel）是一种特殊的类型。管道像一个传送带或者队列，总是遵循先入先出（First In First Out）的规则，保证收发数据的顺序。每一个管道都是一个具体类型的导管，也就是声明`channel` 的时候需要为其指定元素类型。

### channel 类型

channel 是一种类型，一种引用类型。声明管道类型的格式如下：

```go
// 声明一个传递整型的管道
var ch1 chan int
// 声明一个传递布尔类型的管道
var ch2 chan bool
// 声明一个传递int切片的管道
var ch3 chan []int
```

### 创建 channel

声明管道后，需要使用 `make` 函数初始化之后才能使用

```go
make(chan 元素类型, 容量)
```

```go
// 创建一个能存储10个int类型的数据管道
ch1 = make(chan int, 10)
// 创建一个能存储4个bool类型的数据管道
ch2 = make(chan bool, 4)
// 创建一个能存储3个[]int切片类型的管道
ch3 = make(chan []int, 3)
```

### channel 操作

管道有发送，接收和关闭的三个功能

发送和接收 都使用  `<-`  符号

现在先使用以下语句定义一个管道：

```go
ch := make(chan int, 3)
```

#### 发送

将数据放到管道内，将一个值发送到管道内

```go
// 把 10 发送到ch中
ch <- 10
```

#### 取操作

```go
x := <- ch
```

#### 关闭管道

通过调用内置的 `close` 函数来关闭管道

```go
close(ch)
```

```go
// 创建管道
ch := make(chan int, 3)

// 给管道里面存储数据
ch <- 10
ch <- 21
ch <- 32

// 获取管道里面的内容
a := <- ch
fmt.Println("打印出管道的值：", a)
fmt.Println("打印出管道的值：", <- ch)
fmt.Println("打印出管道的值：", <- ch)

// 管道的值、容量、长度
fmt.Printf("地址：%v 容量：%v 长度：%v \n", ch, cap(ch), len(ch))

// 管道的类型
fmt.Printf("%T \n", ch)

// 管道阻塞（当没有数据的时候取，会出现阻塞，同时当管道满了，继续存也会）
<- ch  // 没有数据取，出现阻塞
ch <- 10
ch <- 10
ch <- 10
ch <- 10 // 管道满了，继续存，也出现阻塞
```

## for range 从管道循环取值

​	当向管道中发送完数据时，可以通过 `close` 函数来关闭管道，当管道被关闭时，再往该管道发送值会引发 `panic` ，从该管道取值的操作会去完管道中的值，再然后取到的值一直都是对应类型的零值。

```go
// 创建管道
ch := make(chan int, 10)
// 循环写入值
for i := 0; i < 10; i++ {
    ch <- i
}
// 关闭管道
close(ch)

// for range循环遍历管道的值(管道没有key)
for value := range ch {
    fmt.Println(value)
}
// 通过上述的操作，能够打印值，但是出出现一个deadlock的死锁错误，也就说需要关闭管道
```

注意：使用 `for range` 遍历的时候，一定在之前需要先关闭管道，通过 `for i` 的循环方式，可以不关闭管道

## Goroutine 结合 channel 管道

需求1：定义两个方法，一个方法给管道里面写数据，一个给管道里面读取数据。要求同步进行

- 开启一个 fn1 的的协程给向管道 `inChan` 中写入 10 条数据
- 开启一个 fn2 的协程读取 `inChan` 中写入的数据
- 注意：fn1 和 fn2 同时操作一个管道
- 主线程必须等待操作完成后才可以退出

```go
func write(ch chan int)  {
	for i := 0; i < 10; i++ {
		fmt.Println("写入:", i)
		ch <- i
		time.Sleep(time.Microsecond * 10)
	}
	wg.Done()
}
func read(ch chan int)  {
	for i := 0; i < 10; i++ {
		fmt.Println("读取:", <- ch)
		time.Sleep(time.Microsecond * 10)
	}
	wg.Done()
}
var wg sync.WaitGroup
func main() {
	ch := make(chan int, 10)
	wg.Add(1)
	go write(ch)
	wg.Add(1)
	go read(ch)

	// 等待
	wg.Wait()
	fmt.Println("主线程执行完毕")
}
```

管道是安全的，是一边写入，一边读取，当读取比较快的时候，会等待写入

## goroutine 结合 channel 打印素数

![image-20200723214241459](images/image-20200723214241459.png)

```go
// 想 intChan 中放入 1~ 120000 个数
func putNum(intChan chan int)  {
	for i := 2; i < 120000; i++ {
		intChan <- i
	}
	wg.Done()
	close(intChan)
}

// cong intChan 取出数据，并判断是否为素数，如果是的话，就把得到的素数放到primeChan中
func primeNum(intChan chan int, primeChan chan int, exitChan chan bool)  {
	for value := range intChan {
		var flag = true
		for i := 2; i <= int(math.Sqrt(float64(value))); i++ {
			if  i % i == 0 {
				flag = false
				break
			}
		}
		if flag {
			// 是素数
			primeChan <- value
			break
		}
	}
	// 这里需要关闭 primeChan，因为后面需要遍历输出 primeChan
	exitChan <- true
	wg.Done()
}

// 打印素数
func printPrime(primeChan chan int)  {
	for value := range primeChan {
		fmt.Println(value)
	}
	wg.Done()
}

var wg sync.WaitGroup
func main() {
	// 写入数字
	intChan := make(chan int, 1000)

	// 存放素数
	primeChan := make(chan int, 1000)

	// 存放 primeChan 退出状态
	exitChan := make(chan bool, 16)

	// 开启写值的协程
	go putNum(intChan)

	// 开启计算素数的协程
	for i := 0; i < 10; i++ {
		wg.Add(1)
		go primeNum(intChan, primeChan, exitChan)
	}

	// 开启打印的协程
	wg.Add(1)
	go printPrime(primeChan)

	// 匿名自运行函数
	wg.Add(1)
	go func() {
		for i := 0; i < 16; i++ {
			// 如果 exitChan 没有完成16次遍历，将会等待
			<- exitChan
		}
		// 关闭 primeChan
		close(primeChan)
		wg.Done()
	}()

	wg.Wait()
	fmt.Println("主线程执行完毕")
}
```

## 单向管道

​	有时候会将管道作为参数在多个任务函数间传递，很多时候在不同的任务函数中，使用管道都会对其进行限制，比如限制管道在函数中只能发送或者只能接受

> 默认的管道是 可读可写

```go
// 定义一种可读可写的管道
var ch = make(chan int, 2)
ch <- 10
<- ch

// 管道声明为 只写管道，只能够写入，不能读
var ch2 = make(chan <- int, 2)
ch2 <- 10

// 声明一个只读管道
var ch3 = make(<- chan int, 2)
<- ch3
```

## Select 多路复用

​	在某些场景下需要同时从多个通道接收数据。这个时候就可以用到 golang 中给提供的 `select` 多路复用，通常情况通道在接收数据时，如果没有数据可以接收将会发生阻塞

​	为了应对这种场景，Go内置了 `select` 关键字，可以同时响应多个管道的操作。

​	`select` 的使用类似于 `switch` 语句，它有一系列 `case` 分支和一个默认的分支。每个 `case` 会对应一个管道的通信（接收或发送）过程。`select` 会一直等待，直到某个 `case` 的通信操作完成时，就会执行 `case` 分支对应的语句。具体格式如下：

```go
intChan := make(chan int, 10)
intChan <- 10
intChan <- 12
intChan <- 13
stringChan := make(chan int, 10)
stringChan <- 20
stringChan <- 23
stringChan <- 24

// 每次循环的时候，会随机中一个 chan 中读取，其中 for 是死循环
for {
    select {
        case v:= <- intChan:
        	fmt.Println("从initChan中读取数据：", v)
        case v:= <- stringChan:
        	fmt.Println("从stringChan中读取数据：", v)
        default:
        	fmt.Println("所有的数据获取完毕")
        	return
    }
}
```

> tip：使用select来获取数据的时候，不需要关闭chan，不然会出现问题
