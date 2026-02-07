package com.example.tomatomall.controller;

import com.example.tomatomall.service.AdvertisementService;
import com.example.tomatomall.vo.AdvertisementVO;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementController {

    @Autowired
    AdvertisementService advertisementService;

    /**
     * 获取所有广告信息
     */
    @GetMapping
    public Response<List<AdvertisementVO>>getAllAdvertisements(){
        return Response.buildSuccess(advertisementService.getAllAdvertisements());
    }

    /**
     * 更新广告信息
     */
    @PutMapping
    public Response<String>updateAdvertisement(@RequestBody AdvertisementVO advertisementVO){
        return Response.buildSuccess(advertisementService.updateAdvertisement(advertisementVO));
    }

    /**
     * 创建广告
     */
    @PostMapping
    public Response<AdvertisementVO>createAdvertisement(@RequestBody AdvertisementVO advertisementVO){
        return Response.buildSuccess(advertisementService.createAdvertisement(advertisementVO));
    }

    /**
     * 删除广告
     */
    @DeleteMapping("/{id}")
    public Response<String>deleteAdvertisement(@PathVariable String id){
        return Response.buildSuccess(advertisementService.deleteAdvertisement(id));
    }
}
