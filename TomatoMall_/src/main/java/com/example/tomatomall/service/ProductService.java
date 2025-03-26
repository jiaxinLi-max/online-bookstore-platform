package com.example.tomatomall.service;


import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.StockpileVO;

import java.util.List;

public interface ProductService {
    //获取商品列表
    List<ProductVO>getAllProductInfo();
    //获取商品特定信息
    ProductVO getProductInfo(String id );
    //更新商品信息
    Boolean updateProductInfo(ProductVO productVO,String id);
    //添加商品
    Boolean addProduct(ProductVO productVO);
    //删除商品
    Boolean deleteProduct(String id);
    //更新商品库存
    Boolean updateStock(String productId, StockpileVO stockpileVO);
    //查询商品库存
    StockpileVO getStock(String productId);
}
