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
@ConfigurationProperties(prefix = "article")
@RefreshScope
@Data
public class ArticleProperties {
    private Boolean allowed;
}
