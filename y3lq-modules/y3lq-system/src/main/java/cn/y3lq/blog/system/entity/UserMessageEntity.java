package cn.y3lq.blog.system.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author: Y3lq
 * @description:
 */
@Data
public class UserMessageEntity {

    private Integer fromUserId;

    private String fromUserNickname;

    private String type;

    private String context;

    private Date createTime;
}
