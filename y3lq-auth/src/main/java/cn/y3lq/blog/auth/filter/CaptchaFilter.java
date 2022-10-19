package cn.y3lq.blog.auth.filter;

import cn.y3lq.blog.auth.config.properties.CaptchaProperties;
import cn.y3lq.blog.auth.exception.AuthException;
import cn.y3lq.blog.auth.handler.AuthenticationFailureHandlerImpl;
import cn.y3lq.blog.common.core.constant.CacheConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Y3lq
 * @description: 校验登录验证码Filter
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private CaptchaProperties captchaProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String CODE = "code";

    private static final String UUID = "uuid";

    @Autowired
    private AuthenticationFailureHandlerImpl authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 只对/login 登录请求进行过滤
        if (!request.getMethod().equals("POST") || !request.getRequestURI().equals("/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 查看验证码功能是否开启
        if (!captchaProperties.getEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            // 校验验证码是否正确
            validateCaptcha(request);
        } catch (AuthException authException) {
            // 失败处理器处理
            authenticationFailureHandler.onAuthenticationFailure(request, response, authException);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 校验验证码
     */
    private void validateCaptcha(HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        String uuid = "";
        String code = "";
        if (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE)
                || contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            // JSON格式提交验证码参数
            HashMap<String, String> map = (HashMap<String, String>) new ObjectMapper()
                    .readValue(request.getInputStream(), Map.class);
            uuid = map.get("uuid");
            code = map.get("code");
        } else {
            // 表单提交验证码参数
            uuid = request.getParameter("uuid");
            code = request.getParameter("code");
        }
        String captchaKeyInRedis = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(uuid)) {
            throw new AuthException("缺少验证码相关参数");
        }
        String script = "if redis.call('get', KEYS[1]) == ARGV[1]" +
                "then" +
                "return redis.call('del', KEYS[1])" +
                "else" +
                "return 0" +
                "end";
        // 使用lua脚本验证验证码
        List<String> keys = new ArrayList<>();
        keys.add(captchaKeyInRedis);
        DefaultRedisScript defaultRedisScript = new DefaultRedisScript(script,Long.class);
        Long flag = (Long) redisTemplate.execute(defaultRedisScript, keys, uuid);
        if(flag.equals(0L)){
            throw new AuthException("验证码过期或验证码错误");
        }
    }
}
