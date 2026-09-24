package com.me.forum.services;

import com.me.forum.model.Message;

import java.util.List;

/**
 * 处理站内信相关业务逻辑的Service接口
 *
 * @InterfaceName：IMessageService
 * @Author: JacksonLee
 * @Date: 2026/9/24
 */
public interface IMessageService {
    /**
     * 新增一条站内信
     * @param message 站内信对象
     * @return void
     */
    void create (Message message);

    /**
     * 获取指定用户的未读站内信数量
     * @param receiveUserId 用户Id
     * @return Integer
     */
    Integer selectUnreadCount (Long receiveUserId);

    /**
     * 获取指定用户的消息列表
     * @param receiveUserId 用户Id
     * @return List<Message>
     */
    List<Message> selectByReceiveUserId (Long receiveUserId);
}
