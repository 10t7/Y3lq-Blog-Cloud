package cn.y3lq.blog.system.mapper;

import cn.y3lq.blog.system.entity.DiaryEntity;
import cn.y3lq.blog.system.vo.BlogDiaryVO;
import cn.y3lq.blog.system.vo.DiaryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Y3lq
 * @description: 日记数据层
 */
@Mapper
public interface DiaryMapper {

    /**
     * 新增日记
     */
    void insertDiary(DiaryEntity diaryEntity);

    /**
     * 修改日记
     */
    void updateDiary(DiaryEntity diaryEntity);

    /**
     * 批量删除日记
     */
    void deleteDiary(@Param("diaryIds") Long[] diaryIds);

    /**
     * 修改日记状态
     */
    void changeStatus(@Param("diaryId") Long diaryId, @Param("status") String status, @Param("userId") String userId);

    /**
     * 查询日记列表
     */
    List<BlogDiaryVO> selectBlogDiaryList();

    /**
     * 根据日记ID查询日记
     */
    DiaryEntity selectDiaryByDiaryId(@Param("diaryId") Long diaryId);

    /**
     * 根据条件获取Diary列表
     */
    List<DiaryVO> selectDiaryList(@Param("username") String username, @Param("status") String status);

    /**
     * 改变日记排序
     */
    void changeDiaryOrderNum(@Param("diaryId") Long diaryId, @Param("orderNum") Integer orderNum, @Param("userId") String userId);

    /**
     * 获取推荐日记
     */
    List<BlogDiaryVO> getRecommendDiary();
}
