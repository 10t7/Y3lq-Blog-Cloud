package cn.y3lq.blog.system.mapper;

import cn.y3lq.blog.system.entity.UserRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author: Y3lq
 * @description: 用户和角色关联数据层
 */
@Mapper
public interface UserRoleMapper {
    /**
     * 关联用户和角色
     */
    void insertUserRole(List<UserRoleEntity> entityList);

    /**
     * 拿到该用户的所有角色
     */
    List<Long> getRoleIds(@Param("userId") Long userId);

    /**
     * 删除该用户ID所有的角色关联
     */
    void deleteUserRoleByUserId(@Param("userId") Long userId);

    /**
     * 拿到用户ID数组的所有roleId
     *
     * @param userIds
     * @return
     */
    Set<Long> getRoleIdSet(Long[] userIds);

    /**
     * 删除userId数组所有和角色关联
     *
     * @param userIds
     */
    void deleteUserRoleByUserIds(Long[] userIds);

    /**
     * 计算角色绑定了多少用户
     */
    int countUserRoleByRoleId(@Param("roleId") Long roleId);

    /**
     * 删除用户的角色
     *
     * @param roleId 角色ID
     * @param userIds 用户数组
     */
    void deleteUserRole(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);

    /**
     * 批量授权给用户角色
     * @param roleId 角色ID
     * @param userIds 用户ID数组
     */
    void insertRoleToUsers(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);
}
