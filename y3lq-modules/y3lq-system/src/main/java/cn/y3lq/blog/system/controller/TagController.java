package cn.y3lq.blog.system.controller;

import cn.hutool.core.collection.ListUtil;
import cn.y3lq.blog.common.core.domain.AjaxResult;
import cn.y3lq.blog.common.core.valid.AddGroup;
import cn.y3lq.blog.common.core.valid.UpdateGroup;
import cn.y3lq.blog.common.security.annotation.HasPermissions;
import cn.y3lq.blog.common.security.annotation.ProhibitGuestAccess;
import cn.y3lq.blog.system.entity.TagEntity;
import cn.y3lq.blog.system.service.TagService;
import cn.y3lq.blog.system.vo.TagVO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 标签控制器
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 前端获取所有标签树列表
     */
    @GetMapping("/tag-tree-list")
    public AjaxResult getTagTreeList() {
        // 获取所有标签
        List<TagVO> tagVOList = tagService.selectTagList(new TagEntity());
        // 构建前端需要的tree
        List<TagVO> tagTree = tagService.buildTreeList(tagVOList);
        return AjaxResult.success(tagTree);
    }

    /**
     * 获取标签名字列表
     */
    @HasPermissions("content:tag:list")
    @GetMapping("/tag-name-list")
    public AjaxResult tagNameList(String name) {
        List<TagVO> tagVOList = tagService.getNameList(name);
        return AjaxResult.success(tagVOList);

    }

    /**
     * 改变标签状态
     */
    @HasPermissions("content:tag:edit")
    @PutMapping("/chenge-tag-status")
    @ProhibitGuestAccess
    public AjaxResult changeStatus(TagEntity tagEntity) {
        tagService.changeStatus(tagEntity);
        return AjaxResult.success();
    }

    /**
     * 删除标签
     */
    @HasPermissions("content:tag:delete")
    @DeleteMapping("/{tagId}")
    public AjaxResult delete(@PathVariable("tagId") Long tagId) {
        tagService.deleteTag(tagId);
        return AjaxResult.success();
    }

    /**
     * 更新标签
     */
    @HasPermissions("content:tag:edit")
    @PutMapping
    public AjaxResult edit(@Validated(UpdateGroup.class) @RequestBody TagEntity tagEntity, HttpServletRequest request) {
        tagService.updateTag(tagEntity, request);
        return AjaxResult.success();
    }

    /**
     * 根据标签ID获取信息
     */
    @HasPermissions("content:tag:query")
    @GetMapping("/info/{tagId}")
    public AjaxResult info(@PathVariable("tagId") Long tagId) {
        TagEntity tagEntity = tagService.info(tagId);

        return AjaxResult.success(tagEntity);
    }

    /**
     * 获取所有标签类别
     */
    @HasPermissions("content:tag:list")
    @GetMapping("/tag-category-list")
    public AjaxResult getTagCategoryList() {
        List<TagEntity> tagEntityList = tagService.getTagCategoryList();
        return AjaxResult.success(tagEntityList);
    }

    /**
     * 新增标签
     */
    @HasPermissions("content:tag:add")
    @PostMapping
    public AjaxResult add(@Validated(AddGroup.class) @RequestBody TagEntity tagEntity, HttpServletRequest request) {
        tagService.insertTag(tagEntity, request);
        return AjaxResult.success();
    }

    /**
     * 查询 tag 列表
     */
    @HasPermissions("content:tag:list")
    @GetMapping("/list")
    public AjaxResult list(TagEntity tagEntity) {
        List<TagVO> tagVOS = tagService.selectTagList(tagEntity);
        return AjaxResult.success(tagVOS);
    }
}
