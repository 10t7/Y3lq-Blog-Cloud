package cn.y3lq.blog.system.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Y3lq
 * @description: 文章评论相关信息封装数据类
 */
@Data
public class ArticleCommentVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 评论所属文章ID
     */
    private Long articleId;

    /**
     * 第一层评论（第一层评论，则为0）
     */
    private Long firstCommentId;

    private String toNickname;

    /**
     * 评论指定用户的评论的用户ID
     */
    private Long toUserId;

    private String fromNickname;

    private String avatar;

    /**
     * 评论者的用户ID
     */
    private Long fromUserId;

    /**
     * 点赞数量
     */
    private Integer likeCount;

    /**
     * 是否点赞
     */
    private String isLike;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论创建时间
     */
    private Date createTime;

    /**
     * 该条评论拥有的子评论数量，无则设置为0
     */
    private Integer childrenCommentCount;
}
