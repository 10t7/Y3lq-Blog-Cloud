package cn.y3lq.blog.system.mapper;

import cn.y3lq.blog.system.domain.Menu;
import cn.y3lq.blog.system.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author: Y3lq
 * @description: 菜单信息数据层
 */
@Mapper
public interface MenuMapper {

    /**
     * 根据 userId 查询该用户所有的菜单权限
     */
    List<String> getMenuPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 查询所有菜单
     *
     * @return 菜单list
     */
    List<Menu> selectAllMenuTree();

    /**
     * 根据用户ID找到所有菜单
     */
    Set<Menu> selectMenuTreeByUserId(@Param("userId") Long userId);

    /**
     * 查询菜单列表
     */
    List<Menu> selectMenuList(Menu menu);

    /**
     * 根据用户查询菜单
     */
    Set<Menu> selectMenuListByUserId(@Param("menu") Menu menu, @Param("userId") Long currentUserId);

    /**
     * 获取所有的菜单ID
     */
    Long[] getAllMenuIds();

    /**
     * 根据菜单ID查询菜单信息
     */
    Menu selectMenuByMenuId(@Param("menuId") Long menuId);

    /**
     * 查看菜单名字个数
     */
    int checkMenuNameUnique(@Param("name") String name);

    /**
     * 新增菜单
     */
    void insertMenu(MenuEntity menuEntity);

    /**
     * 更新菜单
     */
    void updateMenu(MenuEntity menuEntity);

    /**
     * 查看该菜单的有无子菜单
     */
    int countChildByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据菜单ID删除菜单
     */
    void deleteMenuByMenuId(@Param("menuId") Long menuId);

    /**
     * 获取所有的权限标识
     */
    Set<String> getMenuPermissionsAll();
}
