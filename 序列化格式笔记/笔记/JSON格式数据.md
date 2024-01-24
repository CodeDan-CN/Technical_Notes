## JSON序列化通信

> JSON(JavaScript Object Notation, JS 对象简谱) 是一种轻量级的数据交换格式。易于人阅读和编写。同时也易于机器解析和生成。采用完全独立于语言的文本格式，但是也使用了类似于C语言家族的习惯（包括C, C++, C#, Java, JavaScript, Perl, Python等）。这些特性使JSON成为理想的数据交换语言。

JSON通常使用在服务器与客户端之间的信息交换上，通过将后端提供数据进行JSON序列化后，得到JSON字符串并将此字符串传递给客户端，客户端拿到传递数据后，将其进行JSON反序列化获取到JSON格式数据对象。从而完成前后端数据的交互。

-----

## JSON数据对象格式

JSON的数据对象格式分为两种对象格式，第一种为对象格式，第二种为数组模式。

### 对象格式

我们假设后端传输一个User对象来到前端，User对象结构如下代码所示：

```java
@Data
public class User{
    private String userName;
    private Integer userAge;
    private String userSex; 
    private Boolean isJoinUs;
}
```

那么JSON对象具体格式如下代码所示：

```json
{"userName":"CodeDan","userAge":18,"userSex":"男","isJoinUs":false}
```

所有对象即可如此表示，在JSON中一个对象是由`{}`作为对象边界，在对象边界内，使用`"field":"value"`来表示对象的某个字段与字段的值。

值的类型可以是基本数据类型，就如同上述JSON对象格式例子中所示。也可以是引用类型，不过引用类型对象的JSON表达方式不同。就如同下述代码所示：

```java
@Data
public class User{
    private String userName;
    private Integer userAge;
    private String userSex; 
    private Boolean isJoinUs;
    //新增一个引用对象
    private User user;
}
```

```json
{
    "userName":"CodeDan",
    "userAge":18,
    "userSex":"男",
    "isJoinUs":false,
    //新增的引用的对象表达
    "user":{
        "userName":"可爱的读者",
        "userAge":24,
        "userSex":"女",
        "isJoinUs":true,
        //注意，后端并没有给这个user对象再次创建对象，而是直接使用的null
        "user":null
    }
}
```

### 数组格式

这个数组模式和数组数据结构并不是完全相同的，数组模式可以被称为`值的有序列表`，数组以`[]`为数组边界，数组边界之内由多个`value`组成，这个value可以由下述几种类型组成：

1. ***字符串类型***，即JSON数据为[ "codedan","zld","123" ]对应Java中的字符串数组

2. ***数值类型***，即JSON数据可以为[1,2,3]或者[2.3,2.7,2.9]依次对应Java中的int[]以及double[]类型

3. ***boolean类型***，即JSON数据为[false,true,false]对应Java中的boolean类型

4. ***数组类型***，属于可嵌套类型，即在数组格式下继续嵌套一个数组格式。

5. ***引用类型***，属于可嵌套类型，多对应Java的集合类型，比如List，Set。如下重点演示。<mark>思考一下没什么没有Map集合。</mark>

#### 数组格式下引用类型(List,Set)

1）**Java中的List集合的构造和数据生成**，我们利用List保存多个User对象后将List进行JSON序列化后传递给前端页面，下述为后端List生成代码以及前端JSON显示代码

```java
/**
* 只包含构造对象代码，JSON序列化和传输方式放在最后说明
*/
//User对象生成代码
public void test(){
    //第一个User对象生成
    User user1 = new User();
    user1.setUserName("CodeDan");
    user1.setUserAge(18);
    user1.setUserSex("男");
    user1.setIsJoinUs(false);
    //第二个User对象生成
    User user2 = new User();
    user2.setUserName("可爱的读者");
    user2.setUserAge(20);
    user2.setUserSex("女");
    user2.setIsJoinUs(true);
//构建list集合代码
    List<User> list =  new ArrayList<>();
    list.add(user1);
    list.add(user2);
}
```

```json
[
    {
        "userName":"CodeDan",
        "userAge":18,
        "userSex":"男",
        "isJoinUs":false
    },
    {
        "userName":"可爱的读者",
        "userAge":20,
        "userSex ":"女",
        "isJoinUs":true
    }
]
```

2）**Java中的Set集合的构造和数据生成**，我们利用Set保存多个User对象后将Set进行JSON序列化后传递给前端页面，下述为后端Set生成代码以及前端JSON显示代码

```java
/**
* 只包含构造对象代码，JSON序列化和传输方式放在最后说明
*/
//User对象生成代码
public void test(){
    //第一个User对象生成
    User user1 = new User();
    user1.setUserName("CodeDan");
    user1.setUserAge(18);
    user1.setUserSex("男");
    user1.setIsJoinUs(false);
    //第二个User对象生成
    User user2 = new User();
    user2.setUserName("可爱的读者");
    user2.setUserAge(20);
    user2.setUserSex("女");
    user2.setIsJoinUs(true);
//构建list集合代码
    Set<User> set =  new HashSet<>();
    set.add(user1);
    set.add(user2);
}
```

```json
[
    {
        "userName":"CodeDan",
        "userAge":18,
        "userSex":"男",
        "isJoinUs":false
    },
    {
        "userName":"可爱的读者",
        "userAge":20,
        "userSex ":"女",
        "isJoinUs":true
    }
]
```

#### Set和List在JSON中的区别？

其实可以说Set和List被序列化为JSON格式之后，在格式上均属于JSON的数组格式。他们的区别在于Set和List的特性。

首先List是有序集合，Set是无序集合。所以在存放制定了排序规则的对象时，会按照排序规划进行排序后再存储，Set则是按照存入顺序进行存储。

所以当存放对象制定了排序规则之后，List和Set存入相同的数据，得到的JSON数据中数组内对象的顺序可能不同。

#### 为什么没有Map？

3）**Java中的Map集合的构造和数据生成**，我们利用Map保存多个User对象后将Map进行JSON序列化后传递给前端页面，下述为后端Map生成代码以及前端JSON显示代码

```java
/**
* 只包含构造对象代码，JSON序列化和传输方式放在最后说明
*/
//User对象生成代码
public void test(){
    //第一个User对象生成
    User user1 = new User();
    user1.setUserName("CodeDan");
    user1.setUserAge(18);
    user1.setUserSex("男");
    user1.setIsJoinUs(false);
    //第二个User对象生成
    User user2 = new User();
    user2.setUserName("可爱的读者");
    user2.setUserAge(20);
    user2.setUserSex("女");
    user2.setIsJoinUs(true);
//构建list集合代码
    Map<User> map =  new HashMap<>();
    map.put("CodeDan",user1);
    map.put("可爱的读者",user2);
    set.add(user2);
}
```

```json
//很明显这里已经不算是数组格式了，而是一个对象内，通过field的value进行对象的嵌套
{
    "可爱的读者":{
        "userName":"可爱的读者",
        "userAge":20,
        "userSex":"女",
        "isJoinUs":true
    },
    "CodeDan":{
        "userName":"CodeDan",
        "userAge":18,
        "userSex":"男",
        "isJoinUs":false
    }
}
```

**<mark>很明显这里已经不算是数组格式了，而是一个对象内，通过field的value进行对象的嵌套</mark>**

--------

### 前后端如何使用JSON格式数据

前端使用JSON格式数据通常有两种情况，即**前端与后端进行JSON数据交互**和**后端与前端进行JSON数据交互**，而后端使用JSON格式数据通常是**接受前端传来的JSON数据并反序列化**和**对象的JSON序列化并发送**

##### 1 前端如何与后端进行JSON数据交互

即前端获取到用户输入数据之后，将这些数据拼接为一个JSON数据格式的变量后，将此变量通过`JSON.stringify(变量)`将变量转化为JSON对象。最后就是通过ajax进行JSON数据的发送

```html
<input type="text" class="input_account" value="" />
<input type="password" class="input_password" value="" />
```

```javascript
//这里采用Jquery的写法，ajax都是原理都是一样的，vue或者原生js看下思路就可以写了
<script type="application/javascript" src="js/jquery-1.12.4.min.js"></script>
<script>
    $(document).ready(function(){
        var account = $(".input_account").val();
        var password = $(".input_password").val();
        var sendJson = {
            "account":account,
            "password":password
        }
        $.ajax({
            url:"sendJson",
            type:"get",
            //如果发送的JSON格式数据包含中文字符，一定要加上UTF-8
            contentType:"application/json;charset=UTF-8",
            data:JSON.stringify(sendJson)
            //注意还没有写success方法，因为只是说明发送流程，之后说接受处理流程时补全

        })
    })
</script>
```

##### 2 后端如何接受前端传来的JSON数据并反序列化

<mark>注意默认在Spring框架下构建的后端项目/服务</mark>，我们需要借助`@RequestBody`标签后端对JSON格式数据的处理。下述代码为后端处理前端通过ajax发起的请求代码。

```java
@ResponseBody
@GetMapping("sendJson")
//重点在于@RequestBody是将JSON格式数据转为被其修饰字段类型对象
public User sendUserToJSon(@RequestBody User user){
   System.out.println(user);
   user.setPassword("123");
   return user;
}
```

##### 3 后端如何接受前端传来的JSON数据并反序列化

<mark>注意默认在Spring框架下构建的后端项目/服务</mark>，我们需要借助`@ResponseBody(@RestController)`标签后端对JSON格式数据的处理。下述代码为后端将对象转化为JSON数据格式代码。

```java
//重点就在于这个标签，这个标签相当于将方法返回值对象进行JSON序列化后传输给前端
@ResponseBody
@GetMapping("sendJson")
//返回类型一定要是准备JSON序列化的对象
public User sendUserToJSon(@RequestBody User user){
   System.out.println(user);
   user.setPassword("123");
   return user;
}
```

##### 4 后端与前端进行数据交互

即前端success方法被异步调用后，将获取的结果变量res通过`JSON.parse(变量)`将变量反序列化为js对象。此时就可以使用结果数据了(该for的for，该取数据的取数据)

```js
$.ajax({
     url:"sendJson",
     type:"post",
     contentType:"application/json;charset=utf-8",
     data:JSON.stringify(sendJson),
     //设置从后端接受数据格式为JSON格式数据
     dataType:"json",
     success:function(res){
        //使用JSON.parse(对象)将后端返回的JSON格式对象反序列化为JS对象
        var data = JSON.parse(res);
        console.log(data);
     }
})
```

---------

### 不依赖Spring框架进行JSON的处理

`@RequestBody`对请求中包含的JSON格式数据参照被其修饰的变量类型进行反序列化。

`@ResponseBody`对方法返回类型的对象进行JSON序列化。

那么这两个标签是如何做到对JSON格式数据进行序列化与反序列化的呢？观察仔细一点我们就会发现起步依赖boot的时，其中会携带一个其名称为`jackson`jar包，这个jar包中的类就是提供给两个标签进行操作的。

下述代码为jackson包的坐标<mark>(可以脱离Spring进行单独的JSON操作)</mark>。

```xml
<dependency>
   <groupId>com.fasterxml.jackson.core</groupId>
   <artifactId>jackson-databind</artifactId>
   <version>2.13.1</version>
</dependency>
```

***<u>首先先声明作者并没有完全深入到两个标签的执行流程中，只是通过Jackson包提供的ObjectMapper类进行JSON数据的序列化和反序列化而已。</u>***

##### 

##### ObjectMapper对象进行对象的JSON序列化

我们需要借助`ObjectMapper`类的`readValue`方法进行对象的JSON序列化，所以我们先来看看readValue方法的源码，如下述代码所示。

```java
 public <T> T readValue(String content, Class<T> valueType) throws JsonProcessingException, JsonMappingException {
     this._assertNotNull("content", content);
     return this.readValue(content, this._typeFactory.constructType(valueType));
 }
```

1）**String content参数**：即JSON格式数据字符串，一般JSON序列化之后数据类型均为字符串类型。

2）**Class<T> valueType参数**：即类的模版对象，一般JSON反序列化化某种对象，均需要按照此对象的类结构进行反序列化。

##### ObjectMapper对象进行JSON格式数据的反序列化

我们需要借助`ObjectMapper`类的``方法进行对象的JSON序列化，所以我们先来看看readValue方法的源码，如下述代码所示。

```java
public String writeValueAsString(Object value) throws JsonProcessingException {
    SegmentedStringWriter sw = new SegmentedStringWriter(this._jsonFactory._getBufferRecycler());

    try {
        this._writeValueAndClose(this.createGenerator((Writer)sw), value);
    }catch (JsonProcessingException var4) {
        throw var4;
    }catch (IOException var5) {
        throw JsonMappingException.fromUnexpectedIOE(var5);
    }

    return sw.getAndClear();
}
```

1）**Object value参数**：即对象实体，将此对象实体序列化为JSON格式字符串。

##### 关于ObjectMapper的使用

```java
ObjectMapper mapper = new ObjectMapper();
//之后就可以使用readValue和WrtieValueAsString方法了
```

--------

### JSON序列化的优点和缺点

> JSON序列化为编码的一种，把对象序列化为字节码流（二进制形式），主要用于网络传输和数据持久化；而反序列化为对象的解码，即把网络中传输的二进制数据流转化为一个正确的对象。

JSON作为轻量级的数据交换格式，优点是

1. 兼容性高，支持多种语言的跨平台性

2. 数据结构比较简单，易于读写

3. 序列化之后数据量小（压缩算法）。

缺点:

1. 语言格式过于严谨

2. 解码后JSON恢复对象结构代码复杂

**适用场景：在项目经常使用HTTP协议+JSON作为客户端与服务端之间的数据传输，也就是配合ajax进行异步信息传输。**
