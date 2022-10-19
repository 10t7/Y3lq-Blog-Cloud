package cn.y3lq.blog.system.mapper;

import cn.y3lq.blog.system.entity.ArticleCommentLikeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Y3lq
 * @description: 文章评论点赞数据层
 */
@Mapper
public interface ArticleCommentLikeMapper {

    /**
     * 新增文章评论点赞
     */
    void insertArticleCommentLike(@Param("addArticleCommentLikeEntities") List<ArticleCommentLikeEntity> addArticleCommentLikeEntities);

    /**
     * 删除文章评论点赞
     */
    void deleteArticleCommentLike(@Param("removeArticleCommentLikeEntities") List<ArticleCommentLikeEntity> removeArticleCommentLikeEntities);

    /**
     * 查看指定文章评论点赞数
     */
    int countArticleCommentLike(@Param("commentId") Long commentId);

    /**
     * 根据评论ID和用户ID获取唯一一条数据（这两个为联合唯一索引）
     */
    ArticleCommentLikeEntity selectOne(@Param("commentId") Long commentId,@Param("userId") Long userId);
}
