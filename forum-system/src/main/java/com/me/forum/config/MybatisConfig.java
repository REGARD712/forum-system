package com.me.forum.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: MybatisConfig
 * @Author: JacksonLee
 * @Date: 2026/9/5
 * @Description: 配置MyBatis的扫描路径
 */
@Configuration
@MapperScan("com.me.forum.dao")
public class MybatisConfig {
}
