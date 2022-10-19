package cn.y3lq.blog.system.service;

import cn.y3lq.blog.system.entity.DiaryCommentEntity;
import cn.y3lq.blog.system.entity.DiaryEntity;
import cn.y3lq.blog.system.vo.BlogDiaryVO;
import cn.y3lq.blog.system.vo.DiaryCommentVO;
import cn.y3lq.blog.system.vo.DiaryVO;

import java.util.List;

/**
 * @author: Y3lq
 * @description: 日记业务层
 */
public interface DiaryService {
    /**
     * 新增日记
     */
    void addDiary(DiaryEntity diaryEntity, Long userId);

    /**
     * 修改日记
     */
    void updateDiary(DiaryEntity diaryEntity, Long userId);

    /**
     * 批量删除日记
     */
    void deleteDiary(Long[] diaryIds);

    /**
     * 修改日记状态
     */
    void changeStatus(Long diaryId, String status, Long userId);

    /**
     * 查询日记列表
     *
     * @param currentUserId
     */
    List<BlogDiaryVO> selectBlogDiaryList(Long currentUserId);

    /**
     * 评论日记
     */
    DiaryCommentVO commentDiary(DiaryCommentEntity diaryCommentEntity);

    /**
     * 点赞日记
     */
    void likeDiary(Long diaryId, Long userId, String nickname);

    /**
     * 取消点赞日记
     */
    void cancelLikeDiary(Long diaryId, Long userId, String nickname);

    /**
     * 存储在redis的点赞持久化到mysql
     */
    void saveDiaryLikedFromRedis();


    /**
     * 根据日记ID查询日记
     */
    DiaryEntity selectDiaryByDiaryId(Long diaryId);

    /**
     * 删除自己的日记评论
     */
    void deleteDiaryComment(Long commentId, Long currentUserId);

    /**
     * 管理系统获取Diary列表
     */
    List<DiaryVO> selectDiaryList(String username, String status);


    /**
     * 改变日记排序
     */
    void changeDiaryOrderNum(Long diaryId, Integer orderNum, Long userId);

    /**
     * 获取推荐日记
     */
    List<BlogDiaryVO> getRecommendDiary(Long userId);

}
