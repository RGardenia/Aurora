# 一、SpringMVC流程

​	SpringMVC 是⼀个基于 MVC 架构的⽤来简化 web 应⽤程序开发框架，它是 Spring 的 ⼀个模块，⽆需中间整合层来整合，它和 Struts2 ⼀样都属于表现层的框架，在 web 模型中， MVC 是⼀种很流⾏的框架，把 Model，View，Controller 分离，把较为复杂的 web 应⽤分为 逻辑清晰的⼏部分，简化开发，减少出错，⽅便组内开发⼈员之间的配合。

## 🌐 **SpringMVC 核心流程**

1. **用户请求 → DispatcherServlet**
2. DispatcherServlet 调用 **HandlerMapping** 查找 Handler
3. 根据 URL 匹配对应的 **Controller 方法**（Handler）
4. 执行 **HandlerInterceptor**（拦截器）
5. **HandlerAdapter** 调用 Controller
6. Controller 执行，返回 **ModelAndView**
7. DispatcherServlet 调用 **ViewResolver** 解析视图
8. 渲染视图 → 填充数据 → 返回给浏览器

> 

## 🧩 **SpringMVC 核心概念**

| 概念                 | 作用                           |
| -------------------- | ------------------------------ |
| `@Controller`        | 标识控制器类                   |
| `@RequestMapping`    | 映射请求路径，可加到类和方法上 |
| `@ResponseBody`      | 返回 JSON 数据                 |
| `@RequestBody`       | 接收前端传入 JSON → Java 对象  |
| `@SessionAttributes` | 将 Model 中数据放入 Session    |
| **ModelAndView**     | 包含视图名和模型数据           |
| **ModelMap/Model**   | 向前台传递数据的载体           |

## 🚦 **请求转发 vs 重定向**

| 方式                | 用法示例                  | 特点                                                         |
| ------------------- | ------------------------- | ------------------------------------------------------------ |
| **转发 forward**    | `return "forward:/path"`  | URL 不变，服务器内部跳转                                     |
| **重定向 redirect** | `return "redirect:/path"` | URL 变，浏览器重新发起请求，常用于跳转到外部或避免表单重复提交 |

## 🛡️ **SpringMVC 拦截器**

- 实现 HandlerInterceptor 接口
  - `preHandle()`：请求前拦截
  - `postHandle()`：请求后拦截
  - `afterCompletion()`：视图渲染后
- 配置方式：在 `springmvc.xml` 中注册 `<mvc:interceptors>`

## ⚠️**异常处理**

- 实现 **HandlerExceptionResolver** 或使用 **@ControllerAdvice + @ExceptionHandler**
- 全局异常处理，返回自定义错误页面或 JSON 结构

## 🏷️ **常见注解**

| 注解                 | 功能                                   |
| -------------------- | -------------------------------------- |
| `@RequestMapping`    | URL 映射                               |
| `@ResponseBody`      | 返回 JSON 数据                         |
| `@RequestBody`       | 接收 JSON 请求体                       |
| `@SessionAttributes` | 将 model 数据同步到 Session            |
| `@ControllerAdvice`  | 全局异常、数据绑定、参数处理器统一管理 |

## 🔄 **参数绑定 & 数据传递**

| 需求                            | 解决方法                                            |
| ------------------------------- | --------------------------------------------------- |
| 获取请求参数                    | 方法参数中直接声明参数名，自动注入                  |
| 多参数对应一个对象              | 方法参数中声明对象，SpringMVC 自动封装              |
| 获取 Request、Session、Response | 直接在方法参数声明：`HttpServletRequest request` 等 |
| 返回数据到前端                  | `ModelMap` / `Model` / `ModelAndView`               |







## 1. DispatcherServlet 结构图

![DispatcherServlet继承图](images/DispatcherServlet%E7%BB%93%E6%9E%84.6us8563buow0.png)

注：`HttpServlet、HttpServletBean、FrameworkServlet` 都是抽象类

客户端向服务器发送请求，首先会来到 `HttpServlet` 的 `doGet()/doPost()`

`FrameworkServlet` 一路继承下来, 表示它也是一个 `HttpServlet`  并且重写了 `doGet/doPost()`

```java
public abstract class FrameworkServlet extends HttpServletBean 
    implements ApplicationContextAware {

    // 重写了 doGet() 方法
    @Override
    protected final void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        processRequest(request, response);
    }

    // 重写了 doPost() 方法
    @Override
    protected final void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        processRequest(request, response);
    }
}
```

可以从重写的方法中发现都调用了 `processRequest(request, response)` 。

```java
public abstract class FrameworkServlet extends HttpServletBean 
    implements ApplicationContextAware {

    protected final void processRequest(HttpServletRequest request, 
                                        HttpServletResponse response) 
        throws ServletException, IOException {
        // 省略...
        try {
            // 核心方法
            doService(request, response);
        } catch (e) {
            // ...
        } 
    }
}
```

`doService()`方法是 `FrameworkServlet` 中的抽象方法。

```java
public abstract class FrameworkServlet extends HttpServletBean 
    implements ApplicationContextAware {

    // 抽象方法
    protected abstract void doService(HttpServletRequest request, 
                                      HttpServletResponse response) throws Exception;
}
```

`FrameServlet`的子类必然要实现 `doService()` 这个抽象方法。

`DispatcherServlet` 中实现了 `doService()` 方法。

```java
public class DispatcherServlet extends FrameworkServlet {
    @Override
    protected void doService(HttpServletRequest request, 
                             HttpServletResponse response) throws Exception {
        // 省略...
        try {
            // 核心方法
            doDispatch(request, response);
        }
        finally {
            // ...
        }
    }
}
```

`DispatcherServlet` 实现的 `doService()` 核心处理逻辑就是 `doDispatch(request, response)`。

只要了解 `doDispatch(request, response)` ，就能明白 SpringMVC 的处理流程了。



## 2. 请求的大致流程

```java
protected void doDispatch(HttpServletRequest request, 
                          HttpServletResponse response) throws Exception {
    
    // 1：检查是否是文件上传请求
    HttpServletRequest processedRequest = checkMultipart(request);
    boolean multipartRequestParsed = (processedRequest != request);
    
    // 2：确定当前请求用哪个处理器（controller）
    // Determine handler for the current request.
    HandlerExecutionChain mappedHandler = getHandler(processedRequest);
    
    // 如果当前请求没有找到匹配的 controller 就抛出异常/错误页面
    if (mappedHandler == null) {
        noHandlerFound(processedRequest, response);
        return;
    }
    
    // 3: 拿到 处理器 的适配器（反射工具）
    // Determine handler adapter for the current request.
    HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
    
    // 4: 核心逻辑1：调用我们的 controller 执行目标方法
    // 方法执行结束后, 会封装成 ModelAndView
    // Actually invoke the handler.
    ModelAndView mv = 
        ha.handle(processedRequest, response, mappedHandler.getHandler());
    
    // 核心逻辑2：页面渲染
    processDispatchResult(processedRequest, response, 
                          mappedHandler, mv, dispatchException);
}
```

1. 所有请求过来，`DispatcherServlet` 收到请求。
2. 调用 `doDispatch()` 方法进行处理。
   1. `getHandler(processedRequest)`：根据当前请求在 HandlerMapping 中获得到目标处理器类。
   2. `getHandlerAdapter(mappedHandler.getHandler())`：根据当前处理器类找到目标处理器的适配器（反射工具）。
   3. 使用上一步获得 Adapter（适配器） 执行目标方法。
   4. 目标方法执行后会返回 `ModelAndView` 对象。
   5. 根据 `ModelAndView` 的信息转发到具体的页面，并可以在请求域中取出 `ModelAndView` 中的模型数据。

# 二、具体流程

## 1. getHandler()

根据 HttpServletRequest 找到目标处理器（controller）。

```java
protected HandlerExecutionChain getHandler(HttpServletRequest request) 
    throws Exception {
    if (this.handlerMappings != null) {
        for (HandlerMapping mapping : this.handlerMappings) {
            HandlerExecutionChain handler = mapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
    }
    return null;
}
```

`HandlerMapping`：处理器映射，它里面保存了每一个处理器能处理哪些方法的映射信息。

![](https://cdn.jsdelivr.net/gh/RingoTangs/image-hosting@master/spring5/image.5md9oxz15b80.png)

HandlerMapping ==> HandlerExecutionChain ==> handler（目标处理器）。封装了3层。



## 2. getHandlerAdapter()

根据 目标处理器（controller）拿到目标方法的适配器，去调用目标方法。

```java
protected HandlerAdapter getHandlerAdapter(Object handler) 
    throws ServletException {
    if (this.handlerAdapters != null) {
        for (HandlerAdapter adapter : this.handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
    }
    throw new ServletException("No adapter for handler [" + handler +
                               "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
}
```

![](https://cdn.jsdelivr.net/gh/RingoTangs/image-hosting@master/spring5/image.7fx22m1rpaw0.png)

遍历所有的 `HandlerAdapter`，拿到 目标处理器（Controller）的适配器。

拿到的适配器是 `RequestMappingHandlerAdapter`。



## 3. 执行目标方法





# 三、SpringMVC 九大组件

## 1. 组件名称

```java
// 以下是 DispatcherServlet 中的九个属性。
// 这就是 SpringMVC 的九大组件。
// SpringMVC 在工作的时候，关键位置都是由这些组件完成的。
// 共同点：九大组件全部都是接口，接口就是规范！

// 1: 文件上传解析器
@Nullable
private MultipartResolver multipartResolver;

// 2: 区域信息解析器, 和国际化相关
@Nullable
private LocaleResolver localeResolver;

// 3: 主题解析器：主题效果更换
@Nullable
private ThemeResolver themeResolver;

// 4: Handler映射
@Nullable
private List<HandlerMapping> handlerMappings;

// 5: Handler的适配器
@Nullable
private List<HandlerAdapter> handlerAdapters;

// 6: SpringMVC异常解析器
@Nullable
private List<HandlerExceptionResolver> handlerExceptionResolvers;

// 7: 请求到视图名字转换器
@Nullable
private RequestToViewNameTranslator viewNameTranslator;

// 8: SpringMVC中运行重定向携带数据的功能
@Nullable
private FlashMapManager flashMapManager;

// 9: 视图解析器
@Nullable
private List<ViewResolver> viewResolvers;
```



## 2. 组件初始化

```java
public class DispatcherServlet extends FrameworkServlet {
    @Override
    protected void onRefresh(ApplicationContext context) {
        initStrategies(context);
    }

    // SpringMVC 九大组件初始化
    protected void initStrategies(ApplicationContext context) {
        initMultipartResolver(context);
        initLocaleResolver(context);
        initThemeResolver(context);
        initHandlerMappings(context);
        initHandlerAdapters(context);
        initHandlerExceptionResolvers(context);
        initRequestToViewNameTranslator(context);
        initViewResolvers(context);
        initFlashMapManager(context);
    }
}
```

`HandleringMapping`的初始化流程：

```java
private void initHandlerMappings(ApplicationContext context) {
    this.handlerMappings = null;

    // detectAllHandlerMappings 默认是 true
    // 1: 在容器里找
    if (this.detectAllHandlerMappings) {
        Map<String, HandlerMapping> matchingBeans =
            BeanFactoryUtils
            .beansOfTypeIncludingAncestors(context,HandlerMapping.class, true, false);
        if (!matchingBeans.isEmpty()) {
            this.handlerMappings = new ArrayList<>(matchingBeans.values());
            // We keep HandlerMappings in sorted order.
            AnnotationAwareOrderComparator.sort(this.handlerMappings);
        }
    }
    else {
        try {
            HandlerMapping hm = context.getBean(HANDLER_MAPPING_BEAN_NAME, HandlerMapping.class);
            this.handlerMappings = Collections.singletonList(hm);
        }
        catch (NoSuchBeanDefinitionException ex) {
            // Ignore, we'll add a default HandlerMapping later.
        }
    }

    // 2: 容器里找不到就使用默认配置
    if (this.handlerMappings == null) {
        // 重点方法
        this.handlerMappings = 
            getDefaultStrategies(context, HandlerMapping.class);
        if (logger.isTraceEnabled()) {
            logger.trace("No HandlerMappings declared for servlet '" 
                         + getServletName() +
                         "': using default strategies from DispatcherServlet.properties");
        }
    }
}
```

`getDefaultStrategies(context, HandlerMapping.class)`实际上是读取默认配置文件







