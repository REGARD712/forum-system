package com.me.forum;

import com.me.forum.dao.UserMapper;
import com.me.forum.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;

@SpringBootTest
class ForumSystemApplicationTests {
    // 注入数据源
    @Resource
    private DataSource dataSource;

    // 注入UserMapper
    @Resource
    private UserMapper userMapper;

    @Test
    void testConnection() throws Exception {
        System.out.println("dataSource: " + dataSource.getClass());
        // 数据库连接
        System.out.println("connection: " + dataSource.getConnection());
    }

    @Test
    public void testMybatis () {
        User user = userMapper.selectByPrimaryKey(1l);
        System.out.println(user);
        System.out.println(user.getUsername());
    }

    @Test
    void contextLoads() {
        System.out.println("Hello forum");
    }

}
