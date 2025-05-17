package com.example.tomatomall.po;

import com.example.tomatomall.vo.AccountVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Account {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "username")  //数据库  下划线
    private String username;

    @Basic
    @Column(name = "password")  //数据库  下划线
    private String password;

    @Basic
    @Column(name = "name")  //数据库  下划线
    private String name;

    @Basic
    @Column(name = "avatar")  //数据库  下划线
    private String avatar;

    @Basic
    @Column(name = "role")  //数据库  下划线
    private String role;

    @Basic
    @Column(name = "telephone")  //数据库  下划线
    private String telephone;

    @Basic
    @Column(name = "email")  //数据库  下划线
    private String email;

    @Basic
    @Column(name = "location")  //数据库  下划线
    private String location;

    //积分模块
    @Basic
    @Column(name="score")
    private BigDecimal score;

    @Basic
    @Column(name = "grade")
    private Integer grade;

    public AccountVO toVO(){
        AccountVO accountVO=new AccountVO();

        accountVO.setId(this.id);
        accountVO.setUsername(this.username);
        accountVO.setPassword(this.password);
        accountVO.setName(this.name);
        accountVO.setAvatar(this.avatar);
        accountVO.setRole(this.role);
        accountVO.setTelephone(this.telephone);
        accountVO.setEmail(this.email);
        accountVO.setLocation(this.location);
        accountVO.setScore(this.score);
        accountVO.setGrade(this.grade);
        return  accountVO;
    }
}
