package cn.y3lq.blog.system.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author: Y3lq
 * @description:
 */
@Component
@ConfigurationProperties(prefix = "thread-pool")
@RefreshScope
@Data
public class ThreadPoolProperties {

    /**
     * 核心线程数量
     */
    private Integer corePoolSize;

    /**
     * 线程池能够容纳同时执行的最大线程数
     */
    private Integer maxPoolSize;

    /**
     * 队列容量
     */
    private Integer queueCapacity;

    /**
     * 空闲存活时间
     */
    private Integer keepAliveSeconds;





}
