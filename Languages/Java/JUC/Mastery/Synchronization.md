# *Synchronization* 







## 同步结构

1. CountDownLatch，允许一个或多个线程等待某些操作完成。
2. CyclicBarrier，一种辅助性的同步结构，允许多个线程等待到达某个屏障。
3. Semaphore，Java 版本的信号量实现。



> CountDownLatch 是不可以重置的，所以无法重用；而 CyclicBarrier 则没有这种限制，可以重用。
>
> CountDownLatch 的基本操作组合是 countDown/await。调用 await 的线程阻塞等待 countDown 足够的次数，不管你是在一个线程还是多个线程里 countDown，只要次数足够即可。所以就像 Brain Goetz 说过的，CountDownLatch 操作的是事件。
>
> CyclicBarrier 的基本操作组合，则就是 await，当所有的伙伴（parties）都调用了 await，才会继续进行任务，并自动进行重置。注意，正常情况下，CyclicBarrier 的重置都是自动发生的，如果我们调用 reset 方法，但还有线程在等待，就会导致等待线程被打扰，抛出 BrokenBarrierException 异常。CyclicBarrier 侧重点是线程，而不是调用事件，它的典型应用场景是用来等待并发线程结束





## SynchronousQueue

​	每个删除操作都要等待插入操作，反之每个插入操作也都要等待删除动作，其内部容量是 0。











