package cn.y3lq.blog.system.vo;

import cn.y3lq.blog.system.entity.DiaryCommentEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 博客展示博客日记数据封装
 */
@Data
public class BlogDiaryVO {

    private Long diaryId;

    private Long diaryUserId;

    private String diaryUserNickname;

    private String avatar;

    private String content;

    /**
     * 1:已点赞  0：未点赞
     */
    private String isLike;

    private List<DiaryLikeVO> likes;

    private List<DiaryCommentVO> comments;

    private Integer orderNum;

    private Date createTime;


}
