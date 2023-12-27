# 实用的 JavaScript 小技巧

**1.滚动到页面顶部**

我们可以使用 window.scrollTo() 平滑滚动到页面顶部。

```javascript
const scrollToTop = () => {
  window.scrollTo({ top: 0, left: 0, behavior: "smooth" });
};
```

**2.滚动到页面底部**

当然，如果知道页面的高度，也可以平滑滚动到页面底部。

```javascript
const scrollToBottom = () => {
  window.scrollTo({
    top: document.documentElement.offsetHeight,
    left: 0,
    behavior: "smooth",
  });
};
```

**3.滚动元素到可见区域**

有时我们需要将元素滚动到可见区域，我们应该怎么做？使用 scrollIntoView 就足够了。

```
const smoothScroll = (element) => {
  element.scrollIntoView({
    behavior: "smooth",
  });
};
```

**4.全屏显示元素**

你一定遇到过这样的场景，需要全屏播放视频，并在浏览器中全屏打开页面。

```
const goToFullScreen = (element) => {
  element = element || document.body;
  if (element.requestFullscreen) {
    element.requestFullscreen();
  } else if (element.mozRequestFullScreen) {
    element.mozRequestFullScreen();
  } else if (element.msRequestFullscreen) {
    element.msRequestFullscreen();
  } else if (element.webkitRequestFullscreen) {
    element.webkitRequestFullScreen();
  }
};
```

**5.退出浏览器全屏状态**

是的，这个和第4点一起使用，你也会有退出浏览器全屏状态的场景。

```
const goExitFullscreen = () => {
  if (document.exitFullscreen) {
    document.exitFullscreen();
  } else if (document.msExitFullscreen) {
    document.msExitFullscreen();
  } else if (document.mozCancelFullScreen) {
    document.mozCancelFullScreen();
  } else if (document.webkitExitFullscreen) {
    document.webkitExitFullscreen();
  }
};
```

**6.获取数据类型**

如何通过函数获取变量的数据类型？

```
const getType = (value) => {
  const match = Object.prototype.toString.call(value).match(/ (\w+)]/)
  return match[1].toLocaleLowerCase()
}

getType() // undefined
getType({}}) // object
getType([]) // array
getType(1) // number
getType('fatfish') // string
getType(true) // boolean
getType(/fatfish/) // regexp
```

**7.停止冒泡事件**

一种适用于所有平台的防止事件冒泡的方法。

```
const stopPropagation = (event) => {
  event = event || window.event;
  if (event.stopPropagation) {
    event.stopPropagation();
  } else {
    event.cancelBubble = true;
  }
};
```

**8. 深拷贝一个对象**

如何复制深度嵌套的对象？

```
const deepCopy = (obj, hash = new WeakMap()) => {
  if (obj instanceof Date) {
    return new Date(obj);
  }
  if (obj instanceof RegExp) {
    return new RegExp(obj);
  }
  if (hash.has(obj)) {
    return hash.get(obj);
  }
  let allDesc = Object.getOwnPropertyDescriptors(obj);
  let cloneObj = Object.create(Object.getPrototypeOf(obj), allDesc);
  hash.set(obj, cloneObj);
  for (let key of Reflect.ownKeys(obj)) {
    if (obj[key] && typeof obj[key] === "object") {
      cloneObj[key] = deepCopy(obj[key], hash);
    } else {
      cloneObj[key] = obj[key];
    }
  }
  return cloneObj;
};
```

**9. 确定设备类型**

我们经常必须这样做才能在手机上显示 A 逻辑，在 PC 上显示 B 逻辑。基本上，设备类型是通过识别浏览器的 userAgent 来确定的。

```
const isMobile = () => {
  return !!navigator.userAgent.match(
    /(iPhone|iPod|Android|ios|iOS|iPad|Backerry|WebOS|Symbian|Windows Phone|Phone)/i
  );
};
```

**10.判断设备是安卓还是IOS**

除了区分是移动端还是PC端，很多时候我们还需要区分当前设备是Android还是IOS。

```
const isAndroid = () => {
  return /android/i.test(navigator.userAgent.toLowerCase());
};

const isIOS = () => {
  let reg = /iPhone|iPad|iPod|iOS|Macintosh/i;
  return reg.test(navigator.userAgent.toLowerCase());
};
```

**11.获取浏览器类型及其版本**

作为前端开发人员，您可能会遇到各种兼容性问题，这时候可能需要获取浏览器的类型和版本。

```
const getExplorerInfo = () => {
  let t = navigator.userAgent.toLowerCase();
  return 0 <= t.indexOf("msie")
    ? {
        //ie < 11
        type: "IE",
        version: Number(t.match(/msie ([\d]+)/)[1]),
      }
    : !!t.match(/trident\/.+?rv:(([\d.]+))/)
    ? {
        // ie 11
        type: "IE",
        version: 11,
      }
    : 0 <= t.indexOf("edge")
    ? {
        type: "Edge",
        version: Number(t.match(/edge\/([\d]+)/)[1]),
      }
    : 0 <= t.indexOf("firefox")
    ? {
        type: "Firefox",
        version: Number(t.match(/firefox\/([\d]+)/)[1]),
      }
    : 0 <= t.indexOf("chrome")
    ? {
        type: "Chrome",
        version: Number(t.match(/chrome\/([\d]+)/)[1]),
      }
    : 0 <= t.indexOf("opera")
    ? {
        type: "Opera",
        version: Number(t.match(/opera.([\d]+)/)[1]),
      }
    : 0 <= t.indexOf("Safari")
    ? {
        type: "Safari",
        version: Number(t.match(/version\/([\d]+)/)[1]),
      }
    : {
        type: t,
        version: -1,
      };
};
```

**12.设置cookies**

cookie 可能是我见过的最糟糕的 API，它很难使用，以至于我们不得不重新封装它以最大限度地提高开发效率。

```
const setCookie = (key, value, expire) => {
  const d = new Date();
  d.setDate(d.getDate() + expire);
  document.cookie = `${key}=${value};expires=${d.toUTCString()}`;
};
```

**13. 获取 cookie**

除了写入 cookie 之外，我们还将参与其读取操作。

```
const getCookie = (key) => {
  const cookieStr = unescape(document.cookie);
  const arr = cookieStr.split("; ");
  let cookieValue = "";
  for (let i = 0; i < arr.length; i++) {
    const temp = arr[i].split("=");
    if (temp[0] === key) {
      cookieValue = temp[1];
      break;
    }
  }
  return cookieValue;
};
```

**14.删除cookies**

删除 cookie 的想法是什么？其实，只要把它的过期时间设置为这一刻，它就会立即过期。

```
const delCookie = (key) => {
  document.cookie = `${encodeURIComponent(key)}=;expires=${new Date()}`;
};
```

**15.生成随机字符串**

不知道大家有没有遇到过需要生成随机字符串的场景。我遇到过很多次，每次都要google一遍，直到学会这个工具功能。

```
const randomString = (len) => {
  let chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz123456789";
  let strLen = chars.length;
  let randomStr = "";
  for (let i = 0; i < len; i++) {
    randomStr += chars.charAt(Math.floor(Math.random() * strLen));
  }
  return randomStr;
};

randomString(10) // pfkMfjEJ6x
randomString(20) // ce6tEx1km4idRNMtym2S
```

**16. 字符串首字母大写**

```
const fistLetterUpper = (str) => {
  return str.charAt(0).toUpperCase() + str.slice(1);
};

fistLetterUpper('fatfish') // Fatfish
```

**17.生成指定范围内的随机数**

也许出于测试目的，我经常需要生成一定范围内的随机数。

```
const randomNum = (min, max) => Math.floor(Math.random() * (max - min + 1)) + min;

randomNum(1, 10) // 6
randomNum(10, 20) // 11
```

**18.打乱数组的顺序**

如何打乱数组的原始顺序？

```
const shuffleArray = (array) => {
  return array.sort(() => 0.5 - Math.random())
}

let arr = [ 1, -1, 10, 5 ]

shuffleArray(arr) // [5, -1, 10, 1]
shuffleArray(arr) // [1, 10, -1, 5]
```

**19. 从数组中获取随机值**

之前做过一个抽奖项目，需要让数组中的奖品随机出现。

```
const getRandomValue = array => array[Math.floor(Math.random() * array.length)]; 
const prizes = [  '$100', '🍫', '🍔' ]

getRandomValue(prizes) // 🍫
getRandomValue(prizes) // 🍔
getRandomValue(prizes) // 🍫
```

**20. 格式化货币**

**格式化货币的方式有很多，比如这两种方式。**

**第一种方法**

```
const formatMoney = (money) => {
  return money.replace(new RegExp(`(?!^)(?=(\\d{3})+${money.includes('.') ? '\\.' : '$'})`, 'g'), ',')  
}

formatMoney('123456789') // '123,456,789'
formatMoney('123456789.123') // '123,456,789.123'
formatMoney('123') // '123'
```

**第二种方式**

**正则表达式让我们很头疼，不是吗？所以我们需要找到一种更简单的方式来格式化货币。**

```
const formatMoney = (money) => {
  return money.toLocaleString()
}

formatMoney(123456789) // '123,456,789'
formatMoney(123456789.123) // '123,456,789.123'
formatMoney(123) // '123'
```

我太喜欢这种方式了，简单易懂。

**总结**

以上就是我今天跟你分享的20个关于JavaScript技术简单实用的技巧，希望你能够从中学到新的知识。如果你觉得有用的话，请记得点赞我，关注我，并将其分享给你身边的朋友，也许能够帮助到他。