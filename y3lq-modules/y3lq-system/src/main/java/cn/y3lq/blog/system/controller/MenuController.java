package cn.y3lq.blog.system.controller;

import cn.y3lq.blog.common.core.domain.AjaxResult;
import cn.y3lq.blog.common.core.util.SecurityUtils;
import cn.y3lq.blog.common.core.valid.AddGroup;
import cn.y3lq.blog.common.core.valid.UpdateGroup;
import cn.y3lq.blog.common.security.annotation.HasPermissions;
import cn.y3lq.blog.common.security.annotation.ProhibitGuestAccess;
import cn.y3lq.blog.system.domain.Menu;
import cn.y3lq.blog.system.entity.MenuEntity;
import cn.y3lq.blog.system.service.MenuService;
import cn.y3lq.blog.system.vo.RouterVO;
import cn.y3lq.blog.system.vo.TreeSelect;
import com.alibaba.nacos.api.naming.pojo.healthcheck.impl.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 菜单控制器
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 删除菜单
     */
    @HasPermissions("authority:menu:delete")
    @DeleteMapping("/{menuId}")
    public AjaxResult delete(@PathVariable("menuId") Long menuId) {
        // 查看是否允许删除此菜单
        menuService.checkAllowedDelete(menuId);
        // 删除菜单
        menuService.deleteMenuByMenuId(menuId);
        return AjaxResult.success();
    }


    /**
     * 修改菜单
     */
    @HasPermissions("authority:menu:edit")
    @PutMapping()
    @ProhibitGuestAccess
    public AjaxResult edit(@Validated(UpdateGroup.class) @RequestBody MenuEntity menu, HttpServletRequest request) {
        // 检查更新菜单参数是否唯一合理
        menuService.checkUpdateParameterCorrect(menu);
        menuService.updateMenu(menu, request);
        return AjaxResult.success();
    }


    /**
     * 新增菜单
     */
    @HasPermissions("authority:menu:add")
    @PostMapping()
    @ProhibitGuestAccess
    public AjaxResult add(@Validated(AddGroup.class) @RequestBody MenuEntity menu, HttpServletRequest request) {
        // 检查新增菜单相关参数是否唯一合理
        menuService.checkAddParameterCorrect(menu);
        // 新增菜单
        menuService.insertMenu(menu, request);
        return AjaxResult.success();
    }


    /**
     * 根据菜单ID查询菜单具体信息
     */
    @HasPermissions("authority:menu:query")
    @GetMapping("/info/{menuId}")
    public AjaxResult info(@PathVariable("menuId") Long menuId) {
        Menu menu = menuService.selectMenuByMenuId(menuId);
        return AjaxResult.success(menu);
    }


    /**
     * 查询系统菜单列表下拉树
     */
    @GetMapping("/tree-select")
    public AjaxResult treeSelect(Menu menu, HttpServletRequest request) {
        // 拿到当前用户所有的菜单
        List<Menu> menuList = menuService.selectMenuList(menu, request);
        // 构建前端需要的数据
        List<TreeSelect> treeSelects = menuService.buildMenuTreeSelect(menuList);
        return AjaxResult.success(treeSelects);
    }

    /**
     * 获取菜单列表
     */
    @HasPermissions("authority:menu:list")
    @GetMapping("/list")
    public AjaxResult list(Menu menu, HttpServletRequest request) {
        // 获取当前用户所拥有的所有菜单
        List<Menu> menuList = menuService.selectMenuList(menu, request);
        return AjaxResult.success("获取菜单列表成功", menuList);
    }

    /**
     * 获取路由信息
     */
    @HasPermissions("authority:menu:list")
    @GetMapping("/get-routers")
    public AjaxResult getRouters(HttpServletRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId(request);
        // 根据ID找到该用户拥有的菜单
        List<Menu> menuList = menuService.selectMenuTreeByUserId(currentUserId, request);
        // 构建前端需要的菜单
        List<RouterVO> menus = menuService.buildMenus(menuList);
        return AjaxResult.success("获取当前用户菜单成功", menus);
    }


}
