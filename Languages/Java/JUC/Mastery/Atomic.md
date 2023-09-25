# AtomicXXX类



​		Java1.5的Atomic包名为java.util.concurrent.atomic。这个包提供了一系列原子类。这些类可以保证多线程环境下，当某个线程在执行 atomic 的方法时，不会被其他线程打断，而别的线程通过自旋锁（CAS），一直等到该方法执行完成，才由JVM从等待队列中选择一个线程执行。

​		Atomic类在软件层面上是非阻塞的，它的原子性其实是依靠底层的 CAS 来保障原子性的更新数据，在要添加或者减少的时候，会使用死循环不断地 CAS 到特定的值，从而达到更新数据的目的。

```java
```



## AtomicStampedReference

