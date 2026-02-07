package com.example.tomatomall.controller;

import com.example.tomatomall.service.AccountService;
import com.example.tomatomall.vo.AccountVO;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

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
     * 获取用户详情，通过id
     */
    @GetMapping
    public Response<AccountVO> getUserById(@RequestParam Integer id) {
        return Response.buildSuccess(accountService.getUserById(id));
    }

    /**
     * 创建新的用户
     */
    @PostMapping()
    public Response<String> createUser(@RequestBody AccountVO accountVO) {
        return Response.buildSuccess(accountService.register(accountVO));
    }

    /**
     * 更新用户信息
     */
    @PutMapping()
    public Response<Boolean> updateUser(@RequestBody AccountVO accountvo) {
        return Response.buildSuccess(accountService.updateInformation(accountvo));
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Response<String> login(@RequestBody AccountVO accountVO) {
        return Response.buildSuccess(accountService.login(accountVO.getUsername(), accountVO.getPassword()));
    }


    @PutMapping("/add")
    public Response<String> addScore(@RequestParam Integer userId, @RequestParam BigDecimal score){
       return Response.buildSuccess(accountService.addScore(userId,score));
    }

    @PutMapping("/update/{userId}")
    public Response<String> updateDegree(@PathVariable(value="userId")Integer userId){
        return Response.buildSuccess(accountService.updateDegree(userId));
    }
}
