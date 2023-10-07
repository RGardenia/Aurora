# Go 并发编程

## 进程、线程以及并行、并发

### 进程

进程（Process）就是程序在操作系统中的一次执行过程，是系统进行资源分配和调度的基本单位，进程是一个动态概念，是程序在执行过程中分配和管理资源的基本单位，每一个进程都有一个自己的地址空间。一个进程至少有5种基本状态，它们是：初始态，执行态，等待状态，就绪状态，终止状态。

通俗的讲进程就是一个正在执行的程序。

### 线程

线程是进程的一个执行实例，是程序执行的最小单元，它是比进程更小的能独立运行的基本单位

一个进程可以创建多个线程，同一个进程中多个线程可以并发执行 ，一个线程要运行的话，至少有一个进程

### 并发和并行

并发：多个线程同时竞争一个位置，竞争到的才可以执行，每一个时间段只有一个线程在执行。

并行：多个线程可以同时执行，每一个时间段，可以有多个线程同时执行。

通俗的讲多线程程序在单核CPU上面运行就是并发，多线程程序在多核CUP上运行就是并行，如果线程数大于CPU核数，则多线程程序在多个CPU上面运行既有并行又有并发

## Go 中的并发安全和锁

下面这一段代码，在并发环境下进行操作，就会出现并发访问的问题

```go
var count = 0
var wg sync.WaitGroup

func test()  {
	count++
	fmt.Println("the count is : ", count)
	time.Sleep(time.Millisecond)
	wg.Done()
}
func main() {
	for i := 0; i < 20; i++ {
		wg.Add(1)
		go test()
	}
	time.Sleep(time.Second * 10)
}
```

### 互斥锁

​	互斥锁是传统并发编程中对共享资源进行访问控制的主要手段，它由标准库sync中的 `Mutex` 结构体类型表示。`sync.Mutex` 类型只有两个公开的指针方法，Lock 和 Unlock。Lock 锁定当前的共享资源，Unlock 进行解锁

```go
// 定义一个锁
var mutex sync.Mutex
// 加锁
mutex.Lock()
// 解锁
mutex.Unlock()
```

```go
var count = 0
var wg sync.WaitGroup
var mutex sync.Mutex

func test()  {
	// 加锁
	mutex.Lock()
	count++
	fmt.Println("the count is : ", count)
	time.Sleep(time.Millisecond)
	wg.Done()
	// 解锁
	mutex.Unlock()
}
func main() {
	for i := 0; i < 20; i++ {
		wg.Add(1)
		go test()
	}
	time.Sleep(time.Second * 10)
}
```

通过下面命令，`build` 的时候，可以查看是否具有竞争关系

```go
// 通过 -race 参数进行构建
go build -race main.go
// 运行插件
main.ext
```

### 读写互斥锁

​	互斥锁的本质是当一个 `goroutine` 访问的时候，其他 `goroutine` 都不能访问。这样在资源同步，避免竞争的同时也降低了程序的并发性能。程序由原来的并行执行变成了串行执行。

​	其实，当对一个不会变化的数据只做“读”操作的话，是不存在资源竞争的问题的。因为数据是不变的，不管怎么读取，多少 `goroutine` 同时读取，都是可以的。

​	所以问题不是出在“读”上，主要是修改，也就是“写”。修改的数据要同步，这样其他 `goroutine` 才可以感知到。所以真正的互斥应该是读取和修改、修改和修改之间，读和读是没有互斥操作的必要的。

​	因此，衍生出另外一种锁，叫做读写锁。

​	读写锁可以让多个读操作并发，同时读取，但是对于写操作是完全互斥的。也就是说，当一个 `goroutine` 进行写操作的时候，其他	 `goroutine` 既不能进行读操作，也不能进行写操作。

应用场景：适用于读多写少的场景下，也就是支持并发读，单个写	特点

- 读的 `goroutine` 来了获取的是读锁，后续的 `goroutine` 能读不能写
- 写的 `goroutine` 来了获取的是写锁，后续的 `goroutine` 不管是读还是写都要等待获取获取锁

```go
var rwLock sync.RWMutex
rwLock.RLock() // 获取读锁
rwLock.Runlock() // 释放锁

rwLock.Lock() // 获取写锁
rwLock.unlock() // 释放写锁
```

## 并行与并发

并发：同一时间段内执行多个任务（你在用微信和两个女朋友聊天）

并行：同一时刻执行多个任务（你和你朋友都在用微信和女朋友聊天）

​	Go 语言的并发通过 `goroutine` 实现。`goroutine` 类似于线程，属于用户态的线程，可以根据需要创建成千上万个 `goroutine` 并发工作。`goroutine` 是由 Go 语言的运行时（runtime）调度完成，而线程是由操作系统调度完成。
​	Go语言还提供 `channel` 在多个 `goroutine` 间进行通信。`goroutine` 和 `channel` 是Go语言秉承CSP（Communicating Sequential Process）并发模式的重要实现基础。

> 用户态：表示程序执行用户自己写的程序时
>
> 内核态：表示程序执行操作系统层面的程序时
>

## 协程（goroutine）以及主线程

​	Golang 中的主线程：（可以理解为线程/也可以理解为进程），在一个 Golang 程序的主线程上可以起多个协程。Golang 中多协程可以实现并行或者并发。

​	**协程**：可以理解为用户级线程，这是对内核透明的，也就是系统并不知道有协程的存在，是完全由用户自己的程序进行调度的。Golang的一大特色就是从语言层面原生持协程，在函数或者方法前面加go关键字就可创建一个协程。可以说 Golang 中的协程就是 `goroutine` 

### 多协程和多线程

多协程和多线程：Golang 中每个 `goroutine` （协程）默认占用内存远比Java、C的线程少。

OS线程（操作系统线程）一般都有固定的栈内存（通常为2MB左右），一个 `goroutine` （协程）占用内存非常小，只有2KB左右，多协程 `goroutine` 切换调度开销方面远比线程要少。

这也是为什么越来越多的大公司使用Golang的原因之一

## Goroutine

​	在`java/c++` 中要实现并发编程的时候，通常需要自己维护一个线程池，并且需要自己去包装一个又一个的任务，同时需要自己去调度线程执行任务并维护上下文切换，这一切通常会耗费程序员大量的心智。那么能不能有一种机制，程序员只需要定义很多个任务，让系统去帮助把这些任务分配到CPU上实现并发执行呢？

​	Go语言中的 `goroutine` 就是一种机制，`goroutine` 的概念类似于线程，但`goroutine` 是由 Go 的运行时（runtime）调度和管理的。Go程序会智能地将`goroutine` 中的任务合理分配给每个CPU，Go语言之所以被称为现代化的编程语言，就是因为它在语言层面已经内置了调度和上下文切换的机制。

​	在 Go 语言编程中不需要去自己写进程、线程、协程，技能包里只有一个技能 `goroutine` ，当需要让某个任务并发执行的时候，只需要把这个任务包装成一个函数、开启一个`goroutine` 去执行这个函数就可以了，就是这么简单粗暴。

### 使用 Goroutine

Go 语言中`goroutine` 非常简单，只需要在调用函数的时候，在前面加上 go 关键字，就可以为一个函数创建一个`goroutine` 

一个`goroutine` 必定对应一个函数，可以创建多个`goroutine` 去执行相同的函数

### 启动 goroutine

启动`goroutine` 的方式非常简单，只需要在调用的函数（普通函数和匿名函数）前面加上一个`go`关键字

举个栗子如下：

```go
func hello() {
	fmt.Println("Hello Goroutine!")
}
func main() {
	hello()
	fmt.Println("main goroutine done!")
}
```

当 `main()` 函数返回的时候该`goroutine`就结束了，所有在`main()`函数中启动的`goroutine`会一同结束，`main`函数所在的`goroutine`就像是权利的游戏中的夜王，其他的`goroutine`都是异鬼，夜王一死它转化的那些异鬼也就全部 GG 了

所以要想办法让 `main` 函数等一等 `hello` 函数，最简单粗暴的方式就是`time.Sleep`了

```go
func main() {
	go hello() // 启动另外一个 goroutine 去执行 hello 函数
	fmt.Println("main goroutine done!")
	time.Sleep(time.Second)
}
```

### 启动多个 goroutine

在 Go 语言中实现并发就是这样简单，我们还可以启动多个`goroutine`。再来一个例子:（这里使用了`sync.WaitGroup`来实现`goroutine` 的同步）

```go
var wg sync.WaitGroup

func hello(i int) {
	defer wg.Done() // goroutine 结束就登记-1
	fmt.Println("Hello Goroutine!", i)
}
func main() {
	for i := 0; i < 10; i++ {
		wg.Add(1) // 启动一个 goroutine 就登记+1
		go hello(i)
	}
	wg.Wait() // 等待所有登记的 goroutine 都结束
}
```

### goroutine 结束时间

​	`goroutine` 对应的函数结束了，`goroutine` 就结束了吗，也就是说当 `main` 函数执行结束，那么 `main` 函数对应的`goroutine` 也结束了。

## goroutine 与线程

### 可增长的栈

​	OS 线程（操作系统线程）一般都有固定的栈内存（通常为2MB）,一个`goroutine`的栈在其生命周期开始时只有很小的栈（典型情况下2KB），`goroutine`的栈不是固定的，它可以按需增大和缩小，`goroutine`的栈大小限制可以达到1GB，虽然极少会用到这么大。所以在Go语言中一次创建十万左右的`goroutine`也是可以的

### Goroutine 调度

`GPM`是Go语言运行时（runtime）层面的实现，是 go 语言自己实现的一套调度系统。区别于操作系统调度 OS 线程

- `G`很好理解，就是个`goroutine` 的，里面除了存放本`goroutine` 信息外 还有与所在P的绑定等信息。
- `P`管理着一组`goroutine` 队列，P里面会存储当前`goroutine` 运行的上下文环境（函数指针，堆栈地址及地址边界），P会对自己管理的`goroutine` 队列做一些调度（比如把占用CPU时间较长的`goroutine` 暂停、运行后续的`goroutine` 等等）当自己的队列消费完了就去全局队列里取，如果全局队列里也消费完了会去其他 P 的队列里抢任务
- `M（machine）`是Go运行时（runtime）对操作系统内核线程的虚拟， M与内核线程一般是一一映射的关系， 一个`goroutine` 最终是要放到M上执行的

​	单从线程调度讲，Go 语言相比起其他语言的优势在于 OS 线程是由 OS 内核来调度的，`goroutine`则是由Go运行时（runtime）自己的调度器调度的，这个调度器使用一个称为 `m:n` 调度的技术（复用/调度 `m` 个`goroutine` 到 n 个OS线程）

​	其一大特点是`goroutine` 的调度是在用户态下完成的， 不涉及内核态与用户态之间的频繁切换，包括内存的分配与释放，都是在用户态维护着一块大的内存池， 不直接调用系统的 `malloc` 函数（除非内存池需要改变），成本比调度 OS 线程低很多。 另一方面充分利用了多核的硬件资源，近似的把若干`goroutine` 均分在物理线程上， 再加上本身`goroutine` 的超轻量，以上种种保证了 go 调度方面的性能。

<hr>
## 等待组

`sync.waitgroup`，用来等 `goroutine` 执行完在继续，是一个结构体，值类型，给函数传参数的时候要传指针

```go
var wg sync.WaitGroup

wg.add(1) // 起几个 goroutine 就加几个数
wg.Done() // 在goroutine对应的函数中，函数要结束的时候调用，表示goroutine完成，计数器减1
wg.Wait() // 阻塞，等待所有的goroutine都结束
```

## sync.WaitGroup

> 等待协程

首先需要创建一个协程计数器

```go
// 定义一个协程计数器
var wg sync.WaitGroup
```

然后当开启协程的时候，要让计数器加 1

```go
// 开启协程，协程计数器加1
wg.Add(1)
go test2()
```

当协程结束前，需要让计数器减 1

```go
// 协程计数器减1
wg.Done()
```

完整代码如下

```go
// 定义一个协程计数器
var wg sync.WaitGroup

func test()  {
	// 这是主进程执行的
	for i := 0; i < 1000; i++ {
		fmt.Println("test1 你好golang", i)
		//time.Sleep(time.Millisecond * 100)
	}
	// 协程计数器减1
	wg.Done()
}

func test2()  {
	// 这是主进程执行的
	for i := 0; i < 1000; i++ {
		fmt.Println("test2 你好golang", i)
		//time.Sleep(time.Millisecond * 100)
	}
	// 协程计数器减1
	wg.Done()
}

func main() {

	// 通过go关键字，就可以直接开启一个协程
	wg.Add(1)
	go test()

	// 协程计数器加1
	wg.Add(1)
	go test2()

	// 这是主进程执行的
	for i := 0; i < 1000; i++ {
		fmt.Println("main 你好golang", i)
		//time.Sleep(time.Millisecond * 100)
	}
	// 等待所有的协程执行完毕
	wg.Wait()
	fmt.Println("主线程退出")
}
```

## Sync.Once

某些函数只需要执行 一次的时候，就可以使用 `sync.Once` 

比如 `blog` 加载图片那个例子

```go
var once sync.Once
once.Do() //接收一个没有参数也乜有返回值的函数，如有需要可以使用闭包
```

## sync.Map

使用场景：并发操作一个 `map` 的时候，内置的 `map` 不是并发安全的

而 `sync.map` 是一个开箱即用（不需要 `make` 初始化）的并发安全的 `map` 

```go
var syncMap sync.Map
syncMap[key] = value	//原生map
syncMap.Store(key, value)	// 存储
syncMap.LoadOrStore()	// 获取
syncMap.Delete()
syncMap.Range()
```

## 原子操作

Go 语言内置了一些针对内置的基本数据类型的一些并发安全的操作

```go
var i int64 = 10
atomic.AddInt64(&i, 1)
```







## 并行运行 占用的 cpu 数量

​	Go 运行时的调度器使用 `GOMAXPROCS` 参数来确定需要使用多少个OS线程来同时执行Go代码。默认值是机器上的CPU核心数。例如在一个8核心的机器上，调度器会把Go代码同时调度到8个OS线程上

​	Go 语言中可以通过 `runtime.GOMAXPROCS()` 函数设置当前程序并发时占用的CPU逻辑核心数

​	Go1.5 版本之前，默认使用的是单核心执行。Go1.5 版本之后，默认使用全部的CPU逻辑核心数

```go
func main() {
	// 获取cpu个数
	npmCpu := runtime.NumCPU()
	fmt.Println("cup的个数:", npmCpu)
	// 设置允许使用的CPU数量
	runtime.GOMAXPROCS(runtime.NumCPU() - 1)
}
```

## for 循环开启多个协程

类似于Java里面开启多个线程，同时执行

```go
func test(num int)  {
	for i := 0; i < 10; i++ {
		fmt.Printf("协程（%v）打印的第%v条数据 \n", num, i)
	}
	// 协程计数器减1
	vg.Done()
}

var vg sync.WaitGroup

func main() {
	for i := 0; i < 10; i++ {
		go test(i)
		vg.Add(1)
	}
	vg.Wait()
	fmt.Println("主线程退出")
}
```

因为协程会在主线程退出后就终止，所以还需要使用到  `sync.WaitGroup` 来控制主线程的终止

## Goroutine Recover 解决协程中出现的Panic

```go
func sayHello()  {
	for i := 0; i < 10; i++ {
		fmt.Println("hello")
	}
}
func errTest()  {
	// 捕获异常
	defer func() {
		if err := recover(); err != nil {
			fmt.Println("errTest发生错误")
		}
	}()
	var myMap map[int]string
	myMap[0] = "10"
}
func main {
    go sayHello()
    go errTest()
}
```

当出现问题的时候，还是按照原来的方法，通过defer func创建匿名自启动

```go
// 捕获异常
defer func() {
    if err := recover(); err != nil {
        fmt.Println("errTest发生错误")
    }
}()
```
