package com.example.tomatomall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// 声明这是一个 Spring Boot 应用的入口类
@EnableScheduling
// 启用 Spring 的定时任务功能
// 配合 @Scheduled 注解使用，使应用支持自动执行的任务（如订单超时处理）
public class TomatoMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(TomatoMallApplication.class, args);
    }

}
