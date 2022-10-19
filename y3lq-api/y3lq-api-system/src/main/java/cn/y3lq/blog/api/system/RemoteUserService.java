package cn.y3lq.blog.api.system;

import cn.y3lq.blog.api.system.factory.RemoteUserFallbackFactory;
import cn.y3lq.blog.common.core.constant.Constants;
import cn.y3lq.blog.common.core.domain.R;
import cn.y3lq.blog.common.core.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: Y3lq
 * @description: 远程system的用户服务
 */
@FeignClient(value = Constants.Y3LQ_SYSTEM, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {

    /**
     * 根据用户名找到User
     *
     * @param username 用户名
     */
    @GetMapping("/user/info/{username}")
    public R<User> findUserByUsername(@PathVariable("username") String username);
}
