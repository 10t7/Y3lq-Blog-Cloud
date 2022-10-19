package cn.y3lq.blog.system.controller;

import cn.y3lq.blog.common.core.domain.AjaxResult;
import cn.y3lq.blog.common.core.valid.AddGroup;
import cn.y3lq.blog.common.core.valid.UpdateGroup;
import cn.y3lq.blog.common.security.annotation.HasPermissions;
import cn.y3lq.blog.common.security.annotation.ProhibitGuestAccess;
import cn.y3lq.blog.system.entity.RoleEntity;
import cn.y3lq.blog.system.entity.UserEntity;
import cn.y3lq.blog.system.service.RoleService;
import cn.y3lq.blog.system.service.UserService;
import cn.y3lq.blog.system.vo.RoleListVO;
import cn.y3lq.blog.system.vo.RoleVO;
import cn.y3lq.blog.system.vo.UserVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 角色控制器
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    /**
     * 批量授权给用户
     */
    @PutMapping("/auth-role-user/{userIds}")
    @HasPermissions("authority:role:edit")
    @ProhibitGuestAccess
    public AjaxResult authRoleToUser(Long roleId,@PathVariable("userIds") Long[] userIds, HttpServletRequest request) {
        roleService.checkAuthRoleToUserAllowed(roleId, userIds, request);
        roleService.authRoleToUser(roleId, userIds);
        return AjaxResult.success();
    }

    /**
     * 批量取消授权给用户
     */
    @HasPermissions("authority:role:edit")
    @PutMapping("/cancel-auth-user/{userIds}")
    @ProhibitGuestAccess
    public AjaxResult cancelAuthToUser(Long roleId, @PathVariable("userIds") Long[] userIds, HttpServletRequest request) {
        roleService.checkCancelAuthToUserAllowed(roleId, userIds, request);
        roleService.cancelAuthToUser(roleId, userIds);
        return AjaxResult.success();
    }

    /**
     * 修改角色状态
     */
    @HasPermissions("authority:role:edit")
    @PutMapping("/change-status")
    @ProhibitGuestAccess
    public AjaxResult changeStatus(RoleEntity roleEntity, HttpServletRequest request) {
        // 查看修改角色状态是否允许
        roleService.checkChangeStatusAllowed(roleEntity, request);
        roleService.updateRoleStatus(roleEntity, request);
        return AjaxResult.success();
    }

    @HasPermissions("authority:role:query")
    @GetMapping("/info/{roleId}")
    public AjaxResult info(@PathVariable Long roleId) {
        RoleVO roleEntity = roleService.getRoleByRoleId(roleId);
        return AjaxResult.success(roleEntity);
    }

    /**
     * 条件查询已经分配角色的用户
     *
     * @param userVO   封装的用户信息
     * @param pageNum  第几页
     * @param pageSize 一页显示个数
     * @return AjaxResult
     */
    @HasPermissions("authority:role:list")
    @GetMapping("/assign-user-list")
    public AjaxResult assignedUserList(UserVO userVO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserEntity> userEntities = userService.selectAssignedUserList(userVO);
        PageInfo<UserEntity> pageInfo = new PageInfo<>(userEntities);
        return AjaxResult.success(pageInfo);
    }

    /**
     * 条件查询拿到 没有分配该角色 的用户
     *
     * @param userVO   封装的用户信息
     * @param pageNum  第几页
     * @param pageSize 一页显示个数
     * @return AjaxResult
     */
    @HasPermissions("authority:role:list")
    @GetMapping("/unassigned-user-list")
    public AjaxResult unAssignedUserList(UserVO userVO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserEntity> userList = userService.selectUnAssignedUserList(userVO);
        PageInfo<UserEntity> pageInfo = new PageInfo<UserEntity>(userList);
        return AjaxResult.success(pageInfo);
    }

    /**
     * 删除角色
     */
    @HasPermissions("authority:role:delete")
    @DeleteMapping("/{roleIds}")
    @ProhibitGuestAccess
    public AjaxResult delete(@PathVariable("roleIds") Long[] roleIds) {
        roleService.deleteRoleByIds(roleIds);
        return AjaxResult.success("删除成功");
    }

    /**
     * 分页条件查询角色列表
     */
    @HasPermissions("authority:role:list")
    @GetMapping("/list")
    public AjaxResult list(RoleEntity roleEntity, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<RoleListVO> roleList = roleService.selectRoleList(roleEntity);
        PageInfo<RoleListVO> pageInfo = new PageInfo<>(roleList);
        return AjaxResult.success("分页获取角色数据成功", pageInfo);
    }

    /**
     * 新增角色
     *
     * @param role 封装新增属性
     */
    @HasPermissions("authority:role:add")
    @PostMapping
    public AjaxResult add(@Validated(AddGroup.class) @RequestBody RoleVO role, HttpServletRequest request) {
        // 检查参数是否唯一
        roleService.checkRoleParameterUnique(role);
        // 新增角色
        roleService.insertRole(role, request);
        return AjaxResult.success("添加成功");
    }

    /**
     * 修改角色
     *
     * @param role 封装修改属性
     */
    @HasPermissions("authority:role:edit")
    @PutMapping
    @ProhibitGuestAccess
    public AjaxResult edit(@Validated(UpdateGroup.class) @RequestBody RoleVO role, HttpServletRequest request) {
        // 检查修改参数是否唯一
        roleService.checkUpdateRoleParameterUnique(role);
        // 修改角色
        roleService.updateRole(role, request);
        return AjaxResult.success("修改成功");
    }
}
