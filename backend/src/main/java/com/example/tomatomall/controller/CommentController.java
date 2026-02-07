package com.example.tomatomall.controller;


import com.example.tomatomall.service.CommentService;

import com.example.tomatomall.vo.CommentVO;

import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    /**
     * 获取指定书籍的全部评价
     * @param productId
     * @return
     */
    @GetMapping("/{productId}")
    public Response<List<CommentVO>> getAllCommentInfo(@PathVariable Integer productId){
        return Response.buildSuccess(commentService.getAllCommentInfo(productId));
    }

    /**
     * 获取特定评价信息
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public Response<CommentVO> getCommentInfo(@PathVariable Integer id){
        return Response.buildSuccess(commentService.getCommentInfo(id));
    }

    /**
     * 更新评价信息
     * @param CommentVO
     * @return
     */
    @PutMapping("/{id}")
    public Response<String> updateCommentInfo(@RequestBody CommentVO CommentVO){
        return Response.buildSuccess(commentService.updateCommentInfo(CommentVO));
    }

    /**
     * 写评价
     * @param CommentVO
     * @return
     */
    @PostMapping
    public Response<String> addComment(@RequestBody CommentVO CommentVO){
        return Response.buildSuccess(commentService.addComment(CommentVO));
    }

    /**
     * 删除评价
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Response<String> deleteComment(@PathVariable Integer id){
        return Response.buildSuccess(commentService.deleteComment(id));
    }

    /**
     * 点赞评价
     * @param id
     * @param userId
     * @return
     */
    @PostMapping("/like/{id}")
    public Response<String> likeComment(@PathVariable Integer id,@RequestParam Integer userId){
        return Response.buildSuccess((commentService.likeComment(id,userId)));
    }

    /**
     * 获取评论的回复
     * @param commentId
     * @return
     */
    @GetMapping("/reply")
    public Response<List<CommentVO>>getReplies(@RequestParam Integer commentId){
        return Response.buildSuccess(commentService.getReplies(commentId));
    }

}
