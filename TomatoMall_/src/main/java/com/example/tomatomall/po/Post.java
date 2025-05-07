package com.example.tomatomall.po;


import com.example.tomatomall.vo.PostVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Post {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="id",nullable = false)
    private Integer id;

    @Basic
    @Column(name="user_id",nullable = false)
    private Integer userId;//关联用户id

    @Basic
    @Column(name="title",nullable = false)
    private String title;

    @Basic
    @Column(name = "content",nullable = false)
    private String content;

    @Basic
    @Column(name = "cover",nullable = false)
    private String cover;

    @Basic
    @Column(name = "time",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time;

    @Basic
    @Column(name = "like",nullable = false)
    private Integer like;

    @Basic
    @Column(name = "dislike",nullable = false)
    private Integer dislike;

    public PostVO toVO(){
        PostVO postVO=new PostVO();
        postVO.setId(this.id);
        postVO.setUserId(this.userId);
        postVO.setTitle(this.title);
        postVO.setContent(this.content);
        postVO.setCover(this.cover);
        postVO.setTime(this.time);
        postVO.setLike(this.like);
        postVO.setDislike(this.dislike);
        return postVO;
    }
}
