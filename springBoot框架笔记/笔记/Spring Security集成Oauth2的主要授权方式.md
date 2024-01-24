# Spring Security集成Oauth2的主要授权方式

### 授权码模式（Authorization Code Model）

![authrotization](..\图库\authrotization.png)



上图就是Spring Security对Oauth2.0授权码模式的具体实现流程，此图将Oauth2授权码模式的大致流程图进行了细化，贴合Security实现思路。

根据上图我们可以将Security实现分为三个服务的构造，即`授权服务`、`被保护的资源服务`、`第三方服务`。

##### 授权服务构造

强烈建议先把项目clone下来，对照着看！！！！！！！！[github链接](https://github.com/CodeDan-CN/security/tree/authorizationCode)

**此链接下`authorization-server-database`子模块为授权模式数据库版本！**

首先授权服务器的构造，即基于Spring Security打造一个授权服务，当然这个服务除了作为获取token的服务之外，也可以担任一个系统诸多微服务的统一身份验证服务。

这个授权服务**本身必须拥有获取当前被保护资源服务用户系统的方式，即管理用户信息的能力**。

***第一步***：结合Spring Security搭建授权服务器用户的注册以及登录功能（为了演示更加专注于授权服务，这里并没有采用RBAC权限模型，而是简单的一个用户表）

构建用户信息表SQL以及样式：

```sql
CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT '用户账号以及名称',
  `password` varchar(255) DEFAULT '密码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
```



构建数据持久层(Mybatis-plus,建议用mybatis-x直接生成)：

```java
@TableName(value ="user_info")
@Data
public class UserInfo implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "username")
    private String username;

    /**
     * 
     */
    @TableField(value = "password")
    private String password;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
```

```java
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}

public interface UserInfoService extends IService<UserInfo> {

}

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

}
```



构建Spring Security的UserDetailServer：

```java
@Service
public class UserDetailFacade implements UserDetailsService {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserInfo one = userInfoService.getOne(new QueryWrapper<UserInfo>().lambda().eq(UserInfo::getUsername, s));
        if(StringUtils.isEmpty(one)){
            throw new UsernameNotFoundException("登录用户"+s+"没有注册啊！！！！！");
        }
        // 不想创建新对象继承UserDetails，这里用Security的User(org.springframework.security.core.userdetails.User)对象构造去造一个
        // 继承UserDetails的写法在JWT模式中有哦
        // 根据用户信息获取角色，再通过角色去获取对象集合,这里就不去搞太麻烦了,不过我源代码里相关数据库表的对象已经有了，可以自己补充
        return new User(one.getUsername(),one.getPassword(), Arrays.asList(new SimpleGrantedAuthority("ROOT")));
    }

    /**
     * 添加一个用户
     * @param userInfoReqDTO
     */
    public void add(UserInfoReqDTO userInfoReqDTO) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoReqDTO,userInfo);
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfoService.save(userInfo);
    }
}
```



构建Spring Security自定义用户登录流程配置信息：

```JAVA
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailFacade userDetailFacade;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {    //auth.inMemoryAuthentication()
        auth.userDetailsService(userDetailFacade)
                .passwordEncoder(passwordEncoder());
        auth.parentAuthenticationManager(authenticationManagerBean());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http   // 配置登录页并允许访问
                .formLogin().permitAll()
                // 配置Basic登录
                //.and().httpBasic()
                .and().authorizeRequests().antMatchers( "/login/**", "/authorization/**","/logout/**").permitAll()
                // 其余所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                // 关闭跨域保护;
                .and().csrf().disable();
    }
}
```



***第二步***：构建授权服务器核心"Security授权配置"

构建授权服务器的授权配置之前，需要先知道一些基础词汇以及字段的意思（这些字段来源自Spring Security官方github发布的数据库字段），我把这些词汇进行分类之后统一进行介绍：

+ client客户端信息（clientDetails）：授权服务上得记录每一个客户端的信息，而这些信息可以被细化为以下字段
  + client_Id：客户端的唯一标识
  + client_secret：客户端的加密密码
  + resource_ids：客户端所能访问的资源id，现在很多资源都是微服务级别的，即当前客户端可以访问的微服务范围。
  + scope：客户端申请的权限范围，可选值分别为`READ:后缀`、`WRITE:后缀`、`admin:后缀`、`ALL`,若同时拥有多个权限使用,分隔即可。这个校验并不是security负责的，而是被其放到了token中，由资源服务器去解析后进行权力范围控制的，比如在controller层接口上使用@PreAuthorize("#oauth2.hasScope('admin:后缀')")进行权限控制。但是**这是客户端的范围，而不是用户权限范围**
  + authorized_grant_types：即当前客户端向授权服务器进行认证的模式，可选分为有authorization_code模式(授权码模式)、password（简单密码模式）、client_credentials（客户端模式）、implicit（隐式模式）除了上述四大模式之外，还存在一个refresh_token模式，即当token获取后向授权服务器刷新token的模式。
  + web_server_redirect_uri：即在授权码模式以及隐式模式模式下，返回code或者token时的重定向地址，这个地址要和客户端注册时一样才能正常使用。
  + authorities：即客户端中用户的权限范围，由于Oauth2中并没有规范scope的使用场景，举个例子来说，用户可以拥有修改其个人信息的权限，但是客户端大可不必。
  + access_token_validity：发放token有效时间
  + refresh_token_validity：刷新token的有效时间
  + additional_information：预留字段，一般是为了解释这个客户端的，一般没人用，要用请存储JSON格式
  + create_time：记录客户端创建时间
  + autoapprove：此客户端在授权服务器中是否可以自动授权，而不是手动，值可以为'true'或者'false'
+ access_token认证授权信息：
  + client_id：即当前token所属客户端的唯一标识
  + token_id：经过MD5加密之后的access_token
  + token：`OAuth2AccessToken.java`对象序列化内容
  + user_name：获取token的用户名称（账号）
  + authentication_id：根据当前的username、client_id与scope通过MD5加密生成该字段的值
  + authentication：`OAuth2Authentication.java`对象序列化内容
  + refresh_token：通过MD5加密`refresh_token`的值
  + create_time：记录创建时间
+ refresh_token重新时间token消息：
  + token_id：通过MD5加密`refresh_token`的值
  + token：`OAuth2RefreshToken.java`对象序列化内容
  + authentication：`OAuth2Authentication.java`对象序列化内容
+ oauth_code授权码消息:
  + authentication：`AuthorizationRequestHolder.java`对象序列化内容
  + code：存储服务端系统生成的`code`的值(未加密)

展示官方提供的SQL数据库建表语句：

```sql
-- used in tests that use HSQL
create table oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);

create table oauth_client_token (
  token_id VARCHAR(256),
  token LONGVARBINARY,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256)
);

create table oauth_access_token (
  token_id VARCHAR(256),
  token LONGVARBINARY,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication LONGVARBINARY,
  refresh_token VARCHAR(256)
);

create table oauth_refresh_token (
  token_id VARCHAR(256),
  token LONGVARBINARY,
  authentication LONGVARBINARY
);

create table oauth_code (
  code VARCHAR(256), authentication LONGVARBINARY
);

create table oauth_approvals (
	userId VARCHAR(256),
	clientId VARCHAR(256),
	scope VARCHAR(256),
	status VARCHAR(10),
	expiresAt TIMESTAMP,
	lastModifiedAt TIMESTAMP
);

-- customized oauth_client_details table
create table ClientDetails (
  appId VARCHAR(256) PRIMARY KEY,
  resourceIds VARCHAR(256),
  appSecret VARCHAR(256),
  scope VARCHAR(256),
  grantTypes VARCHAR(256),
  redirectUrl VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additionalInformation VARCHAR(4096),
  autoApproveScopes VARCHAR(256)
);
```

上面这些基础知识很重要啊，这些表可以无脑建立（都是官方提供的SQL），因为除了注册客户端表我们使用之外，其他都是Spring Security自行操作，我们只需要保证表存在即可。

这些表可以说都对应了一个SpringSecurity提供的一个业务处理类，我们现在要做的就是将数据库表对应的业务处理类替换掉默认的业务处理类。

先起一个头,**继承AuthorizationServerConfigurerAdapter类并使用@EnableAuthorizationServer**表示这是授权服务器的配置类

```java
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {
    /**
    * 这是application.yml中配置好启动时被注入到ioc容器的数据源对象
    */
    @Autowired
    private DataSource dataSource;
}
```

在业务逻辑处理类替换上，比如管理操作客户端消息表的业务操作类对象`JdbcClientDetailsService`作为当前SpringSecurity的实际操作对象，那么就需要在OAuth2Config中添加如下代码：

```java
    @Bean
    public ClientDetailsService clientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }
```

同理，和token相关信息表对应的业务逻辑操作类对象是不是也得安排上

```java
    @Bean
    public TokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService());
        services.setSupportRefreshToken(true);
        services.setTokenStore(jdbcTokenStore());
        return services;
    }
```

token和client都安排上了，授权码code表对应的业务逻辑处理对象也同理：

```java
@Bean
public AuthorizationCodeServices authorizationCodeServices() {
    return new JdbcAuthorizationCodeServices(dataSource);
}
```

来一个Security Oauth2运行的必须配置，这个解释要篇章，现在先理解为将普通Request转化为oauth2形式的Request方便之后处理某些流程。

```java
    /**
     * 替换TokenEndpoint对象中/oauth/token接口中获取TokenRequest的实例为DefaultOAuth2RequestFactory
     * @return
     */
    @Bean
    public OAuth2RequestFactory oAuth2RequestFactory() {
        return new DefaultOAuth2RequestFactory(clientDetailsService());
    }
```

这些基本就可以满足一个正常oauth2授权码模式下的正常使用了，最后直接将这些对象填充入配置项中

````java
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置数据从oauth_client_details表读取来存储
        clients.withClientDetails(clientDetailsService());
    }

    /**
     * 自定义Oauth2协议确认授权方式以及认证请求链接等配置
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(jdbcTokenStore()).authenticationManager(authenticationManager)
                //自定义AccessToken
                //.accessTokenConverter(accessTokenConverter)
                //设置userDetailsService
                .userDetailsService(userDetailFacade)
                //授权码储存
                .authorizationCodeServices(authorizationCodeServices())
                //设置userApprovalHandler
                .userApprovalHandler(userApprovalHandler())
                //设置tokenServices
                .tokenServices(tokenServices())
                //支持获取token方式
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST,HttpMethod.PUT,HttpMethod.DELETE,HttpMethod.OPTIONS)
                //刷新token
                .reuseRefreshTokens(true);
    }

    /**
     * 认证服务器的安全配置
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 开启/oauth/token_key验证端口认证权限访问
                .tokenKeyAccess("isAuthenticated()")
                //  开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("isAuthenticated()")
                //允许表单认证 在授权码模式下会导致无法根据code获取token　
                .allowFormAuthenticationForClients();
    }
````

至此一个完整的授权码模式的数据库版本核心配置就梳理完毕了。

##### 授权模式的使用

本例子中localhost:8080为资源服务器，8081为认证服务器，8082为第三方服务器

其中8080资源服务器中，资源如下

```java
@Configuration
@EnableResourceServer
public class ResourceAdapterConfig extends ResourceServerConfigurerAdapter {

    /**
     * 接受到携带token的请求时，资源服务器向授权服务器发起校验请求
     * @return
     */
    @Bean
    RemoteTokenServices tokenServices() {
        RemoteTokenServices services = new RemoteTokenServices();
        services.setCheckTokenEndpointUrl("http://localhost:8081/oauth/check_token");
        services.setClientId("oa");
        services.setClientSecret("123456");
        return services;
    }

    /**
     * 客户端权限业务处理
     * @param resources
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(tokenServices());
    }

    /**
     * 常规配置
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated();
    }
```

```java
@RestController
@RequestMapping("/info")
public class InfoController {

    @GetMapping("/get")
    @PreAuthorize("hasRole('root')")
    public String get(){
        return "success";
    }
}
```

其中8082第三方服务器中，访问路径如下

```java
@RestController
@RequestMapping("/client")
public class OtherServerController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 第三方客户端免登重要操作请求，即获取授权服务器登录完成重定向过来的code，并使用这个code去授权服务器获取access_token，最后携带access_token去调用资源服务器的资源
     * @param code
     * @return
     */
    @GetMapping("/hello")
    public String hello(String code){
        if(StringUtils.isEmpty(code)){
           return "请去授权：http://localhost:8081/oauth/authorize?client_id=myClient&response_type=code&scope=all&redirect_uri=http://localhost:8082/client/hello";
        }
        //配置一下通过code获取access_token的请求
        LinkedMultiValueMap<Object, Object> requestQuery = new LinkedMultiValueMap<>();
        // 这里的code没有好说的
        requestQuery.add("code",code);
        // 当前客户端在授权服务器中注册的client_id
        requestQuery.add("client_id","myclient");
        // 当前客户端在授权服务器中注册的client_secret
        requestQuery.add("client_secret","123456");
        // 这个数据存在的意义在于校验code，这里的跳转地址要和应用在授权服务器注册时使用的地址一致
        requestQuery.add("redirect_uri", "http://localhost:8082/client/hello");
        // 授权类型
        requestQuery.add("grant_type","authorization_code");
        // 发起请求
        Map<String,String> response = restTemplate.postForObject("http://localhost:8081/oauth/token", requestQuery, Map.class);
        // 提取token
        String access_token = response.get("access_token");
        System.out.println("access_token:"+access_token);
        // 装配一下access_token访问资源服务器的请求
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer "+access_token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/info/get", HttpMethod.GET, httpEntity, String.class);
        return exchange.getBody();
    }
}
```

向ClientDetails表中注册一个client：

```json
{
    "clientId":"myclient",
    "clientSecret":"123456",
    "scope":"all",
    "authorizedGrantTypes":"authorization_code",
    "webServerRedirectUri":"http://localhost:8082/client/clientModel",
    "accessTokenValidity":60,
    "refreshTokenValidity":60,
    "autoapprove":"true"
}
```

现在只需要访问http://localhost:8082/client/hello即可，复制返回的url链接进行访问后，输入账号密码即可，最后应该返回资源的服务器的success

