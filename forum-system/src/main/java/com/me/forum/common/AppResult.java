package com.me.forum.common;

/**
 * @ClassName: AppResult
 * @Author: JacksonLee
 * @Date: 2026/9/5
 * @Description: 统一的返回结果
 */
public class AppResult<T> {
    // 状态码
    private int code;

    // 状态描述
    private String message;

    // 返回的数据
    private T data;

    /**
     * @Title: AppResult
     * @Author: JacksonLee
     * @Date: 2026-09-05
     * @Params: [code, message]
     * @Return: null
     * @Description: 两参数构造方法
     */
    public AppResult(int code, String message) {
        this(code, message, null);
    }

    /**
     * @Title: AppResult
     * @Author: JacksonLee
     * @Date: 2026-09-05
     * @Params: [code, message, data]
     * @Return: null
     * @Description: 全参数构造方法
     */
    public AppResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * @Title: success
     * @Author: JacksonLee
     * @Date: 2026-09-05
     * @Params: []
     * @Return: AppResult
     * @Description: 操作成功
     */
    public static AppResult success () {
        return new AppResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    /**
     * @Title: success
     * @Author: JacksonLee
     * @Date: 2026-09-05
     * @Params: [message]
     * @Return: AppResult
     * @Description: 成功：有描述
     */
    public static AppResult success (String message) {
        return new AppResult(ResultCode.SUCCESS.getCode(), message);
    }

    /**
     * @Title: success
     * @Author: JacksonLee
     * @Date: 2026-09-05
     * @Params: [data]
     * @Return: AppResult<T>
     * @Description: 成功：有数据
     */
    public static <T> AppResult<T> success (T data) {
        return new AppResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * @Title: success
     * @Author: JacksonLee
     * @Date: 2026-09-05
     * @Params: [message, data]
     * @Return: AppResult<T>
     * @Description: 成功：有描述和数据
     */
    public static <T> AppResult<T> success (String message, T data) {
        return new AppResult(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * @Title: failed
     * @Author: JacksonLee
     * @Date: 2026-09-05
     * @Params: []
     * @Return: AppResult
     * @Description: 操作失败
     */
    public static AppResult failed () {
        return new AppResult(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage());
    }

    /**
     * @Title: failed
     * @Author: JacksonLee
     * @Date: 2026-09-05
     * @Params: [message]
     * @Return: AppResult
     * @Description: 失败：有描述
     */
    public static AppResult failed (String message) {
        return new AppResult(ResultCode.FAILED.getCode(), message);
    }

    /**
     * @Title: failed
     * @Author: JacksonLee
     * @Date: 2026-09-05
     * @Params: [resultCode]
     * @Return: AppResult
     * @Description: 特定类型的失败操作
     */
    public static AppResult failed (ResultCode resultCode) {
        return new AppResult(resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * @Title: Getters and Setters
     * @Author: JacksonLee
     * @Date: 2026-09-05
     * @Params:
     * @Return:
     * @Description: Getter和Setter方法
     */
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
