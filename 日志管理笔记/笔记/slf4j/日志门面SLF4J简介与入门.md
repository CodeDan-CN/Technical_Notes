### 日志门面SLF4J

> 第三方日志的种类是很多的，有可能很多项目使用不同的日志，那么就会造成学习成本高、每一种日志API与项目耦合度太高，更换一种日志就会造成大量的代码改动，维护成本也高。
> 
> 但是如果使用外观模式，将底层日志API进行包装，对外提供统一的API，那么不管底层如何更换第三方日志，对开发人员使用来说，并没有改变。

那么我们常使用的第三方日志分别为`log4j`、`log4j2`、`logback`。这些日志都可以被统一的日志门面`SLF4j`进行管理。

对于Java项目来说，日志框架通常都会选择使用`SLF4j`作为日志门面，搭配上具体的实现框架(log系列)。

+ ***功能点一：`门面`与`第三方日志`的适配。***
  
  对于时间线在SLF4J日志门面之后的第三方日志(log4j2、logback)来说，其按照SLF4J提供的API规范进行功能实现，做到了统一接口管理。但是`log4j`本身的时间线位于SLF4J日志门面之前，所以肯定是没有直接按照SLF4J提供的API标准去实现的，所以SLF4J就需要采用适配模式在其中间建立一个适配器完成Log4J的适配

+ ***功能点二：`第三方日志框架`与`SLF4J门面`的桥接。***
  
  如果原本的项目采用

------

### SLF4J使用注意点

##### 第一点：默认日志记录级别为INFO级别

首先SLF4J日志门面集成自家slf4j-simple日志实现时，采用的日志记录级别默认为INFO级别，集成其他日志实现时，采用日志实现时指定的日志级别。

&nbsp;

##### 第二点：其本身有简单的日志实现

SLF4J日志门面也具备简单的日志实现，因为在没有集成任何底层依赖第三方日志的时候，SLF4J就会默认去采用自己的简单日志实现，但是需要导入SLF4J-Simple的jar包。

&nbsp;

##### 第三点：日志级别不同

SLF4J的日志级别只有五种，即从Trace级别、Bebug、Info、Warn、Error级别。

-----

### SLF4J日志门面入门

第一步：在Maven工程下导入SLF4J的坐标

```xml
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.25</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>1.7.25</version>
        </dependency>
```

第二步：编写测试类SLF4JTest.java

```java
public static void main(String[] args) {  
    Logger logger = LoggerFactory.getLogger(SLF4JTest01.class);  
    logger.trace("测试trace级别日志");  
    logger.debug("测试debug级别日志");  
    logger.warn("测试warn级别日志");  
    logger.error("测试trace级别日志");  

}  
```

第三步：查看结果

```tex
[main] WARN cn.wtu.zld.SLF4JTest01 - 测试warn级别日志
[main] ERROR cn.wtu.zld.SLF4JTest01 - 测试trace级别日志

Process finished with exit code 0
```

---

### SLF4J动态显示日志内容

```java
public class SLF4JTest01 {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(SLF4JTest01.class);
        String name = "codedan";
        int age = 19;
        logger.warn("这是日志记录信息，内容为：{}---{}",name,age);
    }
}
```

```tex
[main] WARN cn.wtu.zld.SLF4JTest01 - 这是日志记录信息，内容为：codedan---19
```

扩展用法：即使用日志的方式记录以及输出异常信息

```java
public class SLF4JTest01 {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(SLF4JTest01.class);
        //如果说日志记录的是一个完整的对象，也即是引用类型时,无需使用{}
        try{
            int errorInt = 10/0;
        }catch (Exception e){
            logger.info("异常信息为:",e);
        }

    }
}
```

```tex
[main] INFO cn.wtu.zld.SLF4JTest01 - 异常信息为:
java.lang.ArithmeticException: / by zero
    at cn.wtu.zld.SLF4JTest01.main(SLF4JTest01.java:15)

Process finished with exit code 0
```
