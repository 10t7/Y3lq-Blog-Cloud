package cn.y3lq.blog.system.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * @author: Y3lq
 * @description: 返回前端渲染的user信息实体类
 */
@Data
public class UserListVO implements Serializable {
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
     * 用户微信号
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
     * 用户登录时间
     */
    private Date loginTime;

    /**
     * 用户登录IP
     */
    private String loginIp;

    /**
     * 用户状态(0:正常  1：停用)
     */
    private String status;

    /**
     * 用户删除标识（0：正常  1：删除）
     */
    private String delFlag;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 用户创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 用户信息更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 该角色具有的角色集合
     */
    List<RoleListVO> roles;


}
