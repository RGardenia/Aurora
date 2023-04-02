# 卷积



​	

从数学上讲，卷积就是一种运算。

某种运算，能被定义出来，至少有以下特征：

- 首先是抽象的、符号化的
- 其次，在生活、科研中，有着广泛的作用

比如加法：

- `a+b` ，是抽象的，本身只是一个 `数学符号`
- 在现实中，有非常多的意义，比如增加、合成、旋转等等

> 卷积，是我们学习高等数学之后，新接触的一种运算，因为涉及到积分、级数，所以看起来觉得很复杂。



##  卷积的定义

​	我们称 $(f∗g)(n)$ 为 $f,g$ 的卷积

​	其连续的定义为：

​										$(f∗g)(n) = \int_{-\infty}^{\infty}f(\tau) g(n - \tau)d\tau$

​	其离散的定义为：

​										$ (f∗g)(n) = \sum_{\tau = -\infty}^\infty f(\tau) g(n - \tau) $





我们令 $x=τ,y=n−τ $，那么 $x+y=n$ 就是下面这些直线：

![动图](source/images/v2-8be52f6bada3f7a21cebfc210d2e7ea0_720w.gif)

我们来看看现实中，这样的定义有什么意义

​		我有两枚骰子：![img](source/images/v2-e279045403bb2b0d8de72262f37562cd_720w.webp)

​		把这两枚骰子都抛出去：

​		求：两枚骰子点数加起来为 <span style='color:red'>4</span> 的概率是多少？

>  这里问题的关键是，两个骰子加起来要等于4，这正是卷积的应用场景。

>  我们把骰子各个点数出现的概率表示出来：

![img](source/images/v2-4763fd548536b21640d01d3f8a59c546_r.jpg)

​		那么，两枚骰子点数加起来为 4 的情况有：

​	![img](source/images/v2-a67a711702ce48cd7632e783ae0a1f42_720w.png)

![img](source/images/v2-d6ff10bf39c46397ab2bebb971d4b58c_720w.png)

![img](source/images/v2-0cdabcc04398ea723aa6e47e05072e5c_720w.png)

因此，两枚骰子点数加起来为 4 的概率为：

​														$f(1)g(3) + f(2)g(2) + f(3)g(1)$

符合卷积的定义，把它写成标准的形式就是：

​														$ (f∗g)(4) = \sum_{m = 1}^3f(4 - m) g(m) $





<div align = "center">\$(f∗g)(n)\$</div>

<center> $(f∗g)(n)$ </center>