package cn.y3lq.blog.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author: Y3lq
 * @description: spring security相关异常
 */
public class AuthException extends AuthenticationException {

    public AuthException(String msg) {
        super(msg);
    }
}
