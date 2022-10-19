package cn.y3lq.blog.system.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author: Y3lq
 * @description: 日记实体类
 */
@Data
public class DiaryEntity {

    private Long diaryId;

    private Long diaryUserId;

    private String content;

    private Integer orderNum;

    private String status;

    private String delFlag;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
}
