package cn.y3lq.blog.system.service;

import cn.y3lq.blog.system.domain.Menu;
import cn.y3lq.blog.system.entity.MenuEntity;
import cn.y3lq.blog.system.vo.RouterVO;
import cn.y3lq.blog.system.vo.TreeSelect;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * @author: Y3lq
 * @description: 菜单信息
 */
public interface MenuService {
    /**
     * 根据用户ID 获取该用户所有菜单权限
     */
    Set<String> getMenuPermissions(Long userId);

    /**
     * 根据用户ID查询该用户拥有的菜单
     *
     * @param userId 当前用户ID
     * @param request
     */
    List<Menu> selectMenuTreeByUserId(Long userId, HttpServletRequest request);

    /**
     * 构建前端需要的菜单
     */
    List<RouterVO> buildMenus(List<Menu> menuList);

    /**
     * 拿到当前用户所有的菜单
     */
    List<Menu> selectMenuList(Menu menu, HttpServletRequest request);

    /**
     * 构建前端需要的下拉树结构
     */
    List<TreeSelect> buildMenuTreeSelect(List<Menu> menuList);

    /**
     * 根据菜单ID查询菜单信息
     */
    Menu selectMenuByMenuId(Long menuId);

    /**
     * 检查新增相关参数是否唯一合理
     */
    void checkAddParameterCorrect(MenuEntity menu);

    /**
     * 新增菜单
     */
    void insertMenu(MenuEntity menu, HttpServletRequest request);

    /**
     * 检查更新菜单参数是否唯一合理
     */
    void checkUpdateParameterCorrect(MenuEntity menu);

    /**
     * 更新菜单
     */
    void updateMenu(MenuEntity menu, HttpServletRequest request);

    /**
     * 查看是否允许删除菜单
     */
    void checkAllowedDelete(Long menuId);

    /**
     * 删除菜单
     */
    void deleteMenuByMenuId(Long menuId);

    /**
     * 获取所有的权限标识
     */
    Set<String> getMenuPermissionsAll();
}
