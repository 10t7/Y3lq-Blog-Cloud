package cn.y3lq.blog.system.mapper;

import cn.y3lq.blog.system.entity.RoleEntity;
import cn.y3lq.blog.system.vo.RoleListVO;
import cn.y3lq.blog.system.vo.RoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author: Y3lq
 * @description: 角色数据层
 */
@Mapper
public interface RoleMapper {
    /**
     * 根据用户ID 找到该用户所有角色Key
     */
    Set<String> getRoleKeys(@Param("userId") Long userId);


    /**
     * 条件查询角色列表
     */
    List<RoleListVO> selectRoleList(RoleEntity roleEntity);

    /**
     * 根据roleID找到 Role
     */
    RoleEntity getRoleByRoleId(@Param("roleId") Long roleId);

    /**
     * 查看role name存在的个数
     *
     * @param name 角色名字
     */
    int countRoleName(@Param("name") String name);

    /**
     * 查看role_key存在的个数
     *
     * @param roleKey 角色字符串
     */
    int countRoleKey(@Param("roleKey") String roleKey);

    /**
     * 新增角色
     */
    void insertRole(RoleEntity roleEntity);

    /**
     * 修改角色信息
     */
    void updateRole(RoleEntity roleEntity);

    /**
     * 根据ID数组删除ROLE
     */
    void deleteRole(@Param("roleIds") Long[] roleIds);

    /**
     * 根据用户id找到所有Role
     */
    List<RoleEntity> getRoleListByUserId(@Param("userId") Long currentUserId);

    /**
     * 拿到所有角色列表
     */
    List<RoleEntity> getAllRoleList();

    /**
     * 拿到普通用户角色和后台访客角色
     */
    List<RoleEntity> getNormalRoleList();

    /**
     * 根据角色ID获取角色信息
     */
    RoleVO getRoleInfoByRoleId(@Param("roleId") Long roleId);
}
