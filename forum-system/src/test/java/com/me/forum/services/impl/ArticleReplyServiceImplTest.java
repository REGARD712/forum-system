package com.me.forum.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.forum.model.ArticleReply;
import com.me.forum.services.IArticleReplyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleReplyServiceImplTest {
    @Resource
    private IArticleReplyService articleReplyService;

    @Resource
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void create() {
        ArticleReply articleReply = new ArticleReply();
        articleReply.setArticleId(2L);
        articleReply.setPostUserId(1L);
        articleReply.setContent("单元测试的回复内容");
        articleReplyService.create(articleReply);
    }

    @Test
    void selectByArticleId() throws JsonProcessingException {
        List<ArticleReply> articleReplies = articleReplyService.selectByArticleId(3L);
        System.out.println(objectMapper.writeValueAsString(articleReplies));
    }
}