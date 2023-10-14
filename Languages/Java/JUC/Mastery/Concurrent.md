# ConcurrentXxx



> Concurrent 类型没有类似 CopyOnWrite 之类容器相对较重的修改开销。

​	Concurrent 往往提供了较低的遍历一致性。你可以这样理解所谓的弱一致性，例如，当利用迭代器遍历时，如果容器发生修改，迭代器仍然可以继续进行遍历。

> 弱一致性的另外一个体现是，size 等操作准确性是有限的，未必是 100% 准确。与此同时，读取的性能具有一定的不确定性。
>
> 同步容器常见的行为 `“fast-fail”` 是强一致性的提现，也就是检测到容器在遍历过程中发生了修改，则抛出 ConcurrentModificationException，不再继续遍历。



## ConcurrentLinkedQueue

​	ConcurrentLinkedQueue 只实现了 Queue 接口，并没有实现 `BlockingQueue` 接口，所以它不是阻塞队列，也不能用于线程池中，但是它是线程安全的，可用于多线程环境中。

- Concurrent 类型基于 `lock-free` ，在常见的多线程访问场景，一般可以提供较高吞吐量

- LinkedBlockingQueue 内部则是基于锁，并提供了  `BlockingQueue`  的等待性方法

### 主要属性

```java
// 链表头节点
private transient volatile Node<E> head;
// 链表尾节点
private transient volatile Node<E> tail;
```

### 主要内部类

```java
private static class Node<E> {
    volatile E item;
    volatile Node<E> next;
}
```

### 主要构造方法

```java
// 无界的单链表实现
public ConcurrentLinkedQueue() {
    // 初始化头尾节点
    head = tail = new Node<E>(null);
}
 
public ConcurrentLinkedQueue(Collection<? extends E> c) {
    Node<E> h = null, t = null;
    // 遍历c，并把它元素全部添加到单链表中
    for (E e : c) {
        checkNotNull(e);
        Node<E> newNode = new Node<E>(e);
        if (h == null)
            h = t = newNode;
        else {
            t.lazySetNext(newNode);
            t = newNode;
        }
    }
    if (h == null)
        h = t = new Node<E>(null);
    head = h;
    tail = t;
}
```

### 入队

因为它不是阻塞队列，所以只有两个入队的方法， `add(e)` 和 `offer(e)` 。无界队列， `add(e)` 方法也不用抛出异常了。

```java
public boolean add(E e) {
    return offer(e);
}
 
public boolean offer(E e) {
    // 不能添加空元素
    checkNotNull(e);
    // 新节点
    final Node<E> newNode = new Node<E>(e);
 
    // 入队到链表尾
    for (Node<E> t = tail, p = t;;) {
        Node<E> q = p.next;
        // 如果没有next，说明到链表尾部了，就入队
        if (q == null) {
            // CAS更新p的next为新节点
            // 如果成功了，就返回true
            // 如果不成功就重新取next重新尝试
            if (p.casNext(null, newNode)) {
                // 如果p不等于t，说明有其它线程先一步更新tail
                // 也就不会走到q==null这个分支了
                // p取到的可能是t后面的值
                // 把tail原子更新为新节点
                if (p != t) // hop two nodes at a time
                    casTail(t, newNode);  // Failure is OK.
                // 返回入队成功
                return true;
            }
        }
        else if (p == q)
            // 如果p的next等于p，说明p已经被删除了（已经出队了）
            // 重新设置p的值
            p = (t != (t = tail)) ? t : head;
        else
            // t后面还有值，重新设置p的值
            p = (p != t && t != (t = tail)) ? t : q;
    }
}
```

入队整个流程还是比较清晰的，这里有个前提是出队时会把出队的那个节点的next设置为节点本身。

（1）定位到链表尾部，尝试把新节点放到后面；

（2）如果尾部变化了，则重新获取尾部，再重试；

### 出队

两个出队的方法， `remove()` 和 `poll()` 

```java
public E remove() {
    E x = poll();
    if (x != null)
        return x;
    else
        throw new NoSuchElementException();
}
 
public E poll() {
    restartFromHead:
    for (;;) {
        // 尝试弹出链表的头节点
        for (Node<E> h = head, p = h, q;;) {
            E item = p.item;
            // 如果节点的值不为空，并且将其更新为null成功了
            if (item != null && p.casItem(item, null)) {
                // 如果头节点变了，则不会走到这个分支
                // 会先走下面的分支拿到新的头节点
                // 这时候p就不等于h了，就更新头节点
                // 在updateHead()中会把head更新为新节点
                // 并让head的next指向其自己
                if (p != h) // hop two nodes at a time
                    updateHead(h, ((q = p.next) != null) ? q : p);
                // 上面的casItem()成功，就可以返回出队的元素了
                return item;
            }
            // 下面三个分支说明头节点变了
            // 且p的item肯定为null
            else if ((q = p.next) == null) {
                // 如果p的next为空，说明队列中没有元素了
                // 更新h为p，也就是空元素的节点
                updateHead(h, p);
                // 返回null
                return null;
            }
            else if (p == q)
                // 如果p等于p的next，说明p已经出队了，重试
                continue restartFromHead;
            else
                // 将p设置为p的next
                p = q;
        }
    }
}
// 更新头节点的方法
final void updateHead(Node<E> h, Node<E> p) {
    // 原子更新h为p成功后，延迟更新h的next为它自己
    // 这里用延迟更新是安全的，因为head节点已经变了
    // 只要入队出队的时候检查head有没有变化就行了，跟它的next关系不大
    if (h != p && casHead(h, p))
        h.lazySetNext(h);
}
```

出队的整个逻辑也是比较清晰的：

（1）定位到头节点，尝试更新其值为 null；

（2）如果成功了，就成功出队；

（3）如果失败或者头节点变化了，就重新寻找头节点，并重试；

（4）整个出队过程没有一点阻塞相关的代码，所以出队的时候不会阻塞线程，没找到元素就返回 null；

### 总结

（1）ConcurrentLinkedQueue不是阻塞队列；

（2）ConcurrentLinkedQueue不能用在线程池中；

（3）ConcurrentLinkedQueue使用（CAS+自旋）更新头尾节点控制出队入队操作；



## ConcurrentHashMap

​	ConcurrentHashMap 在 JDK1.7 之前使用分段锁技术，1.7 之后采用了**CAS+自旋**的无锁编程的方式实现。

​	ConcurrentHashMap （ JDK1.7 ）是由 `Segment` 数组结构和 `HashEntry` 数组结构组成。 `Segment` 是一种可重入锁ReentrantLock，在 ConcurrentHashMap 里扮演锁的角色， `HashEntry` 则用于存储键值对数据。一个 ConcurrentHashMap 里包含一个 `Segment` 数组， `Segment` 的结构和 `HashMap` 类似，是一种数组和链表结构， 一个 `Segment` 里包含一个 `HashEntry` 数组，每个 `HashEntry` 是一个链表结构的元素， 每个 `Segment` 守护者一个 `HashEntry` 数组里的元素，当对 `HashEntry` 数组的数据进行修改时，必须首先获得它对应的 `Segment` 锁，modCount 用于记录修改次数，threshold 表示扩容的阈值，loadFactor 表示加载因子。

JDK 1.7 中 ConcurrentHashMap 实现机制：

![这里写图片描述](../images/20160824192750258.jpeg)

![图片2.png](../images/68b66405a89039bf4dcd3e7257ee7e92.png)

​	在 JDK 1.8中，ConcurrentHashMap 进行了重大改进，采用了更加高效的并发控制机制来保证线程安全。相较于JDK 1.7的分段锁设计，JDK 1.8引入了基于**CAS**（Compare and Swap）操作和**链表 / 红黑树**结构的**锁机制**以及其他优化，大大提高了并发性能。

​	具体来说，它将整个哈希桶（Hash Bucket）划分为若干个节点（Node）。每个节点代表一个存储键值对的单元，可以是链表节点（普通节点）或红黑树节点（树节点），这取决于节点内的键值对数量是否达到阈值。**使用红黑树结构可以提高查找、插入、删除等操作的效率**。
```java
static final class Node<K, V> implements Map.Entry<K, V> {
    final int hash;
    final K key;
    volatile V value;
    volatile Node<K, V> next;
    Node(int hash, K key, V value, Node<K, V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }
}
static final class TreeNode<K, V> extends Node<K, V> {
    TreeNode(int hash, K key, V value, Node<K, V> next) {
        super(hash, key, value, next);
    }
    // 省略了红黑树相关的操作代码
}
static final class ConcurrentHashMap<K, V> {
    transient volatile Node<K, V>[] table;
    transient volatile int sizeCtl;
    transient volatile int baseCount;
    transient volatile int modCount;
}
```

1. **插入操作**
   `put` 方法：当进行插入操作时，ConcurrentHashMap 首先计算键的散列值，然后根据散列值和数组长度计算出对应的桶位置。接着使用 CAS 操作尝试插入新节点，如果成功则插入完成；如果失败，则进入下一步。

   `resize` 方法：插入节点时，若发现链表中的节点数量已经达到阈值（默认为 8），则将链表转化为红黑树，提高查找、插入、删除等操作的效率。在转化过程中，利用 synchronized 锁住链表或红黑树所在的桶，并进行相应的操作。(当链表长度小于6时，红黑树会退化为单向链表)

   `forwardTable` 方法：若节点数量超过阈值（默认为64）且 table 未被初始化，则使用CAS操作将 table 指向扩容后的桶数组，并根据需要将链表或红黑树进行分割，以减小线程之间的冲突。

2. **查询操作**
   `get` 方法：当进行查询操作时，首先计算键的散列值，然后根据散列值和数组长度计算出对应的桶位置。接着从桶位置的链表或红黑树中查找对应的节点。

3. **其他操作**
   `remove` 方法：当进行删除操作时，首先计算键的散列值，然后根据散列值和数组长度计算出对应的桶位置。接着使用 synchronized 锁住桶，并进行相应的操作。

Reference
	https://blog.csdn.net/canot/article/details/51236114



## ConcurrentSkipListSet

​	`ConcurrentSkipListSet` 底层是通过 `ConcurrentNavigableMap` 来实现的，它是一个有序的线程安全的集合，基于元素的自然排序或者通过比较器确定的顺序。

**Set 大汇总**

| Set                   | 有序性 | 线程安全 | 底层实现               | 关键接口     | 特点               |
| :-------------------- | :----- | :------- | :--------------------- | :----------- | :----------------- |
| HashSet               | 无     | 否       | HashMap                | 无           | 简单               |
| LinkedHashSet         | 有     | 否       | LinkedHashMap          | 无           | 插入顺序           |
| TreeSet               | 有     | 否       | NavigableMap           | NavigableSet | 自然顺序           |
| CopyOnWriteArraySet   | 有     | 是       | CopyOnWriteArrayList   | 无           | 插入顺序，读写分离 |
| ConcurrentSkipListSet | 有     | 是       | ConcurrentNavigableMap | NavigableSet | 自然顺序           |

从中可以发现一些规律：

（1）除了 `HashSet` 其它 `Set` 都是有序的；

（2）实现了 `NavigableSet` 或者 `SortedSet` 接口的都是自然顺序的；

（3）使用并发安全的集合实现的 `Set` 也是并发安全的；

（4）TreeSet 虽然不是全部都是使用的 `TreeMap` 实现的，但其实都是跟 `TreeMap` 相关的（TreeMap 的子 Map 中组合了 TreeMap）；

（5）ConcurrentSkipListSet 虽然不是全部都是使用的 `ConcurrentSkipListMap`实现的，但其实都是跟 `ConcurrentSkipListMap` 相关的（ConcurrentSkipListeMap 的子 Map 中组合了 ConcurrentSkipListMap）。



## ConcurrentSkipListMap

​	`ConcurrentSkipListMap` 是线程安全的有序的哈希表，适用于高并发的场景。ConcurrentSkipListMap 和 TreeMap，它们虽然都是有序的哈希表。它们的区别如下：

​	第一，它们的线程安全机制不同，TreeMap 是非线程安全的，而 ConcurrentSkipListMap 是线程安全的

​	第二，ConcurrentSkipListMap 是通过跳表实现的，而 TreeMap 是通过红黑树实现的



### 存储结构

跳表在原有的有序链表上面增加了多级索引，通过索引来实现快速查找。

![skiplist3](../images/format,png-1697288730584-3.png)

### 主要内部类

```java
// 数据节点，典型的单链表结构
static final class Node<K,V> {
    final K key;
    // 注意：这里value的类型是Object，而不是V
    // 在删除元素的时候value会指向当前元素本身
    volatile Object value;
    volatile Node<K,V> next;
    
    Node(K key, Object value, Node<K,V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }
    
    Node(Node<K,V> next) {
        this.key = null;
        this.value = this; // 当前元素本身(marker)
        this.next = next;
    }
}
 
// 索引节点，存储着对应的node值，及向下和向右的索引指针
static class Index<K,V> {
    final Node<K,V> node;
    final Index<K,V> down;
    volatile Index<K,V> right;
    
    Index(Node<K,V> node, Index<K,V> down, Index<K,V> right) {
        this.node = node;
        this.down = down;
        this.right = right;
    }
}
 
// 头索引节点，继承自Index，并扩展一个level字段，用于记录索引的层级
static final class HeadIndex<K,V> extends Index<K,V> {
    final int level;
    
    HeadIndex(Node<K,V> node, Index<K,V> down, Index<K,V> right, int level) {
        super(node, down, right);
        this.level = level;
    }
}
```

（1）Node，数据节点，存储数据的节点，典型的单链表结构；
（2）Index，索引节点，存储着对应的node值，及向下和向右的索引指针；
（3）HeadIndex，头索引节点，继承自Index，并扩展一个level字段，用于记录索引的层级；

### 构造方法

```java
public ConcurrentSkipListMap() {
    this.comparator = null;
    initialize();
}
 
public ConcurrentSkipListMap(Comparator<? super K> comparator) {
    this.comparator = comparator;
    initialize();
}
 
public ConcurrentSkipListMap(Map<? extends K, ? extends V> m) {
    this.comparator = null;
    initialize();
    putAll(m);
}
 
public ConcurrentSkipListMap(SortedMap<K, ? extends V> m) {
    this.comparator = m.comparator();
    initialize();
    buildFromSorted(m);
}
```

初始化的时候，跳表中只有一个头索引节点，层级是 1，数据节点是一个空对象，down 和 right 都是 null



### 添加元素

​	跳表插入元素的时候会通过抛硬币的方式决定出它需要的层级，然后找到各层链中它所在的位置，最后通过单链表插入的方式把节点及索引插入进去来实现的。

```java
public V put(K key, V value) {
    // 不能存储value为null的元素
    // 因为value为null标记该元素被删除（后面会看到）
    if (value == null)
        throw new NullPointerException();
 
    // 调用doPut()方法添加元素
    return doPut(key, value, false);
}
 
private V doPut(K key, V value, boolean onlyIfAbsent) {
    // 添加元素后存储在z中
    Node<K,V> z;             // added node
    // key也不能为null
    if (key == null)
        throw new NullPointerException();
    Comparator<? super K> cmp = comparator;
 
    // Part I：找到目标节点的位置并插入
    // 这里的目标节点是数据节点，也就是最底层的那条链
    // 自旋
    outer: for (;;) {
        // 寻找目标节点之前最近的一个索引对应的数据节点，存储在b中，b=before
        // 并把b的下一个数据节点存储在n中，n=next
        // 为了便于描述，我这里把b叫做当前节点，n叫做下一个节点
        for (Node<K,V> b = findPredecessor(key, cmp), n = b.next;;) {
            // 如果下一个节点不为空
            // 就拿其key与目标节点的key比较，找到目标节点应该插入的位置
            if (n != null) {
                // v=value，存储节点value值
                // c=compare，存储两个节点比较的大小
                Object v; int c;
                // n的下一个数据节点，也就是b的下一个节点的下一个节点（孙子节点）
                Node<K,V> f = n.next;
                // 如果n不为b的下一个节点
                // 说明有其它线程修改了数据，则跳出内层循环
                // 也就是回到了外层循环自旋的位置，从头来过
                if (n != b.next)               // inconsistent read
                    break;
                // 如果n的value值为空，说明该节点已删除，协助删除节点
                if ((v = n.value) == null) {   // n is deleted
                    // todo 这里为啥会协助删除？后面讲
                    n.helpDelete(b, f);
                    break;
                }
                // 如果b的值为空或者v等于n，说明b已被删除
                // 这时候n就是marker节点，那b就是被删除的那个
                if (b.value == null || v == n) // b is deleted
                    break;
                // 如果目标key与下一个节点的key大
                // 说明目标元素所在的位置还在下一个节点的后面
                if ((c = cpr(cmp, key, n.key)) > 0) {
                    // 就把当前节点往后移一位
                    // 同样的下一个节点也往后移一位
                    // 再重新检查新n是否为空，它与目标key的关系
                    b = n;
                    n = f;
                    continue;
                }
                // 如果比较时发现下一个节点的key与目标key相同
                // 说明链表中本身就存在目标节点
                if (c == 0) {
                    // 则用新值替换旧值，并返回旧值（onlyIfAbsent=false）
                    if (onlyIfAbsent || n.casValue(v, value)) {
                        @SuppressWarnings("unchecked") V vv = (V)v;
                        return vv;
                    }
                    // 如果替换旧值时失败，说明其它线程先一步修改了值，从头来过
                    break; // restart if lost race to replace value
                }
                // 如果c<0，就往下走，也就是找到了目标节点的位置
                // else c < 0; fall through
            }
 
            // 有两种情况会到这里
            // 一是到链表尾部了，也就是n为null了
            // 二是找到了目标节点的位置，也就是上面的c<0
 
            // 新建目标节点，并赋值给z
            // 这里把n作为新节点的next
            // 如果到链表尾部了，n为null，这毫无疑问
            // 如果c<0，则n的key比目标key大，相当于在b和n之间插入目标节点z
            z = new Node<K,V>(key, value, n);
            // 原子更新b的下一个节点为目标节点z
            if (!b.casNext(n, z))
                // 如果更新失败，说明其它线程先一步修改了值，从头来过
                break;         // restart if lost race to append to b
            // 如果更新成功，跳出自旋状态
            break outer;
        }
    }
 
    // 经过Part I，目标节点已经插入到有序链表中了
 
    // Part II：随机决定是否需要建立索引及其层次，如果需要则建立自上而下的索引
 
    // 取个随机数
    int rnd = ThreadLocalRandom.nextSecondarySeed();
    // 0x80000001展开为二进制为10000000000000000000000000000001
    // 只有两头是1
    // 这里(rnd & 0x80000001) == 0
    // 相当于排除了负数（负数最高位是1），排除了奇数（奇数最低位是1）
    // 只有最高位最低位都不为1的数跟0x80000001做&操作才会为0
    // 也就是正偶数
    if ((rnd & 0x80000001) == 0) { // test highest and lowest bits
        // 默认level为1，也就是只要到这里了就会至少建立一层索引
        int level = 1, max;
        // 随机数从最低位的第二位开始，有几个连续的1则level就加几
        // 因为最低位肯定是0，正偶数嘛
        // 比如，1100110，level就加2
        while (((rnd >>>= 1) & 1) != 0)
            ++level;
 
        // 用于记录目标节点建立的最高的那层索引节点
        Index<K,V> idx = null;
        // 取头索引节点（这是最高层的头索引节点）
        HeadIndex<K,V> h = head;
        // 如果生成的层数小于等于当前最高层的层级
        // 也就是跳表的高度不会超过现有高度
        if (level <= (max = h.level)) {
            // 从第一层开始建立一条竖直的索引链表
            // 这条链表使用down指针连接起来
            // 每个索引节点里面都存储着目标节点这个数据节点
            // 最后idx存储的是这条索引链表的最高层节点
            for (int i = 1; i <= level; ++i)
                idx = new Index<K,V>(z, idx, null);
        }
        else { // try to grow by one level
            // 如果新的层数超过了现有跳表的高度
            // 则最多只增加一层
            // 比如现在只有一层索引，那下一次最多增加到两层索引，增加多了也没有意义
            level = max + 1; // hold in array and later pick the one to use
            // idxs用于存储目标节点建立的竖起索引的所有索引节点
            // 其实这里直接使用idx这个最高节点也是可以完成的
            // 只是用一个数组存储所有节点要方便一些
            // 注意，这里数组0号位是没有使用的
            @SuppressWarnings("unchecked")Index<K,V>[] idxs =
                    (Index<K,V>[])new Index<?,?>[level+1];
            // 从第一层开始建立一条竖的索引链表（跟上面一样，只是这里顺便把索引节点放到数组里面了）
            for (int i = 1; i <= level; ++i)
                idxs[i] = idx = new Index<K,V>(z, idx, null);
 
            // 自旋
            for (;;) {
                // 旧的最高层头索引节点
                h = head;
                // 旧的最高层级
                int oldLevel = h.level;
                // 再次检查，如果旧的最高层级已经不比新层级矮了
                // 说明有其它线程先一步修改了值，从头来过
                if (level <= oldLevel) // lost race to add level
                    break;
                // 新的最高层头索引节点
                HeadIndex<K,V> newh = h;
                // 头节点指向的数据节点
                Node<K,V> oldbase = h.node;
                // 超出的部分建立新的头索引节点
                for (int j = oldLevel+1; j <= level; ++j)
                    newh = new HeadIndex<K,V>(oldbase, newh, idxs[j], j);
                // 原子更新头索引节点
                if (casHead(h, newh)) {
                    // h指向新的最高层头索引节点
                    h = newh;
                    // 把level赋值为旧的最高层级的
                    // idx指向的不是最高的索引节点了
                    // 而是与旧最高层平齐的索引节点
                    idx = idxs[level = oldLevel];
                    break;
                }
            }
        }
 
        // 经过上面的步骤，有两种情况
        // 一是没有超出高度，新建一条目标节点的索引节点链
        // 二是超出了高度，新建一条目标节点的索引节点链，同时最高层头索引节点同样往上长
 
        // Part III：将新建的索引节点（包含头索引节点）与其它索引节点通过右指针连接在一起
 
        // 这时level是等于旧的最高层级的，自旋
        splice: for (int insertionLevel = level;;) {
            // h为最高头索引节点
            int j = h.level;
 
            // 从头索引节点开始遍历
            // 为了方便，这里叫q为当前节点，r为右节点，d为下节点，t为目标节点相应层级的索引
            for (Index<K,V> q = h, r = q.right, t = idx;;) {
                // 如果遍历到了最右边，或者最下边，
                // 也就是遍历到头了，则退出外层循环
                if (q == null || t == null)
                    break splice;
                // 如果右节点不为空
                if (r != null) {
                    // n是右节点的数据节点，为了方便，这里直接叫右节点的值
                    Node<K,V> n = r.node;
                    // 比较目标key与右节点的值
                    int c = cpr(cmp, key, n.key);
                    // 如果右节点的值为空了，则表示此节点已删除
                    if (n.value == null) {
                        // 则把右节点删除
                        if (!q.unlink(r))
                            // 如果删除失败，说明有其它线程先一步修改了，从头来过
                            break;
                        // 删除成功后重新取右节点
                        r = q.right;
                        continue;
                    }
                    // 如果比较c>0，表示目标节点还要往右
                    if (c > 0) {
                        // 则把当前节点和右节点分别右移
                        q = r;
                        r = r.right;
                        continue;
                    }
                }
 
                // 到这里说明已经到当前层级的最右边了
                // 这里实际是会先走第二个if
 
                // 第一个if
                // j与insertionLevel相等了
                // 实际是先走的第二个if，j自减后应该与insertionLevel相等
                if (j == insertionLevel) {
                    // 这里是真正连右指针的地方
                    if (!q.link(r, t))
                        // 连接失败，从头来过
                        break; // restart
                    // t节点的值为空，可能是其它线程删除了这个元素
                    if (t.node.value == null) {
                        // 这里会去协助删除元素
                        findNode(key);
                        break splice;
                    }
                    // 当前层级右指针连接完毕，向下移一层继续连接
                    // 如果移到了最下面一层，则说明都连接完成了，退出外层循环
                    if (--insertionLevel == 0)
                        break splice;
                }
 
                // 第二个if
                // j先自减1，再与两个level比较
                // j、insertionLevel和t(idx)三者是对应的，都是还未把右指针连好的那个层级
                if (--j >= insertionLevel && j < level)
                    // t往下移
                    t = t.down;
 
                // 当前层级到最右边了
                // 那只能往下一层级去走了
                // 当前节点下移
                // 再取相应的右节点
                q = q.down;
                r = q.right;
            }
        }
    }
    return null;
}
 
// 寻找目标节点之前最近的一个索引对应的数据节点
private Node<K,V> findPredecessor(Object key, Comparator<? super K> cmp) {
    // key不能为空
    if (key == null)
        throw new NullPointerException(); // don't postpone errors
    // 自旋
    for (;;) {
        // 从最高层头索引节点开始查找，先向右，再向下
        // 直到找到目标位置之前的那个索引
        for (Index<K,V> q = head, r = q.right, d;;) {
            // 如果右节点不为空
            if (r != null) {
                // 右节点对应的数据节点，为了方便，我们叫右节点的值
                Node<K,V> n = r.node;
                K k = n.key;
                // 如果右节点的value为空
                // 说明其它线程把这个节点标记为删除了
                // 则协助删除
                if (n.value == null) {
                    if (!q.unlink(r))
                        // 如果删除失败
                        // 说明其它线程先删除了，从头来过
                        break;           // restart
                    // 删除之后重新读取右节点
                    r = q.right;         // reread r
                    continue;
                }
                // 如果目标key比右节点还大，继续向右寻找
                if (cpr(cmp, key, k) > 0) {
                    // 往右移
                    q = r;
                    // 重新取右节点
                    r = r.right;
                    continue;
                }
                // 如果c<0，说明不能再往右了
            }
            // 到这里说明当前层级已经到最右了
            // 两种情况：一是r==null，二是c<0
            // 再从下一级开始找
 
            // 如果没有下一级了，就返回这个索引对应的数据节点
            if ((d = q.down) == null)
                return q.node;
 
            // 往下移
            q = d;
            // 重新取右节点
            r = d.right;
        }
    }
}
 
// Node.class中的方法，协助删除元素
void helpDelete(Node<K,V> b, Node<K,V> f) {
    /*
     * Rechecking links and then doing only one of the
     * help-out stages per call tends to minimize CAS
     * interference among helping threads.
     */
    // 这里的调用者this==n，三者关系是b->n->f
    if (f == next && this == b.next) {
        // 将n的值设置为null后，会先把n的下个节点设置为marker节点
        // 这个marker节点的值是它自己
        // 这里如果不是它自己说明marker失败了，重新marker
        if (f == null || f.value != f) // not already marked
            casNext(f, new Node<K,V>(f));
        else
            // marker过了，就把b的下个节点指向marker的下个节点
            b.casNext(this, f.next);
    }
}
 
// Index.class中的方法，删除succ节点
final boolean unlink(Index<K,V> succ) {
    // 原子更新当前节点指向下一个节点的下一个节点
    // 也就是删除下一个节点
    return node.value != null && casRight(succ, succ.right);
}
 
// Index.class中的方法，在当前节点与succ之间插入newSucc节点
final boolean link(Index<K,V> succ, Index<K,V> newSucc) {
    // 在当前节点与下一个节点中间插入一个节点
    Node<K,V> n = node;
    // 新节点指向当前节点的下一个节点
    newSucc.right = succ;
    // 原子更新当前节点的下一个节点指向新节点
    return n.value != null && casRight(succ, newSucc);
}
```

Part I：找到目标节点的位置并插入

（1）这里的目标节点是数据节点，也就是最底层的那条链；

（2）寻找目标节点之前最近的一个索引对应的数据节点（数据节点都是在最底层的链表上）；

（3）从这个数据节点开始往后遍历，直到找到目标节点应该插入的位置；

（4）如果这个位置有元素，就更新其值（onlyIfAbsent=false）；

（5）如果这个位置没有元素，就把目标节点插入；

（6）至此，目标节点已经插入到最底层的数据节点链表中了；

Part II：随机决定是否需要建立索引及其层次，如果需要则建立自上而下的索引

（1）取个随机数rnd，计算(rnd & 0x80000001)；

（2）如果不等于0，结束插入过程，也就是不需要创建索引，返回；

（3）如果等于0，才进入创建索引的过程（只要正偶数才会等于0）；

（4）计算`while (((rnd >>>= 1) & 1) != 0)`，决定层级数，level从1开始；

（5）如果算出来的层级不高于现有最高层级，则直接建立一条竖直的索引链表（只有down有值），并结束Part II；

（6）如果算出来的层级高于现有最高层级，则新的层级只能比现有最高层级多1；

（7）同样建立一条竖直的索引链表（只有down有值）；

（8）将头索引也向上增加到相应的高度，结束Part II；

（9）也就是说，如果层级不超过现有高度，只建立一条索引链，否则还要额外增加头索引链的高度（脑补一下，后面举例说明）；

Part III：将新建的索引节点（包含头索引节点）与其它索引节点通过右指针连接在一起（补上right指针）

（1）从最高层级的头索引节点开始，向右遍历，找到目标索引节点的位置；

（2）如果当前层有目标索引，则把目标索引插入到这个位置，并把目标索引前一个索引向下移一个层级；

（3）如果当前层没有目标索引，则把目标索引位置前一个索引向下移一个层级；

（4）同样地，再向右遍历，寻找新的层级中目标索引的位置，回到第（2）步；

（5）依次循环找到所有层级目标索引的位置并把它们插入到横向的索引链表中；

总结起来，一共就是三大步：

（1）插入目标节点到数据节点链表中；

（2）建立竖直的down链表；

（3）建立横向的right链表；

**添加元素栗子**

假设初始链表是这样：

![ConcurrentSkipList2](../images/format,png-1697288877754-6.png)

假如，现在要插入一个元素9。

（1）寻找目标节点之前最近的一个索引对应的数据节点，在这里也就是找到了 5 这个数据节点；

（2）从 5 开始向后遍历，找到目标节点的位置，也就是在 8 和 12 之间；

（3）插入 9 这个元素，Part I 结束；

![ConcurrentSkipList3](../images/format,png-1697288918008-9.png)

然后，计算其索引层级，假如是 3，也就是 `level=3` 

（1）建立竖直的 down 索引链表；

（2）超过了现有高度 2，还要再增加 `head` 索引链的高度；

（3）至此，Part II 结束；

![ConcurrentSkipList4](../images/format,png-1697288940131-12.png)

最后，把 `right `指针补齐

（1）从第 3 层的 `head` 往右找当前层级目标索引的位置；

（2）找到就把目标索引和它前面索引的 `right` 指针连上，这里前一个正好是 head；

（3）然后前一个索引向下移，这里就是 `head` 下移；

（4）再往右找目标索引的位置；

（5）找到了就把 `right` 指针连上，这里前一个是 3 的索引；

（6）然后 3 的索引下移；

（7）再往右找目标索引的位置；

（8）找到了就把 `right` 指针连上，这里前一个是 5 的索引；

（9）然后 5 下移，到底了，Part III 结束，整个插入过程结束；

![ConcurrentSkipList5](../images/format,png-1697288984520-15.png)



### 删除元素

```java
public V remove(Object key) {
    return doRemove(key, null);
}
 
final V doRemove(Object key, Object value) {
    // key不为空
    if (key == null)
        throw new NullPointerException();
    Comparator<? super K> cmp = comparator;
    // 自旋
    outer: for (;;) {
        // 寻找目标节点之前的最近的索引节点对应的数据节点
        // 为了方便，这里叫b为当前节点，n为下一个节点，f为下下个节点
        for (Node<K,V> b = findPredecessor(key, cmp), n = b.next;;) {
            Object v; int c;
            // 整个链表都遍历完了也没找到目标节点，退出外层循环
            if (n == null)
                break outer;
            // 下下个节点
            Node<K,V> f = n.next;
            // 再次检查
            // 如果n不是b的下一个节点了
            // 说明有其它线程先一步修改了，从头来过
            if (n != b.next)                    // inconsistent read
                break;
            // 如果下个节点的值奕为null了
            // 说明有其它线程标记该元素为删除状态了
            if ((v = n.value) == null) {        // n is deleted
                // 协助删除
                n.helpDelete(b, f);
                break;
            }
            // 如果b的值为空或者v等于n，说明b已被删除
            // 这时候n就是marker节点，那b就是被删除的那个
            if (b.value == null || v == n)      // b is deleted
                break;
            // 如果c<0，说明没找到元素，退出外层循环
            if ((c = cpr(cmp, key, n.key)) < 0)
                break outer;
            // 如果c>0，说明还没找到，继续向右找
            if (c > 0) {
                // 当前节点往后移
                b = n;
                // 下一个节点往后移
                n = f;
                continue;
            }
            // c=0，说明n就是要找的元素
            // 如果value不为空且不等于找到元素的value，不需要删除，退出外层循环
            if (value != null && !value.equals(v))
                break outer;
            // 如果value为空，或者相等
            // 原子标记n的value值为空
            if (!n.casValue(v, null))
                // 如果删除失败，说明其它线程先一步修改了，从头来过
                break;
 
            // P.S.到了这里n的值肯定是设置成null了
 
            // 关键！！！！
            // 让n的下一个节点指向一个market节点
            // 这个market节点的key为null，value为marker自己，next为n的下个节点f
            // 或者让b的下一个节点指向下下个节点
            // 注意：这里是或者||，因为两个CAS不能保证都成功，只能一个一个去尝试
            // 这里有两层意思：
            // 一是如果标记market成功，再尝试将b的下个节点指向下下个节点，如果第二步失败了，进入条件，如果成功了就不用进入条件
            // 二是如果标记market失败了，直接进入条件
            if (!n.appendMarker(f) || !b.casNext(n, f))
                // 通过findNode()重试删除（里面有个helpDelete()方法）
                findNode(key);                  // retry via findNode
            else {
                // 上面两步操作都成功了，才会进入这里，不太好理解，上面两个条件都有非"!"操作
                // 说明节点已经删除了，通过findPredecessor()方法删除索引节点
                // findPredecessor()里面有unlink()操作
                findPredecessor(key, cmp);      // clean index
                // 如果最高层头索引节点没有右节点，则跳表的高度降级
                if (head.right == null)
                    tryReduceLevel();
            }
            // 返回删除的元素值
            @SuppressWarnings("unchecked") V vv = (V)v;
            return vv;
        }
    }
    return null;
}
```

（1）寻找目标节点之前最近的一个索引对应的数据节点（数据节点都是在最底层的链表上）；

（2）从这个数据节点开始往后遍历，直到找到目标节点的位置；

（3）如果这个位置没有元素，直接返回 null，表示没有要删除的元素；

（4）如果这个位置有元素，先通过`n.casValue(v, null)`原子更新把其 `value` 设置为 null；

（5）通过`n.appendMarker(f)`在当前元素后面添加一个 `marker` 元素标记当前元素是要删除的元素；

（6）通过`b.casNext(n, f)`尝试删除元素；

（7）如果上面两步中的任意一步失败了都通过`findNode(key)`中的`n.helpDelete(b, f)`再去不断尝试删除；

（8）如果上面两步都成功了，再通过`findPredecessor(key, cmp)`中的`q.unlink(r)`删除索引节点；

（9）如果 `head`  的 `right` 指针指向了 null，则跳表高度降级；

**删除元素栗子**

假如初始跳表如下图所示，删除 9 这个元素

![ConcurrentSkipList6](../images/format,png-1697289114636-18.png)

（1）找到 9 这个数据节点；

（2）把 9 这个节点的 `value` 值设置为 null；

（3）在 9 后面添加一个marker节点，标记 9 已经删除了；

（4）让 8 指向 12；

（5）把索引节点与它前一个索引的 `right` 断开联系；

（6）跳表高度降级；

![ConcurrentSkipList7](../images/format,png-1697289142898-21.png)

### 查找元素

```java
public V get(Object key) {
    return doGet(key);
}
 
private V doGet(Object key) {
    // key不为空
    if (key == null)
        throw new NullPointerException();
    Comparator<? super K> cmp = comparator;
    // 自旋
    outer: for (;;) {
        // 寻找目标节点之前最近的索引对应的数据节点
        // 为了方便，这里叫b为当前节点，n为下个节点，f为下下个节点
        for (Node<K,V> b = findPredecessor(key, cmp), n = b.next;;) {
            Object v; int c;
            // 如果链表到头还没找到元素，则跳出外层循环
            if (n == null)
                break outer;
            // 下下个节点
            Node<K,V> f = n.next;
            // 如果不一致读，从头来过
            if (n != b.next)                // inconsistent read
                break;
            // 如果n的值为空，说明节点已被其它线程标记为删除
            if ((v = n.value) == null) {    // n is deleted
                // 协助删除，再重试
                n.helpDelete(b, f);
                break;
            }
            // 如果b的值为空或者v等于n，说明b已被删除
            // 这时候n就是marker节点，那b就是被删除的那个
            if (b.value == null || v == n)  // b is deleted
                break;
            // 如果c==0，说明找到了元素，就返回元素值
            if ((c = cpr(cmp, key, n.key)) == 0) {
                @SuppressWarnings("unchecked") V vv = (V)v;
                return vv;
            }
            // 如果c<0，说明没找到元素
            if (c < 0)
                break outer;
            // 如果c>0，说明还没找到，继续寻找
            // 当前节点往后移
            b = n;
            // 下一个节点往后移
            n = f;
        }
    }
    return null;
}
```

（1）寻找目标节点之前最近的一个索引对应的数据节点（数据节点都是在最底层的链表上）；

（2）从这个数据节点开始往后遍历，直到找到目标节点的位置；

（3）如果这个位置没有元素，直接返回 null，表示没有找到元素；

（4）如果这个位置有元素，返回元素的 `value` 值；

**查找元素举例**

假如有如下图所示这个跳表，查找 9 这个元素

![ConcurrentSkipList6](../images/format,png-1697289201319-24.png)

（1）寻找目标节点之前最近的一个索引对应的数据节点，这里就是 5；

（2）从 5 开始往后遍历，经过 8，到 9；

（3）找到了返回；

整个路径如下图所示：

![ConcurrentSkipList8](../images/format,png-1697289218938-27.png)
