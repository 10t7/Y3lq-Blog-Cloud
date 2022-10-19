package cn.y3lq.blog.system.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author: Y3lq
 * @description: 用户设置实体类
 */
@Data
public class UserSettingEntity {
    private Long userSettingId;

    private Long userId;

    /**
     * 有新回复是否接收邮件通知（0：拒绝  1接收）
     */
    private String replyReceiveEmail;

    /**
     * 有订阅新动态是否接收邮件通知（0：拒绝  1接收）
     */
    private String newReceiveEmail;

    /**
     * 是否展现个人信息在展现给他人的资料卡（微信，邮箱）（0：拒绝  1接收）
     */
    private String showPersonInfo;

    private Date createTime;

    private Date updateTime;
}
