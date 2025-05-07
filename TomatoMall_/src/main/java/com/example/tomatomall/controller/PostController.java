package com.example.tomatomall.controller;


import com.example.tomatomall.service.PostService;
import com.example.tomatomall.vo.PostVO;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    PostService postService;

    /**
     * 获取帖子列表
     */
    @GetMapping
    public Response<List<PostVO>>getAllPosts(){
        return Response.buildSuccess(postService.getAllPosts());
    }

    /**
     * 获取指定帖子信息
     */
    @GetMapping("/{postId}")
    public Response<PostVO>getPostInfo(@PathVariable Integer postId){
        return Response.buildSuccess(postService.getPostInfo(postId));
    }

    /**
     * 创建帖子
     */
    @PostMapping
    public Response<String> createPost(@RequestBody PostVO postVO){
        return Response.buildSuccess(postService.createPost(postVO));
    }

    /**
     * 删除帖子
     */
    @DeleteMapping
    public Response<String> deletePost(@RequestParam Integer postId){
        return Response.buildSuccess(postService.deletePost(postId));
    }

    /**
     * 点赞帖子
     */
    @PostMapping("/like")
    public Response<String> likePost(@RequestParam Integer postId){
        return Response.buildSuccess(postService.likePost(postId));

    }

    /**
     * 删除帖子
     */
    @PostMapping("/like")
    public Response<String> dislikePost(@RequestParam Integer postId){
        return Response.buildSuccess(postService.dislikePost(postId));

    }
}
