package com.example.tomatomall.vo;

import com.example.tomatomall.po.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AccountVO {
    private  Integer id;
    private String username;
    private String password;
    private String name;
    private String avatar;
    private String role;
    private String telephone;
    private String email;
    private String location;
//    public Account toPO(){
//        Account account=new Account();
//        account.setId(this.id);
//    }
}
