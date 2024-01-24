# Java反射机制用法

> Java在诞生之初属于静态语言，也就是定义完成之后，无法在运行期间对类的内容进行改变和管理。但是后续诞生的语言大多数都是动态语言，也就是在程序运行时，依旧可以动态的改变类的结构等。所以java语言为了更加接近动态语言，推出了反射的概念。
>
> 但是终究只是接近而已，java哪怕运用了反射也改变不了其本质是属于静态语言的身边，因为`Java可以利用反射，在程序运行期间动态的获取到类模版信息，但是依旧无法做到动态改变类的结构`。

### Java反射基石---类模版对象

**想要使用反射，那么必须要获取操作类的类模版对象**

学习过JVM中类加载机制后，对类模版对象应该会有大致的了解，通常我们定义的类通过编译后变成类文件(class文件)，上面基础了这个类的所有信息，当然是通过其规定的16进制码进行的记录，所以基本没有可读性。但是对于JVM虚拟机来说，他可以将此文件上的信息进行读取以及加载，从而生成可以被程序员操作的类模版对象，这个类模版对象一般存在于方法区(元空间)中。

在java中，如果想要获取一个类的类模版文件，有如下三种方式：

```java
//方式一：即无类实例时，采用此方式
Class<？> clazz = ClassName.class;

//方法二：即有类实例时，采用此方式
Class<？> clazz = classObject.getClass()

//方式三：即采用输入类全名的方式去获取，这方式其实算是类加载中的一种显示加载触发
Class<？> clazz = Class.forName(String className)

```



### 类模版字段简介

在类模版对象中，其将方法、属性等进行对象化后进行了记录。所以我们可以通过类模版对象中相关字段的获取，来获取到此类的方法对象和属性对象、注解对象等等。

##### 获取方法对象的相关常用方法

```java
//获取类模版对象中修饰符为public的全部的方法对象
public Method[] getMethods();

//获取类模版对象中修饰符为public的指定方法对象,name参数为指定方法名称、parameterTypes方法参数
public Method getMethod(String name, Class<?>... parameterTypes);

//获取类模版对象中指定方法对象,name参数为指定方法名称、parameterTypes方法参数
public Field getDeclaredMethod(String name, Class<?>... parameterTypes)
  
//获取类模版对象中全部的方法对象
public Method[] getDeclaredMethods() 
  
```



##### 获取字段对象的相关常用方法

```java
//获取修饰符为public的指定字段名的字段对象
public Field getField(String name)
  
//获取当前类模版对象中修饰符为public的所有字段对象
public Field[] getFields()
  
//获取指定字段名的字段对象
public Field getDeclaredField(String name)
  
//获取当前类模版对象中所有字段对象
public Field[] getDeclaredFields() 
```



### 反射进阶版运用----搭配注解

Spring框架这玩意对于学习Java开发的人来说，肯定熟悉到不行，web项目核心了可以说。而Spring框架这种高度对Java EE进行封装的框架其本质是`反射+注解+动态代理+设计模式`。

所以反射在开发中有很大程度上是配合注解进行开发的，所以类模版对象中也内置了注解字段的方法。

```java
//判断属性或者方法、类对象上是否存在指定注解（此方法存在于类模版、属性或者方法等对象中）
public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) 

//获取当前注解类以及其父类、祖父类的属性或者方法、类对象上的指定注解对象
public <T extends Annotation> T getAnnotation(Class<T> annotationClass) 

//获取当前注解类以及其父类、祖父类的属性或者方法、类对象上的所有注解对象
public Annotation[] getAnnotations()
  
//获取当前注解类的属性或者方法、类对象上的指定注解对象
public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass)
  
//获取当前注解类的属性或者方法、类对象上的所有注解对象
public Annotation[] getDeclaredAnnotations()
```

获取到注解对象之后，就可以获取到此注解中字段的值，从而进行一些操作，比如动态代理等等。