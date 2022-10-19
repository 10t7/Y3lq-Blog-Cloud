package cn.y3lq.blog.auth.config;

import cn.y3lq.blog.auth.filter.*;
import cn.y3lq.blog.auth.handler.*;
import cn.y3lq.blog.common.core.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;


/**
 * @author: Y3lq
 * @description: SpringSecurity配置类
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPointImpl;

    /**
     * 成功退出登录处理类
     */
    @Autowired
    private LogoutSuccessHandlderImpl logoutSuccessHandlderImpl;

    /**
     * 用户认证逻辑
     */
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationFailureHandlerImpl authenticationFailureHandlerImpl;

    /**
     * 认证成功处理类
     */
    @Autowired
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandlerImpl;

    /**
     * 认证filter
     */
    @Autowired
    @Lazy
    private Y3lqBasicAuthenticationFilter y3lqBasicAuthenticationFilter;

    /**
     * 验证码过滤器
     */
    @Autowired
    private CaptchaFilter captchaFilter;


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 使用jwt,关闭csrf
                .csrf().disable()

                // 放行创建验证码请求，其他拦截
                .authorizeRequests()
                .antMatchers("/captcha").permitAll()
                .anyRequest().authenticated()

                // 成功退出登录处理类
                .and().logout().logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandlderImpl)

                // 开启表单登录
                .and().formLogin().loginProcessingUrl("/login")
                .permitAll()

                // 设置为无状态，使用jwt
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 设置认证jwt相关
                .and().httpBasic()
                .authenticationEntryPoint(authenticationEntryPointImpl);


        // 添加认证 jwt相关 过滤器
        httpSecurity.addFilter(y3lqBasicAuthenticationFilter);
        // 添加验证码过滤器
        httpSecurity.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
        // 代替原本的过滤器，增加JSON解析能力
        httpSecurity.addFilterAt(y3lqUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 密码哈希加密
     */
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(Constants.BCRYPT_STRENGTH);
    }

    /**
     * 身份认证接口
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());

    }

    /**
     * 表单认证过滤器，增加解析JSON能力
     */
    @Bean
    public Y3lqUsernamePasswordAuthenticationFilter y3lqUsernamePasswordAuthenticationFilter() throws Exception {
        Y3lqUsernamePasswordAuthenticationFilter authenticationFilter = new Y3lqUsernamePasswordAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandlerImpl);
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandlerImpl);
        return authenticationFilter;
    }

    /**
     * 配置RepeatableFilter过滤器
     */
    @Bean
    FilterRegistrationBean<RepeatableFilter> repeatableFilterBean() {
        FilterRegistrationBean<RepeatableFilter> bean = new FilterRegistrationBean<>();
        bean.addUrlPatterns("/*");
        bean.setFilter(new RepeatableFilter());
        bean.setOrder(-100);
        return bean;
    }

//    /**
//     * 注入 ClientRegistrationRepository 自定义 Oauth2 相关配置
//     *
//     * @return
//     */
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        return new InMemoryClientRegistrationRepository(githubClientRegistration());
//    }
//
//    /**
//     * Github Oauth2 相关配置
//     *
//     * @return
//     */
//    private ClientRegistration githubClientRegistration() {
//        return ClientRegistration.withRegistrationId("github").clientId("33cb9d8f961e2e155159")
//                .clientSecret("7c7c0ff238c616793cedfa2b5a922f3ea69ed5af")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .userNameAttributeName("id")
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUriTemplate("http://localhost:10000/login/oauth2/code/github")
//                .scope("read:user")
//                .authorizationUri("https://github.com/login/oauth/authorize")
//                .tokenUri("https://github.com/login/oauth/access_token")
//                .userInfoUri("https://api.github.com/user")
//                .clientName("Github")
//                .build();
//    }
//
//    /**
//     * QQ oauth2 相关配置
//     *
//     * @return
//     */
//    private ClientRegistration qqClientRegistration() {
//        return ClientRegistration.withRegistrationId("qq").clientId("33cb9d8f961e2e155159")
//                .clientSecret("7c7c0ff238c616793cedfa2b5a922f3ea69ed5af")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .userNameAttributeName("id")
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUriTemplate("http://localhost:10000/login/oauth2/code/qq")
//                .scope("read:user")
//                .authorizationUri("https://graph.qq.com/oauth2.0/authorize")
//                .tokenUri("https://graph.qq.com/oauth2.0/token")
//                .userInfoUri("https://graph.qq.com/user/get_user_info")
//                .clientName("QQ")
//                .build();
//    }
}
