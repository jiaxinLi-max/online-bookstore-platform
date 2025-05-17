package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.po.Comment;
import com.example.tomatomall.po.Post;
import com.example.tomatomall.repository.CommentRepository;
import com.example.tomatomall.repository.PostRepository;
import com.example.tomatomall.service.SortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SortServiceImpl implements SortService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Override
    public List<Object>sortByTime(String type, Integer productId){
        if(type.equals("post")){
            List<Post>postList=postRepository.findAllByOrderByTimeDesc();
            return postList.stream().map(Post::toVO).collect(Collectors.toList());

        }else if(type.equals("comment")){
            List<Comment> commentList;
            if(productId==null) {
                commentList = commentRepository.findAllByOrderByTimeDesc();

            }else {
                commentList=commentRepository.findAllByProductIdOrderByTimeDesc(productId);

            }
            return commentList.stream().map(Comment::toVO).collect(Collectors.toList());

        }
        return null;
    }

    @Override
    public List<Object>sortByLike(String type, Integer productId){
        if(type.equals("post")){
            List<Post>postList=postRepository.findAllByOrderByLikeDesc();
            return postList.stream().map(Post::toVO).collect(Collectors.toList());

        }else if(type.equals("comment")){
            List<Comment> commentList;
            if(productId==null) {
                commentList = commentRepository.findAllByOrderByLikesDesc();

            }else {
                commentList=commentRepository.findAllByProductIdOrderByLikesDesc(productId);

            }
            return commentList.stream().map(Comment::toVO).collect(Collectors.toList());

        }
        return null;
    }
}
