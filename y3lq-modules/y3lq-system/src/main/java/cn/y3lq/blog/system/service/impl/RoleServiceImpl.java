package cn.y3lq.blog.system.service.impl;

import cn.y3lq.blog.common.core.constant.UserRoleConstants;
import cn.y3lq.blog.common.core.exception.ServiceException;
import cn.y3lq.blog.common.core.util.SecurityUtils;
import cn.y3lq.blog.system.entity.RoleEntity;
import cn.y3lq.blog.system.entity.UserRoleEntity;
import cn.y3lq.blog.system.mapper.MenuMapper;
import cn.y3lq.blog.system.mapper.RoleMapper;
import cn.y3lq.blog.system.mapper.RoleMenuMapper;
import cn.y3lq.blog.system.mapper.UserRoleMapper;
import cn.y3lq.blog.system.service.RoleService;
import cn.y3lq.blog.system.vo.RoleListVO;
import cn.y3lq.blog.system.vo.RoleVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: Y3lq
 * @description: 角色业务层
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 根据用户ID找到该用户所有的角色key
     */
    @Override
    public Set<String> getRoleKeys(Long userId) {
        return roleMapper.getRoleKeys(userId);
    }

    /**
     * 条件查询角色列表
     */
    @Override
    public List<RoleListVO> selectRoleList(RoleEntity roleEntity) {
        return roleMapper.selectRoleList(roleEntity);
    }

    /**
     * 查看新增Role 其中参数是否唯一
     */
    @Override
    public void checkRoleParameterUnique(RoleVO role) {
        // 检查角色名字是否唯一
        int nameCount = roleMapper.countRoleName(role.getName());
        if (nameCount > 0) {
            throw new ServiceException("该名字已经存在");
        }
        // 检查角色字符串key是否唯一
        int keyCount = roleMapper.countRoleKey(role.getRoleKey());
        if (keyCount > 0) {
            throw new ServiceException("该角色权限字符串已经存在");
        }
    }

    /**
     * 新增角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertRole(RoleVO role, HttpServletRequest request) {
        RoleEntity roleEntity = new RoleEntity();
        BeanUtils.copyProperties(role, roleEntity);
        // 完善新增Role的参数
        initRoleEntity(roleEntity, request);
        // 新增角色
        roleMapper.insertRole(roleEntity);
        // 新增角色跟菜单关联
        roleMenuMapper.insertRoleMenu(roleEntity.getRoleId(), role.getMenuIds());
    }

    /**
     * 检查修改角色 相关参数是否唯一
     */
    @Override
    public void checkUpdateRoleParameterUnique(RoleVO role) {
        RoleEntity roleEntity = roleMapper.getRoleByRoleId(role.getRoleId());
        // 检查角色名字是否唯一
        if (!role.getName().equals(roleEntity.getName())) {
            int count = roleMapper.countRoleName(role.getName());
            if (count > 0) {
                throw new ServiceException("角色名字已存在");
            }
        }
        //检查roleKey是否唯一
        if (!role.getRoleKey().equals(roleEntity.getRoleKey())) {
            int count = roleMapper.countRoleKey(role.getRoleKey());
            if (count > 0) {
                throw new ServiceException("角色权限字符串已存在");
            }
        }
    }

    /**
     * 修改角色信息
     */
    @Override
    public void updateRole(RoleVO role, HttpServletRequest request) {
        RoleEntity roleEntity = new RoleEntity();
        BeanUtils.copyProperties(role, roleEntity);
        initRoleEntity(roleEntity, request);
        // 修改角色信息
        roleMapper.updateRole(roleEntity);
        // 删除角色和菜单的所有关联
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        // 新增角色和菜单的关联
        roleMenuMapper.insertRoleMenu(role.getRoleId(), role.getMenuIds());

    }

    /**
     * 删除角色
     *
     * @param roleIds 角色ID数组
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleByIds(Long[] roleIds) {
        // 查看该角色是否还跟用户绑定,
        for (Long roleId : roleIds) {
            if (SecurityUtils.isSuperRole(roleId)) {
                throw new ServiceException("无权删除超级管理员");
            }
            RoleEntity roleEntity = roleMapper.getRoleByRoleId(roleId);
            if (ObjectUtils.isEmpty(roleEntity)) {
                throw new ServiceException(roleId + "--角色ID不存在");
            }
            int count = userRoleMapper.countUserRoleByRoleId(roleId);
            if (count > 0) {
                throw new ServiceException(roleEntity.getName() + "该角色还绑定用户，请先解除所有用户绑定");
            }
        }
        // 删除角色
        roleMapper.deleteRole(roleIds);
        // 删除角色跟菜单关联
        roleMenuMapper.deleteRoleMenuByRoleIds(roleIds);
    }

    /**
     * 根据角色ID获取角色信息
     */
    @Override
    public RoleVO getRoleByRoleId(Long roleId) {
        if (UserRoleConstants.SUPER_ADMIN_ROLE_ID.equals(roleId)) {
            RoleVO superAdminInfo = roleMapper.getRoleInfoByRoleId(roleId);
            Long[] menuIds = menuMapper.getAllMenuIds();
            superAdminInfo.setMenuIds(menuIds);
            return superAdminInfo;
        }
        return roleMapper.getRoleInfoByRoleId(roleId);
    }

    /**
     * 查看修改角色状态是否允许
     */
    @Override
    public void checkChangeStatusAllowed(RoleEntity roleEntity, HttpServletRequest request) {
        if (roleEntity.getRoleId() == null || roleEntity.getRoleId() == 0) {
            throw new ServiceException("角色ID不能为空");
        }
        if (StringUtils.isEmpty(roleEntity.getStatus())) {
            throw new ServiceException("角色需要修改状态不能为空");
        }
        if (SecurityUtils.isSuperRole(roleEntity.getRoleId())) {
            throw new ServiceException("无权修改创建管理员");
        }
    }

    /**
     * 修改角色状态
     */
    @Override
    public void updateRoleStatus(RoleEntity roleEntity, HttpServletRequest request) {
        roleEntity.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId(request)));
        roleMapper.updateRole(roleEntity);
    }

    /**
     * 取消授权给用户
     *
     * @param roleId  角色ID
     * @param userIds 用户ID数组
     */
    @Override
    public void cancelAuthToUser(Long roleId, Long[] userIds) {
        userRoleMapper.deleteUserRole(roleId, userIds);
    }

    /**
     * 批量授权给用户
     */
    @Override
    public void authRoleToUser(Long roleId, Long[] userIds) {
        userRoleMapper.insertRoleToUsers(roleId, userIds);
    }

    /**
     * 查看批量授权给用户是否允许
     */
    @Override
    public void checkAuthRoleToUserAllowed(Long roleId, Long[] userIds, HttpServletRequest request) {
        if (SecurityUtils.isSuperAdmin(request)) {
            // 超级管理员放行
            return;
        }
        if (SecurityUtils.isSuperRole(roleId)) {
            throw new ServiceException("无权授权超级管理员角色给用户");
        }

    }

    /**
     * 查看取消授权是否允许
     */
    @Override
    public void checkCancelAuthToUserAllowed(Long roleId, Long[] userIds, HttpServletRequest request) {
        if (SecurityUtils.isSuperAdmin(request)) {
            // 超级管理员放行
            return;
        }
        if (SecurityUtils.isSuperRole(roleId)) {
            throw new ServiceException("无权撤销超级管理员角色的用户");
        }
        if (SecurityUtils.isHasSuperAdminUserId(userIds)) {
            throw new ServiceException("无权操作超级管理员角色");
        }
    }

    /**
     * 给注册用户分配博客用户权限
     */
    @Override
    public void assignRoleToBlogUser(Long userId) {
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setUserId(userId);
        userRoleEntity.setRoleId(UserRoleConstants.BLOG_USER_ROLE_ID);
        List<UserRoleEntity> userRoleEntities = new ArrayList<>();
        userRoleEntities.add(userRoleEntity);
        userRoleMapper.insertUserRole(userRoleEntities);
    }

    /**
     * 完善roleEntity参数,设置创建者或者更新者等
     */
    private void initRoleEntity(RoleEntity roleEntity, HttpServletRequest request) {
        if (roleEntity.getRoleId() == null) {
            // roleId为空，说明为新增
            roleEntity.setCreateBy(String.valueOf(SecurityUtils.getCurrentUserId(request)));
            roleEntity.setDelFlag("0");
        } else {
            // 为修改
            roleEntity.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId(request)));
        }

    }


}
