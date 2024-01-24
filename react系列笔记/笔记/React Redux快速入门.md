# Redux与React-Redux快速入门

### 什么是Redux

即不同组件之间状态集中式管理的工具，比起发布-订阅这种模块，他主要特点就在于集中管理。

![](..\图库\redux.png)

上述为redux工作的原理图，其分别是由React Components、Action Creators、Store以及Reducers组成

1. Action Creators：动作的包装对象，将组件的动作包装为type和data两个字段，用于后续dispatch函数传递包装对象

2. Store：相当于一个大脑，能够将其他成员联系在一起

   获取store方式

   ```jsx
   import {createStore} from 'redux'
   import reducer from './reducers'
   const store = createStore(reducer)
   ```

   使用store方式

   ```jsx
   store.getState ()
   store.dispatch(action)
   store.subscribe(listener) //注册监听
   ```

   

3. Reducers：即真正干活的成员，用于初始化状态以及加工状态，其中previousState代表旧的值，action代表本次动作，其根据这两个值产生新的状态返回给store，再由store交给组件。值得一提的式previousState最开始是undifen，第一次初始化状态的时候，会将状态从undifferent转化为对应的初始值



### 快速启动redux

```jsx
npm install reudx
```



**第二步：创建reducer**

```jsx
/**
 * 重写reducer中方法,reducer本质就是一个函数
 */
const initalState = 0
export default function countReducer(preState=initalState,action){
    console.log(preState+"---"+action)
    const {type,data} = action
    switch(type){
        case 'add': return preState + data; 
        case 'del': return preState - data; 
        default:
            return preState
    }
}
```



**第三步：创建store**

```jsx
/**
 * 自定义一个store并暴露出去
 */
import {legacy_createStore as createStore} from 'redux'
import countReducer from './count_reducer'

export default createStore(countReducer)
```



**第三步：定义action creators**

```jsx
export const add = ()=>{
    return {type:'add',data:1*1}
}

export const del = ()=>{
    return {type:'del',data:1*1}
}
```



**第四步：整合使用redux**

```jsx
import React, { Component } from 'react'
import store from '../redux/store'
import {add,del} from '../redux/count_action'

export default class Count extends Component {

  add = ()=>{
    store.dispatch(add())
  }

  del = ()=>{
    store.dispatch(del())
  }

  render() {
    return (
      <div>
        count:
        {store.getState()}
        <button onClick={this.add}>add</button>
        <button onClick={this.del}>del</button>
      </div>
    )
  }
}
```



**第五步：设置redux中状态改变监听，实现状态改变后的实时更新**

```jsx
import React from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import store from './redux/store'

const container = document.getElementById('root');
const root = createRoot(container);

root.render(<App/>);
// 利用虚拟diff的计算方式只渲染改变的组件
store.subscribe(()=>{
   root.render(<App/>);
})
```



**扩展步骤：进行dispatch的函数传递**

```jsx
// 安装thunk
npm install redux-thunk

// 在action中
export const useFunc = ()=>{
    return ()=>{
        store.dispatch(add())
    }
}

// 在Count.js
  useFunc = ()=>{
    store.dispatch(useFunc())
  }

// 在store.js文件中引入thunk并使用
import {legacy_createStore as createStore,applyMiddleware} from 'redux'
import countReducer from './count_reducer'
import thunk from 'redux-thunk'

export default createStore(countReducer,applyMiddleware(thunk))
```



### React-Redux语法

> React针对Redux推出了React-Redux，React-Redux通过将组件分为UI组件和容器组件，其中容器组件作为UI组件的父亲组件，专门负责与Redux进行状态、动作的交互（所有redux的api调用均在容器组件中）。同时也负责将交互的消息通过父子组件传递方式(props)将更新之后的状态交给UI组件，UI组件再进行状态显示的更新。

![react-redux](..\图库\react-redux.png)

案例写不出来 pass这个逼



### 多个状态时如何操作

多个状态被组件所使用时，需要将状态给与key值，下面通过新增Person组件的方式，新增一个Person状态，并和Count组件及其状态共同使用

**第一步：新增action**

```jsx
import store from '../store'

export const add = ()=>{
    return {type:'add',data:1*1}
}

export const del = ()=>{
    return {type:'del',data:1*1}
}

export const useFunc = ()=>{
    return ()=>{
        store.dispatch(add())
    }
}
```



**第二步：新增reducer**

```jsx
/**
 * 重写reducer中方法,reducer本质就是一个函数
 */
const initalState = 0
export default function countReducer(preState=initalState,action){
    console.log(preState+"---"+action)
    const {type,data} = action
    switch(type){
        case 'add': return preState + data; 
        case 'del': return preState - data; 
        default:
            return preState
    }
}
```



**第三步：改造store**

```jsx
/**
 * 自定义一个store并暴露出去
 */
import {legacy_createStore as createStore,applyMiddleware,combineReducers} from 'redux'
import countReducer from './Count/count_reducer'
import thunk from 'redux-thunk'
import personReducer from './Person/person_reducer'

// 核心就在这里，通过给reducer中操作的状态一个key，通过key让store去获取到对应的值并返回给组件
const allReducer = combineReducers({
    countNumber:countReducer,
    persons:personReducer
})

export default createStore(allReducer,applyMiddleware(thunk))
```



**第四步：编写Person组件**

```jsx
import React, { Component } from 'react'
import  {addProject} from '../redux/Person/person_action'
import store from '../redux/store'

export default class Person extends Component {


  addProject = () =>{
    store.dispatch(addProject())
  }


  render() {

    const persons = store.getState().persons
    return (
      <div>
        数据：
        {
            persons.map((item)=>{
                return (
                    <div key={item.id}>{item.name}</div>
                )
            })
        }
        <button onClick={this.addProject}>添加
        </button>
      </div>
    )
  }
}
```



**第五步：改造Count组件**

```jsx
import React, { Component } from 'react'
import store from '../redux/store'
import {add,del,useFunc} from '../redux/Count/count_action'

export default class Count extends Component {

  add = ()=>{
    store.dispatch(add())
  }

  del = ()=>{
    store.dispatch(del())
  }

  useFunc = ()=>{
    store.dispatch(useFunc())
  }

  render() {
    return (
      <div>
        count:
        {store.getState().countNumber}
        <button onClick={this.add}>add</button>
        <button onClick={this.del}>del</button>
        <button onClick={this.useFunc}>useFunc</button>
      </div>
    )
  }
}
```







































