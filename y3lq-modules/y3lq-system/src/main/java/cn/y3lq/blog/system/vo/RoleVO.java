package cn.y3lq.blog.system.vo;

import cn.y3lq.blog.common.core.valid.AddGroup;
import cn.y3lq.blog.common.core.valid.GenderValue;
import cn.y3lq.blog.common.core.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Y3lq
 * @description: 新增修改角色封装
 */
@Data
public class RoleVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空", groups = UpdateGroup.class)
    @Null(message = "新增不能指定角色ID", groups = AddGroup.class)
    private Long roleId;

    /**
     * 角色名字
     */
    @NotEmpty(message = "角色名字不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(min = 2, max = 7, message = "角色名字应在[2, 7]个字符串", groups = {AddGroup.class, UpdateGroup.class})
    private String name;

    /**
     * 角色字符串 key
     */
    @NotEmpty(message = "角色权限名字不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(min = 2, max = 15, message = "角色权限字符串应在[2, 15]", groups = {AddGroup.class, UpdateGroup.class})
    private String roleKey;

    /**
     * 角色状态（0：正常  1：停用）
     */
    @NotEmpty(message = "角色状态不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @GenderValue(value = {"0", "1"}, message = "角色状态应是字符串 0 1", groups = {AddGroup.class, UpdateGroup.class})
    private String status;

    /**
     * 角色排序
     */
    @NotEmpty(message = "角色排序不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String roleSort;

    /**
     * 角色删除标识
     */
    private String delFlag;

    /**
     * 角色创建者
     */
    private String createBy;

    /**
     * 角色创建时间
     */
    private Date createTime;

    /**
     * 角色更新者
     */
    private String updateBy;

    /**
     * 角色更新时间
     */
    private Date updateTime;

    /**
     * 角色备注
     */
    @Length(min = 1, max = 50, message = "备注内容应在[1 ,50]", groups = {AddGroup.class, UpdateGroup.class})
    private String remark;

    /**
     * 菜单ids
     */
    @NotNull(message = "角色菜单权限不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 1, message = "权限菜单最小为1", groups = {AddGroup.class, UpdateGroup.class})
    private Long[] menuIds;

}
