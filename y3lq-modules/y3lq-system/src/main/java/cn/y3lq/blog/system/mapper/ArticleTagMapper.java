package cn.y3lq.blog.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: Y3lq
 * @description: 文章标签关联表
 */
@Mapper
public interface ArticleTagMapper {
    /**
     * 新增文章和标签数组的关联
     */
    void insertArticleTags(@Param("articleId") Long articleId,@Param("tagIds") Long[] tagIds);

    /**
     * 删除文章所有标签
     */
    void deleteArticleTags(@Param("articleId") Long articleId);

    /**
     * 删除文章标签 根据 文章数组
     */
    void deleteArticleTagByArticleIds(@Param("articleIds") Long[] articleIds);
}
