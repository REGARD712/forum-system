package com.me.forum.services.impl;

import com.me.forum.model.Message;
import com.me.forum.services.IMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageServiceImplTest {
    @Resource
    private IMessageService messageService;

    @Test
    @Transactional
    void create() {
        Message message = new Message();
        message.setPostUserId(2L);
        message.setReceiveUserId(3L);
        message.setContent("你好！");
        messageService.create(message);
        System.out.println("发送站内信成功");
    }
}