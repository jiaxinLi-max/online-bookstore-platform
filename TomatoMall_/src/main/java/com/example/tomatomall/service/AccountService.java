package com.example.tomatomall.service;

import com.example.tomatomall.po.Account;
import com.example.tomatomall.vo.AccountVO;

import java.math.BigDecimal;

public interface AccountService {
    String register(AccountVO accountVO);
    String login(String username,String password);
    AccountVO getInformation(String username);
    Boolean updateInformation(AccountVO accountVO);

    AccountVO getUserById(Integer id);

    //积分模块
    String addScore(Integer userId, BigDecimal score);
    String updateDegree(Integer userId);
    Account findById(Integer id);
}
