### Swagger的使用

> 现在开发，很多采用前后端的模式，前端只负责调用接口，进行渲染，前端和后端的唯一联系，变成了API接口。因此，API文档变得越来越重要。swagger是一个方便我们更好的编写API文档的框架，而且swagger可以模拟http请求调用。

**第一步：导入官方依赖**

```xml
        <!--    Swagger中API所在包    -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.7.0</version>
        </dependency>

        <!--    Swagger中UI界面所在包    -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.7.0</version>
        </dependency>
```

&nbsp;

**第二步：编写Swagger配置文件**

```java
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //监听的包路径
                .apis(RequestHandlerSelectors.basePackage("cn.dcone.swaggerdemo"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("服务:发布为daocke镜像,权限管理，用户管理，页面管理，日志 后台 APIs")
                .description("服务:发布为daocke镜像,权限管理，用户管理，页面管理，日志 后台")
                .termsOfServiceUrl("https://www.baidu.com")   //这里是ui界面提供的跳转url
                .contact("CodeDan")
                .version("1.0")
                .build();
    }

}

```

&nbsp;

**第三步：swagger访问地址**

```url
http://localhost:8080/swagger-ui.html
```


