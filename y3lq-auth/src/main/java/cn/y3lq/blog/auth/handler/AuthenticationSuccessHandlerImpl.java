package cn.y3lq.blog.auth.handler;

import cn.hutool.json.JSONUtil;
import cn.y3lq.blog.auth.config.properties.CaptchaProperties;
import cn.y3lq.blog.auth.domain.LoginUser;
import cn.y3lq.blog.common.core.constant.HttpStatus;
import cn.y3lq.blog.common.core.constant.TokenConstants;
import cn.y3lq.blog.common.core.domain.AjaxResult;
import cn.y3lq.blog.common.security.model.LoginUserModel;
import cn.y3lq.blog.common.security.service.TokenService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author: Y3lq
 * @description: 认证成功处理类
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Autowired
    private TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        LoginUserModel loginUserModel = new LoginUserModel();
        BeanUtils.copyProperties(loginUser, loginUserModel);
        // 这个方法创建jwtToken，并且放到redis中，以后想访问带着 token ，去redis查看是否存在， 存在则已经认证登录
        String token = tokenService.createToken(loginUserModel);
        // 下面进行对前端响应操作
        ServletOutputStream outputStream = null;
        try {
            response.setStatus(HttpStatus.SUCCESS);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            outputStream = response.getOutputStream();
            // 把JWT token 设置进响应
            AjaxResult success = AjaxResult.success("登录成功", token);
            outputStream.write(JSONUtil.toJsonStr(success).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }
}
