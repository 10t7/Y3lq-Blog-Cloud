package cn.y3lq.blog.system.mapper;

import cn.y3lq.blog.system.entity.ArticleCommentEntity;
import cn.y3lq.blog.system.entity.ArticleEntity;
import cn.y3lq.blog.system.vo.ArticleVO;
import cn.y3lq.blog.system.vo.BlogArticleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 文章数据层
 */
@Mapper
public interface ArticleMapper {
    /**
     * 新增文章
     */
    void insertArticle(ArticleEntity articleEntity);

    /**
     * 条件查询文章列表
     */
    List<ArticleVO> selectArticleList(ArticleVO articleVO);

    /**
     * 更新文章
     */
    void updateArticle(ArticleEntity articleEntity);

    /**
     * 删除文章
     */
    void deleteArticle(@Param("articleIds") Long[] articleIds);

    /**
     * h获取博客文章
     *
     * @param tagId 文章标签
     */
    List<BlogArticleVO> selectBlogArticleList(@Param("tagId") Long tagId);

    /**
     * 根据文章ID获取文章
     */
    BlogArticleVO getArticleById(@Param("articleId") Long articleId);

    /**
     * 获取首页推荐文章
     */
    List<BlogArticleVO> selectRecommendArticleList();


    /**
     * 给文章评论数加1
     */
    void addCommentCount(@Param("articleId") Long articleId);

    void reduceArticleCommentCount(@Param("articleId") Long articleId, @Param("count") int count);
}
