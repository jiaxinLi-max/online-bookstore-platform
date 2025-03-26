package com.example.tomatomall.service.serviceImpl;

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
            //TODO throw
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

    @Override
    public String login(String username,String password){
    {
            // 查找用户
             Account account= accountRepository.findByUsername(username);

            if (account != null) {
                // 使用 passwordEncoder 比较原始密码和数据库中加密后的密码
                passwordEncoder.matches(password,account.getPassword());
                return tokenUtil.getToken(account);
            }

            return "1";//TODO throw
        }
    }

    @Override
    public AccountVO getInformation(String username){
        Account account=accountRepository.findByUsername(username);
        if(account==null){
            //TODO THROW
        }
        return  account.toVO();
    }

    @Override
    public Boolean updateInformation(AccountVO accountVO){
        Account account=securityUtil.getCurrentUser();
       if(accountVO.getUsername()!=null){
           account.setUsername(accountVO.getUsername());
       }
        if(accountVO.getPassword()!=null){
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
