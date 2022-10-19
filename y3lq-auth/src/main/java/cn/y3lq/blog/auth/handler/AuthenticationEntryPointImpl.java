package cn.y3lq.blog.auth.handler;

import cn.hutool.json.JSONUtil;
import cn.y3lq.blog.auth.exception.AuthException;
import cn.y3lq.blog.common.core.constant.HttpStatus;
import cn.y3lq.blog.common.core.constant.TokenConstants;
import cn.y3lq.blog.common.core.domain.AjaxResult;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * @author: Y3lq
 * @description: 用户认证失败处理类
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        ServletOutputStream outputStream = null;
        try {
            response.setStatus(HttpStatus.SUCCESS);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            outputStream = response.getOutputStream();
            AjaxResult error = AjaxResult.error(authenticationException.getMessage());
            outputStream.write(JSONUtil.toJsonStr(error).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            outputStream.flush();
            outputStream.close();
        }
//
//        ServletOutputStream outputStream = null;
//        try {
//            Object exception = request.getAttribute("javax.servlet.error.exception");
//            if (exception != null && exception.getClass().equals(AuthException.class)) {
////                 这处理security filter扔出来的异常无法被这拿到，
////                 框架保存到了 request，request保存的异常，优先给前端显示这里抛出来的原因
//                AuthException authException = (AuthException) exception;
//                response.setStatus(HttpStatus.SUCCESS);
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                response.setCharacterEncoding("utf-8");
//                outputStream = response.getOutputStream();
//                AjaxResult error = AjaxResult.error(authException.getMessage());
//                outputStream.write(JSONUtil.toJsonStr(error).getBytes(StandardCharsets.UTF_8));
//            } else {
//                response.setStatus(HttpStatus.SUCCESS);
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                response.setCharacterEncoding("utf-8");
//                outputStream = response.getOutputStream();
//                AjaxResult error = AjaxResult.error(authenticationException.getMessage());
//                outputStream.write(JSONUtil.toJsonStr(error).getBytes(StandardCharsets.UTF_8));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            outputStream.flush();
//            outputStream.close();
//        }
    }
}
