# JWT

**`JWT` 全称：** Json Web Token 
**作用：** JWT 的作用是 **用户授权(Authorization)** ，而不是用户的**身份认证(Authentication)** 
**用户认证** 指的是使用用户名、密码来验证当前用户的身份，即用户登录
**用户授权** 指用户登录成功后，当前用户有足够的权限访问特定的资源

> **传统的 `Session` 登录：**
>
> - 用户登录后，服务器会保存登录的 `Session` 信息
> - `Session ID` 会通过 `cookie` 传递给前端
> - `http` 请求会附带 `cookie`
>   这种登录方式称为 **有状态登录** 













# Cookie

> 在前端面试中，cookies是经常被提及的内容，基本每家公司都会有涉及。如果经常访问国外的一些网站的话，会遇到弹出获取cookies的对话框。由此可见cookies在 实际应用中是无比重要的！！！



Cookies是服务器发送给用户浏览器的一块保存在本地的数据。当用户下一次访问服务器的时候，会被发送到服务器。客户端Cookies的存储大小为4k。 
(Cookie 是保存在本地（客户端）的一种小型文件！区别于Session，其保存在服务器端！)

## 作用

1. 用来进行会话状态管理，例如记录用户的登录状态
2. 用来记录跟踪用户的行为

## 常用属性

### Expires/Max-Age
- 主要是用来设定Cookies的有效期

- Expires用于执行具体的过期事件

例如在Node做为服务端，我们可以使用：

```js
res.setHeader('Set-Cookie', [
  `name=keliq; expires=${new Date(Date.now() + 36000 ).toGMTString()}`,
])
```

Max-Age 以秒为单位设置多少秒之后过期：

```js
res.setHeader('Set-Cookie', ['name=picker; max-age=10;'])
```

如果同时设置Expires和Max-Age，则Max-Age生效。

###  Secure

当Secure 设置为true时，只要服务器使用SSL和HTTPS 时，客户端才能收到Cookies

```js
res.setHeader('Set-Cookie', ['Secure-true;'])
```

### HttpOnly
当HttpOnly设置为true时，客服端无法使用JavaScript来获取Cookies。

```js
res.setHeader('Set-Cookie', ['httpOnly=true;',])
```

## JavaScript设置cookie
### 设定/修改
```js
document.cookie=“name=123”;
```

注意：

```js
document.cookie="name=syl; age=18"
```

这样的话只有name生效，即每次只能设置一个。因此需要封装或者多次调用。

## 封装
### 设置

```js
//设置cookies
function setCookie(name,value,MyDay){
    var ExDate = new Date();
    ExDate.setDate(ExDate.getDate() + MyDay);//如果需要时间的话以这样获取。
    document.cookie = name + "=" + value + ";expires=" + ExDate;
}
```

### 获取
```js
//获取cookies
function getCookie(name){
    //例如cookie是"username=abc; password=123"
    var arr = document.cookie.split('; ');//用“;”和空格来划分cookie
    for(var i = 0 ;i < arr.length ; i++){
        var arr2 = arr[i].split("=");
        if(arr2[0] == name){
            return arr2[1];
        }
    }
    return "";//整个遍历完没找到，就返回空值
}
```

### 删除
```js
//删除cookies
function removeCookie(name){
    setCookie(name, "1", -1)//第二个value值随便设个值，第三个值设为-1表示：已经过期。
}
```



## 缺点
以下缺点，来自百科

Cookie在某种程度上说已经严重危及用户的隐私和安全。其中的一种方法是：一些公司的高层人员为了某种目的（譬如市场调研）而访问了从未去过的网站（通过搜索引擎查到的），而这些网站包含了一种叫做网页臭虫的图片，该图片透明，且只有一个像素大小（以便隐藏），它们的作用是将所有访问过此页面的计算机写入Cookie。而后，电子商务网站将读取这些Cookie信息，并寻找写入这些Cookie的网站，随即发送包含了针对这个网站的相关产品广告的垃圾邮件给这些高级人员。

虽然Cookie没有中电脑病毒那么危险，但它仍包含了一些敏感消息：用户名、电脑名、使用的浏览器和曾经访问的网站。用户不希望这些内容泄漏出去，尤其是当其中还包含有私人信息的时候。

这并非危言耸听，跨站点脚本（Cross site scripting）可以达到此目的。在受到跨站点脚本攻击时，Cookie盗贼和Cookie投毒将窃取内容。一旦Cookie落入攻击者手中，它将会重现其价值。

Cookie盗贼：搜集用户Cookie并发给攻击者的黑客，攻击者将利用Cookie消息通过合法手段进入用户帐户。
Cookie投毒：一般认为，Cookie在储存和传回服务器期间没有被修改过，而攻击者会在Cookie送回服务器之前对其进行修改，达到自己的目的。例如，在一个购物网站的Cookie中包含了顾客应付的款项，攻击者将该值改小，达到少付款的目的。

