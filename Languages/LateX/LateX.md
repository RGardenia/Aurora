# LateX 公式模板

## 矩阵 | Matrices

![img](LateX.assets/4d7c1976dd1434f45ab1552953159209.svg)



```plain
\begin{matrix}
1 & x & x^2 \\
1 & y & y^2 \\
1 & z & z^2 \\
\end{matrix}
```

![img](LateX.assets/f1aa6288806d09a61d79b5c869bceb79.svg)



```plain
\begin{pmatrix}1&2\\3&4\\ \end{pmatrix}
```



![img](LateX.assets/541c0999fd4da35fb20cfbc4aa8a192d.svg)



```plain
\begin{bmatrix}1&2\\3&4\\ \end{bmatrix}
```



![img](LateX.assets/f3dfd57d59fd361fc52ee8c7f12df3ae.svg)



```plain
\begin{Bmatrix}1&2\\3&4\\ \end{Bmatrix}
```



![img](LateX.assets/d0936a9b003e3b5939a1ed5c94f315d5.svg)



```plain
\begin{vmatrix}1&2\\3&4\\ \end{vmatrix}
```



![img](LateX.assets/be6d0101aec0ecb29a841503fcd81b7a.svg)



```plain
\begin{Vmatrix}1&2\\3&4\\ \end{Vmatrix}
```



![img](LateX.assets/8ff56cbad6739087033a7bd07e232c5b.svg)



```plain
\begin{pmatrix}
 1 & a_1 & a_1^2 & \cdots & a_1^n \\
 1 & a_2 & a_2^2 & \cdots & a_2^n \\
 \vdots  & \vdots& \vdots & \ddots & \vdots \\
 1 & a_m & a_m^2 & \cdots & a_m^n    
 \end{pmatrix}
```



![img](LateX.assets/712b53859d2e9a001b5da352c40612c1.svg)



```plain
\left[
\begin{array}{cc|c}
  1&2&3\\
  4&5&6
\end{array}
\right]
```



![img](LateX.assets/4374ae858628a89ed1b568ee027c8006.svg)



```plain
\begin{pmatrix}
    a & b\\
    c & d\\
  \hline
    1 & 0\\
    0 & 1
  \end{pmatrix}
```



![img](LateX.assets/87d1d481eb6cb69bd4001fb8d7a77a80.svg)



```plain
 $\bigl( \begin{smallmatrix} a & b \\ c & d \end{smallmatrix} \bigr)$
```



## 对齐方程 | Aligned equations



![img](LateX.assets/4789d8625f3aeedfc58a52304313e28b.svg)



```plain
\begin{align}
\sqrt{37} & = \sqrt{\frac{73^2-1}{12^2}} \\
 & = \sqrt{\frac{73^2}{12^2}\cdot\frac{73^2-1}{73^2}} \\ 
 & = \sqrt{\frac{73^2}{12^2}}\sqrt{\frac{73^2-1}{73^2}} \\
 & = \frac{73}{12}\sqrt{1 - \frac{1}{73^2}} \\ 
 & \approx \frac{73}{12}\left(1 - \frac{1}{2\cdot73^2}\right)
\end{align}
```



![img](LateX.assets/436281a853b5b97e3d5271f5da3e780e.svg)



```plain
\begin{align} f(x)&=\left(x^3\right)+\left(x^3+x^2+x^1\right)+\left(x^3+x^‌2\right)\\ f'(x)&=\left(3x^2+2x+1\right)+\left(3x^2+2x\right)\\ f''(x)&=\left(6x+2\right)\\ \end{align}
```



## 分段函数 | piecewise functions



![img](LateX.assets/b974263b3feead1e50eecd120fc4ff0a.svg)



```plain
f(n) =
\begin{cases}
n/2,  & \text{if $n$ is even} \\
3n+1, & \text{if $n$ is odd}
\end{cases}
```



![img](LateX.assets/dd096a956213874ec880dabc63f92724.svg)



```plain
\left.
\begin{array}{l}
\text{if $n$ is even:}&n/2\\
\text{if $n$ is odd:}&3n+1
\end{array}
\right\}
=f(n)
```



![img](LateX.assets/9f2eca387a159e80d3dcc251348e1caa.svg)



```plain
f(n) =
\begin{cases}
\frac{n}{2},  & \text{if $n$ is even} \\[2ex]
3n+1, & \text{if $n$ is odd}
\end{cases}
```



## 数组 | Arrays



![img](LateX.assets/2b2237e1b514a95fb4f63d0740c0d6a4.svg)



```plain
\begin{array}{c|lcr}
n & \text{Left} & \text{Center} & \text{Right} \\
\hline
1 & 0.24 & 1 & 125 \\
2 & -1 & 189 & -8 \\
3 & -20 & 2000 & 1+10i
\end{array}
```



![img](LateX.assets/206b7a6c6ffee4d029f625ee5d356dff.svg)



```plain
% outer vertical array of arrays
\begin{array}{c}
% inner horizontal array of arrays
\begin{array}{cc}
% inner array of minimum values
\begin{array}{c|cccc}
\text{min} & 0 & 1 & 2 & 3\\
\hline
0 & 0 & 0 & 0 & 0\\
1 & 0 & 1 & 1 & 1\\
2 & 0 & 1 & 2 & 2\\
3 & 0 & 1 & 2 & 3
\end{array}
&
% inner array of maximum values
\begin{array}{c|cccc}
\text{max}&0&1&2&3\\
\hline
0 & 0 & 1 & 2 & 3\\
1 & 1 & 1 & 2 & 3\\
2 & 2 & 2 & 2 & 3\\
3 & 3 & 3 & 3 & 3
\end{array}
\end{array}
\\
% inner array of delta values
\begin{array}{c|cccc}
\Delta&0&1&2&3\\
\hline
0 & 0 & 1 & 2 & 3\\
1 & 1 & 0 & 1 & 2\\
2 & 2 & 1 & 0 & 1\\
3 & 3 & 2 & 1 & 0
\end{array}
\end{array}
```



![img](LateX.assets/7811229254cb12a679070a4b82a0d3ea.svg)



```plain
\begin{array}{ll} \hfill\mathrm{Bad}\hfill & \hfill\mathrm{Better}\hfill \\ \hline \\ e^{i\frac{\pi}2} \quad e^{\frac{i\pi}2}& e^{i\pi/2} \\ \int_{-\frac\pi2}^\frac\pi2 \sin x\,dx & \int_{-\pi/2}^{\pi/2}\sin x\,dx \\ \end{array}
```



## 方程组 | System of equations



![img](LateX.assets/fe2459e59e8695791efd1257fcc91d7e.svg)



```plain
\left\{ 
\begin{array}{c}
a_1x+b_1y+c_1z=d_1 \\ 
a_2x+b_2y+c_2z=d_2 \\ 
a_3x+b_3y+c_3z=d_3
\end{array}
\right.
```



![img](LateX.assets/3f13050e13b6bdee74b8bc4aaa4a24c2.svg)



```plain
\begin{cases}
a_1x+b_1y+c_1z=d_1 \\ 
a_2x+b_2y+c_2z=d_2 \\ 
a_3x+b_3y+c_3z=d_3
\end{cases}
```



![img](LateX.assets/78c0760f90f5b249c2953cd7a1c9d2c7.svg)



```plain
\left\{
\begin{aligned} 
a_1x+b_1y+c_1z &=d_1+e_1 \\ 
a_2x+b_2y&=d_2 \\ 
a_3x+b_3y+c_3z &=d_3 
\end{aligned} 
\right. 
```



![img](LateX.assets/0b1f7e51f71f6889043d33b935931beb.svg)



```plain
\left\{
\begin{array}{ll}
a_1x+b_1y+c_1z &=d_1+e_1 \\ 
a_2x+b_2y &=d_2 \\ 
a_3x+b_3y+c_3z &=d_3 
\end{array} 
\right.
```



![img](LateX.assets/342b67f6443bcd7813fa3daf9a6c3e13.svg)



```plain
\begin{cases}
a_1x+b_1y+c_1z=\frac{p_1}{q_1} \\[2ex] 
a_2x+b_2y+c_2z=\frac{p_2}{q_2} \\[2ex] 
a_3x+b_3y+c_3z=\frac{p_3}{q_3}
\end{cases}
```



![img](LateX.assets/f1e185da7f127cb74cd33630caceb922.svg)



```plain
\begin{cases}
a_1x+b_1y+c_1z=\frac{p_1}{q_1} \\
a_2x+b_2y+c_2z=\frac{p_2}{q_2} \\
a_3x+b_3y+c_3z=\frac{p_3}{q_3}
\end{cases}
```



![img](LateX.assets/a90a3d3147456432517a1687b457633e.svg)



```plain
\left\{ \begin{array}{l}
0 = c_x-a_{x0}-d_{x0}\dfrac{(c_x-a_{x0})\cdot d_{x0}}{\|d_{x0}\|^2} + c_x-a_{x1}-d_{x1}\dfrac{(c_x-a_{x1})\cdot d_{x1}}{\|d_{x1}\|^2} \\[2ex] 
0 = c_y-a_{y0}-d_{y0}\dfrac{(c_y-a_{y0})\cdot d_{y0}}{\|d_{y0}\|^2} + c_y-a_{y1}-d_{y1}\dfrac{(c_y-a_{y1})\cdot d_{y1}}{\|d_{y1}\|^2} \end{array} \right. 
```



## 颜色 | Colors



![img](LateX.assets/19cb8f66ae7b6c9ac0afa7d4ac3a1fb1.svg)



```plain
\begin{array}{|rc|}
\hline
\verb+\color{black}{text}+ & \color{black}{text} \\
\verb+\color{gray}{text}+ & \color{gray}{text} \\
\verb+\color{silver}{text}+ & \color{silver}{text} \\
\verb+\color{white}{text}+ & \color{white}{text} \\
\hline
\verb+\color{maroon}{text}+ & \color{maroon}{text} \\
\verb+\color{red}{text}+ & \color{red}{text} \\
\verb+\color{yellow}{text}+ & \color{yellow}{text} \\
\verb+\color{lime}{text}+ & \color{lime}{text} \\
\verb+\color{olive}{text}+ & \color{olive}{text} \\
\verb+\color{green}{text}+ & \color{green}{text} \\
\verb+\color{teal}{text}+ & \color{teal}{text} \\
\verb+\color{aqua}{text}+ & \color{aqua}{text} \\
\verb+\color{blue}{text}+ & \color{blue}{text} \\
\verb+\color{navy}{text}+ & \color{navy}{text} \\
\verb+\color{purple}{text}+ & \color{purple}{text} \\ 
\verb+\color{fuchsia}{text}+ & \color{magenta}{text} \\
\hline
\end{array}
```



![img](LateX.assets/19cb8f66ae7b6c9ac0afa7d4ac3a1fb1.svg)



```plain
\begin{array}{|rc|}
\hline
\verb+\color{black}{text}+ & \color{black}{text} \\
\verb+\color{gray}{text}+ & \color{gray}{text} \\
\verb+\color{silver}{text}+ & \color{silver}{text} \\
\verb+\color{white}{text}+ & \color{white}{text} \\
\hline
\verb+\color{maroon}{text}+ & \color{maroon}{text} \\
\verb+\color{red}{text}+ & \color{red}{text} \\
\verb+\color{yellow}{text}+ & \color{yellow}{text} \\
\verb+\color{lime}{text}+ & \color{lime}{text} \\
\verb+\color{olive}{text}+ & \color{olive}{text} \\
\verb+\color{green}{text}+ & \color{green}{text} \\
\verb+\color{teal}{text}+ & \color{teal}{text} \\
\verb+\color{aqua}{text}+ & \color{aqua}{text} \\
\verb+\color{blue}{text}+ & \color{blue}{text} \\
\verb+\color{navy}{text}+ & \color{navy}{text} \\
\verb+\color{purple}{text}+ & \color{purple}{text} \\ 
\verb+\color{fuchsia}{text}+ & \color{magenta}{text} \\
\hline
\end{array}
```



## 交换图 | Commutative diagrams



![img](LateX.assets/724ec27ca37ff36e34d8a8b87a6bb11b.svg)



```plain
\begin{CD}
A @>a>> B\\
@V b V V= @VV c V\\
C @>>d> D
\end{CD}
```



![img](LateX.assets/88f06ec55e9c5a12bfbf4d8e50c812ad.svg)



```plain
\begin{CD}
A @>>> B @>{\text{very long label}}>> C \\
@. @AAA @| \\
D @= E @<<< F
\end{CD}
```



![img](LateX.assets/1e5122f25fb0303b5b4dcc4a07ad1e70.svg)



```plain
\begin{CD}
  RCOHR'SO_3Na @>{\text{Hydrolysis,$\Delta, Dil.HCl$}}>> (RCOR')+NaCl+SO_2+ H_2O 
\end{CD}
```



## 持续分数 | Continued fractions



![img](LateX.assets/afda311849f07adafc5ceffcf4faec86.svg)



```plain
x = a_0 + \cfrac{1^2}{a_1
          + \cfrac{2^2}{a_2
          + \cfrac{3^2}{a_3 + \cfrac{4^4}{a_4 + \cdots}}}}
```



![img](LateX.assets/c5e5afe77da7246ae11b79cda7bb4308.svg)



```plain
x = a_0 + \frac{1^2}{a_1
          + \frac{2^2}{a_2
          + \frac{3^2}{a_3 + \frac{4^4}{a_4 + \cdots}}}}
```



![img](LateX.assets/bc0e7f1f4be216d4617415ac5dce5ee9.svg)



```plain
x = a_0 + \frac{1^2}{a_1+}
          \frac{2^2}{a_2+}
          \frac{3^2}{a_3 +} \frac{4^4}{a_4 +} \cdots
```



![img](LateX.assets/1ce487626257db3062d0b29e5b38f357.svg)



```plain
\cfrac{a_{1}}{b_{1}+\cfrac{a_{2}}{b_{2}+\cfrac{a_{3}}{b_{3}+\ddots }}}=   {\genfrac{}{}{}{}{a_1}{b_1}}   {\genfrac{}{}{0pt}{}{}{+}}   {\genfrac{}{}{}{}{a_2}{b_2}}   {\genfrac{}{}{0pt}{}{}{+}}   {\genfrac{}{}{}{}{a_3}{b_3}}   {\genfrac{}{}{0pt}{}{}{+\dots}}
```



![img](LateX.assets/1ab5c392a8e71aee90d00b3c552d1ce7.svg)



```plain
\underset{j=1}{\overset{\infty}{\LARGE\mathrm K}}\frac{a_j}{b_j}=\cfrac{a_1}{b_1+\cfrac{a_2}{b_2+\cfrac{a_3}{b_3+\ddots}}}.
```



![img](LateX.assets/edf801565218f819051ca46880a44ad9.svg)



```plain
\mathop{\LARGE\mathrm K}_{i=1}^\infty \frac{a_i}{b_i}
```



## 大括号 | Big braces



![img](LateX.assets/42356768b8d8c2b1bdb8279dde117c94.svg)



```plain
f\left(
   \left[ 
     \frac{
       1+\left\{x,y\right\}
     }{
       \left(
          \frac{x}{y}+\frac{y}{x}
       \right)
       \left(u+1\right)
     }+a
   \right]^{3/2}
\right)
```



![img](LateX.assets/765828f68c460a4a9f25c08763232874.svg)



```plain
\begin{aligned}
a=&\left(1+2+3+  \cdots \right. \\
& \cdots+ \left. \infty-2+\infty-1+\infty\right)
\end{aligned}
```



![img](LateX.assets/a7b8e262b55f7e8ce9f5840aa5e9a3ea.svg)



```plain
\left\langle  
  q
\middle\|
  \frac{\frac{x}{y}}{\frac{u}{v}}
\middle| 
   p 
\right\rangle
```



## 高亮 | Highlighting equation



![img](LateX.assets/872cb9d2570bf34ac2c6abd8a269444a.svg)



```plain
\bbox[yellow]
{
e^x=\lim_{n\to\infty} \left( 1+\frac{x}{n} \right)^n
\qquad (1)
}
```



![img](LateX.assets/eed1c3bbed125da233379792a89081f5.svg)



```plain
\bbox[yellow,5px]
{
e^x=\lim_{n\to\infty} \left( 1+\frac{x}{n} \right)^n
\qquad (1)
}
```



![img](LateX.assets/e3747974cfb95c4ffdfdf879cadf2657.svg)



```plain
\bbox[5px,border:2px solid red]
{
e^x=\lim_{n\to\infty} \left( 1+\frac{x}{n} \right)^n
\qquad (2) 
}
```



![img](LateX.assets/a092b7753abe6e29e201d8239960dea6.svg)



```plain
\bbox[yellow,5px,border:2px solid red]
{
e^x=\lim_{n\to\infty} \left( 1+\frac{x}{n} \right)^n
\qquad (1)
}
```

![image-20220424191854694](LateX.assets/image-20220424191854694.png)![image-20220424191907937](LateX.assets/image-20220424191907937.png)

![image-20220424191919543](LateX.assets/image-20220424191919543.png)

![image-20220424191927473](LateX.assets/image-20220424191927473.png)



## Pack of cards



![img](LateX.assets/9cc0dd3f1cfa321eaf324d3220cc4032.svg) ![img](LateX.assets/970ce49d7bd424e1dbe99e458c697d9c.svg)



```plain
\spadesuit\quad\heartsuit\quad\diamondsuit\quad\clubsuit
```



```plain
\color{red}{\heartsuit}\quad\color{red}{\diamondsuit}
```



![img](LateX.assets/0c282f8e5835e6bbdc600f913860306f.svg)



```plain
♠\quad♡\quad♢\quad♣\\
♤\quad♥\quad♦\quad♧
```



## 长除法 | Long division



![img](LateX.assets/d1cb8cdf4b5fe8cab6309787acfd67f4.svg)



```plain
\require{enclose}
\begin{array}{r}
                13  \\[-3pt]
4 \enclose{longdiv}{52} \\[-3pt]
     \underline{4}\phantom{2} \\[-3pt]
                12  \\[-3pt]
     \underline{12}
\end{array}
```



![img](LateX.assets/961d1ec0fc3aea887ed38915ab50dfed.svg)



```plain
\begin{array}{c|rrrr}& x^3 & x^2 & x^1 &  x^0\\ & 1 & -6 & 11 & -6\\ {\color{red}1} & \downarrow & 1 & -5 & 6\\ \hline & 1 & -5 & 6 & |\phantom{-} {\color{blue}0} \end{array}
```



![img](LateX.assets/11cfe6fe7c4d02f0c35e90a2fd3ce7ca.svg)



```plain
x^3−6x^2+11x−6=(x−{\color{red}1})(x^2−5x+6)+{\color{blue}0}
```



## Degree symbol



![img](LateX.assets/42eaf9a01cd76e905e77a70a108cf0fe.svg)



```plain
\begin{array} \\
\text{45^\text{o}} & \text{renders as} & 45^\text{o} \\
\text{45^o} & \text{renders as} & 45^o \\
\text{45^\circ} & \text{renders as} & 45^\circ \\
\text{90°} & \text{renders as} & 90° & \text{Using keyboard entry of symbol}
%
% Use the following line as a template for additional entries
%
% \text{} & \text{renders as} &  \\
\end{array}
```



## 其他 | Others



![img](LateX.assets/03ef124d81b2f8a68b64132cdffb350b.svg)



```plain
\sum_{n=1}^\infty \frac{1}{n^2} \to
  \textstyle \sum_{n=1}^\infty \frac{1}{n^2} \to
  \displaystyle \sum_{n=1}^\infty \frac{1}{n^2}
```



Compare ![img](LateX.assets/3c4c16866bf27c865601dcb14dd59ea8.svg) versus ![img](LateX.assets/47d8d0672f7392822528c0e891d80110.svg)



能量守恒



![img](LateX.assets/aca7a6db422b162f35d72428d4fa3799.svg)



```plain
e=mc^2 \tag{1}\label{eq1}
```



![img](LateX.assets/989a2cb838cb3b47ad2951c1d83b17bc.svg)



```plain
\begin{equation}\begin{aligned}
a &= b + c \\
  &= d + e + f + g \\
  &= h + i
\end{aligned}\end{equation}\tag{2}\label{eq2}
```



![img](LateX.assets/fa1ab4adcc5e3fbdb3991b8f1a35af79.svg)





```plain
\begin{align}
a &= b + c \tag{3}\label{eq3} \\
x &= yz \tag{4}\label{eq4}\\
l &= m - n \tag{5}\label{eq5}
\end{align}
```



![img](LateX.assets/5fc3036aa0ce6d5f4e799e5f90642963.svg)



```plain
54\,321.123\,45
```



![img](LateX.assets/2128b2c86bc98bc4b02829b48b98b3c4.svg)



```plain
\left.\mathrm{m}\middle/\mathrm{s}^2\right.
```



![img](LateX.assets/a5bf08fa5e0e4542d8b6c477b0defed4.svg)



```plain
\mu_0=4\pi\times10^{-7} \ \left.\mathrm{\mathrm{T}\!\cdot\!\mathrm{m}}\middle/\mathrm{A}\right.
```



![img](LateX.assets/517b855a8bc8fab916fac2dec5ebcac8.svg)



```plain
\begin{array}{rrrrrr|r}
           & x_1 & x_2 & s_1 & s_2 & s_3 &    \\ \hline
       s_1 &   0 &   1 &   1 &   0 &   0 &  8 \\
       s_2 &   1 &  -1 &   0 &   1 &   0 &  4 \\
       s_3 &   1 &   1 &   0 &   0 &   1 & 12 \\ \hline
           &  -1 &  -1 &   0 &   0 &   0 &  0
\end{array}
```



![img](LateX.assets/cfe02a57b7e2c29f7ebf69a0cb461ad9.svg)



```plain
\begin{array}{rrrrrrr|rr}
  & x_1 & x_2 & s_1 & s_2 & s_3 &  w &    & \text{ratio} \\ \hline
  s_1 &   0 &   1 &   1 &   0 &   0 &  0 &  8 &            - \\
w & 1^* &  -1 &   0 &  -1 &   0 &  1 &  4 &            4 \\
  s_3 &   1 &   1 &   0 &   0 &   1 &  0 & 12 &           12 \\ \hdashline
  &   1 &  -1 &   0 &  -1 &   0 &  0 &  4 &              \\ \hline
  s_1 &   0 &   1 &   1 &   0 &   0 &  0 &  8 &              \\
  x_1 &   1 &  -1 &   0 &  -1 &   0 &  1 &  4 &              \\
  s_3 &   0 &   2 &   0 &   2 &   1 & -1 &  8 &              \\ \hdashline
  &   0 &   0 &   0 &   0 &   0 & -1 &  0 &
\end{array}
```



![img](LateX.assets/82020009178ea3061a2808aa087a11a5.svg)



```plain
\begin{array}{rrrrrrrr|r}
         & x_1 & x_2 & x_3 & x_4 & x_5 & x_6 &  x_7 &        \\ \hline
     x_4 &   0 &  -3 &   7 &   1 &   0 &   0 &    2 & 2M  -4 \\
     x_5 &   0 &  -9 &   0 &   0 &   1 &   0 &   -1 & -M  -3 \\
     x_6 &   0 &   6 &  -1 &   0 &   0 &   1 & -4^* & -4M +8 \\
     x_1 &   1 &   0 &   1 &   0 &   0 &   0 &    1 &      M \\ \hline
         &   0 &   1 &   1 &   0 &   0 &   0 &    2 &     2M \\
\text{ratio} &     &     &   1 &     &     &     &  1/2 &
\end{array}
```



![img](LateX.assets/e0a227a6d0b846e0f6433bb02bd273b6.svg)



```plain
\begin{array}{rrrrrrr|r}
         &  x_1 &  x_2 &  x_3 &  s_1 &    s_2 &  s_3 &       \\     \hline
     s_1 &   -2 &    0 &   -2 &    1 &      0 &    0 &   -60 \\
     s_2 &   -2 & -4^* &   -5 &    0 &      1 &    0 &   -70 \\
     s_3 &    0 &   -3 &   -1 &    0 &      0 &    1 &   -27 \\ \hdashline
         &    8 &   10 &   25 &    0 &      0 &    0 &     0 \\
\text{ratio} &   -4 & -5/2 &   -5 &      &        &      &       \\     \hline
     s_1 & -2^* &    0 &   -2 &    1 &      0 &    0 &   -60 \\
     x_2 &  1/2 &    1 &  5/4 &    0 &   -1/4 &    0 &  35/2 \\
     s_3 &  3/2 &    0 & 11/4 &    0 &   -3/4 &    1 &  51/2 \\ \hdashline
         &    3 &    0 & 25/2 &    0 &    5/2 &    0 &  -175 \\
\text{ratio} & -3/2 &      & 25/4 &      &        &      &       \\     \hline
     x_1 &    1 &    0 &    1 & -1/2 &      0 &    0 &    30 \\
     x_2 &    0 &    1 &  3/4 &  1/4 &   -1/4 &    0 &   5/2 \\
     s_3 &    0 &    0 &  5/4 &  3/4 & -3/4^* &    1 & -39/2 \\ \hdashline
         &    0 &    0 & 19/2 &  3/2 &    5/2 &    0 &  -265 \\
\text{ratio} &      &      &      &      &  \dots &      &       \\     \hline
     x_1 &    1 &    0 &    1 & -1/2 &      0 &    0 &    30 \\
     x_2 &    0 &    1 &  1/3 &    0 &      0 & -1/3 &     9 \\
     s_2 &    0 &    0 & -5/3 &   -1 &      1 & -4/3 &    26 \\ \hdashline
         &    0 &    0 & 41/3 &    4 &      0 & 10/3 &  -330
\end{array}
```



![img](LateX.assets/a4ae2cb2cb7243143f10e56309b53485.svg)



```plain
\require{extpfeil} % produce extensible horizontal arrows
\begin{array}{ccc} % arrange LPPs
% first row
% first LPP
\begin{array}{ll}
\max & z = c^T x \\
\text{s.t.} & A x \le b \\
& x \ge 0
\end{array}
& \xtofrom{\text{duality}} &
% second LPP
\begin{array}{ll}
\min & v = b^T y \\
\text{s.t.} & A^T y \ge c \\
& y \ge 0
\end{array} \\
({\cal PC}) & & ({\cal DC}) \\
\text{add } {\Large \downharpoonleft} \text{slack var} &  & \text{minus } {\Large \downharpoonright} \text{surplus var}\\ % Change to your favorite arrow style
%
% second row
% third LPP
\begin{array}{ll}
\max & z = c^T x \\
\text{s.t.} & A x + s = b \\
& x,s \ge 0
\end{array}
& \xtofrom[\text{some steps skipped}]{\text{duality}} &
% fourth LPP
\begin{array}{ll}
\min & v = b^T y \\
\text{s.t.} & A^T y - t = c \\
& y,t \ge 0
\end{array} \\
({\cal PS}) & & ({\cal DS})
%
\end{array}
```



![img](LateX.assets/191eb26e80f3567ad459471e111eecc0.svg)



```plain
\Large\LaTeX
```



![img](LateX.assets/7871fbdd01f5f3d83f6f52968e565f35.svg)



```plain
\sum_{i=0}^n i^2 = \frac{(n^2+n)(2n+1)}{6}
```



![img](LateX.assets/26e3580eb6e4a00386288cffa33ee230.svg)



```plain
\Biggl(\biggl(\Bigl(\bigl((egg)\bigr)\Bigr)\biggr)\Biggr)
```



## 字体 | Fonts



![img](LateX.assets/76bfdd44b4462fc83f6df6bd5caf75b5.svg)



```plain
\mathbb{CHNQRZ}
```



![img](LateX.assets/89c8dd2a3243e56c6eb3f8b6946f4a5f.svg)



```plain
\mathbf{ABCDEFGHIJKLMNOPQRSTUVWXYZ}
```



![img](LateX.assets/5c1658a12e12504bf026440399274e9f.svg)



```plain
\mathbf{abcdefghijklmnopqrstuvwxyz}
```



![img](LateX.assets/7f91e225dd6f81637cb164f4aa1be4b0.svg)



```plain
\mathit{ABCDEFGHIJKLMNOPQRSTUVWXYZ}
```



![img](LateX.assets/4786243531d61999563fbfa96344b27e.svg)



```plain
\mathit{abcdefghijklmnopqrstuvwxyz}
```



![img](LateX.assets/5cbbf93dcd322cd84999804f49a34ad6.svg)



```plain
\pmb{ABCDEFGHIJKLMNOPQRSTUVWXYZ,abcdefghijklmnopqrstuvwxyz}
```



![img](LateX.assets/1e2730ec97e766e69021ea7a35f12fa4.svg)



```plain
\mathtt{ABCDEFGHIJKLMNOPQRSTUVWXYZ,abcdefghijklmnopqrstuvwxyz}
```



![img](LateX.assets/e510e69062aafe193fa26b698136d1a2.svg)



```plain
\mathrm{ABCDEFGHIJKLMNOPQRSTUVWXYZ,abcdefghijklmnopqrstuvwxyz}
```



![img](LateX.assets/667517be869e27c6e5e02de4898d2fc4.svg)



```plain
\mathsf{ABCDEFGHIJKLMNOPQRSTUVWXYZ,abcdefghijklmnopqrstuvwxyz}
```



![img](LateX.assets/3ad20e142c563b91717b87a9928709a0.svg)



```plain
\mathcal{ABCDEFGHIJKLMNOPQRSTUVWXYZ,abcdefghijklmnopqrstuvwxyz}
```



![img](LateX.assets/88716451d45405807b13639caecbf4c2.svg)



```plain
\mathscr{ABCDEFGHIJKLMNOPQRSTUVWXYZ,abcdefghijklmnopqrstuvwxyz}
```



![img](LateX.assets/f27a6596b9bda11bc3d503274c63ecf1.svg)



```plain
\mathfrak{ABCDEFGHIJKLMNOPQRSTUVWXYZ} \mathfrak{abcdefghijklmnopqrstuvwxyz}
```





## 