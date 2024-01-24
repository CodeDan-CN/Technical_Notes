# WebScoket协议

## **什么是WebSocket协议？**

WebSocket协议属于应用层协议，属于HTML5超文本标记语言开发的网络协议，可以说在逻辑上和HTTP协议毫无关系，为什么说是逻辑上呢？因为WebSocket协议在握手节点借助了HTTP协议来完成。

如果大家了解HTTP协议的话，就会知道HTTP协议是拉协议，即通过一次Request和对应的一次Response请求完成一次数据的拉取。在HTTP1.0的时候，每一次请求数据，都要建立一次HTTP连接，然后拉取到数据之后关闭连接，非常消耗资源。在HTTP1.1时代，HTTP加入长连接的功能，即在客户端和服务器端之间建立一条长时间稳定的HTTP通道，利用这条通道可以完成多条HTTP报文的RTT。

但是就算如此HTTP协议依旧是一个无状态协议，何为无状态，就是服务端不会记住你，服务器端做的就是回应你的请求，哪怕你重复发送，他也会耐心的重复回应………(虽然借助Cookie可以完成服务器识别，但是还是**无法突破”拉协议”的束缚，也就是无法让服务器记住你，然后主动给你发信息**)。

WebSocket协议就是用来解决这个问题的。

综上所述：

**WebSocket协议是一个有状态的持久化网络应用层协议，其可以让服务器端记住客户端，并主动发信息给客户端。**

也就是可以实现客户端一个Request之后可以不同时间得到多个Response的情况。

------

## HTTP协议和WebSocket协议区别

HTTP实例分为两种：HTTP1.0的短链接和HTTP1.1的长连接

对于短连接来说：建立TCP连接–>请求数据–>获取数据–>关闭TCP连接

对于长连接来说：建立TCP连接–>请求数据–>获取数据–>循环请求以及获取–>关闭TCP连接

有人就会疑惑，都有长连接了，那为什么还要WebSocket协议呢？

我们得先知道HTTP1.1实现长连接的方法是什么？是Keep-alive机制，也就是根据握手成功生成一个能存在一定时间的对应这个客户端的TCP通道，但是这个TCP通道在HTTP协议的”拉模式”下，不能作为全双工的TCP通道，因为服务器端不能主动发信息给客户端。

**我们知道当一个TCP通道被建立之后，双方的Socket都处于ESTABLENTED状态，但是由于每次都是客户端发送信息给服务端，然后服务器回复。从来没有服务端主动发信息给客户端段，也就是说，当客户端Socket处于断开状态时，服务器可以接受到这个信息，但是服务器端Socket突然断开了，那么客户端是收不到服务器断开的信息的，只有当客户端再次去请求的时候，才能发现服务器断开了。**

还有一个小问题：由于http协议是无状态的，所以哪怕是在长连接状态下，每次还是会发送header过去。

那么WebSocket就解决了HTTP这两个问题：

1）当WebSocket协议的TCP连接建立完成之后，双方都是可以相互通信的，即客户端可以向服务器端发信息，然后得到回复，服务端也可以主动向客户端发送信息。实现真正的TCP连接的全双工。最简单的例子就是当服务器端下线了，客户端这边可以收到服务端发来的信息。

2）当WebSocket协议的TCP连接建立完成之后，WebSocket帧只有数据。

------

## 协议底层理解WebSocket协议

之前说过为了简化握手阶段，WebSocket协议利用的是HTTP协议的握手，所以在请求和回复报文格式上WebSocket和HTTP相近。（这里不阐述HTTP报文格式，直接上WebSocket报文格式）

**当然并没有给全，基本上握手阶段HTTP协议有的他都有（列出了有差异的首部行）。**

WebSocket请求报文：

```
GET /chat HTTP/1.1
HOST：server.example.com
Upgrade:websocket
connection:Upgrade
Sec-WebSocket-Key:x3JJHMbDL1EzLkh9GBhxDw==
Sec-WebSocket-Protocol:chat,superchat
Sec-WebSocket-Version:13
```

我们来说说和HTTP报文不同的地方：

1）Upgrade和Connection：连接方式不再是HTTP的长连接或者短连接，而是WebSocket的持久化连接

2）之后就是三个关键字段

- **Sec-WebSocket-Key**：即客户端为来校验发送的服务器是不是支持WebSocket协议的服务器的私钥，WebSocket客户端和服务器端都会有一套公钥，客户端发送给服务器端私钥，然后服务器端通过公钥对私钥进行加密后返回给客户端，客户端解密后如果和私钥相同，则可以确认对方是支持WebSocket协议的服务器。
- **Sec-WebSocket-Protocol**：相同URL下提供的不同的服务类型
- **Sec-WebSocket-Version**：和服务器确定使用的WebSocket协议版本号

WebSocket回复报文：

```
HTTP/1.1 101 Switching Protocols
Upgrade:websocket
Connection:Upgrade
Sec-WebSocket-Accept:HSmrc0sMIYUKAGmm5OPpG2HaGWK=
Sec-WebSocket-Protocol:chat
```

来看看两个关键字段：

- **Sec-WebSocket-Accept**：即请求报文中Sec-WebSocket-Key提供的私钥加密后的字符串
- **Sec-WebSocket-Protocol**：确定提供的服务

到这里WebSocket借助Http协议完成握手就结束了，之后就是WebSocket协议的有状态通信模式。

------

## WebSocket协议的运行模式

客户端：啦啦啦，我要建立Websocket协议，需要的服务：chat，Websocket协议版本：17（HTTP Request）

服务端：ok，确认，已升级为Websocket协议（HTTP Protocols Switched）

客户端：麻烦你有信息的时候推送给我噢。。

服务端：ok，有的时候会告诉你的。

服务端：balabalabalabala

服务端：balabalabalabala

服务端：哈哈哈哈哈啊哈哈哈哈

服务端：笑死我了哈哈哈哈哈哈哈

就变成了这样，只需要经过**一次HTTP请求**，就可以做到源源不断的信息传送了。（在程序设计中，这种设计叫做回调，即：你有信息了再来通知我，而不是我傻乎乎的每次跑来问你）

这样的协议解决了上面同步有延迟，而且还非常消耗资源的这种情况。

------

------

## WebSocket协议在HTML5中的使用

WebSocket协议在浏览器以及服务器之间能够做到在浏览器中实时获取到服务器中指定数据的变化。

一般我们在基于HTTP协议的浏览器以及服务器之间都是只能让浏览器主动发送HTTP请求报文轮询服务器，从而获取到最新数据。但是由于HTTP协议是无状态协议，也就是每次在报文数据中都会携带占比较多的请求头数据，十分占用资源。

如果说在浏览器端和服务器端之间使用WebSocket协议，那么当服务器端有数据发生改变的时候，就会主动发送消息给浏览器端，这样就避免了浏览器发送大量请求占用资源的情况。

------

#### 连接WebSocket服务器

我们来看看WebSocket协议在HTML5中到底怎么使用？

首先我们要在使用WebSocket协议的请求被执行之前，让浏览器连接上我们的WebSocket服务器。也就是建立起双方的WebSocket连接。指令如下所示：

```
var Socket = new WebSocket(url,[protocol]);
```

其中url就不用多说了，为访问WebSocket协议请求路径。第二个参数也就是可选参数protocol用于指定可使用的子协议。

当我们连接上了WebSocket服务器之后，接下来我们肯定得知道当前和WebSocket服务器之间的连接状态，才能进行之后的操作。

------

#### HTML5中WebSocket的连接状态Socket.readyState

Socket.readyState拥有4种不同的状态：

1）*Socket.readyState == 0 ：表示连接尚未建立。*

2）*Socket.readyState == 1 ：表示连接*已经建立成功，可以开始通信。

3）*Socket.readyState == 2 ：表示连接*正在关闭。

4）*Socket.readyState == 3 ：表示连接*已经关闭。

当我们通过关于Socket.readyState的判断之后，就可以使用使用WebSocket协议和指定服务器进行通信了，也就是使用HTML提供的使用Websocket协议事件和方法。

------

#### HTML5中提供的使用WebSocket协议事件和方法

我们先了解一下一共有哪些事件：

| 事件    | 事件处理程序     | 描述                                   |
| :------ | :--------------- | :------------------------------------- |
| open    | Socket.onopen    | 连接建立时触发                         |
| message | Socket.onmessage | 连接成功后，客户端接收服务端数据时触发 |
| error   | Socket.onerror   | 通信发生错误时触发                     |
| close   | Socket.onclose   | 连接关闭时触发                         |

**除了onmessage事件对应ChannelRead0方法之后，其他事件均不对应任何Netty方法。**

我们看看每种事件在HTML5中的写法格式：

```
	<script>
		var socket = new WebSocket("ws://localhost:8000/test");
		//判断一下浏览器支不支持WebSocket协议
	    if(window.WebSocket){
			//ev为收到的服务器端回送的信息，ev.data为服务器返回的消息。
			socket.onopen = function(ev){
				//......;
			}
			
			socket.onmessage = function(ev){
				//......;
			}
			
			socket.onerror = function(ev){
				//......;
			}
			
			socket.onclose = function(ev){
				//......;
			}
		}else{
			alert("当前浏览器不支持WebSocket协议")
		}
	</script>
```

我们再看看看方法：

| 方法  | 方法处理程序   | 描述                               |
| :---- | :------------- | :--------------------------------- |
| send  | Socket.send()  | 连接建立完成后，发送数据给服务器   |
| close | Socket.close() | 连接建立完成后，关闭和服务器的连接 |

我们看看每种方法在HTML5中的写法格式：

```
    <body>
        <script>
                function send(){
			if(!window.socket){
				return ;
			}
			if(socket.readyState == WebSocket.OPEN){
				socket.send("你好");
			}
		}
	</script>
	<button value="发送" onclick="send()"></button>
    </body>
```

**扩展：WebSocket服务器代码可借阅我的博客[“基于Netty编写的WebSocket消息服务器”](http://www.studylove.cn:8000/2021/11/18/基于netty的websocket协议服务器实例（十二）/)。**