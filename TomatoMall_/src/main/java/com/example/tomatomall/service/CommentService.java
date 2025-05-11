package com.example.tomatomall.service;

import com.example.tomatomall.vo.CommentVO;

import java.util.List;

public interface CommentService {
    List<CommentVO> getAllCommentInfo(Integer ProductId);
    CommentVO getCommentInfo(Integer id);
    String updateCommentInfo(CommentVO commentvo);
    String addComment(CommentVO commentVO);
    String deleteComment(Integer id);
    String likeComment(Integer id);

}
