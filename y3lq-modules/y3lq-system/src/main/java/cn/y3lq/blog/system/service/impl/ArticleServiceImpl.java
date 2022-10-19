package cn.y3lq.blog.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.y3lq.blog.common.core.constant.CacheConstants;
import cn.y3lq.blog.common.core.exception.ServiceException;
import cn.y3lq.blog.common.core.util.SecurityUtils;
import cn.y3lq.blog.system.entity.ArticleCommentEntity;
import cn.y3lq.blog.system.entity.ArticleCommentLikeEntity;
import cn.y3lq.blog.system.entity.ArticleEntity;
import cn.y3lq.blog.system.entity.ArticleLikeEntity;
import cn.y3lq.blog.system.mapper.*;
import cn.y3lq.blog.system.service.ArticleService;
import cn.y3lq.blog.system.vo.ArticleCommentVO;
import cn.y3lq.blog.system.vo.ArticleVO;
import cn.y3lq.blog.system.vo.BlogArticleVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author: Y3lq
 * @description: 文章业务层
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private ArticleCommentMapper articleCommentMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    @Autowired
    private ArticleCommentLikeMapper articleCommentLikeMapper;



    /**
     * 新增文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertArticle(ArticleVO articleVO, HttpServletRequest request) {
        ArticleEntity articleEntity = new ArticleEntity();
        BeanUtils.copyProperties(articleVO, articleEntity);
        // 初始化ArticleEntity
        initArticleEntity(articleEntity, request);
        // 新增文章
        articleMapper.insertArticle(articleEntity);
        // 处理文章和标签的关联
        handleArticleTag(articleEntity.getArticleId(), articleVO.getTagIds());
    }

    /**
     * 条件查询文章列表
     */
    @Override
    public List<ArticleVO> selectArticleList(ArticleVO articleVO) {
        List<ArticleVO> articleVOS = articleMapper.selectArticleList(articleVO);
        for (ArticleVO article : articleVOS) {
            int mysqlLikeCount = articleLikeMapper.countLikeNum(article.getArticleId());
            int redisLikeCount = getRedisArticleLikeCount(article.getArticleId());
            article.setLikeCount(mysqlLikeCount + redisLikeCount);
        }
        return articleVOS;
    }

    /**
     * 查看是否允许修改文章
     */
    @Override
    public void checkEditArticleAllowed(ArticleVO articleVO, HttpServletRequest request) {
        if (SecurityUtils.isSuperAdmin(request)) {
            // 超级管理员放行
            return;
        }
        Long[] userId = new Long[]{articleVO.getAuthorUserId()};
        if (SecurityUtils.isHasSuperAdminUserId(userId)) {
            throw new ServiceException("无权修改超级管理员文章");
        }
    }

    /**
     * 修改文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(ArticleVO articleVO, HttpServletRequest request) {
        ArticleEntity articleEntity = new ArticleEntity();
        BeanUtils.copyProperties(articleVO, articleEntity);
        articleEntity.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId(request)));
        articleMapper.updateArticle(articleEntity);
        articleTagMapper.deleteArticleTags(articleVO.getArticleId());
        articleTagMapper.insertArticleTags(articleVO.getArticleId(), articleVO.getTagIds());
    }

    /**
     * 查看删除文章是否允许
     */
    @Override
    public void checkDeleteArticleAllowed(Long[] articleIds, HttpServletRequest request) {
    }

    /**
     * 批量删除文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(Long[] articleIds, HttpServletRequest request) {
        articleMapper.deleteArticle(articleIds);
        articleTagMapper.deleteArticleTagByArticleIds(articleIds);
    }

    /**
     * 改变文章状态
     */
    @Override
    public void changeStatus(ArticleVO articleVO, HttpServletRequest request) {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setArticleId(articleVO.getArticleId());
        articleEntity.setStatus(articleVO.getStatus());
        articleEntity.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId(request)));
        articleMapper.updateArticle(articleEntity);
    }

    /**
     * 获取博客文章
     */
    @Override
    public List<BlogArticleVO> selectBlogArticleList(ArticleVO articleVO, HttpServletRequest request) {
        Long tagId = articleVO.getTagId();
        List<BlogArticleVO> blogArticleVOS = articleMapper.selectBlogArticleList(tagId);
        Long currentUserId = null;
        try {
            currentUserId = SecurityUtils.getCurrentUserId(request);
        } catch (Exception e) {
            // 忽略抛出异常
        }
        for (BlogArticleVO blogArticleVO : blogArticleVOS) {
            int mysqlArticleLikeCount = articleLikeMapper.countLikeNum(blogArticleVO.getArticleId());
            int redisArticleLikeCount = getRedisArticleLikeCount(blogArticleVO.getArticleId());
            blogArticleVO.setLikeCount(mysqlArticleLikeCount + redisArticleLikeCount);
            String flag = (String) redisTemplate.opsForHash().get(CacheConstants.ARTICLE_LIKE, blogArticleVO.getArticleId() + "::" + currentUserId);
            if (StringUtils.isNotEmpty(flag)) {
                blogArticleVO.setIsLike(flag);
                continue;
            }
            ArticleLikeEntity articleLikeEntity = articleLikeMapper.selectRecord(blogArticleVO.getArticleId(), currentUserId);
            if (ObjectUtils.isEmpty(articleLikeEntity)) {
                blogArticleVO.setIsLike("0");
            } else {
                blogArticleVO.setIsLike("1");
            }
        }
        return blogArticleVOS;
    }

    /**
     * 根据文章ID获取文章
     */
    @Override
    public BlogArticleVO getArticleById(Long articleId, HttpServletRequest request) {
        Long currentUserId = 0L;
        try {
            currentUserId = SecurityUtils.getCurrentUserId(request);
        } catch (Exception e) {
            // 忽略抛出的异常
        }
        BlogArticleVO blogArticleVO = articleMapper.getArticleById(articleId);
        int mysqlLikeCount = articleLikeMapper.countLikeNum(articleId);
        int redisLikeCount = getRedisArticleLikeCount(blogArticleVO.getArticleId());
        blogArticleVO.setLikeCount(mysqlLikeCount + redisLikeCount);
        String flag = (String) redisTemplate.opsForHash().get(CacheConstants.ARTICLE_LIKE, blogArticleVO.getArticleId() + "::" + currentUserId);
        if (StringUtils.isNotEmpty(flag)) {
            blogArticleVO.setIsLike(flag);
            return blogArticleVO;
        }
        ArticleLikeEntity articleLikeEntity = articleLikeMapper.selectRecord(blogArticleVO.getArticleId(), currentUserId);
        if (ObjectUtils.isEmpty(articleLikeEntity)) {
            blogArticleVO.setIsLike("0");
        } else {
            blogArticleVO.setIsLike("1");
        }
        return blogArticleVO;
    }

    /**
     * 获取文章在redis的点赞数
     */
    private int getRedisArticleLikeCount(Long articleId) {
        Map articleLikeMap = redisTemplate.opsForHash().entries(CacheConstants.ARTICLE_LIKE);
        LongAdder redisArticleLikeCount = new LongAdder();
        Iterator iterator = articleLikeMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String[] split = key.split("::");
            if (split[0].equals(String.valueOf(articleId)) && "1".equals(articleLikeMap.get(key))) {
                redisArticleLikeCount.increment();
            }
            if (split[0].equals(String.valueOf(articleId)) && "0".equals(articleLikeMap.get(key))) {
                redisArticleLikeCount.decrement();

            }
        }
        Long sum = redisArticleLikeCount.sum();
        if (sum <= 0) {
            sum = 0L;
        }
        int i = sum.intValue();
        return i;


    }

    /**
     * 获取首页推荐文章
     */
    @Override
    public List<BlogArticleVO> selectRecommendArticleList() {

        List<BlogArticleVO> blogArticleVOS = articleMapper.selectRecommendArticleList();
        for (BlogArticleVO blogArticleVO : blogArticleVOS) {
            int mysqlLikeCount = articleLikeMapper.countLikeNum(blogArticleVO.getArticleId());
            int redisLikeCount = getRedisArticleLikeCount(blogArticleVO.getArticleId());
            blogArticleVO.setLikeCount(mysqlLikeCount + redisLikeCount);
        }
        return blogArticleVOS;
    }

    /**
     * 评论文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleCommentVO commentArticle(ArticleCommentEntity articleCommentEntity, HttpServletRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId(request);
        if (articleCommentEntity.getFirstCommentId() == null) {
            // 则为第一层评论，设置为0 作为标记
            articleCommentEntity.setFirstCommentId(0L);
        }
        articleCommentEntity.setDelFlag("0");
        articleCommentEntity.setFromUserId(currentUserId);
        articleCommentMapper.commentArticle(articleCommentEntity);
        articleMapper.addCommentCount(articleCommentEntity.getArticleId());

        ArticleCommentVO articleCommentVO = articleCommentMapper.selectArticleCommentById(articleCommentEntity.getCommentId());
        articleCommentVO.setChildrenCommentCount(0);
        return articleCommentVO;
    }

    /**
     * 获取指定文章评论
     */
    @Override
    public List<ArticleCommentVO> selectArticleCommentList(Long articleId, Long userId) {
        List<ArticleCommentVO> articleCommentVOS = articleCommentMapper.selectArticleCommentList(articleId);
        articleCommentVOS.stream().forEach(articleCommentVO -> {
            // 设置该一级评论的子评论数量
            int count = articleCommentMapper.countArticleComment(articleCommentVO.getCommentId());
            articleCommentVO.setChildrenCommentCount(count);
            // 获取指定评论点赞数量
            int likeCount = getArticleCommentLikeCount(articleCommentVO.getCommentId());
            articleCommentVO.setLikeCount(likeCount);
            // 判断当前用户是否点赞
            String flag = isLikeArticleComment(articleCommentVO.getCommentId(), userId);
            articleCommentVO.setIsLike(flag);
        });
        return articleCommentVOS;
    }

    /**
     * 判断指定用户是否点赞指定评论
     */
    private String isLikeArticleComment(Long commentId, Long userId) {
        String flag = (String) redisTemplate.opsForHash().get(CacheConstants.ARTICLE_COMMENT_LIKE,
                commentId + "::" + userId);
        if (StringUtils.isNotEmpty(flag)) {
            // 查到该用户存储在redis点赞或者取消点赞数据
            return flag;
        }
        ArticleCommentLikeEntity articleCommentLikeEntity = articleCommentLikeMapper.selectOne(commentId, userId);
        if (ObjectUtils.isEmpty(articleCommentLikeEntity)) {
            return CacheConstants.ARTICLE_COMMENT_UNLIKE_FLAG;
        } else {
            return CacheConstants.ARTICLE_COMMENT_LIKE_FLAG;
        }
    }

    /**
     * 获取评论点赞数量
     */
    private int getArticleCommentLikeCount(Long commentId) {
        // 获取mysql的点赞数
        int likeCountFromMysql = articleCommentLikeMapper.countArticleCommentLike(commentId);
//        Long[] commentId = articleCommentLikeMapper.selectArticleComment()
        LongAdder likeCountFromRedis = new LongAdder();
        // 获取redis的点赞数
        Map articleCommentLikeMap = redisTemplate.opsForHash().entries(CacheConstants.ARTICLE_COMMENT_LIKE);
        if (!CollectionUtil.isEmpty(articleCommentLikeMap) || !articleCommentLikeMap.keySet().isEmpty()) {
            Iterator iterator = articleCommentLikeMap.keySet().iterator();
            // 遍历 找到该评论存储在redis的所有点赞和取消点赞
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String[] split = key.split("::");
                if (split[0].equals(String.valueOf(commentId)) && "0".equals(articleCommentLikeMap.get(key))) {
                    likeCountFromRedis.decrement();
                }
                if (split[0].equals(String.valueOf(commentId)) && "1".equals(articleCommentLikeMap.get(key))) {
                    likeCountFromRedis.increment();
                }
            }
        }
//        if (likeCountFromRedis <= 0) {
//            likeCountFromRedis = 0;
//        }
        Long sum = likeCountFromRedis.sum();
        int likeCountFromRediss = sum.intValue();
        int count = likeCountFromRediss + likeCountFromMysql;
        if (count <= 0) {
            count = 0;
        }
        return count;
    }

    /**
     * 获取文章指定一级评论所有子评论
     */
    @Override
    public List<ArticleCommentVO> articleCommentChildren(Long commentId, Long userId) {
        List<ArticleCommentVO> articleCommentVOList = articleCommentMapper.articleCommentChildren(commentId);
//        for (ArticleCommentVO articleCommentVO : articleCommentVOList) {
//            // 获取指定评论点赞数量
//            int likeCount = getArticleCommentLikeCount(articleCommentVO.getCommentId());
//            articleCommentVO.setLikeCount(likeCount);
//            // 判断当前用户是否点赞
//            String flag = isLikeArticleComment(articleCommentVO.getCommentId(), userId);
//            articleCommentVO.setIsLike(flag);
//        }
        return articleCommentVOList;
    }

    /**
     * 点赞文章
     */
    @Override
    public void postLikeArticle(Long articleId, Long userId) {
//        String articleLikeKey = CacheConstants.ARTICLE_LIKE + articleId;
        String key = articleId + "::" + userId;
        redisTemplate.opsForHash().put(CacheConstants.ARTICLE_LIKE, key, CacheConstants.ARTICLE_LIKE_FLAG);
    }

    /**
     * 取消点赞文章
     */
    @Override
    public void cancelLikeArticle(Long articleId, Long userId) {
//        String articleLikeKey = CacheConstants.ARTICLE_LIKE + articleId;
        String key = articleId + "::" + userId;
        redisTemplate.opsForHash().put(CacheConstants.ARTICLE_LIKE, key, CacheConstants.ARTICLE_UNLIKE_FLAG);
    }

    /**
     * 持久化 存储在redis的的文章点赞到mysql
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveArticleLikedFromRedis() {
        Map articleLikeMap = redisTemplate.opsForHash().entries(CacheConstants.ARTICLE_LIKE);
        redisTemplate.delete(CacheConstants.ARTICLE_LIKE);
        List<ArticleLikeEntity> addArticleLikeEntities = new ArrayList<>();
        List<ArticleLikeEntity> removeArticleLikeEntities = new ArrayList<>();
        Iterator iterator = articleLikeMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String[] split = key.split("::");
            if ("0".equals(articleLikeMap.get(key))) {
                // 为 0 则删除点赞
                ArticleLikeEntity articleLikeEntity = new ArticleLikeEntity();
                articleLikeEntity.setArticleId(Long.parseLong(split[0]));
                articleLikeEntity.setUserId(Long.parseLong(split[1]));
                removeArticleLikeEntities.add(articleLikeEntity);
            } else {
                // 为点赞
                ArticleLikeEntity articleLikeEntity = new ArticleLikeEntity();
                articleLikeEntity.setArticleId(Long.parseLong(split[0]));
                articleLikeEntity.setUserId(Long.parseLong(split[1]));
                addArticleLikeEntities.add(articleLikeEntity);
            }
        }
        if (addArticleLikeEntities.size() != 0) {
            articleLikeMapper.insertArticleLike(addArticleLikeEntities);
        }
        if (removeArticleLikeEntities.size() != 0) {
            articleLikeMapper.deleteArticleLike(removeArticleLikeEntities);
        }
    }

    /**
     * 点赞文章评论
     */
    @Override
    public void postLikeArticleComment(Long commentId, Long userId) {
        String key = commentId + "::" + userId;
        redisTemplate.opsForHash().put(CacheConstants.ARTICLE_COMMENT_LIKE, key, CacheConstants.ARTICLE_COMMENT_LIKE_FLAG);
    }

    /**
     * 取消点赞文章评论
     */
    @Override
    public void cancelLikeArticleComment(Long commentId, Long userId) {
        String key = commentId + "::" + userId;
        redisTemplate.opsForHash().put(CacheConstants.ARTICLE_COMMENT_LIKE, key, CacheConstants.ARTICLE_COMMENT_UNLIKE_FLAG);

    }

    /**
     * (定时任务) 将存储在redis的文章评论点赞持久化到mysql
     */
    @Override
    public void saveArticleCommentLikedFromRedis() {
        Map articleLikeMap = redisTemplate.opsForHash().entries(CacheConstants.ARTICLE_COMMENT_LIKE);
        redisTemplate.delete(CacheConstants.ARTICLE_COMMENT_LIKE);
        List<ArticleCommentLikeEntity> addArticleCommentLikeEntities = new ArrayList<>();
        List<ArticleCommentLikeEntity> removeArticleCommentLikeEntities = new ArrayList<>();
        Iterator iterator = articleLikeMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String[] split = key.split("::");
            if ("0".equals(articleLikeMap.get(key))) {
                // 为 0 则删除点赞
                ArticleCommentLikeEntity articleCommentLikeEntity = new ArticleCommentLikeEntity();
                articleCommentLikeEntity.setCommentId(Long.parseLong(split[0]));
                articleCommentLikeEntity.setUserId(Long.parseLong(split[1]));
                removeArticleCommentLikeEntities.add(articleCommentLikeEntity);
            } else {
                // 为点赞
                ArticleCommentLikeEntity articleCommentLikeEntity = new ArticleCommentLikeEntity();
                articleCommentLikeEntity.setCommentId(Long.parseLong(split[0]));
                articleCommentLikeEntity.setUserId(Long.parseLong(split[1]));
                addArticleCommentLikeEntities.add(articleCommentLikeEntity);
            }
        }
        if (addArticleCommentLikeEntities.size() != 0) {
            articleCommentLikeMapper.insertArticleCommentLike(addArticleCommentLikeEntities);
        }
        if (removeArticleCommentLikeEntities.size() != 0) {
            articleCommentLikeMapper.deleteArticleCommentLike(removeArticleCommentLikeEntities);
        }
    }

    /**
     * 删除文章评论
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticleComment(Long userId, Long commentId) {
        ArticleCommentVO articleCommentVO = articleCommentMapper.selectArticleCommentById(commentId);
        if (!articleCommentVO.getFromUserId().equals(userId)) {
            throw new ServiceException("无权删除他人评论");
        }
        if (articleCommentVO.getFirstCommentId().equals(0L)) {
            // 为一级评论
            // 找到子评论的数量
            int count = articleCommentMapper.countChildComment(commentId);
            count = count + 1;
            articleCommentMapper.deleteArticleCommentAndChildren(commentId);
            articleMapper.reduceArticleCommentCount(articleCommentVO.getArticleId(), count);
        } else {
            // 评论的评论
            articleCommentMapper.deleteArticleComment(commentId);
            int count = 1;
            articleMapper.reduceArticleCommentCount(articleCommentVO.getArticleId(), count);
        }
    }


    /**
     * 处理文章和标签的关联
     *
     * @param articleId 文章ID
     * @param tagIds    标签数组
     */
    private void handleArticleTag(Long articleId, Long[] tagIds) {
        if (tagIds == null || tagIds.length == 0) {
            return;
        }
        if (tagIds.length > 5) {
            throw new ServiceException("标签数量最多5个");
        }
        articleTagMapper.insertArticleTags(articleId, tagIds);
    }

    /**
     * 初始化ArticleEntity
     */
    private void initArticleEntity(ArticleEntity articleEntity, HttpServletRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId(request);
        articleEntity.setCreateBy(String.valueOf(currentUserId));
        articleEntity.setDelFlag("0");
        articleEntity.setStatus("0");
        articleEntity.setCommentCount(0);
        articleEntity.setLikeCount(0);
    }
}
