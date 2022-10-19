package cn.y3lq.blog.system.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author: Y3lq
 * @description: 文章评论
 */
@Data
public class ArticleCommentEntity {
    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 评论所属文章ID
     */
    @NotNull(message = "评论文章ID不能为空")
    private Long articleId;

    /**
     * 第一层评论（第一层评论，则为0）
     */
    private Long firstCommentId;

    /**
     * 评论指定用户的评论的用户ID
     */
    private Long toUserId;

    /**
     * 评论者的用户ID
     */
    private Long fromUserId;

    /**
     * 评论内容
     */
    @NotEmpty(message = "评论内容不能为空")
    @Length(min = 1,max = 100,message = "内容长度应在[1, 100]")
    private String content;

    /**
     * 评论删除标识（0 正常 1删除）
     */
    private String delFlag;

    /**
     * 评论创建时间
     */
    private Date createTime;
}
