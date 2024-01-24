### MyBatis关键配置信息之Environment

##### environments标签

<mark>用来配置多个连接数据的环境，</mark> 在实际开发中，我们连接的数据库多半是处于开发环境的数据库，在项目上线之后使用真实环境的数据库，那么我们必须要准备两套连接参数，那么我们决定使用哪一套参数，就是`environments`标签的`default`属性值来决定的。

一般来说`default`的值一般是`environment`标签的id值。

 &nbsp;

##### environment标签

<mark>用来规定一个具体的连接数据环境</mark>。拥有`id`属性作为唯一标识，不能重复。一般取这个环境的抽象名称，比如开发环境就是`development`，测试环境就是`test`。

&nbsp;

##### transactionManager标签

<mark>用来指定当前连接环境中事务的执行方式</mark>，拥有`type`属性，此属性值为采用事务管理的类型，拥有`JDBC`和`MANAGED`两种常量选择。

+ ***JDBC***：表示当前环境中，执行SQL时，使用JDBC原生的事务管理方式，事务的提交或者回滚需要手动处理。

+ ***MANAGED***：表达当前环境中，执行SQL，使用被动管理，也就是将事务管理权限交出去，不再是MyBatis进行管理，比如Spring整合MyBatis就是采用Spring JDBC管理事务等等

&nbsp;

##### dataSource标签

<mark>用来配置当前连接环境中数据源</mark>，拥有`type`属性，此属性值为当前连接是否采用数据源以及采用何种数据源。默认有三种常量值`POOLED`，`UNPOOLED`以及`JNDI`。

+ ***POOLED***：采用默认数据库连接池进行连接管理，需要使用`property`标签进行数据源的填充。

+ ***UNPOOLED***：不采用任何数据连接池进行连接管理，当需要连接的时候，再进行连接连接。也需要使用`property`标签进行数据源的填充。

+ ***JNDI***：采用外部数据源进行数据库池化管理，不需要在进行数据源的配置，因为外部数据源已经配置完毕，需要使用`property`标签将外部数据源引入即可。

&nbsp;

##### 配置信息使用实例

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <!--可以配置多套环境-->
        <environment id="development">
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

-----

### MyBatis关键配置信息之Properties

##### properties标签

<mark>即引入外部配置文件的标签</mark>，通常用在将数据源的配置写在了外部配置文件中，需要在MyBatis的配置文件中进行引入。拥有`resources`属性，属性值就是外部配置文件的路径。

MyBatis文件内部使用`${外部配置字段名}`进行外部配置文件中指定字段值的提取。

***外部文件db.properties***

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://127.0.0.1:3306/db_ChatRoom?useSSL=false&useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai
jdbc.username=root
jdbc.password=123456
```

***MyBatis,xml***

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--导入外部配置文件，相对路径从resources目录下开始-->
    <properties resources="db.properties"></properties>
    <environments default="development">
        <!--可以配置多套环境-->
        <environment id="development">
            <!--配置事务管理器-->
            <transactionManager type="JDBC"/>
            <!--将mybatis和连接池的整合-->
            <dataSource type="POOLED">
                <!--JDBC连接属性-->
                <property name="driverClassName" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
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

------

### MyBatis关键配置信息之typeAliases

##### typeAliase标签

<mark>用于单个POJO类的路径使用别名替换</mark>，当我们在数据持久xml文件中需要使用POJO类进行数据的反射值接收，那么就需要指定POJO类，一般是类路径.类名的方式，如果多个POJO对象都这么写，那么就会出现冗余代码过多。此时就可以设置别名。

拥有两个属性`type`和`alias`，type属性值用来指定POJP的类路径.类名，alias属性值用来表示别名。

***注意：如果不进行alias值的设置，那么采用默认的别名也就是类名作为默认别名。***

&nbsp;

##### package标签

<mark>用于多个POJO类的路径使用别名替换，</mark>，均采用默认别名。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <typeAliases>
        <!--可以配置单个POJO对象别名,typeAliase和package只能使用一个-->
        <typeAliase type="cn.wtu.zld.entiry.User" alias="User"></typeAliase>
        <!--可以配置多个POJO对象别名，typeAliase和package只能使用一个-->
        <package type="cn.wtu.zld.entiry.User"></package>
    </typeAliases>
        <!--导入外部配置文件，相对路径从resources目录下开始-->
    <properties resources="db.properties"></properties>
    <environments default="development">
        <!--可以配置多套环境-->
        <environment id="development">
            <!--配置事务管理器-->
            <transactionManager type="JDBC"/>
            <!--将mybatis和连接池的整合-->
            <dataSource type="POOLED">
                <!--JDBC连接属性-->
                <property name="driverClassName" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
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

------

### MyBatis关键配置信息之Mapper

##### mappers标签

<mark>即表示引入MyBatis相关数据持久层代码实现xml文件的范围</mark>。通过使用`mapper`标签进行xml文件的引入。

&nbsp;

##### mapper标签

<mark>即引入MyBatis相关数据持久层代码实现xml文件的标签</mark>，拥有字段`resource`,字段值为引入值。引入方式分为`指定Mapper.xml文件路径`进行单个Mapper.xml的引入。

***！！！相对路径从resources包下开始***

&nbsp;

##### package标签

<mark>即引入MyBatis相关数据持久层代码实现xml文件的标签</mark>，拥有字段`name`,字段值为引入值。引入方式分为`指定Mapper.xml文件列表所在Mapper包路径`进行多个Mapper.xml的引入。

***！！！相对路径从resources包下开始***

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <environments default="development">
        <!--可以配置多套环境-->
        <environment id="development">
            <!--配置事务管理器-->
            <transactionManager type="JDBC"/>
            <!--将mybatis和连接池的整合-->
            <dataSource type="POOLED">
                <!--JDBC连接属性-->
                <property name="driverClassName" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
                <!--初始连接数-->
                <property name="initialSize" value="30"/>
                <!--最大连接数-->
                <property name="maxActive" value="100"/>
            </dataSource>
        </environment>
    </environments>

    <!--加载SQL映射文件(可以加载多个SQL映射文件)-->
    <mappers>
        <!--mapper或者package只能使用一个-->
        <mapper resource="mappers/ChatRecordMapper.xml"></mapper>
        <package name="mapper" />
    </mappers>
</configuration>
```
