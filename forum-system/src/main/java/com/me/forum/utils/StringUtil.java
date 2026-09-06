package com.me.forum.utils;

/**
 * @ClassName: StringUtil
 * @Author: JacksonLee
 * @Date: 2026/9/6
 * @Description: 提供字符串处理的相关工具类
 */
public class StringUtil {
    /**
     * @Title: isEmpty
     * @Author: JacksonLee
     * @Date: 2026-09-06
     * @Params: [value：要被判断的字符串]
     * @Return: boolean：true为空；false不为空
     * @Description: 判断字符串是否为空
     */
    public static boolean isEmpty (String value) {
        return value == null || value.length() == 0;
    }
}
