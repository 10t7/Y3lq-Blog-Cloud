package cn.y3lq.blog.system.vo;

import cn.y3lq.blog.common.core.valid.AddGroup;
import cn.y3lq.blog.common.core.valid.UpdateGroup;
import cn.y3lq.blog.system.entity.TagEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: Y3lq
 * @description:
 */
@Data
public class ArticleVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 文章ID
     */
    @Null(message = "新增文章不能指定ID", groups = {AddGroup.class})
    @NotNull(message = "修改文章ID不能为空", groups = {UpdateGroup.class})
    @Range(min = 1L, max = 9999999L, message = "文章ID不合理", groups = {UpdateGroup.class})
    private Long articleId;

    /**
     * 文章标题
     */
    @NotBlank(message = "文章标题不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(min = 1, max = 40, message = "文章标题长度应在[1, 40]", groups = {AddGroup.class, UpdateGroup.class})
    private String title;

    /**
     * 文章简介
     */
    @NotBlank(message = "简介不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(min = 1, max = 200, message = "文章简介长度应在[1, 200]", groups = {AddGroup.class, UpdateGroup.class})
    private String summary;

    /**
     * 文章缩略图地址
     */
    @URL(message = "文章缩略图封面格式不正确", groups = {AddGroup.class, UpdateGroup.class})
    private String thumbnail;

    /**
     * 文章内容
     */
    @NotBlank(message = "文章内容不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String content;

    /**
     * 文章评论数
     */
    private Integer commentCount;

    /**
     * 文章点赞数
     */
    private Integer likeCount;

    /**
     * 文章顺序（有排序则为首页推荐文章）
     */
    private Integer orderNum;

    /**
     * 文章状态
     */
    private String status;

    /**
     * 文章创建者用户ID
     */
    private String createBy;

    /**
     * 文章创建时间
     */
    private Date createTime;

    /**
     * 文章更新者用户ID
     */
    private String updateBy;

    /**
     * 文章更新时间
     */
    private Date updateTime;

    /**
     * 文章标签id数组
     */
    @Size(min = 1, max = 5, message = "文章标签个数应在[1, 5]", groups = {AddGroup.class, UpdateGroup.class})
    @NotNull(message = "文章标签不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Long[] tagIds;
    private Long tagId;

    private List<TagEntity> tagEntity;

    private String authorNickname;

    private String authorUsername;

    private Long authorUserId;




}
