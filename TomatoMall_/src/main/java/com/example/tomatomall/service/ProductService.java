package com.example.tomatomall.service;


import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.Response;
import com.example.tomatomall.vo.StockpileVO;

import java.util.List;

public interface ProductService {
    //获取商品列表
    List<ProductVO>getAllProductInfo();
    //获取商品特定信息
    ProductVO getProductInfo(String id );
    //更新商品信息
    String updateProductInfo(ProductVO productVO);
    //添加商品
    ProductVO addProduct(ProductVO productVO);
    //删除商品
    String deleteProduct(String id);
    //更新商品库存
    String updateStock(String productId,StockpileVO stockpileVO);
    //查询商品库存
    StockpileVO getStock(String productId);
}
