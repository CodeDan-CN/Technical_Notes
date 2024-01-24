# React脚手架快速安装

### 快速启动

前置条件：拥有npm指令，没有请下载[node.js](https://blog.csdn.net/yuweiqiang/article/details/122476576?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0-122476576-blog-126001104.235^v28^pc_relevant_recovery_v2&spm=1001.2101.3001.4242.1&utm_relevant_index=3)

第一步：全局安装脚手架搭建基础工具

```tex
> npm install -g create-react-app
```



第二步：更换npm下载源头为淘宝源

```tex
npm config set registry https://registry.npm.taobao.org
```



第三步：跳转工作目录进行脚手架使用

```tex
> create-react-app hello-react
```



第四步：根据提示跳转进目录地址，并开始下述代码,，启动项目：

```tex
> npm start
```



### 结构目录介绍

![MLJG](..\图库\mljg.png)

+ package.json：项目依赖关键文件
+ public/index.html：全局唯一个html页面，即大组件的展示页面
+ src：即组件文件以及其所需文件所在目录



### 简单项目

项目结构目录如下所示：

<img src="..\图库\project.png" alt="project" style="zoom:50%;" />

其中public目录中存在入口html文件(index.html)，其代码如下所示：

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <link rel="icon" href="%PUBLIC_URL%/favicon.ico" />
  </head>
  <body>
    <!-- 浏览器不支持js就会渲染出去 -->
    <noscript>You need to enable JavaScript to run this app.</noscript>
    <div id="root"></div>
  </body>
</html>
```



而在src目录下则存在入口js文件(index.js),其代码如下所示：

```js
import React from "react";
import ReactDOM  from "react-dom";
import App from './App'


ReactDOM.render(<App/>,document.getElementById('root'))
```



App组件(App.js)的js代码，如下所示：

```js
import React from 'react'
import Hello from './components/Hello/Hello'
import Welcome from './components/Welcome/Weclome'


class App extends React.Component{

  render(){
    return (
      <div>
        <Hello />
        <Welcome />
      </div>
    )
  }
}

export default App
```



在App.js组件中引入了子组件Hello和Weclome，其存在于src/component/Hello包与src/component/Weclome下，代码分入如下所示：

```JS
import React from "react";

class Hello extends React.Component{

    render(){
        return (
            <h2>
                Hello world!!!!
            </h2>
        )
    }

}

export default Hello
```

```js
import React from "react";

class Weclome extends React.Component{

    render(){
        return (
            <h2>
                Weclome world!!!!
            </h2>
        )
    }

}

export default Weclome
```



### 入门项目

入门演练项目，从头到尾做到下图中的样子，并实现输入回车之后添加入动态列表中，也可以指定列表中某一行进行删除，在最底部可以进行列表全部选择以及点击按钮去除所有勾中的列表内容

<img src="..\图库\REACT01.png" style="zoom:50%;" />..

主要代码就是App.js、Input.js、List.js、Item.js和Botton.js

```js
import React, { Component } from 'react'
import Input from './components/input/Input.js'
import List from './components/list/List.js'
import Botton from './components/botton/Botton.js'

export default class App extends Component {
  // 列表数据的真实存放地点
  state = {
    totol:[
      {id:1,name:'吃饭',status:true},
      {id:2,name:'睡觉',status:true},
      {id:3,name:'吃饭',status:false},
      {id:4,name:'逛街',status:false}
    ]
  }
  // 添加列表数据
  addItem = (item)=>{
    // 在数组的头部加入一个元素
    const newTotol = [item,...this.state.totol]
    this.setState({totol:newTotol})
  }
  
  // 移出数组中指定id的元素
  removeItem = (id)=>{
    const newTotol = this.state.totol.filter((item)=>{
      return item.id !== id
    })
    this.setState({totol:newTotol})
  }

  // 根据id更新数组中指定元素的字段值
  updateItem = (itemObj) =>{
    const newTotol = this.state.totol.map((item)=>{
      if( item.id === itemObj.id )
        return {...item,status:itemObj.state}
      else
        return item
    })
    this.setState({totol:newTotol})
  }

  // 统一修改数组内每个元素指定字段的值
  allChangeByStatus = (status) =>{
    const newTotol = this.state.totol.map((item)=>{
        return {...item,status:status}
    })
    this.setState({totol:newTotol})
  }
  // 移出数组全部元素
  removeAllItem = () =>{
    const newTotol = []
    this.setState({totol:newTotol})
  }


  render() {
    const {totol} = this.state

    return (
      <div>
        <Input addItem={this.addItem}/>
        <List totol={totol} removeItem = {this.removeItem} updateItem= {this.updateItem}/>
        <Botton totol={totol} allChangeByStatus = {this.allChangeByStatus} removeAllItem = {this.removeAllItem}/>
      </div>
    )
  }
}
```

```js
import React, { Component } from 'react'
import {nanoid} from 'nanoid'

export default class Input extends Component {
  buildItem = (event)=>{
    // 获取当前按钮的对应数字
    let currentKeyCode = event.keyCode;
    if( currentKeyCode !== 13 ){
      return
    }
    let value = event.target.value
    if(value === ''){
      return
    }
    // 获取当前输入框的值
    let id = nanoid()
    let name = value;
    let status = false;
    let item = {id:id,name:name,status:status}
    this.props.addItem(item)
  }

  render() {
    return (
      <div>
        <input type='text' placeholder='请输入你的任务名称，按回车确认' onKeyUp={this.buildItem}/>
      </div>
    )
  }
}
    
```

```js
import React, { Component } from 'react'
import Item from '../item/Item'

export default class List extends Component {
  render() {
    const { totol,updateItem,removeItem } = this.props

    return (
      <ul>
        {
          totol.map((item)=>{
            return <Item item={item} removeItem = {removeItem} updateItem={updateItem}></Item>
          })
        }
      </ul>
    )
  }
}
```

```js
import React, { Component } from 'react'

export default class Item extends Component {

  state = {
    mouse:false
  }

  handleMouse = (flag)=>{
    return ()=>{
      this.setState({mouse:flag})
    }
  }

  deleteOpt = ()=>{
    // 获取当前item的id，并将id传入外部方法中
    const {item} = this.props
    this.props.removeItem(item.id)
  }

  updateOpt = (event)=>{
    const {item} = this.props
    let status = event.target.checked
    item.state = status
    this.props.updateItem(item)
  }

  render() {
    const {item} = this.props
    const {mouse} = this.state
    return (
      // 鼠标移入移出事件
      <li onMouseEnter={this.handleMouse(true)} onMouseLeave={this.handleMouse(false)} style={{ backgroundColor: mouse ? '#add' : 'white' }}  >
        <input type='checkbox' checked={ item.status ? true : false } onChange={this.updateOpt}/>
        <span>{item.name}</span>
        <button style={{ display : mouse ? 'block' : 'none' }} onClick={this.deleteOpt}>删除</button>
      </li>
    )
  }
}
```

````js
import React, { Component } from 'react'

export default class Botton extends Component {


  allChange = (event)=>{
    const value = event.target.checked
    this.props.allChangeByStatus(value)
  }

  render() {

    // 统计一下当前newTotol的数量以及已完成的数量
    const {totol} = this.props
    let length = totol.length
    let doneNum = totol.reduce((pre,current)=>{
      return pre + (current.status ? 1 : 0)
    },0)
    return (
      <div>
        <input type='checkbox' onChange={this.allChange}/>    <span>已完成{doneNum} / 全部{length}</span>   <button onClick={this.props.removeAllItem}>清除已完成任务</button>
      </div>
    )
  }
}
````

