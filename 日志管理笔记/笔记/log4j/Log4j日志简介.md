### Log4j日志简介

> log4j主要由Loggers(日志记录器)、Appenders(输出控制器)和Layout(日志格式化器)组成。其中Loggers控制日志的输出以及输出的级别；Appenders指定日志的输出方式(输出到控制台、文件等)；Layout控制日志信息的输出格式。

##### Loggers组件详解

loggers组件被称为日志记录器，负责收集处理日志记录，其实例即是Logger实例的名称为`全类名`，并且根据命名方式来设置Logger实例之间的`继承`关系。

```java
//比如当前Logger实例名称为： （UserMapper是mapper包下接口）
cn.wtu.codedan.mapper.UserMapper

//那么上述实例的直接父类Logger实例为
cn.wtu.codedan.mapper

//依次类推..基类Logger名为root logger
...
...
Root logger
```

对于log4j来说，子类严格遵守`里式替换法则`，即子类必须可以替代父类。当我们在父类Logger中进行新增或者修改时，子类也会随着变化。

换句话说：**<mark>即父类Logger可以收集到的日志信息，其子类Logger一定能收集到。</mark>**

那么除了`继承`关系之外，Loggers组件还有`日志记录级别`这一关键等级。

日志记录级别分为四大级别，分别用来标识当前日志的重要程度。具体如下述所示：

+ - ***ALL(最低级别)***：用于记录所有级别的日志记录
  
  - ***TRACE级别***：程序推进下的追踪信息，这个级别很低，基本不使用，比如运行时进行System.out.print的操作
  
  - ***DEBUG级别***：指出细粒度信息事件对调试应用程序是非常有帮助的，主要是配合开发，在开发过程中打印一些重要的运行信息<mark>（默认采用级别，低于此界别的无法显示）</mark>
  
  - ***INFO级别***：消息的粗粒度级别运行消息，但是注意不能随便使用，不然重要消息太多。
  
  - ***WARN级别***：表示警告，程序在运行期间又可能会发生的错误，有些消息不是错误，但是可以给开发者提示风险注意。
  
  - ***ERRO级别***：系统的错误消息，虽然不影响系统的正常运行，当不想输出太多日志，使用此级别即可。
  
  - ***FATAL级别***：表示严重错误，一旦发送系统无法运行。
  
  - ***OFF级别***：最高等级日志级别，用户关闭所有的日志记录。

即重要程度为`DEBUG < INFO < WARN < ERROR`，并且log4j只输出当前日志等级以及以上等级的日志记录。

&nbsp;

##### Appenders组件详解

控制日志输出位置，比如输出到控制台、文件等，可以根据`天数`或者`文件大小`产生新的文件，可以以`流的形式`发送到其他地方等等。

下述即是常用的Appenders组件实例：

+ ***ConsoleAppender实例***：即将日志输出到控制台

+ ***FileApperder实例***：将日志输出到文件中

+ ***DailyFileAppender实例***：将日志输出到一个日志文件，并且每天输出到一个新的文件中。

+ ***RollingFileAppender实例***：将日志信息输出到一个日志文件中，并且指定文件的尺寸，当文件大小达到指定尺寸的时候，自动改将文件改名，并生成一个新的文件。

+ ***JDBCAppender实例***：将日志保存到数据库中。

&nbsp;

##### Layouts组件

即用户可能习惯别的日志框架的输出格式或者想自定义一套输出格式，那么Layout就可以通过在Appender组件之后附加Layout组件的方式去实现自定义输出格式的功能。

加上自定义输出格式，Layouts组件一共提供了如下几个Layout实例：

+ ***HTMLLayout实例***：即将日志格式化为HTML表格形式

+ ***SimpleLayout实例***：简单的日志输出格式，打印的日志格式如默认的INFO级别消息

+ ***PatternLayout实例***：最强大的格式化方式，可以根据自定义格式输出日志，如果没有指定Layout组件，就采用此实例作为`默认格式`。

-----

### 日志输出格式说明

当Layouts组件采用PattenLayout实例时，我们就需要自己去定义输出格式，此格式化形式与C语言的printf格式类似。

+ ***%m***：输出代码中的指定日志信息

+ ***%p***：输出优先级，比如DEBUG、INFO等

+ ***%n***：换行符

+ ***%r***：输出应用启动到输出该log所消耗的毫秒数

+ ***%c***：输出打印语句所属的类的全名

+ ***%t***：输出产生该日志的线程全名

+ ***%d***：输出服务器当前时间，支持指定格式，比如%d{ yyyy-MM-dd HH:mm:ss }

+ ***%l***：输出日志实际记录的位置，包括类名、线程、代码行数。比如Test.main(Test.java:10)

+ ***%F***：输出日志消息产生时所在文件名称

+ ***%L***：输出代码中的行号

+ ***%%***：输出一个'%'字符
