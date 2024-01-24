### Vue路由参数传递

> 路由组件传递参数的方式主要有两种，分为query方式和params方式，其中params方式可以使用props配置进行更加合理的传输传递。

##### Query方式参数传递

**传递方**：代码如下所示

```html
      <router-link :to="{
        path:'/foo/new',
        query:{
          fooId:1,
          fooName:'codedan',
          fooAge:19
        }
      }">toNew</router-link>
```

**接收方**：代码如下所示

```js
  data(){
    return {
      show:{
        'fooId':this.$route.query.fooId,
        'fooName':this.$route.query.fooName,
        'fooAge':this.$route.query.fooAge
      }
    }
  },
```

​	

##### Params方式参数传递

**前置配置**：

```js
    {   path: '/foo',
        component: fooPage,
        children:[
            {
                path: 'boot',
              	//新增配置name
                name: 'fooNew',
                component: fooBootPage,
              	//新增配置props
                props:true
            }
          ]
    }
```

​	

**传递方**：

```html
      <router-link :to="{
        //此方式必须要使用name，而不是path
        name:'fooNew',
        params:{
          fooId:1,
          fooName:'zld',
          fooAge:23
        }
      }">toBoot</router-link>
```

​	

**接受方**：

```js
export default {
  name: "FooBootPage",
  props:{
    fooId:{
      type:Number,
      required:true
    },
    fooName:{
      type:String,
      required: true
    },
    fooAge: {
      type:Number,
      required:true
    }
  },
  data(){
    return {
      show:{
        'fooId':this.fooId,
        'fooName':this.fooName,
        'fooAge':this.fooAge
      }
    }
  }
}
```

