在日常开发中，经常需要元素垂直居中或者水平居中，css水平垂直居中方法有很多种，下面列举几种常用的方法；

```html
<div class="parent"><div class="children"></div></div>
```

> 父元素 .parent
>
> 子元素 .children

### 1、利用 flex 布局

Flex是Flexible Box的缩写，意为"弹性布局"，用来为盒状模型提供最大的灵活性。设为Flex布局以后，子元素的`float`、`clear`和`vertical-align`属性将失效。Flex布局是现在最常用的一种布局方案，平时开发各种布局基本上可以flex一把梭。

给一个容器元素设置`display:flex`让它变成`flex`容器。

然后其所有的直接子元素就变成flex子元素了，在flex里存在两根轴，叫主轴和交叉轴，互相垂直，主轴默认水平，flex子元素默认会沿主轴排列，可以控制flex子元素在主轴上伸缩，主轴方向可以设置，相关的css属性分为两类，一类是给flex容器设置的，一类是给flex子元素设置

```css
/* 无需知道被居中元素的宽高 */
.parent{
    display: flex;
    align-items: center;
    justify-content: center;
}
```

### 2、子元素是单行文本
 设置父元素的 text-align 和 line-height = height，这种方法适用于子元素为单行文本的情况。

```css
.parent{
    height: 100px;
    line-height: 100px;
    text-align: center;
}
```

### 3、利用 absolute + transform
给子元素的`position:absolute`，再通过`transform`即可定位到垂直居中的位置。

```css
/* 无需知道被居中元素的宽高 */
/* 设置父元素非 `static` 定位 */
.parent {
    position: relative;
}
/* 子元素绝对定位，使用 translate 的好处是无需知道子元素的宽高 */
/* 如果知道宽高，也可以使用 margin 设置 */
.children{
    position: absolute;
    left: -50%;
    top: -50%;
    transform: translate(-50%, -50%);
}
```

### 4、利用 grid 布局
grid 布局是`W3C`提出的一个二维布局系统，通过` display: grid` 来设置使用，对于以前一些复杂的布局能够得到更简单的解决。grid布局是将容器划分为行和列，产生单元格，然后指定项目所在的单元格，可以看作是二维布局。主流浏览器基本兼容

```css
/* 无需知道被居中元素的宽高 */
.parent{
    display: grid;
}
.children {
    justify-self: center; 
    align-self: center;
}
```



### 5、利用绝对定位和 margin:auto
给子元素设置`position:absolute`绝对定位，然后分别设置left、top、right、botom的值为0，结合`margin:auto`实现垂直居中和水平居中。

```css
/* 无需知道被居中元素的宽高 */
.children{
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    margin: auto;
}
.parent{
    position: relative;
}
```

