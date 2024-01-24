### Vue-Router路由(Vue3)

##### 第一步：下载路由所需依赖

项目中想要使用Vue的路由功能，那么就要先下载一个路由所需依赖，代码如下所示：

```tex
yarn add vue-router@4
```

或者：

```tex
npm install vue-router@4
```

    

##### 第二步：在脚手架中新 建一个路由文件中配置路由基本信息

（1）在启动文件中直接写入路由配置

```js
import * as VueRouter from 'vue-router';


// 1. 定义路由组件.
// 也可以从其他文件导入
const Home = { template: '<div>Home</div>' }
const About = { template: '<div>About</div>' }

// 2. 定义一些路由
// 每个路由都需要映射到一个组件。
// 我们后面再讨论嵌套路由。
const routes = [
  { path: '/', component: Home },
  { path: '/about', component: About },
]

// 3. 创建路由实例并传递 `routes` 配置
// 你可以在这里输入更多的配置，但我们在这里
// 暂时保持简单
const router = VueRouter.createRouter({
  // 4. 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
  history: VueRouter.createWebHashHistory(),
  routes, // `routes: routes` 的缩写
})

// 5. 创建并挂载根实例
const app = Vue.createApp({})
//确保 _use_ 路由实例使
//整个应用支持路由。
app.use(router)

app.mount('#app')
```

    

##### 第三步：在vue文件中<template>标签内使用路由进行跳转

```js

    <!--使用 router-link 组件进行导航 -->
    <!--通过传递 `to` 来指定链接 -->
    <!--`<router-link>` 将呈现一个带有正确 `href` 属性的 `<a>` 标签-->
    <router-link to="/">Go to Home</router-link>
    <router-link to="/about">Go to About</router-link>
```
