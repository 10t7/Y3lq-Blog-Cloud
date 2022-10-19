package cn.y3lq.blog.auth.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Y3lq
 * @description: 验证码配置
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "security.captcha")
@Data
public class CaptchaProperties {

    /**
     * 验证码开关
     */
    private Boolean enabled;

    /**
     * 验证码类型（math：计算   char：字符）
     */
    private String type;
}
