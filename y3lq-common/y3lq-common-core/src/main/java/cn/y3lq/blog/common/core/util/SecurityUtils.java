package cn.y3lq.blog.common.core.util;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.y3lq.blog.common.core.constant.TokenConstants;
import cn.y3lq.blog.common.core.constant.UserRoleConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * @author: Y3lq
 * @description: 安全相关工具
 */
public class SecurityUtils {

    /**
     * 拿到 request 的token并且解析出 当前用户的UserId
     */
    public static Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader(TokenConstants.TOKEN);
        String userId = (String) JWTUtil.parseToken(token).getPayload().getClaim(TokenConstants.USER_ID);
        return Long.parseLong(userId);
    }

    /**
     * 查看当前用户是否为超级管理员
     */
    public static Boolean isSuperAdmin(HttpServletRequest request) {
        String token = request.getHeader(TokenConstants.TOKEN);
        String userId = (String) JWTUtil.parseToken(token).getPayload().getClaim(TokenConstants.USER_ID);
        // 当前用户ID
        long currentUserId = Long.parseLong(userId);
        // 超级管理员id数组
        Long[] superAdminUserIds = UserRoleConstants.SUPER_ADMIN_USER_ID;
        for (Long superAdminUserId : superAdminUserIds) {
            if (superAdminUserId.equals(currentUserId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查看是否为博客用户或者后台访客角色
     *
     * @param roleIds 用户拥有的所有角色
     */
    public static boolean isNormalRole(List<Long> roleIds) {
        for (Long roleId : roleIds) {
            if (UserRoleConstants.BLOG_USER_ROLE_ID.equals(roleId)) {
                continue;
            }
            if (UserRoleConstants.VISITOR_ROLE_ID.equals(roleId)) {
                continue;
            }
            // 只要有一个角色ID不为 博客用户 或者 后台访客，则返回false
            return false;
        }
        return true;
    }

    /**
     * 查看set是否包含有管理员
     *
     * @param roleIdSet
     */
    public static boolean isNormalRoleSet(Set<Long> roleIdSet) {
        for (Long roleId : roleIdSet) {
            if (UserRoleConstants.BLOG_USER_ROLE_ID.equals(roleId)) {
                continue;
            }
            if (UserRoleConstants.VISITOR_ROLE_ID.equals(roleId)) {
                continue;
            }
            // 只要有一个角色ID不为 博客用户 或者 后台访客，则返回false
            return false;
        }
        return true;
    }

    /**
     * 查看数组的用户ID是否包含超级管理员的用户ID
     */
    public static boolean isHasSuperAdminUserId(Long[] userIds) {
        for (Long userId : userIds) {
            Long[] superAdminUserIds = UserRoleConstants.SUPER_ADMIN_USER_ID;
            for (Long superAdminUserId : superAdminUserIds) {
                if (superAdminUserId.equals(userId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 查看是否为超级管理员的 角色ID
     */
    public static boolean isSuperRole(Long roleId) {
        return UserRoleConstants.SUPER_ADMIN_ROLE_ID.equals(roleId);

    }


}
