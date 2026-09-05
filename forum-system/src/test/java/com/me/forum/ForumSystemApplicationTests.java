package com.me.forum;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;

@SpringBootTest
class ForumSystemApplicationTests {
    @Resource
    private DataSource dataSource;

    @Test
    void testConnection() throws Exception {
        System.out.println("dataSource: " + dataSource.getClass());
        // 数据库连接
        System.out.println("connection: " + dataSource.getConnection());
    }

    @Test
    void contextLoads() {
        System.out.println("Hello forum");
    }

}
