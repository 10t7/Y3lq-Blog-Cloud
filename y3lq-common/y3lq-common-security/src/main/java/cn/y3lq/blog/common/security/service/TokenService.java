package cn.y3lq.blog.common.security.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import cn.y3lq.blog.common.core.constant.CacheConstants;
import cn.y3lq.blog.common.core.constant.TokenConstants;
import cn.y3lq.blog.common.core.constant.UserRoleConstants;
import cn.y3lq.blog.common.security.model.LoginUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author: Y3lq
 * @description: token的相关处理
 */
@Component
public class TokenService {
    @Autowired
    private RedisTemplate redisTemplate;

    private static final Integer REDIS_EXPIRATION_TIME = 30;

    /**
     * 创建令牌 并且放到redis中
     */
    public String createToken(LoginUserModel loginUserModel) {
        // 给 LoginUserModel 填充一些基本信息
        Long userId = loginUserModel.getUser().getUserId();
        String username = loginUserModel.getUser().getUsername();
        loginUserModel.setUserId(userId);
        loginUserModel.setUsername(username);
        loginUserModel.setPermissions(loginUserModel.getUser().getPermissions());
        loginUserModel.setRoleKeys(loginUserModel.getUser().getRoleKeys());
        // 减小网络传输负担
        loginUserModel.getUser().setPermissions(null);
        // 完善loginUserModel ，并且保存到redis
        refreshToken(loginUserModel);
        // 设置 JWT 保存的信息
        HashMap<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(TokenConstants.EXPIRATION_TIME, loginUserModel.getExpireTime());
        // 放到 jwtToken 的 userId 为 String 类型
        claimsMap.put(TokenConstants.USER_ID, loginUserModel.getUserId() + "");
        return JWTUtil.createToken(claimsMap, TokenConstants.SECRET.getBytes());
    }

    /**
     * 刷新令牌有效期并且放到redis
     */
    public void refreshToken(LoginUserModel loginUserModel) {
        if (loginUserModel.getLoginTime() == null) {
            // 如果登录时间没有设置说明是第一次登录，需要设置登录时间
            // 如果设置了，则是延长会话，不重写设置登录时间
            loginUserModel.setLoginTime(System.currentTimeMillis());
        }
        loginUserModel.setExpireTime(System.currentTimeMillis() + CacheConstants.EXPIRATION);
        String userKeyInRedis = getLoginRedisKey(loginUserModel.getUserId() + "");
        String userInfoJson = JSONUtil.toJsonStr(loginUserModel);
        redisTemplate.opsForValue().set(userKeyInRedis, userInfoJson, REDIS_EXPIRATION_TIME, TimeUnit.MINUTES);
    }

    /**
     * 拿到缓存在 redis 的 key
     */
    public String getLoginRedisKey(String userId) {
        return CacheConstants.LOGIN_TOKEN_KEY + userId;
    }
}
