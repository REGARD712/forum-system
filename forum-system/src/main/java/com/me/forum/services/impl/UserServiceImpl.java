package com.me.forum.services.impl;

import com.me.forum.common.AppResult;
import com.me.forum.common.ResultCode;
import com.me.forum.dao.UserMapper;
import com.me.forum.exception.ApplicationException;
import com.me.forum.model.User;
import com.me.forum.services.IUserService;
import com.me.forum.utils.MD5Util;
import com.me.forum.utils.StringUtil;
import com.me.forum.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName: UserServiceImpl
 * @Author: JacksonLee
 * @Date: 2026/9/7
 * @Description: 用户相关业务处理的Service接口实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;
    
    @Override
    public void createNormalUser(User user) {
        // 1.非空校验
        if (user == null
        || StringUtil.isEmpty(user.getUsername())
        || StringUtil.isEmpty(user.getNickname())
        || StringUtil.isEmpty(user.getPassword())
        || StringUtil.isEmpty(user.getSalt())) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 抛出异常,统一抛出ApplicationException
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        // 2.按用户名查询用户信息
        User existUser = userMapper.selectByUserName(user.getUsername());
        // 判断是否已存在
        if (existUser != null) {
            // 打印日志
            log.warn(ResultCode.FAILED_USER_EXISTS.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_EXISTS));
        }

        // 3.新增用户
        // 设置非空属性的默认值
        user.setGender((byte) 2);
        user.setArticleCount(0);
        user.setIsAdmin((byte) 0);
        user.setState((byte) 0);
        user.setDeleteState((byte) 0);
        Date date = new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        // 写入数据库
        int row = userMapper.insertSelective(user);
        // 如果影响行数不为1说明写入失败
        if (row != 1) {
            // 打印日志
            log.warn(ResultCode.FAILED_CREATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        // 写入成功
        // 打印日志
        log.info("{} username: {}. ",ResultCode.SUCCESS_NEW_USER_INSERT, user.getUsername());
    }

    @Override
    public User selectByUserName(String username) {
        // 非空校验
        if (StringUtil.isEmpty(username)) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 返回查询的用户信息
        return userMapper.selectByUserName(username);
    }

    @Override
    public User login(String username, String password) {
        // 1.非空校验
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }
        // 2.根据用户名查询用户信息
        User selectedUser = selectByUserName(username);
        // 3.对查询结果进行非空校验
        if (selectedUser == null) {
            // 打印日志
            log.warn("{} username: {}. ", ResultCode.FAILED_USER_NOT_EXISTS, username);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }
        // 4.校验密码是否正确
        String encryptedPassword = MD5Util.md5Salt(password, selectedUser.getSalt());
        if (!encryptedPassword.equalsIgnoreCase(selectedUser.getPassword())) {
            // 打印日志
            log.warn("{} username: {}, password: {}", ResultCode.FAILED_LOGIN, username, password);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }
        // 5.登陆成功
        log.info("{} username: {}, password: {}", ResultCode.SUCCESS_LOGIN, username, password);
        // 返回用户信息
        return selectedUser;
    }

    @Override
    public User selectById(Long id) {
        // 非空校验
        if (id == null) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 返回查询的用户信息
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOneArticleCountById(Long id) {
        // 非空校验
        if (id == null || id <= 0) {
            // 打印日志
            log.warn("{} id: {}", ResultCode.FAILED_USER_ARTICLE_COUNT_UPDATE, id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_ARTICLE_COUNT_UPDATE));
        }

        // 查询对应用户信息
        User user = userMapper.selectByPrimaryKey(id);
        // 非空校验
        if (user == null) {
            // 打印日志
            log.warn("{} userId: {}", ResultCode.FAILED_USER_NOT_EXISTS, id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }

        // 更新帖子数量
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setArticleCount(user.getArticleCount() + 1);
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if (row != 1) {
            // 打印日志
            log.warn("{} row: {}", ResultCode.FAILED_USER_ARTICLE_COUNT_UPDATE, row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_ARTICLE_COUNT_UPDATE));
        }
    }

    @Override
    public void subOneArticleCountById(Long id) {
        // 非空校验
        if (id == null || id <= 0) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_USER_ARTICLE_COUNT_UPDATE);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_ARTICLE_COUNT_UPDATE));
        }

        // 查询对应用户信息
        User user = userMapper.selectByPrimaryKey(id);
        // 非空校验
        if (user == null) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_USER_NOT_EXISTS);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }

        // 更新帖子数量
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setArticleCount(Math.max(user.getArticleCount() - 1, 0));
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if (row != 1) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_USER_ARTICLE_COUNT_UPDATE);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_ARTICLE_COUNT_UPDATE));
        }
    }

    @Override
    public void modifyInfo(User user) {
        // 非空校验
        if (user == null || user.getId() == null || user.getId() <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        // 获取用户信息
        User existsUser = userMapper.selectByPrimaryKey(user.getId());
        // 非空校验
        if (existsUser == null || existsUser.getId() == null || existsUser.getId() <= 0) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_USER_NOT_EXISTS);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }

        // 构造更新对象
        User updateUser = new User();
        // 标志位检查属性是否都为空,false表示都为空,true则表示有不为空的
        boolean check = false;
        // 设置用户Id
        updateUser.setId(user.getId());
        // 对每一个属性进行校验并赋值
        // 校验用户名
        if (!StringUtil.isEmpty(user.getUsername())
        && !user.getUsername().equals(existsUser.getUsername())) {
            // 需要更新用户名,同时需要进行唯一性校验
            User checkUser = userMapper.selectByUserName(updateUser.getUsername());
            // 校验
            if (checkUser != null) {
                // 该用户名已存在,不可改为该用户名
                throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NAME_EXISTS));
            }
            // 可以改为该用户名
            updateUser.setUsername(user.getUsername());
            // 更新标志位
            check = true;
        }
        // 校验昵称
        if (!StringUtil.isEmpty(user.getNickname())
        && !user.getNickname().equals(existsUser.getNickname())) {
            // 需要更新昵称
            updateUser.setNickname(user.getNickname());
            // 更新标志位
            check = true;
        }
        // 校验性别
        if (user.getGender() != null
        && !user.getGender().equals(existsUser.getGender())) {
            // 需要更新性别
            updateUser.setGender(user.getGender());
            // 合法性校验
            if (updateUser.getGender() > 2 || updateUser.getGender() < 0) {
                updateUser.setGender((byte) 2);
            }
            // 更新标志位
            check = true;
        }
        // 校验电子邮箱
        if (!StringUtil.isEmpty(user.getEmail())
        && !user.getEmail().equals(existsUser.getEmail())) {
            // 需要更新昵称
            updateUser.setEmail(user.getEmail());
            // 更新标志位
            check = true;
        }
        // 校验电话号码
        if (!StringUtil.isEmpty(user.getPhoneNum())
        && !user.getPhoneNum().equals(existsUser.getPhoneNum())) {
            // 需要更新昵称
            updateUser.setPhoneNum(user.getPhoneNum());
            // 更新标志位
            check = true;
        }
        // 校验个人简介
        if (!StringUtil.isEmpty(user.getRemark())
        && !user.getRemark().equals(existsUser.getRemark())) {
            // 需要更新昵称
            updateUser.setRemark(user.getRemark());
            // 更新标志位
            check = true;
        }
        // 判断标志位:若为false就抛异常,否则写入数据库
        if (!check) {
            // 打印日志
            log.warn(ResultCode.FAILED_USER_INFO_EMPTY.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_INFO_EMPTY));
        }

        // 调用DAO写入数据库
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if (row != 1) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_USER_INFO_UPDATE);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_INFO_UPDATE));
        }
    }

    @Override
    public void modifyPassword(Long id, String oldPassword, String newPassword) {
        // 非空校验
        if (id == null || id <= 0) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_PARAMS_VALIDATE);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        if (StringUtil.isEmpty(newPassword) || StringUtil.isEmpty(oldPassword)) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_USER_INFO_EMPTY);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_INFO_EMPTY));
        }

        // 获取用户信息
        User existsUser = userMapper.selectByPrimaryKey(id);
        // 非空校验
        if (existsUser == null || existsUser.getId() <= 0 || existsUser.getDeleteState() == 1) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_USER_NOT_EXISTS);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }

        // 校验原密码是否正确
        if (!MD5Util.md5Salt(oldPassword, existsUser.getSalt()).equalsIgnoreCase(existsUser.getPassword())) {
            // 打印日志
            log.warn("{}", ResultCode.FAILED_USER_OLD_PASSWORD_WRONG);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_OLD_PASSWORD_WRONG));
        }

        // 生成新盐
        String newSalt = UUIDUtil.UUID_32();
        // 加密生成新密文
        String encryptedPassword = MD5Util.md5Salt(newPassword, newSalt);

        // 构造更新对象
        User updateUser = new User();
        updateUser.setId(existsUser.getId());
        updateUser.setSalt(newSalt);
        updateUser.setPassword(encryptedPassword);
        updateUser.setUpdateTime(new Date());

        // 调用DAO写入数据库
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if (row != 1) {
            // 打印日志
            log.warn("{}", ResultCode.ERROR_SERVICES);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }
    }
}
