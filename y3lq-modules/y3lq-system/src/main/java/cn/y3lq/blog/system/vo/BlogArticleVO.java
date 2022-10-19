package cn.y3lq.blog.system.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 返回博客前端渲染的文章
 */
@Data
public class BlogArticleVO implements Serializable {
    private static final long serialVersionUID = 1L;
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
     * 是否点赞
     */
    private String isLike;

    /**
     * 文章顺序
     */
    private Integer orderNum;

    /**
     * 文章创建时间
     */
    private Date createTime;

    /**
     * 文章更新时间
     */
    private Date updateTime;

    /**
     * 该文章标签名称
     */
    private List<String> tags;

    private String authorNickname;

    private Long authorUserId;

}
