package cn.y3lq.blog.system.service;

import cn.y3lq.blog.system.entity.RoleEntity;
import cn.y3lq.blog.system.vo.RoleListVO;
import cn.y3lq.blog.system.vo.RoleVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * @author: Y3lq
 * @description: 角色业务层
 */
public interface RoleService {
    /**
     * 根据用户ID找到该用户所有的角色key
     */
    Set<String> getRoleKeys(Long userId);

    /**
     * 条件查询角色列表
     */
    List<RoleListVO> selectRoleList(RoleEntity roleEntity);

    /**
     * 查看新增Role 其中参数是否唯一
     */
    void checkRoleParameterUnique(RoleVO role);

    /**
     * 新增角色
     */
    void insertRole(RoleVO role, HttpServletRequest request);

    /**
     * 检查修改角色 相关参数是否唯一
     */
    void checkUpdateRoleParameterUnique(RoleVO role);

    /**
     * 修改角色信息
     */
    void updateRole(RoleVO role, HttpServletRequest request);

    /**
     * 删除角色
     *
     * @param roleIds 角色ID数组
     */
    void deleteRoleByIds(Long[] roleIds);

    /**
     * 根据角色ID获取角色
     */
    RoleVO getRoleByRoleId(Long roleId);

    /**
     * 查看修改角色状态是否允许
     */
    void checkChangeStatusAllowed(RoleEntity roleEntity, HttpServletRequest request);

    /**
     * 修改角色状态
     */
    void updateRoleStatus(RoleEntity roleEntity, HttpServletRequest request);

    /**
     * 取消授权给用户
     */
    void cancelAuthToUser(Long roleId, Long[] userIds);

    /**
     * 批量授权给用户
     */
    void authRoleToUser(Long roleId, Long[] userIds);

    /**
     * 查看批量授权给用户是否允许
     */
    void checkAuthRoleToUserAllowed(Long roleId, Long[] userIds, HttpServletRequest request);

    /**
     * 查看取消授权是否允许
     */
    void checkCancelAuthToUserAllowed(Long roleId, Long[] userIds, HttpServletRequest request);

    /**
     * 给注册用户分配博客用户权限
     */
    void assignRoleToBlogUser(Long userId);
}
