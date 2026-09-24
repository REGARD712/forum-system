package com.me.forum.services;

import com.me.forum.model.User;

/**
 * @InterfaceName：IUserService
 * @Author: JacksonLee
 * @Date: 2026/9/7
 * @Description: 用户相关业务逻辑的接口
 */
public interface IUserService {
    /**
     * @param user 用户信息
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

    /**
     * @param id 用户id
     * @return User 用户信息
     * @description: 根据用户id查询用户信息
     */
    User selectById (Long id);

    /**
     * @param id 用户id
     * @return void
     * @description: 更新当前用户的发帖数
     */
    void addOneArticleCountById (Long id);

    /**
     * 更显当前用户的发帖数(减少)
     * @param id 用户Id
     * @return void
     */
    void subOneArticleCountById (Long id);

    /**
     * 修改用户个人信息
     * @param user 用户对象
     * @return void
     */
    void modifyInfo (User user);

    /**
     * 修改用户密码
     * @param id 用户Id
     * @param newPassword 新密码
     * @param oldPassword 原密码
     * @return void
     */
    void modifyPassword (Long id, String newPassword, String oldPassword);
}
