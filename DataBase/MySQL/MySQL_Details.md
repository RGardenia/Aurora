

# 不同的删除方式

MySQL删除数据的方式

咱们常用的三种删除方式：通过 delete、truncate、drop 关键字进行删除；这三种都可以用来删除数据，但场景不同。

一、从执行速度上来说           `drop > truncate >> DELETE` 

二、从原理上讲

## 1、DELETE                

`DELETE from TABLE_NAME where xxx` 

- DELETE属于数据库DML操作语言，只删除数据不删除表的结构，会走事务，执行时会触发trigger；
- 在 InnoDB 中，**DELETE其实并不会真的把数据删除，mysql 实际上只是给删除的数据打了个标记为已删除，因此 delete 删除表中的数据时，表文件在磁盘上所占空间不会变小，存储空间不会被释放，只是把删除的数据行设置为不可见。**虽然未释放磁盘空间，但是下次插入数据的时候，仍然可以重用这部分空间（重用 → 覆盖）。
- DELETE执行时，会先将所删除数据缓存到rollback segement中，事务commit之后生效;
- `delete from table_name` 删除表的全部数据,对于 `MyISAM ` 会立刻释放磁盘空间，InnoDB 不会释放磁盘空间;
- 对于`delete from table_name where xxx ` 带条件的删除, 不管是 InnoDB 还是`MyISAM` 都不会释放磁盘空间;
- delete操作以后使用 `optimize table table_name `会立刻释放磁盘空间。不管是InnoDB还是MyISAM 。所以要想达到释放磁盘空间的目的，delete以后执行`optimize table` 操作。

示例：查看表占用硬盘空间大小的SQL语句如下：（用M做展示单位，数据库名：csjdemo，表名：demo2）

```sql
select concat(round(sum(DATA_LENGTH/1024/1024),2),'M') as table_size 
   from information_schema.tables 
      where table_schema='csjdemo' AND table_name='demo2';
```

![image-20220428201845152](../../doc/images/image-20220428201845152.png)

然后执行空间优化语句，以及执行后的表Size变化            `optimize table demo2`

![image-20220428201917726](../../doc/images/image-20220428201917726.png)

再看看这张表的大小，就只剩下表结构size了

![image-20220428201945083](../../doc/images/image-20220428201945083.png)

- delete 操作是一行一行执行删除的，并且同时将该行的的删除操作日志记录在redo和undo表空间中以便进行回滚（rollback）和重做操作，生成的大量日志也会占用磁盘空间。



## 2、truncate

`Truncate table TABLE_NAME`

- truncate：属于数据库DDL定义语言，不走事务，原数据不放到 rollback segment 中，操作不触发 trigger。
- ![image-20220428202116056](../../doc/images/image-20220428202116056.png)

- 执行后立即生效，无法找回执行后立即生效，无法找回执行后立即生效，无法找回
- `truncate table table_name` 立刻释放磁盘空间 ，不管是 InnoDB和 MyISAM 。`truncate table`其实有点类似于`drop table` 然后 create ,只不过这个`create table `的过程做了优化，比如表结构文件之前已经有了等等。所以速度上应该是接近`drop table`的速度;
- truncate 能够快速清空一个表。并且重置`auto_increment`的值。

但对于不同的类型存储引擎需要注意的地方是：

- 对于MyISAM，truncate会重置auto_increment（自增序列）的值为1。而delete后表仍然保持auto_increment。
- 对于InnoDB，truncate会重置 auto_increment 的值为1。delete后表仍然保持auto_increment。但是在做delete整个表之后重启MySQL的话，则重启后的auto_increment会被置为1。

也就是说，InnoDB的表本身是无法持久保存auto_increment。delete表之后auto_increment仍然保存在内存，但是重启后就丢失了，只能从1开始。实质上重启后的auto_increment会从 SELECT 1+MAX(ai_col) FROM t 开始。

小心使用 truncate，尤其没有备份的时候！

>  如果误删除线上的表，记得及时联系中国民航，订票电话：[400-806-9553](http://400-806-9553/)



## 3、drop

`Drop table Tablename`

- drop：属于数据库DDL定义语言，同Truncate

- 执行后立即生效，无法找回执行后立即生效，无法找回执行后立即生效，无法找回

- `drop table table_name` 立刻释放磁盘空间 ，不管是 InnoDB 和 MyISAM; 

  drop 语句将删除表的结构被依赖的约束(constrain)、触发器(trigger)、索引(index); 依赖于该表的存储过程/函数将保留,但是变为 invalid 状态。

- 小心使用 drop ，要删表跑路的兄弟，请在订票成功后在执行操作！



最后 **可以这么理解，一本书，delete是把目录撕了，truncate是把书的内容撕下来烧了，drop是把书烧了。**

![image-20220428201449696](../../doc/images/image-20220428201449696.png)



# 隔离级别

脏读：指读取到其他事物正在处理的未提交数据。

不可重复读：指并发更新时，另一个事务前后查询相同数据时的数据不符合预期。

幻读：指并发新增、删除这种会产生数量变化的操作时，另一个事务前后查询相同数据时不符合预期。

**MySQL 默认隔离级别 RR，MySQL 5.1 以后默认存储引擎就是 InnoDB，因此 MySQL 默认 RR 也能解决幻读问题**。

| 隔离级别         | 脏读可能性 | 不可重复读可能性 | 幻读可能性       | 加锁读 |
| ---------------- | ---------- | ---------------- | ---------------- | ------ |
| READ UNCOMMITTED | 是         | 是               | 是               | 否     |
| READ COMMITTED   | 否         | 是               | 是               | 否     |
| REPEATABLE READ  | 否         | 否               | 是（InnoDB除外） | 否     |
| SERIALIZATION    | 否         | 否               | 否               | 是     |

在 MySQL InnoDB 存储引擎下RC、RR基于 MVCC （多版本并发控制）进行并发事务控制。MVCC 是基于 “数据版本” 对并发事务的访问。