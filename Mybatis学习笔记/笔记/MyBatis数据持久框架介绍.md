### MyBatis数据持久框架介绍

> ***首先MyBatis是支持定制化SQL，存储过程以及高级映射的优秀的持久化层框架，其几乎避免了所有JDBC代码和手动设置参数，获取结果集。***
> 
> ***通过使用简单的XML文件或者注解来进行配置以及原始映射，将数据持久层接口与POJO映射成数据库中的记录。***
> 
> ***MyBatis也被称为半ORM(Object Relation Mapping)框架***

MyBatis与其他持久层框架（JDBC，Hibenate）的对比：

##### JDBC缺点

1. SQL夹杂在Java代码中，导致耦合度高。

2. 维护难度大，只要开发中需求有变化，那么就需要频繁的修改

3. 代码冗余度高，效率低

&nbsp;

##### Hibernate优缺点

1. 操作简单，比如内部直接产生对应SQL以及直接注解即可完成关系映射，开发效率高。

2. 程序中长难复杂的SQL需要自己编写，导致绕过框架。

3. 由于内部直接产生对应SQL，在某些特殊场景下无法进行SQL优化。

4. 基于完全映射的全自动框架，大量字段的POJO进行部分映射时比较困难。

5. 反射操作过多，导致数据库性能地下

&nbsp;

##### MyBatis优缺点

1. 轻量级，性能出色。

2. SQL和Java代码之间进行接耦合，功能边界清晰。

3. 自定义SQL语句，保证某些业务场景下SQL的优化。

4. 开发效率不如Hibernate框架，需要自己维护映射关系以及建立冗余的SQL。

------

### 创建Mavne项目整合MyBatis框架

（1）在pom.xml文件中导入MySQL与Mybatis框架的坐标：

```xml
<!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.7</version>
</dependency>

-》导入MySQl依赖坐标：
<!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.38</version>
</dependency>
```

（2）在resources目录下创建mybatis.xml配置文件，并填入以下内容：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="mysql">
        <!--可以配置多套环境-->
        <environment id="mysql">
            <!--配置事务管理器-->
            <transactionManager type="JDBC"/>
            <!--将mybatis和连接池的整合-->
            <dataSource type="POOLED">
                <!--JDBC连接属性-->
                <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://127.0.0.1:3306/db_ChatRoom?useSSL=false&useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai"/>
                <property name="username" value="root"/>
                <property name="password" value="*******"/>
                <!--初始连接数-->
                <property name="initialSize" value="30"/>
                <!--最大连接数-->
                <property name="maxActive" value="100"/>
            </dataSource>
        </environment>
    </environments>

    <!--加载SQL映射文件(可以加载多个SQL映射文件)-->
    <mappers>
        <mapper resource="mappers/ChatRecordMapper.xml"></mapper>
        <mapper resource="mappers/CURDFriendMapper.xml"></mapper>
    </mappers>
</configuration>
```

（3）在resources目录下创建mappers目录，并在此目录下创建对应Dao层数据持久接口对应的xml文件，这里假设来看一个CURDFriendMapper.xml的内容。

```xml
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.wtu.zld.mapper.CURDFriendMapper">
    <resultMap id="FriendMapper" type="cn.wtu.zld.entity.Friend">
        <result property="userAccount" column="user_account"></result>
        <result property="name" column="user_name"></result>
        <result property="friendEndText" column="fri_endText"></result>
        <result property="friendEndTime" column="fri_endTime"></result>
        <result property="friendEndBackTime" column="fri_endBackTime"></result>
    </resultMap>
    <select id="getFriendListFromDataBase" parameterType="java.lang.String" resultMap="FriendMapper">
        select tu2.user_account ,tu2.user_name ,tf.fri_endText,tf.fri_endTime,tf.fri_endBackTime
        from tb_friends tf
                 join tb_user tu2
                      on tf.fri_account = tu2.user_account
        where tf.user_account = #{account}
    </select>
</mapper>
```

（4）编写一个获取MyBaits操作对象的工具类，辅助使用Mybatis对象。

```java
public class MyBatisUtils {

    //建立一个SqlSession工厂单例，通过此单例工厂来提供数据库连接
    private static SqlSessionFactory sqlSessionFactory;

    /**
     * 静态代码块，主要作用就是读取resource目录下的mybatis.xml配置文件
     * 并以此创建实例工厂
     * */
    static {
        //定义配置文件路径
        String xmlName = "mybatis.xml";
        try {
            //定义好配置文件路径后 使用InputStream 将文件读取进内存
            InputStream inputStream = Resources.getResourceAsStream(xmlName);
            //将配置文件放入 SqlSession工厂构建器 里面创建一个 sqlSession工厂
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 静态方法，获取sqlSession连接
     * @return SqlSession
     * */
    public static SqlSession getSqlSession(){
        return sqlSessionFactory.openSession();
    }
} 
```
