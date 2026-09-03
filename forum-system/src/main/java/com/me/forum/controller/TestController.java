package com.me.forum.controller;

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
}
