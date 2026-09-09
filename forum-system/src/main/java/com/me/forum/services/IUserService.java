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
     * @param user
     * @Return: void
     * @Description: 创建一个普通用户
     */
    void createNormalUser (User user);

    /**
     * @param username 用户名
     * @return User 用户信息
     * @Description 根据用户名查询用户信息
     */
    User selectByUserName (String username);

    /**
     * @param username 用户名
     * @param password 密码
     * @return User 用户信息
     * @Description: 处理用户登录
     */
    User login (String username, String password);
}
