package cn.y3lq.blog.common.security.model;

import cn.y3lq.blog.common.core.domain.User;
import lombok.Data;

import java.util.Set;

/**
 * @author: Y3lq
 * @description: 当前登录用户信息存储到redis的对象
 */
@Data
public class LoginUserModel {
    private static final long serialVersionUID = 1L;

    /**
     * 用户的id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户登录时间
     */
    private Long loginTime;

    /**
     * 用户过期时间
     */
    private Long expireTime;

    /**
     * 用户登录ip
     */
    private String ipaddr;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 用户角色集合
     */
    private Set<String> roleKeys;

    /**
     * 用户信息
     */
    private User user;
}
