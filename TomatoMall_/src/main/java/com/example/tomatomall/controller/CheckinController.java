package com.example.tomatomall.controller;


import com.example.tomatomall.service.CheckinService;
import com.example.tomatomall.vo.CheckinVO;
import com.example.tomatomall.vo.CommentVO;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkin")
public class CheckinController {
    @Autowired
    CheckinService checkinService;

    @GetMapping("/{userId}")
    public Response<List<CheckinVO>> getCheckinInfo(@PathVariable Integer userId){
        return Response.buildSuccess(checkinService.getCheckinInfo(userId));
    }
    @PostMapping("/{userId}")
    public Response<String> setCheckin(@PathVariable Integer userId){
        return Response.buildSuccess(checkinService.setCheckin(userId));
    }
}
