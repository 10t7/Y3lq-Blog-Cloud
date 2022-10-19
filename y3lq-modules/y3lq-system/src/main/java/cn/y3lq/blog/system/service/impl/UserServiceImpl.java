package cn.y3lq.blog.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import cn.y3lq.blog.common.core.constant.Constants;
import cn.y3lq.blog.common.core.constant.UserRoleConstants;
import cn.y3lq.blog.common.core.domain.User;
import cn.y3lq.blog.common.core.exception.ServiceException;
import cn.y3lq.blog.common.core.util.SecurityUtils;
import cn.y3lq.blog.system.entity.RoleEntity;
import cn.y3lq.blog.system.entity.UserEntity;
import cn.y3lq.blog.system.entity.UserRoleEntity;
import cn.y3lq.blog.system.entity.UserSettingEntity;
import cn.y3lq.blog.system.mapper.RoleMapper;
import cn.y3lq.blog.system.mapper.UserMapper;
import cn.y3lq.blog.system.mapper.UserRoleMapper;
import cn.y3lq.blog.system.mapper.UserSettingMapper;
import cn.y3lq.blog.system.service.UserService;
import cn.y3lq.blog.system.vo.ResetPwdVO;
import cn.y3lq.blog.system.vo.UserInfoVO;
import cn.y3lq.blog.system.vo.UserListVO;
import cn.y3lq.blog.system.vo.UserVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: Y3lq
 * @description: 用户业务层
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserSettingMapper userSettingMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据用户名找到 User
     */
    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    /**
     * 根据封装条件查询符合条件的所有用户
     *
     * @param userEntity 封装条件
     * @return List<UserEntity>
     */
    @Override
    public List<UserListVO> selectUserList(UserEntity userEntity) {
        return userMapper.selectUserList(userEntity);

    }

    /**
     * 查看用户名、手机号、邮箱等是否唯一
     */
    @Override
    public void checkParameterUnique(UserVO userVO) {
        // 检查用户名是否唯一
        if (!StringUtils.isEmpty(userVO.getUsername())) {
            int count = userMapper.checkUsernameUnique(userVO.getUsername());
            if (count > 0) {
                throw new ServiceException("该用户名已存在");
            }
        }
        // 检查手机号码是否唯一
        if (!StringUtils.isEmpty(userVO.getPhone())) {
            int count = userMapper.checkPhoneUnique(userVO.getPhone());
            if (count > 0) {
                throw new ServiceException("该手机号码已存在");
            }
        }
        // 检查邮箱是否唯一
        if (!StringUtils.isEmpty(userVO.getEmail())) {
            int count = userMapper.checkEmailUnique(userVO.getEmail());
            if (count > 0) {
                throw new ServiceException("该邮箱已存在");
            }
        }
    }

    /**
     * 查看角色信息是否被允许
     */
    @Override
    public void checkRoleAllowed(UserVO userVO, HttpServletRequest request) {
        if (SecurityUtils.isSuperAdmin(request)) {
            // 超级管理员放行
            return;
        }
        // 角色只能设置为博客用户或者后台访客用户
        Long[] roleIds = userVO.getRoleIds();
        for (Long roleId : roleIds) {
            if (UserRoleConstants.BLOG_USER_ROLE_ID.equals(roleId)) {
                continue;
            }
            if (UserRoleConstants.VISITOR_ROLE_ID.equals(roleId)) {
                continue;
            }
            throw new ServiceException("角色不能设置为管理员");
        }
    }

    /**
     * 添加用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUser(UserVO userVO, HttpServletRequest request) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userVO, userEntity);
        // 初始化 userEntity， 完善其信息
        initUserEntityInfo(userEntity, request);
        // 添加用户
        userMapper.insertUser(userEntity);
        // 关联用户和角色
        insertUserRole(userEntity.getUserId(), userVO.getRoleIds());
    }

    /**
     * 查看是否有权限修改管理员角色
     */
    @Override
    public void checkEditOtherAdmin(UserVO userVO, HttpServletRequest request) {
        Long[] userIds = {userVO.getUserId()};
        if (SecurityUtils.isHasSuperAdminUserId(userIds)) {
            throw new ServiceException("不能修改超级管理员");
        }
        if (SecurityUtils.isSuperAdmin(request)) {
            // 超级管理员放行
            return;
        }
        // 拿到用户原本的角色ID 查看是否为管理员
        Long userId = userVO.getUserId();
        List<Long> roleIds = userRoleMapper.getRoleIds(userId);
        if (CollectionUtil.isEmpty(roleIds)) {
            throw new ServiceException("用户信息不存在");
        }
        if (SecurityUtils.isNormalRole(roleIds)) {
            return;
        }
        throw new ServiceException("无权修改管理员信息，若修改个人信息请去个人中心修改");
    }

    /**
     * 修改用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserVO userVO, HttpServletRequest request) {
        // 删除之前的用户和角色关联
        userRoleMapper.deleteUserRoleByUserId(userVO.getUserId());
        // 新增用户和角色的关联
        insertUserRole(userVO.getUserId(), userVO.getRoleIds());

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userVO, userEntity);
        Long currentUserId = SecurityUtils.getCurrentUserId(request);
        // 设置更新者的ID
        userEntity.setUpdateBy(String.valueOf(currentUserId));
        // 修改用户信息
        userMapper.updateUser(userEntity);
    }

    /**
     * 查看修改用户的个别信息是否唯一
     */
    @Override
    public void checkUpdateParameterUnique(UserVO userVO) {
        UserEntity userEntity = userMapper.getUserUniqueInfoByUserId(userVO.getUserId());
        if (ObjectUtils.isEmpty(userEntity)) {
            throw new ServiceException("该用户不存在");
        }
        // 检查用户名是否唯一
        if (!userVO.getUsername().equals(userEntity.getUsername())) {
            int count = userMapper.checkUsernameUnique(userVO.getUsername());
            if (count > 0) {
                throw new ServiceException("该用户名已存在");
            }
        }
        // 如果手机号码不为空，则检查是否唯一
        if (!StringUtils.isEmpty(userVO.getPhone())) {
            if (!userVO.getPhone().equals(userEntity.getPhone())) {
                int count = userMapper.checkPhoneUnique(userVO.getPhone());
                if (count > 0) {
                    throw new ServiceException("该手机号码已存在");
                }
            }
        }
        // 如果邮箱不为空，则检查是否唯一
        if (!StringUtils.isEmpty(userVO.getEmail())) {
            if (!userVO.getEmail().equals(userEntity.getEmail())) {
                int count = userMapper.checkEmailUnique(userVO.getEmail());
                if (count > 0) {
                    throw new ServiceException("该邮箱已存在");
                }
            }
        }
    }

    /**
     * 查看是否有权删除管理员
     *
     * @param userIds 被删除的用户ID数组
     */
    @Override
    public void checkDeleteAllowed(Long[] userIds, HttpServletRequest request) {
        if (userIds.length == 0) {
            throw new ServiceException("未指定删除的用户");
        }
        if (SecurityUtils.isHasSuperAdminUserId(userIds)) {
            // 超级管理员不能删除，
            throw new ServiceException("超级管理员不能删除");
        }
        if (SecurityUtils.isSuperAdmin(request)) {
            // 超级管理员放行
            return;
        }
        // 拿到准备被删除的用户ID数组的所有roleId
        Set<Long> roleIdSet = userRoleMapper.getRoleIdSet(userIds);
        if (SecurityUtils.isNormalRoleSet(roleIdSet)) {
            // 说明被删除的用户id集合没有包含 管理员
            return;
        }
        throw new ServiceException("无权删除管理员");

    }

    /**
     * 删除用户
     *
     * @param userIds 准备被删除的用户ID数组
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long[] userIds) {
        // 删除用户和角色所有关联
        userRoleMapper.deleteUserRoleByUserIds(userIds);
        // 删除用户
        userMapper.deleteUser(userIds);
    }

    /**
     * 查看修改密码是否被允许
     */
    @Override
    public void checkResetPwdAllowed(ResetPwdVO resetPwdVO, HttpServletRequest request) {
        if (SecurityUtils.isSuperAdmin(request)) {
            // 放行超级管理员
            return;
        }
        // 拿到该用户所有的角色
        List<Long> roleIds = userRoleMapper.getRoleIds(resetPwdVO.getUserId());
        if (SecurityUtils.isNormalRole(roleIds)) {
            // 不包含管理员角色放行
            return;
        }
        if (SecurityUtils.getCurrentUserId(request).equals(resetPwdVO.getUserId())) {
            // 管理员修改自己密码放行
            return;
        }
        throw new ServiceException("无权修改管理员密码");
    }

    /**
     * 修改密码
     */
    @Override
    public void resetPwd(Long userId, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(Constants.BCRYPT_STRENGTH);
        String encode = encoder.encode(password);
        userMapper.updateUserPwd(userId, encode);
    }

    /**
     * 检查UserID和Status是否为空
     */
    @Override
    public void checkUserIdStatus(UserVO userVO) {
        Long userId = userVO.getUserId();
        String status = userVO.getStatus();
        if (userId == null) {
            throw new ServiceException("用户ID不能为空");
        }
        if (status.equals("0")) {
            return;
        }
        if (status.equals("1")) {
            return;
        }
        throw new ServiceException("用户状态只能为字符串0或1");
    }

    /**
     * 修改用户的状态 status
     */
    @Override
    public void updateUserStatus(Long userId, String status, HttpServletRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setStatus(status);
        userEntity.setCreateBy(String.valueOf(SecurityUtils.getCurrentUserId(request)));
        userMapper.updateUser(userEntity);
    }

    /**
     * 获取当前用户的信息
     */
    @Override
    public UserInfoVO info(Long currentUserId) {
        UserInfoVO userInfoVO = userMapper.getUserByUserId(currentUserId);
        List<RoleEntity> roleEntityList = roleMapper.getRoleListByUserId(currentUserId);
        Long[] roleIds = new Long[roleEntityList.size()];
        String[] roles = new String[roleEntityList.size()];
        for (int i = 0; i < roleEntityList.size(); i++) {
            roleIds[i] = roleEntityList.get(i).getRoleId();
            roles[i] = roleEntityList.get(i).getName();
        }
        userInfoVO.setRoleIds(roleIds);
        userInfoVO.setRoles(roles);
        return userInfoVO;
    }



    /**
     * 找到没有分配具体角色的所有用户
     *
     * @param userVO userVO.getRoleIds
     * @return
     */
    @Override
    public List<UserEntity> selectUnAssignedUserList(UserVO userVO) {
        Long[] roleIds = userVO.getRoleIds();
        Long roleId = roleIds[0];
        List<UserEntity> userEntity = userMapper.selectUnAssignedUserList(userVO, roleId);
        return userEntity;
    }

    /**
     * 拿到管理员可以设置的角色列表
     */
    @Override
    public List<RoleEntity> roleAdminCanSet(HttpServletRequest request) {
        List<RoleEntity> roleEntityList = null;
        if (SecurityUtils.isSuperAdmin(request)) {
            // 超级管理员则找到所有未删除的角色
            roleEntityList = roleMapper.getAllRoleList();
        } else {
            // 普通管理员则可拿到 后台访客，博客用户角色
            roleEntityList = roleMapper.getNormalRoleList();
        }
        return roleEntityList;
    }

    /**
     * 查询已经分配该角色的用户列表
     */
    @Override
    public List<UserEntity> selectAssignedUserList(UserVO userVO) {
        Long[] roleIds = userVO.getRoleIds();
        Long roleId = roleIds[0];
        return userMapper.selectAssignedUserList(userVO, roleId);
    }

    /**
     * 前端博客获取指定用户个人基础信息
     */
    @Override
    public UserInfoVO blogUserInfo(Long userId) {
        return userMapper.getUserBasisInfoByUserId(userId);
    }


    /**
     * 注册用户
     *
     * @return
     */
    @Override
    public Long registered(UserEntity userEntity) {
        String email = userEntity.getEmail();
        userEntity.setUsername(email);
        String username = userEntity.getUsername();
        String password = userEntity.getPassword();
        String nickname = userEntity.getNickname();
        int i = userMapper.checkUsernameUnique(username);
        if (i >= 1) {
            throw new ServiceException("用户名重复");
        }
        userEntity.setDelFlag("0");
        userEntity.setStatus("0");
        if (StringUtils.isEmpty(userEntity.getAvatar())) {
            // 用户没有设置头像，则给用户一个默认头像
            userEntity.setAvatar(UserRoleConstants.DEFAULT_AVATAR);
        }
        // 给密码加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(Constants.BCRYPT_STRENGTH);
        String encode = encoder.encode(password);
        userEntity.setPassword(encode);
        // 设置默认备注
        String remark = userEntity.getRemark();
        if (StringUtils.isEmpty(remark)) {
            userEntity.setRemark(UserRoleConstants.DEFAULT_REMARK);
        }
        if (StringUtils.isEmpty(nickname)) {
            String s = UUID.fastUUID().toString();
            String[] split = s.split("-");
            userEntity.setNickname(split[0]);
        }
        userMapper.insertUser(userEntity);
        Long userId = userEntity.getUserId();
        return userId;
    }

    /**
     * 关联用户和角色
     *
     * @param userId  用户id
     * @param roleIds 角色id数组
     */
    private void insertUserRole(Long userId, Long[] roleIds) {
        List<UserRoleEntity> entityList = new ArrayList<>();
        for (Long roleId : roleIds) {
            entityList.add(new UserRoleEntity(userId, roleId));
        }
        // 关联
        userRoleMapper.insertUserRole(entityList);
    }

    /**
     * 初始化添加用户的 userEntity， 完善其信息
     */
    private void initUserEntityInfo(UserEntity userEntity, HttpServletRequest request) {
        String avatar = userEntity.getAvatar();
        if (StringUtils.isEmpty(avatar)) {
            // 用户没有设置头像，则给用户一个默认头像
            userEntity.setAvatar(UserRoleConstants.DEFAULT_AVATAR);
        }
        // 设置删除标识为 '0' 代表未删除
        if (StringUtils.isEmpty(userEntity.getStatus())) {
            userEntity.setDelFlag(UserRoleConstants.USER_STATUS_NORMAL);
        }
        userEntity.setDelFlag(UserRoleConstants.USER_DEL_FLAG_NORMAL);
        Long currentUserId = SecurityUtils.getCurrentUserId(request);
        String createUserId = String.valueOf(currentUserId);
        // 设置创建者的 userId
        userEntity.setCreateBy(createUserId);
        // 给密码加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(Constants.BCRYPT_STRENGTH);
        String password = userEntity.getPassword();
        String encode = encoder.encode(password);
        userEntity.setPassword(encode);
        // 设置默认备注
        String remark = userEntity.getRemark();
        if (StringUtils.isEmpty(remark)) {
            userEntity.setRemark(UserRoleConstants.DEFAULT_REMARK);
        }
    }
}
