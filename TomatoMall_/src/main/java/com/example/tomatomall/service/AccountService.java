package com.example.tomatomall.service;

import com.example.tomatomall.po.Account;
import com.example.tomatomall.vo.AccountVO;

public interface AccountService {
    Boolean register(AccountVO accountVO);
    String login(String username,String password);
    AccountVO getInformation(String username);
    Boolean updateInformation(AccountVO accountVO);
}
