package cn.y3lq.blog.system.entity;

import lombok.Data;

/**
 * @author: Y3lq
 * @description: 文章标签关联表
 */
@Data
public class ArticleTagEntity {
    private Long articleId;

    private Long tagId;
}
