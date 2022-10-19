package cn.y3lq.blog.gateway.util;


import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import cn.y3lq.blog.common.core.domain.R;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * @author: Y3lq
 * @description: 设置webflux响应
 */
public class WebFluxResponseUtils {

    /**
     * webflux响应
     *
     * @param response ServerHttpResponse
     * @param code     http状态码
     * @param msg    响应消息
     * @return Mono<Void>
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, int code, String msg) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        R<String> result = R.fail(code, msg);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSONUtil.toJsonStr(result).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }
}
