package com.me.forum.exception;

import com.me.forum.common.AppResult;

/**
 * @ExceptionName：ApplicationException
 * @Author: JacksonLee
 * @Date: 2026/9/5
 * @Description: 自定义的异常
 */
public class ApplicationException extends RuntimeException {
    // 自定义的错误信息
    protected AppResult errorResult;

    /**
     * @Title: ApplicationException
     * @Author: JacksonLee
     * @Date: 2026-09-05
     * @Params: [errorResult] 
     * @Return: null
     * @Description: 有自定义错误信息的构造方法
     */
    public ApplicationException(AppResult errorResult) {
        super(errorResult.getMessage());
        this.errorResult = errorResult;
    }
    
    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    /**
     * @Title: getErrorResult
     * @Author: JacksonLee
     * @Date: 2026-09-05
     * @Params: []
     * @Return: AppResult
     * @Description: Gettet方法
     */
    public AppResult getErrorResult() {
        return errorResult;
    }
}
