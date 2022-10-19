package cn.y3lq.blog.system.service.impl;

import cn.y3lq.blog.common.core.constant.CacheConstants;
import cn.y3lq.blog.common.core.exception.ServiceException;
import cn.y3lq.blog.system.entity.DiaryCommentEntity;
import cn.y3lq.blog.system.entity.DiaryEntity;
import cn.y3lq.blog.system.entity.DiaryLikeEntity;
import cn.y3lq.blog.system.mapper.DiaryCommentMapper;
import cn.y3lq.blog.system.mapper.DiaryLikeMapper;
import cn.y3lq.blog.system.mapper.DiaryMapper;
import cn.y3lq.blog.system.mapper.UserMapper;
import cn.y3lq.blog.system.service.DiaryService;
import cn.y3lq.blog.system.vo.BlogDiaryVO;
import cn.y3lq.blog.system.vo.DiaryCommentVO;
import cn.y3lq.blog.system.vo.DiaryLikeVO;
import cn.y3lq.blog.system.vo.DiaryVO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Y3lq
 * @description: 日记业务层
 */
@Service
public class DiaryServiceImpl implements DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;

    @Autowired
    private DiaryCommentMapper diaryCommentMapper;

    @Autowired
    private DiaryLikeMapper diaryLikeMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 新增日记
     */
    @Override
    public void addDiary(DiaryEntity diaryEntity, Long userId) {
        // 初始化数据
        diaryEntity.setDiaryUserId(userId);
        diaryEntity.setDelFlag("0");
        diaryEntity.setStatus("0");
        diaryEntity.setCreateBy(String.valueOf(userId));
        diaryMapper.insertDiary(diaryEntity);
    }

    /**
     * 修改日记
     */
    @Override
    public void updateDiary(DiaryEntity diaryEntity, Long userId) {
        diaryEntity.setUpdateBy(String.valueOf(userId));
        diaryMapper.updateDiary(diaryEntity);
    }

    /**
     * 批量删除日记
     */
    @Override
    public void deleteDiary(Long[] diaryIds) {
        diaryMapper.deleteDiary(diaryIds);
    }

    /**
     * 修改日记状态
     */
    @Override
    public void changeStatus(Long diaryId, String status, Long userId) {
        diaryMapper.changeStatus(diaryId, status, String.valueOf(userId));
    }

    /**
     * 查询日记列表
     *
     * @param currentUserId 当前用户ID
     */
    @Override
    public List<BlogDiaryVO> selectBlogDiaryList(Long currentUserId) {
        List<BlogDiaryVO> blogDiaryVOS = diaryMapper.selectBlogDiaryList();
        Map<String, LinkedList<DiaryLikeVO>> diaryLikeMapFromRedis = getDiaryLikeFromRedis();
        for (BlogDiaryVO blogDiaryVO : blogDiaryVOS) {
            // 为每个日记获取评论相关数据
            List<DiaryCommentVO> diaryCommentEntities = diaryCommentMapper.selectCommentByDiaryId(blogDiaryVO.getDiaryId());
            blogDiaryVO.setComments(diaryCommentEntities);
            // 为每个日记设置点赞相关数据
            LinkedList<DiaryLikeVO> likeListFromMysql = diaryLikeMapper.selectDiaryLikeByDiaryId(blogDiaryVO.getDiaryId());
            LinkedList<DiaryLikeVO> diaryLikeFromRedis = diaryLikeMapFromRedis.get(String.valueOf(blogDiaryVO.getDiaryId()));
            if (ObjectUtils.isEmpty(diaryLikeFromRedis)) {
                // redis没有日记赞数据
                // 查看当前用户是否点赞
                String flag = isLike(likeListFromMysql, currentUserId);
                blogDiaryVO.setIsLike(flag);
                List<DiaryLikeVO> collect = likeListFromMysql.stream().filter(like -> {
                    return like.getStatus().equals(CacheConstants.LIKE_FLAG);
                }).collect(Collectors.toList());
                blogDiaryVO.setLikes(collect);
                continue;
            }
            List<DiaryLikeVO> collectLikeFromRedis = diaryLikeFromRedis.stream().filter(diaryLike -> {
                // 对比mysql和redis的点赞
                Long userId = diaryLike.getUserId();
                Boolean flag = true;
                for (DiaryLikeVO diaryLikeVO : likeListFromMysql) {
                    if (userId.equals(diaryLikeVO.getUserId())) {
                        String status = diaryLikeVO.getStatus();
                        if (status.equals(diaryLike.getStatus())) {
                            flag = false;
                        } else {
                            if (CacheConstants.UNLIKE_FLAG.equals(diaryLike.getStatus())) {
                                diaryLikeVO.setStatus(CacheConstants.UNLIKE_FLAG);
                                flag = false;
                            }
                        }
                    }
                }
                return flag;
            }).collect(Collectors.toList());
            for (DiaryLikeVO diaryLike : collectLikeFromRedis) {
                // 将redis中该日记点赞整合到likeListFromMysql
                likeListFromMysql.push(diaryLike);
            }
            // 查看当前用户是否点赞
            String flag = isLike(likeListFromMysql, currentUserId);
            blogDiaryVO.setIsLike(flag);
            // 清除status为0的点赞数据
            List<DiaryLikeVO> collect = likeListFromMysql.stream().filter(like -> {
                return like.getStatus().equals(CacheConstants.LIKE_FLAG);
            }).collect(Collectors.toList());
            blogDiaryVO.setLikes(collect);

        }
        return blogDiaryVOS;
    }

    /**
     * 查看指定用户ID是否点赞
     *
     * @param diaryLikeList 点赞集合
     * @param userId        用户ID
     */
    private String isLike(LinkedList<DiaryLikeVO> diaryLikeList, Long userId) {
        for (DiaryLikeVO diaryLike : diaryLikeList) {
            if (diaryLike.getUserId().equals(userId) && diaryLike.getStatus().equals(CacheConstants.LIKE_FLAG)) {
                return CacheConstants.LIKE_FLAG;
            }
        }
        return CacheConstants.UNLIKE_FLAG;
    }

    /**
     * 获取缓存在redis的日记赞Map
     */
    private Map<String, LinkedList<DiaryLikeVO>> getDiaryLikeFromRedis() {
        Map entries = redisTemplate.opsForHash().entries(CacheConstants.DIARY_LIKE);
        Iterator iterator = entries.keySet().iterator();
        Map<String, LinkedList<DiaryLikeVO>> diaryLikeMapFromRedis = new HashMap<>();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String[] split = key.split(":");
            String diaryId = split[0];
            String userId = split[1];
            String nickname = split[2];
            String status = (String) entries.get(key);
            DiaryLikeVO diaryLikeVO = new DiaryLikeVO();
            diaryLikeVO.setUserId(Long.parseLong(userId));
            diaryLikeVO.setStatus(status);
            diaryLikeVO.setNickname(nickname);
            LinkedList<DiaryLikeVO> diaryLikeEntities = null;
            diaryLikeEntities = diaryLikeMapFromRedis.get(diaryId);
            if (ObjectUtils.isEmpty(diaryLikeEntities)) {
                diaryLikeEntities = new LinkedList<>();
                diaryLikeMapFromRedis.put(diaryId, diaryLikeEntities);
            }

            diaryLikeEntities.add(diaryLikeVO);
        }
        return diaryLikeMapFromRedis;
    }

    /**
     * 评论日记
     */
    @Override
    public DiaryCommentVO commentDiary(DiaryCommentEntity diaryCommentEntity) {
        diaryCommentEntity.setDelFlag("0");
        if (diaryCommentEntity.getToUserId() == null) {
            diaryCommentEntity.setToUserId(0L);
        }
        diaryCommentMapper.insertDiaryComment(diaryCommentEntity);
        DiaryCommentVO diaryComment = diaryCommentMapper.selectDiaryCommentByCommentId(diaryCommentEntity.getCommentId());
        return diaryComment;
    }

    /**
     * 点赞日记
     */
    @Override
    public void likeDiary(Long diaryId, Long userId, String nickname) {
        redisTemplate.opsForHash().put(CacheConstants.DIARY_LIKE,
                diaryId + ":" + String.valueOf(userId) + ":" + nickname, CacheConstants.LIKE_FLAG);
    }

    /**
     * 取消点赞日记
     */
    @Override
    public void cancelLikeDiary(Long diaryId, Long userId, String nickname) {
        redisTemplate.opsForHash().put(CacheConstants.DIARY_LIKE,
                diaryId + ":" + String.valueOf(userId) + ":" + nickname, CacheConstants.UNLIKE_FLAG);

    }

    /**
     * 存储在redis的点赞持久化到mysql
     */
    @Override
    public void saveDiaryLikedFromRedis() {
        Map likeOrCancelLikeMap = redisTemplate.opsForHash().entries(CacheConstants.DIARY_LIKE);
        redisTemplate.delete(CacheConstants.DIARY_LIKE);
        List<DiaryLikeEntity> insertDiaryLikeEntities = new ArrayList<>();
        Iterator iterator = likeOrCancelLikeMap.keySet().iterator();

        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String[] split = key.split(":");
            String diaryId = split[0];
            String userId = split[1];
            String nickname = split[2];
            String status = (String) likeOrCancelLikeMap.get(key);
            DiaryLikeEntity diaryLikeEntity = new DiaryLikeEntity();
            diaryLikeEntity.setDiaryId(Long.parseLong(diaryId));
            diaryLikeEntity.setUserId(Long.parseLong(userId));
            diaryLikeEntity.setStatus(status);
            diaryLikeEntity.setNickname(nickname);
            insertDiaryLikeEntities.add(diaryLikeEntity);
        }

        if (insertDiaryLikeEntities.size() > 0) {
            diaryLikeMapper.insertDiaryLike(insertDiaryLikeEntities);
        }
    }

    /**
     * 根据日记ID查询日记
     */
    @Override
    public DiaryEntity selectDiaryByDiaryId(Long diaryId) {
        return diaryMapper.selectDiaryByDiaryId(diaryId);

    }

    /**
     * 删除自己的日记评论
     */
    @Override
    public void deleteDiaryComment(Long commentId, Long currentUserId) {
        DiaryCommentVO diaryCommentVO = diaryCommentMapper.selectDiaryCommentByCommentId(commentId);
        if (ObjectUtils.isEmpty(diaryCommentVO)) {
            throw new ServiceException("该评论不存在");
        }
        Long fromUserId = diaryCommentVO.getFromUserId();
        if (!currentUserId.equals(fromUserId)) {
            throw new ServiceException("无权删除他人评论");
        }
        diaryCommentMapper.deleteDiaryComment(commentId);
    }

    /**
     * 管理系统获取Diary列表
     */
    @Override
    public List<DiaryVO> selectDiaryList(String username, String status) {
        return diaryMapper.selectDiaryList(username, status);
    }

    /**
     * 改变日记排序
     */
    @Override
    public void changeDiaryOrderNum(Long diaryId, Integer orderNum, Long userId) {
        if(orderNum.equals(0)){
            orderNum = null;
        }
        diaryMapper.changeDiaryOrderNum(diaryId,orderNum,String.valueOf(userId));
    }

    /**
     * 获取推荐日记
     */
    @Override
    public List<BlogDiaryVO> getRecommendDiary(Long currentUserId) {
        List<BlogDiaryVO> blogDiaryVOS = diaryMapper.getRecommendDiary();
        Map<String, LinkedList<DiaryLikeVO>> diaryLikeMapFromRedis = getDiaryLikeFromRedis();
        for (BlogDiaryVO blogDiaryVO : blogDiaryVOS) {
            // 为每个日记获取评论相关数据
            List<DiaryCommentVO> diaryCommentEntities = diaryCommentMapper.selectCommentByDiaryId(blogDiaryVO.getDiaryId());
            blogDiaryVO.setComments(diaryCommentEntities);
            // 为每个日记设置点赞相关数据
            LinkedList<DiaryLikeVO> likeListFromMysql = diaryLikeMapper.selectDiaryLikeByDiaryId(blogDiaryVO.getDiaryId());
            LinkedList<DiaryLikeVO> diaryLikeFromRedis = diaryLikeMapFromRedis.get(String.valueOf(blogDiaryVO.getDiaryId()));
            if (ObjectUtils.isEmpty(diaryLikeFromRedis)) {
                // redis没有日记赞数据
                // 查看当前用户是否点赞
                String flag = isLike(likeListFromMysql, currentUserId);
                blogDiaryVO.setIsLike(flag);
                List<DiaryLikeVO> collect = likeListFromMysql.stream().filter(like -> {
                    return like.getStatus().equals(CacheConstants.LIKE_FLAG);
                }).collect(Collectors.toList());
                blogDiaryVO.setLikes(collect);
                continue;
            }
            List<DiaryLikeVO> collectLikeFromRedis = diaryLikeFromRedis.stream().filter(diaryLike -> {
                // 对比mysql和redis的点赞
                Long userId = diaryLike.getUserId();
                Boolean flag = true;
                for (DiaryLikeVO diaryLikeVO : likeListFromMysql) {
                    if (userId.equals(diaryLikeVO.getUserId())) {
                        String status = diaryLikeVO.getStatus();
                        if (status.equals(diaryLike.getStatus())) {
                            flag = false;
                        } else {
                            if (CacheConstants.UNLIKE_FLAG.equals(diaryLike.getStatus())) {
                                diaryLikeVO.setStatus(CacheConstants.UNLIKE_FLAG);
                                flag = false;
                            }
                        }
                    }
                }
                return flag;
            }).collect(Collectors.toList());
            for (DiaryLikeVO diaryLike : collectLikeFromRedis) {
                // 将redis中该日记点赞整合到likeListFromMysql
                likeListFromMysql.push(diaryLike);
            }
            // 查看当前用户是否点赞
            String flag = isLike(likeListFromMysql, currentUserId);
            blogDiaryVO.setIsLike(flag);
            // 清除status为0的点赞数据
            List<DiaryLikeVO> collect = likeListFromMysql.stream().filter(like -> {
                return like.getStatus().equals(CacheConstants.LIKE_FLAG);
            }).collect(Collectors.toList());
            blogDiaryVO.setLikes(collect);

        }
        return blogDiaryVOS;
    }


}
