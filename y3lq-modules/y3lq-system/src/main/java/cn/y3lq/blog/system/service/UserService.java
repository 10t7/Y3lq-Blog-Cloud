package cn.y3lq.blog.system.service;

import cn.y3lq.blog.common.core.domain.User;
import cn.y3lq.blog.system.entity.RoleEntity;
import cn.y3lq.blog.system.entity.UserEntity;
import cn.y3lq.blog.system.entity.UserSettingEntity;
import cn.y3lq.blog.system.vo.ResetPwdVO;
import cn.y3lq.blog.system.vo.UserInfoVO;
import cn.y3lq.blog.system.vo.UserListVO;
import cn.y3lq.blog.system.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 用户业务层
 */
public interface UserService {

    /**
     * 根据用户名找到 User
     */
    User getUserByUsername(String username);

    /**
     * 根据封装条件查询符合条件的所有用户
     *
     * @param userEntity 封装条件
     * @return List<UserEntity>
     */
    List<UserListVO> selectUserList(UserEntity userEntity);

    /**
     * 查看用户名、手机号、邮箱等是否唯一
     */
    void checkParameterUnique(UserVO userVO);

    /**
     * 查看角色信息是否被允许
     */
    void checkRoleAllowed(UserVO userVO, HttpServletRequest request);

    /**
     * 添加用户
     */
    void insertUser(UserVO userVO, HttpServletRequest request);

    /**
     * 查看是否有权限修改管理员角色
     */
    void checkEditOtherAdmin(UserVO userVO, HttpServletRequest request);

    /**
     * 修改用户信息
     */
    void updateUser(UserVO userVO, HttpServletRequest request);

    /**
     * 查看部分用户信息是否唯一
     */
    void checkUpdateParameterUnique(UserVO userVO);

    /**
     * 查看是否有权删除管理员
     *
     * @param userIds 被删除的用户ID数组
     */
    void checkDeleteAllowed(Long[] userIds, HttpServletRequest request);

    /**
     * 删除用户
     *
     * @param userIds 准备被删除的用户ID数组
     */
    void deleteUser(Long[] userIds);

    /**
     * 查看修改密码是否被允许
     */
    void checkResetPwdAllowed(ResetPwdVO userEntity, HttpServletRequest request);

    /**
     * 修改密码
     */
    void resetPwd(Long userId, String password);

    /**
     * 检查UserID和Status是否为空
     */
    void checkUserIdStatus(UserVO userVO);

    /**
     * 修改用户的状态 status
     */
    void updateUserStatus(Long userId, String status, HttpServletRequest request);

    /**
     * 获取当前用户的信息
     */
    UserInfoVO info(Long currentUserId);

    /**
     * 找到没有分配该角色的所有用户
     *
     * @param userVO userVO.getRoleIds
     * @return
     */
    List<UserEntity> selectUnAssignedUserList(UserVO userVO);

    /**
     * 拿到管理员可以设置的角色列表
     */
    List<RoleEntity> roleAdminCanSet(HttpServletRequest request);

    /**
     * 查询已经分配该角色的用户列表
     */
    List<UserEntity> selectAssignedUserList(UserVO userVO);

    /**
     * 前端博客获取指定用户个人基础信息
     */
    UserInfoVO blogUserInfo(Long userId);


    /**
     * 注册用户
     * @return
     */
    Long registered(UserEntity userEntity);
}


