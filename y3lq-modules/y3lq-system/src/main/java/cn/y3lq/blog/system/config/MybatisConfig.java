package cn.y3lq.blog.system.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Y3lq
 * @description: mybatis配置类
 */
@MapperScan("cn.y3lq.blog.system.mapper")
@Configuration
public class MybatisConfig {
}
