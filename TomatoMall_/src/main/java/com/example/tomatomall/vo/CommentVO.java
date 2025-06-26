package com.example.tomatomall.vo;

import com.example.tomatomall.po.Comment;
import com.example.tomatomall.po.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class CommentVO {
    private  Integer id;
    private Integer userId;
    private Integer productId;
    private String content;
    private Float score;
    private Integer likes;
    private LocalDateTime time;
    //父评论id
    private Integer parentId;

    public Comment toPO(Comment parentComment){
        Comment comment=new Comment();
        comment.setId(this.id);
        comment.setProductId(this.productId);
        comment.setUserId(this.userId);
        comment.setLikes(this.likes);
        comment.setContent(this.content);
        comment.setScore(this.score);
        comment.setTime(this.time);
        comment.setParent(parentComment);
        return comment;
    }
}