-----------------------------------版本说明-------------
https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E

-----------------------------------环境命名空间----------
migrat-local:
	namespace: 410f8791-4d7e-439a-9114-4c5b8749a613
	
migrat-dev:
	namespace: 700c7f6d-ca9e-431d-b1ce-24906ae8a4f5

migrat-pro:
	namespace: 07b4f51b-369f-481b-938d-5566dd97b040	

-----------------------------------模块描述--------------

migrat: 父类项目，管理jar版本

migrat-common: 公用jar或配置

migrat-auth-shiro: 模块化开发统一授权模块

migrat-sso-sever: 单点登录，暂停开发

migrat-module-website: SAAS官网模块

migrat-module-company: SAAS基础模块

migrat-gateway: 基于gateway的网关实现

migrat-zuul: 基于netflix zuul的网关实现

----------------------------SQL路由说明------------------
登录状态：
 
 .路由到对应的业务库：自动路由，对应的控制器或方法 @DS(value = Constants.AUTO_ROUTING)
 
 .路由到主库： 在migrat.properties配置Mapper路径[]同时配置@DS(value = Constants.DEFAULT_DS)
 
未登录状态：
  .路由到主库：@DS(value = Constants.DEFAULT_DS)
  
  .路由到业务库：使用强制路由，设置tenantContext

----------------------------Jar启动方式-------------------

Windows启动：
java -Dfile.encoding=utf-8 -jar  xxx.jar ： 解决Nacos配置文件为中文的情况

Linux启动： java -jar xxx.jar : 无需指定编码方式

   
----------------------------Gateway & Zuul Configuration ---------------
Gateway:
	单机：
		uri: 具体链接
	连接： 服务直连方式
		
	Ribbon LoadBalance:	
		uri: lb://消费者名称
	连接： 通过Nacos进行微服务的LoadBalance,Nacos实现了SpringCloud基于RestTemplate + LoadBalance 	
	
熔断降级： Gateway所有的都是通过拦截器实现，底层基于ServerWebExchange实现，此处使用内置的Hystrix拦截器
		   Swagger2目前不支持POST方式的降级，支持GET，但前后端分离请求不受影响

限流： 令牌桶 、漏桶		   
		
Zuul：
   单机：
   	  url: 具体链接
   连接： 服务直连方式
   
   Ribbon LoadBalance:
   	  url替换为serviceId: 消费者名称
   连接：通过Nacos进行微服务的LoadBalance,Nacos实现了SpringCloud基于RestTemplate + LoadBalance	  	  
 
   Hystrix熔断降级配置
   1. zuul使用url和serviceId做负载均衡下，超时策略是不一样（重点）
   
   2. 本项目采用serviceId从nacos获取服务，通过Ribbon进行负载，所以超时要结合Ribbon的超时策略设置
   
   3. 超时设置：
   	  3.1 如果路由方式是url,则zuul.host开头生效
   	  	zuul:
   	  	  host:
   	  	  	connect-timeout-millis: 3000
   	  	  	socket-timeout-milis: 3000
   	  3.2 如果路由方式是基于微服务的serviceId开发，则Ribbon生效
   	  	ribbon:
   	  		ReadTimeout: 3000
   	  		ConnectTimeout: 1500
   	  	
   	  	hystrix:
   	  		command:
   	  			default:
   	  				execution:
   	  					timeout:
   	  						enabled: true
   	  					isolcation:
   	  						thread:
   	  							timeoutInMilliseconds: 2500		
   	    这里面ribbon和hystrix是同时生效的，哪个值小哪个生效，另一个就看不到效果了  	
   		https://blog.csdn.net/AaronSimon/article/details/84775410
   
----------------------------参考文档----------------------
注册监控中心：Nacos
https://nacos.io
https://segmentfault.com/u/larscheng

分布式事务：Seata
https://github.com/seata/seata/wiki/%E6%A6%82%E8%A7%88

流量哨兵：Sentinel
https://github.com/alibaba/Sentinel

OpenTracing标准： 
https://opentracing.io/

基于javaagent插件开发：https://github.com/alibaba/arthas

注意：
    .本项目是基于SpringBoot Parent 2.0.3.RELEASE 版本开发的，需要对应Alibaba Cloud版本来支持，请勿随意升级或降低jar版本
     Nacos Client : 1.1.4
    
     Alibaba Cloud Nacos Discovery & Alibaba Cloud Nacos Config : 2.0.1.RELEASE
	 Config Wiki: https://github.com/alibaba/spring-cloud-alibaba/wiki/Nacos-config
	 
	.目前只支持properties和yaml类型的文件
	
	.通过原生的SpringCloud注解 @RefreshScope 实现配置文件的自动更新
	
---------------------FeiginClient 传参--------------------------

	1、传基本类型参数

　　使用@RequestParam，注意注解的value参数不可少，代码如下：

	2、传Map参数

　　参数太多的时候，上面的方式就要写一堆，可以直接上map

　　注意，需要加@RequestParam注解，但不需要加注解的value参数

　　3、传对象参数

　　Fegin传对象的时候，需要加@RequestBody注解，如下：

　　注意，服务提供者的Controller的接收参数前也需要加@RequestBody注解

　　@RequestBody接收的是一个Json对象的字符串，而不是一个Json对象。 如果这时候要使用postman直接请求上面的test3接口，那么需要将Content-Type修改为application/json
	
	4、传对象 + RequestInteceptor

　　处理思路：feign发请求的时候，将json body转成query。

　　服务提供者的Controller的接收参数前不需要@RequestBody注解，即无需改动了 
	
------------------------------------------------------------------------

Seata使用：

1. Seata分为三个角色： 
	TC(服务端)   -> 事务协调器，维护全局和分支事务的状态，驱动全局和分支事务的提交和回滚
	
	TM（客户端） -> 事务管理器，定义全局事务的范围：开始全局事务、提交和回滚全局事务
	
	RM (客户端)  -> 资源管理器，管理分支事务处理的资源，与TC交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交和回滚

2.服务端和客户端初始化： https://seata.io/zh-cn/docs/ops/deploy-guide-beginner.html
  ${project}/script/**

3. 服务端启动：
   [当前seata1.0.0]之后，file.conf 和 registry.conf 可以直接通过yaml或者properties进行配置
   
   单环境： 直接使用file.conf
   
   多环境配置： 使用registry.conf,
   
   命令启动: seata-server.sh -h 127.0.0.1 -p 8091 -m db -n 1
    -h: 注册到注册中心的ip
    -p: Server rpc 监听端口
    -m: 全局事务会话信息存储模式，file、db，优先读取启动参数
    -n: Server node，多个Server时，需区分各自节点，用于生成不同区间的transactionId，以免冲突
    -e: 多环境配置参考 http://seata.io/en-us/docs/ops/multi-configuration-isolation.html
    
4. 表结构：
   服务端： 全局事务依赖表 global_table， branch_table, lock_table
   客户端：
   AT模式： undo_log
   https://lidong1665.blog.csdn.net/article/details/106262197
   
   事务管理（ACID）
   A: 原子性
   C: 一致性
   I: 隔离性
   D: 持久性

分布式事务都要支持两阶段模型：
> 一阶段 ： prepare行为
> 二阶段 ： commit或者 rollback行为
根据两阶段行为模式的不同，分为AT Mode & TCC Mode
  
AT模式： 基于本地事务（ACID）的关系型数据库
1.一阶段prepare行为 ： 本地事务中，一并提交业务数据和回滚日志记录
2.二阶段commit行为 ： 马上结束，自动异步批量清理回滚日志（如seata中的TC接受到commit）
3.二阶段rollback行为 ： 通过回滚日志，自动生成补偿操作，完成数据回滚（如seata中TC接受到rollback）

TCC模式： 不依赖于底层数据源的事务支持
1.一阶段prepare行为： 调用自定义的prepare逻辑
2.二阶段commit行为： 调用自定义的commit逻辑
3.二阶段rollback行为： 调用自定义的rollback逻辑
所谓TCC，就是把自定义的分支事务纳入到全局事务中
   
   
Saga模式：基于状态机实现，且支持高可用

状态机引擎分为三层：
1.Eventing: 实现事件驱动架构
2.ProcessController
3.StateMachineEngine:
  实现状态机每种state的行为和路由逻辑
  提供API，状态机语言仓库