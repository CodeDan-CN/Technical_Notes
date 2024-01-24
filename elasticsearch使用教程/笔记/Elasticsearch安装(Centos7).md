# Elasticsearch快速安装

> Elasticsearch本质是为了更好的进行全文检索而开发的搜索引擎，因为在传统的结构化数据库中哪怕优化索引也无法得到很好的检索性能。
>
> 而在实际使用业务场景中，电商商品检索、日志监控与收集都是使用ES的高频场景。

### 快速启动

环境准备：华为云Centos7版本2核4G内存云服务器

JDK：open jdk 1.8（如果不想本机安装jdk也行，es自带高版本jdk运行）

es安装包：elasticsearch-7.13.2-linux-x86_64.tar.gz

##### Centos7安装es过程

**第一步：上传源码包，并解压**

作者存放tar包地址为`/usr/local/tar`

```tex
> cd /usr/local/tar
> tar -zxvf elasticsearch-7.13.2-linux-x86_64.tar.gz
> mv elasticsearch-7.13.2 ../
```

解压完成后，使用mv指令将es解压文件夹放入上一层目录中，即`/usr/local下`



**第二步：创建一个专门用来启用es的linux用户**

因为es不让root用户操作，就这么简单

```tex
> groupadd elsearch
> useradd elsearch -g elsearch -p yourpassword
> cd /usr/local
> chown -R elsearch:elsearch elasticsearch-7.13.2
```



**第三步：接受所有对外ip对9200的访问(可设置指定ip)**

```tex
> cd /usr/local/elasticsearch-7.13.2/config
> vim elasticsearch.yml
```

```xml
# 添加如下内容即可
network.host: 0.0.0.0
node.name: node-1
cluster.initial_master_nodes: ["node-1"]
http.port: 9200
```



**第四步：切换用户，启动elasticsearch服务**

```tex
> su elsearch
> cd /usr/local/elasticsearch-7.13.2/bin
> ./elasticsearch -d
```



**第五步：设置开放安全组以及打开防火墙下9200端口**

```tex
> systemctl start firewalld.service
> firewall-cmd --add-port=9200/tcp --permanent
> firewall-cmd --reload
```



##### 可能出现的启动异常情况处理

情况一：ERROR:最大虚拟内存区域vm.max_map_count[65530]太低

```tex
#修改文件
> vim /etc/sysctl.conf
 
#添加参数
...
vm.max_map_count = 262144

#使得配置生效
> sysctl -p
```



情况二：jvm启动内存不够

```TEX
> vim /usr/local/elasticsearch-7.13.2/config/jvm.options

# 把这两个参数设置到足够的值即可
-Xms512m
-Xmx512m
```

  
