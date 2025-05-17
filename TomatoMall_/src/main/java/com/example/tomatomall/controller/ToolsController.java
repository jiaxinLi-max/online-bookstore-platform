package com.example.tomatomall.controller;

import com.example.tomatomall.po.Comment;
import com.example.tomatomall.po.Post;
import com.example.tomatomall.service.ImageService;
import com.example.tomatomall.service.SortService;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ToolsController {
    @Autowired
    ImageService imageService;

    @Autowired
    SortService sortService;

    @PostMapping("/images")
    public Response<String> upload(@RequestParam("file") MultipartFile file){
        return Response.buildSuccess(imageService.upload(file));
    }

    @GetMapping("/sortByTime")
    public Response<List<Object>> sortByTime(@RequestParam String type,@RequestParam(required = false)Integer productId) {
        return Response.buildSuccess(sortService.sortByTime(type,productId));
    }

    @GetMapping("/sortByLike")
    public Response<List<Object>> sortByLike(@RequestParam String type,@RequestParam(required = false)Integer productId) {
        return Response.buildSuccess(sortService.sortByLike(type,productId));
    }

}