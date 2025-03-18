# Golang





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



## 接收者方法语法糖

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





# Unicode

**判断字符是否为字母或数字**：

- `unicode.IsLetter(r rune) bool`：判断字符是否为字母。
- `unicode.IsDigit(r rune) bool`：判断字符是否为数字。

**转换字符为小写**：

- `unicode.ToLower(r rune) rune`：将字符转换为小写。

**`unicode.IsLetter(ch)`**：检查字符 `ch` 是否为字母。

**`unicode.IsDigit(ch)`**：检查字符 `ch` 是否为数字。

**`unicode.ToLower(ch)`**：将字符 `ch` 转换为小写。





# List

```go
func maxDepth(root *TreeNode) int {
    if root == nil {return 0}
    stack := list.New()
    res := 0
    stack.PushBack(root)
    for stack.Len() != 0 {
        levelSize := stack.Len()
        for i := 0; i < levelSize; i ++ {
            node := stack.Back()
            stack.Remove(node)
            tmp := node.Value.(*TreeNode)
            if tmp.Left != nil {
                stack.PushBack(tmp.Left)
            }
            if tmp.Right != nil {
                stack.PushBack(tmp.Right)
            }
        }
        res ++
    }
    return res
}
```

# 搜索二叉树 IF

```go
func isValidBST(root *TreeNode) bool {
    return validate(root, -1 << 63, 1 << 63 - 1)
}

func validate(node *TreeNode, lower, upper int) bool {
    if node == nil {
        return true
    }
    if node.Val <= lower || node.Val >= upper {
        return false
    }
    return validate(node.Left, lower, node.Val) && validate(node.Right, node.Val, upper)
}
```

# 对称二叉树

```go
/**
 * Definition for a binary tree node.
 * type TreeNode struct {
 *     Val   int
 *     Left  *TreeNode
 *     Right *TreeNode
 * }
 */

func isSymmetric(root *TreeNode) bool {
    if root == nil {
        return true
    }
    queue := []*TreeNode{root.Left, root.Right}

    for len(queue) > 0 {
        left := queue[0]
        right := queue[1]
        queue = queue[2:]
        if left == nil && right == nil {
            continue
        }
        if left == nil || right == nil {
            return false
        }
        if left.Val != right.Val {
            return false
        }
        queue = append(queue, left.Left, right.Right, left.Right, right.Left)
    }

    return true
}
```







# `strconv`

`strconv.Atoi(s string) (i int, err error)`

`strconv.Itoa(i int) string`



## Parse 系列

Parse类函数用于转换字符串为给定类型的值：ParseBool()、ParseFloat()、ParseInt()、ParseUint()

```go
// 返回字符串表示的bool值，它只接受值为1、0、t、f、T、F、true、false、True、False、TRUE、FALSE的字符串，否则返回错误

fmt.Println(strconv.ParseBool("1"))    // true
fmt.Println(strconv.ParseBool("t"))    // true
fmt.Println(strconv.ParseBool("T"))    // true
fmt.Println(strconv.ParseBool("true")) // true
fmt.Println(strconv.ParseBool("True")) // true
fmt.Println(strconv.ParseBool("TRUE")) // true
fmt.Println(strconv.ParseBool("0"))     // false
fmt.Println(strconv.ParseBool("f"))     // false
fmt.Println(strconv.ParseBool("F"))     // false
fmt.Println(strconv.ParseBool("false")) // false
fmt.Println(strconv.ParseBool("False")) // false
fmt.Println(strconv.ParseBool("FALSE")) // false

//转换成功
s1 := "123"
ps1,err := strconv.ParseInt(s1, 10, 8) // 指将s1转换为10进制数，8指的是转换结果最大值不超过int8，即127
if err != nil{
	fmt.Printf("err is %v\n", err)
	return
}else{
	fmt.Printf("类型： %T ，值： %d", ps1, ps1) //类型： int64 ，值： 123
}

//转换失败
s1 := "129" // 超过int8最大值127
ps1,err := strconv.ParseInt(s1, 10, 8) // 指将s1转换为10进制数，8指的是转换结果最大值不超过int8，即127
if err != nil{
	fmt.Printf("err is %v\n", err)
	return
}else{
	fmt.Printf("类型： %T ，值： %d", ps1, ps1) //err is strconv.ParseInt: parsing "129": value out of range
}

f := "1.2342332"
f1, err := strconv.ParseFloat(f, 64)
if err != nil {
  fmt.Printf("err is %v\n", err)
  return
} else {
  fmt.Printf("类型为：%T,值为：%f", f1, f1)
} //类型为：float64,值为：1.234230
```





