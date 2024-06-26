# Go的数组

## Array数组介绍

​	数组是指一系列同一类型数据的集合。数组中包含的每个数据被称为数组元素（element），这种类型可以是意的原始类型，比如int、string 等，也可以是用户自定义的类型。一个数组包含的元素个数被称为数组的长度。在 Golang 中数组是一个长度固定的数据类型，数组的长度是类型的一部分，也就是说 `[5]int` 和 `[10]int` 是两个不同的类型。Golang 中数组的另一个特点是占用内存的连续性，也就是说数组中的元素是被分配到连续的内存地址中的，因而索引数组元素的速度非常快。

​	和数组对应的类型是 Slice（切片），Slice 是可以增长和收缩的动态序列，功能也更灵活，但是想要理解 `slice` 工作原理的话需要先理解数组，所以本节主要为大家讲解数组的使用。

## 数组定义

```go
var 数组变量名 [元素数量] T
```

```go
// 数组的长度是类型的一部分
var arr1 [3]int
var arr2 [4]string
fmt.Printf("%T, %T \n", arr1, arr2)

// 数组的初始化 第一种方法
var arr3 [3]int
arr3[0] = 1
arr3[1] = 2
arr3[2] = 3
fmt.Println(arr3)

// 第二种初始化数组的方法
var arr4 = [4]int {10, 20, 30, 40}
fmt.Println(arr4)

// 第三种数组初始化方法，自动推断数组长度
var arr5 = [...]int{1, 2}
fmt.Println(arr5)

// 第四种初始化数组的方法，指定下标
a := [...]int{1:1, 3:5}
fmt.Println(a)	// [0 1 0 5]
```

## 遍历数组

```go
a := [...]int{1:1, 3:5}

for i := 0; i < len(a); i++ {
	fmt.Print(a[i], " ")
}

for _, value := range a {
    fmt.Print(value, " ")
}
```

## 数组的值类型

数组是值类型，赋值和传参会赋值整个数组，因此改变副本的值，不会改变本身的值

```go
var array1 = [...]int{1, 2, 3}
array2 := array1
array2[0] = 3
fmt.Println(array1, array2)
```

例如上述的代码，将数组进行赋值后，该改变数组中的值时，发现结果如下

```GO
[1 2 3] [3 2 3]
```

这就说明了，golang 中的数组是值类型，而不是和 java 一样属于引用数据类型

## 切片定义(引用类型)

在 golang 中，切片的定义和数组定义是相似的，但是需要注意的是，切片是引用数据类型

```go
// 切片定义
var array3 = []int{1,2,3}
array4 := array3
array4[0] = 3
fmt.Println(array3, array4)
```

切片定义时，**方括号内是空的**，通过改变第一个切片元素，然后查看最后的效果

```GO
[3 2 3] [3 2 3]
```

## 二维数组

Go 语言支持多维数组，这里以二维数组为例（数组中又嵌套数组）：

```go
var 数组变量名 [元素数量][元素数量] T
```

```go
// 二维数组
var array5 = [2][2]int{{1,2},{2,3}}
fmt.Println(array5)
```

### 多维数组遍历

```go
// 二维数组
var array5 = [2][2]int{{1,2},{2,3}}
for i := 0; i < len(array5); i++ {
    for j := 0; j < len(array5[0]); j++ {
        fmt.Println(array5[i][j])
    }
}

for _, item := range array5 {
    for _, item2 := range item {
        fmt.Println(item2)
    }
}
```

### 类型推导

另外在进行数组的创建的时候，还可以使用类型推导，但是只能使用一个  **...**

```go
// 二维数组（正确写法）
var array5 = [...][2]int{{1,2},{2,3}}
```

```go
// 二维数组（错误写法）
var array5 = [2][...]int{{1,2},{2,3}}
```
