package com.me.forum.controller;

import com.me.forum.common.AppResult;
import com.me.forum.common.ResultCode;
import com.me.forum.model.User;
import com.me.forum.services.IUserService;
import com.me.forum.utils.MD5Util;
import com.me.forum.utils.UUIDUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName: UserController
 * @Author: JacksonLee
 * @Date: 2026/9/7
 * @Description: 用户相关接口
 */

@Api(tags = "用户接口")
@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {
    @Resource
    private IUserService userService;

    /**
     * @Title register
     * @param username 用户名
     * @param nickname 昵称
     * @param password 密码
     * @param passwordRepeat 确认密码
     * @Return AppResult
     * @Description 注册新用户
     */
    @ApiOperation("注册新用户")
    @PostMapping("/register")
    public AppResult register (@ApiParam("用户名") @RequestParam("username") @NonNull String username,
                               @ApiParam("昵称") @RequestParam("nickname") @NonNull String nickname,
                               @ApiParam("密码") @RequestParam("password") @NonNull String password,
                               @ApiParam("确认密码") @RequestParam("passwordRepeat") @NonNull String passwordRepeat) {
        // 校验两次输入的密码是否一致
        if (!passwordRepeat.equals(password)) {
            // 打印日志
            log.warn(ResultCode.FAILED_TWO_PWD_NOT_SAME.toString());
            // 返回响应
            return AppResult.failed(ResultCode.FAILED_TWO_PWD_NOT_SAME);
        }

        // 准备数据
        User user = new User();
        user.setUsername(username);
        user.setNickname(nickname);
        // 处理密码
        String salt = UUIDUtil.UUID_32();
        // 生成加密后的密文
        String encryptedPassword = MD5Util.md5Salt(password, salt);
        // 设置密文和盐
        user.setPassword(encryptedPassword);
        user.setSalt(salt);

        // 调用service
        userService.createNormalUser(user);
        // 返回成功响应
        return AppResult.success();
    }
}
