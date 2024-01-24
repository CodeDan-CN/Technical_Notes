### 第一类：时间线在SLF4J之后的第三方日志框架集成

此类代表为logback、SLF4J-simple日志框架，logback日志框架是严格按照SLF4J日志门面提供的API规则进行实现的，所以SLF4J集成Logback时几乎没有其他操作，SLF4J日志门面原本如何操作，集成logback后继续如何操作即可。

***第一步：导入SLF4J与Logback的jar包***

```xml
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.25</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
        </dependency>
```

***第二步：按SLF4J提供的API使用日志记录功能***

```java
public class SLF4JTest01 {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(SLF4JTest01.class);
        try{
            int errorInt = 10/0;
        }catch (Exception e){
            logger.info("异常信息为:",e);
        }

    }
}
```

```tex
结果：
11:14:03.530 [main] INFO cn.wtu.zld.SLF4JTest01 - 异常信息为:
java.lang.ArithmeticException: / by zero
    at cn.wtu.zld.SLF4JTest01.main(SLF4JTest01.java:11)

Process finished with exit code 0
```

***注意：其实SLF4J支持多个日志框架，但是只会使用最先加载的那个(maven先导入的那个)，所以实际开发中尽量只使用一种日志实现即可。***

-----

### 第二类：时间线在SLF4J之前的第三方日志框架集成

此类代表为log4j、log4j2日志框架，虽然log4j2日志框架时间线在SLF4J日志门面之后，但是并没有按照SLF4J提供API进行实现，***`所以需要采用适配器模式去构建一个适配类，让SLF4J日志门面通过适配类去调用log4j中的API。`***

那么SLF4J为log4j提供的适配器名为`slf4j-log4j12`的jar包，我们只需要在SLF4J与Log4j之间导入此适配器即可使用SLF4J管理Log4J日志框架。

***第一步：导入slf4j、slf4j-log4j12、log4j的jar包***

```xml
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.25</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.7.25</version>
        </dependency>
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>
```

***第二步：配置log4j.properties配置文件***

```properties
log4j.logger.cn.wtu.zld.SLF4JTest01=trace,printConsole
log4j.appender.printConsole=org.apache.log4j.ConsoleAppender
log4j.appender.printConsole.layout=org.apache.log4j.PatternLayout
log4j.appender.printConsole.layout.conversionPattern=[%6p] %r %c %t %d{yyyy-MM-dd hh:mm:ss} %m%n
```

***第三步：使用SLF4J提供的API去编写日志记录代码***

```java
public class SLF4JTest01 {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(SLF4JTest01.class);
        try{
            int errorInt = 10/0;
        }catch (Exception e){
            logger.info("异常信息为:",e);
        }

    }
}
```

```tex
结果：
[  INFO] 0 cn.wtu.zld.SLF4JTest01 main 2022-06-07 11:41:22 异常信息为:
java.lang.ArithmeticException: / by zero
    at cn.wtu.zld.SLF4JTest01.main(SLF4JTest01.java:13)

Process finished with exit code 0
```

&nbsp;

##### log4j2的适配器使用

关于log4j2的适配器名为`log4j-slf4j-impi`的jar包，其坐标如下：

```xml
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>2.14.1</version>
        </dependency>
```

使用前按照Log4j2进行配置完后，再按照SLF4J提供API使用即可。
