package com.example.tomatomall.service.serviceImpl;


import com.example.tomatomall.po.Product;
import com.example.tomatomall.repository.ProductRepository;
import com.example.tomatomall.service.ProductService;
import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.StockpileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;
    //获取商品列表
    @Override
    public List<ProductVO> getAllProductInfo(){
        return productRepository.findAll().stream().map(Product::toVO).collect(Collectors.toList());
    }
    //获取商品特定信息
    @Override
    public ProductVO getProductInfo(String id ){
        return null;
    }
    //更新商品信息
    @Override
    public Boolean updateProductInfo(ProductVO productVO,String id){
        return null;
    }
    //添加商品
    @Override
    public Boolean addProduct(ProductVO productVO){
        return null;
    }
    //删除商品
    @Override
    public Boolean deleteProduct(String id){
        return null;
    }

    //更新商品库存
    @Override
    public Boolean updateStock(String productId, StockpileVO stockpileVO){
        return null;
    }
    //查询商品库存
    @Override
    public StockpileVO getStock(String id){
        return null;
    }
}
