## Golang





## Install

**Windows**

```bash
go env -w GO111MODULE=on
go env -w GOPROXY=https://goproxy.cn,direct

```

**Linux**

```bash
# 下载
wget https://studygolang.com/dl/golang/go1.20.6.linux-amd64.tar.gz
gunzip go1.20.6.linux-amd64.tar.gz
tar -xvf go1.20.6.linux-amd64.tar
# 配置系统变量
mv go /usr/local/
echo 'export PATH=$PATH:/usr/local/go/bin' >> /etc/profile
source /etc/profile

# 查看版本   正常显示则表明安装正常
go --version
```







# Golang 语法糖



## 1. 接收者方法语法糖

Go 语言的指针 (Pointer)，大致上理解如下：

- **变量名**前的 & 符号，是取变量的内存地址，不是取值；
- **数据类型**前的 * 符号，代表要储存的是对应数据类型的内存地址，不是存值；
- **变量名前**的 * 符号，代表从内存地址中取值 (Dereferencing)。

在 Go 中，对于自定义类型 T，为它定义方法时，其接收者可以是类型 T 本身，也可能是 T 类型的指针 *T

```go
func (ins *Instance) Foo() string {
  return ""
```

定义了 Instance 的 Foo 方法时，其接收者是一个指针类型（*Instance）

```go
var _ = Instance{}.Foo() 
// 编译错误：cannot call pointer method on Instance{} ，变量是不可变的（该变量没有地址，不能对其进行寻址操作）
```

用 Instance 类型本身 `Instance{}` 值去调用 Foo 方法，将会得到以上错误

```go
func (ins Instance) Foo() string {
 return ""
}

func main() {
  var _ = Instance{}.Foo() // 编译通过 注：值接收者方法执行完后，修改无效
}
```

将 Foo 方法的接收者改为 Instance 类型，就没有问题

定义类型 T 的函数方法时，其接收者类型决定了之后什么样的类型对象能去调用该函数方法

```go
func (ins *Instance) String() string {
 return ""
}

func main() {
  var ins Instance
  _ = ins.String() // 编译器会自动获取 ins 的地址并将其转换为指向 Instance 类型的指针 _ = (&ins).String()
}
```

实际上，即使是在实现 Foo 方法时的接收者是指针类型，上面 ins 调用的使用依然没有问题

Ins 值属于 Instance 类型，而非 *Instance，却能调用 Foo 方法，这其实就是 Go 编译器提供的语法糖！

当一个变量可变时（也就是说，该变量是一个具有地址的变量，可以对其进行寻址操作），对类型 T 的变量直接调用 *T 方法是合法的，因为 Go 编译器隐式地获取了它的地址。变量可变意味着变量可寻址，因此，上文提到的 Instance{}.Foo() 会得到编译错误，就在于 Instance{} 值不能寻址。

> 在 Go 中，即使变量没有被显式初始化，编译器仍会为其分配内存空间，因此变量仍然具有内存地址。不过，由于变量没有被初始化，它们在分配后仅被赋予其类型的默认零值，而不是初始值。当然，这些默认值也是存储在变量分配的内存空间中的。
>
> 例如，下面的代码定义了一个整型变量 `x`，它没有被显式初始化，但是在分配内存时仍然具有一个地址：
> `var x int fmt.Printf("%p\n", &x)`  // 输出变量 x 的内存地址
> 输出结果类似于：`0xc0000120a0`，表明变量 `x` 的内存地址已经被分配了。但是由于变量没有被初始化，`x` 的值将为整型的默认值 `0`

假设 `T` 类型的方法上接收器既有 `T` 类型的，又有 `*T` 指针类型的，那么就不可以在不能寻址的 `T` 值上调用 `*T` 接收器的方法

- `&B{}` 是指针，可寻址
- `B{}` 是值，不可寻址
- `b := B{}` b是变量，可寻址

