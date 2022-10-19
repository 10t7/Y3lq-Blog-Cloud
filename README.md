## 项目简介

Y3lq-Blog 是基于微服务架构的前后端分离精简博客系统

前端主要使用**Vue**、**Element**、**Vuetify**、后端主要使用**SpringBoot**、**SpringCloud**、**Mybatis**进行开发

使用**Jwt**+**SpringSecurity**进行登录认证，自定义注解作为权限校验

采用**Docker**容器化部署

博客前台主要功能包括：文章、日记发表以及对其进行评论点赞，用户登录、注册

后台管理主要功能包括：文章、文章标签、日记、用户、角色、菜单的管理



## 演示地址

[博客前台演示](https://www.10t7.tech)
[后台管理演示](https://admin.10t7.tech)


## 架构图
<img width="920" alt="image" src="https://user-images.githubusercontent.com/87005680/196652444-6b7170e2-816f-4082-a232-d4fb2617e175.png">



## 核心技术

### 后端

SpringBoot、SpringCloud&Alibaba、SpringSecurity、Mybatis、Mysql、Redis、JWT、Nginx、Docker

### 前端

Vue.js、Vue-router、Vuex、Element-ui、Vuetify





## 系统设计

本系统基于微服务架构并且采用现流行的前后端分离模式

本项目规模比较小，但仍然采用的SpringCloud而不是单体架构是为了学习微服务开发，以及在认证服务采用的SpringSecurity+JWT也是为了学习相关技术。

由于时间因素，本项目并未加入熔断限流，链路监控，日志系统以及系统状态监控

Gateway主要是根据请求转发服务，并且进行JWT鉴权，认证服务是进行登录验证以及登出功能，

Nacos作为服务注册中心以及配置中心，服务之间的调用采用了OpenFeign

系统服务（y3lq-system）完成了大部分系统的功能

部署使用Docker

### 认证授权设计

本系统采用 Jwt与SpringSecurity进行认证，自定义注解进行权限校验

由于Jwt是无状态的，本系统引入Redis使其具有状态，可对用户进行注销登录操作



本系统采用**登录认证**流程

1. 浏览器带着账号密码请求服务器
2. 网关会对登录请求进行放行，转发到认证服务
3. 认证服务集成 SpringSecurity，由它的一系列过滤链进行验证（SpringSecurity只负责认证不负责鉴权）
4. 认证成功将会创建Jwt Token进行返回给前端，Jwt 荷载保存着用户Id、过期时间、同时也会将用户基本信息（用户名、拥有的权限或者角色）缓存redis过期时间默认为30分钟
5. 前端拿到token进行保存



本系统采用的**鉴权**流程

1. 浏览器对服务器发起请求
2. Gateway 过滤器进行Jwt验证（白名单的请求会放行）
3. 如果令牌为空或者验证错误，令牌过期会进行返回
4. 认证成功查看是否刷新过期时间
5. 认证成功转发到具体服务，具体方法是否有权限基于AOP的注解进行鉴权





### 权限系统设计

本系统采用的权限模型为**RBAC0（基于角色的访问控制）**，一个用户可以具备多个角色，一个角色可以具备多个权限，最终用户所具备的权限是用户所具备的角色的权限并集。

本系统角色有

1. 超级管理员  		--拥有所有权限
2. 普通管理员          --拥有大部分权限，除了角色和菜单相关权限
3. 访客                      --可以登录后台管理系统，拥有除了删除的基本权限
4. 博客用户              --拥有该角色才能登录博客前台



本系统后台管理系统实现了动态菜单，每个用户登录会获取属于他自己的路由，并且动态渲染出来

流程

1. 用户登录后台管理系统，登录成功发起请求获取当前用户个人消息（用户基本信息与拥有角色、权限字符串）
2. 发起请求获取当前用户拥有的路由，前端获取路由信息动态渲染页面，并且按钮会根据当前用户的权限动态显示


## 后台管理

基于 [vue-element-admin](https://panjiachen.github.io/vue-element-admin-site/zh/guide/) 以及 element-ui




## 博客前台

主要基于 [vuetify官方主题](https://material-landing.vuetifyjs.com/)以及 [vuetify ui](https://vuetify.cn/zh-Hans/  )





## 项目

项目结构

```tex
Y3lq-Blog-Cloud
└── y3lq-api    					// 接口模块
     ├── y3lq-api-auth    					// 认证中心接口
     └── y3lq-api-system    				// 系统接口
├── y3lq-auth    					// 认证中心[10000]
└── y3lq-common    				// 通用模块
     ├── y3lq-common-core    				// 核心模块
     ├── y3lq-common-redis     			// 缓存服务
     └── y3lq-common-security    		// 安全模块
├── y3lq-gateway    			// 网关模块[8080]
└── y3lq-modules    			// 业务模块
     ├── y3lq-system    						// 系统模块[11000]
     └── y3lq-thirdparty    				// 第三方服务模块[12000]
```





