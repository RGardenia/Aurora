# Go













## go mod

> go modules是go1.11版本加的新特性。
>
> Modules模块是相关Go包的集合。modules是源代码交换和版本控制的单元。 go命令直接支持使用modules，包括记录和解析对其他模块的依赖性。modules替换旧的基于GOPATH的方法来指定在给定构建中使用哪些源文件。



`go env`  可查看当前 GO 的所有环境变量



GO111MODULE一般有三个值

- GO111MODULE=off，go命令行将不会支持module功能，寻找依赖包的方式将会沿用旧版本那种通过vendor目录或者GOPATH模式来查找。
- GO111MODULE=on，go命令行会使用modules，而一点也不会去GOPATH目录下查找。
- GO111MODULE=auto，默认值，go命令行将会根据当前目录来决定是否启用module功能。
  这种情况下可以分为两种情形：

1. 当前目录在GOPATH/src之外且该目录包含go.mod文件
2. 当前文件在包含go.mod文件的目录下面。
当modules 功能启用时，依赖包的存放位置变更为 $GOPATH/pkg，允许同一个package多个版本并存，且多个项目可以共享缓存的 module。





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
> 其中，direct的作用是：特殊指示符，用于指示 Go 回源到模块版本的源地址去抓取





## go private

go 命令会从公共镜像 http://goproxy.io 上下载依赖包，并且会对下载的软件包和代码库进行安全校验，当你的代码库是公开的时候，这些功能都没什么问题。但是如果你的仓库是私有的怎么办呢？

环境变量 GOPRIVATE 用来控制 go 命令把哪些仓库看做是私有的仓库，这样的话，就可以跳过 proxy server 和校验检查，这个变量的值支持用逗号分隔，可以填写多个值，例如：

```shell
GOPRIVATE=*.corp.example.com,rsc.io/private
```

这样 go 命令会把所有包含这个后缀的软件包，包括 http://git.corp.example.com/xyzzy , http://rsc.io/private, 和 http://rsc.io/private/quux 都以私有仓库来对待。