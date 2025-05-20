package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.po.Comment;
import com.example.tomatomall.po.Post;


import com.example.tomatomall.service.CommentService;
import com.example.tomatomall.service.PostService;
import com.example.tomatomall.service.SortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class SortServiceImpl implements SortService {
    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Override
    public List<Object>sortByTime(String type, Integer productId){
        if(type.equals("post")){
            List<Post>postList=postService.findAllByOrderByTimeDesc();
            return postList.stream().map(Post::toVO).collect(Collectors.toList());

        }else if(type.equals("comment")){
            List<Comment> commentList;
            if(productId==null) {
                commentList = commentService.findAllByOrderByTimeDesc();

            }else {
                commentList=commentService.findAllByProductIdOrderByTimeDesc(productId);

            }
            return commentList.stream().map(Comment::toVO).collect(Collectors.toList());

        }
        return null;
    }

    @Override
    public List<Object>sortByLike(String type, Integer productId){
        if(type.equals("post")){
            List<Post>postList=postService.findAllByOrderByLikeDesc();
            return postList.stream().map(Post::toVO).collect(Collectors.toList());

        }else if(type.equals("comment")){
            List<Comment> commentList;
            if(productId==null) {
                commentList = commentService.findAllByOrderByLikesDesc();

            }else {
                commentList=commentService.findAllByProductIdOrderByLikesDesc(productId);

            }
            return commentList.stream().map(Comment::toVO).collect(Collectors.toList());

        }
        return null;
    }
}
