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





# 位运算

包含若干整数的集合 $S = \{0, 2, 3\}$。在编程中，通常用哈希表（hash table）表示集合。例如 Java 中的 HashSet，C++ 中的 std::unordered_set。

在集合论中，有交集 $\cap$、并集 $\cup$、包含于 $\subseteq$ 等等概念。如果编程实现「求两个哈希表的交集」，需要一个一个地遍历哈希表中的元素。

集合可以用二进制表示，二进制从低到高第 $i$ 位为 $1$ 表示 $i$ 在集合中，为 $0$ 表示 $i$ 不在集合中。例如集合 $\{0, 2, 3\}$ 可以用二进制数 $1101_2$ 表示；反过来，二进制数 $1101_2$ 就对应着集合 $\{0, 2, 3\}$。

正式地说，包含非负整数的集合 $S$ 可以用如下方式「压缩」成一个数字：

$$
f(S) = \sum_{i \in S} 2^i
$$

例如集合 $\{0, 2, 3\}$ 可以压缩成 $2^0 + 2^2 + 2^3 = 13$，也就是二进制数 $1101_2$。

利用位运算「并行计算」的特点，我们可以高效地做一些和集合有关的运算。按照常见的应用场景，可以分为以下四类：

1. **集合与集合**
2. **集合与元素**
3. **遍历集合**
4. **枚举集合**

### 一、集合与集合

其中 $\&$ 表示按位与，$|$ 表示按位或，$\oplus$ 表示按位异或，$\sim$ 表示按位取反。

两个集合的「对称差」是只属于其中一个集合，而不属于另一个集合的元素组成的集合，也就是不在交集中的元素组成的集合。

| 术语       | 集合                           | 位运算                        | 集合示例                                        | 位运算示例                                                  |
| ---------- | ------------------------------ | ----------------------------- | ----------------------------------------------- | ----------------------------------------------------------- |
| 交集       | $A \cap B$                     | $a \& b$                      | $\{0, 2, 3\} \cap \{0, 1, 2\} = \{0, 2\}$       | $1101_2 \& 0111_2 = 0101_2$                                 |
| 并集       | $A \cup B$                     | $a | b$                       | $\{0, 2, 3\} \cup \{0, 1, 2\} = \{0, 1, 2, 3\}$ | $1101_2 | 0111_2 = 1111_2$                                  |
| 对称差     | $A \Delta B$                   | $a \oplus b$                  | $\{0, 2, 3\} \Delta \{0, 1, 2\} = \{1, 3\}$     | $1101_2 \oplus 0111_2 = 1010_2$                             |
| 差         | $A \setminus B$                | $a \& \sim b$                 | $\{0, 2, 3\} \setminus \{1, 2\} = \{0, 3\}$     | $1101_2 \& 1001_2 = 1001_2$                                 |
| 差（子集） | $A \setminus B, B \subseteq A$ | $a \oplus b$                  | $\{0, 2, 3\} \setminus \{0, 2\} = \{3\}$        | $1101_2 \oplus 0101_2 = 1000_2$                             |
| 包含于     | $A \subseteq B$                | $a \& b = a$<br />$a | b = b$ | $\{0, 2\} \subseteq \{0, 2, 3\}$                | $0101_2 \& 1101_2 = 0101_2$<br />$0101_2 | 1101_2 = 1101_2$ |

注 1：按位取反的例子中，仅列出最低 $4$ 个比特位取反后的结果，即 $0110_2$ 取反后是 $1001_2$。

注 2：包含于（判断子集）的两种位运算写法是等价的，在编程时只需判断其中任意一种。此外，还可以用 $(a \& \sim b) == 0$ 判断，如果成立，也表示 $A$ 是 $B$ 的子集。

注 3：编程时，请注意运算符的优先级。例如 $==$ 在某些语言中优先级比位运算更高。

### 二、集合与元素

通常会用到移位运算，其中 $<<$ 表示左移，$>>$ 表示右移

注：左移 $i$ 位相当于乘以 $2^i$，右移 $i$ 位相当于除以 $2^i$

| 术语                     | 集合                              | 位运算                    | 集合示例                                        | 位运算示例                      |
| ------------------------ | --------------------------------- | ------------------------- | ----------------------------------------------- | ------------------------------- |
| 空集                     | $\emptyset$                       | $0$                       |                                                 |                                 |
| 单元素集合               | $\{i\}$                           | $1 << i$                  | $\{2\}$                                         | $1 << 2$                        |
| 全集                     | $U = \{0, 1, 2, \dots, n-1\}$     | $(1 << n) - 1$            | $\{0, 1, 2, 3\}$                                | $(1 << 4) - 1$                  |
| 补集                     | $\complement_U S = U \setminus S$ | $((1 << n) - 1) \oplus s$ | $U = \{0, 1, 2, 3\}$, $C_U \{1, 2\} = \{0, 3\}$ | $1111_2 \oplus 0110_2 = 1001_2$ |
| 属于                     | $i \in S$                         | $(s >> i) \& 1 = 1$       | $2 \in \{0, 2, 3\}$                             | $(1101_2 >> 2) \& 1 = 1$        |
| 不属于                   | $i \notin S$                      | $(s >> i) \& 1 = 0$       | $1 \notin \{0, 2, 3\}$                          | $(1101_2 >> 1) \& 1 = 0$        |
| 添加元素                 | $S \cup \{i\}$                    | $s | (1 << i)$            | $\{0, 3\} \cup \{2\}$                           | $1001_2 | (1 << 2)$             |
| 删除元素                 | $S \setminus \{i\}$               | $s \& \sim(1 << i)$       | $\{0, 2, 3\} \setminus \{2\}$                   | $1101_2 \& \sim(1 << 2)$        |
| 删除元素（一定在集合中） | $S \setminus \{i\}, i \in S$      | $s \oplus (1 << i)$       | $\{0, 2, 3\} \setminus \{2\}$                   | $1101_2 \oplus (1 << 2)$        |
| 删除最小元素             |                                   | $s \& (s - 1)$            |                                                 | 见下                            |

$s = 101100_2$
$s-1 = 101011_2$
$s \& (s - 1) = 101000_2$

特别地，如果 $s$ 是 $2$ 的幂，那么 $s \& (s - 1) = 0$

此外，编程语言提供了一些和二进制有关的库函数，例如：

- 计算二进制中的 $1$ 的个数，也就是集合大小；
- 计算二进制长度，减一后得到集合最大元素；
- 计算二进制尾零个数，也就是集合最小元素。

调用这些函数的时间复杂度都是 $O(1)$

| 术语         | Python                        | Java                                   | C++                        | Go                      |
| ------------ | ----------------------------- | -------------------------------------- | -------------------------- | ----------------------- |
| 集合大小     | $s.bit\_count()$              | $Integer.bitCount(s)$                  | $\_\_builtin\_popcount(s)$ | $bits.OnesCount(s)$     |
| 二进制长度   | $s.bit\_length()$             | $32 - Integer.numberOfLeadingZeros(s)$ | $\_\_lg(s) + 1$            | $bits.Len(s)$           |
| 集合最大元素 | $s.bit\_length() - 1$         | $31 - Integer.numberOfLeadingZeros(s)$ | $\_\_lg(s)$                | $bits.Len(s) - 1$       |
| 集合最小元素 | $(s \& -s).bit\_length() - 1$ | $Integer.numberOfTrailingZeros(s)$     | $\_\_builtin\_ctz(s)$      | $bits.TrailingZeros(s)$ |

请特别注意 $s = 0$ 的情况。对于 C++ 来说，$\_\_lg(0)$ 和 $\_\_builtin\_ctz(0)$ 是未定义行为。其他语言请查阅 API 文档。

此外，对于 C++ 的 long long，需使用相应的 $\_\_builtin\_popcountll$ 等函数，即函数名后缀添加 `ll`（两个小写字母 L）。$\_\_lg$ 支持 long long。

特别地，只包含最小元素的子集，即二进制最低 $1$ 及其后面的 $0$，也叫 $lowbit$，可以用 $s \& -s$ 算出。举例说明：

$$
s = 101100_2 \\ \sim s = 010011_2 \\ (\sim s) + 1 = 010100_2 \\ \quad s \& -s = 000100_2
$$

### 三、遍历集合

设元素范围从 $0$ 到 $n-1$，枚举范围中的元素 $i$，判断 $i$ 是否在集合 $s$ 中。

```python
for i in range(n):
    if (s >> i) & 1:  # i 在 s 中
        # 处理 i 的逻辑
```

也可以直接遍历集合 $s$ 中的元素：不断地计算集合最小元素、去掉最小元素，直到集合为空。

```go
for t := uint(s); t > 0; t &= t - 1 {
    i := bits.TrailingZeros(t)
    // 处理 i 的逻辑
}
```

### 四、枚举集合

#### § 4.1 枚举所有集合

设元素范围从 $0$ 到 $n-1$，从空集 $\emptyset$ 枚举到全集 $U$：

```go
for s := 0; s < 1 << n; s++ {
    // 处理 s 的逻辑
}
```

#### § 4.2 枚举非空子集

设集合为 $s$，从大到小枚举 $s$ 的所有非空子集 $sub$：

```go
for sub := s; sub > 0; sub = (sub - 1) & s {
    // 处理 sub 的逻辑
}
```

暴力做法是从 $s$ 出发，不断减一，直到 $0$。但这样做，中途会遇到很多并不是 $s$ 的子集的情况。例如 $s = 10101$ 时，减一得到 $10100$，这是 $s$ 的子集。但再减一就得到 $10011$，这并不是 $s$ 的子集，下一个子集应该是 $10001$。

把所有的合法子集按顺序列出来，会发现我们做的相当于「压缩版」的二进制减法，例如：

$$
10101 \rightarrow 10100 \rightarrow 10001 \rightarrow 10000 \rightarrow 00101 \rightarrow \dots
$$

如果忽略掉 $10101$ 中的两个 $0$，数字的变化和二进制减法是一样的，即：

$$
111 \rightarrow 110 \rightarrow 101 \rightarrow 100 \rightarrow 011 \rightarrow \dots
$$

如，怎么从 $10100$ 跳到 $10001$？

- 普通的二进制减法是 $10100 - 1 = 10011$，也就是把最低位的 $1$ 变成 $0$，同时把最低位的 $1$ 右边的 $0$ 都变成 $1$。
- 压缩版的二进制减法也是类似的，对于 $10100 \rightarrow 10001$，也会把最低位的 $1$ 变成 $0$，对于最低位的 $1$ 右边的 $0$，并不是都变成 $1$，只有在 $s = 10101$ 中的 $1$ 才会变成 $1$。减一后：

$$
(10100 - 1) \& 10101 = 10001
$$

#### § 4.3 枚举子集（包含空集）

如果要从大到小枚举 $s$ 的所有子集 $sub$（从 $s$ 枚举到空集 $\emptyset$），可以这样写：

```go
for sub := s; ; {
    // 处理 sub 的逻辑
    sub = (sub - 1) & s
    if sub == s {
        break
    }
}
```

原理是当 $sub = 0$ 时（空集），再减一就得到 $-1$，对应的二进制为 $111\dots 1$，再 $\&s$ 就得到了 $s$。所以当循环到 $sub = s$ 时，说明最后一次循环的 $sub = 0$（空集），$s$ 的所有子集都枚举到了，退出循环。

注：还可以枚举全集 $U$ 的所有大小恰好为 $k$ 的子集，这一技巧叫做 Gosper's Hack。

#### § 4.4 枚举超集

如果 $T$ 是 $S$ 的子集，那么称 $S$ 是 $T$ 的超集（superset）

枚举超集的原理和上文枚举子集是类似的，这里通过**或运算**保证枚举的集合 $S$ 一定包含集合 $T$ 中的所有元素

枚举 $S$，满足 $S$ 是 $T$ 的超集，也是全集 $U = {0, 1, 2, \dots, n-1}$ 的子集：

```go
for s := t; s < 1<<n; s = (s + 1) | t {
    // 处理 s 的逻辑
}
```





![Binar_Fundamentals_00.png](images/1686879866-VArRlW-Binar_Fundamentals_00.png)



