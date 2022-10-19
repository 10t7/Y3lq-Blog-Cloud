package cn.y3lq.blog.api.auth;

import cn.y3lq.blog.api.auth.factory.RemoteAuthFallbackFactory;
import cn.y3lq.blog.common.core.constant.Constants;
import cn.y3lq.blog.common.core.constant.TokenConstants;
import cn.y3lq.blog.common.core.domain.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: Y3lq
 * @description: 远程auth认证用户服务
 */
@FeignClient(value = Constants.Y3LQ_AUTH, fallbackFactory = RemoteAuthFallbackFactory.class)
public interface RemoteAuthService {

    /**
     * 根据token把对应的 user 登录认证删除
     *
     * @param token
     * @return
     */
    @PostMapping(value = "/logout")
    public AjaxResult logout(@RequestHeader(TokenConstants.TOKEN) String token);

    /**
     * 批量删除用户登录凭证
     *
     * @param userIds 用户ID数组
     * @param token   执行此操作的用户 token
     */
    @PostMapping(value = "/logoutUserByIds", headers = {"Content-Type=application/json;charset=UTF-8"})
    void logoutUserByIds(@RequestBody List<String> userIds, @RequestHeader(TokenConstants.TOKEN) String token);
}
