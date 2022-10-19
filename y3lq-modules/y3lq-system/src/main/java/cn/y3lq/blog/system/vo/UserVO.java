package cn.y3lq.blog.system.vo;

import cn.y3lq.blog.common.core.valid.AddGroup;
import cn.y3lq.blog.common.core.valid.GenderValue;
import cn.y3lq.blog.common.core.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Y3lq
 * @description: 前端新增修改封装的VO
 */
@Data
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Null(message = "新增用户不能指定ID", groups = {AddGroup.class})
    @NotNull(message = "修改用户用户ID不能为空", groups = {UpdateGroup.class})
    @Range(min = 1L, max = 9999999L, message = "用户ID不合理", groups = {UpdateGroup.class})
    private Long userId;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(min = 2, max = 18, message = "用户名长度应在[2, 18]", groups = {AddGroup.class, UpdateGroup.class})
    private String username;

    /**
     * 用户昵称
     */
    @NotBlank(message = "昵称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(min = 2, max = 18, message = "用户昵称长度应在[2, 18]", groups = {AddGroup.class, UpdateGroup.class})
    private String nickname;

    /**
     * 用户密码
     */
    @Length(min = 2, max = 18, message = "用户密码长度应在[2, 18]", groups = {AddGroup.class})
    @NotBlank(message = "密码不能为空", groups = {AddGroup.class})
    @Null(message = "此接口不能修改密码", groups = {UpdateGroup.class})
    private String password;

    /**
     * 用户邮箱
     */
    @Email(message = "邮箱格式不合法", groups = {AddGroup.class, UpdateGroup.class})
    private String email;

    /**
     * 用户手机号码
     */
    @Length(min = 11, max = 11, message = "手机号码应为11位", groups = {AddGroup.class, UpdateGroup.class})
    private String phone;

    /**
     * 用户微信号
     */
    @Length(min = 6, max = 20, message = "微信号长度应在[6， 20]", groups = {AddGroup.class, UpdateGroup.class})
    private String wechat;

    /**
     * 用户性别（0：女  1：男  2：未知）
     */
    @GenderValue(value = {"0", "1", "2"}, message = "用户性别必须是字符串 0, 1, 2", groups = {AddGroup.class, UpdateGroup.class})
    private String gender;

    /**
     * 用户头像地址
     */
    @URL(message = "头像地址不合法", groups = {AddGroup.class, UpdateGroup.class})
    private String avatar;
//
//    /**
//     * 用户登录时间
//     */
//    private Date loginTime;
//
//    /**
//     * 用户登录IP
//     */
//    private String loginIp;

    /**
     * 用户状态(0:正常  1：停用) @GenderValue注解也能用这
     */
    @GenderValue(value = {"0", "1"}, message = "用户状态必须是字符串 0, 1", groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message = "必须设置用户状态", groups = {AddGroup.class, UpdateGroup.class})
    private String status;

//    /**
//     * 用户删除标识（0：正常  1：删除）
//     */
//    private String delFlag;
//
//    /**
//     * 创建者
//     */
//    private String createBy;
//
//    /**
//     * 用户创建时间
//     */
//    private Date createTime;
//
//    /**
//     * 更新者
//     */
//    @Null(message = "无权设置更新者", groups = {AddGroup.class, UpdateGroup.class})
//    private String updateBy;
//
//    /**
//     * 用户信息更新时间
//     */
//    @Null(message = "无权设置更新时间", groups = {AddGroup.class, UpdateGroup.class})
//    private Date updateTime;

    /**
     * 备注
     */
    @Length(min = 1, max = 50, message = "用户备注应为[1, 50]个字符", groups = {AddGroup.class, UpdateGroup.class})
    private String remark;

    /**
     * 用户角色ID数组
     */
    @Size(min = 1, max = 20, message = "用户角色设置不合理", groups = {AddGroup.class, UpdateGroup.class})
    @NotNull(message = "必须设置用户角色", groups = {AddGroup.class, UpdateGroup.class})
    private Long[] roleIds;
}
