# 客户端 QAS





# php如何设置cookie对整个域名有效

默认情况下的cookie仅对该域名（例如www.example.com）本身有效，出了该域名（例如test.example.com），cookie便会失效，但是可以通过setcookie函数让cookie对整个域名（*.example.com）有效。

```
setcookie("cookie_test", 'this is cookie test', time()+3600, "/", "example.com");
```

由于域名相关参数在第五个，所以，前面的参数都需要设置，第一个参数是cookie名称，第二个参数是cookie值，第三个参数是cookie过期时间，这几个参数一般都是常用参数，这里就不在过多进行介绍了。

这里需要重点说明的是第四个参数，这个参数用于设置cookie的有效路径，如果设置为/，则对所有目录有效，如果设置为/test/，仅对/test/以及其子目录/test/tmp/有效。一般情况下会设置为/，对整个目录有效。

接着便是第五个参数，也就是我们今天的主角。第五个参数便是规定cookie生效域名，一般情况下，cookie只会在当前域名下生效，例如test.example.com下的cookie不会在www.example.com生效，但是我们可以通过设置，让cookie在同一主域名下通用，只需要把这个参数设置成主域名即可（www.example.com同样是子域名，不要搞混）。

第四个参数和第五个参数都可以控制cookie的生效范围，可以根据实际情况下设置。