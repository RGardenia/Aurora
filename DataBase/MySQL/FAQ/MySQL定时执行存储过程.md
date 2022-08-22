# MySQL定时执行存储过程

# 1、创建[存储过程](https://so.csdn.net/so/search?q=存储过程&spm=1001.2101.3001.7020)

```sql
CREATE DEFINER=`root`@`%` PROCEDURE `synData`()
BEGIN
	 drop table if exists user;
	 create table user select * from rych.user;
END
```

# 2、创建定时任务事件

```sql
CREATE DEFINER=`root`@`localhost` EVENT `loop_syn` 
ON SCHEDULE EVERY 20 MINUTE STARTS '2022-03-18 03:05:00' 
ON COMPLETION NOT PRESERVE ENABLE 
DO CALL `synData`
```

# 3、开启定时器

```sql
set GLOBAL event_scheduler = 1; 
```

# 4、开启/暂停事件

```sql
ALTER EVENT loop_syn ENABLE;
```