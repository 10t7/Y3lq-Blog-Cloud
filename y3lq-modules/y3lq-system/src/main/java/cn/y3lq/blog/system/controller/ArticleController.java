package cn.y3lq.blog.system.controller;

import cn.y3lq.blog.common.core.domain.AjaxResult;
import cn.y3lq.blog.common.core.util.SecurityUtils;
import cn.y3lq.blog.common.core.valid.AddGroup;
import cn.y3lq.blog.common.core.valid.UpdateGroup;
import cn.y3lq.blog.common.security.annotation.HasPermissions;
import cn.y3lq.blog.common.security.annotation.HasRoles;
import cn.y3lq.blog.common.security.annotation.ProhibitGuestAccess;
import cn.y3lq.blog.system.entity.ArticleCommentEntity;
import cn.y3lq.blog.system.entity.ArticleEntity;
import cn.y3lq.blog.system.entity.RoleEntity;
import cn.y3lq.blog.system.service.ArticleService;
import cn.y3lq.blog.system.vo.ArticleCommentVO;
import cn.y3lq.blog.system.vo.ArticleVO;
import cn.y3lq.blog.system.vo.BlogArticleVO;
import cn.y3lq.blog.system.vo.RoleListVO;
import com.alibaba.nacos.api.naming.pojo.healthcheck.impl.Http;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 文章控制器
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 点赞文章评论
     */
    @HasRoles("y3lq_user")
    @PutMapping("/post-like-article-comment")
    public AjaxResult postLikeArticleComment(Long commentId, HttpServletRequest request) {
        Long userId = SecurityUtils.getCurrentUserId(request);
        articleService.postLikeArticleComment(commentId, userId);
        return AjaxResult.success();
    }


    /**
     * 取消点赞文章评论
     */
    @HasRoles("y3lq_user")
    @PutMapping("/cancel-like-article-comment")
    public AjaxResult cancelLikeArticleComment(Long commentId, HttpServletRequest request) {
        Long userId = SecurityUtils.getCurrentUserId(request);
        articleService.cancelLikeArticleComment(commentId, userId);
        return AjaxResult.success();
    }

    /**
     * 取消点赞文章
     */
    @HasRoles("y3lq_user")
    @PutMapping("/cancel-like-article")
    public AjaxResult cancelLikeArticle(Long articleId, HttpServletRequest request) {
        Long userId = SecurityUtils.getCurrentUserId(request);
        articleService.cancelLikeArticle(articleId, userId);
        return AjaxResult.success();
    }

    /**
     * 点赞文章
     */
    @HasRoles("y3lq_user")
    @PutMapping("/post-like-article")
    public AjaxResult postLikeArticle(Long articleId, HttpServletRequest request) {
        Long userId = SecurityUtils.getCurrentUserId(request);
        articleService.postLikeArticle(articleId, userId);
        return AjaxResult.success();
    }

    /**
     * 删除文章评论
     */
    @HasRoles("y3lq_user")
    @DeleteMapping("/article-comment")
    public AjaxResult deleteArticleComment(@RequestParam("commentId") Long commentId, HttpServletRequest request) {
        Long userId = SecurityUtils.getCurrentUserId(request);
        articleService.deleteArticleComment(userId, commentId);
        return AjaxResult.success();
    }

    /**
     * 获取文章指定一级评论所有子评论
     */
    @GetMapping("/articleCommentChildren")
    public AjaxResult articleCommentChildren(@RequestParam("commentId") Long commentId, HttpServletRequest request) {
        Long userId = 0L;
        try {
            userId = SecurityUtils.getCurrentUserId(request);
        } catch (Exception e) {
            // 忽略抛出的异常
        }
        List<ArticleCommentVO> articleCommentVOList = articleService.articleCommentChildren(commentId, userId);
        return AjaxResult.success(articleCommentVOList);
    }

    /**
     * 博客分页获取指定文章评论
     */
    @GetMapping("/articleCommentList")
    public AjaxResult selectArticleCommentList(@RequestParam("articleId") Long articleId, Integer pageNum, Integer pageSize, HttpServletRequest request) {
        PageHelper.startPage(pageNum, pageSize);
        Long userId = 0L;
        try {
            userId = SecurityUtils.getCurrentUserId(request);
        } catch (Exception e) {
            // 忽略抛出的异常
        }
        List<ArticleCommentVO> articleVOList = articleService.selectArticleCommentList(articleId, userId);
        PageInfo<ArticleCommentVO> pageInfo = new PageInfo<>(articleVOList);
        return AjaxResult.success(pageInfo);
    }

    /**
     * 评论文章
     */
    @HasRoles("y3lq_user")
    @PostMapping("/comment")
    public AjaxResult commentArticle(@RequestBody @Validated ArticleCommentEntity articleCommentEntity, HttpServletRequest request) {
        ArticleCommentVO articleCommentVO = articleService.commentArticle(articleCommentEntity, request);
        return AjaxResult.success(articleCommentVO);
    }

    /**
     * 获取首页推荐文章
     */
    @GetMapping("/recommend")
    public AjaxResult getRecommendArticleList() {
        List<BlogArticleVO> blogArticleVO = articleService.selectRecommendArticleList();
        return AjaxResult.success(blogArticleVO);
    }

    /**
     * 根据id获取文章
     */
    @GetMapping("/get")
    public AjaxResult getArticleById(@RequestParam("articleId") Long articleId, HttpServletRequest request) {
        BlogArticleVO blogArticleVO = articleService.getArticleById(articleId, request);
        return AjaxResult.success(blogArticleVO);
    }

    /**
     * 后台管理系统分页条件查询文章列表
     */
    @HasPermissions("content:article:list")
    @GetMapping("/list")
    public AjaxResult list(ArticleVO articleVO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ArticleVO> articleVOList = articleService.selectArticleList(articleVO);
        PageInfo<ArticleVO> pageInfo = new PageInfo<>(articleVOList);
        return AjaxResult.success(pageInfo);
    }



    /**
     * 前端博客文章分页条件查询文章列表
     */
    @GetMapping("/blog-list")
    public AjaxResult blogList(ArticleVO articleVO, Integer pageNum, Integer pageSize, HttpServletRequest request) {
        PageHelper.startPage(pageNum, pageSize);
        List<BlogArticleVO> blogArticleVOS = articleService.selectBlogArticleList(articleVO, request);
        PageInfo<BlogArticleVO> pageInfo = new PageInfo<>(blogArticleVOS);
        return AjaxResult.success(pageInfo);
    }

    /**
     * 新增文章
     */
    @HasPermissions("content:article:add")
    @PostMapping
    public AjaxResult add(@Validated(AddGroup.class) @RequestBody ArticleVO articleVO, HttpServletRequest request) {
        articleService.insertArticle(articleVO, request);
        return AjaxResult.success();
    }

    /**
     * 修改文章
     */
    @HasPermissions("content:article:edit")
    @PutMapping
    public AjaxResult edit(@RequestBody ArticleVO articleVO, HttpServletRequest request) {
        articleService.checkEditArticleAllowed(articleVO, request);
        articleService.updateArticle(articleVO, request);
        return AjaxResult.success();
    }

    /**
     * 删除文章
     */
    @HasPermissions("content:article:delete")
    @DeleteMapping("/{articleIds}")
    public AjaxResult delete(@PathVariable("articleIds") Long[] articleIds, HttpServletRequest request) {
        articleService.checkDeleteArticleAllowed(articleIds, request);
        articleService.deleteArticle(articleIds, request);
        return AjaxResult.success();
    }

    /**
     * 修改文章状态
     */
    @HasPermissions("content:article:edit")
    @PutMapping("/change-status")
    @ProhibitGuestAccess
    public AjaxResult changeArticleStatus(ArticleVO articleVO, HttpServletRequest request) {
        articleService.changeStatus(articleVO, request);
        return AjaxResult.success();

    }

}
