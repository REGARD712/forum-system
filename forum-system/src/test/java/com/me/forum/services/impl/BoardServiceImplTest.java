package com.me.forum.services.impl;

import com.me.forum.services.IBoardService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceImplTest {
    @Resource
    private IBoardService boardService;

    @Test
    void selectByNum() {
        boardService.selectByNum(5).forEach(System.out::println);
    }

    @Transactional
    @Test
    void addOneArticleCountById() {
        boardService.addOneArticleCountById(1L);
    }
}