package com.me.forum.services.impl;

import com.me.forum.model.Article;
import com.me.forum.services.IArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@SpringBootTest
class ArticleServiceImplTest {
    @Resource
    private IArticleService articleService;

    @Transactional
    @Test
    void create() {
        Article article = new Article();
        article.setUserId(1l);
        article.setBoardId(1l);
        article.setTitle("单元测试标题");
        article.setContent("单元测试内容");
        articleService.create(article);
    }
}