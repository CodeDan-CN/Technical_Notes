### Log4j操作定义Logger组件

首先如何操作自定义Logger呢？从下述代码中，我们可以创建一个自定义Logger对象：

```java
 Logger logger = Logger.getLogger(Log4jTest01.class);
```

当前自定义Logger对象名称为`cn.wtu.zld.Log4jTest01`，其父类Logger组件为`cn.wtu.zld`，依次类推到最后根Logger组件为Root Logger。

之前都是直接使用properties配置文件去配置RootLogger组件，那么如何使用properties去配置自定义Logger呢？

```properties
log4j.logger.cn.wtu.zld.Log4jTest01=all,jdbcAppender

#JDBCAppender配置
log4j.appender.jdbcAppender=org.apache.log4j.jdbc.JDBCAppender
log4j.appender.jdbcAppender.Driver=com.mysql.jdbc.Driver
log4j.appender.jdbcAppender.URL=jdbc:mysql://101.43.73.10:3306/db_ChatRoom?useSSL=false&useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai
log4j.appender.jdbcAppender.User=root
log4j.appender.jdbcAppender.Password=zld19981016
log4j.appender.jdbcAppender.Sql=INSERT INTO tb_log(log_content) VALUE('%6p %r %c %t %d{yyyy-MM-dd hh:mm:ss} %m')
log4j.appender.jdbcAppender.layout=org.apache.log4j.PatternLayout
log4j.appender.jdbcAppender.layout.conversionPattern=[%6p] %r  %c %t %d{yyyy-mm-dd HH:mm:ss:SSS} %m%n


```

----

### 多个不同级下Logger组件运行规则

当同一个树枝下的不同Logger同时运行时，满足一下条件：

+ ***对于日志等级来说***：当前Logger日志等级会重写父类的Logger日志等级，不管父类的Logger日志等级为何值，均重写。并按重写之后的日志等级进行当前Logger以及其子类处理。

+ ***对于Appender组件来说***：呈追加的形式，即父类Logger组件中存在Appender列表，如果当前Logger组件新增一个Appender对象，那么就会追加进当前Logger组件中继承父类的Appender列表中。

我们拿RootLogger与cn.wtu.zld.Log4jTest01的Logger来说明，现在我们同时配置好两个Logger，然后进行测试。

```properties
log4j.logger.cn.wtu.zld.Log4jTest01=all,console
log4j.rootLogger=error,console

#JDBCAppender配置
log4j.appender.console=org.apache.log4j.ConsoleAppender
log4j.appender.console.layout=org.apache.log4j.PatternLayout
log4j.appender.console.layout.conversionPattern=[%6p] %r  %c %t %d{yyyy-mm-dd HH:mm:ss:SSS} %m%n


```

```java
public class Log4jTest01 {

    public static void main(String[] args) {
        //创建Logger组件
        Logger logger = Logger.getLogger(Log4jTest01.class);
        logger.fatal("测试fatal级别日志信息");
        logger.error("测试error级别日志信息");
        logger.warn("测试warn级别日志信息");
        logger.info("测试info级别日志信息");
        logger.debug("测试debug级别日志信息");
        logger.trace("测试trace级别日志信息");
    }

}
```

```tex
结果：
[ FATAL] 0  cn.wtu.zld.Log4jTest01 main 2022-31-05 14:31:32:213 测试fatal级别日志信息
[ FATAL] 0  cn.wtu.zld.Log4jTest01 main 2022-31-05 14:31:32:213 测试fatal级别日志信息
[ ERROR] 0  cn.wtu.zld.Log4jTest01 main 2022-31-05 14:31:32:213 测试error级别日志信息
[ ERROR] 0  cn.wtu.zld.Log4jTest01 main 2022-31-05 14:31:32:213 测试error级别日志信息
[  WARN] 0  cn.wtu.zld.Log4jTest01 main 2022-31-05 14:31:32:213 测试warn级别日志信息
[  WARN] 0  cn.wtu.zld.Log4jTest01 main 2022-31-05 14:31:32:213 测试warn级别日志信息
[  INFO] 1  cn.wtu.zld.Log4jTest01 main 2022-31-05 14:31:32:214 测试info级别日志信息
[  INFO] 1  cn.wtu.zld.Log4jTest01 main 2022-31-05 14:31:32:214 测试info级别日志信息
[ DEBUG] 1  cn.wtu.zld.Log4jTest01 main 2022-31-05 14:31:32:214 测试debug级别日志信息
[ DEBUG] 1  cn.wtu.zld.Log4jTest01 main 2022-31-05 14:31:32:214 测试debug级别日志信息
[ TRACE] 1  cn.wtu.zld.Log4jTest01 main 2022-31-05 14:31:32:214 测试trace级别日志信息
[ TRACE] 1  cn.wtu.zld.Log4jTest01 main 2022-31-05 14:31:32:214 测试trace级别日志信息
```

可以看到使用Log4jTest01生成的Logger组件进行日志记录时，重写了RootLogger的日志记录级别为trace级别，并追加一个console的Appender组件，导致输出时，每个日志信息被打印两次，因为此时log4jTest01的Logger中拥有两个Console类型的Appender组件。
