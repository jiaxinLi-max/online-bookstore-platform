package com.example.tomatomall.vo;

import com.example.tomatomall.po.Account;
import com.example.tomatomall.po.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

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
    private List<Address> addressBook;


    //积分模块
    private BigDecimal score;
    private Integer grade;

    public Account toPO(){
        Account account=new Account();
        account.setId(this.id);
        account.setUsername(this.username);
        account.setPassword(this.password);
        account.setName(this.name);
        account.setAvatar(this.avatar);
        account.setRole(this.role);
        account.setTelephone(this.telephone);
        account.setEmail(this.email);
        account.setLocation(this.location);
        account.setGrade(this.grade);
        account.setScore(this.score);
        account.setAddressBook(this.addressBook);
        return  account;
    }
}
