### Mybatis传值方式解析

主要是为`${}`和`#{}`两种传值方式，关于MyBatis传递参数的方式的`#{}占位符赋值`就不做多的说明了。

##### #{}传递参数

#{}是预编译处理，MyBatis在处理#{ }时，它会将sql中的#{ }替换为？，然后调用PreparedStatement的set方法来赋值，传入参数如果为字符串，则会在值两边加上单引号。

&nbsp;

##### ${}传递参数

主要是说说`${}：字符串拼接`这个方式。是字符串替换，在处理是字符串替换，MyBatis在处理时,它会将sql中的{ }替换为变量的值，传入的数据不会加两边加上单引号。所以需要手动在使用${}的时候添加`''`。

<mark>如果不进行字符串拼接，那么两种方式都可以读取到参数，正常使用</mark>。但是当我们需要对参数进行拼接的时候，就不能使用#{参数}参与拼接，而是要使用`'${参数}'`去参与拼接(注意单引号位置)。

&nbsp;

##### 关于${}的问题

使用${ }会导致sql注入，不利于系统的安全性！SQL注入：就是通过把SQL命令插入到Web表单提交或输入域名或页面请求的查询字符串，最终达到欺骗服务器执行恶意的SQL命令。常见的有匿名登录（在登录框输入恶意的字符串）、借助异常获取数据库信息等。

解决方法有两种：

+ 第一种：使用`concat`参数进行#{}方式的字符串拼接，这样把传入参数进行控制。

**格式：concat('拼接内容',#{参数},'拼接内容' )**

&nbsp;

+ 第二种：直接简单粗暴的拼接     

**格式："拼接内容"#{}"拼接内容"**

-----

### @Param参数

##### 参数使用

当需要将多个参数传入SQL语句中时，我们要不使用Map集合进行数据封装，要不使用POJO类作为参数的载体，但是其实还有一种方式比Map数据封装要来的更加直观，那就是使用@Param注解。

要知道使用方式，我们可以看看它的一些源码。从源码中的元注解@target中获取使用范围或者从具有字段分析其作用。

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Param {
    String value();
}
```

从中我们可以得到一下信息：

+ Target( { ElementType.PARAMETER } )：参数可以知道此注解只能作用于属性上。

+ String value()：说明此注解可以存储一个字符串类型值。

**关知道这个估计都能猜出来是干嘛的了，只要将Mapper提供的接口上参数均使用@Param注解进行标注并设置名称，那么就可以在SQL中借助设置的名称进行值的传递。**

但是这是如何做到的呢？这里笔者进行了源码的追踪，当我们在Mapper接口中使用@Param注解之后，这个接口就会被动态代理，采用的是InvocationHandler接口实现。

首先我们会跳转到MapperProxy类中下述方法内：

```java
@Override
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
  try {
    if (Object.class.equals(method.getDeclaringClass())) {
      //如果是Object中定义的方法，直接执行。如toString(),hashCode()等
      return method.invoke(this, args);
    } else {
      //其他Mapper接口定义的方法交由mapperMethod来执行
      return cacahedInvoker(method).invoke(proxy,method,arsg,sqlSession);
    }
  } catch (Throwable t) {
    throw ExceptionUtil.unwrapThrowable(t);
  }
}
```

然后来到了此类的另一个重载方法中：

```java
public Object invoke(Object proxy, Method method, Object[] args, SqlSession sqlSession) throws Throwable {
   return this.mapperMethod.execute(sqlSession, args);
}
```

之后再跳转到mapperMethod类的execute方法中，这个`mapperMehtod`对象是当前Mapper映射的接口。

```java
    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result;
        Object param;
        //判断当前接口的操作，command对象中只有两个字段
        //name字段表示当前Mapper中接口的完整名称(包路径.类名)
        //type字段表达name字段所表达接口进行的SQL类型(SELECT,INSERT,DELETE,UPDATE)
        switch(this.command.getType()) {
        case INSERT:
            param = this.method.convertArgsToSqlCommandParam(args);
            result = this.rowCountResult(sqlSession.insert(this.command.getName(), param));
            break;
        case UPDATE:
            param = this.method.convertArgsToSqlCommandParam(args);
            result = this.rowCountResult(sqlSession.update(this.command.getName(), param));
            break;
        case DELETE:
            param = this.method.convertArgsToSqlCommandParam(args);
            result = this.rowCountResult(sqlSession.delete(this.command.getName(), param));
            break;
        case SELECT:
            //this.method.returnsVoid()当前Mapper接口的返回值是否为Void
            if (this.method.returnsVoid() && this.method.hasResultHandler()) {
                this.executeWithResultHandler(sqlSession, args);
                result = null;
            } else if (this.method.returnsMany()) {
            //this.executeForMany当前Mapper接口的返回值是否为多条数据
                result = this.executeForMany(sqlSession, args);
            } else if (this.method.returnsMap()) {
            //this.executeForMap当前Mapper接口的返回值是否为Map数据
                result = this.executeForMap(sqlSession, args);
            } else if (this.method.returnsCursor()) {
                result = this.executeForCursor(sqlSession, args);
            } else {
                //返回值均不为上述情况的其他情况
                param = this.method.convertArgsToSqlCommandParam(args);
                result = sqlSession.selectOne(this.command.getName(), param);
                if (this.method.returnsOptional() && (result == null || !this.method.getReturnType().equals(result.getClass()))) {
                    result = Optional.ofNullable(result);
                }
            }
            break;
        case FLUSH:
            result = sqlSession.flushStatements();
            break;
        default:
            throw new BindingException("Unknown execution method for: " + this.command.getName());
        }

        if (result == null && this.method.getReturnType().isPrimitive() && !this.method.returnsVoid()) {
            throw new BindingException("Mapper method '" + this.command.getName() + " attempted to return null from a method with a primitive return type (" + this.method.getReturnType() + ").");
        } else {
            return result;
        }
    }
```

我们会发现在execute方法中不管是执行DML语句那种语句均为有`this.method.convertArgsToSqlCommandParam(args)`这个方法执行。

args参数好理解，通过反射获取的当前方法参数列表罢了，那么这个`convertArgsToSqlCommandParam(args)`是起什么作用呢？

英文翻译一下方法字面意思就是<mark>"将当前参数转化为当前SQL语句的参数"</mark>。

那么具体是怎么转化的呢？所以我们继续跟下去，来到`convertArgsToSqlCommandParam(args)`方法内部看看，如下代码所示：

```java
public Object convertArgsToSqlCommandParam(Object[] args) {
   return this.paramNameResolver.getNamedParams(args);
}
```

再往paraNameResolver对象的getNameParams方法中跳转：

```java
    public Object getNamedParams(Object[] args) {
        //这个names参数很可疑，在[1]中进行names字段的详解查看
        int paramCount = this.names.size();
        if (args != null && paramCount != 0) {
            //当names中存在元素时，表明当前接口参数存在被@Param注解标记的参数
            if (!this.hasParamAnnotation && paramCount == 1) {
                //names中存在键值对，但是却没有被@Param修饰的参数(不管)
                Object value = args[(Integer)this.names.firstKey()];
                return wrapToMapIfCollection(value, this.useActualParamName ? (String)this.names.get(0) : null);
            } else {
                //names中存在键值对，而且有被@Param修饰的参数
                Map<String, Object> param = new ParamMap();
                int i = 0;
                //通过for来对names中的键值对进行遍历
                for(Iterator var5 = this.names.entrySet().iterator(); var5.hasNext(); ++i) {
                    Entry<Integer, String> entry = (Entry)var5.next();
                    //将names转化为一个Map参数集合，key为@Param值，value为参数值
                    //这样param集合就可以作为传参的Map了
                    param.put(entry.getValue(), args[(Integer)entry.getKey()]);
                    //下述两条代码为Map传参数时，也可以使用param1和param1..来使用
                    String genericParamName = "param" + (i + 1);
                    //如果names中没有以genericParamName为key的键值对，那么就生成一下
                    //主要是为了Map传递参数的直接用参数名或者param1都可以的特性
                    if (!this.names.containsValue(genericParamName)) {
                        param.put(genericParamName, args[(Integer)entry.getKey()]);
                    }
                    //假设@Param修饰字段为name:CodeDan,@Param值为name
                    //那么此时param集合数据为name:CodeDan,param1:CodeDan
                }

                return param;
            }
        } else {
            //names中不存在键值对
            return null;
        }
    }
```

***[1]*** names字段，在源码中是如下显示：

```java
//声明字段
private final SortedMap<Integer, String> names;


//names的赋值方法
public ParamNameResolver(Configuration config, Method method) {
        this.useActualParamName = config.isUseActualParamName();
        //关键语句一出现了，获取当前调用Mapper接口中的参数类型集合
        Class<?>[] paramTypes = method.getParameterTypes();
        //关键语句二出现了，获取当前调用Mapper接口中参数对应的注解集合
        //结果为二维数组，[i][j]中i表示当前参数，j表示当前参数的注解
        //比如[i][0]表示第i个参数的第1个注解，同理[i][1]表示第i个参数的第2个注解
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        SortedMap<Integer, String> map = new TreeMap();
        //获取了参数个数
        int paramCount = paramAnnotations.length;
        //进行二维数组的遍历
        for(int paramIndex = 0; paramIndex < paramCount; ++paramIndex) {
            //判断参数类型是不是特殊类型，当不是特殊类型开始处理
            if (!isSpecialParameter(paramTypes[paramIndex])) {
                String name = null;
                //获取这个参数的注解列表
                Annotation[] var9 = paramAnnotations[paramIndex];
                //获取这个参数的注解个数
                int var10 = var9.length;
                //遍历注解列表
                for(int var11 = 0; var11 < var10; ++var11) {
                    Annotation annotation = var9[var11];
                    //如果这个注解属于Param类型
                    if (annotation instanceof Param) {
                        this.hasParamAnnotation = true;
                        //取这个参数的Param注解值
                        name = ((Param)annotation).value();
                        break;
                    }
                }
                //如果这个参数没有Param注解，那么执行下述方法
                if (name == null) {
                    if (this.useActualParamName) {
                        name = this.getActualParamName(method, paramIndex);
                    }

                    if (name == null) {
                        name = String.valueOf(map.size());
                    }
                }
                //将参数与注解值对应存储
                map.put(paramIndex, name);
            }
        }
        //将参数与注解值的Map集合交给names
        this.names = Collections.unmodifiableSortedMap(map);
}
```

------

### @Param注解原理图
