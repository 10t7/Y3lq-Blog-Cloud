package cn.y3lq.blog.system.mapper;

import cn.y3lq.blog.system.entity.DiaryCommentEntity;
import cn.y3lq.blog.system.vo.DiaryCommentVO;
import cn.y3lq.blog.system.vo.DiaryLikeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Y3lq
 * @description: 日记评论数据层
 */
@Mapper
public interface DiaryCommentMapper {


    List<DiaryCommentVO> selectCommentByDiaryId(@Param("diaryId") Long diaryId);

    void insertDiaryComment(DiaryCommentEntity diaryCommentEntity);

    DiaryCommentVO selectDiaryCommentByCommentId(@Param("commentId") Long commentId);

    void deleteDiaryComment(@Param("commentId") Long commentId);
}
