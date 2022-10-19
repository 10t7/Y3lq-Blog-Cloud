package cn.y3lq.blog.system.vo;

import cn.y3lq.blog.common.core.valid.AddGroup;
import cn.y3lq.blog.common.core.valid.GenderValue;
import cn.y3lq.blog.common.core.valid.UpdateGroup;
import cn.y3lq.blog.system.entity.UserSettingEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author: Y3lq
 * @description: 用户信息VO
 */
@Data
public class UserInfoVO implements Serializable {
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
     * 用户状态
     */
    private String status;

    /**
     * 用户角色ID数组
     */
    private Long[] roleIds;

    /**
     * 角色名字数组
     */
    private String[] roles;

    /**
     * 权限字符集合
     */
    private Set<String> perms;


    private Integer articleCount;

    private Integer diaryCount;
}
