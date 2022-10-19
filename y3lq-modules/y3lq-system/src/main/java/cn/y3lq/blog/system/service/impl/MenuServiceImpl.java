package cn.y3lq.blog.system.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.y3lq.blog.common.core.constant.CacheConstants;
import cn.y3lq.blog.common.core.constant.Constants;
import cn.y3lq.blog.common.core.constant.UserRoleConstants;
import cn.y3lq.blog.common.core.exception.ServiceException;
import cn.y3lq.blog.common.core.util.SecurityUtils;
import cn.y3lq.blog.system.domain.Menu;
import cn.y3lq.blog.system.entity.MenuEntity;
import cn.y3lq.blog.system.mapper.MenuMapper;
import cn.y3lq.blog.system.mapper.RoleMenuMapper;
import cn.y3lq.blog.system.service.MenuService;
import cn.y3lq.blog.system.vo.MetaVO;
import cn.y3lq.blog.system.vo.RouterVO;
import cn.y3lq.blog.system.vo.TreeSelect;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: Y3lq
 * @description: 菜单信息业务层
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "y3lqThreadPool")
    private Executor executor;

    /**
     * 根据 userId 获取该用户所有菜单权限
     */
    @Override
    public Set<String> getMenuPermissions(Long userId) {
        Long[] userIds = {userId};
        if (SecurityUtils.isHasSuperAdminUserId(userIds)) {
            // 超级管理员赋予所有权限
            Set<String> permissions = new HashSet<>();
            permissions.add("*:*:*");
            return permissions;
        } else {
            // 非超级管理员则查询具体拥有的权限
            List<String> permissions = menuMapper.getMenuPermissionsByUserId(userId);
            Set<String> permis = new HashSet<>();
            for (String permission : permissions) {
                if (!StringUtils.isEmpty(permission)) {
                    permis.add(permission);
                }
            }
            return permis;
        }
    }

    /**
     * 根据用户ID查询该用户拥有的菜单
     *
     * @param userId  当前用户ID
     * @param request
     */
    @Override
    public List<Menu> selectMenuTreeByUserId(Long userId, HttpServletRequest request) {
        List<Menu> menuList = new ArrayList<>();
        if (SecurityUtils.isSuperAdmin(request)) {
            // 为超级管理员 查询所有菜单
            menuList = menuMapper.selectAllMenuTree();
//            String menuListInRedis = (String) redisTemplate.opsForValue().get(CacheConstants.MENU_LIST);
//            if (ObjectUtils.isEmpty(menuListInRedis)) {
//                // 缓存存储的所有菜单失效，从数据库拿
//                List<Menu> menuListInMysql = menuMapper.selectAllMenuTree();
//                menuList = menuListInMysql;
//                CompletableFuture.runAsync(() -> {
//                    String s = JSONUtil.toJsonStr(menuListInMysql);
//                    redisTemplate.opsForValue().set(CacheConstants.MENU_LIST, s, 2, TimeUnit.DAYS);
//                }, executor);
//            }
//            JSONArray menuArr = JSONUtil.parseArray(menuListInRedis);
//            Object[] objects = menuArr.toArray();
//            for (int i = 0; i < objects.length; i++) {
//                JSONObject menu = (JSONObject) objects[i];
//                menuList.add(JSONUtil.toBean(menu, Menu.class));
//            }
        } else {
            Set<Menu> menus = menuMapper.selectMenuTreeByUserId(userId);
            for (Menu menu : menus) {
                menuList.add(menu);
            }
        }
        // 排序
        Collections.sort(menuList, new Comparator<Menu>() {
            @Override
            public int compare(Menu m1, Menu m2) {
                int i;
                i = m1.getParentId().compareTo(m2.getParentId());
                if (i == 0) {
                    i = m1.getOrderNum().compareTo(m2.getOrderNum());
                }
                return i;
            }
        });
        return getChildPerms(menuList, 0L);
    }

    /**
     * 构建前端需要的菜单
     */
    @Override
    public List<RouterVO> buildMenus(List<Menu> menus) {
        List<RouterVO> routers = new LinkedList<RouterVO>();
        for (Menu menu : menus) {
            RouterVO router = new RouterVO();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVO(menu.getName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
            List<Menu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserRoleConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVO> childrenList = new ArrayList<RouterVO>();
                RouterVO children = new RouterVO();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVO(menu.getName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVO(menu.getName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVO> childrenList = new ArrayList<RouterVO>();
                RouterVO children = new RouterVO();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserRoleConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVO(menu.getName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 拿到当前用户所有的菜单
     */
    @Override
    public List<Menu> selectMenuList(Menu menu, HttpServletRequest request) {
        List<Menu> menuList = new ArrayList<>();
        Long currentUserId = SecurityUtils.getCurrentUserId(request);
        if (SecurityUtils.isSuperAdmin(request)) {
            // 为超级管理员 显示所有菜单
            menuList = menuMapper.selectMenuList(menu);
        } else {
            Set<Menu> menuSet = menuMapper.selectMenuListByUserId(menu, currentUserId);
            for (Menu menua : menuSet) {
                menuList.add(menua);
            }
        }
        // 排序
        Collections.sort(menuList, new Comparator<Menu>() {
            @Override
            public int compare(Menu m1, Menu m2) {
                int i;
                i = m1.getParentId().compareTo(m2.getParentId());
                if (i == 0) {
                    i = m1.getOrderNum().compareTo(m2.getOrderNum());
                }
                return i;
            }
        });
        return menuList;
    }

    /**
     * 构建前端需要的下拉树结构
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<Menu> menuList) {
        List<Menu> menuTree = buildMenuTree(menuList);
        return menuTree.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据菜单ID查询菜单信息
     */
    @Override
    public Menu selectMenuByMenuId(Long menuId) {
        return menuMapper.selectMenuByMenuId(menuId);
    }

    /**
     * 检查新增相关参数是否唯一合理
     */
    @Override
    public void checkAddParameterCorrect(MenuEntity menu) {
        int count = menuMapper.checkMenuNameUnique(menu.getName());
        if (count > 0) {
            throw new ServiceException("菜单名称重复");
        }
        if (UserRoleConstants.YES_FRAME.equals(menu.getIsFrame())) {
            if (StringUtils.isEmpty(menu.getPath())) {
                throw new ServiceException("路径不能为空");
            }
            if (!menu.getPath().startsWith("http")) {
                throw new ServiceException("路由地址必须以http开头");
            }
        }
    }

    /**
     * 新增菜单
     */
    @Override
    public void insertMenu(MenuEntity menuEntity, HttpServletRequest request) {
        menuEntity.setCreateBy(String.valueOf(SecurityUtils.getCurrentUserId(request)));
        menuMapper.insertMenu(menuEntity);
    }

    /**
     * 检查更新菜单参数是否唯一合理
     */
    @Override
    public void checkUpdateParameterCorrect(MenuEntity menu) {
        if (UserRoleConstants.YES_FRAME.equals(menu.getIsFrame())) {
            if (StringUtils.isEmpty(menu.getPath())) {
                throw new ServiceException("路径不能为空");
            }
            if (!menu.getPath().startsWith("http")) {
                throw new ServiceException("路由地址必须以http开头");
            }
        }
        if (menu.getMenuId().equals(menu.getParentId())) {
            throw new ServiceException("上级菜单不能选自己");
        }
        Menu menuInfo = menuMapper.selectMenuByMenuId(menu.getMenuId());
        if (menuInfo.getName().equals(menu.getName())) {
            // 名字没更改
            return;
        }
        int i = menuMapper.checkMenuNameUnique(menu.getName());
        if (i > 0) {
            throw new ServiceException("菜单名称重复");
        }


    }

    /**
     * 更新菜单
     */
    @Override
    public void updateMenu(MenuEntity menuEntity, HttpServletRequest request) {
        menuEntity.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId(request)));
        menuMapper.updateMenu(menuEntity);
    }

    /**
     * 查看是否允许删除菜单
     */
    @Override
    public void checkAllowedDelete(Long menuId) {
        int childCount = menuMapper.countChildByMenuId(menuId);
        if (childCount > 0) {
            throw new ServiceException("存在子菜单，请先删除所有子菜单");
        }
        int associationCount = roleMenuMapper.countMenuAssociation(menuId);
        if (associationCount > 0) {
            throw new ServiceException("该菜单已被分配，请先解除分配");
        }
    }

    /**
     * 删除菜单
     */
    @Override
    public void deleteMenuByMenuId(Long menuId) {
        menuMapper.deleteMenuByMenuId(menuId);
    }

    /**
     * 获取所有的权限标识
     */
    @Override
    public Set<String> getMenuPermissionsAll() {
        return menuMapper.getMenuPermissionsAll();
    }

    /**
     * 构建前端需要的树结构
     */
    public List<Menu> buildMenuTree(List<Menu> menus) {
        List<Menu> returnList = new ArrayList<Menu>();
        List<Long> tempList = new ArrayList<Long>();
        for (Menu menu : menus) {
            tempList.add(menu.getMenuId());
        }
        for (Iterator<Menu> iterator = menus.iterator(); iterator.hasNext(); ) {
            Menu menu = (Menu) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }


    /**
     * 内链域名特殊字符替换
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS},
                new String[]{"", ""});
    }


    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(Menu menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(Menu menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserRoleConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserRoleConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(Menu menu) {
        String component = UserRoleConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = UserRoleConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserRoleConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(Menu menu) {
        return menu.getParentId().intValue() == 0 && UserRoleConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserRoleConstants.NO_FRAME);
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(Menu menu) {
        return menu.getIsFrame().equals(UserRoleConstants.NO_FRAME) && ishttp(menu.getPath());
    }

    /**
     * 查看是否HTTP请求
     */
    private boolean ishttp(String path) {
        return StringUtils.startsWithAny(path, Constants.HTTPS, Constants.HTTPS);
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(Menu menu) {
        return menu.getParentId().intValue() != 0 && UserRoleConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 根据父节点ID获取所有子节点
     *
     * @param menuList 菜单列表
     * @param parentId 父节点ID
     * @return
     */
    private List<Menu> getChildPerms(List<Menu> menuList, long parentId) {
        return menuList.stream().filter(menu -> {
            if (menu.getParentId() == parentId) {
                // 收集父节点所有子节点
                recursionFn(menuList, menu);
            }
            // 收集所有父节点
            return menu.getParentId() == parentId;
        }).collect(Collectors.toList());
    }

    /**
     * 递归列表
     *
     * @param menuList   所有菜单
     * @param parentMenu 父节点菜单
     */
    private void recursionFn(List<Menu> menuList, Menu parentMenu) {
        List<Menu> childList = getChildList(menuList, parentMenu);
        parentMenu.setChildren(childList);
        for (Menu menu : childList) {
            // 查看子节点有无子节点
            if (hasChild(menuList, menu)) {
                recursionFn(menuList, menu);
            }
        }
    }

    /**
     * 查看是否有子节点
     */
    private boolean hasChild(List<Menu> menuList, Menu menu) {
        return getChildList(menuList, menu).size() > 0;
    }

    /**
     * 获取该菜单所有子节点
     */
    private List<Menu> getChildList(List<Menu> menuList, Menu parentMenu) {
        return menuList.stream().filter(menu -> {
            return menu.getParentId().equals(parentMenu.getMenuId());
        }).collect(Collectors.toList());
    }
}
