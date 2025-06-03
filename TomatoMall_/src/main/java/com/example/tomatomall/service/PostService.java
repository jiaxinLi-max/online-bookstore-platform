package com.example.tomatomall.service;

import com.example.tomatomall.po.Post;
import com.example.tomatomall.vo.PostVO;


import java.util.List;



public interface PostService {
    List<PostVO> getAllPosts();

    PostVO getPostInfo(Integer postId);

    String createPost(PostVO postVO);

    String deletePost(Integer postId);

    String likePost(Integer postId,Integer userId);

    String dislikePost(Integer postId,Integer userId);

    List<Post>findAllByOrderByTimeDesc();
    List<Post>findAllByOrderByLikeDesc();
}
