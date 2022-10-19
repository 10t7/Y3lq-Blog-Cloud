package cn.y3lq.blog.system.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.y3lq.blog.common.core.constant.CacheConstants;
import cn.y3lq.blog.common.core.domain.AjaxResult;
import cn.y3lq.blog.common.core.util.SecurityUtils;
import cn.y3lq.blog.common.security.annotation.HasPermissions;
import cn.y3lq.blog.common.security.annotation.HasRoles;
import cn.y3lq.blog.common.security.annotation.ProhibitGuestAccess;
import cn.y3lq.blog.common.security.model.LoginUserModel;
import cn.y3lq.blog.system.entity.DiaryCommentEntity;
import cn.y3lq.blog.system.entity.DiaryEntity;
import cn.y3lq.blog.system.service.DiaryService;
import cn.y3lq.blog.system.vo.BlogDiaryVO;
import cn.y3lq.blog.system.vo.DiaryCommentVO;
import cn.y3lq.blog.system.vo.DiaryVO;
import com.alibaba.nacos.api.naming.pojo.healthcheck.impl.Http;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 日记控制器
 */
@RestController
@RequestMapping("/diary")
public class DiaryController {
    @Autowired
    private DiaryService diaryService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 获取推荐日记
     */
    @GetMapping("recommend-diary")
    public AjaxResult recommendDiary(HttpServletRequest request) {
        Long userId = 0L;
        try {
            userId = SecurityUtils.getCurrentUserId(request);
        } catch (Exception e) {
            // 忽略抛出的异常
        }
        List<BlogDiaryVO> blogDiaryVOList = diaryService.getRecommendDiary(userId);
        return AjaxResult.success(blogDiaryVOList);
    }

    /**
     * 改变日记排序（0则不推荐至首页）
     */
    @HasPermissions("content:diary:edit")
    @PutMapping("/change-diary-order-num")
    public AjaxResult changeDiaryOrderNum(DiaryEntity diaryEntity, HttpServletRequest request) {
        Long userId = SecurityUtils.getCurrentUserId(request);
        diaryService.changeDiaryOrderNum(diaryEntity.getDiaryId(), diaryEntity.getOrderNum(), userId);
        return AjaxResult.success();
    }


    /**
     * 删除自己评论
     */
    @HasRoles("y3lq_user")
    @DeleteMapping("/deleteComment/{commentId}")
    public AjaxResult deleteDiaryComment(@PathVariable("commentId") Long commentId, HttpServletRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId(request);
        diaryService.deleteDiaryComment(commentId, currentUserId);
        return AjaxResult.success();
    }


    /**
     * 取消点赞日记
     */
    @HasRoles("y3lq_user")
    @PutMapping("/cancel-like/{diaryId}")
    public AjaxResult cancelLikeDiary(@PathVariable("diaryId") Long diaryId, HttpServletRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId(request);
        Object userInfo = redisTemplate.opsForValue().get(CacheConstants.LOGIN_TOKEN_KEY + currentUserId);
        LoginUserModel userModel = JSONUtil.toBean(JSONUtil.parseObj(userInfo), LoginUserModel.class);
        diaryService.cancelLikeDiary(diaryId, currentUserId, userModel.getUser().getNickname());
        return AjaxResult.success().put("userId", currentUserId).put("nickname", userModel.getUser().getNickname());
    }

    /**
     * 点赞日记
     */
    @HasRoles("y3lq_user")
    @PutMapping("/like/{diaryId}")
    public AjaxResult likeDiary(@PathVariable("diaryId") Long diaryId, HttpServletRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId(request);
        Object userInfo = redisTemplate.opsForValue().get(CacheConstants.LOGIN_TOKEN_KEY + currentUserId);
        LoginUserModel userModel = JSONUtil.toBean(JSONUtil.parseObj(userInfo), LoginUserModel.class);
        diaryService.likeDiary(diaryId, currentUserId, userModel.getUser().getNickname());
        return AjaxResult.success().put("userId", currentUserId).put("nickname", userModel.getUser().getNickname());
    }

    /**
     * 新增指定日记评论
     */
    @HasRoles("y3lq_user")
    @PostMapping("/comment")
    public AjaxResult commentDiary(@Validated DiaryCommentEntity diaryCommentEntity, HttpServletRequest request) {
        Long userId = SecurityUtils.getCurrentUserId(request);
        diaryCommentEntity.setFromUserId(userId);
        DiaryCommentVO diaryComment = diaryService.commentDiary(diaryCommentEntity);
        return AjaxResult.success(diaryComment);
    }


    /**
     * 新增日记
     */
    @HasPermissions("content:diary:add")
    @PostMapping
    public AjaxResult add(@RequestBody DiaryEntity diaryEntity, HttpServletRequest request) {
        Long userId = SecurityUtils.getCurrentUserId(request);
        diaryService.addDiary(diaryEntity, userId);
        return AjaxResult.success();
    }

    /**
     * 修改日记
     */
    @HasPermissions("content:diary:edit")
    @PutMapping
    public AjaxResult update(@RequestBody DiaryEntity diaryEntity, HttpServletRequest request) {
        Long userId = SecurityUtils.getCurrentUserId(request);
        diaryService.updateDiary(diaryEntity, userId);
        return AjaxResult.success();
    }

    /**
     * 批量删除日记
     */
    @HasPermissions("content:diary:delete")
    @DeleteMapping("/{diaryIds}")
    public AjaxResult deleteBatch(@PathVariable("diaryIds") Long[] diaryIds) {
        diaryService.deleteDiary(diaryIds);
        return AjaxResult.success();
    }

    /**
     * 删除自己日记
     */
    @HasRoles("y3lq_user")
    @DeleteMapping("/deleteById/{diaryId}")
    public AjaxResult delete(@PathVariable("diaryId") Long diaryId, HttpServletRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId(request);
        DiaryEntity diaryEntity = diaryService.selectDiaryByDiaryId(diaryId);
        if (!currentUserId.equals(diaryEntity.getDiaryUserId())) {
            return AjaxResult.error("无权删除他人日记");
        }
        Long[] diaryIds = new Long[1];
        diaryIds[0] = diaryId;
        diaryService.deleteDiary(diaryIds);
        return AjaxResult.success();
    }

    /**
     * 修改日记状态
     */
    @HasPermissions("content:diary:edit")
    @PutMapping("/change-status")
    public AjaxResult changeStatus(DiaryEntity diaryEntity, HttpServletRequest request) {
        Long userId = SecurityUtils.getCurrentUserId(request);
        diaryService.changeStatus(diaryEntity.getDiaryId(), diaryEntity.getStatus(), userId);
        return AjaxResult.success();
    }


    /**
     * 博客分页获取日记列表
     */
    @GetMapping("/blog-list")
    public AjaxResult blogList(Integer pageNum, Integer pageSize, HttpServletRequest request) {
        PageHelper.startPage(pageNum, pageSize);
        Long userId = 0L;
        try {
            userId = SecurityUtils.getCurrentUserId(request);
        } catch (Exception e) {
            // 忽略抛出的异常
        }
        List<BlogDiaryVO> blogDiaryVOList = diaryService.selectBlogDiaryList(userId);
        PageInfo<BlogDiaryVO> pageInfo = new PageInfo<BlogDiaryVO>(blogDiaryVOList);
        return AjaxResult.success(pageInfo);
    }

    /**
     * 后台管理系统获取日记列表
     */
    @HasPermissions("content:diary:list")
    @GetMapping("/list")
    public AjaxResult list(Integer pageNum, Integer pageSize, String status, String username) {
        PageHelper.startPage(pageNum, pageSize);
        List<DiaryVO> diaryVOList = diaryService.selectDiaryList(username, status);
        PageInfo<DiaryVO> pageInfo = new PageInfo<DiaryVO>(diaryVOList);
        return AjaxResult.success(pageInfo);
    }
}
