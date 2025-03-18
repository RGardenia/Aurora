# DataStructure

# 一、基本概念

## 补码

特性：

1、一个负整数（或原码）与其补数（或补码）相加，和为模。

2、对一个整数的补码再求补码，等于该整数自身。

3、补码的正零与负零表示方法相同

按位取反 ~

~，用法只有一个那就是按位取反，需要注意的是：

- ~ 的按位取反，包括符号位
- 正数各位取反变为负数，显示时转化为其补码
- 负数本身需要先转换为补码（符号位不变，各位取反再加 1），再对其补码进行各位去反

1. ~ 5

5 的二进制为 0101，

~5

- （1）各位取反，1010
- （2）变为负数，转化为其补码形式（符号位保持不变），各位取反 1（1101），再加1（1110），也即 -6

```
>> ~5
>> -6
```

2. ~(-5)

-5 因为是负数，存储时存储的是其补码：

- -5 的补码是：1011
- ~(-5)将其各位取反（包括符号位），也即 0100（4）

```
>> ~(-5)
>> 4
```







# 二、线性表







# 三、栈与队列







# 四、串







# 五、树







# 六、图







# 七、查找

## 7.1.二分查找

```java
package com.ymy.search;

/**
 * 二分查找：前提必须是有序序列
 */
public class BinarySearch {

    private Comparable[] Keys;

    public void setKeys(Comparable[] keys) {
        Keys = keys;
    }

    /**
     * 二分查找(迭代)
     *
     * @param Key 要查找的值
     * @return 找到的值的下标
     */
    public int rankIter(Comparable Key) {
        int lo = 0, hi = Keys.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = Key.compareTo(Keys[mid]);
            if (cmp < 0)
                hi = mid - 1;
            else if (cmp > 0)
                lo = mid + 1;
            else
                return mid;
        }
        return -1;
    }

    /**
     * 二分查找(递归)
     *
     * @param Key 要查找的值
     * @return 找到的值的下标
     */
    public int rank(Comparable Key) {
        return rank(Key, 0, Keys.length - 1);
    }

    /**
     * 递归的二分查找
     *
     * @param key 要查找的值
     * @param lo  开启下标
     * @param hi  结束下标
     * @return 找到的值的下标
     */
    private int rank(Comparable key, int lo, int hi) {
        if (hi < lo) return -1; // 递归的出口,证明没有搜索到
        int mid = (lo + hi) >>> 1;
        int cmp = key.compareTo(Keys[mid]);
        if (cmp < 0)
            return rank(key, lo, mid - 1);
        else if (cmp > 0)
            return rank(key, mid + 1, hi);
        else
            return mid;
    }

    public static void main(String[] args) {
        BinarySearch bs = new BinarySearch();
        bs.setKeys(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        System.out.println(bs.rank(1)); // 0
        System.out.println(bs.rank(90)); // -1
        System.out.println(bs.rankIter(5)); // 4
        System.out.println(bs.rankIter(-1)); // -1
    }
}
```

## 7.2.二叉查找树

```java
package com.ymy.search;

/**
 * 二叉搜索树
 *
 * @param <Key>   键
 * @param <Value> 值
 */
public class BST<Key extends Comparable<Key>, Value> {
    /**
     * 定义节点Node
     */
    private class Node {
        private Key key;            // 键
        private Value val;          // 值
        private Node left, right;   // 指向子树的链接
        private int N;              // 以该节点为根的子树中的结点总数

        public Node(Key key, Value val, int N) {
            this.key = key;
            this.val = val;
            this.N = N;
        }
    }

    /**
     * BST的根结点
     */
    private Node root;

    /**
     * @return 获得BST的结点总数
     */
    public int size() {
        return size(root);
    }

    /**
     * @param x 当前结点Node
     * @return 获得以当前结点为根的子树中节点总数
     */
    private int size(Node x) {
        if (x == null)
            return 0;
        else
            return x.N;
    }

    /**
     * BST中插入键值,并初始化根结点root
     *
     * @param key 键
     * @param val 值
     */
    public void put(Key key, Value val) {
        root = put(root, key, val);
    }

    /**
     * 在当前结点下插入键值
     *
     * @param x   当前结点
     * @param key 键
     * @param val 值
     * @return 当前结点
     */
    private Node put(Node x, Key key, Value val) {
        // 如果根结点root没有创建就创建根结点
        if (x == null)
            return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if (cmp < 0)
            x.left = put(x.left, key, val);
        else if (cmp > 0)
            x.right = put(x.right, key, val);
        else
            x.val = val;
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    /**
     * BST的root为根结点查找key所对应的值
     *
     * @param key 键
     * @return 值 val
     */
    public Value get(Key key) {
        return get(root, key);
    }

    /**
     * 在以x为根结点的子树中查找Key所对应的值
     *
     * @param x   子树的根结点
     * @param key 键
     * @return 值 Val
     */
    private Value get(Node x, Key key) {
        // 递归的出口,如果找不到就返回null
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0)
            return get(x.left, key);
        if (cmp > 0)
            return get(x.right, key);
        else
            return x.val;
    }
}
```

使用二叉查找树的算法的运行时间取决于树的形状，而树的形状又取决于键被插入的先后顺序。

在最好的情况下，一颗含有N个结点的树是完全平衡的，每条空链接和根结点的距离都为~`lgN`。

在最坏的情况下，搜索路径上可能有N个节点，二叉树会变成单链表的数据结构。

但是在一般情况下，树的情况和最好情况更接近。

对于这种二分的模型而言，二叉查找树和快速排序几乎就是"双胞胎"。树的根结点就是快速排序中的第一个切分元素（左侧的键都比它小，右侧的键都比它大），而对于所有的子树同样适用，这和快速排序中対子数组的递归排序完全对应。

## 7.3.红黑树

红黑树的定义是含有红黑链并满足下列条件的二叉查找树：

- 红链接均为左链接；
- 没有任何一个结点同时和两条红链接相连；
- 该树是**完美黑色平衡**的，即任意空链接到根结点的路径上的黑链接数量相同。

颜色表示：

- 每个结点都只会有一条指向自己的链接（从它的父结点指向它），我们将链接的颜色保存在表示结点`Node`数据类型的布尔变量`color`中；
- 如果指向当前结点的链接是红色的，那么布尔变量`color`的值就为`true`，黑色则为`false`，**我们约定空链接为黑色**。

红黑树的插入操作遵守以下规则：

- 如果右子结点是红色而左子结点是黑色的，进行左旋转；
- 如果左子结点是红色的且它的左子结点也是红色的，进行右旋转；
- 如果左右子结点均为红色，进行颜色转换。

```java
package com.ymy.search;

/**
 * 红黑树
 *
 * @param <Key>   键
 * @param <Value> 值
 */
public class RedBlackBST<Key extends Comparable<Key>, Value> {

    private static final boolean RED = true;    // 红链接
    private static final boolean BLACK = false; // 黑链接

    /**
     * 定义结点Node
     */
    private class Node {
        Key key;           // 键
        Value val;         // 值
        Node left, right;  // 指向子树的链接
        int N;             // 以该节点为根的子树中的结点总数
        boolean color;     // 其父结点指向它的链接的颜色

        public Node(Key key, Value val, int N, boolean color) {
            this.key = key;
            this.val = val;
            this.N = N;
            this.color = color;
        }
    }

    /**
     * 定义红黑树根结点
     */
    private Node root;

    /**
     * @return 红黑树结点总数
     */
    public int size() {
        return size(root);
    }

    /**
     * 红黑树的root为根结点查找key所对应的值
     *
     * @param key 键
     * @return 值 val
     */
    public Value get(Key key) {
        return get(root, key);
    }

    /**
     * 在以h为根结点的子树中查找Key所对应的值
     *
     * @param h   子树的根结点
     * @param key 键
     * @return 值 val
     */
    private Value get(Node h, Key key) {
        if (h == null) return null;
        int cmp = key.compareTo(h.key);
        if (cmp < 0)
            return get(h.left, key);
        else if (cmp > 0)
            return get(h.right, key);
        else
            return h.val;
    }

    /**
     * 红黑树根结点下插入键值
     *
     * @param key 键
     * @param val 值
     */
    public void put(Key key, Value val) {
        root = put(root, key, val);
        root.color = BLACK;
    }


    /**
     * 红黑树中当前结点下插入键值
     *
     * @param h   当前结点
     * @param key 键
     * @param val 值
     * @return 当前结点
     */
    private Node put(Node h, Key key, Value val) {
        if (h == null)
            return new Node(key, val, 1, RED);
        int cmp = key.compareTo(h.key);
        if (cmp < 0)
            h.left = put(h.left, key, val);
        else if (cmp > 0)
            h.right = put(h.right, key, val);
        else
            h.val = val;
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        h.N = size(h.left) + size(h.right) + 1;

        return h;
    }

    /**
     * @param x 当前结点Node
     * @return 获得以当前结点为根的子树中节点总数
     */
    private int size(Node x) {
        if (x == null)
            return 0;
        else
            return x.N;
    }

    /**
     * 指向当前结点的链接是否是红链接
     *
     * @param x 当前结点Node
     * @return true表示是红链接;false表示是黑链接
     */
    private boolean isRed(Node x) {
        if (x == null)
            return false;
        return x.color == RED;
    }

    /**
     * 左旋
     *
     * @param h 当前结点
     * @return 左旋后的父结点
     */
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        x.N = h.N;
        h.N = 1 + size(h.left) + size(h.right);
        return x;
    }

    /**
     * 右旋
     *
     * @param h 当前结点
     * @return 右旋后的父结点
     */
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        x.N = h.N;
        h.N = 1 + size(h.left) + size(h.right);
        return x;
    }

    /**
     * 颜色转换
     *
     * @param h 当前结点
     */
    private void flipColors(Node h) {
        h.color = RED;
        h.left.color = BLACK;
        h.right.color = BLACK;
    }
}
```

## 7.4.散列表

`java`对于`hashCode()`的约定：

- 每种数据类型都需要相应的散列函数，于是`java`令所有数据类型都继承了一个能够返回一个32位比特整数的`hashCode()`方法。每一种数据类型的`hashCode()`方法都必须和`equals()`方法一致；
- 如果`a.equals(b)`返回`true`，那么`a.hashCode()`的返回值必然和`b.hashCode()`的返回值相同；
- 如果两个对象的`hashCode()`方法的返回值不同，那么我们就知道这两个对象是不同的。
- 但是如果两个对象的`hahCode()`方法的返回值相同，这两个对象也有可能不同，我们还需要用`equals()`方法进行判断。

> 自定义类型中`hashCode()`的实现

```java
public class Transaction {
    private final String who;
    private final Date when;
    private final double amount;
    
    public int hashCode() {
        int hash = 1;
        int hash = 31 * hash + this.who.hashCode();
        hash = 31 * hash + this.when.hashCode();
        hash = 31 * hash + Double.valueOf(this.amount).hashCode();
        return hash;
    }
}
```

> 基于拉链法的散列表

```java
package com.ymy.search;

/**
 * 基于拉链法的散列表
 *
 * @param <Key>   键
 * @param <Value> 值
 */
public class SeparateChainingHashST<Key, Value> {

    /**
     * 键值对总数
     */
    private int N;

    /**
     * 散列表的大小
     */
    private int M;

    /**
     * 存放链表对象的数组
     */
    private SequentialSearchST<Key, Value>[] st;

    public SeparateChainingHashST() {
        this(997);
    }

    public SeparateChainingHashST(int M) {
        this.M = M;
        st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[M];
        for (int i = 0; i < M; i++) {
            st[i] = new SequentialSearchST();
        }
    }

    public Value get(Key key) {
        return (Value) st[hash(key)].get(key);
    }

    /**
     * 散列表中插入键值
     *
     * @param key 键
     * @param val 值
     */
    public void put(Key key, Value val) {
        st[hash(key)].put(key, val);
    }

    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }

    public static void main(String[] args) {
        SeparateChainingHashST<Integer, Object> hashST = new SeparateChainingHashST<>();
        for (int i = 1; i <= 10; i++) {
            hashST.put(i, 100 + i);
        }
        System.out.println(hashST.get(5));
    }
}
```

```java
package com.ymy.search;

/**
 * 单链表(顺序搜索)
 *
 * @param <Key>   键
 * @param <Value> 值
 */
public class SequentialSearchST<Key, Value> {

    /**
     * 链表结点定义
     */
    private class Node {
        Key key;
        Value val;
        Node next;

        public Node(Key key, Value val, Node next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    /**
     * 链表首结点
     */
    private Node first;

    /**
     * 单链表中插入键值
     * 查找给定的键,找到则更新其值,否则在表中新建结点
     *
     * @param key 键
     * @param val 值
     */
    public void put(Key key, Value val) {
        for (Node x = first; x != null; x = x.next) {
            // 命中,更新结点的值
            if (key.equals(x.key)) {
                x.val = val;
                return;
            }
        }
        // 未命中,新建结点(妙) 头插法
        first = new Node(key, val, first);
    }

    /**
     * 根据键在链表中查找值
     *
     * @param key 键
     * @return 值
     */
    public Value get(Key key) {
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) {
                return x.val;  // 命中
            }
        }
        return null;          // 未命中
    }
}
```

## 7.5.HashMap

> 1、HashMap内部数据结构

`JDK1.8`版本，`HashMap`内部使用数组 + 链表 + 红黑树。

![HashMap](https://img-blog.csdnimg.cn/20200812143831704.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1JyaW5nb18=,size_16,color_FFFFFF,t_70#pic_center)

> 2、HashMap的数据插入原理

![HashMap插入流程](https://img-blog.csdnimg.cn/20200812145624476.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1JyaW5nb18=,size_16,color_FFFFFF,t_70#pic_center)

流程：

- 判断数组是否为空，为空进行初始化；数组不为空，计算`Key`的`hash`值，通过`(length - 1) & hash`计算数组的下标。
- 查看`table[index]`是否存在数据，没有数据就构造一个`Node`结点存放在`table[index]`中；存在数据，说明发生了`Hash`冲突，继续判断`Key`是否相等；
- `Key`相等就替换`Value`的值；如果不相等，就将新的结点插入到链表或者红黑树中。
- 插入完成后判断当前结点数是否大于阈值，如果大于阈值`HashMap`开始扩容为原来数组的2倍。

> 3、HashMap初始化，如何设定初始容量的大小？

```java
/**
* Returns a power of two size for the given target capacity.
* 如果假设cap等于10
*/
static final int tableSizeFor(int cap) {
    int n = cap - 1;  // 9
    n |= n >>> 1;     // n = 9 | 4   ==> n = 13
    n |= n >>> 2;     // n = 13 | 3  ==> n = 15 
    n |= n >>> 4;     // n = 15 | 0  ==> n = 15
    n |= n >>> 8;     // n = 15 | 0  ==> n = 15
    n |= n >>> 16;    // n = 15 | 0  ==> n = 15
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1; // 16
}
```

如果`new HashMap()`不指定容量，默认数组大小就是16，负载因子是0.75,；如果`new HashMap(capacity)`指定了初始容量，那么初始容量为大于`capacity`的2的整数次方，例如：传10，`HashMap`的初始容量就是16。

> 4、HashMap的哈希函数是如何设计的？

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

`hash()`函数式先拿到`key`的`hashCode`，是一个32位的`int`值，然后让`hashCode`的高16位和低16位做异或运算。

这样设计的原因：

- 尽可能降低`hash`碰撞，越分散越好；
- 算法一定要尽可能高效，采用位运算，效率会高。

> 5、为什么采用hashcode的高16位和低16位异或能降低hash碰撞？hash函数能不能直接用key的hashcode？

因为`Key.hashCode()`函数调用的是`Key`键值类型自带的哈希函数，返回`int`型散列值。`int`值范围为`-2147483648~2147483647`，前后加起来大概40亿的映射空间。只要哈希函数映射得比较均匀松散，一般应用是很难出现碰撞的。但问题是一个40亿长度的数组，内存是放不下的。你想，如果`HashMap`数组的初始大小才16，用之前需要对数组的长度取模运算，得到的余数才能用来访问数组下标。

![计算索引下标](https://img-blog.csdnimg.cn/20200812153340683.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1JyaW5nb18=,size_16,color_FFFFFF,t_70#pic_center)

> 6、JDK1.8对hash函数做了优化，1.8还有别的优化吗？

`jdk1.8`还有三点主要的优化：

- 1.数组 + 链表 ==>  数组 + 链表或红黑树；
- 2.链表的插入方式从头插法改成了尾插法，简单说就是插入时，如果数组位置上已经有元素，1.7将新元素放到数组中，原始节点作为新节点的后继节点，1.8遍历链表，将元素放置到链表的最后；
- 3.扩容的时候1.7需要对原数组中的元素进行重新hash定位在新数组的位置，1.8采用更简单的判断逻辑，位置不变或索引+旧容量大小；
- 4.在插入时，1.7先判断是否需要扩容，再插入，1.8先进行插入，插入完成再判断是否需要扩容；

> 7、分别讲讲这几点优化？

- 1.防止发生hash冲突，链表长度过长，将时间复杂度由`O(n)`降为`O(logn)`;
- 2.因为1.7头插法扩容时，头插法会使链表发生反转，多线程环境下会产生环；A线程在插入节点B，B线程也在插入，遇到容量不够开始扩容，重新hash，放置元素，采用头插法，后遍历到的B节点放入了头部，这样形成了环，如下图所示：

​     ![头插法](https://img-blog.csdnimg.cn/20200812161636256.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1JyaW5nb18=,size_16,color_FFFFFF,t_70#pic_center)

- 3.扩容的时候为什么1.8 不用重新hash就可以直接定位原节点在新数据的位置呢?

  这是由于扩容是扩大为原数组大小的2倍，用于计算数组位置的掩码仅仅只是高位多了一个1，怎么理解呢？

  扩容前长度为16，用于计算`(n-1) & hash` 的二进制`n - 1`为`0000 1111`，扩容为32后的二进制就高位多了1，为`0001 1111`。因为是& 运算，1和任何数 & 都是它本身，那就分二种情况，如下图：原数据`hashcode`高位第4位为0和高位为1的情况；第四位高位为0，重新`hash`数值不变，第四位为1，重新`hash`数值比原来大16（旧数组的容量）。

  ![为什么不用rehash？](https://img-blog.csdnimg.cn/20200812162404729.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1JyaW5nb18=,size_16,color_FFFFFF,t_70#pic_center)

> 8、那你平常怎么解决HashMap线程不安全的问题？

` Java中`有`HashTable`、`Collections.synchronizedMap`、以及`ConcurrentHashMap`可以实现线程安全的`Map`。

- `HashTable`是直接在操作方法上加`synchronized`关键字，锁住整个数组，粒度比较大;

```java
// HashTable中的put()方法
public synchronized V put(K key, V value) {
    // Make sure the value is not null
    if (value == null) {
        throw new NullPointerException();
    }

    // Makes sure the key is not already in the hashtable.
    Entry<?,?> tab[] = table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    @SuppressWarnings("unchecked")
    Entry<K,V> entry = (Entry<K,V>)tab[index];
    for(; entry != null ; entry = entry.next) {
        if ((entry.hash == hash) && entry.key.equals(key)) {
            V old = entry.value;
            entry.value = value;
            return old;
        }
    }

    addEntry(hash, key, value, index);
    return null;
}
```

- `Collections.synchronizedMap`是使用`Collections`集合工具的内部类，通过传入Map封装出一个`SynchronizedMap`对象，内部定义了一个对象锁，方法内通过对象锁实现；

```java
private static class SynchronizedMap<K,V>implements Map<K,V>, Serializable {
    final Object      mutex;
    public V put(K key, V value) {
        synchronized (mutex) {return m.put(key, value);}
    }
}
```

- `ConcurrentHashMap`使用CAS算法，让并发度大大提高。`ConcurrentHashMap`成员变量使用`volatile `修饰，免除了指令重排序，同时保证内存可见性，另外使用CAS操作和`synchronized`结合实现赋值操作，多线程操作只会锁住当前操作索引的节点。

> 9、链表转红黑树是链表长度达到阈值，这个阈值是多少？

阈值是8，红黑树转链表阈值为6。

> 10、为什么是8，不是16，32甚至是7 ？又为什么红黑树转链表的阈值是6，不是8了呢？

因为经过计算，在`hash`函数设计合理的情况下，发生`hash`碰撞8次的几率为百万分之6，概率说话。。因为8够用了，至于为什么转回来是6，因为如果`hash`碰撞次数在8附近徘徊，会一直发生链表和红黑树的互相转化，为了预防这种情况的发生。

> 11、HashMap内部节点是有序的吗？那有没有有序的Map？

`HashMap` 是无序的，根据`hash`值随机插入。`LinkedHashMap `和 `TreeMap`是有序的。



`LinkedHashMap`内部维护了一个单链表，有头尾节点，同时`LinkedHashMap`节点Entry内部除了继承`HashMap`的`Node`结点，其中有`before` 和 `after`用于标识前置节点和后置节点。可以实现按插入的顺序或访问顺序排序。

```java
public class LinkedHashMap<K,V> extends HashMap<K,V> implements Map<K,V>{
    
    transient LinkedHashMap.Entry<K,V> head;
    
    transient LinkedHashMap.Entry<K,V> tail;
    
    static class Entry<K,V> extends HashMap.Node<K,V> {
        Entry<K,V> before, after;
        Entry(int hash, K key, V value, Node<K,V> next) {
            super(hash, key, value, next);
        }
    }
    
    // link at the end of list
    private void linkNodeLast(LinkedHashMap.Entry<K,V> p) {
        LinkedHashMap.Entry<K,V> last = tail;
        tail = p;
        if (last == null)
            head = p;
        else {
            p.before = last;
            last.after = p;
        }
    }
}
```

![LinkedHashMap](http://md.demojie.com/img/20200329020913.png)

`TreeMap`是按照`Key`的自然顺序或者`Comparator`的顺序进行排序，内部是通过红黑树来实现。所以要么`Key`所属的类实现`Comparable`接口，或者自定义一个实现了`Comparator`接口的比较器，传给`TreeMap`用于`key`的比较。





# 八、排序

## 8.1.封装工具类

```java
/**
 * 学习排序算法需要使用的工具类
 */
public class SortedUtils {
    
    /**
     * 比较v和w的大小
     *
     * @return v比w小返回true, 反之返回false
     */
    public static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    // 交换数组中两个元素的位置
    public static void exchange(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    // 测试数组元素是否升序排序
    public static boolean isSorted(Comparable[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            if (less(a[i + 1], a[i])) {
                return false;
            }
        }
        return true;
    }
    
    // 输出数组的内容
    public static void show(Comparable[] a) {
        int N = a.length;
        String ret = "[";
        for (int i = 0; i < N; i++) {
            if (i != (N - 1)) {
                ret += a[i] + ",";
            } else {
                ret += a[N - 1] + "]";
            }
        }
        System.out.println(ret);
    }    
}
```

## 8.2.初级排序算法

### 8.2.1.选择排序

选择排序算法：

- 找到数组中最小的那个元素`min`；
- 将`min`和数组中第一个元素交换位置（如果第一个元素就是最小元素那么它就和自己交换）；
- 在剩下的元素中找到最小的元素，将它与数据的第二个元素交换位置。
- 执行上述循环，直到将整个数组排序。

```java
import static com.ymy.sort.utils.SortedUtils.*;

/**
 * 选择排序：不断地选择剩余元素之中的最小者
 */
public class Selection {

    public static void main(String[] args) {
        Integer[] a = {-2, 100, 20, 9};
        sort(a);
        show(a); // [-2, 9, 20, 100]
    }

    public static void sort(Comparable[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            int min = i;
            for (int j = i + 1; j < N; j++) {
                if (less(a[j], a[min])) {
                    min = j;
                }
            }
            exchange(a, i, min);
        }
    }
}
```

选择排序特点：

- **运行时间和输入无关。**一个已经有序的数组或是主键全部相等的数组和一个元素随机排列的数组所用的排序时间是一样长的。
- **数据移动最少。**每次交换都会改变两个数组元素的值，因此选择排序用了N次交换——交换次数和数组的大小是线性关系。

### 8.2.2.插入排序

插入排序算法：通常人们整理桥牌的方法是一张一张的来，将每一张牌插入到其他已经有序的牌中的适当位置。在计算机的实现中，为了给要插入的元素腾出空间，我们需要将其余所有元素在插入之前都向右移动一位。

> 基本的插入排序算法

```java
import static com.ymy.sort.utils.SortedUtils.*;

/**
 * 插入排序
 * [44, 69, 40, 98, 80, 25, 36, 96, 17, 11]
 *
 * [44, 69, 40, 98, 80, 25, 36, 96, 17, 11] i=1
 * [40, 44, 69, 98, 80, 25, 36, 96, 17, 11] i=2
 * [40, 44, 69, 98, 80, 25, 36, 96, 17, 11] i=3
 * [40, 44, 69, 80, 98, 25, 36, 96, 17, 11] i=4
 * [25, 40, 44, 69, 80, 98, 36, 96, 17, 11] i=5
 * [25, 36, 40, 44, 69, 80, 98, 96, 17, 11] i=6
 * [25, 36, 40, 44, 69, 80, 96, 98, 17, 11] i=7
 * [17, 25, 36, 40, 44, 69, 80, 96, 98, 11] i=8
 * [11, 17, 25, 36, 40, 44, 69, 80, 96, 98] i=9
 */
public class Insertion {

    public static void main(String[] args) {
        Integer[] a = new Integer[]{44, 69, 40, 98, 80, 25, 36, 96, 17, 11};
        sort(a);
    }

    public static void sort(Comparable[] a) {
        int N = a.length;
        for (int i = 1; i < N; i++) {
            for (int j = i; j > 0 && less(a[j], a[j - 1]); j--) {
                exchange(a, j, j - 1);
            }
            //show(a);
        }
    }
}
```

插入排序与选择排序的异同点：

- 与选择排序一样，当前索引左边的所有元素都是有序的，但是它们的最终位置还不能确定，为了给更小的元素腾出空间，它们可能会被移动。但是当索引达到数组的右端时，数组排序就完成了。
- 和选择排序不同的是，**插入排序所需的时间取决于输入中元素的初始顺序。**

> 插入排序的改进算法

要大幅度提高插入排序速度并不难，只需要在内循环中将较大的元素都向右移动而不总是交换两个元素，这样访问数组的次数就能减半。

```java
import static com.ymy.sort.utils.SortedUtils.*;

/**
 * 插入排序改进算法
 * [44, 69, 40, 98, 80, 25, 36, 96, 17, 11]
 *
 * [44, 69, 40, 98, 80, 25, 36, 96, 17, 11] i=1
 * [40, 44, 69, 98, 80, 25, 36, 96, 17, 11] i=2
 * [40, 44, 69, 98, 80, 25, 36, 96, 17, 11] i=3
 * [40, 44, 69, 80, 98, 25, 36, 96, 17, 11] i=4
 * [25, 40, 44, 69, 80, 98, 36, 96, 17, 11] i=5
 * [25, 36, 40, 44, 69, 80, 98, 96, 17, 11] i=6
 * [25, 36, 40, 44, 69, 80, 96, 98, 17, 11] i=7
 * [17, 25, 36, 40, 44, 69, 80, 96, 98, 11] i=8
 * [11, 17, 25, 36, 40, 44, 69, 80, 96, 98] i=9
 */
public class InsertionHalfExchange {

    public static void main(String[] args) {
        Integer[] a = new Integer[]{44, 69, 40, 98, 80, 25, 36, 96, 17, 11};
        sort(a);
    }

    public static void sort(Comparable[] a) {
        int N = a.length;
        for (int i = 1; i < N; i++) {
            Comparable temp = a[i]; // 用temp来保存当前索引值
            int j = i;
            while (j > 0 && less(temp, a[j - 1])) {
                // 当前索引值比前边的数都小时，就整体后移一位(当前索引左边的元素都是有序的)
                a[j] = a[j - 1]; 
                j--;
            }
            a[j] = temp; // 最后再temp的值插入
            //show(a);
        }
    }
}
```

### 8.2.3.希尔排序

希尔排序算法：

- 希尔排序是基于插入排序的快速的排序算法。
- 对于大规模乱序数组插入排序很慢，因为它只会交换相邻元素，元素只能一点一点地从数组的一端移动到另一端。
- **希尔排序的思想是使数组中任意间隔为h的元素都是有序的。**这样的数组被称为h有序数组。换句话说，一个h有序数组就是h个互相独立的有序数组编织在一起组成的一个数组。

![h有序数组](https://img-blog.csdnimg.cn/20200808141957212.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1JyaW5nb18=,size_16,color_FFFFFF,t_70)

> 希尔排序算法

```java
import static com.ymy.sort.utils.SortedUtils.*;

/**
 * 希尔排序
 * [S, H, E, L, L, S, O, R, T, E, X, A, M, p, L, E]
 * 
 * [L, E, E, A, M, H, L, E, S, S, O, L, T, p, X, R]
 * [A, E, E, E, H, L, L, L, M, O, R, S, S, T, X, p]
 */
public class Shell {

    public static void main(String[] args) {
        String[] a = new String[]{"S", "H", "E", "L", "L", "S", "O", "R", "T", "E", "X", "A", "M", "p", "L", "E"};
        sort(a);
    }
    
    public static void sort(Comparable[] a) {
        int N = a.length;
        int h = 1;
        while (h < N / 3) h = 3 * h + 1;
        while (h >= 1) {
            for (int i = h; i < N; i++) {
                for (int j = i; j >= h && less(a[j], a[j - h]); j -= h) {
                    exchange(a, j, j - h);
                }
            }
            h = h / 3;
        }
    }
}
```

### 8.2.4.比较三种排序算法

```java
import com.ymy.sort.Insertion;
import com.ymy.sort.Selection;
import com.ymy.sort.InsertionHalfExchange;
import com.ymy.sort.Shell;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;


/**
 * 比较排序算法
 */
public class SortCompare {

    public static Double time(String alg, Comparable[] a) {
        Stopwatch timer = new Stopwatch();
        if (alg.equals("Selection")) Selection.sort(a);
        if (alg.equals("Insertion")) Insertion.sort(a);
        if (alg.equals("InsertionHalfExchange")) InsertionHalfExchange.sort(a);
        if (alg.equals("Shell")) Shell.sort(a);
        return timer.elapsedTime();
    }

    public static double timeRandomInput(String alg, int N, int T) {
        double total = 0.0;
        Double[] a = new Double[N];
        for (int t = 0; t < T; t++) {
            for (int i = 0; i < N; i++) {
                a[i] = StdRandom.uniform();
            }
            total += time(alg, a);
        }
        return total;
    }


    public static void main(String[] args) {
        // 下面使用不同的算法対长度为10000的随机数组进行排序,执行100次所需的时间
        System.out.println(timeRandomInput("Selection", 1_0000, 100)); // 12.11s
        System.out.println(timeRandomInput("Insertion", 1_0000, 100)); // 15.97s
        System.out.println(timeRandomInput("InsertionHalfExchange", 1_0000, 100)); // 7.39s
        System.out.println(timeRandomInput("Shell", 1_0000, 100)); // 0.28s
    }
}
```

**希尔排序算法比插入排序和选择排序都要快的多，并且数组越大，优势越大。**通过测试，长度100万的随机数组，使用希尔排序只需要1.5秒的时间，希尔排序能够解决一些初级排序算法无法解决的问题。

有经验的程序员有时会选择希尔排序，因为对于中等大小的数组它的运行时间是可以接受的。**希尔排序的代码量很小，并且不需要额外的运行空间**，后面接触到的更加高效的算法，对于很大的N，它们可能只会比希尔排序快两倍（可能还打不到）。

## 8.3.归并排序

归并排序：

- 要将一个数组排序，可以先（递归地）将 它分成两半分别排序，然后将结果归并起来。
- 归并排序最引人注目的性质是它能够保证将任意长度为N的数组排序所需的时间和`NlogN`成正比，它的主要 缺点是所需要的额外空间和`N`成正比。      

```java
package com.ymy.sort;

import com.ymy.sort.utils.SortCompare;

import static com.ymy.sort.utils.SortedUtils.*;

/**
 * 归并排序
 */
public class Merge {
    /**
     * 归并需要的额外的数组
     */
    private static Comparable[] aux;

    public static void sort(Comparable[] a) {
        aux = new Comparable[a.length];  // 数组aux初始化
        sort(a, 0, a.length - 1);
    }

    /**
     * 归并排序
     *
     * @param a  目标数组
     * @param lo 开始下标
     * @param hi 结束下标
     */
    private static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) return; // 递归的出口
        int mid = (lo + hi) >>> 1; // 计算mid中间值
        sort(a, lo, mid); // 将左半边排序
        sort(a, mid + 1, hi); // 将右半边排序
        merge(a, lo, mid, hi); // 归并结果
    }

    /**
     * 原地归并方法
     * 将a[lo...mid]和a[mid+1...hi]归并
     * 前提：a[lo...mid]和a[mid+1...hi]都是有序的！
     * For Example：
     * 0  1  2  3  4  5  6  7    8  9  10 11 12 13 14 15
     * E  E  G  M  O  R  R  S |  A  E  E  L  M  P  T  X
     * 主要比较aux[i]和aux[j]的大小,只要满足less(aux[j],a[i])那么a[k]=aux[j++],
     * 其他情况下就是a[k]=aux[i++];不过当i>mid的时候,就是左边遍历完了,就是a[k]=aux[j++];
     * 右边遍历完了就是a[k]=aux[i++]
     *
     * @param a   目标数组
     * @param lo  开始下标
     * @param mid 中间下标
     * @param hi  结束下标
     */
    private static void merge(Comparable[] a, int lo, int mid, int hi) {
        int i = lo;
        int j = mid + 1;

        // 将a[lo..hi]复制到aux[lo..hi]
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }

        // 将aux数组归并到a数组中
        for (int k = lo; k <= hi; k++) {
            if (i > mid)
                a[k] = aux[j++];
            else if (j > hi)
                a[k] = aux[i++];
            else if (less(aux[j], aux[i]))
                a[k] = aux[j++];
            else
                a[k] = aux[i++];
        }
    }

    public static void main(String[] args) {

        /**
         * 1、测试基本的排序
         */
        Integer[] a = new Integer[]{44, 69, 40, 98, 80, 25, 36, 96, 17, 11};
        sort(a);
        show(a); // [11, 17, 25, 36, 40, 44, 69, 80, 96, 98]

        /**
         * 2、测试性能：
         * 长度为100W的随机数组排序1次需要0.833s
         * 长度为1000W的随机数组排序1次需要8.653s长度为1000W的随机数组排序1次需要8.653s
         */
        System.out.println(SortCompare.timeRandomInput("Merge", 1000_000, 1));
        System.out.println(SortCompare.timeRandomInput("Merge", 1000_0000, 1));
    }
}
```

归并排序是应用高效算法设计中**分治思想**的最典型的一个例子。这和之前的初级排序算法不可同日而语，它表明我们只需要比遍历整个数组多个对数因子的时间就能将一个庞大的数组排序。**可以用归并排序处理数百万甚至更大规模的数组**！



## 8.4.快速排序

快速排序：

- **快速排序是一种分治的排序算法**。它将一个数组分成两个子数组，将两部分独立的排序。
- 快速排序和归并排序是互补的：归并排序将数组分成两个子数组分别排序，并将有序的子数组归并以将整个数组排序；而快速排序将数组排序的方式则是当两个子数组都有序时整个数组也就自然有序了。
- 在归并排序中，递归调用发生在处理整个数组之前，一个数组被切分成两半；在快速排序中，递归调用发生在处理整个数组之后，切分的位置取决于数组的内容。

```java
package com.ymy.sort;

import com.ymy.sort.utils.SortCompare;

import static com.ymy.sort.utils.SortedUtils.*;

/**
 * 快速排序
 */
public class Quick {

    /**
     * 快速排序封装
     *
     * @param a 目标数组
     */
    public static void sort(Comparable[] a) {
        sort(a, 0, a.length - 1);
    }

    /**
     * 快速排序
     *
     * @param a  目标数组
     * @param lo 开始下标
     * @param hi 结束下标
     */
    private static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) return; // 递归的出口
        int j = partition(a, lo, hi); // 获得数组的切分点
        sort(a, lo, j - 1); // 将左半部分a[lo..j-1]排序
        sort(a, j + 1, hi); // 将右半部分a[j+1..hi]排序
    }

    /**
     * 获取快速排序切分点(partition:隔断)
     * 将数组切分为a[lo..i-1],a[i],a[i+1..hi]
     * <p>
     * 0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15
     * K  R  A  T  E  L  E  P  U  I  M  Q  C  X  O  S
     * v=K;
     * less(a[1],K); 指针从左向右移动
     * less(K,a[15]); 指针从右向左移动
     * 当左-->右的时候,说明左边指针指向的值是大于K的,所以要交换下标i,j的值。
     * 当右-->左的时候,说明右边指针指向的值是小于K的,所以要交换下标i,j的值。
     *
     * @param a  目标数组
     * @param lo 开始下标
     * @param hi 结束下标
     * @return 快速排序的切分点
     */
    private static int partition(Comparable[] a, int lo, int hi) {
        int i = lo, j = hi + 1; // 左右扫描指针
        Comparable v = a[lo]; // 切分数组的元素
        while (true) {
            while (less(a[++i], v)) if (i == hi) break; // 指针从左向右移动
            while (less(v, a[--j])) if (j == lo) break; // 指针从右向左移动
            if (i >= j) break;  // 退出循环的临界条件i大于j
            exchange(a, i, j);
        }
        exchange(a, lo, j); // 将v=a[j]放入正确的位置
        return j;
    }

    public static void main(String[] args) {
        /**
         * 1、测试基本的排序
         */
        Integer[] a = new Integer[]{44, 69, 40, 98, 80, 25, 36, 96, 17, 11};
        sort(a);
        show(a); // [11, 17, 25, 36, 40, 44, 69, 80, 96, 98]

        /**
         * 2、测试性能：
         * 长度为100W的随机数组排序1次需要0.485s
         * 长度为1000W的随机数组排序1次需要5.197s
         */
        System.out.println(SortCompare.timeRandomInput("Quick", 100_0000, 1)); // 0.485s
        System.out.println(SortCompare.timeRandomInput("Quick", 1000_0000, 1)); // 5.197s
    }
}
```

快速排序算法，可能是应用最广泛的排序算法了。快速排序算法流行的原因是实现简单，适用于各种不同的输入数据并且在一般应用中比其他排序算法都要快的多。

**快速排序引人注目的特点**：

- 它是原地排序（只需要一个很小的辅助栈）；
- 将长度为N的数组排序所需的时间和`NlgN`成正比。

**快速排序的缺点**：

- 快速排序非常脆弱，在实现时要非常小心才能避免低劣的性能。

> 快速排序算法改进

和大多数递归排序算法一样，改进快速排序性能的一个简单办法基于以下两点：

- 对于小数组，快速排序比插入排序慢；
- 因为递归，快速排序的`sort()`方法在小数组中也会调用自己。**因此，在排序小数组时应该切换到插入排序**。

```java
// 将sort()中的语句
if (hi <= lo) return;

// 替换为
if (hi <= lo + M) {
    Insertion.sort(a, lo, hi);
    return;
}

public static void sort(Comparable[] a, int lo, int hi) {
    for(int i = lo + 1; i < hi; ++i) {
        for(int j = i; j > lo && less(a[j], a[j - 1]); --j) {
            exch((Object[])a, j, j - 1);
        }
    }
    assert isSorted(a, lo, hi);
}
```

转换参数`M`的最佳值是和系统相关的，但是`5~15`之间的任意值在大多数情况下都能令人满意。





# 九、帮助 与 总结









**11可能用到的标准库：**

- **链接：https://pan.baidu.com/s/1KjJHzPbWhhygLEaIGqIO4A** 
- **提取码：bs6b**

### 欧几里得算法

计算两个非负整数`p`和`q`的最大公约数算法：

- 若`q`是0，则最大公约数为`p`。
- 若`q`不是0，将`p`除以`q`得到余数`r`，`p`和`q`的最大公约数即为`q`和`r`的最大公约数。

```java
/**
 * 使用欧几里得算法计算最大公约数
 */
public class Gcd {
    public static void main(String[] args) {
        System.out.println(gcd(10, 0)); // 10
        System.out.println(gcd(100, 25)); // 25
    }

    public static int gcd(int p, int q) {
        if (q == 0) return p;
        int r = p % q;
        return gcd(q, r);
    }
}
```

















