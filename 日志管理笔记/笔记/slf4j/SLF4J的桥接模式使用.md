### SLF4J的桥接模式使用

在原本仅仅只是使用日志框架的项目上，进行日志框架的更换是很麻烦的，往往要在上一个日志框架进行日志记录的地方更改大量代理，维护成本大。

而SLF4J日志门面则巧妙的使用桥接设计模式，将构造和原本日志框架相同的抽象结构，但是实现结构已经完完全全替换成了SLF4J的API。实现悄无声息的进行底层日志框架替换。

使用一个采用log4j的项目向slf4j + logback日志框架过度的Demo来说明：

首先来看看原来log4j项目的构成：

```xml
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>
```

```properties
log4j.logger.cn.wtu.zld.Log4jTest01=all,console

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

现在将log4j的jar包移除,并不修改源码，就会发现大量的报错，此时导入`SLF4j`以及`Logback`的jar包，以及最重要的桥接器所在jar包`log4j-over-slf4j`。

```xml
    <dependencies>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.25</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>log4j-over-slf4j</artifactId>
            <version>1.7.25</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.11</version>
        </dependency>
    </dependencies>
```

现在就可以直接使用原本的log4j提供的API去使用SLF4J的API了，核心就在于桥接器使用log4j的API的抽象去欺骗编译器，然后具体实现采用SLF4J的API去实现。如下是打印结果：

```tex
结果：
13:51:20.100 [main] ERROR cn.wtu.zld.Log4jTest01 - 测试fatal级别日志信息
13:51:20.103 [main] ERROR cn.wtu.zld.Log4jTest01 - 测试error级别日志信息
13:51:20.103 [main] WARN cn.wtu.zld.Log4jTest01 - 测试warn级别日志信息
13:51:20.103 [main] INFO cn.wtu.zld.Log4jTest01 - 测试info级别日志信息
13:51:20.103 [main] DEBUG cn.wtu.zld.Log4jTest01 - 测试debug级别日志信息

Process finished with exit code 0
```

可以发现SLF4J是没有`Tatal`日志级别的，所以其选择了ERROR进行其的替代

-----

### 各大日志框架相应的桥接器

```xml
A. log4j --> slf4j
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>log4j-over-slf4j</artifactId>
            <version>2.0.0-alpha1</version>
        </dependency>

B. log4j2 --->slf4j
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-to-slf4j</artifactId>
            <version>2.14.1</version>
        </dependency>
C. jul--->slf4j
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>jul-to-slf4j</artifactId>
            <version>2.0.0-alpha1</version>
        </dependency>

D. jcl --->slf4j
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>jcl-over-slf4j</artifactId>
            <version>2.0.0-alpha1</version>
        </dependency>
```
