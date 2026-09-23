package com.me.forum.model;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleReply {
    private Long id;

    private Long articleId;

    private Long postUserId;

    private Long replyId;

    private Long replyUserId;

    private String content;

    private Integer likeCount;

    private Byte state;

    private Byte deleteState;

    private Date createTime;

    private Date updateTime;

    // 关联User对象(回复发送者)
    private User user;
}