package cn.y3lq.blog.gateway.handler;

import cn.hutool.http.HttpStatus;
import cn.y3lq.blog.gateway.util.WebFluxResponseUtils;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author: Y3lq
 * @description: 网关异常统一处理
 */
@Configuration
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        return WebFluxResponseUtils.webFluxResponseWriter(response, HttpStatus.HTTP_INTERNAL_ERROR, ex.getMessage());

    }
}
