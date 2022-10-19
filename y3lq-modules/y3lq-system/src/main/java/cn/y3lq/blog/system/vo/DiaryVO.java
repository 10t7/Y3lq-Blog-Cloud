package cn.y3lq.blog.system.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author: Y3lq
 * @description:
 */
@Data
public class DiaryVO {
    private Long diaryId;

    private Long diaryUserId;

    private String diaryUsername;

    private String diaryUserNickname;

    private Integer orderNum;

    private String content;

    private String status;

    private String delFlag;

    private Date createTime;

    private Date updateTime;
}
