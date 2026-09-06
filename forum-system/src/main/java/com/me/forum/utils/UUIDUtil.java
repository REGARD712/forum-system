package com.me.forum.utils;

import java.util.UUID;

/**
 * @ClassName: UUIDUtil
 * @Author: JacksonLee
 * @Date: 2026/9/6
 * @Description: 提供生成UUID（随机字符串）的工具类
 */
public class UUIDUtil {
    /**
     * @Title: UUID_36
     * @Author: JacksonLee
     * @Date: 2026-09-06
     * @Params: []
     * @Return: String：生成的36位UUID（随机字符串）
     * @Description: 生成一个标准的36位的UUID
     */
    public static String UUID_36 () {
        return UUID.randomUUID().toString();
    }


    /**
     * @Title: UUID_32
     * @Author: JacksonLee
     * @Date: 2026-09-06
     * @Params: []
     * @Return: String：生成的32位UUID（随机字符串）
     * @Description: 生成一个32位的UUID
     */
    public static String UUID_32 () {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
