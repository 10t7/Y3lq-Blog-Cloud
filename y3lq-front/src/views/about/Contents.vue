<template>
  <section id="content">
    <base-card>
      <h2><a id="_0"></a>项目简介</h2>
      <p>Y3lq-Blog 是基于微服务架构的前后端分离精简博客系统</p>
      <p>
        前端主要使用
        <strong>Vue</strong
        >、<strong>Element</strong>、<strong>Vuetify</strong>、后端主要使用<strong>SpringBoot</strong>、<strong>SpringCloud</strong>、<strong>Mybatis</strong>进行开发
      </p>
      <p>
        使用<strong>Jwt</strong>+<strong>SpringSecurity</strong>进行登录认证，自定义注解作为权限校验
      </p>
      <p>采用<strong>Docker</strong>容器化部署</p>
      <p>
        博客前台主要功能包括：文章、日记发表以及对其进行评论点赞，用户登录、注册
      </p>
      <p>后台管理主要功能包括：文章、文章标签、日记、用户、角色、菜单的管理</p>
      <br />
      <h2><a id="_14"></a>演示地址</h2>
      <p>
        <a href="https://www.10t7.tech" target="_blank">博客前台演示</a><br />
        <a href="https://admin.10t7.tech" target="_blank">后台管理演示</a>
      </p>
      <br />
      <h2><a id="_18"></a>架构图</h2>
      <img src="../../assets/jiagoutu.png" style="zoom: 50%" />
      <br />
      <h2><a id="_20"></a>核心技术</h2>
      <p>
        后端<br />
        SpringBoot、SpringCloud&amp;Alibaba、SpringSecurity、Mybatis、Mysql、Redis、JWT、Nginx、Docker
      </p>
      <p>
        前端<br />
        Vue.js、Vue-router、Vuex、Element-ui、Vuetify
      </p>
      <br />
      <h2><a id="_27"></a>系统设计</h2>
      <p>本系统基于微服务架构并且采用现流行的前后端分离模式</p>
      <p>
        本项目规模比较小，但仍然采用的SpringCloud而不是单体架构是为了学习微服务开发，以及在认证服务采用的SpringSecurity+JWT也是为了学习相关技术。
      </p>
      <p>
        由于时间因素，本项目并未加入熔断限流，链路监控，日志系统以及系统状态监控
      </p>
      <p>
        Gateway主要是根据请求转发服务，并且进行JWT鉴权，认证服务是进行登录验证以及登出功能，
      </p>
      <p>Nacos作为服务注册中心以及配置中心，服务之间的调用采用了OpenFeign</p>
      <p>系统服务（y3lq-system）完成了大部分系统的功能</p>
      <p>部署使用Docker</p>
      <h3><a id="_42"></a>认证授权设计</h3>
      <p>本系统采用 Jwt与SpringSecurity进行认证，自定义注解进行权限校验</p>
      <p>
        由于Jwt是无状态的，本系统引入Redis使其具有状态，可对用户进行注销登录操作
      </p>
      <p>本系统采用<strong>登录认证</strong>流程</p>
      <ol>
        <li>浏览器带着账号密码请求服务器</li>
        <li>网关会对登录请求进行放行，转发到认证服务</li>
        <li>
          认证服务集成
          SpringSecurity，由它的一系列过滤链进行验证（SpringSecurity只负责认证不负责鉴权）
        </li>
        <li>
          认证成功将会创建Jwt Token进行返回给前端，Jwt
          荷载保存着用户Id、过期时间、同时也会将用户基本信息（用户名、拥有的权限或者角色）缓存redis过期时间默认为30分钟
        </li>
        <li>前端拿到token进行保存</li>
      </ol>
      <p>本系统采用的<strong>鉴权</strong>流程</p>
      <ol>
        <li>浏览器对服务器发起请求</li>
        <li>Gateway 过滤器进行Jwt验证（白名单的请求会放行）</li>
        <li>如果令牌为空或者验证错误，令牌过期会进行返回</li>
        <li>认证成功查看是否刷新过期时间</li>
        <li>认证成功转发到具体服务，具体方法是否有权限基于AOP的注解进行鉴权</li>
      </ol>
      <h3><a id="_67"></a>权限设计</h3>
      <p>
        本系统采用的权限模型为<strong>RBAC0（基于角色的访问控制）</strong>，一个用户可以具备多个角色，一个角色可以具备多个权限，最终用户所具备的权限是用户所具备的角色的权限并集。
      </p>
      <p>本系统角色有</p>
      <ol>
        <li>超级管理员 --拥有所有权限</li>
        <li>普通管理员 --拥有大部分权限，除了角色和菜单相关权限</li>
        <li>访客 --可以登录后台管理系统，拥有除了删除的基本权限</li>
        <li>博客用户 --拥有该角色才能登录博客前台</li>
      </ol>
      <p>
        本系统后台管理系统实现了动态菜单，每个用户登录会获取属于他自己的路由，并且动态渲染出来
      </p>
      <p>流程</p>
      <ol>
        <li>
          用户登录后台管理系统，登录成功发起请求获取当前用户个人消息（用户基本信息与拥有角色、权限字符串）
        </li>
        <li>
          发起请求获取当前用户拥有的路由，前端获取路由信息动态渲染页面，并且按钮会根据当前用户的权限动态显示
        </li>
      </ol>
      <br />
      <h2><a id="_87"></a>后台管理</h2>
      <p>
        基于
        <a
          href="https://panjiachen.github.io/vue-element-admin-site/zh/guide/"
          target="_blank"
          >vue-element-admin</a
        >
        以及
        <a href="https://element.eleme.cn/#/zh-CN" target="_blank"
          >element-ui</a
        >
        <img src="../../assets/hou.png" style="zoom: 30%" />
      </p>
      <br />
      <h2><a id="_90"></a>博客前台</h2>
      <p>
        基于
        <a href="https://material-landing.vuetifyjs.com/" target="_blank"
          >vuetify官方主题</a
        >以及
        <a href="https://vuetify.cn/zh-Hans/" target="_blank">vuetify-ui</a>
      </p>
      <img src="../../assets/qian.png" style="zoom: 30%" />
    </base-card>
  </section>
</template>

<script>
export default {
  name: "SampleContents",
  data() {
    value: "";
  },
};
</script>
