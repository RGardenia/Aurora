# *Synchronization* 







## 同步结构

1. CountDownLatch，允许一个或多个线程等待某些操作完成
2. CyclicBarrier，一种辅助性的同步结构，允许多个线程等待到达某个屏障
3. Semaphore，Java 版本的信号量实现

> CountDownLatch 是不可以重置的，所以无法重用；而 CyclicBarrier 则没有这种限制，可以重用。
>
> CountDownLatch 的基本操作组合是 countDown/await。调用 await 的线程阻塞等待 countDown 足够的次数，不管你是在一个线程还是多个线程里 countDown，只要次数足够即可。所以就像 Brain Goetz 说过的，CountDownLatch 操作的是事件。
>
> CyclicBarrier 的基本操作组合，则就是 await，当所有的伙伴（parties）都调用了 await，才会继续进行任务，并自动进行重置。注意，正常情况下，CyclicBarrier 的重置都是自动发生的，如果我们调用 reset 方法，但还有线程在等待，就会导致等待线程被打扰，抛出 BrokenBarrierException 异常。CyclicBarrier 侧重点是线程，而不是调用事件，它的典型应用场景是用来等待并发线程结束

### SynchronousQueue

​	每个删除操作都要等待插入操作，反之每个插入操作也都要等待删除动作，其内部容量是 0

​	在多线程中线程的执行顺序是依靠哪个线程先获得到CUP的执行权谁就先执行，虽说可以通过线程的优先权进行设置，但是只是获取CUP执行权的概率高点，但是也不一定必须先执行。在这种情况下如何保证线程按照一定的顺序进行执行

1. 通过 `Object` 的 `wait` 和 `notify`
2. 通过 `Condition` 的 `await` 和 `signal`
3. 通过一个阻塞队列
4. 通过两个阻塞队列
5. 通过 `SynchronousQueue`
6. 通过线程池的 `Callback` 回调
7. 通过同步辅助类 `CountDownLatch`
8. 通过同步辅助类 `CyclicBarrier`



## CountDownLatch（门栓）

​	CountDownLatch 虽然是一个同步工具，但是CountDownLatch不是锁，主要的作用使一个线程等待其他线程各自执行完毕后再执行，是通过一个计数器来实现的，CountDownLatch在初始化时，需要给定一个整数作为计数器。当调用countDown方法时，计数器会被减1；当调用 `await `方法时，如果计数器大于0时，线程会被阻塞，一直到计数器被 `countDown` 方法减到0时，线程才会继续执行。计数器是无法重置的，当计数器被减到0时，调用await方法都会直接返回。
​        要明白重要的一点执行 `countDown` 方法的线程不会进行阻塞，执行awit方法的线程才会阻塞。同时也可以设置等待过期时间，等待时间过后开始执行。使用场景和 `join` 差不多。

```java
import java.util.concurrent.CountDownLatch;
 
public class T06_TestCountDownLatch {
    public static void main(String[] args) {
        // 对比 join 和 countDownLatch
        usingJoin();
        usingCountDownLatch();
    }
 
    /**
     * @Description //TODO 使用countDownLatch进行等待
     **/
    private static void usingCountDownLatch() {
        Thread[] threads = new Thread[100];
        CountDownLatch latch = new CountDownLatch(threads.length);
        for(int i=0; i<threads.length; i++) {
            threads[i] = new Thread(()->{
                int result = 0;
                for(int j=0; j<10000; j++) result += j;
                // 计数器减一
                latch.countDown();
            });
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        try {
            // 阻塞的是当前线程，所以2无法执行下面的输出
            // 可以加时间 latch.await(3, TimeUnit.SECONDS);
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end latch");
    }
    private static void usingJoin() {
        Thread[] threads = new Thread[100];
        for(int i=0; i<threads.length; i++) {
            threads[i] = new Thread(()->{
                int result = 0;
                for(int j=0; j<10000; j++) result += j;
            });
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("end join");
    }
}
```



## CyclicBarrier（栅栏）

​	其实 `CyclicBarrier` 和 `CountDownLatch` 非常容易混淆， `CountDownLatch` 是阻塞一个线程，等待其他线程执行完阻塞线程在执行，而 `CyclicBarrier` 是阻塞一个线程组，在 `CyclicBarrier` 类的内部有一个计数器，每个线程在到达屏障点的时候都会调用 `await` 方法将自己阻塞，此时计数器会减 1，当计数器减为 0 的时候所有因调用 `await` 方法而被阻塞的线程将被唤醒。
<img src="../images/20181218144511688" alt="img" style="zoom: 67%;" />

```java
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
 
public class T07_TestCyclicBarrier {
    public static void main(String[] args) {
        //三种写法
        //CyclicBarrier barrier = new CyclicBarrier(20);
 
        CyclicBarrier barrier = new CyclicBarrier(20, () -> System.out.println("满人"));
        /*CyclicBarrier barrier = new CyclicBarrier(20, new Runnable() {
            @Override
            public void run() {
                System.out.println("满人，发车");
            }
        });*/
        for(int i=0; i<100; i++) {
                new Thread(()->{
                    try {
                        // 发现每次满20个线程才会执行，而且可以重复使用
                        barrier.await();
 
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }).start();
        }
    }
}
```

## Phaser（可以控制的栅栏）

​	`Phaser` 是一个灵活的线程同步工具，包含了 `CyclicBarrier` 和 `CountDownLatch` 的相关功能。

​	`Phaser` 替代 `CountDownLatch` 。对于`CountDownLatch` 而言，有 2 个重要的方法，一个是`await()` 方法，可以使线程进入等待状态，在`Phaser` 中，与之对应的方法是 `awaitAdvance(int n)` 。`CountDownLatch` 中另一个重要的方法是 `countDown()` ，使计数器减一，当计数器为 0 时所有等待的线程开始执行，在`Phaser` 中，与之对应的方法是 `arrive()`

​	同时`Phaser` 也可以理解为一个可以控制的栅栏，`CyclicBarrier` 的 `await()` 方法可以直接用`Phaser` 的 `arriveAndAwaitAdvance()` 方法替代

```java
import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
// 代码演示替换 cyclicBarrier
public class T08_TestPhaser {
    static Random r = new Random();
    static MarriagePhaser phaser = new MarriagePhaser();
 
    //每次写这个太麻烦，直接提取出来
    static void milliSleep(int milli) {
        try {
            TimeUnit.MILLISECONDS.sleep(milli);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        phaser.bulkRegister(5);
        for(int i=0; i<5; i++) {
            final int nameIndex = i;
            new Thread(()->{
                Person p = new Person("person " + nameIndex);
                p.arrive();
                // 相当于awit
                phaser.arriveAndAwaitAdvance();
                p.eat();
                phaser.arriveAndAwaitAdvance();
                p.leave();
                phaser.arriveAndAwaitAdvance();
            }).start();
        }
    }
    static class MarriagePhaser extends Phaser {
        /*
         * boolean onAdvance(int phase, int registeredParties)方法。此方法有2个作用：
         * 1、当每一个阶段执行完毕，此方法会被自动调用，因此，重载此方法写入的代码会在每个阶段执行完毕时执行，相当于CyclicBarrier的barrierAction。
         * 2、当此方法返回true时，意味着Phaser被终止，因此可以巧妙的设置此方法的返回值来终止所有线程。
         * phase表示执行阶段，registeredParties表示管理的线程
         **/
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            switch (phase) {
                case 0:
                    System.out.println("所有人到齐了！");
                    return false;
                case 1:
                    System.out.println("所有人吃完了！");
                    return false;
                case 2:
                    System.out.println("所有人离开了！");
                    System.out.println("婚礼结束！");
                    return true;
                default:
                    return true;
            }
        }
    }
    static class Person {
        String name;
        public Person(String name) {
            this.name = name;
        }
        public void arrive() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 到达现场！\n", name);
        }
        public void eat() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 吃完!\n", name);
        }
        public void leave() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 离开！\n", name);
        }
    }
}
```

可以随时控制 phaser 的大小，上边通过 `bulkRegister` 方法指定，下边可以看一下通过 `register` 注册新的 phaser ，就是相当于+1

```java
import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
 
public class T09_TestPhaser2 {
    static Random r = new Random();
    static MarriagePhaser phaser = new MarriagePhaser();
    static void milliSleep(int milli) {
        try {
            TimeUnit.MILLISECONDS.sleep(milli);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        phaser.bulkRegister(7);
        for(int i=0; i<5; i++) {
            new Thread(new Person("p" + i)).start();
        }
        new Thread(new Person("新郎")).start();
        new Thread(new Person("新娘")).start();
    }
    static class MarriagePhaser extends Phaser {
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            switch (phase) {
                case 0:
                    System.out.println("所有人到齐了！" + registeredParties);
                    System.out.println();
                    return false;
                case 1:
                    System.out.println("所有人吃完了！" + registeredParties);
                    System.out.println();
                    return false;
                case 2:
                    System.out.println("所有人离开了！" + registeredParties);
                    System.out.println();
                    return false;
                case 3:
                    System.out.println("婚礼结束！新郎新娘抱抱！" + registeredParties);
                    return true;
                default:
                    return true;
            }
        }
    }
    static class Person implements Runnable {
        String name;
        public Person(String name) {
            this.name = name;
        }
        public void arrive() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 到达现场！\n", name);
            phaser.arriveAndAwaitAdvance();
        }
        public void eat() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 吃完!\n", name);
            phaser.arriveAndAwaitAdvance();
        }
        public void leave() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 离开！\n", name);
            phaser.arriveAndAwaitAdvance();
        }
        private void hug() {
            if(name.equals("新郎") || name.equals("新娘")) {
                milliSleep(r.nextInt(1000));
                System.out.printf("%s 洞房！\n", name);
                phaser.arriveAndAwaitAdvance();
            } else {
                //观察加一和减一的区别：发现加一的时候最后不会打印：婚礼结束，说明当前阶段没有完毕
                //phaser.arriveAndDeregister();
                phaser.register();
            }
        }
        //每个线程在启动的时候调用一下方法
        @Override
        public void run() {
            arrive();
            eat();
            leave();
            hug();
        }
    }
}
```



## Semaphore（指示灯）

​	可以设置几个线程同时执行，设置俩个的话，就是等这俩个线程执行完之后，其他才能执行，可以实现限流，同时可以设置公平锁和非公平锁。

```java
import java.util.concurrent.Semaphore;
 
public class T11_TestSemaphore {
    public static void main(String[] args) {
        //Semaphore s = new Semaphore(2);
        //设置许可数和公平锁
        Semaphore s = new Semaphore(2, true);
        //允许一个线程同时执行
        //Semaphore s = new Semaphore(1);
 
        new Thread(()->{
            try {
                //从此信号量获取一个许可，在提供一个许可前一直将线程阻塞，否则线程被中断。
                //就是上边的2-1
                s.acquire();
 
                System.out.println("T1 running...");
                Thread.sleep(200);
                System.out.println("T1 running...");
 
                System.out.println("查看可用许可数："+s.availablePermits());
                System.out.println("查看是否有线程在等待许可："+s.hasQueuedThreads());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //释放一个许可，将其返回给信号量。
                //+1
                s.release();
            }
        }).start();
 
        new Thread(()->{
            try {
                s.acquire();
                
                System.out.println("T2 running...");
                Thread.sleep(200);
                System.out.println("T2 running...");
                
                s.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
```

## Exchanger（交换）

​	`Exchanger` 用于线程间进行通信、数据交换。`Exchanger` 提供了一个同步点 `exchange` 方法，两个线程调用 `exchange` 方法时，无论调用时间先后，两个线程会互相等到线程到达 `exchange `方法调用点，此时两个线程可以交换数据，将本线程产出数据传递给对方。

```java
import java.util.concurrent.Exchanger;
 
public class T12_TestExchanger {
    static Exchanger<String> exchanger = new Exchanger<>();
    public static void main(String[] args) {
        new Thread(()->{
            String s = "T1";
            try {
                //阻塞，等待交换，没在俩个以上用过
                s = exchanger.exchange(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " " + s);
 
        }, "t1").start();
        new Thread(()->{
            String s = "T2";
            try {
                s = exchanger.exchange(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " " + s);
        }, "t2").start();
    }
}
```

