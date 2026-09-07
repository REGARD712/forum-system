package com.me.forum.services.impl;

import com.me.forum.common.AppResult;
import com.me.forum.common.ResultCode;
import com.me.forum.dao.UserMapper;
import com.me.forum.exception.ApplicationException;
import com.me.forum.model.User;
import com.me.forum.services.IUserService;
import com.me.forum.utils.StringUtil;
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
            log.info(ResultCode.FAILED_USER_EXISTS.toString());
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
            log.info(ResultCode.FAILED_CREATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        // 写入成功
        // 打印日志
        log.info("新增用户成功, username: " + user.getUsername() + ". ");
    }
}
