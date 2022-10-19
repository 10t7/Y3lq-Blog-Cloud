package cn.y3lq.blog.system.mapper;

import cn.y3lq.blog.system.entity.TagEntity;
import cn.y3lq.blog.system.vo.TagVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Y3lq
 * @description: 标签数据层
 */
@Mapper
public interface TagMapper {
    /**
     * 条件获取标签列表
     */
    List<TagVO> selectTagList(TagEntity tagEntity);

    /**
     * 新增标签
     */
    void insertTag(TagEntity tagEntity);

    /**
     * 获取所有的标签类别
     */
    List<TagEntity> getTagCategoryList();

    /**
     * 根据标签ID获取标签信息
     */
    TagEntity selectTagByTagId(@Param("tagId") Long tagId);

    /**
     * 更新标签
     */
    void updateTag(TagEntity tagEntity);

    /**
     * 查看是否拥有子类
     */
    int checkHasChildren(@Param("tagId") Long tagId);

    /**
     * 删除标签
     */
    void deleteByTagId(@Param("tagId") Long tagId);

    /**
     * 改变标签状态
     */
    void changeStatus(@Param("tagId") Long tagId,@Param("status") String status);

    /**
     * 获取名字相关列表
     */
    List<TagVO> getNameList(@Param("name") String name);
}
