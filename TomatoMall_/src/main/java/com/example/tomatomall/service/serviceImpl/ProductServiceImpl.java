package com.example.tomatomall.service.serviceImpl;


import com.example.tomatomall.TomatoMallApplication;
import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.po.Stockpile;
import com.example.tomatomall.repository.ProductRepository;
import com.example.tomatomall.repository.SpecificationRepository;
import com.example.tomatomall.repository.StockpileRepository;
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

    @Autowired
    StockpileRepository stockpileRepository;

    @Autowired
    SpecificationRepository specificationRepository;


    //获取商品列表
    @Override
    public List<ProductVO> getAllProductInfo(){
        return productRepository.findAll().stream().map(Product::toVO).collect(Collectors.toList());
    }

    //获取商品特定信息
    @Override
    public ProductVO getProductInfo(Integer id ){
        Product product=productRepository.findById(id).orElse(null);
        if(product==null){
            throw TomatoMallException.productNotExist();
        }
        return product.toVO();
    }

    //更新商品信息
    @Override
    public Boolean updateProductInfo(ProductVO productVO,Integer id){
        Product product=productRepository.findById(id).orElse(null);
        if(product==null){
            throw TomatoMallException.productNotExist();
        }
        product.setTitle(productVO.getTitle());
        product.setPrice(productVO.getPrice());
        product.setRate(productVO.getRate());
        product.setDescription(productVO.getDescription());
        product.setCover(productVO.getCover());
        product.setDetail(productVO.getDetail());
        product.setSpecifications(productVO.getSpecifications());

        productRepository.save(product);
        return true;

    }

    //添加商品
    @Override
    public Boolean addProduct(ProductVO productVO){
        Product product=productRepository.findByTitle(productVO.getTitle());
        if(product!=null){
            throw TomatoMallException.titleAlreadyExist();
        }
        Product newProduct=productVO.toPO();
        productRepository.save(newProduct);
        return true;
    }

    //删除商品
    @Override
    public Boolean deleteProduct(Integer id){
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            // 商品不存在，抛出异常
            throw TomatoMallException.productNotExist();
        }

        stockpileRepository.deleteByProductId(id);  // 根据 productId 删除库存信息

        specificationRepository.deleteByProductId(id);  // 根据 productId 删除规格信息

        // 删除商品
        productRepository.deleteById(id);
        return true;  // 返回操作成功
    }

    //更新商品库存
    @Override
    public Boolean updateStock(Integer productId, StockpileVO stockpileVO){
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            // 商品不存在，抛出异常
            throw TomatoMallException.productNotExist();
        }
        Stockpile stockpile = stockpileRepository.findByProductId(productId);
        if (stockpile == null) {
            stockpile = new Stockpile();
            stockpile.setProduct(product);
        }

        stockpile.setAmount(stockpileVO.getAmount()); // 更新库存数
        stockpile.setFrozen(stockpileVO.getFrozen()); // 更新冻结数

        stockpileRepository.save(stockpile);

        return true; // 返回操作成功
    }
    //查询商品库存
    @Override
    public StockpileVO getStock(Integer id){
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            // 商品不存在，抛出异常
            throw TomatoMallException.productNotExist();
        }
        Stockpile stockpile = stockpileRepository.findByProductId(id);
        if (stockpile == null) {
            return new StockpileVO(id,0,0);
        }
        return stockpile.toVO();
    }
}
