package cn.y3lq.blog.system.service;

import cn.y3lq.blog.system.entity.ArticleCommentEntity;
import cn.y3lq.blog.system.vo.ArticleCommentVO;
import cn.y3lq.blog.system.vo.ArticleVO;
import cn.y3lq.blog.system.vo.BlogArticleVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 文章业务层
 */
public interface ArticleService {
    /**
     * 新增文章
     */
    void insertArticle(ArticleVO articleVO, HttpServletRequest request);

    /**
     * 条件查询文章列表
     */
    List<ArticleVO> selectArticleList(ArticleVO articleVO);

    /**
     * 查看是否允许修改文章
     */
    void checkEditArticleAllowed(ArticleVO articleVO, HttpServletRequest request);

    /**
     * 修改文章
     */
    void updateArticle(ArticleVO articleVO, HttpServletRequest request);

    /**
     * 查看删除文章是否允许
     */
    void checkDeleteArticleAllowed(Long[] articleIds, HttpServletRequest request);

    /**
     * 删除文章
     */
    void deleteArticle(Long[] articleIds, HttpServletRequest request);

    /**
     * 改变文章状态
     */
    void changeStatus(ArticleVO articleVO, HttpServletRequest request);

    /**
     * 获取博客文章
     */
    List<BlogArticleVO> selectBlogArticleList(ArticleVO articleVO, HttpServletRequest request);

    /**
     * 根据文章ID获取文章
     */
    BlogArticleVO getArticleById(Long articleId, HttpServletRequest request);

    /**
     * 获取首页推荐文章
     */
    List<BlogArticleVO> selectRecommendArticleList();

    /**
     * 评论文章
     */
    ArticleCommentVO commentArticle(ArticleCommentEntity articleCommentEntity, HttpServletRequest request);

    /**
     * 获取指定文章评论
     */
    List<ArticleCommentVO> selectArticleCommentList(Long articleId, Long userId);

    /**
     * 获取文章指定一级评论所有子评论
     */
    List<ArticleCommentVO> articleCommentChildren(Long commentId,Long userId);

    /**
     * 点赞文章
     */
    void postLikeArticle(Long articleId, Long userId);

    /**
     * 取消点赞文章
     */
    void cancelLikeArticle(Long articleId, Long userId);

    /**
     * 持久化 存储在redis的的文章点赞到mysql
     */
    void saveArticleLikedFromRedis();

    /**
     * 点赞文章评论
     */
    void postLikeArticleComment(Long commentId, Long userId);

    /**
     * 取消点赞文章评论
     */
    void cancelLikeArticleComment(Long commentId, Long userId);

    /**
     * (定时任务) 将存储在redis的文章评论点赞持久化到mysql
     */
    void saveArticleCommentLikedFromRedis();

    /**
     * 删除文章评论
     */
    void deleteArticleComment(Long userId, Long commentId);
}
