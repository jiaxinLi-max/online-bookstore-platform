package com.example.tomatomall.controller;

import com.example.tomatomall.service.AccountService;
import com.example.tomatomall.vo.AccountVO;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    /**
     * 获取用户详情
     */
    @GetMapping("/{username}")
    public Response<AccountVO> getUser(@PathVariable(value="username")String username) {
        return Response.buildSuccess(accountService.getInformation(username));
    }

    /**
     * 创建新的用户
     */
    @PostMapping()
    public Response<Boolean> createUser(@RequestBody AccountVO accountVO) {
        return Response.buildSuccess(accountService.register(accountVO));
    }

    /**
     * 更新用户信息
     */
    @PutMapping()
    public Response<Boolean> updateUser(AccountVO accountvo) {
        return Response.buildSuccess(accountService.updateInformation(accountvo));
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Response<String> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        return Response.buildSuccess(accountService.login(username,password));
    }
}
