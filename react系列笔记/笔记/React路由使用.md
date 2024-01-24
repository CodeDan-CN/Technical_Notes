# React-Route快速启动

### React-Route5简单案例流程

##### 引入React-Route依赖

```tex
npm i react-router-dom@5
```

```jsx
import {Link,BrowserRoute,Route} from 'react-router-dom'
```



##### 编写React-Route-Dom路由链接

扩展点：

+ BrowserRoute：即/a/b/c
+ HashRoute：即/#/b/c

```jsx
<Link to="/componentName" >ComponentName</Link>
```

```jsx
<NavLink activeName = "active" className = "className" to="/componentName" >ComponentName</Link>
```



##### 注册React-Route-Dom路由

```jsx
<Route path="/linkToPath" component={componentName} ></Route>
```

小技巧一：(Switch可以只匹配一次路由)

```jsx
import {Switch} from 'react-router-dom'

<Switch>
    <Route path="/linkToPath" component={componentName} ></Route>
    <Route path="/linkToPath1" component={componentName123} ></Route>
</Switch>
```



##### 交给统一的路由容器进行管理

```jsx
// 在入口js中
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import {BrowserRoute} from 'react-router-dom'

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <BrowserRoute>
    <App />
  </BrowserRoute>
);
```

小技巧二：（%Public_URL%）

***在使用多级路由的情况下，一切静态资源的引入以绝对路径为主，而`%Public_URL%`则是获取public目录绝对路径的函数***

小技巧三：Redirect组件

在所有Route组件中设置路由没有被匹配的时候，都被重定向到指定的Route组件对应的路由中

```jsx
import {Switch,Redirect} from 'react-router-dom'

<Switch>
    <Route path="/linkToPath" component={componentName} ></Route>
    <Route path="/linkToPath1" component={componentName123} ></Route>
	<Redirect to="/linkToPath" />
</Switch>
```



### 路由的模糊匹配以及严格匹配

**模糊匹配**：是最左匹配原则，即从链接的多级路由中从左到右去注册的路由中进行匹配。

**严格匹配**：完美一模一样的匹配

默认是模糊匹配，下述代码开启严格匹配：

```jsx
<Route exact path="/linkToPath" component={componentName} ></Route>
```

**尽量不开严格模式，严格模式下二级路由存在无法匹配的情况**



### 路由的多级匹配

在React中，如果第一级路由比如/news。你已经点击跳转进去，那么这个news组件中定义的路由你要想匹配的话，就必须将链接变成/news/show才能进行匹配



这个时候就可以想想多级模式下开启严格匹配的后果了，**首先如果我们是去匹配/news/show的话，第一层/news就无法进入，导致直接进入到redirect**

这个问题在React Route V6会被解决



### 路由组件的参数传递

固定三个参数形式传递：

```jsx
history:
	go:f goBack()
	goBack: f goBack()
	goForward: f goForward()
	push: f push(path,state)
	replace: f replace(path,state)
location：
	pathname:"/componentName"
	search:""
	state: undefined
match：
	isExact: true
	params: {}
	path: "/componentName"
	url: "/componentName"
```



##### params参数传递方式

```jsx
<Link to={`/componentName/${value1}/${value2}`} >ComponentName</Link>

<Route path="/linkToPath/:value1/:value2" component={componentName} ></Route> 

// 在componentName组件中使用params参数
const {value1,value2} = this.props.math.params    useParams const [value1,value2] = useParams()
```



##### search参数传递方式

此参数传递值为?id=213&name=123,所以手动进行参数的分解，但是也是有库进行操作的`querystring`

```jsx
import qs from 'querystring'
```

```jsx
<Link to={`/componentName/?id=${value1}&name=${value2}`} >ComponentName</Link>

// 在componentName组件中使用params参数
const {search} = this.props.location
// 获取参数JSON串
const result = qs.parse(search.slice(1))    useSearchParams        const [search,setSearch] = useSearchParams()     search.get('KEY')
```



##### state参数传递方式

```jsx
<Link to={{pathname:'/componentName',state:{id:value1,name:value2}}} >ComponentName</Link>

// 接受方式,这里的|| {}是当清空缓存或者具体地说是history对象之后，location和math也就变成了初始的样子，取不到上次传递的值的。所以为了防止网站为空报错，给个空数据
const {state} = this.props.location || {}


<Link to='/componentName' state:{{id:value1,name:value2}} >ComponentName</Link>
useLocation
const {state} = useLocation()
```



### 路由的Push模式与Replace模式

+ push模式：即浏览器的路由跳转均会被压入栈中，呈栈压入的顺序，Route默认是此模式
+ replace模式：即压入栈中替换到栈顶元素记录

```jsx
<Route replace={true} path="/linkToPath1" component={componentName123} ></Route>
```

上述两个模式可以在不同层级的Route中混用



### 编程式路由

即在用户不点击路由的情况下，完成路由的跳转。通常是借助其他按钮去触发指定的路由，比如下方代码

```jsx
pushShow = (id,title)=>{
    // 参数传递就按照上面三种传递方式写就行比如push(`/componentName/?id=${value1}&name=${value2}`),但是state方式有些不同，有专门的方法push(path,state)，replace(path,state)
    this.props.history.push("/linkToPath")
}

render(){
    const {id,title} = this.props.location.state
    return(
        <Link to="/linkToPath">链接1</Link>
		<Route path="/linkToPath" component={componentName} ></Route>
		<button onClick={()=>{this.pushShow(id,title)}}></button>
    )
}


const navigate = useNavigate()
pushShow = (id,title)=>{
    // 参数传递就按照上面三种传递方式写就行比如push(`/componentName/?id=${value1}&name=${value2}`),但是state方式有些不同，有专门的方法push(path,state)，replace(path,state)
    navigate("/linkToPath")   如果不要/，单纯linkToPath，那就会去匹配相对路由
}

```

##### 特殊方法介绍

+ goBack()：路由回退
+ goForward()：路由前进
+ go(n)：整数前进，负数回退

```jsx
this.props.history.goBack()       navigate(-1) 
this.props.history.goForward()    navigate(1) 
this.props.history.go(-2)
```

**注意**：普通组件中不包含路由组件的特殊方法，需要使用withRouter将普通组件改造一下，才能实现通过普通组件中的方法去调用上述三个路由方法。（一些特定需求会需要）

```jsx
class Hello extend Component{.....]

export default withRouter(Hello)
```



### React-Route6简单案例流程

**常规流程**

```tex
> npm i react-router-dom
```

```jsx
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { BrowserRouter } from 'react-router-dom';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <BrowserRouter>
    <App />
  </BrowserRouter>
);
```



##### 普通写法

```jsx
import React from 'react'
import { Link, Navigate, Route, Routes } from 'react-router-dom'
import TopRouter  from './route/TopRouter'
import BottonRouter from './route/BottonRouter'

export default function App() {

  return (
    <div>
      <Link to="/topRouter">左侧导航1</Link>
      <br></br>
      <Link to="/bottonRouter">左侧导航2</Link>
      <br></br>
      <br></br>
      <br></br>
      <br></br>
      <br></br>
      <Routes>
        <Route path='/topRouter' element={<TopRouter/>} ></Route>
        <Route path='/bottonRouter' element={<BottonRouter/>} ></Route>
        {/* 代替Redired，点击默认跳转 */}
        <Route path='/' element={<Navigate to="/topRouter"/>}></Route>
      </Routes>
    </div>
  )
}
```



##### 路由表写法

```jsx
import React from 'react'
import { Link, Navigate, useRoutes  } from 'react-router-dom'
import TopRouter  from './route/TopRouter'
import BottonRouter from './route/BottonRouter'

export default function App() {

  const element = useRoutes([
    {path:'/topRouter',element:<TopRouter/>},
    {path:'/bottonRouter',element:<BottonRouter/>},
    {path:'/',element:<Navigate to="/topRouter"/>}
  ])

  return (
    <div>
      <Link to="/topRouter">左侧导航1</Link>
      <br></br>
      <Link to="/bottonRouter">左侧导航2</Link>
      <br></br>
      <br></br>
      <br></br>
      <br></br>
      <br></br>
      {element}
    </div>
  )
}
```

一般为了保证组件内部的纯洁性，不会把路由表的指定放在组件内，而是通过创建src/route文件夹，在其中存放路由表

```jsx
```



































































