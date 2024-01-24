此面经为笔者自己用学习的知识撰写，如果有错误的地方，请指正，感谢！

**问题一：了解过Netty嘛？为什么说Netty是高性能网络通信框架？**

​    Netty是一款基于Java Nio封装的高性能的网络应用程序框架，也就是基于事件异步驱动，让开发者可以快速的开发高性能网络客户端/服务端。

  Netty被称为高性能网络通信框架的原因主要是：

1. **高并发**：由于Netty使用Nio作为实现基础，具有非阻塞以及异步IO的特性，所以在并发上性能不错。
2. **封装度强**：把大部分NIO操作代码进行了封装，使用简单，我们只需要考虑网络应用中的业务逻辑代码的编写
3. **传输快**：因为Netty的文件传输是通过零拷贝的方式进行的，在系统内部不需要CPU多次在不同的内存中拷贝数据。
4. **支持多种编码解码协议**，在Netty中内置多种类型的编码与解码器，比如HttpRequestEncode和HttpResponseDecode，ProtoBuf等。
5. **串行化处理读写操作**：即通过轮训到请求之后，会把对应的channel放入单独channelPipeline中，ChannelPipeline中维护了一个双向链表，其中一个方向代表信息入站处理链路，一个方向代表信息出站处理链路。通过这种方式解决了使用锁带来的性能问题。

------

**问题二：什么是零拷贝？**

零拷贝指的是尽量减少CPU将数据从一块存储拷贝到另一块存储的技术，注意是减少CPU拷贝。

我们在传统网络IO上，我们会先将磁盘中的数据通过DMA(也就是直接内存拷贝)加载到我们的内核缓存区，然后CPU则通过调度应用进程去获取内核缓存区中的数据并操作此数据后，再使用CPU调度应用进程将修改后的数据拷贝到Scoket缓存区中，最后Socket缓存区再通过DMA写入到网络中。

也就是我们正常会经历两次CPU参与的数据拷贝，每一次CPU去跨域去内核中获取数据都会照成性能开销，我们可以使用mmp(内存映射)和sendFile技术。

mmp技术主要是用于把内核缓存区中的数据映射到应用进程缓存区中，这样应用进程就可以直接修改数据，减少了一次CPU参与的数据拷贝，但是还需要CPU调度我们的应用程序进程把修改之后的数据写入(拷贝)到Socket网络缓存区中，也就是还有一次CPU参与的数据拷贝。

SendFile分为两个版本：linux2.1版本下让数据不在通过应用进程缓存区，直接从内核缓存区中将数据通过CPU拷贝到Socket缓存区中，但是还是有一次CPU参与的拷贝。

Linux2.4版本下：sendFIle机制作出了优化，也就是不再让CPU参与内核缓存区数据拷贝到Socket数据缓存区，借助硬件的特性，具体就是把内核缓存区的描述符传递到Socket缓存区中，再把数据长度传递到Socket缓冲区中，这样就可以直接使用DMA到内核缓冲区中取出对应数据直接到网络中。这样就没有一次CPU参与的数据拷贝了。

但是sendFile的缺陷也很明显，就是无法进行数据的修改，只能直接进行数据的IO传输。

------

**问题三：BIO，NIO和AIO的区别？**

BIO全程Block IO也就是同步阻塞IO，当IO操作进行时系统会进入阻塞，直到IO操作结束才会解除阻塞。当多线程环境下，就会分出一个线程对应一个IO操作的方式，来提高性能，但是线程也是一个宝贵的资源。

AIO即异步IO，当我们主线进行IO的时候，那么就会使用一个子线程去执行这个IO操作，当子线程IO操作执行完毕之后，通过通知等方式去告知主线程结果。

NIO即非阻塞IO，也被称为多路复用IO，通过单线程对内核进行轮训，一旦发生特定的IO事件，那么可以选择就轮训线程去执行，也可以选择创建一个新的线程去执行处理操作，然后轮训线程继续轮训。

------

**问题四：说说NIO的组成？**

Java NIO由Channel，ByteBuffer，Seletor组件组成。

Channel用于内核到应用程序之间的连接，但是不同于IO中的Reader或者inputStream通道流，Channel是双向的，也就是数据可以通过Channel从内核到应用程序，也可以应用程序到内核。其拥有文件传输的FileChannel，基于UDP协议传输的DatagramChannel，基于TCP协议的SocketChannel和监听Socket的ServerSocketChannel。

ByteBuffer组件是作为传输数据的载体，数据再Channel通道中传输均是以ByteBuf为数据单位进行的传输，ByteBuffer本质上是一块连续的内存地址，存储数据通过编码之后生成的字节码流。其通过字段position，limit，capacity和mark来通知ByteBuffer中的读写。

Seletor组件用于轮训监听的Socket，一旦Seletor组件轮训到Socket中发生Seltor中注册的Channel感兴趣的特定事件的时候，那么就会把这个Channel放入SeletorKey集合中，等待处理。

可以选择就在轮训线程中进行对应Handler处理，也可以新建一个线程进行Handler处理。

------

**问题五：了解过Netty的线程模型嘛，是一个怎么样的模型？**

Netty的线程模型为主从Reactor多线程模型，主从Reactor为主Reactor（BossGroup）和从Reactor（WorkerGroup），其中主Reactor主要用于监听Socket发生accept事件，从WorkerGroup负责对应的读写请求处理。

多线程为BossGroup和WorkerGroup中均管理着多个线程，每个线程都具有一个seletor组件和dispatch组件，其中BossGroup中的seletor组件用于轮训监听的Socket，一旦发生accept事件，就将产生的SocketChannel通过dispatch转发组件注册到WorkerGroup中某一个线程的Seletor组件中。一旦这个注册到某个WorkerGroup的Seletor组件中的SocketChannel发生读写事件的时候，就会由这个WorkerGroup线程去处理这个事件。

------

**问题六：TCP粘包/拆包的原因以及解决方法？**

我们得先说说TCP粘包和拆分的来源，由于TCP协议是一个流式传输协议，在TCP短连接中，一次数据的完整性可以通过连接的建立和连接关闭来确定，但是在TCP长连接中，我们无法通过上述的方式来确定一个数据是否完整，当我们在一个长连接的数据传输的时候，连续发送两个数据，那么就有可能在服务端收到的数据，可能是一个数据加上另一个数据的一部分，也可以是一个数据的一部分，也可能是两个数据都接受到了，这些情况被某些程序员称为粘包/拆包**（为什么会这样设计到TCP的分段与重组）**，其实TCP协议根本就没有数据包的概念，但是这样说的程序员多了之后，也就是使用粘包和拆包形成上述现象。

解决这两种现象的方法大致分为三种：都是建立在编码和解码的步骤上

1. **固定数据长度**，也就是在解码的时候，每次固定获取多少数据长度的字节流进行解码。
2. **通过制定的字符或者字符串当作数据的结束符**，即当解码器读取到一段字节流的时候，一旦读取到定制的字符或者字符串就把这一段字节流数据进行解码。
3. **通过定义逻辑上的”包头+包体”来自定义上层协议的方式，**即通过固定长度的报文头获取到包体中数据的长度之和，读取指定数据长度的字节流进行编码。

------

**问题七：Netty中共有那些重要组件？列举一下**

1. Channel组件：在Netty中提供了不同与NIO的Channel组件，用于网络操作，比如bind，connect，read，write等。Netty为其提供了不少的派生类，比如NioServerSocketChannel和NioSocketChannel等等。
2. EventLoopGroup组件，BosGroup和WorkerGroup的本质，是一个拥有多个线程的线程池，其中管理着多个EventLoop线程对象，每一个EventLoop现场对象中都包含一个Seletor组件和dispath组件，用以监听特定的IO事件以及转发注册到别的Seletor组件中。
3. EventLoop组件，除了上述功能之外，EventLoop还具有异步处理当前特定IO事件的能力。EventLoop也是一个线程池对象，只不过是一个核心线程等于最大线程数等于1的线程池，也就是当多个在此EventLoop中注册的通道同时发送读写事件的时候，EventLoop就会将这些通道任务放入异步任务阻塞队列中，然后一件一件的处理。

  EventLoop异步任务队列一共有两个，一个TaskQueue，一个ScheduledtaskQueue。

- ChannelFutue组件：为了保证Netty中所有的IO操作是异步的，我们需要使用ChannelFuture的addListener()方法注册一个ChannelFutureListener监听事件对象，当操作执行成功或者失败的时候，监听就会自动触发返回结果。
- ChannelHandler，ChannelHandlerContext，ChannelPipeline组件：这三个组件不好分开说，我们先从数据结构上说，ChannelPipeline维护着一个由ChannelHandlerContext作为节点组成的一个双向链表，每一个ChannelHandlerContext中都包含了一个Handler。

为什么要说维护而不是拥有，因为这个双向链表的头尾节点是DefaultChannelPipeline对象，实际上使用他们拥有的。

知道这个之后我们再来分组件说说作用，ChannelHandler中即对应事件的处理业务逻辑代码封装类，而ChannelPipeline负责拦截和处理Channel通道中入站和出站的事件和操作，相当于一个链式处理的实现。而每一个ChannelHandler就是起到拦截和处理的，但是ChannelHandler之间还分为出站Handler和入站Handler，分别拦截和处理不同的事件，那么ChannelhandlerContext使用inbound和outbound的boolean字段来标志这个ChannelHandler是用于出站的还是入站的。

ChannelPipeline和ChannelhandlerContext拥有相同的channel，用来标志链上的对应关系。

------

**问题八：了解过几种序列化协议，我们该如何配合业务场景选择这些序列话协议？**

想要清楚分析不同序列化协议的优缺点，我们就得从序列化看重的地方说起，序列化为编码的一种，把对象序列化为字节码流（二进制形式），主要用于网络传输和数据持久化；而反序列化为对象的解码，即把网络中传输的二进制数据流转化为一个正确的对象。

影响序列化的三大因素：

1）序列化之后的码流大小（即占网络带宽的大小）

2）序列化的性能（即CPU的占用情况）

3）是否支持跨语言（即从Java服务消费者序列化对象到Python服务提供者之间的对象传输）

Java本身提供了序列化方式，即通过实现Serialize接口配合ObjectinputStream和ObjectoutputStream流完成对象之间的序列化传输。

但是这种序列化方式占用很大的网络带宽，序列化时CPU占用率高，也不支持跨语言。

所以我使用最多的还是xml，json，protobuf这三种序列话协议。

首先xml，优点是（1）人机可行性好，可指定元素的名称。（2）支持跨语言。缺点是(1)序列化数据只包含数据本身以及类的结构，不包含类型标识；(2)只能序列化公共字段；不能序列化方法；(3)文件庞大，文件格式复杂，传输占大带宽。

适用场景：当作配置文件存储数据。

然后来说说JSON，JSON作为轻量级的数据交换格式，优点是（1）兼容性高，支持多种语言的跨平台性（2）数据结构比较简单，易于读写（3）序列化之后数据量小（压缩算法）。

缺点：（1）语言过于严谨（2）解码后JSON恢复对象结构代码复杂

适用场景：在项目经常使用HTTP协议+JSON作为客户端与服务端之间的数据传输，也就是配合ajax进行异步信息传输。

最后来说说protobuf协议，这个协议是谷歌内部使用的一个PRC框架中服务之间的数据传输协议。优点是（1）支持跨平台（2）结构化数据存储格式（3）序列化之后码流小。缺点：（1）需要使用protoc工具支持，（2）构建结构化对象实例时链式编程繁琐

  适用场景：服务与服务之间的数据传输。

------

**问题九：知道Netty的心跳机制嘛？**

由于Netty是基于TCP协议开发的，所以TCP协议的缺陷，Netty也会有，就比如在长连接下，客户端因为网络原因或者长时间没有发送数据，连接可能会中断，此时客户端并不会发送任何的断开信息。那么为了解决这个问题，TCP协议采用了保活机制，也就是可以实时指导这个TCP连接通道的状态。

Netty中的心跳机制就是这样的一种保活策略，Netty即通过IdleStateHandler类支持三种超时时间检测，即当满足下述条件就出触发下一个Handler的userEventTriggered方法，我们可以在这个方法中进行关闭或者发送心跳包进行检测等等操作。

1）readerldleTime：读超时时间

2）writerldieTime：写超时时间

3）timeUtil：超时时间单位

------

**问题十：说说Netty的异步处理机制？**

Netty网络框架的所有IO操作都是异步的，比如connect连接操作，bind监听操作，read读操作，writer写操作都会返回一个ChannelFuture对象。而实现这些的基础都是ChannelFuture的底层是有Future类和Callable类组成的。

通过返回的ChannelFuture对象，我们可以获取到异步的结果。以及一些异步操作的方法操作。

比如判断当前异步操作是否完成的isDone方法

比如判断已完成的当前操作是否是成功完成的方法isSuccess()

比如获取当前已完成的当前操作失败的原因getCause方法

还可以使用addListener()给异步方法添加监听器，当操作完成（isDone为true）时，将会通知指定的监听器；