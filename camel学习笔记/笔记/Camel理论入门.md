# Camel理论入门

### Apache Camel的作用

> came本身是一个Java编写的集成API库，通过此API可以帮组我们完成不同应用之间的集成和数据流处理。其核心思维也可以算是搭建企业服务总线，通过不同服务之间的编排形成一个完整的集成线路。

由一幅图来理解Camel的结构组成：

![camel](../图库/camel.png)

##### Camel Context

Camel自己管理的一个路由容器，将路由实例交给Camel Context容器进行管理。比如当我们创建一个具体路由实例时，就可以交给其进行生命周期的管理

+ 当Camel启动后，其会读取记录在Java或者XML中的路由定义，并生成实例加入容器内并启动。
+ 当Came关闭后，它会关闭其中的所有路由实例，并关闭当前容器。



##### Route

即路由，其实可以理解成一个完整管道，即数据可以从一端管道口达到另一端的管道出口，但是数据在管道中可以被任意的改变形式甚至是结构，达到管道出口时要不已经是处于满足需求状态的数据结构，要不已经到达指定的另一个应用内。



##### Endpoint和Components

EndPoint为端点，Camel通过它完成与另一个系统交换信息，也就是说Camel可以从端点处获取到数据，也可以从端点处输出数据。那么接受到的数据如果是千奇百怪的，那么如何让他们正确的通过端点呢？

那么就涉及到了Components（组件），可以说组件是Camel的灵魂之一，由于Camel采用开源策略，无数的开发者为Camel提供了很多好用的组件，比如Groovy组件、Kafka组件、Servlet组件等等，那么这个组件和端点到底有啥关系呢？

**可以把端点想像成接口，而端点的具体实现依赖于组件**

```java
timer:hello?period=2000
```

其中timer为定时器组件，是Camel官方提供的组件之一，hello为定时器的名称，period为时间间隔的值。

一条完整的`timer:hello?period=2000`可以被称为端点，即可以被放置在EIP中。



##### EIP（企业集成模式）也被称为Processes

这个则是属于Camel的灵魂之二，当数据从端点以特殊的组件进行获取之后，数据需要在多个小管道之间进行流转后，才会来到输出的端点。那么数据在多个小管道流转时可能会在某个小管道中需要进行处理，那么EIP就会提供各种处理方式给我们。

EIP这个东西官方提供了一些操作，比如将数据变成一条简单的日志信息，比如将数据拆分成多个部分，我们在使用上经常使用官方提供的日志信息，不过由于官方提供了自定义Processes的方式，所以其实大部分还是使用自定义的Processes比较多。

+ From：从组件中接受数据EIP
+ to：向组件中发送数据EIP



##### Exchange

即在通道内流转的数据格式，这个格式是由Camel写死的，但是其支持存储`几乎任何类型`的java对象，记住是几乎任何类型！！！！

熟练的掌握这个数据结构可以让我们自定义Processes事半功倍。

TODO：列出其完整的数据结构进行说明，下班不太想连公司vpn进行debug查看，等有机会吧。



# 基本的Route格式

基本我们想要定义一条可用的Route时，我们要选择一个入口小管道(EIP)，并通过定义此管道的端点(EndPoint)以及此端点所定义的组件(Components)来完善此管道。

而作为Router的入口小管道一般都是由名为`form`的EIP担任的，其可以容纳一个各种被组件实现的端点作为数据的来源。

Router中的小管道一般可以有多种EIP担任，其包含端点的实现组件可以是Camel提供的，也可以是自定义组件，来帮助进行数据的流转。

最后作为Router的出口小管道，常见的EIP为`to`等

```java
from("timer:hello?period=2000").routeId("hello")
.transform().method("myBean","getData")     
.to("log:out"); 
```







# Camel 在Spring Boot上的快速启动

### 第一步：构建一个Spring Boot项目

这里就不仔细展开了，使用idea集成开发工具构建一个Spring Boot项目这属于基本功，不过还是需要说明此次快速启动的Boot版本为`2.3.7.RELEASE`。



### 第二步：完善POM文件，进行所需jar的引入与管理

```xml
    <dependencyManagement>
        <dependencies>
            <!--   Spring boot 自动构建内容     -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <!--    Camel 整合内容    -->
            <dependency>
                <groupId>org.apache.camel.springboot</groupId>
                <artifactId>camel-spring-boot-bom</artifactId>
                <version>3.14.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <!--   Spring boot 自动构建内容     -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <!--    Camel 整合内容    -->
        <!--    Camel 在Spring Boot下的camel核心包    -->
        <dependency>
            <groupId>org.apache.camel.springboot</groupId>
            <artifactId>camel-spring-boot-starter</artifactId>
        </dependency>

        <!--    Camel 在Spring Boot下的Servlet组件包    -->
        <dependency>
            <groupId>org.apache.camel.springboot</groupId>
            <artifactId>camel-servlet-starter</artifactId>
        </dependency>

        <!--    Camel 在Spring Boot下的处理JSON数据组件包    -->
        <dependency>
            <groupId>org.apache.camel.springboot</groupId>
            <artifactId>camel-jackson-starter</artifactId>
        </dependency>

        <!--    Camel 在Spring Boot下的swagger组件包    -->
        <dependency>
            <groupId>org.apache.camel.springboot</groupId>
            <artifactId>camel-swagger-java-starter</artifactId>
        </dependency>
 

    </dependencies>
```



### 第三步：定义一个路由，通过继承`RouteBuilder`抽象类并重写其configure方法的方式。

```java
@Component
public class MyRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        //这部分属于Camel中EIP与组件的组合使用，需要先掌握Camel的基本概念，组件和EIP用到就去查文档即可。
        //首先from其实算是Processes的一种，算是特殊的processes，但是其中的timer明显就是组件
        //transform.method也是processes的一种，属于普通的processes。用于提取一个指定实体类中的方法并执行，将其返回值保存在exchange中
        //to也是processes的一种，算是特殊的processes，而log明显是组件
        //这路由的作用就是每隔2秒调用一次myBean对象的getData方法，并通过日志的形式打印出exchange中out的内容
        from("timer:hello?period=2000").routeId("hello")
                .transform().method("myBean","getData")     
                .to("log:out"); 
    }
}
```

这一步还搭配了一个MyBean对象，一起放出来，更好的理解：

```java
@Component("myBean")
public class MyBean {

    private String data = "Hello word";

    public String getData(){
        return data;
    }
    
}
```



### 第四步：启动Boot项目启动类即可

上述路由的作用是设置一个名为hello的定时器，每隔2秒就打印一次MyBean对象中data字段的值。