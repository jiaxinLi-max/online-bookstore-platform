package com.example.tomatomall.service;

import com.example.tomatomall.po.Account;

public interface AccountService {
    Boolean register(Account account);
    String login(String username,String password);
    String getInformation();
    Boolean updateInformation(Account account);
}
