# axios框架快速入门

### axios跨域处理方式

使用axios的时候，如果前后端环境处于跨域情况，那么就需要使用axios的代理机制去解决此次跨域请求与响应。

我们知道跨域问题其实是浏览器与服务器处于不同的源中，而浏览器要保证同源策略，所以axios就采用暗中启动一个小的服务器（与前端同端口），使用这个小的服务器与真实的后端服务进行交互。由于是服务器与服务器之间的交互，所以不会存在跨域问题。而小服务器与浏览器之间能够保证同源策略，所以自然就解决了跨域问题。



### axios代理使用方式

##### 方式一：调整package.json

```json
"proxy":"服务器地址[http://localhost:5000]"
```

修改axios的请求地址为当前前端所在地址，比如当前前端地址为`http://localhost:3000`,那么就要把axios当前请求的后端地址换成`http://localhost:3000`

**注意：使用代理需要保证代理的请求或者资源是在前端不存在的，不然首先前端资源**

权限：现在项目中，其实有一些微服务之间的交互场景是通过前端完成的，所以前端往往需要对接多个微服务，每个不同业务的微服务所在服务器可能是不同的，也就是无法完成代理多台服务器。



##### 方式二： 在src目录下编写stepProxy.js文件

```json
const {createProxyMiddleware} = require('http-proxy-middleware')
 
module.exports = function(app) {
  app.use(
    createProxyMiddleware('/gateway', {  //api1是需要转发的请求(所有带有/api1前缀的请求都会转发给5000)
      target: 'http://localhost:9092', //配置转发目标地址(能返回数据的服务器地址)
      changeOrigin: true, //控制服务器接收到的请求头中host字段的值
      /*
      	changeOrigin设置为true时，服务器收到的请求头中的host为：localhost:5000
      	changeOrigin设置为false时，服务器收到的请求头中的host为：localhost:3000
      	changeOrigin默认值为false，但我们一般将changeOrigin值设为true
      */
      pathRewrite: {'^/gateway': ''} //去除请求前缀，保证交给后台服务器的是正常请求地址(必须配置)
    }),
 
    createProxyMiddleware('/webhook', { 
      target: 'http://localhost:9091',
      changeOrigin: true,
      pathRewrite: {'^/webhook': ''}
    })
  )
} 
```

通过类似nginx设置不同前缀进而向不同服务器转发的原理相同，axios也可以设置前缀去将不同的请求代理转发给不同的服务器

而使用方式则是如下代码所示：

```js
  getResult = ()=>{
    axios.get('http://localhost:3000/gateway/axios/hello').then(
      response => { console.log('成功了',response.data) },
      error =>{ console.log('失败了',error) }
    )
  }
```

