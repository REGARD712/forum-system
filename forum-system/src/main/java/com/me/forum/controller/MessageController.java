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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

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

    /**
     * 获取用户未读消息数
     * @param request HTTP请求
     * @return AppResult<Integer>
     */
    @ApiOperation("获取用户未读消息数")
    @GetMapping("/getUnreadCount")
    public AppResult<Integer> getUnreadCount (HttpServletRequest request) {
        // 获取用户信息
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION_KEY);

        // 调用Service查询
        Integer count = messageService.selectUnreadCount(user.getId());

        // 返回结果
        return AppResult.success(count);
    }

    /**
     * 获取用户消息列表
     * @param request HTTP请求
     * @return AppResult<List<Message>>
     */
    @ApiOperation("获取用户消息列表")
    @GetMapping("/getAll")
    public AppResult<List<Message>> getAll (HttpServletRequest request) {
        // 获取用户信息
        HttpSession session = request.getSession(false);
        User receiveUser = (User) session.getAttribute(AppConfig.USER_SESSION_KEY);

        // 调用Service查询
        List<Message> messages = messageService.selectByReceiveUserId(receiveUser.getId());

        // 返回结果
        return AppResult.success(messages);
    }

    /**
     * 更新站内信状态为已读
     * @param request HTTP请求
     * @param id 站内信Id
     * @return AppResult
     */
    @ApiOperation("更新站内信状态为已读")
    @PostMapping("/markRead")
    public AppResult markRead (HttpServletRequest request,
                               @ApiParam("站内信Id") @RequestParam("id") @NonNull Long id) {
        // 根据Id查询站内信信息
        Message message = messageService.selectById(id);

        // 非空校验
        if (message == null || message.getDeleteState() == 1) {
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_MESSAGE_NOT_EXISTS);
        }

        // 获取用户信息
        HttpSession session = request.getSession(false);
        User receiveUser = (User) session.getAttribute(AppConfig.USER_SESSION_KEY);

        // 校验站内信的接收者是否是当前登录用户
        if (!receiveUser.getId().equals(message.getReceiveUserId())) {
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_FORBIDDEN);
        }

        // 调用Service将站内信更新为已读(1)
        messageService.updateStateById(id, (byte) 1);

        // 返回成功信息
        return AppResult.success();
    }

    /**
     * 回复站内信
     * @param request HTTP请求
     * @param repliedId 站内信Id
     * @param content 回复的内容
     * @return AppResult
     */
    @ApiOperation("回复站内信")
    @PostMapping("/reply")
    public AppResult reply (HttpServletRequest request,
                            @ApiParam("站内信Id") @RequestParam("repliedId") @NonNull Long repliedId,
                            @ApiParam("回复的内容") @RequestParam("content") @NonNull String content) {
        // 校验用户状态
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION_KEY);
        if (user.getState() == 1) {
            // 用户处于禁言状态,返回错误信息
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }

        // 校验要回复的站内信的状态
        Message existsMessage = messageService.selectById(repliedId);
        // 非空校验
        if (existsMessage == null || existsMessage.getDeleteState() == 1) {
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_MESSAGE_NOT_EXISTS);
        }

        // 校验接收者用户是否为当前登录用户
        if (user.getId().equals(existsMessage.getPostUserId())) {
            // 不能给自己发送回复信息,返回错误信息
            return AppResult.failed(ResultCode.FAILED_MESSAGE_CREATE_RECEIVE_USER_BANNED);
        }

        // 构造对象
        Message message = new Message();
        message.setPostUserId(user.getId());
        message.setReceiveUserId(existsMessage.getPostUserId());
        message.setContent(content);

        // 调用Service
        messageService.reply(repliedId, message);

        // 返回成功信息
        return AppResult.success();
    }
}
