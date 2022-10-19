package cn.y3lq.blog.system.mapper;

import cn.y3lq.blog.common.core.domain.User;
import cn.y3lq.blog.system.entity.UserEntity;
import cn.y3lq.blog.system.vo.UserInfoVO;
import cn.y3lq.blog.system.vo.UserListVO;
import cn.y3lq.blog.system.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Y3lq
 * @description: 用户数据层
 */
@Mapper
public interface UserMapper {
    /**
     * 根据用户名找到用户
     */
    User getUserByUsername(@Param("username") String username);

    /**
     * 根据封装条件查询符合条件的所有用户
     *
     * @param userEntity 封装条件
     * @return List<UserEntity>
     */
    List<UserListVO> selectUserList(UserEntity userEntity);

    /**
     * 检查用户名是否唯一
     */
    int checkUsernameUnique(@Param("username") String username);

    /**
     * 检查手机号是否唯一
     */
    int checkPhoneUnique(@Param("phone") String phone);

    /**
     * 检查邮箱是否唯一
     */
    int checkEmailUnique(@Param("email") String email);

    /**
     * 新增用户
     */
    Long insertUser(UserEntity userEntity);

    /**
     * 修改用户信息y
     */
    void updateUser(UserEntity userEntity);

    /**
     * 根据 userId 找到用户
     */
    UserEntity getUserUniqueInfoByUserId(@Param("userId") Long userId);

    /**
     * 删除用户
     */
    void deleteUser(Long[] userIds);

    /**
     * 修改密码
     *
     * @param userId   用户ID
     * @param password 用户密码
     */
    void updateUserPwd(@Param("userId") Long userId, @Param("password") String password);

    /**
     * 根据用户ID获取用户信息
     */
    UserInfoVO getUserByUserId(@Param("userId") Long currentUserId);

    /**
     * 找到没有分配具体角色的所有用户
     */
    List<UserEntity> selectUnAssignedUserList(@Param("userVO") UserVO userVO, @Param("roleId") Long roleId);

    /**
     * 查询已经分配该角色的用户列表
     */
    List<UserEntity> selectAssignedUserList(@Param("userVO") UserVO userVO, @Param("roleId") Long roleId);

    /**
     * 获取用户基础信息
     */
    UserInfoVO getUserBasisInfoByUserId(@Param("userId") Long userId);
}
