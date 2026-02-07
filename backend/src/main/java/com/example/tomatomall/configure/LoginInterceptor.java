package com.example.tomatomall.configure;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * LoginInterceptor 是一个自定义的登录状态拦截器，
 * 用于在请求到达 Controller 之前，验证请求头中是否携带合法的 token，
 * 从而实现对接口访问权限的控制（即拦截未登录用户的非法请求）。
 *
 * 该拦截器会在 SpringMVC 的拦截链中执行，并通过 TokenUtil 解析 token 信息，
 * 若验证通过，则将当前用户信息存入 session 中，供后续业务逻辑使用；
 * 若验证失败，则抛出自定义异常，阻止请求继续执行。
 */

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    TokenUtil tokenUtil;

    /**
     * 在请求到达 Controller 之前执行的预处理方法。
     *
     * 功能：拦截所有需要登录权限的请求，通过检查 token 判断用户是否已登录。
     *
     * @param request  当前 HTTP 请求对象
     * @param response 当前 HTTP 响应对象
     * @param handler  请求处理器对象（通常是 Controller 中的方法）
     * @return 若返回 true，则请求继续执行；返回 false 或抛异常则中止请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");
        if (token != null && tokenUtil.verifyToken(token)) {
            request.getSession().setAttribute("currentUser",tokenUtil.getUser(token));
            return true;
        }else {
            throw TomatoMallException.notLogin();
        }
    }

}