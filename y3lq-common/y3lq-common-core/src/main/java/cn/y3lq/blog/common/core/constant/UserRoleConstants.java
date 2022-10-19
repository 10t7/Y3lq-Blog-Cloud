package cn.y3lq.blog.common.core.constant;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Y3lq
 * @description: 用户和角色相关常量
 */
public class UserRoleConstants {

    /**
     * 用户状态常量（0为正常 1为停用）
     */
    public static final String USER_STATUS_NORMAL = "0";

    /**
     * 用户状态常量（0为正常 1为停用）
     */
    public static final String USER_STATUS_DISABLE = "1";

    /**
     * 用户账号删除常量（0为正常 1为删除）
     */
    public static final String USER_DEL_FLAG_NORMAL = "0";

    /**
     * 用户账号删除常量（0为正常 1为删除）
     */
    public static final String USER_DEL_FLAG_DELETE = "1";

    /**
     * 超级管理员的userId
     */
    public static final Long[] SUPER_ADMIN_USER_ID = {1L};

    /**
     * 超级管理员的 roleId
     */
    public static final Long SUPER_ADMIN_ROLE_ID = 1L;


    /**
     * 后台访客的 roleId
     */
    public static final Long VISITOR_ROLE_ID = 4L;

    /**
     * 博客用户 roleId
     */
    public static final Long BLOG_USER_ROLE_ID = 5L;

    /**
     * 默认头像地址
     */
    public static final String DEFAULT_AVATAR = "https://y3lq-blog.oss-cn-shenzhen.aliyuncs.com/1607146438803.jpg";


    /**
     * 用户默认备注
     */
    public static final String DEFAULT_REMARK = "#";


    /**
     * 菜单类型
     */
    public static final String TYPE_DIR = "M";
    /**
     * Layout 组件
     */
    public static final String LAYOUT = "Layout";
    /**
     * InnerLink 组件标识
     */
    public static final String INNER_LINK = "InnerLink";

    /**
     * ParentView 组件
     */
    public static final String PARENT_VIEW = "ParentView";

    /**
     * 菜单类型
     */
    public static final String TYPE_MENU = "C";
    /**
     * 是否外链(是)
     */
    public static final String NO_FRAME = "1";

    /**
     * 是否外链(否)
     */
    public static final String YES_FRAME = "0";

    public static final String BLOG_USER_ROLE_KEY = "y3lq_user";

    public static final String VISITOR_ROLE_KEY = "visitor";
}
