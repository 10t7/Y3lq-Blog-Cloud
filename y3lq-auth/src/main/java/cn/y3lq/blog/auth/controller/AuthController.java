package cn.y3lq.blog.auth.controller;

import cn.y3lq.blog.common.core.constant.CacheConstants;
import cn.y3lq.blog.common.core.constant.TokenConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 认证控制器
 */
@RestController
public class AuthController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 批量删除用户登录凭证
     *
     * @param userIds 需要被删除登录凭证的用户ID数组
     * @param token   本次操作用户的jwtToken
     */
    @PostMapping("/logoutUserByIds")
    public void logoutUserByIds(@RequestBody List<String> userIds, @RequestHeader(TokenConstants.TOKEN) String token) {
        ArrayList<String> userLoginKeys = new ArrayList<>();
        for (String userId : userIds) {
            userLoginKeys.add(CacheConstants.LOGIN_TOKEN_KEY + userId);
        }
        // 批量删除用户登录凭证
        redisTemplate.delete(userLoginKeys);
    }

}
