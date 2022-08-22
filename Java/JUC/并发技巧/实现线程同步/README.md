在[多线程](https://so.csdn.net/so/search?q=多线程&spm=1001.2101.3001.7020)中线程的执行顺序是依靠哪个线程先获得到CUP的执行权谁就先执行，虽然说可以通过线程的优先权进行设置，但是他只是获取CUP执行权的概率高点，但是也不一定必须先执行。在这种情况下如何保证线程按照一定的顺序进行执行，今天就来一个大总结，分别介绍一下几种方式。

1. 通过Object的wait和notify
2. 通过Condition的awiat和signal
3. 通过一个阻塞队列
4. 通过两个阻塞队列
5. 通过SynchronousQueue 
6. 通过线程池的Callback回调
7. 通过同步辅助类CountDownLatch
8. 通过同步辅助类CyclicBarrier

