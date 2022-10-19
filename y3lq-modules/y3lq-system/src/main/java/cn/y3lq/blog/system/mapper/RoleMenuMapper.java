package cn.y3lq.blog.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: Y3lq
 * @description: 角色和菜单关联
 */
@Mapper
public interface RoleMenuMapper {
    /**
     * 新增角色和菜单的关联
     *
     * @param roleId  角色ID
     * @param menuIds 菜单数组
     */
    void insertRoleMenu(@Param("roleId") Long roleId, @Param("menuIds") Long[] menuIds);

    /**
     * 根据roleId 删除角色和菜单的关联
     */
    void deleteRoleMenuByRoleId(@Param("roleId") Long roleId);

    /**
     * 删除角色和菜单的关联
     *
     * @param roleIds 角色ID数组
     */
    void deleteRoleMenuByRoleIds(@Param("roleIds") Long[] roleIds);

    /**
     * 查看该菜单有无被分配给角色
     */
    int countMenuAssociation(@Param("menuId") Long menuId);
}
