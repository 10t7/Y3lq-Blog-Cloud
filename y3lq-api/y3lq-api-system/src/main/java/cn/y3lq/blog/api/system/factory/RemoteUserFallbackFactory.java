package cn.y3lq.blog.api.system.factory;

import cn.y3lq.blog.api.system.RemoteUserService;
import cn.y3lq.blog.common.core.domain.R;
import cn.y3lq.blog.common.core.domain.User;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author: Y3lq
 * @description: 远程调用服务降级处理
 */
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {
    @Override
    public RemoteUserService create(Throwable cause) {

        return new RemoteUserService() {
            @Override
            public R<User> findUserByUsername(String usernmae) {

                return R.fail("远程获取用户失败，原因：" + cause.getMessage());
            }
        };
    }
}
