package com.me.forum.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.forum.model.Article;
import com.me.forum.services.IArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class ArticleServiceImplTest {
    @Resource
    private IArticleService articleService;
    @Resource
    private ObjectMapper objectMapper;

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

    @Test
    void selectAll() throws JsonProcessingException {
        List<Article> articles = articleService.selectAll();
        System.out.println(objectMapper.writeValueAsString(articles));
    }

    @Test
    void selectAllByBoardId() throws JsonProcessingException {
        List<Article> articles = articleService.selectAllByBoardId(3l);
        System.out.println(objectMapper.writeValueAsString(articles));
    }

    @Test
    void selectDetailById() throws JsonProcessingException {
        Article article = articleService.selectDetailById(1L);
        System.out.println(objectMapper.writeValueAsString(article));
    }

    @Test
    @Transactional
    void modify() {
        articleService.modify(3L,  "这是一个标题", "这是一个内容");
        System.out.println("更新成功");
    }

    @Test
    void selectById() throws JsonProcessingException {
        Article article = articleService.selectById(3L);
        System.out.println(objectMapper.writeValueAsString(article));
    }

    @Test
    @Transactional
    void thumbsUpById() {
        articleService.thumbsUpById(1L);
        System.out.println("点赞成功");
    }

    @Test
    @Transactional
    void deleteById() {
        articleService.deleteById(2L);
        System.out.println("删除帖子成功");
    }

    @Test
    @Transactional
    void addOneArticleReplyCountById() {
        articleService.addOneArticleReplyCountById(1L);
        System.out.println("更新帖子回复数成功");
    }
}