package cn.y3lq.blog.system.controller;

import cn.y3lq.blog.api.auth.RemoteAuthService;
import cn.y3lq.blog.common.core.constant.TokenConstants;
import cn.y3lq.blog.common.core.domain.AjaxResult;
import cn.y3lq.blog.common.core.domain.R;
import cn.y3lq.blog.common.core.domain.User;
import cn.y3lq.blog.common.core.util.SecurityUtils;
import cn.y3lq.blog.common.core.valid.AddGroup;
import cn.y3lq.blog.common.core.valid.UpdateGroup;
import cn.y3lq.blog.common.security.annotation.HasPermissions;
import cn.y3lq.blog.common.security.annotation.ProhibitGuestAccess;
import cn.y3lq.blog.system.entity.RoleEntity;
import cn.y3lq.blog.system.entity.UserEntity;
import cn.y3lq.blog.system.service.MenuService;
import cn.y3lq.blog.system.service.RoleService;
import cn.y3lq.blog.system.service.UserService;
import cn.y3lq.blog.system.vo.ResetPwdVO;
import cn.y3lq.blog.system.vo.UserInfoVO;
import cn.y3lq.blog.system.vo.UserListVO;
import cn.y3lq.blog.system.vo.UserVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ObjectUtils;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: Y3lq
 * @description: 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RemoteAuthService remoteAuthService;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 注册用户
     */
    @PostMapping("/registered")
    public AjaxResult registeredUser(@Validated @RequestBody UserEntity userEntity) {
        Long userId = userService.registered(userEntity);
        roleService.assignRoleToBlogUser(userId);
        return AjaxResult.success("注册成功");
    }


    /**
     * 前端博客获取指定用户个人基础信息
     */
    @GetMapping("/blog-user-info")
    public AjaxResult blogUserInfo(@RequestParam("userId") Long userId) {
        UserInfoVO userInfoVO = userService.blogUserInfo(userId);
        return AjaxResult.success(userInfoVO);
    }

    /**
     * 拿到管理员可以设置的角色列表
     */
    @HasPermissions("authority:user:list")
    @GetMapping("/role-admin-can-set")
    public AjaxResult roleAdminCanSet(HttpServletRequest request) {
        List<RoleEntity> roleList = userService.roleAdminCanSet(request);
        return AjaxResult.success(roleList);
    }


    /**
     * 修改用户状态
     */
    @HasPermissions("authority:user:edit")
    @PutMapping("/change-status")
    @ProhibitGuestAccess
    public AjaxResult changeStatus(UserVO userVO, HttpServletRequest request) {
        // 判断userId和status是否为空
        userService.checkUserIdStatus(userVO);
        // 查看是否有权限修改
        userService.checkEditOtherAdmin(userVO, request);
        // 修改用户状态
        userService.updateUserStatus(userVO.getUserId(), userVO.getStatus(), request);
        // 如果状态为1 则删除用户登录凭证
        if (userVO.getStatus().equals('1')) {
            List<String> userIds = new ArrayList<>();
            userIds.add(String.valueOf(userVO.getUserId()));
            remoteAuthService.logoutUserByIds(userIds, request.getHeader(TokenConstants.TOKEN));
        }
        return AjaxResult.success("状态修改成功");
    }


    /**
     * 重置密码
     */
    @HasPermissions("authority:user:resetpwd")
    @PutMapping("/reset-pwd")
    @ProhibitGuestAccess
    public AjaxResult resetPwd(@Validated @RequestBody ResetPwdVO resetPwdVO, HttpServletRequest request) {
        // 查看修改密码是否被允许（如：修改其他管理员密码）
        userService.checkResetPwdAllowed(resetPwdVO, request);
        // 修改密码
        userService.resetPwd(resetPwdVO.getUserId(), resetPwdVO.getPassword());
        List<String> userIdList = new ArrayList<>();
        userIdList.add(String.valueOf(resetPwdVO.getUserId()));
        // 删除登录凭证
        remoteAuthService.logoutUserByIds(userIdList, request.getHeader(TokenConstants.TOKEN));
        return AjaxResult.success("修改密码成功");
    }

    /**
     * 删除用户
     *
     * @param userIds 用户ID数组
     */
    @HasPermissions("authority:user:delete")
    @DeleteMapping("/{userIds}")
    public AjaxResult delete(@PathVariable Long[] userIds, HttpServletRequest request) {
        // 查看是否有权删除管理员
        userService.checkDeleteAllowed(userIds, request);
        // 执行删除用户
        userService.deleteUser(userIds);
        List<String> userIdList = new ArrayList<>();
        for (Long userId : userIds) {
            userIdList.add(String.valueOf(userId));
        }
        // 删除登录凭证
        remoteAuthService.logoutUserByIds(userIdList, request.getHeader(TokenConstants.TOKEN));
        return AjaxResult.success("删除成功");
    }

    /**
     * 修改用户信息
     *
     * @param userVO 封装了修改用户的信息
     */
    @HasPermissions("authority:user:edit")
    @PutMapping
    @ProhibitGuestAccess
    public AjaxResult edit(@Validated(UpdateGroup.class) @RequestBody UserVO userVO, HttpServletRequest request) {
        String lockName = "updateUserLock";
        RLock lock = redissonClient.getLock(lockName);
        lock.lock();
        try {
            // 查看用户名、手机号、邮箱等是否唯一
            userService.checkUpdateParameterUnique(userVO);
            // 查看是否有权限修改管理员信息
            userService.checkEditOtherAdmin(userVO, request);
            // 查看角色信息是否被允许（如：普通用户角色设置为超级管理员或者其他管理员角色）
            userService.checkRoleAllowed(userVO, request);
            // 添加用户
            userService.updateUser(userVO, request);
        } finally {
            lock.unlock();
        }
        return AjaxResult.success("修改用户信息成功");
    }

    /**
     * 添加用户
     *
     * @param userVO 封装了添加用户的信息
     */
    @HasPermissions("authority:user:add")
    @PostMapping
    public AjaxResult add(@Validated(AddGroup.class) @RequestBody UserVO userVO, HttpServletRequest request) {
        String lockName = "insertUserLock";
        RLock lock = redissonClient.getLock(lockName);
        lock.lock();
        try {
            // 查看用户名、手机号、邮箱等是否唯一
            userService.checkParameterUnique(userVO);
            // 查看角色是否被允许（如：角色设置为超级管理员或者其他管理员角色）
            userService.checkRoleAllowed(userVO, request);
            // 添加用户
            userService.insertUser(userVO, request);
        } finally {
            lock.unlock();
        }

        return AjaxResult.success("添加用户成功");
    }


    /**
     * 查询用户列表
     *
     * @param userEntity 前端传过来的查询参数封装实体类
     * @param pageNum    页码
     * @param pageSize   一页需要展示多少条
     * @return AjaxResult
     */
    @HasPermissions("authority:user:list")
    @GetMapping("/list")
    public AjaxResult list(UserEntity userEntity, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserListVO> userList = userService.selectUserList(userEntity);
        PageInfo<UserListVO> pageInfo = new PageInfo<UserListVO>(userList);
        return AjaxResult.success("分页获取用户数据成功", pageInfo);
    }


    /**
     * 根据用户名找到User
     *
     * @param username 用户名
     */
    @GetMapping("/info/{username}")
    public R<User> findUserByUsername(@PathVariable("username") String username) {
        User user = userService.getUserByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            return R.fail("用户名或密码错误");
        }
        // 获取该用户所有角色字符串
        Set<String> roleKeys = roleService.getRoleKeys(user.getUserId());
        // 获取该用户所有的菜单权限
        Set<String> permissions = menuService.getMenuPermissions(user.getUserId());
        user.setPermissions(permissions);
        user.setRoleKeys(roleKeys);
        return R.ok(user, "根据用户名获取用户成功");
    }

    /**
     * 获取当前用户的信息
     */
    @GetMapping("/info")
    public AjaxResult info(HttpServletRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId(request);
        UserInfoVO userInfoVO = userService.info(currentUserId);
        Set<String> menuPermissions = null;
        if (SecurityUtils.isSuperAdmin(request)) {
            // 超级管理员获取所有的权限标识
            menuPermissions = menuService.getMenuPermissionsAll();
        } else {
            menuPermissions = menuService.getMenuPermissions(currentUserId);
        }

        userInfoVO.setPerms(menuPermissions);
        return AjaxResult.success("获取用户信息成功", userInfoVO);
    }

    /**
     * 根据用户ID获取用户信息
     */
    @HasPermissions("authority:user:query")
    @GetMapping("/infoByUserId/{userId}")
    public AjaxResult getUserInfoByUserId(@PathVariable("userId") Long userId) {
        UserInfoVO userInfoVO = userService.info(userId);
        return AjaxResult.success("获取指定用户信息成功", userInfoVO);
    }
}
