package com.me.forum.services.impl;

import com.me.forum.common.AppResult;
import com.me.forum.common.ResultCode;
import com.me.forum.dao.MessageMapper;
import com.me.forum.exception.ApplicationException;
import com.me.forum.model.Message;
import com.me.forum.model.User;
import com.me.forum.services.IMessageService;
import com.me.forum.services.IUserService;
import com.me.forum.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * IMessageService的实现类
 *
 * @ClassName: MessageServiceImpl
 * @Author: JacksonLee
 * @Date: 2026/9/24
 */
@Slf4j
@Service
public class MessageServiceImpl implements IMessageService {
    @Resource
    private MessageMapper messageMapper;

    @Resource
    private IUserService userService;

    @Override
    public void create(Message message) {
        // 非空校验
        if (message == null || message.getPostUserId() == null || message.getReceiveUserId() == null) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_PARAMS_VALIDATE);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        if (StringUtil.isEmpty(message.getContent())) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_MESSAGE_CREATE_CONTENT_EMPTY);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_MESSAGE_CREATE_CONTENT_EMPTY));
        }

        // 校验接收者是否存在
        User user = userService.selectById(message.getReceiveUserId());
        if (user == null || user.getId() <= 0 || user.getDeleteState() == 1) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_PARAMS_VALIDATE);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        // 设置默认值
        message.setState((byte) 0);
        message.setDeleteState((byte) 0);
        Date date = new Date();
        message.setCreateTime(date);
        message.setUpdateTime(date);

        // 调用DAO写入数据库
        int row = messageMapper.insertSelective(message);
        if (row != 1) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_MESSAGE_CREATE);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_MESSAGE_CREATE));
        }
    }

    @Override
    public Integer selectUnreadCount(Long receiveUserId) {
        // 非空校验
        if (receiveUserId == null || receiveUserId <= 0) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_PARAMS_VALIDATE);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        // 调用DAO查询用户信息并返回
        return messageMapper.selectUnreadCount(receiveUserId);
    }

    @Override
    public List<Message> selectByReceiveUserId(Long receiveUserId) {
        // 非空校验
        if (receiveUserId == null || receiveUserId <= 0) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_PARAMS_VALIDATE);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        // 调用DAO查询并返回结果
        return messageMapper.selectByReceiveUserId(receiveUserId);
    }

    @Override
    public Message selectById(Long id) {
        // 非空校验
        if (id == null || id <= 0) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_PARAMS_VALIDATE);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        // 调用DAO查询并返回结果
        return messageMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateStateById(Long id, Byte state) {
        // 非空校验
        if (id == null || id <= 0
          || state < 0 || state > 2) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_PARAMS_VALIDATE);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        // 构造更新对象
        Message updateMessage = new Message();
        updateMessage.setId(id);
        updateMessage.setState(state);
        updateMessage.setUpdateTime(new Date());

        // 调用DAO写入数据库
        int row = messageMapper.updateByPrimaryKeySelective(updateMessage);
        if (row != 1) {
            // 打印日志
            log.warn("{}", ResultCode.ERROR_SERVICES);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }
    }
}
