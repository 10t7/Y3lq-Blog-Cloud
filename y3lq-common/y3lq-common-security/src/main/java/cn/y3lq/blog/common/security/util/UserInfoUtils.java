package cn.y3lq.blog.common.security.util;

import cn.hutool.json.JSONUtil;
import cn.y3lq.blog.common.core.constant.CacheConstants;
import cn.y3lq.blog.common.core.exception.ServiceException;
import cn.y3lq.blog.common.core.util.SecurityUtils;
import cn.y3lq.blog.common.security.model.LoginUserModel;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: Y3lq
 * @description: 获取用户信息
 */
@Component
public class UserInfoUtils {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private static RedisTemplate redisTemplateStatic;

    public static ThreadLocal<LoginUserModel> userModelThreadLocal = new ThreadLocal<>();

    /**
     * 获取当前用户存储在redis的个人信息
     */
    public static LoginUserModel getUserInfo(HttpServletRequest request) {
        Long userId = SecurityUtils.getCurrentUserId(request);
        Object userInfo = redisTemplateStatic.opsForValue().get(CacheConstants.LOGIN_TOKEN_KEY + userId);
        if (ObjectUtils.isEmpty(userInfo)) {
            throw new ServiceException("未登录或登录过期");
        }
        String userInfoJson = JSONUtil.toJsonStr(userInfo);
        LoginUserModel userModel = JSONUtil.toBean(userInfoJson, LoginUserModel.class);
        userModelThreadLocal.set(userModel);
        return userModel;
    }

    @PostConstruct
    public void init(){
        redisTemplateStatic = this.redisTemplate;
    }


}
