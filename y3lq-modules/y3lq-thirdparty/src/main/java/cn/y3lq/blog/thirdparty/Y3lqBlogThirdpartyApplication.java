package cn.y3lq.blog.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: Y3lq
 * @description: 第三方服务启动类
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class Y3lqBlogThirdpartyApplication {
    public static void main(String[] args) {
        SpringApplication.run(Y3lqBlogThirdpartyApplication.class, args);
    }
}
