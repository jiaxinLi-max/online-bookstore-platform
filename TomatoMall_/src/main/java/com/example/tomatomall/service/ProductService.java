package com.example.tomatomall.service;


import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.StockpileVO;

import java.util.List;

public interface ProductService {
    //获取商品列表
    List<ProductVO>getAllProductInfo();
    //获取商品特定信息
    ProductVO getProductInfo(Integer id );
    //更新商品信息
    Boolean updateProductInfo(ProductVO productVO);
    //添加商品
    Boolean addProduct(ProductVO productVO);
    //删除商品
    Boolean deleteProduct(Integer id);
    //更新商品库存
    Boolean updateStock(Integer productId,Integer amount);
    //查询商品库存
    StockpileVO getStock(Integer productId);
}
