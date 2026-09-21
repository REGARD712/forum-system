package com.me.forum.common;

/**
 * @EnumName：ResultCode
 * @Author: JacksonLee
 * @Date: 2026/9/5
 * @Description: 系统状态码
 */
public enum ResultCode {
    // 定义状态码

    // 基本操作
    SUCCESS                                       (0, "操作成功"),
    FAILED                                        (1000, "操作失败"),
    FAILED_UNAUTHORIZED                           (1001, "未授权"),
    FAILED_PARAMS_VALIDATE                        (1002, "参数校验失败"),
    FAILED_FORBIDDEN                              (1003, "禁止访问"),
    FAILED_CREATE                                 (1004, "新增失败"),
    FAILED_NOT_EXISTS                             (1005, "资源不存在"),

    // 用户相关操作
    SUCCESS_LOGIN                                 (1100, "登录成功"),
    SUCCESS_NEW_USER_INSERT                       (1101, "注册成功"),
    SUCCESS_LOG_OUT                               (1102, "退出登录成功"),
    FAILED_USER_EXISTS                            (1103, "用户已存在"),
    FAILED_USER_NOT_EXISTS                        (1104, "用户不存在"),
    FAILED_LOGIN                                  (1105, "用户名或密码错误"),
    FAILED_USER_BANNED                            (1106, "您已被禁言，请联系管理员，并重新登录"),
    FAILED_TWO_PWD_NOT_SAME                       (1107, "两次输入的密码不一致"),
    FAILED_USER_ARTICLE_COUNT_UPDATE              (1108, "更新用户帖子数量失败"),

    // 板块相关操作
    FAILED_BOARD_NOT_EXISTS                       (1200, "板块不存在"),
    FAILED_BOARD_BANNED                           (1201, "版块禁用"),
    FAILED_BOARD_ARTICLE_COUNT_UPDATE             (1202, "更新板块帖子数量失败"),

    // 帖子相关操作
    SUCCESS_NEW_ARTICLE_INSERT                    (1300, "发布新帖成功"),
    FAILED_NEW_ARTICLE_INSERT                     (1301, "发布新帖失败"),
    FAILED_NEW_ARTICLE_TITLE_OR_CONTENT_IS_NULL   (1302, "新帖标题或内容为空"),
    FAILED_ARTICLE_NOT_EXISTS                     (1303, "帖子不存在"),
    FAILED_ARTICLE_DETAIL_SELECT                  (1304, "查询帖子详情失败"),
    FAILED_ARTICLE_UPDATE                         (1305, "更新帖子失败"),
    FAILED_ARTICLE_BANNED                         (1306, "帖子状态异常"),

    // 错误
    ERROR_SERVICES                                (2000, "服务器内部错误"),
    ERROR_IS_NULL                                 (2001, "IS NULL.");

    // 状态码
    int code;

    // 状态描述
    String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "code: " + code + ", message: " + message + ". ";
    }
}
