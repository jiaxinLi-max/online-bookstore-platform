package com.example.tomatomall.controller;


import com.example.tomatomall.service.ProductService;
import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.Response;
import com.example.tomatomall.vo.StockpileVO;
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
    public Response<List<ProductVO>> getAllProductInfo(){
        return Response.buildSuccess(productService.getAllProductInfo());
    }


    /**
     * 获取特定商品信息
     */
    @GetMapping("/{id}")
    public Response<ProductVO> getProductInfo(@PathVariable String id){
        return Response.buildSuccess(productService.getProductInfo(id));
    }

    /**
     * 更新商品信息
     */
    @PutMapping
    public Response<Boolean> updateProductInfo(@RequestBody ProductVO productVO,@PathVariable String id){
        return Response.buildSuccess(productService.updateProductInfo(productVO,id));
    }

    /**
     * 增加商品
     */
    @PostMapping
    public Response<Boolean> addProduct(@RequestBody ProductVO productVO){
        return Response.buildSuccess(productService.addProduct(productVO));
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/{id}")
    public Response<Boolean> deleteProduct(@PathVariable String id){
        return Response.buildSuccess(productService.deleteProduct(id));
    }

    /**
     * 更新商品库存
     * @param productId
     * @param stockpileVO
     * @return
     */
    @PatchMapping("/stockpile/{productId}")
    public Response<Boolean> updateStock(@PathVariable String productId, @RequestBody StockpileVO stockpileVO) {
        return Response.buildSuccess(productService.updateStock(productId, stockpileVO));
    }


    /**
     *查询商品库存
     * @param productId
     * @return
     */
    @GetMapping("/stockpile/{productId}")
    public Response<StockpileVO> getStock(@PathVariable String productId){
        return Response.buildSuccess(productService.getStock(productId));
    }


}
