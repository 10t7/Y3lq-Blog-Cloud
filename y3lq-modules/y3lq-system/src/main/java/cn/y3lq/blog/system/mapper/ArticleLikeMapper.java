package cn.y3lq.blog.system.mapper;

import cn.y3lq.blog.system.entity.ArticleCommentLikeEntity;
import cn.y3lq.blog.system.entity.ArticleLikeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Y3lq
 * @description: 文章点赞数据层
 */
@Mapper
public interface ArticleLikeMapper {
    /**
     * 新增文章点赞
     */
    void insertArticleLike(@Param("addArticleLikeEntities") List<ArticleLikeEntity> addArticleLikeEntities);

    /**
     * 删除文章点赞
     */
    void deleteArticleLike(@Param("removeArticleLikeEntities") List<ArticleLikeEntity> removeArticleLikeEntities);

    /**
     * 查看是否有无相应记录
     */
    ArticleLikeEntity selectRecord(@Param("articleId") Long articleId, @Param("userId") Long currentUserId);

    /**
     * 查看文章有多少个点赞
     */
    int countLikeNum(@Param("articleId") Long articleId);

}
