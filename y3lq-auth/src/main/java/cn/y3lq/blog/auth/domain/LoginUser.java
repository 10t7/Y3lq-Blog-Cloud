package cn.y3lq.blog.auth.domain;


import cn.y3lq.blog.common.core.domain.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * @author: Y3lq
 * @description: 登录的用户
 */
@Data
public class LoginUser implements UserDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 用户的id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户登录时间
     */
    private Long loginTime;

    /**
     * 用户过期时间
     */
    private Long expireTime;

    /**
     * 用户登录ip
     */
    private String ipaddr;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 用户角色集合
     */
    private Set<String> roleKeys;

    /**
     * 用户信息
     */
    private User user;

    public LoginUser(User user) {
        this.user = user;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> auth = new ArrayList<>();
        Set<String> roleKeys = user.getRoleKeys();
        for (String roleKey : roleKeys) {
            auth.add(new SimpleGrantedAuthority(roleKey));
        }
        return auth;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
