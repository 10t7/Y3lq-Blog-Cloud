package cn.y3lq.blog.system.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author: Y3lq
 * @description: 文章实体类
 */
@Data
public class ArticleEntity {
    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章简介
     */
    private String summary;

    /**
     * 文章缩略图地址
     */
    private String thumbnail;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章评论数
     */
    private Integer commentCount;

    /**
     * 文章点赞数
     */
    private Integer likeCount;

    /**
     * 文章顺序
     */
    private Integer orderNum;

    /**
     * 文章状态
     */
    private String status;

    /**
     * 文章删除标识
     */
    private String delFlag;

    /**
     * 文章创建者用户ID
     */
    private String createBy;

    /**
     * 文章创建时间
     */
    private Date createTime;

    /**
     * 文章更新者用户ID
     */
    private String updateBy;

    /**
     * 文章更新时间
     */
    private Date updateTime;
}
