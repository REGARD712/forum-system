package com.me.forum.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.forum.model.Message;
import com.me.forum.services.IMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageServiceImplTest {
    @Resource
    private IMessageService messageService;

    @Resource
    private ObjectMapper objectMapper;

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

    @Test
    void selectUnreadCount() {
        Integer count = messageService.selectUnreadCount(1L);
        System.out.println("count: " + count);
        Integer count2 = messageService.selectUnreadCount(2L);
        System.out.println("count2: " + count2);
        Integer count3 = messageService.selectUnreadCount(1000L);
        System.out.println("count3: " + count3);
    }

    @Test
    void selectByReceiveUserId() throws JsonProcessingException {
        List<Message> messages = messageService.selectByReceiveUserId(220L);
        System.out.println(objectMapper.writeValueAsString(messages));
    }
}