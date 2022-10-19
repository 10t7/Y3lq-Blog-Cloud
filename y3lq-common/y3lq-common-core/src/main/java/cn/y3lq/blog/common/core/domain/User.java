package cn.y3lq.blog.common.core.domain;

import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * @author: Y3lq
 * @description: 用户登录查询密码和缓存用户基本信息到 redis 业务实体类
 */
@Data
public class User {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户手机号码
     */
    private String phone;

    /**
     * 用户微信
     */
    private String wechat;

    /**
     * 用户性别（0：女  1：男  2：未知）
     */
    private String gender;

    /**
     * 用户头像地址
     */
    private String avatar;

    /**
     * 用户状态(0:正常，1：停用)
     */
    private String status;

    /**
     * 用户删除标识（0：正常  1：删除）
     */
    private String delFlag;

    /**
     * 权限列表集合
     */
    private Set<String> permissions;


    /**
     * 用户角色集合
     */
    private Set<String> roleKeys;
}
