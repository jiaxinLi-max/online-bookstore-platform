package com.example.tomatomall.service;

import com.example.tomatomall.po.Comment;
import com.example.tomatomall.vo.CommentVO;

import java.util.List;

public interface CommentService {
    //获取特定书籍的全部评价
    List<CommentVO> getAllCommentInfo(Integer ProductId);
    //获取特定评价信息
    CommentVO getCommentInfo(Integer id);
    //更新评价信息
    String updateCommentInfo(CommentVO commentvo);
    //写评价
    String addComment(CommentVO commentVO);
    //删除评价
    String deleteComment(Integer id);
    //点赞评价
    String likeComment(Integer id,Integer userId);
    //获取评论的回复
    List<CommentVO> getReplies(Integer commentId);

    //service提供的接口
    List<Comment>findAllByOrderByTimeDesc();
    List<Comment>findAllByOrderByLikesDesc();
    List<Comment>findAllByProductIdOrderByTimeDesc(Integer productId);
    List<Comment>findAllByProductIdOrderByLikesDesc(Integer productId);

}
