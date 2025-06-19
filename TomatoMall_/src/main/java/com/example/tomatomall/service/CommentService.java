package com.example.tomatomall.service;

import com.example.tomatomall.po.Comment;
import com.example.tomatomall.vo.CommentVO;

import java.util.List;

public interface CommentService {
    List<CommentVO> getAllCommentInfo(Integer ProductId);
    CommentVO getCommentInfo(Integer id);
    String updateCommentInfo(CommentVO commentvo);
    String addComment(CommentVO commentVO);
    String deleteComment(Integer id);
    String likeComment(Integer id,Integer userId);
    List<Comment>findAllByOrderByTimeDesc();
    List<Comment>findAllByOrderByLikesDesc();
    List<Comment>findAllByProductIdOrderByTimeDesc(Integer productId);
    List<Comment>findAllByProductIdOrderByLikesDesc(Integer productId);


    List<CommentVO> getReplies(Integer commentId);
}
