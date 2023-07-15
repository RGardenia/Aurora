# 一、SpringMVC流程

是⼀个基于 [MVC](https://so.csdn.net/so/search?q=MVC&spm=1001.2101.3001.7020) 架构的⽤来简化 web 应⽤程序开发框架，它是 Spring 的 ⼀个模块，⽆需中间整合层来整合，它和 Struts2 ⼀样都属于表现层的框架，在 web 模型中， MVC 是⼀种很流⾏的框架，把 Model，View，Controller 分离，把较为复杂的 web 应⽤分为 逻辑清晰的⼏部分，简化开发，减少出错，⽅便组内开发⼈员之间的配合。



**SpringMVC 的流程**
1.⽤户发送请求 ⾄前端控制器 DispatcherServlet 。
\2. DispatcherServlet 收 到 请 求 后 ， 调 ⽤HandlerMapping 处理器映射器，请求获取 Handle。
\3. 处理器映射器根据请求 url 找到具体的处理器，⽣成处理器对象及处理器拦截器(如果则⽣成 并返回给 DispatcherServlet)。
\4. 执⾏处理器(Handler，也叫后端控制器)。
\5. Handler 执⾏完成返回 ModelAndView。
\6. HandlerAdapter 将Handler 执 ⾏结 果 ModelAndView 返回给 DispatcherServlet 。
\7. DispatcherServlet 将ModelAndView 传给 ViewResolver 视图解析器进⾏解析。
\8. ViewResolver 解析后返回具体View。
\9. DispatcherServlet 对 View 进⾏渲染视图(即将模型数据填充⾄视图中)。
\10. DispatcherServlet 响应⽤户。

**SpringMVC 的工作原理**
a.用户向服务器发送请求，请求被 [springMVC](https://so.csdn.net/so/search?q=springMVC&spm=1001.2101.3001.7020) 前端控制器 DispatchServlet 捕获；
b.DispatcherServle 对请求 URL 进行解析，得到请求资源标识符（URL），然后根据该 URL 调用HandlerMapping将请求映射到处理器 HandlerExcutionChain；
c.DispatchServlet 根据获得 Handler 选择一个合适的 HandlerAdapter 适配器处理；
d.Handler 对数据处理完成以后将返回一个 ModelAndView（）对象给 DisPatchServlet;
e.Handler 返回的 ModelAndView()只是一个逻辑视图并不是一个正式的视图DispatcherSevlet 通过ViewResolver 试图解析器将逻辑视图转化为真正的视图 View;
h.DispatcherServle 通过 [model](https://so.csdn.net/so/search?q=model&spm=1001.2101.3001.7020) 解析出 ModelAndView()中的参数进行解析最终展现出完整的 view并返回给客户端;

**SpringMVC 如何设定重定向和转发的？**
在返回值前⾯ forward,就可以让结果转发，譬如 forward:user.do?name=method4
在返回值前⾯ redirect，就可以让返回值重定向，譬如 redirect：http://www.baidu.com

**SpringMVC 里面拦截器如何写？**
有两种写法，⼀种是实现 HandlerInterceptor 接⼝，另⼀种是继承适配器类，接着在接⼝⽅法 当中实现处理逻辑，然后在 SpringMVC 的配置⽂件中配置拦截器即可

**SpringMVC 的异常处理**
可以将异常抛给 Spring [框架](https://so.csdn.net/so/search?q=框架&spm=1001.2101.3001.7020)，由 Spring 的 AOP 来处理，我们只需要配置简单的异常处理器
在异常处理器中添加视图⻚⾯即可。

**SpringMVC 的控制器是不是单例模式，如果是，有什么问题，如何解决。**
SpringMVC 的控制器是单例模式，所以在多线程访问的时候有线程安全问题，不要⽤同步， 会
影响性能的，解决⽅案是在控制器⾥⾯不能写字段。

**SpringMVC 的控制器的注解⼀般⽤那个，有没有别的注解可以替代？**
⼀般⽤@Controller,表示表现层，不能⽤别的注解替代。

**SpringMVC 的@RequestMapping 注解⽤在类上⾯有什么作⽤？**
是⼀个⽤来处理请求地址映射的注解，可以⽤于类或⽅法上，表示类中的所有响应请求的⽅ 法都
是以该路径作为⽗路径。

**SpringMVC 如何把某个请求映射到特定的⽅法上⾯？**
直接在⽅法上⾯加上注解@RequestMapping,并且在这个注解⾥⾯写上要拦截的路径。

**SpringMVC 如果想在拦截的⽅法⾥⾯得到从前台传⼊的参数，如何得到？**
直接在⽅法中声明这个对象，SpringMvc 就⾃动会把属性赋值到这个对象⾥⾯。

**SpringMVC 中的函数的返回值是什么？**
返回值可以有很多类型，有 String，ModelAndview，Model

**SpringMVC ⽤什么对象从后台向前台传递数据的？**
通过 ModelMap 对象，可以在这个对象⾥⾯⽤ put ⽅法，把对象加到⾥⾯，前台就可以通过 el 表
达式拿到。

**SpringMVC 中有个类把视图和数据合并在⼀起，叫什么？**
ModelAndview

**SpringMVC 中怎么把 ModelMap ⾥⾯的数据放⼊ Session ⾥⾯？**
可以在类上⾯加上@SessionAttributes 注解，⾥⾯包含的字符串就是要放⼊ Session ⾥⾯的 key。

**SpringMVC 如何在⽅法⾥⾯得到 Request 或者 Session？**
直接在⽅法的形参中声明 Request，SpringMvc 就⾃动把 request 对象传⼊。获取 Session，也 是
同 样 的 ⽅ 法 ，但 是 需 要 在 ⽅ 法 中 获 取 request 中 的 Session ，例 如 ：Session
session=request.getSession();即可，获取 Response 也是需要在⽅法的形参中声明 Response。

**SpringMVC 常用注解都有哪些？**
@requestMapping 用于请求 url 映射。
@RequestBody 注解实现接收 http 请求的 json 数据，将 json 数据转换为 java 对象。
@ResponseBody 注解实现将 controller 方法返回对象转化为 json 响应给客户。

**如何开启注解处理器和适配器？**
我们在项目中一般会在 springmvc.xml 中通过开启<mvc：annotation-driven>来实现注解处
理器和适配器的开启。

**SpringMvc 怎么和 AJAX 相互调用的？**
通过 Jackson 框架就可以把 Java 里面的对象直接转化成 Js 可以识别的 Json 对象。具体步骤如下 ：
（1）加入 Jackson.jar
（2）在配置文件中配置 json 的映射
（3）在接受 Ajax 方法里面可以直接返回 Object,List 等,但方法前面要加上@ResponseBody 注解。

**如果在拦截请求中,我想拦截 get 方式提交的方法,怎么配置？**
答：可以在@RequestMapping 注解里面加上 method=RequestMethod.GET

**如果前台有很多个参数传入,并且这些参数都是一个对象的,那么怎么样快速得到这个对象？**
答：直接在方法中声明这个对象,SpringMvc 就自动会把属性赋值到这个对象里面

**当一个方法向 AJAX 返回特殊对象,譬如 Object,List 等,需要做什么处理？**
答：要加上@ResponseBody 注解。







## 1. DispatcherServlet结构图

![DispatcherServlet继承图](https://cdn.jsdelivr.net/gh/RingoTangs/image-hosting@master/spring5/DispatcherServlet结构.6us8563buow0.png)

注：`HttpServlet、HttpServletBean、FrameworkServlet` 都是抽象类。

客户端向服务器发送请求，首先会来到 `HttpServlet` 的 `doGet()/doPost()`。

`FrameworkServlet` 一路继承下来, 表示它也是一个 `HttpServlet`。并且重写了 `doGet/doPost()`。

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





# 三、SpringMVC九大组件

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

`getDefaultStrategies(context, HandlerMapping.class)`实际上是读取默认配置文件。

![SpringMVC默认配置文件](https://cdn.jsdelivr.net/gh/RingoTangs/image-hosting@master/spring5/image.2tddy3tm00a0.png)

