package cn.y3lq.blog.system.service;

import cn.y3lq.blog.system.entity.TagEntity;
import cn.y3lq.blog.system.vo.TagVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 标签业务层
 */
public interface TagService {

    /**
     * 条件获取标签列表
     */
    List<TagVO> selectTagList(TagEntity tagEntity);

    /**
     * 新增标签
     */
    void insertTag(TagEntity tagEntity, HttpServletRequest request);

    /**
     * 获取所有标签类别
     */
    List<TagEntity> getTagCategoryList();

    /**
     * 获取tag 信息
     */
    TagEntity info(Long tagId);

    /**
     * 更新标签
     */
    void updateTag(TagEntity tagEntity, HttpServletRequest request);

    /**
     * 删除标签
     */
    void deleteTag(Long tagId);

    /**
     * 改变标签状态
     */
    void changeStatus(TagEntity tagEntity);

    /**
     * 获取标签名字相关参数列表
     */
    List<TagVO> getNameList(String name);

    /**
     * 构建前端需要的tree
     */
    List<TagVO> buildTreeList(List<TagVO> tagVOList);
}
