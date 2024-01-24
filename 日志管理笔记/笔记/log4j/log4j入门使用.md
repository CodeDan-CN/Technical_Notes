### log4j入门使用

第一步：导入log4j的maven依赖

```xml
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>
```

第二步：进行log4j三大组件的配置，从而得到一个完整的Logger组件

```java
public class Log4jTest01 {

    public static void main(String[] args) {
        //给Logeer组件添加Appender组件
        BasicConfigurator.configure();
        //创建Logger组件
        Logger logger = Logger.getLogger(Log4jTest01.class);
        //输出info级别日志，最开始初始化Append的时候，使用了patternLayout组件进行输出格式化。
        logger.info("测试info级别日志信息");
    }

}
```

```java
public class BasicConfigurator {
    protected BasicConfigurator() {
    }

    /**
    * 无参调用，通过给根Root Logger添加相应的Append组件来做到全局的改变
    */
    public static void configure() {
        //获取最根部分的RootLogger作为操作Logger，可以改变全局的子类Logger
        Logger root = Logger.getRootLogger();
        //添加以PatternLatout组件为输出格式的ConsoleAppender组件给Root Logger
        root.addAppender(new ConsoleAppender(new PatternLayout("%r [%t] %p %c %x - %m%n")));
    }

    public static void configure(Appender appender) {
        Logger root = Logger.getRootLogger();
        root.addAppender(appender);
    }

    public static void resetConfiguration() {
        LogManager.resetConfiguration();
    }
}
```

```tex
输出结果：0 [main] INFO cn.wtu.zld.Log4jTest01  - 测试info级别日志信息
```

从上述测试demo中可以发现，只要配置好三大组件即可正常使用log4j的日志功能。

-----

### log4j使用配置文件进行配置

从采用`Logger对象的getRootLooger()`方法源码中分析得到`LoggerManage`以及`PropertyConfigurator`类，可以得到配置文件的log4j使用方式。

首先采用BasicConfigurator进行根Root Logger其他两个组件的配置并不是优先级首选，优先级首选是使用`log4j.properties`文件进行log4j的其他三个组件配置。

在Maven项目中，在其`resource`目录下创建`log4j.properties`文件即可被LoggerManage类检测到，从而去加载其中的配置文件。

那么在log4j.properties文件下，怎么去配置log4j呢？本质上此文件的解析器类为`org.apache.log4j.PropertyConfigurator类`中定义配置前缀的常量，通过查看此类即可知道log4j采用配置文件时如何进行配置，如下是PropertyConfigurator类的常量代码：

```java
    static final String CATEGORY_PREFIX = "log4j.category.";
    static final String LOGGER_PREFIX = "log4j.logger.";
    static final String FACTORY_PREFIX = "log4j.factory";
    static final String ADDITIVITY_PREFIX = "log4j.additivity.";
    static final String ROOT_CATEGORY_PREFIX = "log4j.rootCategory";
    static final String ROOT_LOGGER_PREFIX = "log4j.rootLogger";
    static final String APPENDER_PREFIX = "log4j.appender.";
    static final String RENDERER_PREFIX = "log4j.renderer.";
    static final String THRESHOLD_PREFIX = "log4j.threshold";
```

在简单入门使用中，我们只需要使用到`log4j.rootLogger`和`log4j.appender`即可。如下是配置时的格式要求：

```properties
log4j.rootLogger=日志级别值,选中的Appender组件名称01，选中的Appender组件名称02，.....
log4j.appender.自定义Appender组件名称=Appender组件的实现类全类名
log4j.appender.自定义Appender组件名称.layout=Layout组件的实现类全类名
```

通过上述格式要求，完成下述配置文件下的log4j运行实例：

```properties
log4j.rootLogger=all,console
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
        logger.info("测试info级别日志信息");
    }

}
```

```tex
运行结果：
[ FATAL] 0  cn.wtu.zld.Log4jTest01 main 2022-48-05 09:48:26:628 测试fatal级别日志信息
[  INFO] 1  cn.wtu.zld.Log4jTest01 main 2022-48-05 09:48:26:629 测试info级别日志信息

Process finished with exit code 0
```

-----

### 开启详细日志记录(Debug级别)

开启条件在`LogLog类中的debug方法内`，代码如下

```java
public static void debug(String msg, Throwable t) {
        if (debugEnabled && !quietMode) {
            System.out.println("log4j: " + msg);
            if (t != null) {
                t.printStackTrace(System.out);
            }
        }

    }
```

所以想要开启log4j的详细日志记录，那么就要让`dubugEnabled`参数为true，`quietMode`参数为false即可。    

不过quietMode参数初始值便为false，所以我们只需要让dubugEnable为true即可。

```java
public class Log4jTest01 {

    public static void main(String[] args) {
        //设置其DebugEnable参数为true，开启详细日志记录,一定要在配置Logger组件之前开启
        LogLog.setInternalDebugging(true);
        //创建Logger组件
        Logger logger = Logger.getLogger(Log4jTest01.class);
        logger.fatal("测试fatal级别日志信息");
        logger.info("测试info级别日志信息");
    }

}
```

```tex
运行结果：
log4j: Trying to find [log4j.xml] using context classloader sun.misc.Launcher$AppClassLoader@18b4aac2.
log4j: Trying to find [log4j.xml] using sun.misc.Launcher$AppClassLoader@18b4aac2 class loader.
log4j: Trying to find [log4j.xml] using ClassLoader.getSystemResource().
log4j: Trying to find [log4j.properties] using context classloader sun.misc.Launcher$AppClassLoader@18b4aac2.
log4j: Using URL [file:/Users/zld/local/Project/log4jDemo/target/classes/log4j.properties] for automatic log4j configuration.
log4j: Reading configuration from URL file:/Users/zld/local/Project/log4jDemo/target/classes/log4j.properties
log4j: Parsing for [root] with value=[all,console].
log4j: Level token is [all].
log4j: Category root set to ALL
log4j: Parsing appender named "console".
log4j: Parsing layout options for "console".
log4j: Setting property [conversionPattern] to [[%6p] %r  %c %t %d{yyyy-mm-dd HH:mm:ss:SSS} %m%n].
log4j: End of parsing for "console".
log4j: Parsed "console" options.
log4j: Finished configuring.
[ FATAL] 0  cn.wtu.zld.Log4jTest01 main 2022-48-05 09:48:26:628 测试fatal级别日志信息
[  INFO] 1  cn.wtu.zld.Log4jTest01 main 2022-48-05 09:48:26:629 测试info级别日志信息

Process finished with exit code 0
```
