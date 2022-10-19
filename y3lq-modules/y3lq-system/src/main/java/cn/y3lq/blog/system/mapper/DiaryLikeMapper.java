package cn.y3lq.blog.system.mapper;

import cn.y3lq.blog.system.entity.DiaryLikeEntity;
import cn.y3lq.blog.system.vo.DiaryLikeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 日记点赞数据层
 */
@Mapper
public interface DiaryLikeMapper {

    /**
     * 根据日记ID选择点赞列表
     */
    LinkedList<DiaryLikeVO> selectDiaryLikeByDiaryId(@Param("diaryId") Long diaryId);

    /**
     * 新增点赞
     */
    void insertDiaryLike(@Param("diaryLikeEntities") List<DiaryLikeEntity> diaryLikeEntities);

}
