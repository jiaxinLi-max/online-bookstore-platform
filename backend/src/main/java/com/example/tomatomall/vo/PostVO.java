package com.example.tomatomall.vo;


import com.example.tomatomall.po.Post;
import com.example.tomatomall.po.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PostVO {
    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private List<String> covers;
    private LocalDateTime time;
    private Integer like;
    private Integer dislike;
    private List<Integer> productIds;

    public Post toPO(Set<Product> products){
        Post post=new Post();
        post.setId(this.id);
        post.setUserId(this.userId);
        post.setTitle(this.title);
        post.setContent(this.content);
        post.setCovers(this.covers);
        post.setTime(this.time);
        post.setLike(this.like);
        post.setDislike(this.dislike);
        post.setProducts(products);
        return post;
    }
}
