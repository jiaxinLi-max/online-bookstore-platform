package com.example.tomatomall.po;


import com.example.tomatomall.vo.CommentVO;
import com.example.tomatomall.vo.PostVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "post_covers", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "cover_url")
    private List<String> covers = new ArrayList<>();

    @Basic
    @Column(name = "time",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time;

    @Basic
    @Column(name = "likes",nullable = false)
    private Integer like;

    @Basic
    @Column(name = "dislike",nullable = false)
    private Integer dislike;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Integer> likedUserIds = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Integer> dislikedUserIds = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "post_product", // 中间表名字
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")  // 维护外键在 comment 表里
    private Set<Comment> comments = new HashSet<>();



    public PostVO toVO(){
        PostVO postVO=new PostVO();
        postVO.setId(this.id);
        postVO.setUserId(this.userId);
        postVO.setTitle(this.title);
        postVO.setContent(this.content);
        postVO.setCovers(this.covers);
        postVO.setTime(this.time);
        postVO.setLike(this.like);
        postVO.setDislike(this.dislike);
        if (this.products != null) {
            List<Integer> productIds = this.products.stream()
                    .map(Product::getId)
                    .collect(Collectors.toList());
            postVO.setProductIds(productIds);
        }
        if (this.comments != null && !this.comments.isEmpty()) {
            postVO.setComments(
                    this.comments.stream()
                            .map(Comment::toVO)
                            .collect(Collectors.toList())
            );
        }

        return postVO;
    }
}
