package cn.y3lq.blog.auth.handler;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import cn.y3lq.blog.common.core.constant.CacheConstants;
import cn.y3lq.blog.common.core.constant.HttpStatus;
import cn.y3lq.blog.common.core.constant.TokenConstants;
import cn.y3lq.blog.common.core.domain.AjaxResult;
import cn.y3lq.blog.common.security.model.LoginUserModel;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author: Y3lq
 * @description: 退出成功处理类
 */
@Component
public class LogoutSuccessHandlderImpl implements LogoutSuccessHandler {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 下面退出逻辑代码是因为这个微服务是使用 jwt 作为会话管理，SessionCreationPolicy 设置为无状态
     * 所以框架不会拿到sessionid去session拿到authentication，放到SecurityContextHolder
     * 拿到jwt 解析出缓存在redis上的key，然后进行删除，出错了则不管
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            String token = request.getHeader(TokenConstants.TOKEN);
            boolean verify = JWTUtil.verify(token, TokenConstants.SECRET.getBytes());
            if (verify) {
                String userId = (String) JWTUtil.parseToken(token).getPayload().getClaim(TokenConstants.USER_ID);
                String userKeyInRedis = CacheConstants.LOGIN_TOKEN_KEY + userId;
                redisTemplate.delete(userKeyInRedis);
                ServletOutputStream outputStream = null;
                // 对前端进行相应
                try {
                    response.setStatus(HttpStatus.SUCCESS);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("utf-8");
                    outputStream = response.getOutputStream();
                    // 把JWT token 设置进响应
                    AjaxResult success = AjaxResult.success("退出登录成功");
                    outputStream.write(JSONUtil.toJsonStr(success).getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    outputStream.flush();
                    outputStream.close();
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            // 出错则不响应
        }

    }
}
