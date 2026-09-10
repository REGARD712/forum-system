package com.me.forum.services.impl;

import com.me.forum.model.User;
import com.me.forum.services.IUserService;
import com.me.forum.utils.MD5Util;
import com.me.forum.utils.UUIDUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@SpringBootTest()
class UserServiceImplTest {
    @Resource
    private IUserService userService;

    @Transactional // 测试完成后自动回滚, 不会污染数据库的数据
    @Test
    void createNormalUser() {
        // 构造User对象
        User user = new User();
        user.setUsername("wangwu");
        user.setNickname("wu_user");
        // 设置密码
        String password = "123456";
        String salt = UUIDUtil.UUID_32();
        String ciphertext = MD5Util.md5Salt(password, salt);
        user.setPassword(ciphertext);
        user.setSalt(salt);

        // 调用service
        userService.createNormalUser(user);
        System.out.println(user);
    }

    @Test
    void selectByUserName() {
        User user = userService.selectByUserName("zhangsan");
        System.out.println(user);
    }

    @Test
    void login() {
        User user = userService.login("zhangsan", "123456");
        System.out.println(user);
    }

    @Test
    void selectById() {
        User user = userService.selectById(2l);
        System.out.println(user);
    }
}