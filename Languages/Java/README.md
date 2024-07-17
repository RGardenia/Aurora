# Jarkata







## 魔法类 Unsafe

> ​	很多低级语言中可用的技巧在 Java 中都是不被允许的。Java 是一个安全的开发工具，它阻止开发人员犯很多低级的错误，而大部份的错误都是基于内存管理方面的。 JAVA 作为高级语言的重要创新一点就是在于 `JVM` 的内存管理功能，这完全区别于 C 语言开发过程中需要对变量的内存分配小心控制，JVM 很大程度解放了码农对于内存的调整.
>
> ​	一直以来，JAVA 在大多数人心目中没有办法对内存进行操作的，其实不然，Unsafe 类就是一把操作 `JAVA` 内存的钥匙。如果你想搞破坏，可以使用 Unsafe 这个类。这个类是属于 `sun.* API` 中的类 Unsafe 做操作的是直接内存区，所以该类没有办法通过 HotSpot 的 GC 进行回收，需要进行手动回收，因此在使用此类时需要注意内存泄漏（Memory Leak）和内存溢出（Out Of Memory）
>
> ​	因为这是一个平台相关的类，因此在实际开发中，建议不要使用。但是，为了更好地了解 Java 的生态体系，我们应该去学习它，去了解它，不求深入到底层的 C/C++ 代码，但求能了解它的基本功能。

1. 获取 Unsafe 的实例

```java
@CallerSensitive
public static Unsafe getUnsafe() {
    Class var0 = Reflection.getCallerClass();
    if (!VM.isSystemDomainLoader(var0.getClassLoader())) {
        throw new SecurityException("Unsafe");
    } else {
        return theUnsafe;
    }
}
```

> 直接调用这个方法会抛出一个SecurityException异常，这是因为Unsafe仅供java内部类使用，外部类不应该使用它。
>
> 有一个属性叫theUnsafe，直接通过反射拿到它即可。

```java
public class UnsafeTest {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);
    }
}
```

2. 内存操作案例

```java
public class UnsafeDemo {
    private int i = 0;
 
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        //获取Unsafe实例
        Field f = Unsafe.class.getDeclaredField("theUnsafe"); // Internal reference
        f.setAccessible(true);//设置为true，通过反射获取私有变量的时候，会忽略访问修饰符的检查  
        Unsafe unsafe = (Unsafe) f.get(null);
 
        //获取字段i在内存中偏移量
        long offset = unsafe.objectFieldOffset(UnsafeDemo.class.getDeclaredField("i"));
 
        //创建对象实例，设置字段的值
        UnsafeDemo unsafeDemo = new UnsafeDemo();
        unsafe.putInt(unsafeDemo, offset, 100);
 
        //打印结果   100
        System.out.println(unsafeDemo.i); 
    }
}
```

> 其中offset是表示的是 i 在内存中的偏移量
>
> ​	JVM的实现可以自由选择如何实现Java对象的“布局”，也就是在内存里Java对象的各个部分放在哪里，包括对象的实例字段和一些元数据之类。
>
> ​	sun.misc.Unsafe里关于对象字段访问的方法把对象布局抽象出来，它提供了objectFieldOffset()方法用于获取某个字段相对Java对象的“起始地址”的偏移量，也提供了getInt、getLong、getObject之类的方法可以使用前面获取的偏移量来访问某个Java对象的某个字段。
>
> ​	类似的，Unsafe也提供了putLong、putFloat、putDouble、putChar、putByte、putShort、putBoolean、以及putObject等方法给对应类型的变量赋值。并提供了相应的get方法。

3. 突破限制创建实例

​		通过`allocateInstance()`方法，你可以创建一个类的实例，但是却不需要调用它的构造函数、初使化代码、各种JVM安全检查以及其它的一些底层的东西。即使构造函数是私有，也可以通过这个方法创建它的实例。

```java
public class UnsafeDemo2 {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        //获取Unsafe实例
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);
 
        // 这将在不进行任何初始化的情况下创建player类的实例
        Player player = (Player) unsafe.allocateInstance(Player.class);
        //打印年龄
        System.out.println(player.getAge());
 
        //给Player未实例化对象 设置年龄
        player.setAge(45);
        //打印年龄
        System.out.println(player.getAge());
    }
}
 
class Player {
    private int age;
    private Player() {this.age = 50;}
    public int getAge() {return this.age;}
    public void setAge(int age) {this.age = age;}
}
```

4. 抛出 checked 异常

​		如果代码抛出了checked异常，要不就使用try...catch捕获它，要不就在方法签名上定义这个异常，但是，通过Unsafe可以抛出一个checked异常，同时却不用捕获或在方法签名上定义它。

```java
// 使用正常方式抛出IOException需要定义在方法签名上往外抛
public static void readFile() throws IOException {
    throw new IOException();
}
// 使用Unsafe抛出异常不需要定义在方法签名上往外抛
public static void readFileUnsafe() {
    unsafe.throwException(new IOException());
}
```

5. 使用堆外内存

​		如果进程在运行过程中JVM上的内存不足了，会导致频繁的进行GC。理想情况下，我们可以考虑使用堆外内存，这是一块不受JVM管理的。

​		使用`Unsafe`的`allocateMemory()`我们可以直接在堆外分配内存，这可能非常有用，但我们要记住，这个内存不受JVM管理，因此我们要调用`freeMemory()`方法手动释放它。

> 假设我们要在堆外创建一个巨大的int数组，我们可以使用allocateMemory()方法来实现，在构造方法中调用allocateMemory()分配内存，在使用完成后调用freeMemory()释放内存，代码如下：

```java
import sun.misc.Unsafe;
 
import java.lang.reflect.Field;
 
/**
 * @author Gardenia
 * @description 堆外创建一个巨大的int数组，进行数据存储、读取demo
 * @date 2020/5/20 17:34
 * Version 1.0
 **/
public class OffHeapArray {
    // 一个int等于4个字节
    private static final int INT = 4;
    private long size;
    private long address;
 
    private static Unsafe unsafe;
 
    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
 
    // 构造方法，分配内存
    public OffHeapArray(long size) {
        this.size = size;
        // 参数字节数
        address = unsafe.allocateMemory(size * INT);
    }
 
    // 获取指定索引处的元素
    public int get(long i) {
        return unsafe.getInt(address + i * INT);
    }
 
    // 设置指定索引处的元素
    public void set(long i, int value) {
        unsafe.putInt(address + i * INT, value);
    }
 
    // 元素个数
    public long size() {
        return size;
    }
 
    // 释放堆外内存
    public void freeMemory() {
        unsafe.freeMemory(address);
    }
 
    public static void main(String[] args) {
        OffHeapArray offHeapArray = new OffHeapArray(4);
        offHeapArray.set(0, 1);
        offHeapArray.set(1, 2);
        offHeapArray.set(2, 3);
        offHeapArray.set(3, 4);
        offHeapArray.set(2, 5); // 在索引2的位置重复放入元素
 
        int sum = 0;
        for (int i = 0; i < offHeapArray.size(); i++) {
            sum += offHeapArray.get(i);
        }
 
        // 打印12
        System.out.println(sum);
        //将内存释放回操作系统
        offHeapArray.freeMemory();
    }
}
```

6. CompareAndSwap 操作

​		JUC下面大量使用了CAS操作，它们的底层是调用的Unsafe的`CompareAndSwapXXX()`方法。这种方式广泛运用于无锁算法，与 java 中标准的悲观锁机制相比，它可以利用CAS处理器指令提供极大的加速。

​		比如，我们可以基于Unsafe的compareAndSwapInt()方法构建线程安全的计数器。

> 定义了一个volatile的字段count，以便对它的修改所有线程都可见，并在类加载的时候获取count在类中的偏移地址。
>
> 在`increment()`方法中，我们通过调用Unsafe的`compareAndSwapInt()`方法来尝试更新之前获取到的count的值，如果它没有被其它线程更新过，则更新成功，否则不断重试直到成功为止。

我们可以通过使用多个线程来测试我们的代码：

```java
import sun.misc.Unsafe;
 
import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
 
/**
 * @author Gardenia
 * @description 基于Unsafe的compareAndSwapInt()方法构建线程安全的计数器Demo
 * @date 2020/5/20 17:42
 * Version 1.0
 **/
public class UnsafeCounter {
    private volatile int count = 0;
 
    private static long offset;
    private static Unsafe unsafe;
 
    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
            offset = unsafe.objectFieldOffset(UnsafeCounter.class.getDeclaredField("count"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
 
    public void increment() {
        int before = count;
        // 失败了就重试直到成功为止
        while (!unsafe.compareAndSwapInt(this, offset, before, before + 1)) {
            before = count;
        }
    }
 
    public int getCount() {
        return count;
    }
 
    public static void main(String[] args) throws InterruptedException {
        UnsafeCounter counter = new UnsafeCounter();
        ExecutorService threadPool = Executors.newFixedThreadPool(100);
 
        // 起100个线程，每个线程自增10000次
        IntStream.range(0, 100)
                .forEach(i -> threadPool.submit(() -> IntStream.range(0, 10000)
                        .forEach(j -> counter.increment())));
 
        threadPool.shutdown();
 
        Thread.sleep(2000);
 
        // 打印1000000
        System.out.println(counter.getCount());
    }
}
```

7.   **park/unpark**

JVM在上下文切换的时候使用了Unsafe中的两个非常牛逼的方法park()和unpark()。

- 当一个线程正在等待某个操作时，JVM调用Unsafe的park()方法来阻塞此线程。

- 当阻塞中的线程需要再次运行时，JVM调用Unsafe的unpark()方法来唤醒此线程。

> java中的集合时看到了大量的`LockSupport.park()/unpark()`，它们底层都是调用的Unsafe的这两个方法。







# Maven

> https://www.yuque.com/yuzhi-vmblo/qesfbo/go0pzd



# Gradle 

> https://www.yuque.com/youyi-ai1ik/emphm9/kyhenl