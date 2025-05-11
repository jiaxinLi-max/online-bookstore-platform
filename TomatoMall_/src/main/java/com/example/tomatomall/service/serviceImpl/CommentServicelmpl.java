package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Comment;


import com.example.tomatomall.repository.CommentRepository;
import com.example.tomatomall.service.CommentService;
import com.example.tomatomall.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class CommentServicelmpl implements CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Override
    public List<CommentVO> getAllCommentInfo(Integer productId){
        List<Comment> comments = commentRepository.findByProductId(productId);
        // 假设你有一个工具方法或构造器能把 Comment 转成 CommentVO
        return comments.stream().map(Comment::toVO).collect(Collectors.toList());
    }
    @Override
    public CommentVO getCommentInfo(Integer id){
        try {
            Comment comment = commentRepository.findById(id).orElse(null);
            if (comment == null) {
                throw TomatoMallException.commentNotExist();
            }
            return comment.toVO();
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format: " + id);  // 打印错误的 ID 格式
            throw TomatoMallException.invalidProductId();
        }
    }
    @Override
    public String updateCommentInfo(CommentVO commentVO){
        Comment comment = commentRepository.findById(commentVO.getId()).orElse(null);

        if (comment == null) {
            throw TomatoMallException.commentNotExist();
        }
        if(commentVO.getContent()!=null){
            comment.setContent(commentVO.getContent());
        }
        if(commentVO.getScore()!=null){
            comment.setScore(commentVO.getScore());
        }
        comment.setTime(new Date());




        commentRepository.save(comment);
        return "更新成功";
    }

    @Override
    public String addComment(CommentVO commentVO){
        Comment comment=commentVO.toPO();
        comment.setLikes(0);
        comment.setTime(new Date());
        commentRepository.save(comment);
        return "创建成功";
    }
    @Override
    public String deleteComment(Integer id){
        Comment comment=commentRepository.findById(id).orElse(null);

        if (comment == null) {
            throw TomatoMallException.commentNotExist();
        }


        commentRepository.deleteById(id);
        return "删除成功";
    }
    @Override
    public String likeComment(Integer id){
        Comment comment=commentRepository.findById(id).orElse(null);
        if(comment==null){
            throw TomatoMallException.commentNotExist();
        }
        comment.setLikes(comment.getLikes()+1);
        commentRepository.save(comment);
        return "点赞成功";
    }
}
