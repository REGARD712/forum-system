package com.me.forum.services;

import com.me.forum.model.User;

/**
 * @InterfaceName：IUserService
 * @Author: JacksonLee
 * @Date: 2026/9/7
 * @Description: 用户相关接口
 */
public interface IUserService {
    /**
     * @Title: createNormalUser
     * @Author: JacksonLee
     * @Date: 2026-09-07
     * @Params: [user]
     * @Return: void
     * @Description: 创建一个普通用户
     */
    void createNormalUser (User user);
}
