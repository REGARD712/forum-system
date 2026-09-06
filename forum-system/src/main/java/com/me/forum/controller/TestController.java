package com.me.forum.controller;

import com.me.forum.common.AppResult;
import com.me.forum.exception.ApplicationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: TestController
 * @Author: JacksonLee
 * @Date: 2026/9/3
 * @Description: 测试Spring环境
 */
@Api(tags = "测试类相关接口")
@RequestMapping("/test")
@RestController
public class TestController {
    @ApiOperation("测试接口1：显示“Hello Spring Boot!”")
    @GetMapping("/hello")
    public String hello () {
        return "Hello Spring Boot!";
    }

    @ApiOperation("测试接口4：显示对传入的姓名打招呼的信息")
    @PostMapping("/helloByName")
    public AppResult helloByName (@ApiParam("用户姓名") @RequestParam("name") String name) {
        return AppResult.success("Hello, " + name + "！");
    }

    @ApiOperation("测试接口2：显示抛出的异常信息")
    @GetMapping("/exception")
    public AppResult testException () throws Exception {
        throw new Exception("这是一个exception...");
    }

    @ApiOperation("测试接口3：显示抛出的自定义异常信息")
    @GetMapping("/appException")
    public AppResult testApplicationException ()  {
        throw new ApplicationException("这是一个applicationException...");
    }
}
