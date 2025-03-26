package com.example.tomatomall.controller;


import com.example.tomatomall.service.ProductService;
import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    /**
     * 获取所有商品信息
     */
    @GetMapping
    public Response<List<ProductVO>> getAllProductInfo(){return null;}


    /**
     * 获取特定商品信息
     */
    @GetMapping("/id")
    public Response<ProductVO> getProductInfo(){return null;}

    /**
     * 更新商品信息
     */
    @PutMapping
    public Response<Boolean> updateProductInfo(){return null;}

    /**
     * 增加商品
     */
    @PostMapping
    public Response<Boolean> addProduct(){return null;}

    /**
     * 删除商品
     */
    @DeleteMapping("/id")
    public Response<Boolean> deleteProduct(){return null;}



}
