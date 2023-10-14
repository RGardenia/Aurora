# Go Commands



## Go 命令全列表

​	快速浏览Go语言的所有内建命令及其基本功能。这些命令涵盖了从代码构建、测试，到依赖管理和其他工具等方面。

| 命令                            | 功能描述                 |
| :------------------------------ | :----------------------- |
| [`go build`](# go build)        | 编译Go源文件             |
| [`go run`](#go run)             | 编译并运行Go程序         |
| [`go get`](#go get)             | 下载并安装依赖或项目     |
| [`go mod`](#go mod)             | Go模块支持               |
| [`go list`](# go list)          | 列出包或模块             |
| [`go fmt`](#go fmt)             | 格式化代码               |
| [`go vet`](#go vet)             | 静态检查代码             |
| [`go test`](#go test)           | 运行测试                 |
| [`go doc`](#go doc)             | 查看文档                 |
| [`go env`](#go env)             | 打印Go环境信息           |
| [`go clean`](#go clean)         | 删除编译生成的文件       |
| [`go tool`](#go too)            | 运行指定的go工具         |
| [`go version`]()                | 打印Go当前版本           |
| [`go install`](#go install)     | 编译和安装Go程序或库     |
| [`go generate`](#go generate)   | 通过处理源生成Go文件     |
| [`go fix`](#go fix)             | 更新包以使用新的API      |
| [`go workspace`](#go workspace) | 管理 Go 工作区（实验性） |
| [`go help`]()                   | 查看命令或主题的帮助信息 |



## go build

命令说明

`go build`命令用于编译Go源文件。该命令会根据源代码生成可执行文件或库。

使用场景

- 编译单个Go文件或整个项目
- 创建库文件
- 交叉编译

实际返回案例

```go
$ go build hello.go
# 无输出，但会生成一个名为hello的可执行文件
```

## go run

命令说明

`go run`命令用于编译并运行Go程序。适用于快速测试代码片段。

使用场景

- 快速测试小程序
- 不需要生成持久的可执行文件

实际返回案例

```
$ go run hello.go
Hello, world!
```

## go get

命令说明

`go get`用于下载并安装依赖或项目。

使用场景

- 下载第三方库
- 更新项目依赖

实际返回案例

```
$ go get github.com/gin-gonic/gin
# 下载并安装gin库，无输出
```

## go mod

> go modules 是 go1.11 版本加的新特性，用于Go模块支持，包括初始化、添加依赖等。
>
> `Modules` 模块是相关Go包的集合。 `modules` 是源代码交换和版本控制的单元。 go 命令直接支持使用 `modules` ，包括记录和解析对其他模块的依赖性。 `modules` 替换旧的基于GOPATH的方法来指定在给定构建中使用哪些源文件。

​						`go env`  可查看当前 GO 的所有环境变量

GO111MODULE一般有三个值

- GO111MODULE=off，go命令行将不会支持module功能，寻找依赖包的方式将会沿用旧版本那种通过 vendo r目录或者 GOPATH 模式来查找。
- GO111MODULE=on，go命令行会使用 modules，而一点也不会去GOPATH目录下查找。
- GO111MODULE=auto，默认值，go命令行将会根据当前目录来决定是否启用module功能。
  这种情况下可以分为两种情形：
  
  1. 当前目录在 GOPATH/src 之外且该目录包含 go.mod 文件
  
  2. 当前文件在包含 go.mod 文件的目录下面。
     当 modules 功能启用时，依赖包的存放位置变更为 $GOPATH/pkg，允许同一个 package 多个版本并存，且多个项目可以共享缓存的 module。

```bash
$ go mod init my-module
go: creating new go.mod: module my-module
```

## go list

命令说明

`go list`用于列出包或模块。

使用场景

- 查看当前项目依赖
- 查看全局安装的包

实际返回案例

```bash
$ go list ./...
# 列出当前项目所有包
```

## go fmt

命令说明

`go fmt`用于自动格式化Go源代码。

使用场景

- 代码审查
- 统一代码风格

实际返回案例

```bash
$ go fmt hello.go
# 格式化hello.go文件，返回格式化后的文件名
hello.go
```

## go vet

命令说明

`go vet`用于对Go代码进行静态分析，检查可能存在的错误。

使用场景

- 代码质量检查
- 发现潜在问题

实际返回案例

```bash
$ go vet hello.go
#  若代码无问题，则没有输出
```

## go test

命令说明

`go test`用于运行Go程序的测试。

使用场景

- 单元测试
- 性能测试

实际返回案例

```
$ go test
ok      github.com/yourusername/yourpackage 0.002s
```

## go doc

命令说明

`go doc`用于查看Go语言标准库或你的代码库中的文档。

使用场景

- 查找库函数说明
- 查看接口文档

实际返回案例

```
$ go doc fmt.Println
func Println(a ...interface{}) (n int, err error)
```

## go env

命令说明

`go env`用于打印Go的环境信息

使用场景

- 环境配置
- 问题诊断

实际返回案例

```
$ go env
GOARCH="amd64"
GOBIN=""
...
```

## go clean

命令说明

`go clean`用于删除编译生成的文件。

使用场景

- 清理项目目录
- 回复到初始状态

实际返回案例

```
$ go clean
# 删除编译生成的文件，无输出
```

## go tool

命令说明

`go tool`用于运行指定的Go工具。

使用场景

- 编译优化
- 调试

实际返回案例

```
$ go tool compile hello.go
# 编译hello.go，生成中间文件
```

## go install

命令说明

`go install`用于编译和安装Go程序或库

使用场景

- 创建可分发的二进制文件
- 安装库到系统路径

```
$ go install hello.go
# 编译并安装hello程序，无输出
```

## go generate

命令说明

`go generate`用于通过处理源代码来生成Go文件

使用场景

- 代码生成
- 模板处理

```
$ go generate
# 运行生成指令，生成代码，无输出
```

## go fix

命令说明

`go fix`用于更新包以使用新的 API

使用场景

- API 迁移
- 自动修复代码

实际返回案例

```bash
$ go fix oldpackage
# 更新oldpackage包的API调用，无输出
```

## go workspace

命令说明

`go workspace`用于管理Go工作区。这是一个实验性功能。

使用场景

- 多项目管理
- 环境隔离

```bash
$ go workspace create myworkspace
# 创建名为myworkspace的工作区，无输出
```

## go proxy

```go
go env -w GO111MODULE=on
go env -w GOPROXY=https://goproxy.cn,direct
```

> 作用：使 Go 在拉取模块版本时能够脱离传统的 VCS 方式从镜像站点快速拉取。

> 目前常用的代理是：
>
> 1、https://goproxy.cn
>
> 2、https://goproxy.io
>
> 其中，direct 的作用是：特殊指示符，用于指示 Go 回源到模块版本的源地址去抓取



## go private

go 命令会从公共镜像 http://goproxy.io 上下载依赖包，并且会对下载的软件包和代码库进行安全校验，当你的代码库是公开的时候，这些功能都没什么问题。但是如果你的仓库是私有的呢？

环境变量 GOPRIVATE 用来控制 go 命令把哪些仓库看做是私有的仓库，这样的话，就可以跳过 proxy server 和校验检查，这个变量的值支持用逗号分隔，可以填写多个值，例如：

```shell
GOPRIVATE=*.corp.example.com,rsc.io/private
```

这样 go 命令会把所有包含这个后缀的软件包，包括 http://git.corp.example.com/xyzzy , http://rsc.io/private, 和 http://rsc.io/private/quux 都以私有仓库来对待。

