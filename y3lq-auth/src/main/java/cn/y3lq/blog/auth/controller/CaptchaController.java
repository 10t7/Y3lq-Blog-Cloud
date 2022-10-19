package cn.y3lq.blog.auth.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import cn.y3lq.blog.auth.config.properties.CaptchaProperties;
import cn.y3lq.blog.common.core.constant.CacheConstants;
import cn.y3lq.blog.common.core.domain.AjaxResult;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author: Y3lq
 * @description: 返回前端验证码
 */
@RestController
public class CaptchaController {
    /**
     * 验证码类型常量
     */
    public static final String MATH = "math";
    public static final String CHAR = "char";

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CaptchaProperties captchaProperties;

    /**
     * 生成验证码
     */
    @GetMapping("/captcha")
    public AjaxResult createCaptcha() {
        AjaxResult ajax = AjaxResult.success();
        Boolean enabled = captchaProperties.getEnabled();
        if (!enabled) {
            return AjaxResult.success().put("captchaEnabled",false);
        }

        String uuid = UUID.fastUUID().toString();
        String keyInRedis = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;
        String type = captchaProperties.getType();
        if (MATH.equals(type)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if (CHAR.equals(type)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }
        // 把验证码正确答案放到redis 有效期为3分钟
        redisTemplate.opsForValue().set(keyInRedis, code, CacheConstants.CAPTCHA_EXPIRATION_TIME, TimeUnit.MINUTES);
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }

        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        ajax.put("captchaEnabled", captchaProperties.getEnabled());
        return ajax;
    }
}
