package com.me.forum.services.impl;

import com.me.forum.model.ArticleReply;
import com.me.forum.services.IArticleReplyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleReplyServiceImplTest {
    @Resource
    private IArticleReplyService articleReplyService;

    @Test
    void create() {
        ArticleReply articleReply = new ArticleReply();
        articleReply.setArticleId(2L);
        articleReply.setPostUserId(1L);
        articleReply.setContent("单元测试的回复内容");
        articleReplyService.create(articleReply);
    }
}