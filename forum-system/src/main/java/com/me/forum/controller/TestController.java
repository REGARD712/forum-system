package com.me.forum.controller;

import com.me.forum.common.AppResult;
import com.me.forum.exception.ApplicationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TestController
 * @Author: JacksonLee
 * @Date: 2026/9/3
 * @Description: 测试Spring环境
 */
@RequestMapping("/test")
@RestController
public class TestController {
    @RequestMapping("/hello")
    public String hello () {
        return "Hello Spring Boot!";
    }

    @GetMapping("/exception")
    public AppResult testException () throws Exception {
        throw new Exception("这是一个exception...");
    }

    @GetMapping("/appException")
    public AppResult testApplicationException ()  {
        throw new ApplicationException("这是一个applicationException...");
    }
}
