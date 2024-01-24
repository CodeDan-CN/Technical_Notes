# elasticSearch快速启动(Java)

> 本文使用elasticsearch-rest-high-level-client-7.6.2.jar作为操作ES的第三方客户端

### 前置工作(环境列表)

+ Spring boot 2.3.8.RELEASE

```xml
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <type>pom</type>
                <version>2.3.8.RELEASE</version>
                <scope>import</scope>
                <exclusions>
                    <exclusion>
                        <groupId>commons-codec</groupId>
                        <artifactId>commons-codec</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.apache.logging.log4j</groupId>
                    <artifactId>log4j-to-slf4j</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
     <dependencies>
```

+ spring-boot-starter-data-elasticsearch 7.6.2

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
            <version>7.6.2</version>
        </dependency>
```



### 快速启动流程

##### 配置信息准备

使用`elasticsearch-rest-high-level-client`进行操作客户端时，最简单的配置仅仅需要进行ES服务器所在`IP`、`HOST`、`访问方式`的配置。这里使用yml文件进行自定义配置信息之后在配置类中进行读取的方式。

```properties
elasticsearch:
  hostName: 123.60.151.31
  port: 9200
  scheme: http
```

```java
@Configuration
public class ElasticSearchClientConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${elasticsearch.hostName}")
    private String hostName;
    @Value("${elasticsearch.port}")
    private int port;
    @Value("${elasticsearch.scheme}")
    private String scheme;

    /**
     * 获取ES客户端
     * @return esClient
     */
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        logger.info("es client start.");
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(hostName, port, scheme)));
    }
}
```

通过配置文件可以将配置之后`RestHighLevelClient`对象交给`Spring`管理。以便后续使用。



##### 构建基础工具类构造(开启以及创建索引)

```java
@Component
@Slf4j
public class EsOperationUtil {
    // 这里需要在配置文件yml中新增一个GatewayAccessIndexName字段作为索引名称(固定名称后按需求以及月份进行分片即可)
    @Value("${elasticsearch.GatewayAccessIndexName}")
    private String indexName;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    public void close(){
        try {
            restHighLevelClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建指定索引
     * @param indexName
     */
    public void createIndexRequest(String indexName){
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        try {
            CreateIndexResponse createIndexResponse = 
                    restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            // 获取响应状态
            boolean acknowledged = createIndexResponse.isAcknowledged();
            log.info("运行结果;{}",acknowledged);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```



##### 测试类构建(各种操作以及检索代码)

```java
@SpringBootTest
public class EsApplication {

    @Test
    public void test() throws IOException {

        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("123.60.151.31", 9200, "http"))
        );
        matchQueryParams(restHighLevelClient);
        restHighLevelClient.close();
    }

    /**
     * 添加索引
     * @param restHighLevelClient
     */
    private void add(RestHighLevelClient restHighLevelClient){
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("gateway_access_index");
        createIndexRequest.settings(Settings.builder()
                .put("index.number_of_shards",4)
                .put("index.number_of_replicas",1)
        );
// 创建索引时指定映射Mapping（其实第三方客户端可以根据java实体类的字段类型自动进行映射）
// createIndexRequest.source("{\n" +
//                "    \"settings\" : {\n" +
//                "        \"number_of_shards\" : 1,\n" +
//                "        \"number_of_replicas\" : 0\n" +
//                "    },\n" +
//                "    \"mappings\" : {\n" +
//                "        \"properties\" : {\n" +
//                "            \"message\" : { \"type\" : \"text\" }\n" +
//                "        }\n" +
//                "    },\n" +
//                "    \"aliases\" : {\n" +
//                "        \"twitter_alias\" : {}\n" +
//                "    }\n" +
//                "}", XContentType.JSON);
        try {
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            // 获取响应状态
            boolean acknowledged = createIndexResponse.isAcknowledged();
            System.out.println("运行结果;{}"+acknowledged);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询索引配置信息
     * @param restHighLevelClient
     */
    private void get(RestHighLevelClient restHighLevelClient){
        GetIndexRequest getIndexRequest = new GetIndexRequest("gateway_access_index");
        try {
            GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(getIndexRequest, RequestOptions.DEFAULT);
            // 获取响应状态
            System.out.println("运行结果getAliases"+getIndexResponse.getAliases());
            System.out.println("运行结果getMappings"+getIndexResponse.getMappings());
            System.out.println("运行结果getSettings"+getIndexResponse.getSettings());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除索引
     * @param restHighLevelClient
     */
    private void delete(RestHighLevelClient restHighLevelClient){
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("gateway_access_index");
        try {
            AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            // 获取响应状态
            boolean acknowledged = delete.isAcknowledged();
            System.out.println("删除是否成功:"+acknowledged);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 新增文档数据
     * @param restHighLevelClient
     */
    public void insert(RestHighLevelClient restHighLevelClient){
        IndexRequest indexRequest = new IndexRequest();
        // 设置新增内容的索引以及指定文档的id
        indexRequest.index("gateway_access_index").id("1001");
        User user = new User();
        user.setName("CodeDan");
        user.setSex("男生");
        user.setAge(30);
        String userJson = JSONUtil.toJsonStr(user);
        indexRequest.source(userJson, XContentType.JSON);
        try {
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            DocWriteResponse.Result result = indexResponse.getResult();
            System.out.println(result); //成功是CREATED

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改文档数据
     * @param restHighLevelClient
     */
    public void update(RestHighLevelClient restHighLevelClient){
        UpdateRequest updateRequest = new UpdateRequest();
        // 设置新增内容的索引以及指定文档的id
        updateRequest.index("gateway_access_index").id("1001");
        User user = new User();
        user.setName("CodeDan01");
        user.setSex("男生");
        user.setAge(29);
        String userJson = JSONUtil.toJsonStr(user);
        updateRequest.doc(userJson,XContentType.JSON);
        try {
            UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            DocWriteResponse.Result result = update.getResult();
            System.out.println(result); //成功是CREATED
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询文档数据
     * @param restHighLevelClient
     */
    public void select(RestHighLevelClient restHighLevelClient){
        GetRequest getRequest = new GetRequest();
        // 设置新增内容的索引以及指定文档的id
        getRequest.index("gateway_access_index").id("1001");
        try {
            GetResponse documentFields = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            Map<String, Object> source = documentFields.getSource();
            System.out.println(source);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除文档数据
     * @param restHighLevelClient
     */
    public void deleteDoc(RestHighLevelClient restHighLevelClient){
        DeleteRequest deleteRequest = new DeleteRequest();
        // 设置新增内容的索引以及指定文档的id
        deleteRequest.index("gateway_access_index").id("1001");
        try {
            DeleteResponse delete = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            String s = delete.toString();
            System.out.println(s);
            System.out.println("文档删除情况"+delete.getResult()); //DELETED
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量添加文档数据
     * @param restHighLevelClient
     **/
    public void maxAddDoc(RestHighLevelClient restHighLevelClient){
        BulkRequest bulkRequest = new BulkRequest();
        User user = new User();
        user.setName("CodeDan01");
        user.setSex("男生");
        user.setAge(29);
        IndexRequest add1 = new IndexRequest()
                .index("gateway_access_index").id("1001")
                .source(JSONUtil.toJsonStr(user), XContentType.JSON);
        User user1 = new User();
        user1.setName("CodeDan02");
        user1.setSex("女生");
        user1.setAge(18);
        IndexRequest add2 = new IndexRequest()
                .index("gateway_access_index").id("1002")
                .source(JSONUtil.toJsonStr(user1), XContentType.JSON);
        User user2 = new User();
        user2.setName("CodeDan03");
        user2.setSex("女生11111");
        user2.setAge(183);
        IndexRequest add3 = new IndexRequest()
                .index("gateway_access_index").id("1003")
                .source(JSONUtil.toJsonStr(user2), XContentType.JSON);
        User user3 = new User();
        user3.setName("CodeDan04");
        user3.setSex("女生22222");
        user3.setAge(185534);
        IndexRequest add4 = new IndexRequest()
                .index("gateway_access_index").id("1004")
                .source(JSONUtil.toJsonStr(user3), XContentType.JSON);
        bulkRequest.add(add1);
        bulkRequest.add(add2);
        bulkRequest.add(add3);
        bulkRequest.add(add4);

        try {
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            long ingestTookInMillis = bulk.getIngestTookInMillis();
            System.out.println("操作总计时间戳:"+ingestTookInMillis);
            TimeValue took = bulk.getTook();
            System.out.println("操作总计时间:"+took); //16ms
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量删除档数据
     * @param restHighLevelClient
     **/
    public void maxDeleteDoc(RestHighLevelClient restHighLevelClient){
        BulkRequest bulkRequest = new BulkRequest();
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.index("gateway_access_index").id("1001");
        DeleteRequest deleteRequest1 = new DeleteRequest();
        deleteRequest1.index("gateway_access_index").id("1002");
        DeleteRequest deleteRequest2 = new DeleteRequest();
        deleteRequest2.index("gateway_access_index").id("1003");
        DeleteRequest deleteRequest3 = new DeleteRequest();
        deleteRequest3.index("gateway_access_index").id("1004");
        bulkRequest.add(deleteRequest);
        bulkRequest.add(deleteRequest1);
        bulkRequest.add(deleteRequest2);
        bulkRequest.add(deleteRequest3);
        try {
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            long ingestTookInMillis = bulk.getIngestTookInMillis();
            System.out.println("操作总计时间戳:"+ingestTookInMillis);
            TimeValue took = bulk.getTook();
            System.out.println("操作总计时间:"+took); //16ms
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询当前索引下全部文档（全量查询）
     * @param restHighLevelClient
     */
    public void matchAll(RestHighLevelClient restHighLevelClient) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("gateway_access_index");
        SearchRequest source = searchRequest.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));
        try {
            SearchResponse search = restHighLevelClient.search(source, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            System.out.println("总共有"+hits.getTotalHits().toString()+"条数据");
            System.out.println("花费时间:"+search.getTook());
            for(SearchHit hit : hits){
                System.out.println(hit.getSourceAsString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询当前索引下全部文档（全量查询分页）
     * @param restHighLevelClient
     */
    public void matchAllPage(RestHighLevelClient restHighLevelClient) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("gateway_access_index");
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        builder.from(0); //这个不是mybatisplus那种，这个是要计算的原始的，from的值是根据想要页数计算，(当前页码 - 1) * size;
        builder.size(2);
        searchRequest.source(builder);
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            System.out.println("总共有"+hits.getTotalHits().toString()+"条数据");
            System.out.println("花费时间:"+search.getTook());
            for(SearchHit hit : hits){
                System.out.println(hit.getSourceAsString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询当前索引下全部文档（全量查询排序）
     * @param restHighLevelClient
     */
    public void matchAllSort(RestHighLevelClient restHighLevelClient) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("gateway_access_index");
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        builder.sort("age", SortOrder.DESC);
        // 按照顺序多写一个就是多个条件的排序
//        builder.sort("age", SortOrder.DESC);
        searchRequest.source(builder);
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            System.out.println("总共有"+hits.getTotalHits().toString()+"条数据");
            System.out.println("花费时间:"+search.getTook());
            for(SearchHit hit : hits){
                System.out.println(hit.getSourceAsString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询当前索引下全部文档（全量查询过滤字段）
     * @param restHighLevelClient
     */
    public void matchAllFilter(RestHighLevelClient restHighLevelClient) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("gateway_access_index");
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        // 包含的
        String[] excludes = {};
        // 拦截的
        String[] includes = {"name","age"};
        builder.fetchSource(excludes, includes);
        searchRequest.source(builder);
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            System.out.println("总共有"+hits.getTotalHits().toString()+"条数据");
            System.out.println("花费时间:"+search.getTook());
            for(SearchHit hit : hits){
                System.out.println(hit.getSourceAsString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 查询当前索引下单个条件查询文档(不分词)
     * @param restHighLevelClient
     */
    public void termQueryOneParam(RestHighLevelClient restHighLevelClient) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("gateway_access_index");
        // 这个term不会分词，MatchQuery指定字段会分词
        SearchRequest source = searchRequest.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("name","CodeDan")));
        try {
            SearchResponse search = restHighLevelClient.search(source, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            System.out.println("总共有"+hits.getTotalHits().toString()+"条数据");
            System.out.println("花费时间:"+search.getTook());
            for(SearchHit hit : hits){
                System.out.println(hit.getSourceAsString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询当前索引下多个条件查询文档 (分词)
     * @param restHighLevelClient
     */
    public void matchQueryParams(RestHighLevelClient restHighLevelClient) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("gateway_access_index");
        // 这个term不会分词，MatchQuery指定字段会分词
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("sex","男生"));
        boolQueryBuilder.must(QueryBuilders.matchQuery("age",18));
        // or
//        boolQueryBuilder.should(QueryBuilders.matchQuery("sex","男生"));
//        boolQueryBuilder.should(QueryBuilders.matchQuery("age",18));
        builder.query(boolQueryBuilder);
        searchRequest.source(builder);
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            System.out.println("总共有"+hits.getTotalHits().toString()+"条数据");
            System.out.println("花费时间:"+search.getTook());
            for(SearchHit hit : hits){
                System.out.println(hit.getSourceAsString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 范围查询
     * @param restHighLevelClient
     */
    public void rangeSelect(RestHighLevelClient restHighLevelClient) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("gateway_access_index");
        // 这个term不会分词，MatchQuery指定字段会分词
        SearchSourceBuilder builder = new SearchSourceBuilder();
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("age");
        rangeQuery.gte(30); //大于30
        rangeQuery.lte(30000); //小于30000
        builder.query(rangeQuery);
        searchRequest.source(builder);
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            System.out.println("总共有"+hits.getTotalHits().toString()+"条数据");
            System.out.println("花费时间:"+search.getTook());
            for(SearchHit hit : hits){
                System.out.println(hit.getSourceAsString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 模糊查询(和分词不一样啊)
     * @param restHighLevelClient
     */
    public void fuzzySelect(RestHighLevelClient restHighLevelClient) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("gateway_access_index");
        // 这个term不会分词，MatchQuery指定字段会分词
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 多出一个字符可以匹配
        FuzzyQueryBuilder fuzzyQuery = QueryBuilders.fuzzyQuery("name","Code").fuzziness(Fuzziness.ONE);
        builder.query(fuzzyQuery);
        searchRequest.source(builder);
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            System.out.println("总共有"+hits.getTotalHits().toString()+"条数据");
            System.out.println("花费时间:"+search.getTook());
            for(SearchHit hit : hits){
                System.out.println(hit.getSourceAsString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 模糊查询
     * @param restHighLevelClient
     */
    public void aggregationSelect(RestHighLevelClient restHighLevelClient) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("gateway_access_index");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        AggregationBuilder aggregationBuilder = AggregationBuilders.max("maxAge").field("age"); //有max，avg等函数
        builder.aggregation(aggregationBuilder);
        searchRequest.source(builder);
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            System.out.println("总共有"+hits.getTotalHits().toString()+"条数据");
            System.out.println("花费时间:"+search.getTook());
            for(SearchHit hit : hits){
                System.out.println(hit.getSourceAsString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 分组查询
     * @param restHighLevelClient
     */
    public void groupSelect(RestHighLevelClient restHighLevelClient) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("gateway_access_index");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("ageGroup").field("age"); //有max，avg等函数
        builder.aggregation(aggregationBuilder);
        searchRequest.source(builder);
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            System.out.println("总共有"+hits.getTotalHits().toString()+"条数据");
            System.out.println("花费时间:"+search.getTook());
            for(SearchHit hit : hits){
                System.out.println(hit.getSourceAsString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
```

