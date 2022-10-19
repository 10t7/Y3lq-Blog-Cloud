package cn.y3lq.blog.system.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author: Y3lq
 * @description:
 */
@Data
public class DiaryCommentVO {

    private Long commentId;

    private Long diaryId;

    private Long toUserId;

    private String toUserNickname;

    private Long fromUserId;

    private String fromUserNickname;

    private String content;

    private String delFlag;

    private Date createTime;
}
