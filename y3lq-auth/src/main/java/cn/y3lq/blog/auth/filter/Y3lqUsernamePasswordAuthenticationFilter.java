package cn.y3lq.blog.auth.filter;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import cn.y3lq.blog.auth.config.properties.CaptchaProperties;
import cn.y3lq.blog.auth.exception.AuthException;
import cn.y3lq.blog.common.core.constant.UserRoleConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Y3lq
 * @description: 前端使用axios 默认是json数据格式传输数据
 */
public class Y3lqUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthException("只支持POST请求");
        }
        String contentType = request.getContentType();
        // 如果格式为JSON，则自己解析
        if (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)
                || contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
            HashMap<String, String> map = (HashMap<String, String>) new ObjectMapper()
                    .readValue(request.getInputStream(), Map.class);
            String username = map.get("username");
            String password = map.get("password");
            String type = map.get("type");
            if(StringUtils.isNotEmpty(type)){
                checkIsBlogLogin(type);
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            setDetails(request, authenticationToken);
            return this.getAuthenticationManager().authenticate(authenticationToken);
        }
        // 不为JSON格式，调用父类方法
        return super.attemptAuthentication(request, response);
    }

    /**
     * 查看是否为博客登录
     * @param type
     */
    private void checkIsBlogLogin(String type) {
        if ("blog-login".equals(type)){
            threadLocal.set("blog-login");
        }
    }

}
