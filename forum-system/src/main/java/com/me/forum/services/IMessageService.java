package com.me.forum.services;

import com.me.forum.model.Message;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 根据Id查询站内信
     * @param id 站内信Id
     * @return Message
     */
    Message selectById (Long id);

    /**
     * 更新站内信的状态(未读0/已读1/已回复2)
     * @param id 站内信Id
     * @param state 目标状态
     * @return void
     */
    void updateStateById (Long id, Byte state);

    /**
     * 回复站内信
     * @param repliedId 要被回复的站内信
     * @param message 回复的内容
     * @return void
     */
    @Transactional // 用事务管理(更新站内信的状态+将回复站内信写入表中)
    void reply (Long repliedId, Message message);
}
