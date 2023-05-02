#  设计模式

## 前言

有一些重要的设计原则在开篇和大家分享下，这些原则将贯通全文：

- 面向接口编程，而不是面向实现。这个很重要，也是优雅的、可扩展的代码的第一步，这就不需要多说了吧。

- 职责单一原则。每个类都应该只有一个单一的功能，并且该功能应该由这个类完全封装起来。

- 对修改关闭，对扩展开放。对修改关闭是说，我们辛辛苦苦加班写出来的代码，该实现的功能和该修复的 bug 都完成了，别人可不能说改就改；对扩展开放就比较好理解了，也就是说在我们写好的代码基础上，很容易实现扩展。

每个代理模式的代码都必须自己手动完成一遍， 本篇文章大都以 Java 作为程序语言来描述设计模式。

> 这里分享一个学习设计模式的链接：https://www.runoob.com/design-pattern/design-pattern-tutorial.html

# 创建型模式

创建型模式的作用就是创建对象，说到创建一个对象，最熟悉的就是 new 一个对象，然后 set 相关属性。但是，在很多场景下，我们需要给客户端提供更加友好的创建对象的方式，尤其是那种我们定义了类，但是需要提供给其他开发者用的时候。

工厂模式分为简单工厂模式，工厂模式，抽象工厂模式

在工厂模式中，我们在创建对象时不会对客户端暴露创建逻辑，并且是通过使用一个共同的接口来指向新创建的对象。**本质就是使用工厂方法代替new操作。**

### 简单工厂模式

```java
public class FoodFactory {
    public static Food makeFood(String name) {
        if (name.equals("兰州拉面")) {
            Food noodle = new LanZhouNoodle();
            System.out.println("兰州拉面"+noodle+"出锅啦");
            return noodle;
        } else if (name.equals("黄焖鸡")) {
            Food chicken = new HuangMenChicken();
            System.out.println("黄焖鸡"+ chicken +"出锅啦");
            return chicken;
        } else {
            System.out.println("不知道你做的什么哦~");
            return null;
        }
    }
}
```

其中，LanZhouNoodle 和 HuangMenChicken 都继承自 Food。

```java
public class Cook {
    public static void main(String[] args) {
        Food food = FoodFactory.makeFood("黄焖鸡");
        FoodFactory.makeFood("jaja");
    }
}
```

简单地说，**简单工厂模式通常就是这样，一个工厂类 XxxFactory，里面有一个静态方法，根据我们不同的参数，返回不同的派生自同一个父类（或实现同一接口）的实例对象。**

> 我们强调**职责单一**原则，一个类只提供一种功能，FoodFactory 的功能就是只要负责生产各种 Food。

在此例中可以看出，Cook 类在使用 FoodFactory 时就不需要 new 任何一个对象，这就是简单工厂模式的好处，封装了 new 的部分，做到的代码易用性。

### 工厂模式

简单工厂模式很简单，如果它能满足我们的需要，我觉得就不要折腾了。之所以需要引入工厂模式，是因为我们往往需要使用两个或两个以上的工厂。

```java
public interface FoodFactory {
    Food makeFood(String name);
}
public class ChineseFoodFactory implements FoodFactory {

    @Override
    public Food makeFood(String name) {
        if (name.equals("A")) {
            return new ChineseFoodA();
        } else if (name.equals("B")) {
            return new ChineseFoodB();
        } else {
            return null;
        }
    }
}
public class AmericanFoodFactory implements FoodFactory {

    @Override
    public Food makeFood(String name) {
        if (name.equals("A")) {
            return new AmericanFoodA();
        } else if (name.equals("B")) {
            return new AmericanFoodB();
        } else {
            return null;
        }
    }
}
```

其中，ChineseFoodA、ChineseFoodB、AmericanFoodA、AmericanFoodB 都派生自 Food。

客户端调用：

```java
public class APP {
    public static void main(String[] args) {
        // 先选择一个具体的工厂
        FoodFactory factory = new ChineseFoodFactory();
        // 由第一步的工厂产生具体的对象，不同的工厂造出不一样的对象
        Food food = factory.makeFood("A");
    }
}
```

虽然都是调用 makeFood("A") 制作 A 类食物，但是，不同的工厂生产出来的完全不一样。

第一步，我们需要选取合适的工厂，然后第二步基本上和简单工厂一样。

**核心在于，我们需要在第一步选好我们需要的工厂**。比如，我们有 LogFactory 接口，实现类有 FileLogFactory 和 KafkaLogFactory，分别对应将日志写入文件和写入 Kafka 中，显然，我们客户端第一步就需要决定到底要实例化 FileLogFactory 还是 KafkaLogFactory，这将决定之后的所有的操作。

### 抽象工厂模式

当涉及到**产品族**的时候，就需要引入抽象工厂模式了。 一个经典的例子是造一台电脑 。

当涉及到这种产品族的问题的时候，就需要抽象工厂模式来支持了。我们不再定义 CPU 工厂、主板工厂、硬盘工厂、显示屏工厂等等，我们直接定义电脑工厂，每个电脑工厂负责生产所有的设备，这样能保证肯定不存在兼容问题。

当然，抽象工厂的问题也是显而易见的，比如我们要加个显示器，就需要修改所有的工厂，给所有的工厂都加上制造显示器的方法。这有点违反了**对修改关闭，对扩展开放**这个设计原则。

本节要介绍的抽象工厂模式将考虑多等级产品的生产，将同一个具体工厂所生产的位于不同等级的一组产品称为一个产品族，图 1 所示的是海尔工厂和 TCL 工厂所生产的电视机与空调对应的关系图。

![电器工厂的产品等级与产品族](images/3-1Q1141559151S.gif)

抽象工厂（AbstractFactory）模式的定义：是一种为访问类提供一个创建一组相关或相互依赖对象的接口，且访问类无须指定所要产品的具体类就能得到同族的不同等级的产品的模式结构。

抽象工厂模式是工厂方法模式的升级版本，工厂方法模式只生产一个等级的产品，而抽象工厂模式可生产多个等级的产品。

使用抽象工厂模式一般要满足以下条件。

- 系统中有多个产品族，每个具体工厂创建同一族但属于不同等级结构的产品。
- 系统一次只可能消费其中某一族产品，即同族的产品一起使用。

抽象工厂模式除了具有工厂方法模式的优点外，其他主要优点如下。

- 可以在类的内部对产品族中相关联的多等级产品共同管理，而不必专门引入多个新的类来进行管理。
- 当增加一个新的产品族时不需要修改原代码，满足开闭原则。


其缺点是：当产品族中需要增加一个新的产品时，所有的工厂类都需要进行修改。



### 单例模式

简单点说，就是一个应用程序中，某个类的实例对象只有一个，你没有办法去new，因为构造器是被private修饰的，一般通过`getInstance()`的方法来获取它们的实例。

getInstance()的返回值是一个对象的引用，并不是一个新的实例，所以不要错误的理解成多个对象。

**特点**

- 类构造器私有
- 持有自己类型的属性
- 对外提供获取实例的静态方法

**饿汉式写法**

```
public class Singleton {  
   private static Singleton instance = new Singleton();  
   private Singleton (){}  
   public static Singleton getInstance() {  
   return instance;  
   }  
}
```

弊端：因为类加载的时候就会创建对象，所以有的时候还不需要使用对象，就会创建对象，造成内存的浪费；

**饱汉模式最容易出错：**

```
public class Singleton {
    // 首先，也是先堵死 new Singleton() 这条路
    private Singleton() {}
    // 和饿汉模式相比，这边不需要先实例化出来，注意这里的 volatile，它是必须的
    private static volatile Singleton instance = null;

    public static Singleton getInstance() {
        if (instance == null) {
            // 加锁
            synchronized (Singleton.class) {
                // 这一次判断也是必须的，不然会有并发问题
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

> 双重检查，指的是两次检查 instance 是否为 null。
>
> volatile 在这里是需要的，希望能引起读者的关注。
>
> 很多人不知道怎么写，直接就在 getInstance() 方法签名上加上 synchronized，这就不多说了，性能太差。

嵌套类最经典，以后大家就用它吧：

```
public class Singleton {

    private Singleton() {}
    // 主要是使用了 嵌套类可以访问外部类的静态属性和静态方法 的特性
    private static class Holder {
        private static Singleton instance = new Singleton();
    }
    public static Singleton getInstance() {
        return Holder.instance;
    }
}
```

> 注意，很多人都会把这个**嵌套类**说成是**静态内部类**，严格地说，内部类和嵌套类是不一样的，它们能访问的外部类权限也是不一样的。

最后，我们说一下枚举，枚举很特殊，它在类加载的时候会初始化里面的所有的实例，而且 JVM 保证了它们不会再被实例化，所以它天生就是单例的。

**TODO:**

建造者模式

原型模式

# 结构型模式

前面创建型模式介绍了创建对象的一些设计模式，这节介绍的结构型模式旨在通过改变代码结构来达到解耦的目的，使得我们的代码容易维护和扩展。



## 装饰器模式

装饰器模式**能够实现从一个对象的外部来给对象添加功能，有非常灵活的扩展性，可以在对原来的代码毫无修改的前提下，为对象添加新功能**。除此之外，装饰器模式***还能够实现对象的动态组合，借此我们可以很灵活地给动态组合的对象，匹配所需要的功能***。

![image-20220523183637196](../Java/JUC/pics/image-20220523183637196.png)



- Component为统一接口，也是装饰类和被装饰类的基本类型。
- ConcreteComponent为具体实现类，也是被装饰类，他本身是个具有一些功能的完整的类。
- Decorator是装饰类，实现了Component接口的同时还在内部维护了一个ConcreteComponent的实例，并可以通过构造函数初始化。而Decorator本身，通常采用默认实现，他的存在仅仅是一个声明：我要生产出一些用于装饰的子类了。而其子类才是赋有具体装饰效果的装饰产品类。
- ConcreteDecorator是具体的装饰产品类，每一种装饰产品都具有特定的装饰效果。可以通过构造器声明装饰哪种类型的ConcreteComponent，从而对其进行装饰。

### 基本用法

基于装饰器模式实现的装修功能的代码结构简洁易读，业务逻辑也非常清晰，并且如果我们需要扩展新的装修功能，只需要新增一个继承了抽象装饰类的子类即可。

<font color=red>装饰器模式包括了以下几个角色：接口、具体对象、装饰类、具体装饰类。</font>

- 接口定义了具体对象的一些实现方法；

- 具体对象定义了一些初始化操作，比如开头设计装修功能的案例中，水电装修、天花板以及粉刷墙等都是初始化操作；
- 装饰类则是一个抽象类，主要用来初始化具体对象的一个类；
- 其它的具体装饰类都继承了该抽象类。

1. 接口定义：去定义具体需要实现的相关方法

```java
public interface IDecorator {
    /**
     * 装修方法
     */
    void decorate();
}
```

2. 具体对象：针对需要实现的方法做初始化操作，即基本的实现

```java
/**
 * 描述：装修基本类
 */
public class Decorator implements IDecorator {
    /**
     * 基本实现方法
     */
    @Override
    public void decorate() {
        System.out.println("水电装修、天花板以及粉刷墙.");
    }
}
```

3. 装饰类：抽象类，初始化具体对象

```java
/**
 * 描述：基本装饰类
 */
public abstract class BaseDecorator implements IDecorator {
    private IDecorator decorator;
 
    public BaseDecorator(IDecorator decorator) {
        this.decorator = decorator;
    }
 
    /**
     * 调用装饰方法
     */
    @Override
    public void decorate() {
        if (decorator != null) {
            decorator.decorate();
        }
    }
}
```

4. 其他具体装饰类实现自己特性的需求

   如果我们想要在基础类上添加新的装修功能，只需要基于抽象类 BaseDecorator 去实现继承类，通过构造函数调用父类，以及重写装修方法实现装修窗帘的功能即可。

```java
/**
 * 描述：窗帘装饰类
 */
public class CurtainDecorator extends BaseDecorator {
    public CurtainDecorator(IDecorator decorator) {
        super(decorator);
    }
 
    /**
     * 窗帘具体装饰方法
     */
    @Override
    public void decorate() {
        System.out.println("窗帘装饰。。。");
        super.decorate();
    }
}
```

5. 实际使用

```java
/**
 * 描述：具体使用测试
 */
public class Test {
    public static void main(String[] args) {
        IDecorator decorator = new Decorator();
        IDecorator curtainDecorator = new CurtainDecorator(decorator);
        curtainDecorator.decorate();
    }
}
```



## 代理模式

第一个要介绍的代理模式是最常使用的模式之一了，用一个代理来隐藏具体实现类的实现细节，通常还用于在真实的实现的前后添加一部分逻辑。

既然说是**代理**，那就要对客户端隐藏真实实现，由代理来负责客户端的所有请求。当然，代理只是个代理，它不会完成实际的业务逻辑，而是一层皮而已，但是对于客户端来说，它必须表现得就是客户端需要的真实实现。

理解**代理**这个词，这个模式其实就简单了。 下面上代码理解。 代理接口：

```
//要有一个代理接口让实现类和代理实现类来实现。
public interface FoodService {
    Food makeChicken();
}
```

被代理的实现类：

```
public class FoodServiceImpl implements FoodService {
    @Override
    public Food makeChicken() {
        Food f = new Chicken();
        f.setChicken("1kg");
        f.setSpicy("1g");
        f.setSalt("3g");
        System.out.println("鸡肉加好佐料了");
        return f;
    }
}
```

被代理实现类就只需要做自己该做的事情就好了，不需要管别的。

代理实现类：

```
public class FoodServiceProxy implements FoodService {
    // 内部一定要有一个真实的实现类，当然也可以通过构造方法注入
    private FoodService foodService = new FoodServiceImpl();
    
    @Override
    public Food makeChicken() {
        System.out.println("开始制作鸡肉");
        
        // 如果我们定义这句为核心代码的话，那么，核心代码是真实实现类做的，
        // 代理只是在核心代码前后做些“无足轻重”的事情
        Food food = foodService.makeChicken();
        
        System.out.println("鸡肉制作完成啦，加点胡椒粉");
        food.addCondiment("pepper");
        System.out.println("上锅咯");
        return food;
    }
}
```

客户端调用，注意，我们要用代理来实例化接口：

```
// 这里用代理类来实例化
FoodService foodService = new FoodServiceProxy();
foodService.makeChicken();
```

所谓代理模式，**就是对被代理方法包装或者叫增强， 在面向切面编程（AOP）中，其实就是动态代理的过程。比如 Spring 中，我们自己不定义代理类，但是 Spring 会帮我们动态来定义代理，然后把我们定义在 @Before、@After、@Around 中的代码逻辑动态添加到代理中。**

待续。。。



# 结构型模式

### 模板模式

在含有继承结构的代码中，模板方法模式是非常常用的。

**父类定义了骨架（调用哪些方法及顺序），某些特定方法由子类实现**

模板方法只负责定义第一步应该要做什么，第二步应该做什么，第三步应该做什么，至于怎么做，由子类来实现。

好处：代码复用，减少重复代码。除了子类要实现的特定方法，其他方法及方法调用顺序都在父类中预先写好

缺点： 每一个不同的实现都需要一个子类来实现，导致类个数增加，使系统更加庞大

**模板模式的关键点：**

　　　　1、使用抽象类定义模板类，并在其中定义所有的基本方法、模板方法，钩子方法，不限数量，以实现功能逻辑为主。其中基本方法使用final修饰，其中要调用基本方法和钩子方法，基本方法和钩子方法可以使用protected修饰，表明可被子类修改。

　　　　2、定义实现抽象类的子类，重写其中的模板方法，甚至钩子方法，完善具体的逻辑。

　　使用场景：

　　　　1、在多个子类中拥有相同的方法，而且逻辑相同时，可以将这些方法抽出来放到一个模板抽象类中。

　　　　2、程序主框架相同，细节不同的情况下，也可以使用模板方法。

#### 架构方法介绍

模板方法使得子类可以在不改变算法结构的情况下，重新定义算法中的某些步骤。其主要分为两大类：模版方法和基本方法，而基本方法又分为：抽象方法（Abstract Method），具体方法（Concrete Method），钩子方法（Hook Method）。

四种方法的基本定义（前提：在抽象类中定义）：

（1）抽象方法：由抽象类声明，由具体子类实现，并以abstract关键字进行标识。

（2）具体方法：由抽象类声明并且实现，子类并不实现或者做覆盖操作。其实质就是普遍适用的方法，不需要子类来实现。

（3）钩子方法：由抽象类声明并且实现，子类也可以选择加以扩展。通常抽象类会给出一个空的钩子方法，也就是没有实现的扩展。**它和具体方法在代码上没有区别，不过是一种意识的区别**；而它和抽象方法有时候也是没有区别的，就是在子类都需要将其实现的时候。而不同的是抽象方法必须实现，而钩子方法可以不实现。也就是说钩子方法为你在实现某一个抽象类的时候提供了可选项，**相当于预先提供了一个默认配置。**

（4）模板方法：定义了一个方法，其中定义了整个逻辑的基本骨架。

```
public abstract class AbstractTemplate {
    // 这就是模板方法
    public void templateMethod() {
        init();
        apply(); // 这个是重点
        end(); // 可以作为钩子方法
    }
	//这是具体方法
    protected void init() {
        System.out.println("init 抽象层已经实现，子类也可以选择覆写");
    }

    // 这是抽象方法，留给子类实现
    protected abstract void apply();
	//这是钩子方法，可定义一个默认操作，或者为空
    protected void end() {
    }
}
```



