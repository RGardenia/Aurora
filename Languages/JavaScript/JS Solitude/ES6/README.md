# ES6 中的类与继承



**类：定义一个类通过class关键字，ES6的类可以看成是构造函数的另一种写法。**

```js
class Animal{
    constructor(name ,age,weight){
        // 实例的私有属性
        this.name=name;
        this.age=age;
        this.weight=weight;
        console.log("我是父构造器");
    }
    // 写在外面的属性 也是实例的私有属性
    test=[];
    // 实例的公有方法
    sayName(){
        console.log("我是实例的公有方法");
    }
    // 类的静态属性 方法  用static关键字修饰
    static arr="我是类的静态属性";
    static myMethods(){
        console.log("我是类的静态方法");
    }
 
}
//写在类体外的属性也是类的静态属性
Animal.arr2="我是类的静态属性2";
console.log(Animal,"类");
let ani=new Animal("hah",15,"20kg");
//调用实例的私有属性
ani.test=[1,2,3]
let ani2=new Animal("hihi",12,"11kg");
//调用实例的公有方法
ani.sayName();
console.log(ani,ani2);
console.log(ani.sayName===ani2.sayName);
console.log(Animal.arr);
```

![image-20220704171727049](../%E9%98%B2%E6%8A%96%E5%92%8C%E8%8A%82%E6%B5%81/images/image-20220704171727049.png)



**继承：class可以通过extends关键字实现继承，子类可以没有构造函数，系统会默认分配。子类提供了构造函数则必须要显式调用super。super函数类似于借用构造函数。类似于Animal.call()**

**1.子类对象指向父类对象**

**2.子类原型对象继承父类原型对象**

```js
class Animal{
    // 静态属性
    static animalAttr='Animal的静态属性';
    constructor(name,age,weight){
        this.name=name;
        this.age=age;
        this.weight=weight;
        console.log('Animal的构造器')
    }
    // 实例方法
    sayName(){
        console.log('实例方法')
    }
    
    // 静态方法
    static animalmethod(){
        console.log('我是父类的静态方法')
    }
}
// 要实现继承
class Dog extends Animal{
    constructor(name,age,weight,color){
        super(name,age,weight);
        this.color=color;
        console.log('Dog的构造器')
    }
}
let dog=new Dog('哈哈',1,10,'white');
// 继承 子类的原型对象继承父类的原型对象
dog.sayName();
console.log(Dog.prototype.__proto__===Animal.prototype)
// 继承 子类的对象指向父类的对象
Dog.animalmethod();
console.log(Dog.__proto__===Animal);
console.log(Dog.animalAttr);
```

![image-20220704171817071](../%E9%98%B2%E6%8A%96%E5%92%8C%E8%8A%82%E6%B5%81/images/image-20220704171817071.png)