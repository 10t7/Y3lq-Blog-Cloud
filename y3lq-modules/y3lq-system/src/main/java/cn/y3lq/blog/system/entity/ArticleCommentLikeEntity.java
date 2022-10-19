package cn.y3lq.blog.system.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author: Y3lq
 * @description: 文章评论点赞实体类
 */
@Data
public class ArticleCommentLikeEntity {


    private Long likeId;

    private Long commentId;

    private Long userId;

    private Date createTime;

}
