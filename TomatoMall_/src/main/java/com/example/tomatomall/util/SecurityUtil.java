package com.example.tomatomall.util;

import com.example.tomatomall.po.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: DingXiaoyu
 * @Date: 0:28 2023/11/26
 * 你可以通过这个类的方法来获得当前用户的信息。
 */
@Component
public class SecurityUtil {

//    private static final ThreadLocal<Account> currentUser = new ThreadLocal<>();
//
//    public static void setCurrentUser(Account account) {
//        currentUser.set(account);
//        System.out.println("从 session 获取用户s: " + currentUser.get());
//    }
//
//    public static Account getCurrentUser() {
//        System.out.println("从 session 获取用户g: " + currentUser.get());
//        return currentUser.get();
//    }
//
//    public static void clear() {
//        currentUser.remove();
//    }
}
