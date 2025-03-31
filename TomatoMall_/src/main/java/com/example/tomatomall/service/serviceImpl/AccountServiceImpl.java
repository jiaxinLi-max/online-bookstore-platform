package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Account;
import com.example.tomatomall.repository.AccountRepository;
import com.example.tomatomall.service.AccountService;
import com.example.tomatomall.util.SecurityUtil;
import com.example.tomatomall.util.TokenUtil;
import com.example.tomatomall.vo.AccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private PasswordEncoder passwordEncoder;  // 自动注入 BCryptPasswordEncoder 实现

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SecurityUtil securityUtil;

    @Override
    public Boolean register(AccountVO accountVO){
        Account account=accountRepository.findByUsername(accountVO.getUsername());
        if(account!=null){
            throw  TomatoMallException.usernameAlreadyExists();
        }
        String rawPassword = accountVO.getPassword();

        // 使用 BCryptPasswordEncoder 对密码进行加密
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 将加密后的密码设置到用户对象
        accountVO.setPassword(encodedPassword);
        Account newaccount=accountVO.toPO();
        accountRepository.save(newaccount);
        return true;
    }

    public String login(String username, String password) {
        // 查找用户
        Account account = accountRepository.findByUsername(username);

        if (account != null) {
            // 使用 passwordEncoder 比较原始密码和数据库中加密后的密码
            if (passwordEncoder.matches(password, account.getPassword())) {
                // 密码匹配，生成并返回 token
                return tokenUtil.getToken(account);
            }
        }

        // 密码不匹配或用户不存在，抛出自定义异常
        throw TomatoMallException.usernameOrPasswordError();
    }



    @Override
    public AccountVO getInformation(String username){
        Account account=accountRepository.findByUsername(username);
        if(account==null){
            throw TomatoMallException.userNotExist();
        }
        return  account.toVO();
    }

    @Override
    public Boolean updateInformation(AccountVO accountVO){
        Account account=securityUtil.getCurrentUser();
       if(accountVO.getUsername()!=null){
           Account account_temp=accountRepository.findByUsername(accountVO.getUsername());
           if(account_temp!=null){
               throw  TomatoMallException.usernameAlreadyExists();
           }
           account.setUsername(accountVO.getUsername());
       }
        if(accountVO.getPassword()!=null){
            String rawPassword = accountVO.getPassword();

            // 使用 BCryptPasswordEncoder 对密码进行加密
            String encodedPassword = passwordEncoder.encode(rawPassword);

            // 将加密后的密码设置到用户对象
            accountVO.setPassword(encodedPassword);

            account.setPassword(accountVO.getPassword());
        }
        if(accountVO.getName()!=null){
            account.setName(accountVO.getName());
        }
        if(accountVO.getAvatar()!=null){
            account.setAvatar(accountVO.getAvatar());
        }
        if(accountVO.getRole()!=null){
            account.setRole(accountVO.getRole());
        }
        if(accountVO.getTelephone()!=null){
            account.setTelephone(accountVO.getTelephone());
        }
        if(accountVO.getEmail()!=null){
            account.setEmail(accountVO.getEmail());
        }
        if(accountVO.getLocation()!=null){
            account.setLocation(accountVO.getLocation());
        }
        accountRepository.save(account);
        return  true;
    }

}
