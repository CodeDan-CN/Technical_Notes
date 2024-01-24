# React UI Component 使用

### 使用前提

第一步：安装antd组件库

```jsx
npm install antd
```

第二步：引用antd和css样式文件

```jsx
import {antdComponentName} from 'antd'
import 'antd/dist/antd.css'  //可以自动按需引入
```

如果需要使用图标的话，还得引入图标库

```jsx
import {iconName} from 'antd/dist/antd.css'
```



### antd按需引入css

第一步：修改启动文件`package.json`中启动配置

```jsx
  "scripts": {
    "start": "react-scripts start",
    "build": "react-scripts build",
    "test": "react-scripts test",
    "eject": "react-scripts eject"
  },
  // 替换为
  "scripts": {
    "start": "react-app-rewired start",
    "build": "react-app-rewired build",
    "test": "react-app-rewired test",
    "eject": "react-app-rewired eject"
  },
```



第二步：在项目根目录创建一个`config-overrides.js`，用于修改默认配置

```jsx
const {override,fixBabelImports} = require('customize-cra')

module.exports = override(
    fixBabelImports('import',{
        libraryName:'antd',
        libraryDirectory: 'es',
        style:'css'
    })
);
```



第三步：安装babel-plugin-import

```tex
npm install babel-plugin-import
```



网址：https://ant.design/