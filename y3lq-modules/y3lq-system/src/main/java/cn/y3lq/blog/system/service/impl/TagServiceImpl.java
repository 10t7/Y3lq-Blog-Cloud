package cn.y3lq.blog.system.service.impl;

import cn.y3lq.blog.common.core.constant.Constants;
import cn.y3lq.blog.common.core.domain.AjaxResult;
import cn.y3lq.blog.common.core.exception.ServiceException;
import cn.y3lq.blog.common.core.util.SecurityUtils;
import cn.y3lq.blog.system.entity.TagEntity;
import cn.y3lq.blog.system.mapper.TagMapper;
import cn.y3lq.blog.system.service.TagService;
import cn.y3lq.blog.system.vo.TagVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Y3lq
 * @description: 标签业务层
 */
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;


    /**
     * 条件获取标签列表
     */
    @Override
    public List<TagVO> selectTagList(TagEntity tagEntity) {
        return tagMapper.selectTagList(tagEntity);
    }

    /**
     * 新增标签
     */
    @Override
    public void insertTag(TagEntity tagEntity, HttpServletRequest request) {
        initTagEntity(tagEntity, request);
        if (tagEntity.getParentId() == null) {
            // 为新增标签类别
            tagEntity.setParentId(0L);
        }
        // 查看它所属类别是否是标签
        TagEntity tag = tagMapper.selectTagByTagId(tagEntity.getParentId());
        if (ObjectUtils.isNotEmpty(tag)) {
            if (tag.getParentId() != 0) {
                throw new ServiceException("父级必须是标签类别");
            }
        }
        tagMapper.insertTag(tagEntity);
    }

    /**
     * 获取所有标签类别
     */
    @Override
    public List<TagEntity> getTagCategoryList() {
        return tagMapper.getTagCategoryList();
    }

    /**
     * 获取 标签 信息
     */
    @Override
    public TagEntity info(Long tagId) {
        return tagMapper.selectTagByTagId(tagId);
    }

    /**
     * 更新标签
     */
    @Override
    public void updateTag(TagEntity tagEntity, HttpServletRequest request) {
        tagEntity.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId(request)));
        tagMapper.updateTag(tagEntity);
    }

    /**
     * 删除标签
     */
    @Override
    public void deleteTag(Long tagId) {
        int count = tagMapper.checkHasChildren(tagId);
//        if (Constants.USERTAGID.equals(tagId)) {
//            throw new ServiceException("用户标签不能删除");
//        }
        if (count > 0) {
            throw new ServiceException("该类别拥有子类，请先删除所有子类");
        }
        tagMapper.deleteByTagId(tagId);
    }

    /**
     * 改变标签状态
     */
    @Override
    public void changeStatus(TagEntity tagEntity) {
        Long tagId = tagEntity.getTagId();
        String status = tagEntity.getStatus();
        if (tagId == null || tagId == 0 || StringUtils.isEmpty(status)) {
            throw new ServiceException("缺乏相关参数(标签ID，标签需要修改的状态)");
        }
        tagMapper.changeStatus(tagId, status);
    }

    /**
     * 获取标签名字相关参数列表
     */
    @Override
    public List<TagVO> getNameList(String name) {
        return tagMapper.getNameList(name);
    }

    /**
     * 构建前端需要的tree
     */
    @Override
    public List<TagVO> buildTreeList(List<TagVO> tagVOList) {
        List<TagVO> returnList = tagVOList.stream().filter(tag -> {
            return tag.getParentId().equals(0L);
        }).collect(Collectors.toList());
        returnList.forEach(tag -> {
            Long tagId = tag.getTagId();
            ArrayList<TagVO> tagList = new ArrayList<>();
            tagVOList.stream().forEach(tagVO->{
                if(tagVO.getParentId().equals(tagId)){
                    tagList.add(tagVO);
                }
            });
            tag.setChildren(tagList);
        });
        return returnList;
    }

    /**
     * 初始化tagEntity
     */
    private void initTagEntity(TagEntity tagEntity, HttpServletRequest request) {
        // 设置创建者的ID
        tagEntity.setCreateBy(String.valueOf(SecurityUtils.getCurrentUserId(request)));
        tagEntity.setDelFlag("0");
        tagEntity.setStatus("0");
    }
}
