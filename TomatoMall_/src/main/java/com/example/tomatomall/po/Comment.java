package com.example.tomatomall.po;


import com.example.tomatomall.vo.CommentVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Comment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "userId")  //数据库  下划线
    private Integer userId;

    @Basic
    @Column(name = "productId")  //数据库  下划线
    private Integer productId;

    @Basic
    @Column(name = "content")  //数据库  下划线
    private String content;

    @Basic
    @Column(name = "score")  //数据库  下划线
    private Float score;

    @Basic
    @Column(name = "likes")  //数据库  下划线
    private Integer likes;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Integer> likedUserIds = new HashSet<>();


    @Basic
    @Column(name = "time",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time;

    public CommentVO toVO(){
        CommentVO commentVO=new CommentVO();

        commentVO.setId(this.id);
        commentVO.setProductId(this.productId);
        commentVO.setUserId(this.userId);
        commentVO.setScore(this.score);
        commentVO.setContent(this.content);
        commentVO.setLikes(this.likes);
        commentVO.setTime(this.time);
        return  commentVO;
    }
}

