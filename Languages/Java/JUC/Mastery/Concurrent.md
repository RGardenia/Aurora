# ConcurrentXxx



> Concurrent 类型没有类似 CopyOnWrite 之类容器相对较重的修改开销。

​	Concurrent 往往提供了较低的遍历一致性。你可以这样理解所谓的弱一致性，例如，当利用迭代器遍历时，如果容器发生修改，迭代器仍然可以继续进行遍历。

> 弱一致性的另外一个体现是，size 等操作准确性是有限的，未必是 100% 准确。与此同时，读取的性能具有一定的不确定性。
>
> 同步容器常见的行为“fast-fail”是强一致性的提现，也就是检测到容器在遍历过程中发生了修改，则抛出 ConcurrentModificationException，不再继续遍历。







## ConcurrentLinkedQueue

> Concurrent 类型基于 lock-free，在常见的多线程访问场景，一般可以提供较高吞吐量
>
> LinkedBlockingQueue 内部则是基于锁，并提供了 BlockingQueue 的等待性方法



