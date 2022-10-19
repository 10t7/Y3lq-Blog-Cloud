package cn.y3lq.blog.auth.handler;

import cn.y3lq.blog.api.system.RemoteUserService;

import cn.y3lq.blog.auth.domain.LoginUser;
import cn.y3lq.blog.auth.exception.AuthException;
import cn.y3lq.blog.auth.filter.Y3lqUsernamePasswordAuthenticationFilter;
import cn.y3lq.blog.common.core.constant.Constants;
import cn.y3lq.blog.common.core.constant.HttpStatus;
import cn.y3lq.blog.common.core.constant.UserRoleConstants;
import cn.y3lq.blog.common.core.domain.R;
import cn.y3lq.blog.common.core.domain.User;
import cn.y3lq.blog.common.core.exception.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author: Y3lq
 * @description: 用户验证处理
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RemoteUserService remoteUserService;


    /**
     * 根据用户名找到User
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        R<User> result = null;
        try {
            if (StringUtils.isEmpty(username)) {
                throw new AuthException("用户登录参数缺失");
            }
            result = remoteUserService.findUserByUsername(username);
            String type = Y3lqUsernamePasswordAuthenticationFilter.threadLocal.get();
            if("blog-login".equals(type)){
                Set<String> roleKeys = result.getData().getRoleKeys();
                boolean flag = true;
                for (String roleKey : roleKeys) {
                    if(UserRoleConstants.BLOG_USER_ROLE_KEY.equals(roleKey)){
                        flag = false;
                    }
                }
                if(flag){
                    throw new AuthException("缺失博客用户角色");
                }
            }
            Y3lqUsernamePasswordAuthenticationFilter.threadLocal.remove();
        } catch (Exception e) {
            // 自己处理远程调用的异常
            throw new AuthException(e.getMessage());
        }

        if (result.getCode() == Constants.FAIL) {
            throw new AuthException(result.getMsg());
        }
        User user = result.getData();
        if (user.getStatus() == UserRoleConstants.USER_STATUS_DISABLE) {
            throw new AuthException("账号已停用");
        }
        if (user.getDelFlag() == UserRoleConstants.USER_DEL_FLAG_DELETE) {
            throw new AuthException("账号已被删除");
        }
        LoginUser loginUser = new LoginUser(user);
        return loginUser;
    }
}
