# Golang的数据类型

## 概述

Go 语言中数据类型分为：基本数据类型和复合数据类型

1. 基本数据类型有：

​	整型、浮点型、布尔型、字符串

2. 复合数据类型有：

​	数组、切片、结构体、函数、map、通道（channel）、接口等

## 整型

整型的类型有很多中，包括 int8，int16，int32，int64。我们可以根据具体的情况来进行定义

如果我们直接写 int 也是可以的，它在不同的操作系统中，int 的大小是不一样的

- 32位操作系统：int  -> int32
- 64位操作系统：int -> int64

![image-20200719084018801](images/image-20200719084018801.png)

> 可以通过 unsafe.Sizeof 查看不同长度的整型，在内存里面的存储空间
>
> ```
> var num2 = 12
> fmt.Println(unsafe.Sizeof(num2))
> ```

### 类型转换

通过在变量前面添加指定类型，就可以进行强制类型转换

```go
var a1 int16 = 10
var a2 int32 = 12
var a3 = int32(a1) + a2
fmt.Println(a3)
```

注意，高位转低位的时候，需要注意，会存在精度丢失，比如上述 16 转 8 位的时候

```go
var n1 int16 = 130
fmt.Println(int8(n1)) // 变成 -126
```

### 数字字面量语法

Go1.13 版本之后，引入了数字字面量语法，这样便于开发者以二进制、八进制或十六进制浮点数的格式定义数字，例如：

```go
v := 0b00101101  // 代表二进制的101101
v：= Oo377       // 代表八进制的377
```

### 进制转换

```go
var number = 17
// 原样输出
fmt.Printf("%v\n", number)
// 十进制输出
fmt.Printf("%d\n", number)
// 以八进制输出
fmt.Printf("%o\n", number)
// 以二进制输出
fmt.Printf("%b\n", number)
// 以十六进制输出
fmt.Printf("%x\n", number)
```

## 浮点型

Go 语言支持两种浮点型数：float32 和 float64。这两种浮点型数据格式遵循 IEEE754 标准：

float32 的浮点数的最大范围约为 3.4e38，可以使用常量定义：math.MaxFloat32。float64 的浮点数的最大范围约为 1.8e308，可以使用一个常量定义：math.MaxFloat64

打印浮点数时，可以使用 fmt 包配合动词 `%f`，代码如下：

```go
var pi = math.Pi
// 打印浮点类型，默认小数点6位
fmt.Printf("%f\n", pi)
// 打印浮点类型，打印小数点后2位
fmt.Printf("%.2f\n", pi)
```

### Golang中精度丢失的问题

几乎所有的编程语言都有精度丢失的问题，这是典型的二进制浮点数精度损失问题，在定长条件下，二进制小数和十进制小数互转可能有精度丢失

```go
d := 1129.6
fmt.Println(d*100) //输出112959.99999999
```

解决方法，使用第三方包来解决精度损失的问题	http://github.com/shopspring/decimal

## 布尔类型

```go
var fl = false
if f1 {
    fmt.Println("true")
} else {
    fmt.Println("false")
}
```

## 字符串类型

Go 语言中的字符串以原生数据类型出现，使用字符串就像使用其他原生数据类型（int、bool、float32、float64等）一样。Go 语言里的字符串的内部实现使用`UTF-8`编码。字符串的值为双引号（"）中的内容，可以在Go语言的源码中直接添加非 `ASCll` 码字符，例如：

```go
s1 := "hello"
s1 := "你好"
```

如果想要定义多行字符串，可以使用反引号

```go
var str = `第一行
           第二行`
fmt.Println(str)
```

### 字符串常见操作

- len(str)：长度
- `+` 或 fmt.Sprintf：拼接字符串
- strings.Split：分割
- strings.contains：判断是否包含
- strings.HasPrefix      strings.HasSuffix：前缀/后缀判断
- strings.Index()      strings.LastIndex()：子串出现的位置
- strings.Join()：join操作
- strings.Index()：判断在字符串中的位置

## byte 和 rune 类型

组成每个字符串的元素叫做 “字符”，可以通过遍历字符串元素获得字符。字符用单引号 '' 包裹起来

Go语言中的字符有以下两种类型

- uint8 类型：或者叫 byte 型，代表了 ACII 码的一个字符
- rune 类型：代表一个 UTF-8 字符

当需要处理中文，日文或者其他复合字符时，则需要用到 rune 类型，rune 类型实际上是一个 int32

Go使用了特殊的 rune 类型来处理 Unicode，让基于 Unicode 的文本处理更为方便，也可以使用 byte 型进行默认字符串处理，性能和扩展性都有照顾。

需要注意的是，在go语言中，一个汉字占用 3个字节（utf-8），一个字母占用 1 个字节

```go
package main
import "fmt"

func main() {
	var a byte = 'a'
	// 输出的是ASCII码值，也就是说当我们直接输出 byte（字符）的时候，输出的是这个字符对应的码值
	fmt.Println(a)	// 97
	// 输出的是字符
	fmt.Printf("%c", a)	// a

	// for循环打印字符串里面的字符
	// 通过 len 来循环的，相当于打印的是 ASCII码
	s := "你好 golang"
	for i := 0; i < len(s); i++ {
		fmt.Printf("%v(%c)\t", s[i], s[i])	
        // 228(ä)  189(½)  160( )  229(å)  165(¥)  189(½)  32( )   103(g)  111(o)  108(l) 97(a)    110(n)  103(g)
	}

	// 通过 rune 打印的是 utf-8 字符
	for index, v := range s {
		fmt.Println(index, v)	// index 0、3、6~12 共 13 个字节
	}
}
```

### 修改字符串

修改字符串，需要先将其转换成`[]rune` 或 `[]byte` 类型，完成后在转换成`string` ，无论哪种转换都会重新分配内存，并复制字节数组

转换为 `[]byte`  类型

```go
// 字符串转换
s1 := "big"
byteS1 := []byte(s1)
byteS1[0] = 'p'
fmt.Println(string(byteS1))	// pig
```

转换为rune类型

```go
// rune类型
s2 := "你好golang"
byteS2 := []rune(s2)
byteS2[0] = '我'
fmt.Println(string(byteS2))	// 我好golang
```

## 基本数据类型转换

### 数值类型转换

```go
// 整型和浮点型之间转换
var aa int8 = 20
var bb int16 = 40
fmt.Println(int16(aa) + bb)	// 60

// 建议整型转换成浮点型
var cc int8 = 20
var dd float32 = 40
fmt.Println(float32(cc) + dd)	// 60
```

### 转换成字符串类型

第一种方式，就是通过 `fmt.Sprintf()` 来转换

```go
// 字符串类型转换
var i int = 20
var f float64 = 12.456
var t bool = true
var b byte = 'a'
str1 := fmt.Sprintf("%d", i)
fmt.Printf("类型：%v-%T \n", str1, str1)	// 类型：20-string

str2 := fmt.Sprintf("%f", f)
fmt.Printf("类型：%v-%T \n", str2, str2)	// 类型：12.456000-string

str3 := fmt.Sprintf("%t", t)
fmt.Printf("类型：%v-%T \n", str3, str3)	// 类型：true-string

str4 := fmt.Sprintf("%c", b)
fmt.Printf("类型：%v-%T \n", str4, str4)	// 类型：a-string
```

第二种方法就是通过 `strconv` 包里面的集中转换方法进行转换

```go
// int 类型转换 str 类型
var num1 int64 = 20
s1 := strconv.FormatInt(num1, 10)	// 10: 表示十进制
fmt.Printf("转换：%v - %T", s1, s1)	// 转换：20 - string

// float类型转换成string类型
var num2 float64 = 3.1415926

/*
    参数1：要转换的值
    参数2：格式化类型 'f'表示float，'b'表示二进制，‘e’表示 十进制
    参数3：表示保留的小数点，-1表示不对小数点格式化
    参数4：格式化的类型，传入64位 或者 32位
 */
s2 := strconv.FormatFloat(num2, 'f', -1, 64)
fmt.Printf("转换：%v-%T", s2, s2)	// 转换：3.1415926-string
```

### 字符串转换成 int 和 float 类型

```go
str := "10"
// 第一个参数：需要转换的数，第二个参数：进制， 参数三：32位或64位
num,_ = strconv.ParseInt(str, 10, 64)

// 转换成float类型
str2 := "3.141592654"
num,_ = strconv.ParseFloat(str2, 10)
```

