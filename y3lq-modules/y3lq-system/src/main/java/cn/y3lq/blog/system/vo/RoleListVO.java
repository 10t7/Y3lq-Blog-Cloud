package cn.y3lq.blog.system.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: Y3lq
 * @description: 返回前端渲染的role信息实体类
 */
@Data
public class RoleListVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色名字
     */
    private String name;

    /**
     * 角色字符串 key
     */
    private String roleKey;

    /**
     * 角色状态（0：正常  1：停用）
     */
    private String status;

    /**
     * 角色排序
     */
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
    private String remark;

    /**
     * 菜单ids
     */
    private Long[] menuIds;

}
