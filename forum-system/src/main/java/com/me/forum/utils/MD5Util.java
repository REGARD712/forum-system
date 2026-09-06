package com.me.forum.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @ClassName: MD5Util
 * @Author: JacksonLee
 * @Date: 2026/9/6
 * @Description: 提供MD5加密的工具类
 */
public class MD5Util {
    /**
     * @Title: md5
     * @Author: JacksonLee
     * @Date: 2026-09-06
     * @Params: [str：明文]
     * @Return: String：加密后的密文
     * @Description: 对字符串进行MD5加密
     */
    public static String md5 (String str) {
        return DigestUtils.md5Hex(str);
    }

    /**
     * @Title: md5Salt
     * @Author: JacksonLee
     * @Date: 2026-09-06
     * @Params: [str：用户的密码原文, </br>salt：随机生成的扰动字符串（盐值）]
     * @Return: String：加密后的密文
     * @Description: 对用户的密码加密
     */
    public static String md5Salt (String str, String salt) {
        return md5(md5(str) + salt);
    }
}
