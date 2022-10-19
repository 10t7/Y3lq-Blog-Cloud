package cn.y3lq.blog.common.security.aspect;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import cn.y3lq.blog.common.core.constant.CacheConstants;
import cn.y3lq.blog.common.core.constant.PermiConstants;
import cn.y3lq.blog.common.core.constant.TokenConstants;
import cn.y3lq.blog.common.core.exception.ServiceException;
import cn.y3lq.blog.common.core.util.SecurityUtils;
import cn.y3lq.blog.common.security.annotation.HasPermissions;
import cn.y3lq.blog.common.security.annotation.HasRoles;
import cn.y3lq.blog.common.security.model.LoginUserModel;
import cn.y3lq.blog.common.security.util.UserInfoUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author: Y3lq
 * @description: 基于 AOP 注解鉴权
 */
@Aspect
@Component
public class PreAuthorizeAspect {

    @Autowired
    private HttpServletRequest request;


    public PreAuthorizeAspect() {
    }

    public static final String POINTCUT_SIGN = " @annotation(cn.y3lq.blog.common.security.annotation.ProhibitGuestAccess)";


    @Pointcut(POINTCUT_SIGN)
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        checkMethodAnnotation(signature.getMethod(), request);
        try {
            Object proceed = joinPoint.proceed();
            return proceed;
        } catch (Throwable e) {
            throw e;
        }
    }


    /**
     * 检查方法是否标注相关注解
     */
    public void checkMethodAnnotation(Method method, HttpServletRequest request) {

        HasRoles hasRoles = method.getAnnotation(HasRoles.class);
        if (hasRoles != null) {
            // 要求的角色数组
            String[] roles = hasRoles.value();
            LoginUserModel userInfo = null;
            if (ObjectUtils.isEmpty(UserInfoUtils.userModelThreadLocal.get())) {
                userInfo = UserInfoUtils.getUserInfo(request);
            } else {
                userInfo = UserInfoUtils.userModelThreadLocal.get();
            }
            // 用户拥有的角色
            Set<String> roleKeys = userInfo.getRoleKeys();
            // 验证用户是否拥有指定角色
            for (String role : roles) {
                boolean flag = false;
                for (String roleKey : roleKeys) {
                    if (roleKey.equals(role)) {
                        flag = true;
                    }
                }
                if (!flag) {
                    throw new ServiceException("当前用户未拥有" + role + "角色");
                }
            }

        }

        HasPermissions hasPermissions = method.getAnnotation(HasPermissions.class);
        if (hasPermissions != null) {
            // 要求的权限
            String[] permissions = hasPermissions.value();
            LoginUserModel userInfo = null;
            if (ObjectUtils.isEmpty(UserInfoUtils.userModelThreadLocal.get())) {
                userInfo = UserInfoUtils.getUserInfo(request);
            } else {
                userInfo = UserInfoUtils.userModelThreadLocal.get();
            }
            // 用户拥有的权限
            Set<String> userPermissions = userInfo.getPermissions();
            boolean contains = userPermissions.contains("*:*:*");
            if (contains) {
                return;
            }
            for (String permission : permissions) {
                boolean flag = false;
                for (String userPermission : userPermissions) {
                    if (permission.equals(userPermission)) {
                        flag = true;
                    }
                }
                if (!flag) {
                    throw new ServiceException("权限不足");
                }
            }
        }
    }
}

