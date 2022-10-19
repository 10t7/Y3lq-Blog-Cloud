package cn.y3lq.blog.api.auth.factory;

import cn.y3lq.blog.api.auth.RemoteAuthService;
import cn.y3lq.blog.common.core.constant.TokenConstants;
import cn.y3lq.blog.common.core.domain.AjaxResult;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * @author: Y3lq
 * @description:
 */
public class RemoteAuthFallbackFactory implements FallbackFactory<RemoteAuthService> {

    @Override
    public RemoteAuthService create(Throwable cause) {
        return new RemoteAuthService() {
            @Override
            public AjaxResult logout(String token) {

                return AjaxResult.error("远程退出用户失败，原因：" + cause.getMessage());
            }

            @Override
            public void logoutUserByIds(List<String> userIds, String token) {
            }
        };

    }
}
