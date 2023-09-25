# BlockingXxx







## BlockingQueue



​	ArrayBlockingQueue 是最典型的的有界队列，其内部以 final 的数组保存数据，数组的大小就决定了队列的边界，所以在创建 ArrayBlockingQueue 时，都要指定容量，如

<p style="color:red;text-align:center">public ArrayBlockingQueue(int capacity, boolean fair)</p>



​	LinkedBlockingQueue，容易被误解为无边界，但其实其行为和内部代码都是基于有界的逻辑实现的，只不过如果没有在创建队列时就指定容量，那么其容量限制就自动被设置为 Integer.MAX_VALUE，成为了无界队列。



## PriorityBlockingQueue

​	无边界的优先队列，严格意义上来讲，其大小是要受系统资源影响。



> DelayedQueue 和 LinkedTransferQueue 同样是无边界的队列。对于无边界的队列，有一个自然的结果，就是 put 操作永远也不会发生其他 BlockingQueue 的那种等待情况。
>
> 根据需求可以从很多方面考量：
>
> ​	考虑应用场景中对队列边界的要求。ArrayBlockingQueue 是有明确的容量限制的，而 LinkedBlockingQueue 则取决于是否在创建时指定，SynchronousQueue 则干脆不能缓存任何元素。
>
> ​	从空间利用角度，数组结构的 ArrayBlockingQueue 要比 LinkedBlockingQueue 紧凑，因为其不需要创建所谓节点，但是其初始分配阶段就需要一段连续的空间，所以初始内存需求更大。
>
> ​	通用场景中，LinkedBlockingQueue 的吞吐量一般优于 ArrayBlockingQueue，因为它实现了更加细粒度的锁操作。ArrayBlockingQueue 实现比较简单，性能更好预测，属于表现稳定的“选手”。
>
> ​	如果需要实现的是两个线程之间接力性（handoff）的场景，可能会选择 CountDownLatch，但是SynchronousQueue也是完美符合这种场景的，而且线程间协调和数据传输统一起来，代码更加规范。
>
> ​	可能令人意外的是，很多时候 SynchronousQueue 的性能表现，往往大大超过其他实现，尤其是在队列元素较小的场景。















