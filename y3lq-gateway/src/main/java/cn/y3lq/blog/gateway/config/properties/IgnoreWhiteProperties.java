package cn.y3lq.blog.gateway.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 白名单路径配置
 */
@Component
@ConfigurationProperties(prefix = "security.ignore")
@RefreshScope
@Data
public class IgnoreWhiteProperties {

    private List<String> whites = new ArrayList<>();

}
