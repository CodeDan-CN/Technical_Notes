# 基础复习

### JOIN

1. 左连接：左表的数据必须都有。当遇到左表没有对应的数据时，则右表使用NULL补全数据，形成一条完成的记录
2. 右连接：右边的数据必须都有，当遇到右表没有对应的数据时，则左表使用NULL补全数据，形成一条完成的记录
3. 全连接：结果集为左连接+右连接+去重之后的样子



### UNION ALL

连接两个查询的结果集，但是要保重两个查询结果集中**字段数量和名称相同**



### WITH AS

称为"子查询部分",定义一个SQL片断，该SQL片断会被整个SQL语句所用到。

+ 特别对于UNION ALL比较有用。因为UNION ALL的每个部分可能相同，但是如果每个部分都去执行一遍的话，则成本太高，所以可以使用WITH AS短语，则只要执行一遍即可。

+ 如果WITH AS短语所定义的表名被调用两次以上，则优化器会自动将WITH AS短语所获取的数据放入一个TEMP表里，如果只是被调用一次，则不会。

**用法：**

**–针对一个别名**

```sql
with tmp as (select * from tb_name)
```



**–针对多个别名**

```sql
with
tmp as (select * from tb_name),
tmp2 as (select * from tb_name2),
tmp3 as (select * from tb_name3),
…
```



**–相当于建了个e临时表**

```sql
with e as (select * from scott.emp e where e.empno=7499)
select * from e;
```



**–相当于建了e、d临时表**

```sql
with
e as (select * from scott.emp),
d as (select * from scott.dept)
select * from e, d where e.deptno = d.deptno;
```



**–相当于自己构建临时表结构和数据**

```sql
WITH tem ( dept_code, dept_name, date ) AS (
	VALUES
		ROW ( "1614470462714445825", "营销部", "2022-01" ),
		ROW ( "1614470959278095767", "销售部", "2022-01" ),
		ROW ( "1614471266527641602", "设计部", "2022-01" ),
		ROW ( "1614471057957486593", "宣传部", "2022-01" ) 
	) 
	
SELECT	* FROM tem
```



**-相当于生成拼接表的临时表**

```sql
WITH temp AS
(
  SELECT id,name FROM A
  UNION ALL
  SELECT id,name FROM B
)
```



### WITH RECURSIVE

用于数据的递归操作、比如树形查找、数据生成

```sql
WITH RESURSIVE temp AS
(
  SELECT 1 AS num
  UNION ALL
  SELECT 1 + num
  FROM temp
  where num < 12
)
```
