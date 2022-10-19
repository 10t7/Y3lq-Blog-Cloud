package cn.y3lq.blog.system.entity;

import cn.y3lq.blog.common.core.valid.AddGroup;
import cn.y3lq.blog.common.core.valid.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author: Y3lq
 * @description: 标签实体类
 */
@Data
public class TagEntity {
    /**
     * 标签ID
     */
    @NotNull(message = "标签ID不能为空", groups = {UpdateGroup.class})
    private Long tagId;

    /**
     * 标签名字
     */
    @NotEmpty(message = "标签名字不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String name;

    /**
     * 父标签ID
     */
    private Long parentId;

    /**
     * 标签排序
     */
    @NotEmpty(message = "标签排序不能为空", groups = {AddGroup.class, UpdateGroup.class})

    private String orderNum;

    /**
     * 标签状态（0：正常  1停用）
     */
    private String status;

    /**
     * 标签删除标识（0：正常  1删除）
     */
    private String delFlag;


    /**
     * 标签创建者ID
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 标签更新者ID
     */
    private String updateBy;

    /**
     * 标签更新时间
     */
    private Date updateTime;
}
