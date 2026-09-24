package com.me.forum.controller;

import com.me.forum.common.AppResult;
import com.me.forum.common.ResultCode;
import com.me.forum.config.AppConfig;
import com.me.forum.model.Message;
import com.me.forum.model.User;
import com.me.forum.services.IMessageService;
import com.me.forum.services.IUserService;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 站内信接口
 *
 * @ClassName: MessageController
 * @Author: JacksonLee
 * @Date: 2026/9/24
 */
@Slf4j
@Api(tags = "站内信接口")
@RequestMapping("/message")
@RestController
public class MessageController {
    @Resource
    private IMessageService messageService;

    @Resource
    private IUserService userService;

    /**
     * 发送站内信
     * @param request HTTP请求
     * @param receiveUserId 接收者用户Id
     * @param content 站内信内容
     * @return AppResult
     */
    @ApiOperation("发送站内信")
    @PostMapping("/send")
    public AppResult send (HttpServletRequest request,
                           @ApiParam("接收者用户Id") @RequestParam("receiveUserId") @NonNull Long receiveUserId,
                           @ApiParam("站内信内容") @RequestParam("content") @NonNull String content) {
        // 获取发送用户的信息
        HttpSession session = request.getSession(false);
        User postUser = (User) session.getAttribute(AppConfig.USER_SESSION_KEY);
        // 校验发送用户的状态
        if (postUser.getState() == 1) {
            // 禁言状态下不可发送站内信,返回错误信息
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        // 校验当前接收者用户是否自己,不能给自己发送站内信
        if (postUser.getId().equals(receiveUserId)) {
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_MESSAGE_CREATE);
        }

        // 查询接收者用户信息
        User receiveUser = userService.selectById(receiveUserId);
        if (receiveUser == null || receiveUser.getDeleteState() == 1) {
            // 接收者用户不存在,返回错误信息
            return AppResult.failed(ResultCode.FAILED_MESSAGE_CREATE_RECEIVE_USER_BANNED);
        }

        // 构造对象
        Message message = new Message();
        message.setPostUserId(postUser.getId());
        message.setReceiveUserId(receiveUserId);
        message.setContent(content);

        // 调用Service
        messageService.create(message);

        // 返回成功信息
        return AppResult.success(ResultCode.SUCCESS_MESSAGE_CREATE);
    }
}
