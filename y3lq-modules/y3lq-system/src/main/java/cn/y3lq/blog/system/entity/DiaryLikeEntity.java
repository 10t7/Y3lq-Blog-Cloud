package cn.y3lq.blog.system.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author: Y3lq
 * @description: 日记点赞实体类
 */
@Data
public class DiaryLikeEntity {
    private Long likeId;

    private Long diaryId;

    private Long userId;

    private String nickname;

    /**
     * 点赞状态（1：点赞  0：取消点赞）
     */
    private String status;

    private Date createTime;


}
