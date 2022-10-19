package cn.y3lq.blog.common.security.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Y3lq
 * @description: 查看是否拥有某个角色
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface HasRoles {

    /**
     * 验证的角色字符串数组
     */
    String[] value() default {};
}
