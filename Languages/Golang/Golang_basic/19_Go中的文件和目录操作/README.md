# Go中的文件和目录操作

## 文件的读取

###  通过 os.Open 方法读取文件

```go
func main() {
	// 读取文件 方法1
	file, err := os.Open("./main/test.txt")
	// 关闭文件流
	defer file.Close();
	if err != nil {
		fmt.Println("打开文件出错")
	}
	// 读取文件里面的内容
	var tempSlice = make([]byte, 1024)
	var strSlice []byte
	for {
		n, err := file.Read(tempSlice)
		if err == io.EOF {
			fmt.Printf("读取完毕")
			break
		}
		fmt.Printf("读取到了%v 个字节 \n", n)
		strSlice := append(strSlice, tempSlice...)
		fmt.Println(string(strSlice))
	}
}
```

### 通过 bufio 的方式读取

```go
func main() {
	// 读取文件 方法2
	file, err := os.Open("./main/test.txt")
	// 关闭文件流
	defer file.Close();
	if err != nil {
		fmt.Println("打开文件出错")
	}
	// 通过创建 bufio 来读取
	reader := bufio.NewReader(file)
	var fileStr string
	var count int = 0
	for {
		// 相当于读取一行
		str, err := reader.ReadString('\n')
		if err == io.EOF {
			// 读取完成的时候，也会有内容
			fileStr += str
			fmt.Println("读取结束", count)
			break
		}
		if err != nil {
			fmt.Println(err)
			break
		}
		count ++
		fileStr += str
	}
	fmt.Println(fileStr)
}
```

### 通过 ioutil 读取

文件比较少的时候，可以通过 `ioutil` 来读取文件

```go
// 通过IOUtil读取
byteStr, _ := ioutil.ReadFile("./main/test.txt")
fmt.Println(string(byteStr))
```

## 文件的写入

文件的写入，我们首先需要通过 `os.OpenFile` 打开文件

```go
// 打开文件
file, _ := os.OpenFile("./main/test.txt", os.O_CREATE | os.O_RDWR, 777)
```

- `name`：要打开的文件名
- `flag`：打开文件的模式
  - os.O_WRONLY：只读
  - os.O_CREATE：创建
  - os.O_RDONLY：只读
  - os.O_RDWR：读写
  - os.O_TRUNC：清空
  - os.O_APPEND：追加
- `perm`：文件权限，一个八进制数，r（读）04，w（写）02，x（执行）01

### 通过 OpenFile 打开文件写入

```go
// 打开文件
file, _ := os.OpenFile("./main/test.txt", os.O_CREATE | os.O_RDWR | os.O_APPEND, 777)
defer file.Close()
str := "啦啦啦 \r\n"
file.WriteString(str)
```

### 通过 bufio 写入

```go
// 打开文件
file, _ := os.OpenFile("./main/test.txt", os.O_CREATE | os.O_RDWR | os.O_APPEND, 777)
defer file.Close()
str := "啦啦啦 \r\n"
file.WriteString(str)

// 通过 bufio 写入
writer := bufio.NewWriter(file)
// 先将数据写入缓存
writer.WriteString("你好，我是通过writer写入的 \r\n")
// 将缓存中的内容写入文件
writer.Flush()	
```

## 通过 ioutil 写入

```go
// 第三种方式，通过ioutil
str2 := "hello"
ioutil.WriteFile("./main/test.txt", []byte(str2), 777)
```

## 文件复制

通过 `ioutil` 读取和复制文件

```go
// 读取文件
byteStr, err := ioutil.ReadFile("./main/test.txt")
if err != nil {
    fmt.Println("读取文件出错")
    return
}
// 写入指定的文件
ioutil.WriteFile("./main/test2.txt", byteStr, 777)
```

## 创建目录

```go
os.Mkdir("./abc", 777)
```

## 删除操作

```go
// 删除文件
os.Remove("aaa.txt")
// 删除目录
os.Remove("./aaa")
// 删除多个文件和目录
os.RemoveAll("./aaa")
```

## 重命名

```go
os.Rename("")
```





## Fmt

### **1. 基于 `io.Reader` 的扫描函数**

从实现了 `io.Reader` 接口的对象（如文件、网络连接、缓冲区）中读取数据

| 函数签名                      | 作用                                                        | 行为特点                                                     |
| :---------------------------- | :---------------------------------------------------------- | :----------------------------------------------------------- |
| `fmt.Fscan(r, &a, &b, ...)`   | 从 `r` 读取数据并按空格分隔解析到变量中                     | 跳过空格和换行符，按顺序填充变量，直到输入结束或解析失败     |
| `fmt.Fscanf(r, format, ...)`  | 根据 `format` 字符串的格式从 `r` 读取数据                   | 严格匹配格式（如 `"%d,%s"` 表示整数+逗号+字符串），类似 `C` 的 `scanf` |
| `fmt.Fscanln(r, &a, &b, ...)` | 从 `r` 读取一行数据（到换行符为止），按空格分隔解析到变量中 | 读取到换行符后停止，并丢弃换行符                             |

```go
reader := strings.NewReader("123 hello\n456 world")
var num int
var str string

// 使用 Fscan 读取第一个空格分隔的值
fmt.Fscan(reader, &num, &str)  // num=123, str="hello"

// 使用 Fscanln 读取下一行
fmt.Fscanln(reader, &num, &str) // num=456, str="world"
```

### **2. 基于字符串的扫描函数**

直接从字符串中解析数据（无需 `io.Reader`）

| 函数签名                      | 作用                                                         | 行为特点                         |
| :---------------------------- | :----------------------------------------------------------- | :------------------------------- |
| `fmt.Sscan(s, &a, &b, ...)`   | 从字符串 `s` 中按空格分隔解析到变量                          | 类似 `Fscan`，但输入源是字符串   |
| `fmt.Sscanf(s, format, ...)`  | 根据 `format` 字符串的格式从 `s` 中解析数据                  | 类似 `Fscanf`，但输入源是字符串  |
| `fmt.Sscanln(s, &a, &b, ...)` | 从字符串 `s` 中读取一行（到换行符为止），按空格分隔解析到变量 | 类似 `Fscanln`，但输入源是字符串 |

```go
s := "42 3.14 Golang"
var i int
var f float64
var lang string

fmt.Sscan(s, &i, &f, &lang)  // i=42, f=3.14, lang="Golang"
```

### **3. 基于标准输入的扫描函数**

直接从标准输入（如命令行）读取数据

| 函数签名                  | 作用                                                     | 行为特点                                 |
| :------------------------ | :------------------------------------------------------- | :--------------------------------------- |
| `fmt.Scan(&a, &b, ...)`   | 从标准输入读取数据并按空格分隔解析到变量                 | 跳过空格和换行符，直到输入结束或解析失败 |
| `fmt.Scanf(format, ...)`  | 根据 `format` 字符串的格式从标准输入读取数据             | 严格匹配格式（如 `"%d,%s"`）             |
| `fmt.Scanln(&a, &b, ...)` | 从标准输入读取一行（到换行符为止），按空格分隔解析到变量 | 读取到换行符后停止，并丢弃换行符         |

```go
// 用户输入 "100 200\n"
var a, b int
fmt.Scan(&a, &b)     // a=100, b=200

// 用户输入 "Name: Alice\n"
var name string
fmt.Scanf("Name: %s", &name)  // name="Alice"
```

### 通用行为总结

| **特性**       | **Fscan/Fscanf/Fscanln** | **Sscan/Sscanf/Sscanln** | **Scan/Scanf/Scanln**  |
| :------------- | :----------------------- | :----------------------- | :--------------------- |
| **输入源**     | `io.Reader`              | 字符串                   | 标准输入 (`os.Stdin`)  |
| **空格处理**   | 跳过空格                 | 跳过空格                 | 跳过空格               |
| **换行符处理** | 仅 `Fscanln` 关注换行符  | 仅 `Sscanln` 关注换行符  | 仅 `Scanln` 关注换行符 |
| **格式灵活性** | 高（支持自定义格式）     | 高                       | 高                     |

### **错误处理**

所有 `Scan` 函数返回两个值：

```go
n, err := fmt.Fscan(reader, &a, &b)

if n, err := fmt.Scan(&a, &b); err != nil {
    fmt.Printf("解析失败，成功解析 %d 个变量，错误：%v\n", n, err)
}
```

- `n`：成功解析的变量数量
- `err`：错误信息（如 `io.EOF` 表示输入结束，或类型不匹配的解析错误）  成功读取结果为 nil
