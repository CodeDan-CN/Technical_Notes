# React语法

### 快速启动React

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>

    <!-- 第一步：定义容器 -->
    <div id = "test"></div>


     <!-- 有顺序的引入 -->
     <!-- react基础库 -->
    <script src="https://unpkg.com/react@16/umd/react.development.js" crossorigin></script>
    <!-- react基础库扩展dom库 -->
    <script src="https://unpkg.com/react-dom@16/umd/react-dom.development.js" crossorigin></script>
    <!-- jsx->js -->
    <script src="https://unpkg.com/babel-standalone@6/babel.min.js"></script>

    <!-- jsx -->
    <script type="text/babel">
        // 第二步：申明虚拟dom
        const vdom = (<h1 id = "title"><span>Hello world</span></h1>);
        // 第三步：让ReactDOM渲染DOM
        ReactDOM.render(vdom,document.getElementById('test'));
    </script>
</body>
</html>
```



### jsx语法

1. 定义虚拟DOM，不要使用引号
2. 如果要在虚拟DOM中使用js表达式，那么{对象.toLowerCase()}
3. 如果要在虚拟DOM中使用class样式，请把class换成className
4. 如果要在虚拟DOM中使用style定义样式， 不能使用style="key:value, key:value"，而是style={{ key:value, key:value }}
5. 如果在虚拟DOM中写小写字母开头标签，那么渲染为html中同名元素，若元素不存在，则会在console中报错。反之大写字母开头标签，则为组件标签，如果没有定义，直接页面空白，并进行报错。
6. 在虚拟DOM中，{}内部只能存放js表达式,而不是js代码，js表达式包括a、a+b、demo(1)、true|false判断等等。

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>jsx表达式练习</title>
</head>
<body>

    <div id = "test" ></div>


    <!-- 有顺序的引入 -->
    <!-- react基础库 -->
    <script src="https://unpkg.com/react@16/umd/react.development.js" crossorigin></script>
    <!-- react基础库扩展dom库 -->
    <script src="https://unpkg.com/react-dom@16/umd/react-dom.development.js" crossorigin></script>
    <!-- jsx->js -->
    <script src="https://unpkg.com/babel-standalone@6/babel.min.js"></script>

    <script type="text/babel" >
        // 定义模拟数据
        let data  = ["zld","wcc"];
        // 定义虚拟DOM
        const vdom = (
            <div>
                <h1>Hello world</h1>
                <ul>
                    {
                        data.map((item,index)=>{
                            return <li key = {index}> {item} </li>
                        })
                    }
                </ul>

            </div>
        );
        // 使用react dom进行虚拟DOM渲染
        ReactDOM.render(vdom,document.getElementById('test'));
    </script>
</body>
</html>
```



### 函数式组件

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>函数式组件</title>
</head>
<body>
    
    <div id = "test"></div>

    <!-- 有顺序的引入 -->
    <!-- react基础库 -->
    <script src="https://unpkg.com/react@16/umd/react.development.js" crossorigin></script>
    <!-- react基础库扩展dom库 -->
    <script src="https://unpkg.com/react-dom@16/umd/react-dom.development.js" crossorigin></script>
    <!-- jsx->js -->
    <script src="https://unpkg.com/babel-standalone@6/babel.min.js"></script>


    <script type="text/babel" >
        
        // 定义函数式虚拟DOM
        function MyCommpont(){
            return <h1>Hello world</h1>
        }
        // 渲染函数式虚拟DOM
        ReactDOM.render(<MyCommpont/>,document.getElementById('test'))
        //  ReactDOM.render执行之后，React解析组件标签，找到MyCommpont标签，然后判断其是何种类型的组件，如果是函数组件，那么就会将返回的虚拟DOM渲染成真实DOM，显示在页面上

    </script>
</body>
</html>
```



### 类式组件

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>类组件测试</title>
</head>
<body>
    <div id = "test"></div>

    <!-- 有顺序的引入 -->
    <!-- react基础库 -->
    <script src="https://unpkg.com/react@16/umd/react.development.js" crossorigin></script>
    <!-- react基础库扩展dom库 -->
    <script src="https://unpkg.com/react-dom@16/umd/react-dom.development.js" crossorigin></script>
    <!-- jsx->js -->
    <script src="https://unpkg.com/babel-standalone@6/babel.min.js"></script>

    <script type="text/babel" >
        // 定义类组件,继承React的Component类
        class MyComponent extends React.Component{
            // 编写render方法
            render(){
                return (
                    <h1>Hello World</h1>
                )
            }
        }
        ReactDOM.render(<MyComponent/>, document.getElementById('test'))
        // ReactDOM.render执行之后，React解析组件标签，找到MyCommpont标签，然后判断其是何种类型的组件，如果是类组件，那么就会new出这个类，然后将调用此类的原型中render方法返回的虚拟DOM渲染成真实DOM，显示在页面上
    </script>
</body>
</html>
```



### 类式组件的三大关键属性

##### state属性

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>state动态改变的代码逻辑写法</title>
</head>
<body>
    <div id = "test"></div>

    <!-- 有顺序的引入 -->
    <!-- react基础库 -->
    <script src="https://unpkg.com/react@16/umd/react.development.js" crossorigin></script>
    <!-- react基础库扩展dom库 -->
    <script src="https://unpkg.com/react-dom@16/umd/react-dom.development.js" crossorigin></script>
    <!-- jsx->js -->
    <script src="https://unpkg.com/babel-standalone@6/babel.min.js"></script>

    <script type="text/babel">
        // 定义一个类DOM(逻辑写法)
        class MyDom extends React.Component{
            // JS构造写法
            constructor(props){
                super(props);
                // 初始化类组件的父类Component的字段state
                this.state = {isHot:false};
                // 将this绑定到方法中（React独有）
                this.change = this.change.bind(this);
            }
            // JS类的实例方法
            change(){
                let flag = this.state.isHot;
                // 通过调用父类Component的setState方法去系统性的改变一个类组件的state
                this.setState({isHot:!flag});
            }
            
            render(){
                let isHot = this.state.isHot;
                // React规定的onclick为onClick方式，而且内部通过{方法名}的方式进行事件绑定
                return <h1 onClick={this.change}> 你好，{isHot == true ? '夏天' : '冬天'} </h1>
            }
        }
        ReactDOM.render(<MyDom/>,document.getElementById('test'));
    </script>
</body>
</html>
```

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>state动态改变的代码简化写法</title>
</head>
<body>
    <div id = "test"></div>

    <!-- 有顺序的引入 -->
    <!-- react基础库 -->
    <script src="https://unpkg.com/react@16/umd/react.development.js" crossorigin></script>
    <!-- react基础库扩展dom库 -->
    <script src="https://unpkg.com/react-dom@16/umd/react-dom.development.js" crossorigin></script>
    <!-- jsx->js -->
    <script src="https://unpkg.com/babel-standalone@6/babel.min.js"></script>

    <script type="text/babel">

        // 定义一个类DOM(逻辑写法)
        class MyDom extends React.Component{

            state = {isHot:false};

            // JS类的实例方法
            change = () => {
                let flag = this.state.isHot;
                // 通过调用父类Component的setState方法去系统性的改变一个类组件的state
                this.setState({isHot:!flag});
            }
            
            render(){
                let isHot = this.state.isHot;
                // React规定的onclick为onClick方式，而且内部通过{方法名}的方式进行事件绑定
                return <h1 onClick={this.change}> 你好，{isHot == true ? '夏天' : '冬天'} </h1>
            }
        }

        ReactDOM.render(<MyDom/>,document.getElementById('test'));

    </script>
</body>
</html>
```



##### props属性

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <div id = "test"></div>
    <div id = "test1"></div>
    <!-- 有顺序的引入 -->
    <!-- react基础库 -->
    <script src="https://unpkg.com/react@16/umd/react.development.js" crossorigin></script>
    <!-- react基础库扩展dom库 -->
    <script src="https://unpkg.com/react-dom@16/umd/react-dom.development.js" crossorigin></script>
    <!-- jsx->js -->
    <script src="https://unpkg.com/babel-standalone@6/babel.min.js"></script>
    <!-- props -->
    <script src="https://unpkg.com/prop-types@15.6.2/prop-types.js"></script>


    <script type="text/babel">
        // 创建一个类DOM
        class Person extends React.Component{

            render(){
                const {name,age,sex} = this.props;
                return (
                    <h1>{name}----{age}----{sex}</h1>
                );
            }

        }
        // 添加参数限制
        Person.propTypes = {
            name : PropTypes.string.isRequired,
            age : PropTypes.number,
            sex : PropTypes.string
        }
        // 添加参数默认限制
        Person.defaultProps = {
            age : 18,
            sex : "男生"
        }


        ReactDOM.render(<Person name = "zld" age = {18} sex = "男生"/>, document.getElementById('test'))
        ReactDOM.render(<Person name = "wcc" age = {20} sex = "女生"/>, document.getElementById('test1'))

    </script>
</body>
</html>
```



```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <div id = "test"></div>
    <div id = "test1"></div>
    <!-- 有顺序的引入 -->
    <!-- react基础库 -->
    <script src="https://unpkg.com/react@16/umd/react.development.js" crossorigin></script>
    <!-- react基础库扩展dom库 -->
    <script src="https://unpkg.com/react-dom@16/umd/react-dom.development.js" crossorigin></script>
    <!-- jsx->js -->
    <script src="https://unpkg.com/babel-standalone@6/babel.min.js"></script>
    <!-- props -->
    <script src="https://unpkg.com/prop-types@15.6.2/prop-types.js"></script>

    <script type="text/babel">
        // 创建一个类DOM
        class Person extends React.Component{
            // 添加参数限制
            static propTypes = {
                name : PropTypes.string.isRequired,
                age : PropTypes.number,
                sex : PropTypes.string
            }
            // 添加参数默认限制
            static defaultProps = {
                age : 18,
                sex : "男生"
            }
            render(){
                const {name,age,sex} = this.props;
                return (
                    <h1>{name}----{age}----{sex}</h1>
                );
            }
        }
        ReactDOM.render(<Person name = "zld" age = {18} sex = "男生"/>, document.getElementById('test'))
        ReactDOM.render(<Person name = "wcc" age = {20} sex = "女生"/>, document.getElementById('test1'))
    </script>
</body>
</html>
```



##### ref属性

 ```html
 <!DOCTYPE html>
 <html lang="en">
 <head>
     <meta charset="UTF-8">
     <meta http-equiv="X-UA-Compatible" content="IE=edge">
     <meta name="viewport" content="width=device-width, initial-scale=1.0">
     <title>ref写法</title>
 </head>
 <body>
     <div id = "test"></div>
     <!-- 有顺序的引入 -->
     <!-- react基础库 -->
     <script src="https://unpkg.com/react@16/umd/react.development.js" crossorigin></script>
     <!-- react基础库扩展dom库 -->
     <script src="https://unpkg.com/react-dom@16/umd/react-dom.development.js" crossorigin></script>
     <!-- jsx->js -->
     <script src="https://unpkg.com/babel-standalone@6/babel.min.js"></script>
     <!-- props -->
     <script src="https://unpkg.com/prop-types@15.6.2/prop-types.js"></script>
 <!-- 写法一
     <script type="text/babel">
         // 创建一个类DOM
         class Demo extends React.Component{
             // 编写一个ref
             show = ()=>{
                 let input1 = this.refs.input1;
                 alert(input1.value);
             }   
             render(){
                 return (
                     <div>
                         <input ref="input1" type="text" />
                         <button onClick={this.show}>点我查看内容</button>
                     </div>
                 )
             }
 
         }
          -->
 
          <script type="text/babel">
             // 创建一个类DOM
             class Demo extends React.Component{
                 input1 = React.createRef();
                 // 编写一个ref
                 show = ()=>{
                     let input1 = this.input1;
                     alert(input1.current.value);
                     console.log(input1);
                 }   
                 render(){
                     return (
                         <div>
                             <input ref={this.input1} type="text" />
                             <button onClick={this.show}>点我查看内容</button>
                         </div>
                     )
                 }
     
             }
 
         ReactDOM.render(<Demo />,document.getElementById('test'))
     </script>
 </body>
 </html>
 ```

**注意：ref不要滥用，当发生事件的元素是操作元素时，更推荐使用event参数，比如input框失去焦点时，提示输入等情况**

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ref写法</title>
</head>
<body>
    <div id = "test"></div>
    <!-- 有顺序的引入 -->
    <!-- react基础库 -->
    <script src="https://unpkg.com/react@16/umd/react.development.js" crossorigin></script>
    <!-- react基础库扩展dom库 -->
    <script src="https://unpkg.com/react-dom@16/umd/react-dom.development.js" crossorigin></script>
    <!-- jsx->js -->
    <script src="https://unpkg.com/babel-standalone@6/babel.min.js"></script>
    <!-- props -->
    <script src="https://unpkg.com/prop-types@15.6.2/prop-types.js"></script>
         <script type="text/babel">
            class Demo extends React.Component{
                // 编写一个ref
                show = (event)=>{
                    let input1 = event.target;
                    alert(input1.value);
                    console.log(input1);
                }   
                render(){
                    return (
                        <div>
                            <input onBlur={this.show} type="text" />
                        </div>
                    )
                }
    
            }
        ReactDOM.render(<Demo />,document.getElementById('test'))
    </script>
</body>
</html>
```



### 受控组件与非受控组件（柯里化）

非受控组件：即从React组件中获取到节点后直接在方法中使用节点的

受控组件：即从React组件中获取节点字段之后存储到状态中，再通过状态去使用字段的

柯里化：通过函数调用继续返回函数的方式，实现多次接受参数最后统一处理的函数编码方式

**柯里化＋受控组件**

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>受控组件与函数的柯里化</title>
</head>
<body>
     <div id = "test"></div>
    <!-- 有顺序的引入 -->
    <!-- react基础库 -->
    <script src="https://unpkg.com/react@16/umd/react.development.js" crossorigin></script>
    <!-- react基础库扩展dom库 -->
    <script src="https://unpkg.com/react-dom@16/umd/react-dom.development.js" crossorigin></script>
    <!-- jsx->js -->
    <script src="https://unpkg.com/babel-standalone@6/babel.min.js"></script>
    <!-- props -->
    <script src="https://unpkg.com/prop-types@15.6.2/prop-types.js"></script>

    <script type="text/babel">
        class Demo extends React.Component{
            state = {username:'',password:''};
            saveData = ()=>{
                // 阻止表单自动提交
                event.preventDefault()
                // 受控组件
                const {username,password} = this.state;
                alert(username +"----"+ password)
            }
            //函数的柯里化，注意[],如果不参与写入state。又不需要[],不懂
            changeData = (dataType)=>{
                return (event)=>{
                    this.setState({ [dataType]:event.target.value })
                }
            }
            render(){
                return (
                    <div>
                        <form onSubmit={this.saveData}>
                            <input onChange={this.changeData('username')} type="text" name="username"/>
                            <input onChange={this.changeData('password')} type="password" name="password"/>
                            <button>保存</button>
                        </form>
                    </div>
                )
            }
        }
        ReactDOM.render(<Demo/>,document.getElementById('test'))
    </script>
</body>
</html>
```



### 旧React生命周期

第一条生命周期：手动挂载：ReactDOM.render()

类组件构造器：constructor(){}

预备挂载状态：componentWillMount(){}

初始化渲染和状态更新：render(){}

挂载完毕：componentDidMount(){}



第二条生命周期线： setState()

组件是否应该被更新（true/false）：shouldComponentUpdate(){}

组件即将更新：componentWillUpdate(){}

组件状态更新：render(){}

组件状态完毕：componentMidUpdate(){}



第三条生命周期线(强制更新)：forceUpdate() 

组件即将更新：componentWillUpdate(){}

组件状态更新：render(){}

组件状态完毕：componentMidUpdate(){}



第四条生命周期线（父组件包含后子组  件的生命周期）：

组件即将接受参数（第一次不执行，而且可以放props参数）：componentWillReceiveProps(){}

组件是否应该被更新（true/false）：shouldComponentUpdate(){}

组件即将更新：componentWillUpdate(){}

组件状态更新：render(){}

组件状态完毕：componentMidUpdate(){}



手动卸载：ReactDOM.unmountComponentAtNode(document.getElementById('test'))

预备卸载状态：ComponentWillUnmount(){}

 ```html
 <!DOCTYPE html>
 <html lang="en">
 
 <head>
     <meta charset="UTF-8">
     <meta http-equiv="X-UA-Compatible" content="IE=edge">
     <meta name="viewport" content="width=device-width, initial-scale=1.0">
     <title>old React 生命周期</title>
 </head>
 
 <body>
 
     <div id="test"></div>
     <!-- 有顺序的引入 -->
     <!-- react基础库 -->
     <script src="https://unpkg.com/react@16/umd/react.development.js" crossorigin></script>
     <!-- react基础库扩展dom库 -->
     <script src="https://unpkg.com/react-dom@16/umd/react-dom.development.js" crossorigin></script>
     <!-- jsx->js -->
     <script src="https://unpkg.com/babel-standalone@6/babel.min.js"></script>
     <!-- props -->
     <script src="https://unpkg.com/prop-types@15.6.2/prop-types.js"></script>
 
     <script type="text/babel">
 
         // class OldReact extends React.Component{
         //     // 第一条生命周期顺序
         //     constructor(props){
         //         super(props)
         //         console.log("constructor")
         //     }
         //     componentWillMount(){
         //         console.log("componentWillMount")
         //     }
         //     render(){
         //         console.log("render")
         //         return (
         //             <div>
         //                 <h1>22222</h1>
         //             </div>
         //         )
         //     }
         //     // 渲染
         //     componentDidMount(){
         //         console.log("componentMidMount")
         //     }
         // }
         // ReactDOM.render(<OldReact />, document.getElementById('test'))
 
 
         // class OldReact extends React.Component{
         //     // 第二条生命周期顺序
         //     shouldComponentUpdate(){
         //         console.log("shouldComponentUpdate")
         //         return true;
         //     }
         //     componentWillUpdate(){
         //         console.log("componentWillUpdate")
         //     }
         //     static = {
         //         count:0
         //     }
         //     add = () => {
         //         let count = this.static.count;
         //         this.setState({count:count+1})
         //     }
         //     render(){
         //         console.log("render")
         //         const {count} = this.static;
         //         return (
         //             <div>
         //                 <h1>{count}</h1>
         //                 <button onClick={this.add}>+1</button>
         //             </div>
         //         )
         //     }
         //     // 渲染
         //     componentDidUpdate(){
         //         console.log("componentDidUpdate")
         //     }
         // }
 
         // class OldReact extends React.Component{
         //     // 第三条生命周期顺序
         //     componentWillUpdate(){
         //         console.log("componentWillUpdate")
         //     }
         //     static = {
         //         count:0
         //     }
         //     add = () => {
         //         let count = this.static.count;
         //         this.forceUpdate()
         //     }
         //     render(){
         //         console.log("render")
         //         const {count} = this.static;
         //         return (
         //             <div>
         //                 <h1>{count}</h1>
         //                 <button onClick={this.add}>+1</button>
         //             </div>
         //         )
         //     }
         //     // 渲染
         //     componentDidUpdate(){
         //         console.log("componentDidUpdate")
         //     }
         // }
 
         class A extends React.Component {
             // 第四条生命周期顺序
             state = {
                car : '英仕派' 
             }
             change = () =>{
                 this.setState({car:'雅阁'})
             }
             render() {
                 const {car} = this.state;
                 return (
                     <div>
                         <div>A</div>
                         <B name={car} />
                         <button onClick={this.change}>换车</button>
                     </div>
                 )
             }
         }
 
         class B extends React.Component{
 
             componentWillReceiveProps(props){
                 console.log("componentWillReceiveProps:"+props.name)
             }
 
             componentWillUpdate(){
                 console.log("componentWillUpdate")
             }
 
             render(){
                 console.log("render")
                 return (
                     <div>B</div>
                 )
             }
             // 渲染
             componentDidUpdate(){
                 console.log("componentDidUpdate")
             }
         }
         ReactDOM.render(<A />, document.getElementById('test'))
     </script>
 </body>
 </html>
 ```









### 新React生命周期

 三个即将操作被标志为不安全的，如果要使用得加上前缀 `UNSAFE_`,三个即将分别是：

+ componentWillMount
+ componentWillUpdate
+ componentWillReceiveProps

推荐：

 第一条生命周期：手动挂载：ReactDOM.render()

类组件构造器：constructor(){}

预备挂载状态（接受pros，如果return出去那么将放在状态中，还可以传入state）：getDerivedStateFromProps(props){return pros} 

初始化渲染和状态更新：render(){}

挂载完毕：componentDidMount(){}



第二条生命周期线： setState()

预备挂载状态：getDerivedStateFromProps(){}

组件是否应该被更新（true/false）：shouldComponentUpdate(){}

组件状态更新：render(){}

组件渲染之前，保留数据,返回数据传递到componentMidUpdate：getSnaphotBeforeUpdate(prevprops，prevstate){return data}

组件状态完毕：componentMidUpdate(prevprops，prevstate,snaphotValue){}



第三条生命周期线(强制更新)：forceUpdate() 

预备挂载状态：getDerivedStateFromProps(){}

组件状态更新：render(){}

getSnaphotBeforeUpdate(){}

组件状态完毕：componentMidUpdate(){}



第四条生命周期线（父组件包含后子组  件的生命周期）：

预备挂载状态：getDerivedStateFromProps(){}

组件是否应该被更新（true/false）：shouldComponentUpdate(){}

组件状态更新：render(){}

getSnaphotBeforeUpdate(){}

组件状态完毕：componentMidUpdate(){}



手动卸载：ReactDOM.unmountComponentAtNode(document.getElementById('test'))

预备卸载状态：ComponentWillUnmount(){}



### 兄弟传参—发布订阅

```tex
> npm install pubsub-js
```

##### 订阅使用方式

```js
var mySubscriber = function(msg,data){
    console.log(msg,data);
}
// 订阅了my_topic，只要有人往这个通道发送消息，这边就是执行mySubscriber方法回调
var token = PubSub.subscribe('my_topic',mySubscriber)
// 这个token可以用来停止订阅
```

##### 发布使用方式

分布分为异步发布与同步发布

```js
// 异步发布
PubSub.publist('my_topic','hello world')

// 同步发布
PubSub.publishSync('my_topic','hello world')
```





