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
     * 获取指定书籍的所有评价
     */
    @GetMapping("/{productId}")
    public Response<List<CommentVO>> getAllCommentInfo(@PathVariable Integer productId){
        return Response.buildSuccess(commentService.getAllCommentInfo(productId));
    }


    /**
     * 获取特定评价信息
     */
    @GetMapping("/detail/{id}")
    public Response<CommentVO> getCommentInfo(@PathVariable Integer id){
        return Response.buildSuccess(commentService.getCommentInfo(id));
    }

    /**
     * 更新评价信息
     */
    @PutMapping("/{id}")
    public Response<String> updateCommentInfo(@RequestBody CommentVO CommentVO){
        return Response.buildSuccess(commentService.updateCommentInfo(CommentVO));
    }

    /**
     * 写评价
     */
    @PostMapping
    public Response<String> addComment(@RequestBody CommentVO CommentVO){
        return Response.buildSuccess(commentService.addComment(CommentVO));
    }

    /**
     * 删除评价
     */
    @DeleteMapping("/{id}")
    public Response<String> deleteComment(@PathVariable Integer id){
        return Response.buildSuccess(commentService.deleteComment(id));
    }

//      点赞评价
    @PostMapping("/like/{id}")
    public Response<String> likeComment(@PathVariable Integer id){
        return Response.buildSuccess((commentService.likeComment(id)));
    }


}
