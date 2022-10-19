package cn.y3lq.blog.system.vo;

import cn.y3lq.blog.system.domain.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Y3lq
 * @description:
 */
@Data
public class TagVO {

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 标签名字
     */
    private String name;

    /**
     * 父标签ID
     */
    private Long parentId;

    /**
     * 标签排序
     */
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
     * 创建者用户名
     */
    private String createUsername;

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


    /**
     * 子标签
     */
    private List<TagVO> children = new ArrayList<>();
}
