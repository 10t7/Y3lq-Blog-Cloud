package cn.y3lq.blog.system.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author: Y3lq
 * @description: 文章点赞实体类
 */
@Data
public class ArticleLikeEntity {

    private Long likeId;

    private Long articleId;

    private Long userId;

    private Date createTime;

}
