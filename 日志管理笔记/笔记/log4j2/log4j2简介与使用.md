### log4j2简介

> log4j2是对log4j的升级版，并且根据logback的优点进行更好的优化，同时也修复了logback中会出现的一些问题，被誉为目前最优秀的Java日志框架。

log4j2中，存在两个关键的组成jar包，分别为`log4j-api`与`log4j-core`，其中log4j-api为log4j2本身自带的门面，而log4j2-core才是实现log4j2日志系统的基础包。

所以哪怕不整合SLF4J日志门面，单独使用log4j2也是可以的，log4j2日志门面采用下述代码获取指定Logger对象：***<mark>注意log4j2的默认日志级别是ERROR级别</mark>***

```java
org.apache.logging.log4j.Logger logger = LogManager.getLogger(指定类文件);
```

如果整合SLF4J日志门面，那么就需要采用专门的SLF4J对log4j2日志实现的适配器对象，此适配器去适配的并不是log4j2日志实现，而是log4j2的日志门面。所以哪怕使用了SLF4J作为日志门面，我们依旧需要导入log4j2的两个关键jar包。

```xml
        <!--    SLF4J对log4j2的适配器-->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>2.14.1</version>
        </dependency>
```

-----

### Log4J2的使用

##### ConsoleAppender搭配PatternLayout使用

```xml
    <dependencies>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.36</version>
        </dependency>
        <!--    SLF4J对log4j2的适配器-->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>2.14.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.17.1</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.17.1</version>
        </dependency>

    </dependencies>
```

```xml
<!--此文件名为log4j2.xml-->
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>


    <properties>
        <property name="patternEncoder">[ %-5level ] %d{yyyy-MM-dd hh:mm:ss.SSS} %m%n</property>
    </properties>

    <Appenders>
        <Console name="consoleAppender" target="SYSTEM_ERR">
            <PatternLayout pattern="${patternEncoder}"></PatternLayout>
        </Console>
    </Appenders>

    <Loggers>
        <Root level="trace">
            <AppenderRef ref="consoleAppender"></AppenderRef>
        </Root>
    </Loggers>

</configuration>
```

```java
public class Log4j2Test {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Log4j2Test.class);
        logger.trace("测试日志级别trace");
        logger.debug("测试日志级别debug");
        logger.info("测试日志级别info");
        logger.warn("测试日志级别warn");
        logger.error("测试日志级别error");
    }

}
```

```tex
结果：
[ TRACE ] 2022-06-12 09:50:37.382 测试日志级别trace
[ DEBUG ] 2022-06-12 09:50:37.385 测试日志级别debug
[ INFO  ] 2022-06-12 09:50:37.385 测试日志级别info
[ WARN  ] 2022-06-12 09:50:37.385 测试日志级别warn
[ ERROR ] 2022-06-12 09:50:37.385 测试日志级别error

Process finished with exit code 0
```

&nbsp;

##### FileAppender搭配PatternLayout使用

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!--此文件名为log4j2.xml-->
<configuration>

    <properties>
        <property name="patternEncoder">[ %-5level ] %d{yyyy-MM-dd hh:mm:ss.SSS} %m%n</property>
        <property name="fileUrl">/Users/zld/Desktop/log4j2</property>
    </properties>

    <Appenders>
<!--        <Console name="consoleAppender" target="SYSTEM_ERR">-->
<!--            <PatternLayout pattern="${patternEncoder}"></PatternLayout>-->
<!--        </Console>-->

        <File name="fileAppender" fileName="${fileUrl}/log4j2.log">
            <PatternLayout pattern="${patternEncoder}"></PatternLayout>
        </File>

    </Appenders>

    <Loggers>
        <Root level="trace">
<!--            <AppenderRef ref="consoleAppender"></AppenderRef>-->
            <AppenderRef ref="fileAppender"></AppenderRef>
        </Root>
    </Loggers>

</configuration>
```

```java
public class Log4j2Test {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Log4j2Test.class);
        logger.trace("测试日志级别trace");
        logger.debug("测试日志级别debug");
        logger.info("测试日志级别info");
        logger.warn("测试日志级别warn");
        logger.error("测试日志级别error");
    }

}
```

&nbsp;

##### RollingAppender搭配PatternLayout使用

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!--此文件名为log4j2.xml-->
<configuration>


    <properties>
        <property name="patternEncoder">[ %-5level ] %d{yyyy-MM-dd hh:mm:ss.SSS} %m%n</property>
        <property name="fileUrl">/Users/zld/Desktop/log4j2</property>
    </properties>

    <Appenders>
<!--        <Console name="consoleAppender" target="SYSTEM_ERR">-->
<!--            <PatternLayout pattern="${patternEncoder}"></PatternLayout>-->
<!--        </Console>-->

<!--        <File name="fileAppender" fileName="${fileUrl}/log4j2.log">-->
<!--            <PatternLayout pattern="${patternEncoder}"></PatternLayout>-->
<!--        </File>-->
        <RollingFile name="rollingFIleAppender" fileName="${fileUrl}/log4j2.log" filePattern="${fileUrl}/$${date:yyyy-MM-dd}/rolling-%d{yyyy-MM-dd hh:mm}-%i.log">
            <PatternLayout pattern="${patternEncoder}"></PatternLayout>
            <Policies>
                <!--   在系统启动时，触发拆分规则，生成一个日志文件   -->
                <OnstartupTriggeringPolicy></OnstartupTriggeringPolicy>
                <!--   按照指定大小去拆分日志文件   -->
                <SizeBasedTriggeringPolicy size="10KB"></SizeBasedTriggeringPolicy>
                <!--   按照指定的事件格式去拆分日志   -->
                <TimeBasedTriggeringPolicy></TimeBasedTriggeringPolicy>


            </Policies>
            <!--   最大拆分文件个数   -->
            <DefaultRolloverStrategy max="30"></DefaultRolloverStrategy>

        </RollingFile>

    </Appenders>

    <Loggers>
        <Root level="trace">
<!--            <AppenderRef ref="consoleAppender"></AppenderRef>-->
<!--            <AppenderRef ref="fileAppender"></AppenderRef>-->
            <AppenderRef ref="rollingFIleAppender"></AppenderRef>

        </Root>
    </Loggers>

</configuration>
```

```java
public class Log4j2Test {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Log4j2Test.class);

        for (int i = 0; i < 1000; i++) {
            logger.trace("测试日志级别trace");
            logger.debug("测试日志级别debug");
            logger.info("测试日志级别info");
            logger.warn("测试日志级别warn");
            logger.error("测试日志级别error");
        }

    }

}
```

-----

### Log4j2异步方式

log4J2日志框架分为`全局异步`或者`局部异步`。

+ ***全局异步***：当前所有的Logger统一采用异步的形式进行日志记录，从RootLogger到各种子Logger均采用异步方式。

+ ***局部异步***：指定特定的Logger进行异步方式进行日志记录，其他Logger依旧采用同步的方式。***<mark>异步不具备继承特性。</mark>***

```xml
    <!-- 此异步必须加上此jar包 -->
    <dependency>
        <groupId>com.lmax</groupId>
        <artifactId>disruptor</artifactId>
        <version>3.4.2</version>
    </dependency>
```

##### 全局异步设置方式(AsyncLogger)

设置一个新的配置文件`log4j2.component.properties`。

```properties
#log4j2.component.properties内容
log4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!--此文件名为log4j2.xml-->
<configuration>

    <properties>
        <property name="patternEncoder">[ %-5level ] %d{yyyy-MM-dd hh:mm:ss.SSS} %m%n</property>
        <property name="fileUrl">/Users/zld/Desktop/log4j2</property>
    </properties>

    <Appenders>
        <Console name="consoleAppender" target="SYSTEM_ERR">
            <PatternLayout pattern="${patternEncoder}"></PatternLayout>
        </Console>

    </Appenders>

    <Loggers>
        <Root level="trace">
            <AppenderRef ref="consoleAppender"></AppenderRef>
        </Root>

    </Loggers>

</configuration>
```

```java
public class Log4j2Test {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Log4j2Test.class);

        for (int i = 0; i < 100; i++) {
            logger.trace("测试日志级别trace");
            logger.debug("测试日志级别debug");
            logger.info("测试日志级别info");
            logger.warn("测试日志级别warn");
            logger.error("测试日志级别error");
        }

        System.out.println("1................");
        System.out.println("2................");
        System.out.println("3................");
        System.out.println("4................");
        System.out.println("5................");
        System.out.println("6................");
        System.out.println("7................");
    }

}
```

```tex
结果：
[ DEBUG ] 2022-06-13 10:50:21.144 测试日志级别debug
1................
2................
3................
4................
5................
6................
7................
[ INFO  ] 2022-06-13 10:50:21.144 测试日志级别info
[ WARN  ] 2022-06-13 10:50:21.144 测试日志级别warn
```

&nbsp;

##### 局部异步设置方式

***方式一：设置AsyncAppender***

```xml
<configuration>

    <properties>
        <property name="patternEncoder">[ %-5level ] %d{yyyy-MM-dd hh:mm:ss.SSS} %m%n</property>
        <property name="fileUrl">/Users/zld/Desktop/log4j2</property>
    </properties>

    <Appenders>
        <Console name="consoleAppender" target="SYSTEM_ERR">
            <PatternLayout pattern="${patternEncoder}"></PatternLayout>
        </Console>

        <Async name="asyncAppender" >
            <AppenderRef ref="consoleAppender"></AppenderRef>

        </Async>

    </Appenders>

    <Loggers>
        <Root level="trace">
<!--            <AppenderRef ref="consoleAppender"></AppenderRef>-->
<!--            <AppenderRef ref="fileAppender"></AppenderRef>-->
<!--            <AppenderRef ref="rollingFIleAppender"></AppenderRef>-->

            <AppenderRef ref="consoleAppender"></AppenderRef>
        </Root>


        <Logger name="cn.wtu.Log4j2FatherTest" level="trace" additivity="false">
            <AppenderRef ref="asyncAppender"></AppenderRef>
        </Logger>

    </Loggers>

</configuration>
```

```java
package cn.wtu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4j2FatherTest {
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Log4j2FatherTest.class);

        for (int i = 0; i < 100; i++) {
            logger.trace("测试日志级别trace");
            logger.debug("测试日志级别debug");
            logger.info("测试日志级别info");
            logger.warn("测试日志级别warn");
            logger.error("测试日志级别error");
        }

        System.out.println("1................");
        System.out.println("2................");
        System.out.println("3................");
        System.out.println("4................");
        System.out.println("5................");
        System.out.println("6................");
        System.out.println("7................");
    }
}
```

```tex
结果：
[ INFO  ] 2022-06-13 10:22:49.417 测试日志级别info
[ WARN  ] 2022-06-13 10:22:49.417 测试日志级别warn
1................
2................
3................
4................
5................
6................
7................
[ ERROR ] 2022-06-13 10:22:49.417 测试日志级别error
[ TRACE ] 2022-06-13 10:22:49.417 测试日志级别trace
[ DEBUG ] 2022-06-13 10:22:49.417 测试日志级别debug
```

```java
package cn.wtu.zld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4j2Test {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Log4j2Test.class);

        for (int i = 0; i < 100; i++) {
            logger.trace("测试日志级别trace");
            logger.debug("测试日志级别debug");
            logger.info("测试日志级别info");
            logger.warn("测试日志级别warn");
            logger.error("测试日志级别error");
        }

        System.out.println("1................");
        System.out.println("2................");
        System.out.println("3................");
        System.out.println("4................");
        System.out.println("5................");
        System.out.println("6................");
        System.out.println("7................");
    }
}
```

```tex
结果：
[ WARN  ] 2022-06-13 10:23:34.743 测试日志级别warn
[ ERROR ] 2022-06-13 10:23:34.743 测试日志级别error
1................
2................
3................
4................
5................
6................
7................

Process finished with exit code 0
```

&nbsp;

***方式二：设置AsyncLogger(推荐)***

<mark>**去掉方式一中所在的配置,采用Log4j内置的异步**</mark>

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!--此文件名为log4j2.xml-->
<configuration>

    <properties>
        <property name="patternEncoder">[ %-5level ] %d{yyyy-MM-dd hh:mm:ss.SSS} %m%n</property>
        <property name="fileUrl">/Users/zld/Desktop/log4j2</property>
    </properties>

    <Appenders>
        <Console name="consoleAppender" target="SYSTEM_ERR">
            <PatternLayout pattern="${patternEncoder}"></PatternLayout>
        </Console>

<!--        <Async name="asyncAppender" >-->
<!--            <AppenderRef ref="consoleAppender"></AppenderRef>-->

<!--        </Async>-->

    </Appenders>

    <Loggers>
        <Root level="trace">
            <AppenderRef ref="consoleAppender"></AppenderRef>
        </Root>
        <!--  关键配置  -->
        <AsyncLogger name="cn.wtu.Log4j2FatherTest" level="trace" additivity="false">
            <AppenderRef ref="consoleAppender"></AppenderRef>
        </AsyncLogger>

    </Loggers>
```

```java
package cn.wtu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4j2FatherTest {
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Log4j2FatherTest.class);

        for (int i = 0; i < 100; i++) {
            logger.trace("测试日志级别trace");
            logger.debug("测试日志级别debug");
            logger.info("测试日志级别info");
            logger.warn("测试日志级别warn");
            logger.error("测试日志级别error");
        }

        System.out.println("1................");
        System.out.println("2................");
        System.out.println("3................");
        System.out.println("4................");
        System.out.println("5................");
        System.out.println("6................");
        System.out.println("7................");
    }
}
```

```tex
结果：
[ INFO  ] 2022-06-13 10:30:52.157 测试日志级别info
[ WARN  ] 2022-06-13 10:30:52.157 测试日志级别warn
1................
2................
3................
4................
5................
6................
7................
[ ERROR ] 2022-06-13 10:30:52.157 测试日志级别error
[ TRACE ] 2022-06-13 10:30:52.157 测试日志级别trace
```
