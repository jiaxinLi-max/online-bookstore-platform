package com.example.tomatomall.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {
    @Autowired
    LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/accounts")
                .excludePathPatterns("/api/accounts/login")
                .excludePathPatterns("/api/images", "/api/images/**")  // 确保排除图片上传相关的路径
                .excludePathPatterns("/oss/**")  // 排除 OSS 相关路径（根据实际情况修改）
                .excludePathPatterns("/api/orders/notify")
                .excludePathPatterns("/api/products")
                .excludePathPatterns("/api/advertisements")
                .excludePathPatterns("/api/columns")
                .order(1);
    }
}
