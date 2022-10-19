package cn.y3lq.blog.system.vo;

import lombok.Data;

/**
 * @author: Y3lq
 * @description: 封装日记点赞人信息
 */
@Data
public class DiaryLikeVO {
    private Long likeId;

    private Long userId;

    private String nickname;

    private String status;

}
