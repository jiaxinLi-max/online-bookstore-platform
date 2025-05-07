package com.example.tomatomall.vo;


import com.example.tomatomall.po.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostVO {
    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private String cover;
    private LocalDateTime time;
    private Integer like;
    private Integer dislike;

    public Post toPO(){
        Post post=new Post();
        post.setId(this.id);
        post.setUserId(this.userId);
        post.setTitle(this.title);
        post.setContent(this.content);
        post.setCover(this.cover);
        post.setTime(this.time);
        post.setLike(this.like);
        post.setDislike(this.dislike);
        return post;
    }
}
