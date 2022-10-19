package cn.y3lq.blog.system.domain;

import cn.y3lq.blog.common.core.valid.AddGroup;
import cn.y3lq.blog.common.core.valid.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 业务菜单
 */
@Data
@EqualsAndHashCode
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 菜单ID
     */
    @NotNull(message = "菜单ID不能为空", groups = {UpdateGroup.class})
    private Long menuId;

    /**
     * 菜单名字
     */
    @NotBlank(message = "菜单名字不能为空", groups = {UpdateGroup.class, AddGroup.class})
    @Size(min = 1, max = 50, message = "菜单名称长度应在[1, 50]", groups = {UpdateGroup.class, AddGroup.class})
    private String name;

    /**
     * 父菜单的名字
     */
    private String parentName;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空", groups = {UpdateGroup.class, AddGroup.class})
    private Integer orderNum;

    /**
     * 路由地址
     */
    @Size(min = 1, max = 100, message = "路由地址长度应在[1, 100]", groups = {UpdateGroup.class, AddGroup.class})
    private String path;

    /**
     * 组件路径
     */
    @Size(min = 1, max = 100, message = "组件路径长度应在[1, 100]", groups = {UpdateGroup.class, AddGroup.class})
    private String component;

    /**
     * 路由参数
     */
    private String query;

    /**
     * 是否外链（0：是  1：否）
     */
    private String isFrame;

    /**
     * 是否缓存（0：是  1：否）
     */
    private String isCache;

    /**
     * 类型（M:目录  C:菜单  F:按钮）
     */
    @NotBlank(message = "菜单类型不能为空", groups = {UpdateGroup.class, AddGroup.class})
    private String menuType;

    /**
     * 显示状态（0：显示  1：隐藏）
     */
    private String visible;

    /**
     * 菜单状态（0：显示  1：隐藏）
     */
    private String status;

    /**
     * 权限字符串
     */
    @Size(min = 1, max = 100, message = "权限字符长度应在[1 ,100]", groups = {UpdateGroup.class, AddGroup.class})
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 子菜单
     */
    private List<Menu> children = new ArrayList<>();

    /**
     * 备注
     */
    @Size(min = 1, max = 50, message = "备注字符长度应在[1 ,50]", groups = {UpdateGroup.class, AddGroup.class})
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;


}
