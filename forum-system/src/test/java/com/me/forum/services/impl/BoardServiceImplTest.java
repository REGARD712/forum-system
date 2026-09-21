package com.me.forum.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.forum.model.Board;
import com.me.forum.services.IBoardService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@SpringBootTest
class BoardServiceImplTest {
    @Resource
    private IBoardService boardService;

    @Resource
    private ObjectMapper objectMapper;

    @Test
    void selectByNum() {
        boardService.selectByNum(5).forEach(System.out::println);
    }

    @Transactional
    @Test
    void addOneArticleCountById() {
        boardService.addOneArticleCountById(1L);
    }

    @Test
    void selectByPrimaryKey() throws JsonProcessingException {
        Board board = boardService.selectByPrimaryKey(1l);
        System.out.println(objectMapper.writeValueAsString(board));
    }

    @Test
    void subOneArticleCountById() {
        boardService.subOneArticleCountById(6L);
        System.out.println("更新版块帖子数成功");
    }
}