package cn.y3lq.blog.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author: Y3lq
 * @description: 认证服务启动类
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients("cn.y3lq.blog")
public class Y3lqBlogAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(Y3lqBlogAuthApplication.class, args);
    }
}
