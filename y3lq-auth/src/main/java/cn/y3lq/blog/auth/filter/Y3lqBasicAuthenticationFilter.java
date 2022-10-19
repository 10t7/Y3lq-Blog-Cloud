package cn.y3lq.blog.auth.filter;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.y3lq.blog.auth.exception.AuthException;
import cn.y3lq.blog.common.core.constant.TokenConstants;
import cn.y3lq.blog.common.core.exception.ServiceException;
import cn.y3lq.blog.common.security.model.LoginUserModel;
import cn.y3lq.blog.common.security.service.TokenService;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: Y3lq
 * @description: 验证JWT
 */
@Component
public class Y3lqBasicAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisTemplate redisTemplate;

    public Y3lqBasicAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 拿到请求头的token 下面则进行验证
        String token = request.getHeader(TokenConstants.TOKEN);
        if (StringUtils.isEmpty(token)) {
            // 没有token 则为匿名访问，此处放行，后面filter还会有验证处理
            chain.doFilter(request, response);
            return;
        }
        boolean verify = false;
        // 验证是否是自己颁发的令牌
        try {
            verify = JWTUtil.verify(token, TokenConstants.SECRET.getBytes());
        } catch (Exception e) {
            // 此处忽略JWTUtil抛出的异常，改为抛出自己的异常
            throw new AuthException("令牌验证失败");
        }
        if (!verify) {
            throw new AuthException("令牌验证失败");
        }

        JWTPayload payload = JWTUtil.parseToken(token).getPayload();
        String userId = (String) payload.getClaim(TokenConstants.USER_ID);
        if (StringUtils.isEmpty(userId)) {
            throw new AuthException("颁发的令牌异常，没有存储存储在redis的用户信息 key");
        }

        String keyInRedis = tokenService.getLoginRedisKey(userId);
        // 去redis查询是否有用户存在，不存在说明登录过期了
        Object userInfo = redisTemplate.opsForValue().get(keyInRedis);
        if (ObjectUtils.isEmpty(userInfo)) {
            throw new AuthException("用户登录状态已过期，请重新登录");
        }
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(userInfo);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }


    /**
     * 返回authentication，完善信息
     *
     * @param userInfo
     */
    private UsernamePasswordAuthenticationToken getAuthentication(Object userInfo) {
        LoginUserModel loginUserModel = JSONUtil.toBean(JSONUtil.toJsonStr(userInfo), LoginUserModel.class);
        String username = loginUserModel.getUser().getUsername();
        // 放置角色
        List<SimpleGrantedAuthority> auth = new ArrayList<>();
        Set<String> roleKeys = loginUserModel.getUser().getRoleKeys();
        for (String roleKey : roleKeys) {
            auth.add(new SimpleGrantedAuthority(roleKey));
        }
        return new UsernamePasswordAuthenticationToken(username, null, auth);
    }
}
