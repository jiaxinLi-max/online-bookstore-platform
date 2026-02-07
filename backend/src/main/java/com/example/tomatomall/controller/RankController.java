package com.example.tomatomall.controller;

import com.example.tomatomall.service.RankService;
import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rank")
public class RankController {
    @Autowired
    RankService rankService;
    /**
     * 获取排行榜信息
     */
    @GetMapping
    public Response<List<ProductVO>>getRankInfo(){
        return Response.buildSuccess(rankService.getRankInfo());
    }
}
