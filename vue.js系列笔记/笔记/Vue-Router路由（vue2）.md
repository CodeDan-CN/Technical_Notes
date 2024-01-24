### Vue-Router路由（vue2）

##### 第一步：下载路由所需依赖

项目中想要使用Vue的路由功能，那么就要先下载一个路由所需依赖，代码如下所示：

```tex
npm i vue-router@3
```

    

##### 第二步：在脚手架中新 建一个路由文件中配置路由基本信息

（1）在启动文件中直接写入路由配置

```js
import Vue from 'vue'
import App from './App.vue'
//路由关键一

import fooPage from "./pages/FooPage.vue";
import booPage from "./pages/BooPage.vue";

Vue.config.productionTip = false

//路由关键

// 0. 如果使用模块化机制编程，导入Vue和VueRouter，要调用 Vue.use(VueRouter)
Vue.use(VueRouter)
// 2. 定义路由
// 每个路由应该映射一个组件。 其中"component" 可以是
// 通过 Vue.extend() 创建的组件构造器，
// 或者，只是一个组件配置对象。
// 我们晚点再讨论嵌套路由。
const routes = [
  { path: '/foo', component: fooPage },
  { path: '/bar', component: booPage }
]

// 3. 创建 router 实例，然后传 `routes` 配置
// 你还可以传别的配置参数, 不过先这么简单着吧。
const router = new VueRouter({
  routes // (缩写) 相当于 routes: routes
})

// 4. 创建和挂载根实例。
// 记得要通过 router 配置参数注入路由，
// 从而让整个应用都有路由功能
// 现在，应用已经启动了！
const app = new Vue({
  router,
  render: h => h(App),
})

app.$mount('#app')

```

    

（2）单独定义路由文件后在启动文件中引入路由配置

```js
//单独的路由配置
import VueRouter from 'vue-router'
import fooPage from "@/pages/FooPage";
import booPage from "@/pages/BooPage";

// 2. 定义路由
// 每个路由应该映射一个组件。 其中"component" 可以是
// 通过 Vue.extend() 创建的组件构造器，
// 或者，只是一个组件配置对象。
// 我们晚点再讨论嵌套路由。
const routes = [
    { path: '/foo', component: fooPage },
    { path: '/bar', component: booPage }
]

// 3. 创建 router 实例，然后传 `routes` 配置
// 你还可以传别的配置参数, 不过先这么简单着吧。
export default new VueRouter({
    routes // (缩写) 相当于 routes: routes
})


```

```js
//启动文件
import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import router from './router/index'
Vue.config.productionTip = false

Vue.use(VueRouter)

// 4. 创建和挂载根实例。
// 记得要通过 router 配置参数注入路由，
// 从而让整个应用都有路由功能
// 现在，应用已经启动了！
const app = new Vue({
  router:router,
  render: h => h(App),
})

app.$mount('#app')

```





    

##### 第三步：在vue文件中<template>标签内使用路由调整

```html
<template>
  <div id="app">
    <img alt="Vue logo" src="./assets/logo.png">
    <p>
      <!-- 使用 router-link 组件来导航. -->
      <!-- 通过传入 `to` 属性指定链接. -->
      <!-- <router-link> 默认会被渲染成一个 `<a>` 标签 -->
      <router-link to="/foo">Go to Foo</router-link>
      <router-link to="/bar">Go to Bar</router-link>
      <!-- 路由出口 -->
      <!-- 路由匹配到的组件将渲染在这里 -->
      <router-view></router-view>
    </p>
  </div>
</template>
```
