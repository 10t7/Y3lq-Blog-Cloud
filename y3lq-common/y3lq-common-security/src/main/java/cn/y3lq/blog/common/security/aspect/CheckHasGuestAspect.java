package cn.y3lq.blog.common.security.aspect;

import cn.hutool.json.JSONUtil;
import cn.y3lq.blog.common.core.constant.CacheConstants;
import cn.y3lq.blog.common.core.constant.UserRoleConstants;
import cn.y3lq.blog.common.core.exception.ServiceException;
import cn.y3lq.blog.common.core.util.SecurityUtils;
import cn.y3lq.blog.common.security.annotation.ProhibitGuestAccess;
import cn.y3lq.blog.common.security.model.LoginUserModel;
import cn.y3lq.blog.common.security.util.UserInfoUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author: Y3lq
 * @description:
 */
@Aspect
@Component
public class CheckHasGuestAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisTemplate redisTemplate;

    public CheckHasGuestAspect() {
    }

    public static final String POINTCUT_SIGN =
            " @annotation(cn.y3lq.blog.common.security.annotation.HasPermissions) || " +
                    " @annotation(cn.y3lq.blog.common.security.annotation.HasRoles)";


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

    public void checkMethodAnnotation(Method method, HttpServletRequest request) {
        ProhibitGuestAccess annotation = method.getAnnotation(ProhibitGuestAccess.class);
        if (annotation != null) {
            LoginUserModel userInfo = null;

            if (ObjectUtils.isEmpty(UserInfoUtils.userModelThreadLocal.get())) {
                userInfo = UserInfoUtils.getUserInfo(request);
            }else{
                userInfo = UserInfoUtils.userModelThreadLocal.get();
            }
            for (String roleKey : userInfo.getRoleKeys()) {
                if (roleKey.equals(UserRoleConstants.VISITOR_ROLE_KEY)) {
                    throw new ServiceException("访客角色暂无此权限");
                }
            }

        }

    }


}
