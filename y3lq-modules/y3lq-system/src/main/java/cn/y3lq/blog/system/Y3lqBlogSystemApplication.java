package cn.y3lq.blog.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: Y3lq
 * @description: 系统核心服务启动类
 */
@SpringBootApplication
@EnableFeignClients("cn.y3lq.blog")
@ComponentScan("cn.y3lq.blog")
public class Y3lqBlogSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(Y3lqBlogSystemApplication.class, args);
    }
}
