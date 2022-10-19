package cn.y3lq.blog.system.mapper;

import cn.y3lq.blog.system.entity.ArticleCommentEntity;
import cn.y3lq.blog.system.vo.ArticleCommentVO;
import cn.y3lq.blog.system.vo.BlogArticleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Y3lq
 * @description: 文章评论
 */
@Mapper
public interface ArticleCommentMapper {
    /**
     * 评论文章
     */
    void commentArticle(ArticleCommentEntity articleCommentEntity);

    /**
     * 获取指定文章评论
     */
    List<ArticleCommentVO> selectArticleCommentList(@Param("articleId") Long articleId);

    /**
     * 查看评论的子评论个数
     */
    int countArticleComment(@Param("commentId") Long commentId);

    /**
     * 获取文章指定一级评论所有子评论
     */
    List<ArticleCommentVO> articleCommentChildren(@Param("commentId") Long commentId);

    /**
     * 根据ID获取评论
     */
    ArticleCommentVO selectArticleCommentById(@Param("commentId") Long commentId);

    /**
     * 删除指定评论
     */
    void deleteArticleComment(@Param("commentId") Long commentId);

    /**
     * 删除指定评论以及它的子评论
     */
    void deleteArticleCommentAndChildren(@Param("commentId") Long commentId);

    /**
     * 查看子评论数量
     */
    int countChildComment(@Param("commentId") Long commentId);
}
