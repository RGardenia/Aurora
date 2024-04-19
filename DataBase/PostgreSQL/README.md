# PostgreSQL



在数据库术语里，PostgreSQL使用一种客户端/服务器的模型。一次PostgreSQL会话由下列相关的进程（程序）组成：

- 一个服务器进程，它管理数据库文件、接受来自客户端应用与数据库的联接并且代表客户端在数据库上执行操作。 该数据库服务器程序叫做`postgres`。
- 那些需要执行数据库操作的用户的客户端（前端）应用。 客户端应用可能本身就是多种多样的：可以是一个面向文本的工具， 也可以是一个图形界面的应用，或者是一个通过访问数据库来显示网页的网页服务器，或者是一个特制的数据库管理工具。 一些客户端应用是和 PostgreSQL发布一起提供的，但绝大部分是用户开发的。

和典型的客户端/服务器应用（C/S应用）一样，这些客户端和服务器可以在不同的主机上。 这时它们通过 TCP/IP 网络联接通讯。 应该记住的是，在客户机上可以访问的文件未必能够在数据库服务器机器上访问（或者只能用不同的文件名进行访问）。

PostgreSQL服务器可以处理来自客户端的多个并发请求。 因此，它为每个连接启动（“forks”）一个新的进程。 从这个时候开始，客户端和新服务器进程就不再经过最初的 `postgres`进程的干涉进行通讯。 因此，主服务器进程总是在运行并等待着客户端联接， 而客户端和相关联的服务器进程则是起起停停（当然，这些对用户是透明的。介绍这些主要是为了内容的完整性）。

> 安装使用 请参见 云原生 -> Docker -> Applications
>
> 中文社区：http://www.postgres.cn/v2/home



## 一、使用

### 1.1 创建一个数据库

一台运行着的PostgreSQL服务器可以管理许多数据库。 通常会为每个项目和每个用户单独使用一个数据库。

的站点管理员可能已经为创建了可以使用的数据库。 如果这样就可以省略这一步， 并且跳到下一节。

要创建一个新的数据库，在这个例子里叫`mydb`，可以使用下面的命令：



```
$ createdb mydb
```

如果不产生任何响应则表示该步骤成功，可以跳过本节的剩余部分。

如果看到类似下面这样的信息：

```
createdb: command not found
```

那么就是PostgreSQL没有安装好。或者是根本没安装， 或者是的shell搜索路径没有设置正确。尝试用绝对路径调用该命令试试：

```
$ /usr/local/pgsql/bin/createdb mydb
```

在的站点上这个路径可能不一样。和的站点管理员联系或者看看安装指导获取正确的位置。

另外一种响应可能是这样：

```
createdb: could not connect to database postgres: could not connect to server: No such file or directory
        Is the server running locally and accepting
        connections on Unix domain socket "/tmp/.s.PGSQL.5432"?
```

这意味着该服务器没有启动，或者没有按照`createdb`预期地启动。同样， 也要查看安装指导或者咨询管理员。

另外一个响应可能是这样：

```
createdb: could not connect to database postgres: FATAL:  role "joe" does not exist
```

在这里提到了自己的登录名。如果管理员没有为创建PostgreSQL用户帐号， 就会发生这些现象。（PostgreSQL用户帐号和操作系统用户帐号是不同的。） 如果是管理员，参阅[第 21 章](http://www.postgres.cn/docs/12/user-manag.html)获取创建用户帐号的帮助。 需要变成安装PostgreSQL的操作系统用户的身份（通常是 `postgres`）才能创建第一个用户帐号。 也有可能是赋予的PostgreSQL用户名和的操作系统用户名不同； 这种情况下，需要使用`-U`选项或者使用`PGUSER`环境变量指定的PostgreSQL用户名。

如果有个数据库用户帐号，但是没有创建数据库所需要的权限，那么会看到下面的信息：

```
createdb: database creation failed: ERROR:  permission denied to create database
```

并非所有用户都被许可创建新数据库。 如果PostgreSQL拒绝为创建数据库， 那么需要让站点管理员赋予创建数据库的权限。出现这种情况时请咨询的站点管理员。 如果自己安装了PostgreSQL， 那么应该以启动数据库服务器的用户身份登录然后参考手册完成权限的赋予工作。 [[1\]](http://www.postgres.cn/docs/12/tutorial-createdb.html#ftn.id-1.4.3.4.10.4)

还可以用其它名字创建数据库。PostgreSQL允许在一个站点上创建任意数量的数据库。 数据库名必须是以字母开头并且小于 63 个字符长。 一个方便的做法是创建和当前用户名同名的数据库。 许多工具假设该数据库名为缺省数据库名，所以这样可以节省的敲键。 要创建这样的数据库，只需要键入：

```
$ createdb
```



如果再也不想使用的数据库了，那么可以删除它。 比如，如果是数据库`mydb`的所有人（创建人）， 那么就可以用下面的命令删除它：

```
$ dropdb mydb
```

（对于这条命令而言，数据库名不是缺省的用户名，因此就必须声明它） 。这个动作将在物理上把所有与该数据库相关的文件都删除并且不可取消， 因此做这中操作之前一定要考虑清楚。

更多关于`createdb`和`dropdb`的信息可以分别在[createdb](http://www.postgres.cn/docs/12/app-createdb.html)和[dropdb](http://www.postgres.cn/docs/12/app-dropdb.html)中找到。

### 1.2 访问数据库



一旦创建了数据库，就可以通过以下方式访问它：

- 运行PostgreSQL的交互式终端程序，它被称为*psql*， 它允许交互地输入、编辑和执行SQL命令。
- 使用一种已有的图形化前端工具，比如pgAdmin或者带ODBC或JDBC支持的办公套件来创建和管理数据库。这种方法在这份教程中没有介绍。
- 使用多种绑定发行的语言中的一种写一个自定义的应用。这些可能性在[第 IV 部分](http://www.postgres.cn/docs/12/client-interfaces.html)中将有更深入的讨论。

可能需要启动`psql`来试验本教程中的例子。 可以用下面的命令为`mydb`数据库激活它：

```
$ psql mydb
```

如果不提供数据库名字，那么它的缺省值就是的用户账号名字。在前面使用`createdb`的小节里应该已经了解了这种方式。

在`psql`中，将看到下面的欢迎信息：

```
psql (12.2)
Type "help" for help.

mydb=>
```

最后一行也可能是：

```
mydb=#
```

这个提示符意味着是数据库超级用户，最可能出现在自己安装了 PostgreSQL实例的情况下。 作为超级用户意味着不受访问控制的限制。 对于本教程的目的而言， 是否超级用户并不重要。

如果启动`psql`时碰到了问题，那么请回到前面的小节。诊断`createdb`的方法和诊断 `psql`的方法很类似， 如果前者能运行那么后者也应该能运行。

`psql`打印出的最后一行是提示符，它表示`psql`正听着说话，这个时候就可以敲入 SQL查询到一个`psql`维护的工作区中。试验一下下面的命令：

```
mydb=> SELECT version();
                                         version
------------------------------------------------------------------------------------------
 PostgreSQL 12.2 on x86_64-pc-linux-gnu, compiled by gcc (Debian 4.9.2-10) 4.9.2, 64-bit
(1 row)

mydb=> SELECT current_date;
    date
------------
 2016-01-07
(1 row)

mydb=> SELECT 2 + 2;
 ?column?
----------
        4
(1 row)
```



`psql`程序有一些不属于SQL命令的内部命令。它们以反斜线开头，“`\`”。 欢迎信息中列出了一些这种命令。比如，可以用下面的命令获取各种PostgreSQL的SQL命令的帮助语法：

```
mydb=> \h
```



要退出`psql`，输入：

```
mydb=> \q
```

`psql`将会退出并且让返回到命令行shell。 （要获取更多有关内部命令的信息，可以在`psql`提示符上键入`\?`。） `psql`的完整功能在[psql](http://www.postgres.cn/docs/12/app-psql.html)中有文档说明。在这份文档里，将不会明确使用这些特性，但是自己可以在需要的时候使用它们。



## 二、 SQL语言

本章提供一个如何使用SQL执行简单操作的概述。本教程的目的只是给一个介绍，并非完整的SQL教程。有许多关于SQL的书籍，包括[[melt93\]](http://www.postgres.cn/docs/12/biblio.html#MELT93)和[[date97\]](http://www.postgres.cn/docs/12/biblio.html#DATE97)。还要知道有些PostgreSQL语言特性是对标准的扩展。

在随后的例子里，假设已经创建了名为`mydb`的数据库，就象在前面的章里面介绍的一样，并且已经能够启动psql。

本手册的例子也可以在PostgreSQL源代码的目录`src/tutorial/`中找到（二进制PostgreSQL发布中可能没有编译这些文件）。要使用这些文件，首先进入该目录然后运行make：

```
$ cd ..../src/tutorial
$ make
```

这样就创建了那些脚本并编译了包含用户定义函数和类型的 C 文件。接下来，要开始本教程，按照下面说的做：

```
$ cd ..../tutorial
$ psql -s mydb

...


mydb=> \i basics.sql
```

`\i`命令从指定的文件中读取命令。`psql`的`-s`选项把置于单步模式，它在向服务器发送每个语句之前暂停。 在本节使用的命令都在文件`basics.sql`中



​	PostgreSQL是一种*关系型数据库管理系统* （RDBMS）。这意味着它是一种用于管理存储在*关系*中的数据的系统。关系实际上是*表*的数学术语。 今天，把数据存储在表里的概念已经快成了固有的常识了， 但是还有其它的一些方法用于组织数据库。在类 Unix 操作系统上的文件和目录就形成了一种层次数据库的例子。 更现代的发展是面向对象数据库。

​	每个表都是一个命名的*行*集合。一个给定表的每一行由同一组的命名*列*组成，而且每一列都有一个特定的数据类型。虽然列在每行里的顺序是固定的， 但一定要记住 SQL 并不对行在表中的顺序做任何保证（但可以为了显示的目的对它们进行显式地排序）。

表被分组成数据库，一个由单个PostgreSQL服务器实例管理的数据库集合组成一个数据库*集簇*。

### 2.1 基础操作

1. 创建一个新表

可以通过指定表的名字和所有列的名字及其类型来创建表∶

```
CREATE TABLE weather (
    city            varchar(80),
    temp_lo         int,           -- 最低温度
    temp_hi         int,           -- 最高温度
    prcp            real,          -- 湿度
    date            date
);
```

可以在`psql`输入这些命令以及换行符。`psql`可以识别该命令直到分号才结束。

可以在 SQL 命令中自由使用空白（即空格、制表符和换行符）。 这就意味着可以用和上面不同的对齐方式键入命令，或者将命令全部放在一行中。两个划线（“`--`”）引入注释。 任何跟在它后面直到行尾的东西都会被忽略。SQL 是对关键字和标识符大小写不敏感的语言，只有在标识符用双引号包围时才能保留它们的大小写（上例没有这么做）。

`varchar(80)`指定了一个可以存储最长 80 个字符的任意字符串的数据类型。`int`是普通的整数类型。`real`是一种用于存储单精度浮点数的类型。`date`类型应该可以自解释（没错，类型为`date`的列名字也是`date`。 这么做可能比较方便或者容易让人混淆 — 自己选择）。

PostgreSQL支持标准的SQL类型`int`、`smallint`、`real`、`double precision`、`char(*`N`*)`、`varchar(*`N`*)`、`date`、`time`、`timestamp`和`interval`，还支持其他的通用功能的类型和丰富的几何类型。PostgreSQL中可以定制任意数量的用户定义数据类型。因而类型名并不是语法关键字，除了SQL标准要求支持的特例外。

第二个例子将保存城市和它们相关的地理位置：

```
CREATE TABLE cities (
    name            varchar(80),
    location        point
);
```

类型`point`就是一种PostgreSQL特有数据类型的例子。

最后，还要提到如果不再需要某个表，或者想以不同的形式重建它，那么可以用下面的命令删除它：

```
DROP TABLE tablename;
```

2. 在表中增加行

`INSERT`语句用于向表中添加行：

```
INSERT INTO weather VALUES ('San Francisco', 46, 50, 0.25, '1994-11-27');
```

请注意所有数据类型都使用了相当明了的输入格式。那些不是简单数字值的常量通常必需用单引号（`'`）包围，就象在例子里一样。`date`类型实际上对可接收的格式相当灵活，不过在本教程里，应该坚持使用这种清晰的格式。

`point`类型要求一个座标对作为输入，如下：

```
INSERT INTO cities VALUES ('San Francisco', '(-194.0, 53.0)');
```



到目前为止使用的语法要求记住列的顺序。一个可选的语法允许明确地列出列：

```
INSERT INTO weather (city, temp_lo, temp_hi, prcp, date)
    VALUES ('San Francisco', 43, 57, 0.0, '1994-11-29');
```

如果需要，可以用另外一个顺序列出列或者是忽略某些列， 比如说，不知道降水量：

```
INSERT INTO weather (date, city, temp_hi, temp_lo)
    VALUES ('1994-11-29', 'Hayward', 54, 37);
```

许多开发人员认为明确列出列要比依赖隐含的顺序是更好的风格。

请输入上面显示的所有命令，这样在随后的各节中才有可用的数据。

还可以使用`COPY`从文本文件中装载大量数据。这种方式通常更快，因为`COPY`命令就是为这类应用优化的， 只是比 `INSERT`少一些灵活性。比如：

```
COPY weather FROM '/home/user/weather.txt';
```

这里源文件的文件名必须在运行后端进程的机器上是可用的， 而不是在客户端上，因为后端进程将直接读取该文件。可以在[COPY](http://www.postgres.cn/docs/12/sql-copy.html)中读到更多有关`COPY`命令的信息。

3. 查询一个表

要从一个表中检索数据就是*查询*这个表。SQL的`SELECT`语句就是做这个用途的。 该语句分为选择列表（列出要返回的列）、表列表（列出从中检索数据的表）以及可选的条件（指定任意的限制）。比如，要检索表`weather`的所有行，键入：

```
SELECT * FROM weather;
```

这里`*`是“所有列”的缩写。 [[2\]](http://www.postgres.cn/docs/12/tutorial-select.html#ftn.id-1.4.4.6.2.10) 因此相同的结果应该这样获得：

```
SELECT city, temp_lo, temp_hi, prcp, date FROM weather;
```

而输出应该是：

```
     city      | temp_lo | temp_hi | prcp |    date
---------------+---------+---------+------+------------
 San Francisco |      46 |      50 | 0.25 | 1994-11-27
 San Francisco |      43 |      57 |    0 | 1994-11-29
 Hayward       |      37 |      54 |      | 1994-11-29
(3 rows)
```



可以在选择列表中写任意表达式，而不仅仅是列的列表。比如，可以：

```
SELECT city, (temp_hi+temp_lo)/2 AS temp_avg, date FROM weather;
```

这样应该得到：

```
     city      | temp_avg |    date
---------------+----------+------------
 San Francisco |       48 | 1994-11-27
 San Francisco |       50 | 1994-11-29
 Hayward       |       45 | 1994-11-29
(3 rows)
```

请注意这里的`AS`子句是如何给输出列重新命名的（`AS`子句是可选的）。

一个查询可以使用`WHERE`子句“修饰”，它指定需要哪些行。`WHERE`子句包含一个布尔（真值）表达式，只有那些使布尔表达式为真的行才会被返回。在条件中可以使用常用的布尔操作符（`AND`、`OR`和`NOT`）。 比如，下面的查询检索旧金山的下雨天的天气：

```
SELECT * FROM weather
    WHERE city = 'San Francisco' AND prcp > 0.0;
```

结果：

```
     city      | temp_lo | temp_hi | prcp |    date
---------------+---------+---------+------+------------
 San Francisco |      46 |      50 | 0.25 | 1994-11-27
(1 row)
```



可以要求返回的查询结果是排好序的：

```
SELECT * FROM weather
    ORDER BY city;
```



```
     city      | temp_lo | temp_hi | prcp |    date
---------------+---------+---------+------+------------
 Hayward       |      37 |      54 |      | 1994-11-29
 San Francisco |      43 |      57 |    0 | 1994-11-29
 San Francisco |      46 |      50 | 0.25 | 1994-11-27
```

在这个例子里，排序的顺序并未完全被指定，因此可能看到属于旧金山的行被随机地排序。但是如果使用下面的语句，那么就总是会得到上面的结果：

```
SELECT * FROM weather
    ORDER BY city, temp_lo;
```



可以要求在查询的结果中消除重复的行：

```
SELECT DISTINCT city
    FROM weather;
```



```
     city
---------------
 Hayward
 San Francisco
(2 rows)
```

再次声明，结果行的顺序可能变化。可以组合使用`DISTINCT`和`ORDER BY`来保证获取一致的结果： [[3\]](http://www.postgres.cn/docs/12/tutorial-select.html#ftn.id-1.4.4.6.6.7)

```
SELECT DISTINCT city
    FROM weather
    ORDER BY city;
```



------

[[2\] ](http://www.postgres.cn/docs/12/tutorial-select.html#id-1.4.4.6.2.10)虽然`SELECT *`对于即席查询很有用，但普遍认为在生产代码中这是很糟糕的风格，因为给表增加一个列就改变了结果。

[[3\] ](http://www.postgres.cn/docs/12/tutorial-select.html#id-1.4.4.6.6.7)在一些数据库系统里，包括老版本的PostgreSQL，`DISTINCT`的实现自动对行进行排序，因此`ORDER BY`是多余的。但是这一点并不是 SQL 标准的要求，并且目前的PostgreSQL并不保证`DISTINCT`会导致行被排序。

4. **在表之间连接**

到目前为止，的查询一次只访问一个表。查询可以一次访问多个表，或者用这种方式访问一个表而同时处理该表的多个行。 一个同时访问同一个或者不同表的多个行的查询叫*连接*查询。举例来说，比如想列出所有天气记录以及相关的城市位置。要实现这个目标，需要拿 `weather`表每行的`city`列和`cities`表所有行的`name`列进行比较， 并选取那些在该值上相匹配的行对。

**注意**

这里只是一个概念上的模型。连接通常以比实际比较每个可能的行对更高效的方式执行， 但这些是用户看不到的。

这个任务可以用下面的查询来实现：

```
SELECT *
    FROM weather, cities
    WHERE city = name;
```



```
     city      | temp_lo | temp_hi | prcp |    date    |     name      | location
---------------+---------+---------+------+------------+---------------+-----------
 San Francisco |      46 |      50 | 0.25 | 1994-11-27 | San Francisco | (-194,53)
 San Francisco |      43 |      57 |    0 | 1994-11-29 | San Francisco | (-194,53)
(2 rows)
```



观察结果集的两个方面：

- 没有城市Hayward的结果行。这是因为在`cities`表里面没有Hayward的匹配行，所以连接忽略 `weather`表里的不匹配行。稍后将看到如何修补它。

- 有两个列包含城市名字。这是正确的， 因为`weather`和`cities`表的列被串接在一起。不过，实际上不想要这些， 因此将可能希望明确列出输出列而不是使用`*`：

  ```
  SELECT city, temp_lo, temp_hi, prcp, date, location
      FROM weather, cities
      WHERE city = name;
  ```

**练习：.** 看看这个查询省略`WHERE`子句的语义是什么

因为这些列的名字都不一样，所以规划器自动地找出它们属于哪个表。如果在两个表里有重名的列，需要*限定*列名来说明究竟想要哪一个，如：

```
SELECT weather.city, weather.temp_lo, weather.temp_hi,
       weather.prcp, weather.date, cities.location
    FROM weather, cities
    WHERE cities.name = weather.city;
```

人们广泛认为在一个连接查询中限定所有列名是一种好的风格，这样即使未来向其中一个表里添加重名列也不会导致查询失败。

到目前为止，这种类型的连接查询也可以用下面这样的形式写出来：

```
SELECT *
    FROM weather INNER JOIN cities ON (weather.city = cities.name);
```

这个语法并不象上文的那个那么常用，在这里写出来是为了让更容易了解后面的主题。

现在将看看如何能把Hayward记录找回来。想让查询干的事是扫描`weather`表， 并且对每一行都找出匹配的`cities`表行。如果没有找到匹配的行，那么需要一些“空值”代替cities表的列。 这种类型的查询叫*外连接* （在此之前看到的连接都是内连接）。这样的命令看起来象这样：

```
SELECT *
    FROM weather LEFT OUTER JOIN cities ON (weather.city = cities.name);

     city      | temp_lo | temp_hi | prcp |    date    |     name      | location
---------------+---------+---------+------+------------+---------------+-----------
 Hayward       |      37 |      54 |      | 1994-11-29 |               |
 San Francisco |      46 |      50 | 0.25 | 1994-11-27 | San Francisco | (-194,53)
 San Francisco |      43 |      57 |    0 | 1994-11-29 | San Francisco | (-194,53)
(3 rows)
```

这个查询是一个*左外连接*， 因为在连接操作符左部的表中的行在输出中至少要出现一次， 而在右部的表的行只有在能找到匹配的左部表行时才被输出。 如果输出的左部表的行没有对应匹配的右部表的行，那么右部表行的列将填充空值（null）。

**练习：.** 还有右外连接和全外连接。试着找出来它们能干什么。

也可以把一个表和自己连接起来。这叫做*自连接*。 比如，假设想找出那些在其它天气记录的温度范围之外的天气记录。这样就需要拿 `weather`表里每行的`temp_lo`和`temp_hi`列与`weather`表里其它行的`temp_lo`和`temp_hi`列进行比较。可以用下面的查询实现这个目标：

```
SELECT W1.city, W1.temp_lo AS low, W1.temp_hi AS high,
    W2.city, W2.temp_lo AS low, W2.temp_hi AS high
    FROM weather W1, weather W2
    WHERE W1.temp_lo < W2.temp_lo
    AND W1.temp_hi > W2.temp_hi;

     city      | low | high |     city      | low | high
---------------+-----+------+---------------+-----+------
 San Francisco |  43 |   57 | San Francisco |  46 |   50
 Hayward       |  37 |   54 | San Francisco |  46 |   50
(2 rows)
```

在这里把weather表重新标记为`W1`和`W2`以区分连接的左部和右部。还可以用这样的别名在其它查询里节约一些敲键，比如：

```
SELECT *
    FROM weather w, cities c
    WHERE w.city = c.name;
```

5. **聚集函数**



和大多数其它关系数据库产品一样，PostgreSQL支持*聚集函数*。 一个聚集函数从多个输入行中计算出一个结果。 比如，有在一个行集合上计算`count`（计数）、`sum`（和）、`avg`（均值）、`max`（最大值）和`min`（最小值）的函数。

比如，可以用下面的语句找出所有记录中最低温度中的最高温度：

```
SELECT max(temp_lo) FROM weather;
```



```
 max
-----
  46
(1 row)
```



如果想知道该读数发生在哪个城市，可以用：

```
SELECT city FROM weather WHERE temp_lo = max(temp_lo);     错误
```

不过这个方法不能运转，因为聚集`max`不能被用于`WHERE`子句中（存在这个限制是因为`WHERE`子句决定哪些行可以被聚集计算包括；因此显然它必需在聚集函数之前被计算）。 不过，通常都可以用其它方法实现的目的；这里就可以使用*子查询*：

```
SELECT city FROM weather
    WHERE temp_lo = (SELECT max(temp_lo) FROM weather);
```



```
     city
---------------
 San Francisco
(1 row)
```

这样做是 OK 的，因为子查询是一次独立的计算，它独立于外层的查询计算出自己的聚集。

聚集同样也常用于和`GROUP BY`子句组合。比如，可以获取每个城市观测到的最低温度的最高值：

```
SELECT city, max(temp_lo)
    FROM weather
    GROUP BY city;
```



```
     city      | max
---------------+-----
 Hayward       |  37
 San Francisco |  46
(2 rows)
```

这样给每个城市一个输出。每个聚集结果都是在匹配该城市的表行上面计算的。可以用`HAVING` 过滤这些被分组的行：

```
SELECT city, max(temp_lo)
    FROM weather
    GROUP BY city
    HAVING max(temp_lo) < 40;
```



```
  city   | max
---------+-----
 Hayward |  37
(1 row)
```

这样就只给出那些所有`temp_lo`值曾都低于 40的城市。最后，如果只关心那些名字以“`S`”开头的城市，可以用：

```
SELECT city, max(temp_lo)
    FROM weather
    WHERE city LIKE 'S%'            -- (1)
    GROUP BY city
    HAVING max(temp_lo) < 40;
```

[(1)](http://www.postgres.cn/docs/12/tutorial-agg.html#co.tutorial-agg-like)  `LIKE`操作符进行模式匹配，在[第 9.7 节](http://www.postgres.cn/docs/12/functions-matching.html)里有解释。

​	理解聚集和SQL的`WHERE`以及`HAVING`子句之间的关系对非常重要。`WHERE`和`HAVING`的基本区别如下：`WHERE`在分组和聚集计算之前选取输入行（因此，它控制哪些行进入聚集计算）， 而`HAVING`在分组和聚集之后选取分组行。因此，`WHERE`子句不能包含聚集函数； 因为试图用聚集函数判断哪些行应输入给聚集运算是没有意义的。相反，`HAVING`子句总是包含聚集函数（严格说来，可以写不使用聚集的`HAVING`子句， 但这样做很少有用。同样的条件用在`WHERE`阶段会更有效）。

​	在前面的例子里，可以在`WHERE`里应用城市名称限制，因为它不需要聚集。这样比放在`HAVING`里更加高效，因为可以避免那些未通过 `WHERE`检查的行参与到分组和聚集计算中。

6. **更新**

可以用`UPDATE`命令更新现有的行。假设发现所有 11 月 28 日以后的温度读数都低了两度，那么就可以用下面的方式改正数据：

```
UPDATE weather
    SET temp_hi = temp_hi - 2,  temp_lo = temp_lo - 2
    WHERE date > '1994-11-28';
```

7. **删除**

数据行可以用`DELETE`命令从表中删除。假设对Hayward的天气不再感兴趣，那么可以用下面的方法把那些行从表中删除：

```
DELETE FROM weather WHERE city = 'Hayward';
```

所有属于Hayward的天气记录都被删除。

```
SELECT * FROM weather;
```

```
     city      | temp_lo | temp_hi | prcp |    date
---------------+---------+---------+------+------------
 San Francisco |      46 |      50 | 0.25 | 1994-11-27
 San Francisco |      41 |      55 |    0 | 1994-11-29
(2 rows)
```



用下面形式的语句的时候一定要小心

```
DELETE FROM tablename;
```

如果没有一个限制，`DELETE`将从指定表中删除所有行，把它清空。做这些之前系统不会请求确认！

### 2.2 SQL语法



http://www.postgres.cn/docs/14/sql-syntax.html



### 2.3 数据定义



http://www.postgres.cn/docs/14/ddl.html



### 2.4 数据操纵

http://www.postgres.cn/docs/14/dml.html



### 2.5 查询



### 2.6 数据类型



### 2.7 函数和操作符



### 2.8 类型转换



### 2.9 索引



### 2.10 全文搜索



### 2.11 并发控制



### 2.12 性能提示



### 2.13 并行查询





## 三、高级特性

[3.1. 视图](http://www.postgres.cn/docs/12/tutorial-views.html)

[3.2. 外键](http://www.postgres.cn/docs/12/tutorial-fk.html)

[3.3. 事务](http://www.postgres.cn/docs/12/tutorial-transactions.html)

[3.4. 窗口函数](http://www.postgres.cn/docs/12/tutorial-window.html)

[3.5. 继承](http://www.postgres.cn/docs/12/tutorial-inheritance.html)

请见在线文档 ~~！

















