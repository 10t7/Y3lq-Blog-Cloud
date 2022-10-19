package cn.y3lq.blog.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: Y3lq
 * @description: 网关启动类
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class Y3lqBlogGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(Y3lqBlogGatewayApplication.class, args);
    }
}
