package cn.y3lq.blog.system.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Y3lq
 * @description: 用户实体类，跟数据库一一对应
 */
@Data
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Null
    private Long userId;

    /**
     * 用户名
     */

    private String username;

    /**
     * 用户昵称
     */
    @Length(min = 2,max = 18,message = "用户昵称长度应在[2, 18]")
    private String nickname;

    /**
     * 用户密码
     */
    @NotEmpty
    @Length(min = 2,max = 18,message = "用户昵称长度应在[2, 18]")
    private String password;

    /**
     * 用户邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Length(min = 2,max = 18,message = "邮箱长度应在[2, 18]")
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
}
