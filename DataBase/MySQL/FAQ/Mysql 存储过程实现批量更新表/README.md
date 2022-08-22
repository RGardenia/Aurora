# Mysql 存储过程实现批量更新表



```sql
DELIMITER $$
CREATE 
 PROCEDURE duplicate01()
 BEGIN
 
  DECLARE num int;
  DECLARE i int;
  
  DECLARE n_id int;
  DECLARE n_title VARCHAR(100);
  DECLARE n_time VARCHAR(100);
  
  declare done BOOLEAN default 0;
  
  declare s_list cursor for select * from n_news_copy1;
  
  declare continue handler for not found set done=1;

  set i = 0;
  
  open s_list; 
  select count(*) into num from n_news_copy1;
  
  WHILE i < num DO
   fetch s_list into n_id, n_title,n_time;
   update n_news set title = n_title,time = n_time where id = n_id;
   set i = i + 1;
  END WHILE;

  close s_list;
  
 END$$
DELIMITER;

-- 执行 存储过程
call news.duplicate01();
```

