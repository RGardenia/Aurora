# Volatile

​	`volatile` 是Java提供的一种轻量级的同步机制（可以理解为轻量级锁）。Java 语言包含两种内在的同步机制：同步块（或方法）和 volatile 变量，相比于 synchronized，volatile 更轻量级，因为它不会引起线程上下文的切换和调度。但是 `volatile` 变量的同步性较差，无法保证操作的原子性。


## 一、volatile 的特性
​        `volatile` 的原理就是对特性的解释。同时说 `volatile` 之前，我们先要了解多线程的三大特性：有序性、可见性和原子性（以前已经讲解过可以点击链接进行跳转查看）。这三点是进行多线程开发时首要考虑的问题。而 `volatile` 主要解决的就是可见性问题。 `volatile` 的俩大特性：

### 1、保证可见性

​    当写一个 `volatile` 变量时，JMM 会把该线程本地内存中的变量强制刷新到主内存中去，这个写会操作会导致其他线程中的 `volatile` 变量缓存无效。

   下面通过代码对比 `volatile` 可见性对程序的影响

```java
/**
 * volatile 关键字，使一个变量在多个线程间可见
 * A B线程都用到一个变量，java默认是A线程中保留一份copy，这样如果B线程修改了该变量，则A线程未必知道
 * 使用volatile关键字，会让所有线程都会读到变量的修改值
 * 
 * 在下面的代码中，running是存在于堆内存的t对象中
 * 当线程t1开始运行的时候，会把running值从内存中读到t1线程的工作区，在运行过程中直接使用这个copy，并不会每次都去
 * 读取堆内存，这样，当主线程修改running的值之后，t1线程感知不到，所以不会停止运行
 * 
 * 使用volatile，将会强制所有线程都去堆内存中读取running的值
 * 
 * 可以阅读这篇文章进行更深入的理解
 * http://www.cnblogs.com/nexiyi/p/java_memory_model_and_thread.html
 * 
 * volatile并不能保证多个线程共同修改running变量时所带来的不一致问题，也就是说volatile不能替代synchronized
 * @author mashibing
 */
import java.util.concurrent.TimeUnit;
 
public class T01_HelloVolatile {
	/*volatile*/ boolean running = true; //对比一下有无volatile的情况下，整个程序运行结果的区别
	void m() {
		System.out.println("m start");
		// 模拟服务器直接宕机或者人工关闭
		while(running) {}
		System.out.println("m end!");
	}
	
	public static void main(String[] args) {
		T01_HelloVolatile t = new T01_HelloVolatile();
		new Thread(t::m, "t1").start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.running = false;
	}
}
```

>  注意： `volatile` 修饰引用类型（包括数组）时，只能保证引用本身的可见性，不能保证内部数据的可见性。
>
> ```java
> /**
>  * volatile 引用类型（包括数组）只能保证引用本身的可见性，不能保证内部字段的可见性
>  */
> import java.util.concurrent.TimeUnit;
>  
> public class TestVolatileReference1 {
>     boolean running = true;
>     volatile static TestVolatileReference1 T = new TestVolatileReference1();
>     void m() {
>         System.out.println("m start");
>         while(running) {
> 			/*
> 			try {
> 				TimeUnit.MILLISECONDS.sleep(10);
> 			} catch (InterruptedException e) {
> 				e.printStackTrace();
> 			}*/
>         }
>         System.out.println("m end!");
>     }
>  
>     public static void main(String[] args) {
>         //lambda表达式 new Thread(new Runnable( run() {m()}
>         new Thread(T::m, "t1").start();
>  
>         try {
>             TimeUnit.SECONDS.sleep(1);
>         } catch (InterruptedException e) {
>             e.printStackTrace();
>         }
>         // 观察false是否起作用
>         T.running = false;
>     }
> }
> ```
>
> 

### 2、禁止指令重排序

​    重排序是指编译器和处理器为了优化程序性能而对指令序列进行排序的一种手段。同时重排序也需要遵守一定规则：

​	1.重排序操作不会对存在数据依赖关系的操作进行重排序

​	2.重排序是为了优化性能，但是不管怎么重排序，单线程下程序的执行结果不能被改变

> 其实指令重排序主要了解实现原理以及运用就行，原理下边讲解，运用的话主要就是在**单例模式中的双重检查锁（基本就是在面试中问一下，实际从来没用过，单例饿汉式就很常用）**。

**1）**双重检查单例模式
        主要是因为 new 一个对象的操作是非原子性的，对象的创建过程在java字节码中主要分为如下4个步骤，这4个步骤后两个有可能会重排序，1234 1243 都有可能，造成未初始化完全的对象发布。volatile可以禁止指令重排序，从而避免这个问题。

> 1、申请内存空间，
> 2、初始化默认值（区别于构造器方法的初始化），
> 3、执行构造器方法
> 4、连接引用和实例。

```java
public class Singleton{   
    // 静态属性，volatile保证可见性和禁止指令重排序，这里主要是禁止重排序
    private volatile static Singleton instance = null; 
    // 私有化构造器  
    private Singleton(){}   
 
    public static  Singleton getInstance(){   
        // 第一重检查锁定，减少线程对同步锁锁的竞争
        if(instance==null){  
            // 同步锁定代码块 
            synchronized(Singleton.class){
                // 第二重检查锁定，保证单例
                if(instance==null){
                    // 注意：非原子操作
                    instance=new Singleton(); 
                }
            }              
        }   
        return instance;   
    }   
}
```

### 3、不保证原子性

​     `volatile` 是不保证原子性，这是要注意的一点，就是在进行一些复合操作的时候要特别注意，比如；`count++` 操作。代码对比 `volatile` 和`synchronized` 对多线程原子性的影响：
```java
/**
 * volatile并不能保证多个线程共同修改running变量时所带来的不一致问题，也就是说volatile不能替代synchronized
 * 运行下面的程序，并分析结果
 */
import java.util.ArrayList;
import java.util.List;
 
public class T04_VolatileNotSync {
	volatile int count = 0;
	//volatile保证了count的可见性，但是无法保证count++的原子性，比如：线程1修改count为1，线程2，线程3都读取到count=1，同时修改此时少++一次
	void m() {
		for(int i=0; i<10000; i++) count++;
	}
	public static void main(String[] args) {
		T04_VolatileNotSync t = new T04_VolatileNotSync();
		
		List<Thread> threads = new ArrayList<Thread>();
		
		for(int i=0; i<10; i++) {
			threads.add(new Thread(t::m, "thread-"+i));
		}
		
		threads.forEach((o)->o.start());
		
		threads.forEach((o)->{
			try {
				o.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		System.out.println(t.count);
	}
}

/**
 * 对比上一个程序，可以用synchronized解决，synchronized可以保证可见性和原子性，volatile只能保证可见性
 */
import java.util.ArrayList;
import java.util.List;
 
 
public class T05_VolatileVsSync {
	/*volatile*/ int count = 0;
 
	synchronized void m() { 
		for (int i = 0; i < 10000; i++)
			count++;
	}
	public static void main(String[] args) {
		T05_VolatileVsSync t = new T05_VolatileVsSync();
 
		List<Thread> threads = new ArrayList<Thread>();
 
		for (int i = 0; i < 10; i++) {
			threads.add(new Thread(t::m, "thread-" + i));
		}
 
		threads.forEach((o) -> o.start());
 
		threads.forEach((o) -> {
			try {
				o.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		System.out.println(t.count);
	}
}
```

## 二、volatile 的原理
​        一：可见性方面，使用的是缓存一致性协议

​	二：禁止指令重排序，从 JVM 底层进行理解

### 1. 可见性
​	为了提高处理器的执行速度，在处理器和内存之间增加了多级缓存来提升。但是由于引入了多级缓存，就存在缓存数据不一致问题。此时为了保证缓存数据的一致性，就要引入缓存一致性协议了。如果一个变量被 `volatile` 所修饰的话，在每次数据变化之后，其值都会被强制刷入主存。而其他处理器的缓存由于遵守了缓存一致性协议，也会把这个变量的值从主存加载到自己的缓存中。这就保证了一个 `volatile` 在并发编程中，其值在多个缓存中是可见的。

> 缓存一致性协议：每个处理器通过嗅探在总线上传播的数据来检查自己缓存的值是不是过期了，当处理器发现自己缓存行对应的内存地址被修改，就会将当前处理器的缓存行设置成无效状态，当处理器要对这个数据进行修改操作的时候，会强制重新从系统内存里把数据读到处理器缓存里。

### 2. 禁止指令重排序

​	在JVM底层 `volatile` 是采用“内存屏障”来实现的。观察加入 `volatile` 关键字和没有加入 `volatile` 关键字时所生成的汇编代码发现（直接使用反编译工具，查看反编译代码），加入 `volatile` 关键字时，会多出一个 `lock` 前缀指令，`lock`  前缀指令实际上相当于一个内存屏障（也成内存栅栏），内存屏障会提供3个功能：

​	（1）它确保指令重排序时不会把其后面的指令排到内存屏障之前的位置，也不会把前面的指令排到内存屏障的后面；即在执行到内存屏障这句指令时，在它前面的操作已经全部完成；

​	（2）它会强制将对缓存的修改操作立即写入主存；

​	（3）如果是写操作，它会导致其他CPU中对应的缓存行无效。