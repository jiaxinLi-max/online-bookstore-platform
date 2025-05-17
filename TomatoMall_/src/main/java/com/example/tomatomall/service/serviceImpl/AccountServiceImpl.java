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

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private PasswordEncoder passwordEncoder;  // 自动注入 BCryptPasswordEncoder 实现

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    AccountRepository accountRepository;


    @Override
    public String register(AccountVO accountVO){
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
        return "注册成功";
    }

    @Override
    public String login(String username, String password) {
        // 查找用户
        Account account = accountRepository.findByUsername(username);

        if (account != null) {
            // 使用 passwordEncoder 比较原始密码和数据库中加密后的密码
            if (passwordEncoder.matches(password, account.getPassword())) {
                // 密码匹配，生成并返回 token
                return tokenUtil.getToken(account);
            }
            else {
                throw TomatoMallException.usernameOrPasswordError();
            }
        }

        // 密码不匹配或用户不存在，抛出自定义异常
        else {
            throw TomatoMallException.usernameOrPasswordError();
        }

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
        Account account= accountRepository.findByUsername(accountVO.getUsername());
//       if(accountVO.getUsername()!=null){
//           Account account_temp=accountRepository.findByUsername(accountVO.getUsername());
//           if(account_temp!=null&& !Objects.equals(account_temp.getId(), account.getId())){
//               throw  TomatoMallException.usernameAlreadyExists();
//           }
//           account.setUsername(accountVO.getUsername());
//       }
        if (accountVO.getPassword() != null && !passwordEncoder.matches(accountVO.getPassword(), account.getPassword())) {
            String rawPassword = accountVO.getPassword();
            String encodedPassword = passwordEncoder.encode(rawPassword);  // 使用 Spring Security 加密
            account.setPassword(encodedPassword);
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

    @Override
    public AccountVO getUserById(Integer id){
        Account account=accountRepository.findById(id).orElse(null);
        if(account==null){
            throw TomatoMallException.userNotExist();
        }
        return account.toVO();
    }

    //积分模块
    @Override
    public String addScore(Integer userId, BigDecimal score){
        Account account=accountRepository.findById(userId).orElse(null);
        if(account==null){
            throw TomatoMallException.userNotExist();
        }
        account.setScore(account.getScore().add(score));
        return "添加积分成功";
    }

    @Override
    public String updateDegree(Integer userId){
        Account account=accountRepository.findById(userId).orElse(null);
        if(account==null){
            throw TomatoMallException.userNotExist();
        }
        while(BigDecimal.valueOf(100.0).compareTo(account.getScore())<=0 && account.getDegree()<10){
            account.setScore(account.getScore().subtract(BigDecimal.valueOf(100.0)));
            account.setDegree(account.getDegree()+1);
        }
        return "更新等级成功";
    }

}
