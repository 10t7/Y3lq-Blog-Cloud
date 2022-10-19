package cn.y3lq.blog.system.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author: Y3lq
 * @description: 日记评论实体类
 */
@Data
public class DiaryCommentEntity {

    private Long commentId;

    @NotNull(message = "日记ID不能为空")
    private Long diaryId;

    private Long toUserId;

    private Long fromUserId;

    @NotEmpty(message = "评论内容不能为空")
    @Size(min = 1, max = 150, message = "评论内容长度应为[1, 150]")
    private String content;

    private String delFlag;

    private Date createTime;

}
