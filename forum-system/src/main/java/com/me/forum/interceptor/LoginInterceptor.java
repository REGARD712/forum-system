package com.me.forum.interceptor;

import com.me.forum.config.AppConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ClassName: LoginInterceptor
 * @Author: JacksonLee
 * @Date: 2026/9/10
 * @Description: 登录拦截器类
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Value("${me-forum.login.url}")
    private String defaultURL;  // 跳转的路径,具体在配置文件中(解耦合)

    /**
     * @return boolean true-通过;false-拦截
     * @description: 前置处理的拦截器
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求中获取session
        HttpSession session = request.getSession(false);
        // 非空校验
        if (session != null && session.getAttribute(AppConfig.USER_SESSION_KEY) != null) {
            // 用户处于已登录状态,校验通过
            return true;
        }
        // 校验URL
        if (!defaultURL.startsWith("/")) {
            defaultURL = "/" + defaultURL;
        }
        // 拦截,跳转到登录页面
        response.sendRedirect(defaultURL);
        return false;
    }
}
