package cn.y3lq.blog.gateway.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.y3lq.blog.common.core.constant.CacheConstants;
import cn.y3lq.blog.common.core.constant.SecurityConstants;
import cn.y3lq.blog.common.core.constant.TokenConstants;
import cn.y3lq.blog.common.security.model.LoginUserModel;
import cn.y3lq.blog.common.security.service.TokenService;
import cn.y3lq.blog.gateway.config.properties.IgnoreWhiteProperties;
import cn.y3lq.blog.gateway.util.WebFluxResponseUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author: Y3lq
 * @description: 网关鉴权
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private IgnoreWhiteProperties ignoreWhiteProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private Executor executor;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestUri = request.getURI().getPath();
        // 查看是否为白名单的路径，是的话放行
        if (isIgnoreWhitePath(requestUri, ignoreWhiteProperties.getWhites())) {
            return chain.filter(exchange);
        }
        String token = request.getHeaders().getFirst(TokenConstants.TOKEN);
        if (StringUtils.isEmpty(token)) {
            return WebFluxResponseUtils.webFluxResponseWriter(exchange.getResponse()
                    , HttpStatus.HTTP_UNAUTHORIZED, "令牌不能为空");
        }

        boolean verify = false;
        try {
            verify = JWTUtil.verify(token, TokenConstants.SECRET.getBytes());
        } catch (Exception e) {
            // 忽略 JWTUtil 抛出的异常
        }
        if (!verify) {
            return WebFluxResponseUtils.webFluxResponseWriter(exchange.getResponse()
                    , HttpStatus.HTTP_UNAUTHORIZED, "令牌验证错误");
        }

        // 查看缓存还有无用户信息，无则登录过期
        String userKeyInRedis = getUserKeyInRedis(token);
        Boolean flag = redisTemplate.hasKey(userKeyInRedis);
        if (!flag) {
            return WebFluxResponseUtils.webFluxResponseWriter(exchange.getResponse()
                    , HttpStatus.HTTP_UNAUTHORIZED, "登录状态已过期");
        }
        // 异步查看是否需要刷新会话有效期
        CompletableFuture.runAsync(() -> {
            refreshToken(token);
        }, executor);

        return chain.filter(exchange);
    }

    /**
     * 查看令牌有效期，看是否要延长会话，如果小于15分钟过期，自动延长会话
     *
     * @param token jwtToken
     */
    private void refreshToken(String token) {
        JWTPayload payload = JWTUtil.parseToken(token).getPayload();
        long expiration = (long) payload.getClaim(TokenConstants.EXPIRATION_TIME);
        long leftTime = expiration - System.currentTimeMillis();
        // 查看缓存在 redis 的 key 剩余时间是不是小于最低刷新时间，是的话刷新缓存的过期时间
        if (leftTime < CacheConstants.REFRESH_TIME) {
            Object o = redisTemplate.opsForValue().get(CacheConstants.LOGIN_TOKEN_KEY
                    + payload.getClaim(TokenConstants.USER_ID));
            if (ObjectUtils.isEmpty(o)) {
                // 恰巧这时候缓存过期，则不处理
                return;
            }
            LoginUserModel loginUserModel = JSONUtil.toBean(JSONUtil.toJsonStr(o), LoginUserModel.class);
            tokenService.refreshToken(loginUserModel);
        }
    }

    /**
     * 判断是否为放行路径
     *
     * @param requestUri 本次请求的path
     * @param whites     白名单集合
     */
    private boolean isIgnoreWhitePath(String requestUri, List<String> whites) {
        if (StringUtils.isEmpty(requestUri) || CollectionUtil.isEmpty(whites)) {
            return false;
        }
        AntPathMatcher matcher = new AntPathMatcher();
        for (String path : whites) {
            if (matcher.match(requestUri, path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取jwtToken存储的userKey，并且拼接上前缀
     *
     * @param token jwt
     */
    private String getUserKeyInRedis(String token) {
        String userId = (String) JWTUtil.parseToken(token).getPayload().getClaim(TokenConstants.USER_ID);
        return CacheConstants.LOGIN_TOKEN_KEY + userId;
    }

    @Override
    public int getOrder() {
        return -300;
    }
}
